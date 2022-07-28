package org.practice.haeng.apidemo.search.common.model;

import java.util.Date;

/**
 * 카프카 메시지 모델
 */
public class KeywordEvent {

    private String keyword; //정제된 키워드, 파티션 판별을 위한 키값으로 쓰인다.
    private String originKeyword;   //원본 키워드
    private Date updateDate;    //키워드 검색 요청 시점, 비동기처리기 떄문에 정확한 시점처리를 위한 전달

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOriginKeyword() {
        return originKeyword;
    }

    public void setOriginKeyword(String originKeyword) {
        this.originKeyword = originKeyword;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
