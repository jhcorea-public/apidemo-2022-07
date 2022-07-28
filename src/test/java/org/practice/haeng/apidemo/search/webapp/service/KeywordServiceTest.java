package org.practice.haeng.apidemo.search.webapp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.practice.haeng.apidemo.search.webapp.api.KeywordApiClient;
import org.practice.haeng.apidemo.search.webapp.api.LocalConvertedData;
import org.practice.haeng.apidemo.search.webapp.collector.LocalDataCollector;
import org.practice.haeng.apidemo.search.webapp.rank.KeywordRecorder;

class KeywordServiceTest {


    @Test
    void callApiParallel() {

        List<KeywordApiClient> apiClientList = new ArrayList<>();
        apiClientList.add(keyword -> {
            SourceDataList apiResult = new SourceDataList(SourceType.KAKAO, Collections.EMPTY_LIST);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            return apiResult;
        });
        apiClientList.add(keyword -> {
            SourceDataList apiResult = new SourceDataList(SourceType. NAVER, Collections.EMPTY_LIST);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            return apiResult;
        });
        KeywordService keywordService = new KeywordService(apiClientList, null, null);

        long start = System.currentTimeMillis();
        List<SourceDataList<LocalConvertedData>> sourceDataLists = keywordService.callApiParallel("test");
        long end = System.currentTimeMillis();
        Assertions.assertTrue(end - start < 2000);
        System.out.println("result time :: " + (end - start));
        Assertions.assertEquals(2, sourceDataLists.size());
    }

    @Test
    void callApiParallel_fail() {

        List<KeywordApiClient> apiClientList = new ArrayList<>();
        apiClientList.add(keyword -> {
            System.out.println("run api1 " + System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            return new SourceDataList(SourceType.KAKAO, testSet1ForSorting());
        });
        apiClientList.add(keyword -> {
            System.out.println("run api2 " + System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            throw new RuntimeException("test exception");
        });

        KeywordService keywordService = new KeywordService(apiClientList, null, null);

        List<SourceDataList<LocalConvertedData>> sourceDataLists = keywordService.callApiParallel("test");
        Assertions.assertEquals(1, sourceDataLists.size());
    }


    @Test
    void search() {

        List<KeywordApiClient> apiClientList = new ArrayList<>();
        apiClientList.add(keyword -> new SourceDataList(SourceType.KAKAO, testSet1ForSorting()));
        apiClientList.add(keyword -> new SourceDataList(SourceType. NAVER, testSet2ForSorting()));


        KeywordService keywordService = new KeywordService(apiClientList, new LocalDataCollector(), new KeywordRecorder(null, null) {
            @Override
            public void recordKeyword(String keyword) {
                //do-nothing
            }
        });

        List<KeywordApiResponse> result = keywordService.search("temp", 20);
        for (KeywordApiResponse response : result) {
            System.out.println(response);
        }

    }

    private List<LocalConvertedData> testSet1ForSorting() {
        List<LocalConvertedData> list1 = new ArrayList<>();
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 0, " 장소 1", "000", "주소1  ", "  도로명1    ")); //중복 데이터
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 1, " 장소 2", "000", "주소2  ", "  도로명2    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 2, " 장소 3", "000", "주소3  ", "  도로명3    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 3, " 장소 4", "000", "주소4  ", "  도로명4    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 4, " 장소 5", "000", "주소5  ", "  도로명5    ")); //중복 데이터
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 5, " 장소 6", "000", "주소6  ", "  도로명6    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 6, " 장소 6", "000", "주소1 다름  ", "  도로명1 다름   "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 7, " 장소 7", "000", "주소1  ", "  도로명1    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 8, " 장소 7", "000", "주소1 다름 ", "  도로명1 다름   "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 9, " 장소 8", "000", "주소9  ", "  도로명9    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 10, " 장소 8", "000", "주소10  ", "  도로명10    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 11, " 장소 9", "000", "주소11  ", "  도로명11    "));
        return list1;
    }

    private List<LocalConvertedData> testSet2ForSorting() {
        List<LocalConvertedData> list1 = new ArrayList<>();
        list1.add(LocalConvertedData.create(SourceType.NAVER, 0, " <br> 장소 1 </br>", "000", "주소1  ", "  도로명1    ")); //중복 데이터
        list1.add(LocalConvertedData.create(SourceType.NAVER, 1, " 다른 장소 2", "000", "주소2  ", "  도로명2    "));
        list1.add(LocalConvertedData.create(SourceType.NAVER, 2, " 장소 5", "000", "주소5  ", "   도로명5    ")); //중복 데이터
        list1.add(LocalConvertedData.create(SourceType.NAVER, 3, " 장소 6", "000", "주소1 또다름", "  도로명1 또다름   "));
        list1.add(LocalConvertedData.create(SourceType.NAVER, 4, " 장소 10", "000", "주소1  ", "  도로명1    "));
        list1.add(LocalConvertedData.create(SourceType.NAVER, 5, " 장소 11", "000", "주소1  ", "  도로명1    "));
        list1.add(LocalConvertedData.create(SourceType.NAVER, 6, " 장소 12", "000", "주소111  ", "  도로명1 다름   "));
        return list1;
    }

    //병렬처리 관련 다양한 테스트를 진행하면서 코드를 develop 한다.
}