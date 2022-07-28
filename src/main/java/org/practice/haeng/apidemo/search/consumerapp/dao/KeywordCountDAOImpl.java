package org.practice.haeng.apidemo.search.consumerapp.dao;

import java.util.Date;

import org.practice.haeng.apidemo.search.common.model.KeywordCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("keywordCountDAO")
public class KeywordCountDAOImpl implements KeywordCountDAO {

    @Autowired
    private KeywordCountMapper mapper;

    @Override
    public KeywordCount selectKeyword(String keyword) {
        return mapper.selectKeyword(keyword);
    }

    @Override
    public int insertKeyword(String keyword, long initCount, Date updateDate) {
        return mapper.insertKeyword(keyword, initCount, updateDate);
    }

    @Override
    public int increaseKeywordCount(String keyword, long increase, Date updateDate) {
        return mapper.increaseKeywordCount(keyword, increase, updateDate);
    }
}
