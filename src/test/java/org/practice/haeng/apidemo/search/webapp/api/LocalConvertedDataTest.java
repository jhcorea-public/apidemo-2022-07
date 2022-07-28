package org.practice.haeng.apidemo.search.webapp.api;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.practice.haeng.apidemo.search.webapp.service.SourceType;

import static org.junit.jupiter.api.Assertions.*;

class LocalConvertedDataTest {

    //TODO 두개 같은것 테스트

    @Test
    void replaceName() {

        String expected = "판교역";

        List<String> testList = Arrays.asList(
            "판 교    역     "
            , "    판교역 "
            , "        판교역 "
            , "<br>판교역</br>"
            , "판교역<br/>"
        );

        for (String item : testList) {
            Assertions.assertEquals(expected, LocalConvertedData.replaceName(item));
        }
    }

    @Test
    void keyEqualTest() {
        LocalConvertedData a = LocalConvertedData.create(SourceType.KAKAO, 4, " 장소 5", "000", "주소5  ", "  도로명5    ");
        LocalConvertedData b = LocalConvertedData.create(SourceType.NAVER, 2, " 장소 5", "000", "주소5  ", "   도로명5    ");

        assertEquals(a.getKey(), b.getKey());
    }

    @Test
    void equalTest() {
        LocalConvertedData a = LocalConvertedData.create(SourceType.KAKAO, 4, " 장소 5", "000", "주소5  ", "  도로명5    ");
        LocalConvertedData b = LocalConvertedData.create(SourceType.NAVER, 2, " 장소 5", "000", "주소5  ", "   도로명5    ");
        LocalConvertedData c = LocalConvertedData.create(SourceType.NAVER, 11, " 장소 5 <br>", "000", "주소5  ", "도로명5");

        assertTrue(a.equalData(b));
        assertTrue(b.equalData(a));

        assertTrue(b.equalData(c));
        assertTrue(c.equalData(b));

        assertTrue(a.equalData(c));
        assertTrue(c.equalData(a));
    }
}