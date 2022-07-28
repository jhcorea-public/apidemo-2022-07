package org.practice.haeng.apidemo.search.webapp.rank.dao;

import java.util.List;

import org.practice.haeng.apidemo.search.common.model.KeywordCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("keywordRankDAO")
public class KeywordRankDAOImpl implements KeywordRankDAO {

    @Autowired
    private KeywordRankMapper mapper;

    @Override
    public List<KeywordCount> selectTopKeyword(int top) {
        return mapper.selectTopKeyword(top);
    }
}
