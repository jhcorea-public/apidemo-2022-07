package org.practice.haeng.apidemo.search.webapp.rank.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.practice.haeng.apidemo.search.common.model.KeywordCount;

public interface KeywordRankDAO {

    List<KeywordCount> selectTopKeyword(@Param("top") int top);
}
