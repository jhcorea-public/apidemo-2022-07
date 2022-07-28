package org.practice.haeng.apidemo.search.webapp.service;

public enum SourceType {
    KAKAO(0)
    , NAVER(1)
    , UNKNOWN(100)
    ;

    private int priority;

    SourceType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
