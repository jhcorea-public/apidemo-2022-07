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
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 0, " ?????? 1", "000", "??????1  ", "  ?????????1    ")); //?????? ?????????
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 1, " ?????? 2", "000", "??????2  ", "  ?????????2    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 2, " ?????? 3", "000", "??????3  ", "  ?????????3    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 3, " ?????? 4", "000", "??????4  ", "  ?????????4    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 4, " ?????? 5", "000", "??????5  ", "  ?????????5    ")); //?????? ?????????
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 5, " ?????? 6", "000", "??????6  ", "  ?????????6    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 6, " ?????? 6", "000", "??????1 ??????  ", "  ?????????1 ??????   "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 7, " ?????? 7", "000", "??????1  ", "  ?????????1    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 8, " ?????? 7", "000", "??????1 ?????? ", "  ?????????1 ??????   "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 9, " ?????? 8", "000", "??????9  ", "  ?????????9    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 10, " ?????? 8", "000", "??????10  ", "  ?????????10    "));
        list1.add(LocalConvertedData.create(SourceType.KAKAO, 11, " ?????? 9", "000", "??????11  ", "  ?????????11    "));
        return list1;
    }

    private List<LocalConvertedData> testSet2ForSorting() {
        List<LocalConvertedData> list1 = new ArrayList<>();
        list1.add(LocalConvertedData.create(SourceType.NAVER, 0, " <br> ?????? 1 </br>", "000", "??????1  ", "  ?????????1    ")); //?????? ?????????
        list1.add(LocalConvertedData.create(SourceType.NAVER, 1, " ?????? ?????? 2", "000", "??????2  ", "  ?????????2    "));
        list1.add(LocalConvertedData.create(SourceType.NAVER, 2, " ?????? 5", "000", "??????5  ", "   ?????????5    ")); //?????? ?????????
        list1.add(LocalConvertedData.create(SourceType.NAVER, 3, " ?????? 6", "000", "??????1 ?????????", "  ?????????1 ?????????   "));
        list1.add(LocalConvertedData.create(SourceType.NAVER, 4, " ?????? 10", "000", "??????1  ", "  ?????????1    "));
        list1.add(LocalConvertedData.create(SourceType.NAVER, 5, " ?????? 11", "000", "??????1  ", "  ?????????1    "));
        list1.add(LocalConvertedData.create(SourceType.NAVER, 6, " ?????? 12", "000", "??????111  ", "  ?????????1 ??????   "));
        return list1;
    }

    //???????????? ?????? ????????? ???????????? ??????????????? ????????? develop ??????.
}