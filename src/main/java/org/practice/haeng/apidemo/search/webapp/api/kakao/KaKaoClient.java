package org.practice.haeng.apidemo.search.webapp.api.kakao;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.practice.haeng.apidemo.search.webapp.api.KeywordApiClient;
import org.practice.haeng.apidemo.search.webapp.api.LocalConvertedData;
import org.practice.haeng.apidemo.search.webapp.api.SourceApiException;
import org.practice.haeng.apidemo.search.webapp.api.kakao.model.Document;
import org.practice.haeng.apidemo.search.webapp.api.kakao.model.KaKaoApiResponse;
import org.practice.haeng.apidemo.search.webapp.service.SourceDataList;
import org.practice.haeng.apidemo.search.webapp.service.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

public class KaKaoClient implements KeywordApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(KaKaoClient.class);

    private RestOperations restOperations;
    private KaKaoApiInfo kaKaoApiInfo;

    public KaKaoClient(RestOperations restOperations, KaKaoApiInfo kaKaoApiInfo) {
        this.restOperations = restOperations;
        this.kaKaoApiInfo = kaKaoApiInfo;
    }

    @Override
    public SourceDataList<LocalConvertedData> search(String keyword) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kaKaoApiInfo.getAppKey());

            HttpEntity requestEntity = new HttpEntity<>(null, headers);


            ResponseEntity<KaKaoApiResponse> apiResponse = restOperations.exchange(uri(kaKaoApiInfo.getUrl(), keyword, kaKaoApiInfo.getPageSize()), HttpMethod.GET, requestEntity,  KaKaoApiResponse.class);

            if (!apiResponse.getStatusCode().is2xxSuccessful() || apiResponse.getBody() == null) {
                throw new SourceApiException("[KAKAO API FAIL]");
            }

            KaKaoApiResponse apiResult = apiResponse.getBody();
            if (CollectionUtils.isEmpty(apiResult.getDocuments())) {
                return new SourceDataList(SourceType.KAKAO, Collections.EMPTY_LIST);
            }

            List<LocalConvertedData> dataList = new ArrayList<>();
            for (int order = 0 ; order < apiResult.getDocuments().size(); order++) {
                dataList.add(this.convert(order, apiResult.getDocuments().get(order)));
            }
            return new SourceDataList(SourceType.KAKAO, dataList);

        } catch (Exception e) {
            LOGGER.error("[KAKAO API ERROR] keword :: {}",  keyword, e);
            throw new SourceApiException(e);
        }
    }

    private URI uri(String baseUrl, String keyword, int size) throws Exception {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
            .queryParam("query", keyword)
            .queryParam("size", Integer.toString(size))
            .encode(Charset.forName("UTF-8"))
            .build()
            .toUri();
    }

    public LocalConvertedData convert(int originOrder, Document origin) {
        return LocalConvertedData.create(SourceType.KAKAO, originOrder, origin.getPlaceName(), origin.getPhone(), origin.getAddressName(), origin.getRoadAddressName());
    }
}
