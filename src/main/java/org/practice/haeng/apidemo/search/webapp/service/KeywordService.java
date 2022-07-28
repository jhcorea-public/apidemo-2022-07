package org.practice.haeng.apidemo.search.webapp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.practice.haeng.apidemo.search.webapp.api.KeywordApiClient;
import org.practice.haeng.apidemo.search.webapp.api.LocalConvertedData;
import org.practice.haeng.apidemo.search.webapp.collector.LocalDataCollector;
import org.practice.haeng.apidemo.search.webapp.collector.sort.DataComparator;
import org.practice.haeng.apidemo.search.webapp.rank.KeywordRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * TODO 병렬 쓰레드 처리에 대한 타임아웃, 팬딩 등에 대한 처리를 확인한다...
 * //TODO 이쪽로직해보자..
 *
 * 남은일, 키워드, 컨트롤러, 구조 다잡히면 테스트해서 구현 확인
 */
@Service
public class KeywordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordService.class);

    private final List<KeywordApiClient> keywordApiClients;
    private final LocalDataCollector dataCollector;
    private final KeywordRecorder keywordRecorder;

    private final int MAX_DATA_COUNT_PER_SOURCE = 5;
    private final int DEFAULT_PAGE_SIZE = 10;

    ExecutorService executor = Executors.newCachedThreadPool();

    public KeywordService(List<KeywordApiClient> keywordApiClients,
        LocalDataCollector dataCollector,
        KeywordRecorder keywordRecorder) {
        this.keywordApiClients = keywordApiClients;
        this.dataCollector = dataCollector;
        this.keywordRecorder = keywordRecorder;
    }

    public List<KeywordApiResponse> search(String keyword) {
        return search(keyword, DEFAULT_PAGE_SIZE);
    }

    public List<KeywordApiResponse> search(String keyword, int pageSize) {

        if (StringUtils.isBlank(keyword)) {
            return Collections.EMPTY_LIST;
        }

        List<SourceDataList<LocalConvertedData>> apiResult = callApiParallel(keyword);
        apiResult = adjustApiResult(apiResult);

        List<LocalConvertedData> collectResult = dataCollector.collect(apiResult, pageSize,
            DataComparator.COMPACTION_DATA_COMPARATOR);

        List<KeywordApiResponse> result = collectResult.stream().map(item -> convert(item)).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(result)) {
            keywordRecorder.recordKeyword(keyword);
        }
        return result;

    }

    protected List<SourceDataList<LocalConvertedData>> callApiParallel(String keyword) {

        List<CompletableFuture<SourceDataList<LocalConvertedData>>> apiFutureList = new ArrayList<>();
        for (KeywordApiClient apiClient : keywordApiClients) {
            apiFutureList.add(CompletableFuture.supplyAsync(() -> apiClient.search(keyword), executor));
        }

        CompletableFuture allFuture = CompletableFuture.allOf(apiFutureList.toArray(new CompletableFuture[apiFutureList.size()]));

        List<SourceDataList<LocalConvertedData>> apiResult = new ArrayList<>();

        try {
            allFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.debug("[some api fail]");
        }

        try {
            for (CompletableFuture<SourceDataList<LocalConvertedData>> future : apiFutureList) {
                if (!future.isCompletedExceptionally()) {
                    apiResult.add(future.get());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("merge api result error",e);
        }
        return apiResult;
    }

    protected List<SourceDataList<LocalConvertedData>> adjustApiResult(List<SourceDataList<LocalConvertedData>> originResult) {

        List<SourceDataList<LocalConvertedData>> adjustResult = new ArrayList<>();

        List<SourceDataList<LocalConvertedData>> spareResult = new ArrayList<>();

        long insufficient = 0;
        for (SourceDataList<LocalConvertedData> item : originResult) {
            if (item.getList().size() <= MAX_DATA_COUNT_PER_SOURCE) {
                insufficient += MAX_DATA_COUNT_PER_SOURCE - item.getList().size();
                adjustResult.add(item);
            } else {
                spareResult.add(item);
            }
        }


        for (SourceDataList<LocalConvertedData> spareItem : spareResult) {
            //부족분이 있으면
            if (insufficient > 0) {
                //부족분이 훨씬 큰경우, 통째로 사용한다.
                if (insufficient > spareItem.getList().size() - MAX_DATA_COUNT_PER_SOURCE) {
                    adjustResult.add(spareItem);
                    insufficient -= spareItem.getList().size() - MAX_DATA_COUNT_PER_SOURCE;
                    //부족분을 충분히 커버할 수 있으면 잘라서 쓰고, 해당 로직을 종료한다.
                } else {
                    adjustResult.add(new SourceDataList<>(spareItem.getType(), spareItem.getList()
                        .stream()
                        .limit(MAX_DATA_COUNT_PER_SOURCE + insufficient)
                        .collect(Collectors.toList())));
                    insufficient = 0;
                }
            //부족분이 없거나, 해소되었을 경우
            } else {
                adjustResult.add(new SourceDataList<>(spareItem.getType(), spareItem.getList()
                    .stream()
                    .limit(MAX_DATA_COUNT_PER_SOURCE)
                    .collect(Collectors.toList())));
            }
        }

        return adjustResult;
    }

    protected KeywordApiResponse convert(LocalConvertedData localConvertedData) {
        KeywordApiResponse response = new KeywordApiResponse();
        response.setName(localConvertedData.getName());
        response.setAddress(localConvertedData.getAddress());
        return response;
    }
}
