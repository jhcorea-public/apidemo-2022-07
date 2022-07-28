package org.practice.haeng.apidemo.search.common.keyword;

import org.practice.haeng.apidemo.search.webapp.api.LocalConvertedData;
import org.springframework.stereotype.Component;

/**
 * 확장포인트, 추후 외부 시스템을 통한 유사도 검증, 형태소 분석등등, 복잡한 로직을 수행할 가능성이 있다.
 * 지금은 단순한 replace 연산만 진행
 */
@Component
public class RefinedKeywordGenerator implements KeywordGenerator {

    @Override
    public String refineKeyword(String originKeyword) {
        return LocalConvertedData.replaceName(originKeyword);
    }
}
