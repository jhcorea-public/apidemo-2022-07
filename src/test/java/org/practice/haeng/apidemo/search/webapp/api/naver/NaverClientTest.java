package org.practice.haeng.apidemo.search.webapp.api.naver;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.practice.haeng.apidemo.search.webapp.api.LocalConvertedData;
import org.practice.haeng.apidemo.search.webapp.service.SourceDataList;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

class NaverClientTest {

    static RestOperations restOperations;

    @BeforeAll
    public static void setup() {
        System.out.println("init test");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        restOperations = restTemplate;

    }

    @Test
    void search() {

        NaverApiInfo naverApiInfo = new NaverApiInfo();
        naverApiInfo.setUrl("https://openapi.naver.com/v1/search/local.json");
        naverApiInfo.setClientId("wZW4lz_kPr_kArqWtw3v");
        naverApiInfo.setClientSecret("SSueMoPjiy");
        naverApiInfo.setPageSize(5);

        NaverClient naverClient = new NaverClient(restOperations, naverApiInfo);

        SourceDataList<LocalConvertedData> data = naverClient.search("판교역");
        for (LocalConvertedData convertedData : data.getList()){
            System.out.println(convertedData.getName());
        }
        Assertions.assertEquals(5, data.getList().size());
    }
}