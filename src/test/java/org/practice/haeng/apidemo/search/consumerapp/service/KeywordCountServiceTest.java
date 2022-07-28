package org.practice.haeng.apidemo.search.consumerapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.practice.haeng.apidemo.search.common.model.KeywordEvent;

import static org.junit.jupiter.api.Assertions.*;

class KeywordCountServiceTest {

    @Test
    void getUpdateDate() {

        List<KeywordEvent> keywordCountList = new ArrayList<>();
        KeywordEvent event1 = new KeywordEvent();
        event1.setUpdateDate(DateUtils.addDays(new Date(),1));
        keywordCountList.add(event1);
        KeywordEvent event2 = new KeywordEvent();
        event2.setUpdateDate(DateUtils.addDays(new Date(),3));
        keywordCountList.add(event2);

        System.out.println(event1.getUpdateDate());
        System.out.println(event2.getUpdateDate());

        Date date = KeywordCountService.getUpdateDate(keywordCountList);
        System.out.println(date);

        assertEquals(event2.getUpdateDate(), date);
    }
}