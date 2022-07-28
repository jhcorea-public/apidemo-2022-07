package org.practice.haeng.apidemo.search.webapp.api.naver;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.practice.haeng.apidemo.search.webapp.api.ConvertedData;
import org.practice.haeng.apidemo.search.webapp.api.KeywordApiClient;
import org.practice.haeng.apidemo.search.webapp.api.LocalConvertedData;
import org.practice.haeng.apidemo.search.webapp.api.SourceApiException;
import org.practice.haeng.apidemo.search.webapp.api.naver.model.Item;
import org.practice.haeng.apidemo.search.webapp.api.naver.model.NaverApiResponse;
import org.practice.haeng.apidemo.search.webapp.service.SourceDataList;
import org.practice.haeng.apidemo.search.webapp.service.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

public class NaverClient implements KeywordApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        NaverClient.class);

    private RestOperations restOperations;
    private NaverApiInfo naverApiInfo;

    public NaverClient(RestOperations restOperations, NaverApiInfo naverApiInfo) {
        this.restOperations = restOperations;
        this.naverApiInfo = naverApiInfo;
    }

    @Override
    public SourceDataList<LocalConvertedData> search(String keyword) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", naverApiInfo.getClientId());
            headers.set("X-Naver-Client-Secret", naverApiInfo.getClientSecret());

            HttpEntity requestEntity = new HttpEntity<>(null, headers);

            ResponseEntity<NaverApiResponse> apiResponse = restOperations.exchange(uri(naverApiInfo.getUrl(), keyword, naverApiInfo.getPageSize()), HttpMethod.GET, requestEntity, NaverApiResponse.class);

            if (!apiResponse.getStatusCode().is2xxSuccessful() || apiResponse.getBody() == null) {
                throw new SourceApiException("[NAVER API FAIL]");
            }

            NaverApiResponse apiResult = apiResponse.getBody();
            List<ConvertedData> dataList = new ArrayList<>();
            for (int order = 0 ; order < apiResult.getItems().size(); order++) {
                dataList.add(this.convert(order, apiResult.getItems().get(order)));
            }
            return new SourceDataList(SourceType.NAVER, dataList);

        } catch (Exception e) {
            LOGGER.error("[NAVER API ERROR] keword :: {}",  keyword, e);
            throw new SourceApiException(e);
        }
    }

    public LocalConvertedData convert(int originOrder, Item origin) {
        return LocalConvertedData.create(SourceType.NAVER, originOrder, origin.getTitle(),  origin.getTelephone(), origin.getAddress(), origin.getLoadAddress());
    }


    private URI uri(String baseUrl, String keyword, int pageSize) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
            .queryParam("query", keyword)
            .queryParam("display", Integer.toString(pageSize))
            .encode(Charset.forName("UTF-8"))
            .build()
            .toUri();
    }
}
