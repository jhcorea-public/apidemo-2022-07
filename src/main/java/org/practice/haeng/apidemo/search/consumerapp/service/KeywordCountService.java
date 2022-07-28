package org.practice.haeng.apidemo.search.consumerapp.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.practice.haeng.apidemo.search.common.model.KeywordCount;
import org.practice.haeng.apidemo.search.common.model.KeywordEvent;
import org.practice.haeng.apidemo.search.consumerapp.dao.KeywordCountDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KeywordCountService {

    private final KeywordCountDAO dao;

    public KeywordCountService(@Qualifier("keywordCountDAO") KeywordCountDAO dao) {
        this.dao = dao;
    }

    public void increaseKeywordCount(String keyword, long increase, Date updateDate) {
        KeywordCount keywordCount = dao.selectKeyword(keyword);
        if (keywordCount == null) {
            dao.insertKeyword(keyword, increase, updateDate);
        } else {
            dao.increaseKeywordCount(keyword, increase, updateDate);
        }
    }

    @Transactional
    public void increaseKeywordCountBulk(List<KeywordEvent> keywordCountList) {
        Map<String, List<KeywordEvent>> keywordCountMap  = keywordCountList.stream().collect(Collectors.groupingBy(KeywordEvent::getKeyword));
        for (Map.Entry<String, List<KeywordEvent>> entry : keywordCountMap.entrySet()) {
            String keyword = entry.getKey();
            List<KeywordEvent> bulkEvent = entry.getValue();
            increaseKeywordCount(keyword, bulkEvent.size(), getUpdateDate(bulkEvent));
        }
    }

    public static Date getUpdateDate(List<KeywordEvent> keywordCountList) {
        return keywordCountList.stream().map(KeywordEvent::getUpdateDate).sorted(
            (a, b) -> {
                if (a.equals(b)) {
                    return 0;
                } else if (a.before(b)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        ).findFirst().get();
    }
}
