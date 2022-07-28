package org.practice.haeng.apidemo.search.webapp.collector.compaction;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.practice.haeng.apidemo.search.webapp.api.ConvertedData;
import org.practice.haeng.apidemo.search.webapp.service.SourceType;

import static org.junit.jupiter.api.Assertions.*;

class CompactionDataTest {


    @Test
    void compaction() {

        CompactionData data = new CompactionData(new ConvertedData(SourceType.NAVER, 0) {
            @Override
            public String getKey() {
                return "abcd";
            }

            @Override
            public boolean equalData(Object other) {
                return StringUtils.equals(((ConvertedData)other).getKey(), this.getKey());
            }
        });
        assertEquals(SourceType.NAVER, data.getSourceType());


        ConvertedData newPriorityData = new ConvertedData(SourceType.KAKAO, 8) {
            @Override
            public String getKey() {
                return "abcd";
            }

            @Override
            public boolean equalData(Object other) {
                return StringUtils.equals(((ConvertedData)other).getKey(), this.getKey());
            }
        };

        assertTrue(data.compaction(newPriorityData));
        assertEquals(SourceType.KAKAO, data.getSourceType());
        assertEquals(8, data.getOrder());
        assertEquals(2, data.getRatio());
        assertEquals(newPriorityData, data.getPriorityData());

        ConvertedData newPriorityData2 = new ConvertedData(SourceType.NAVER, 8) {
            @Override
            public String getKey() {
                return "abcd4";
            }

            @Override
            public boolean equalData(Object other) {
                return StringUtils.equals(((ConvertedData)other).getKey(), this.getKey());
            }
        };

        assertFalse(data.compaction(newPriorityData2));

        ConvertedData newPriorityData3 = new ConvertedData(SourceType.NAVER, 8) {
            @Override
            public String getKey() {
                return "abcd";
            }

            @Override
            public boolean equalData(Object other) {
                return StringUtils.equals(((ConvertedData)other).getKey(), this.getKey());
            }
        };
        assertTrue(data.compaction(newPriorityData3));
        assertEquals(3, data.getRatio());
        assertNotEquals(newPriorityData3, data.getPriorityData());

    }
}