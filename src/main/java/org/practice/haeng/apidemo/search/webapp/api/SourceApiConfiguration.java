package org.practice.haeng.apidemo.search.webapp.api;

import java.nio.charset.Charset;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.practice.haeng.apidemo.search.webapp.api.kakao.KaKaoApiInfo;
import org.practice.haeng.apidemo.search.webapp.api.kakao.KaKaoClient;
import org.practice.haeng.apidemo.search.webapp.api.naver.NaverApiInfo;
import org.practice.haeng.apidemo.search.webapp.api.naver.NaverClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SourceApiConfiguration {

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);                    //최대 오픈되는 커넥션 수
        connectionManager.setDefaultMaxPerRoute(5);            //IP, 포트 1쌍에 대해 수행할 커넥션 수
        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("dapi.kakao.com")), 100);
        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("openapi.naver.com")), 100);

        return connectionManager;
    }

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
            .setConnectionRequestTimeout(3000)        //연결요청시간초과, ms
            .setConnectTimeout(3000)                //연결시간초과, ms
            .setSocketTimeout(3000)                    //소켓시간초과, ms
            .build();
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
        return HttpClientBuilder
            .create()
            .setConnectionManager(poolingHttpClientConnectionManager)
            .setDefaultRequestConfig(requestConfig)
            .build();
    }

    @Bean
    public RestTemplate restTemplate(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        RestTemplate restTemplate =  new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;

    }

    @Bean
    public KaKaoApiInfo kaKaoApiInfo(@Value("${kakao.appkey}") String appKey) {
        KaKaoApiInfo kaKaoApiInfo = new KaKaoApiInfo();
        kaKaoApiInfo.setUrl("http://dapi.kakao.com/v2/local/search/keyword.json");
        kaKaoApiInfo.setAppKey(appKey);
        kaKaoApiInfo.setPageSize(10);
        return kaKaoApiInfo;
    }

    @Bean
    public NaverApiInfo naverApiInfo(@Value("${naver.client}") String client, @Value("${naver.secret}") String secret) {
        NaverApiInfo naverApiInfo = new NaverApiInfo();
        naverApiInfo.setUrl("https://openapi.naver.com/v1/search/local.json");
        naverApiInfo.setClientId(client);
        naverApiInfo.setClientSecret(secret);
        naverApiInfo.setPageSize(5);

        return naverApiInfo;
    }

    @Bean
    public KaKaoClient kaKaoClient(RestOperations restOperations, KaKaoApiInfo kaKaoApiInfo) {
        return new KaKaoClient(restOperations, kaKaoApiInfo);
    }

    @Bean
    public NaverClient naverClient(RestOperations restOperations, NaverApiInfo naverApiInfo) {
        return new NaverClient(restOperations, naverApiInfo);
    }

}
