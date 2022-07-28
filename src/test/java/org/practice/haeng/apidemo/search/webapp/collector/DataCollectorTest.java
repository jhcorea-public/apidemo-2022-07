package org.practice.haeng.apidemo.search.webapp.collector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.practice.haeng.apidemo.search.webapp.collector.compaction.CompactionData;
import org.practice.haeng.apidemo.search.webapp.collector.sort.DataComparator;
import org.practice.haeng.apidemo.search.webapp.service.SourceDataList;
import org.practice.haeng.apidemo.search.webapp.service.SourceType;

import static org.junit.jupiter.api.Assertions.*;

class DataCollectorTest {

    @Test
    void compactionTwo_insert_into_empty() {

        DataCollector<StringConvertedData> dataCollector = new DataCollector<>() {};

        Map<String, List<CompactionData<StringConvertedData>>> base = new HashMap<>();
        dataCollector.compactionTwo(base, testSet1());

        assertEquals(3, base.get("keydata1").size());
        assertEquals(1, base.get("keydata1").get(1).getRatio());
        assertEquals(1, base.get("keydata3").size());
        assertEquals(3, base.get("keydata4").size());
    }

    @Test
    void compactionTwo_insert_into_not_empty() {
        DataCollector<StringConvertedData> dataCollector = new DataCollector<>() {};

        Map<String, List<CompactionData<StringConvertedData>>> base = new HashMap<>();

        dataCollector.compactionTwo(base, testSet1());

        dataCollector.compactionTwo(base, testSet2());

        assertEquals(6, base.entrySet().size());
        assertEquals(1, base.get("keydata6").size());
        assertEquals(2, base.get("keydata1").get(0).getRatio());

    }

    @Test
    void compaction() {

        DataCollector<StringConvertedData> dataCollector = new DataCollector<>() {};

        List<SourceDataList<StringConvertedData>> compactionInput = new ArrayList<>();
        compactionInput.add(new SourceDataList(SourceType.UNKNOWN, testSet1()));
        compactionInput.add(new SourceDataList(SourceType.UNKNOWN, testSet2()));

        List<CompactionData<StringConvertedData>> result = dataCollector.compaction(compactionInput);

        assertEquals(11, result.size());

    }

    @Test
    void collect_only_source_order() {

        DataCollector<StringConvertedData> dataCollector = new DataCollector<>() {};

        List<SourceDataList<StringConvertedData>> apiInput = new ArrayList<>();
        apiInput.add(new SourceDataList(SourceType.KAKAO, testSet1ForSorting()));
        apiInput.add(new SourceDataList(SourceType.NAVER, testSet2ForSorting()));

        List<StringConvertedData> list = dataCollector.collect(apiInput, 5, DataComparator.SOURCE_ORDER);
        int prev = 0;
        for (StringConvertedData item : list) {
            System.out.println(item.getValue() + "-" + item.getValue2() + "-" + item.getOrder());
            assertTrue(item.getOrder() - prev >= 0);
            prev = item.getOrder();
        }

    }

    @Test
    void collect_only_source_type_and_order() {

        DataCollector<StringConvertedData> dataCollector = new DataCollector<>() {};

        List<SourceDataList<StringConvertedData>> apiInput = new ArrayList<>();
        apiInput.add(new SourceDataList(SourceType.KAKAO, testSet1ForSorting()));
        apiInput.add(new SourceDataList(SourceType.NAVER, testSet2ForSorting()));

        List<StringConvertedData> list = dataCollector.collect(apiInput, 10, DataComparator.SOURCE_TYPE.thenComparing(DataComparator.SOURCE_ORDER));
        int prev = 0;
        SourceType prevType = null;
        int kakaoCount = 0;
        int naverCount = 0;
        for (StringConvertedData item : list) {
            System.out.println(item.getValue() + "-" + item.getValue2() + "-" + item.getSourceType() + "-" + item.getOrder());

            if (prevType != null && prevType == item.getSourceType()) {
                assertTrue(item.getOrder() - prev >= 0);
            }
            prevType = item.getSourceType();
            prev = item.getOrder();


            if (SourceType.KAKAO == item.getSourceType()) {
                kakaoCount++;
            } else {
                naverCount++;
            }
        }

        assertEquals(8, kakaoCount);
        assertEquals(2, naverCount);

    }

    @Test
    void collect() {

        DataCollector<StringConvertedData> dataCollector = new DataCollector<>() {};

        List<SourceDataList<StringConvertedData>> apiInput = new ArrayList<>();
        apiInput.add(new SourceDataList(SourceType.KAKAO, testSet1ForSorting()));
        apiInput.add(new SourceDataList(SourceType.NAVER, testSet2ForSorting()));

        List<StringConvertedData> list = dataCollector.collect(apiInput, 10, DataComparator.COMPACTION_DATA_COMPARATOR);
        for (StringConvertedData item : list) {
            System.out.println(item.getValue() + "-" + item.getValue2() + "-" + item.getSourceType() + "-" + item.getOrder());
        }

        assertEquals(SourceType.KAKAO, list.get(0).getSourceType());
        assertEquals(0, list.get(0).getOrder());
        assertEquals(SourceType.KAKAO, list.get(1).getSourceType());
        assertEquals(5, list.get(1).getOrder());
    }

    private List<StringConvertedData> testSet1() {
        List<StringConvertedData> list1 = new ArrayList<>();
        list1.add(new StringConvertedData( "soruceForIdentify", "keydata1"));
        list1.add(new StringConvertedData( "soruceForIdentify2", "keydata1"));
        list1.add(new StringConvertedData( "soruceForIdentify2", "keydata2"));
        list1.add(new StringConvertedData( "soruceForIdentify2", "keydata3"));
        list1.add(new StringConvertedData( "soruceForIdentify2", "keydata4"));
        list1.add(new StringConvertedData( "soruceForIdentify4", "keydata4"));
        list1.add(new StringConvertedData( "soruceForIdentify1", "keydata1"));
        list1.add(new StringConvertedData( "soruceForIdentify3", "keydata4"));
        return list1;
    }

    private List<StringConvertedData> testSet2() {
        List<StringConvertedData> list2 = new ArrayList<>();
        list2.add(new StringConvertedData( "soruceForIdentify", "keydata1"));
        list2.add(new StringConvertedData( "soruceForIdentify2", "keydata5"));
        list2.add(new StringConvertedData( "soruceForIdentify3", "keydata5"));
        list2.add(new StringConvertedData( "soruceForIdentify3", "keydata4"));
        list2.add(new StringConvertedData( "soruceForIdentify2", "keydata6"));
        return list2;
    }

    private List<StringConvertedData> testSet1ForSorting() {
        List<StringConvertedData> list1 = new ArrayList<>();
        list1.add(new StringConvertedData(SourceType.KAKAO, 5, "soruceForIdentify", "keydata1")); //중복 데이터
        list1.add(new StringConvertedData(SourceType.KAKAO, 7, "soruceForIdentify2", "keydata1"));
        list1.add(new StringConvertedData(SourceType.KAKAO, 4, "soruceForIdentify2", "keydata2"));
        list1.add(new StringConvertedData(SourceType.KAKAO, 6, "soruceForIdentify2", "keydata3"));
        list1.add(new StringConvertedData(SourceType.KAKAO, 2, "soruceForIdentify2", "keydata4"));
        list1.add(new StringConvertedData(SourceType.KAKAO, 3,"soruceForIdentify4", "keydata4"));
        list1.add(new StringConvertedData(SourceType.KAKAO, 1, "soruceForIdentify1", "keydata1"));
        list1.add(new StringConvertedData(SourceType.KAKAO, 0, "soruceForIdentify3", "keydata4")); //중복 데이터
        return list1;
    }

    private List<StringConvertedData> testSet2ForSorting() {
        List<StringConvertedData> list2 = new ArrayList<>();
        list2.add(new StringConvertedData(SourceType.NAVER, 3, "soruceForIdentify", "keydata1"));//중복 데이터
        list2.add(new StringConvertedData(SourceType.NAVER, 0, "soruceForIdentify2", "keydata5"));
        list2.add(new StringConvertedData(SourceType.NAVER, 1, "soruceForIdentify3", "keydata5"));
        list2.add(new StringConvertedData(SourceType.NAVER, 4, "soruceForIdentify3", "keydata4")); //중복 데이터
        list2.add(new StringConvertedData(SourceType.NAVER, 2, "soruceForIdentify2", "keydata6"));
        return list2;
    }


}