package org.practice.haeng.apidemo.search.webapp.api.kakao;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.practice.haeng.apidemo.search.webapp.api.LocalConvertedData;
import org.practice.haeng.apidemo.search.webapp.service.SourceDataList;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

class KaKaoClientTest {

    static RestOperations restOperations;

    @BeforeAll
    public static void setup() {
        System.out.println("init test");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        restOperations = restTemplate;

    }


    @Test
    public void search() {

        KaKaoApiInfo kaKaoApiInfo = new KaKaoApiInfo();
        kaKaoApiInfo.setUrl("http://dapi.kakao.com/v2/local/search/keyword.json");
        kaKaoApiInfo.setAppKey("cc415ba0ee96416efb08242eb4eeb2b4");
        kaKaoApiInfo.setPageSize(10);

        KaKaoClient kakaoClient = new KaKaoClient(restOperations, kaKaoApiInfo);

        SourceDataList<LocalConvertedData> data = kakaoClient.search("판교역");
        for (LocalConvertedData convertedData : data.getList()){
            System.out.println(convertedData.getName());
        }
        Assertions.assertEquals(10, data.getList().size());
    }


}