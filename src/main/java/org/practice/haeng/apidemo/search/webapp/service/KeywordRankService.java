package org.practice.haeng.apidemo.search.webapp.service;

import java.util.List;

import org.practice.haeng.apidemo.search.common.model.KeywordCount;
import org.practice.haeng.apidemo.search.webapp.rank.dao.KeywordRankDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class KeywordRankService {

    private final KeywordRankDAO dao;

    public KeywordRankService(@Qualifier("keywordRankDAO") KeywordRankDAO dao) {
        this.dao = dao;
    }

    //db 부하 경감 및 속도를 위한 캐시
    @Cacheable(value = "rankCache", key =  "#top")
    public List<KeywordCount> selectTopKeyword(int top) {
        return dao.selectTopKeyword(top);
    }
}
