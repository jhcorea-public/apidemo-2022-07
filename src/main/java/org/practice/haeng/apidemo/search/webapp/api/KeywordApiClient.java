package org.practice.haeng.apidemo.search.webapp.api;

import org.practice.haeng.apidemo.search.webapp.service.SourceDataList;

public interface KeywordApiClient {

    SourceDataList<LocalConvertedData> search(String keyword);
}
