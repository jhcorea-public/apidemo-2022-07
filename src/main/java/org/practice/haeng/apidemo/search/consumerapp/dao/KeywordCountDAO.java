package org.practice.haeng.apidemo.search.consumerapp.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.practice.haeng.apidemo.search.common.model.KeywordCount;

public interface KeywordCountDAO {

    KeywordCount selectKeyword(@Param("keyword") String keyword);

    int insertKeyword(@Param("keyword") String keyword, @Param("initCount") long initCount, @Param("updateDate") Date updateDate);

    int increaseKeywordCount(@Param("keyword") String keyword, @Param("increase") long increase, @Param("updateDate") Date updateDate);
}
