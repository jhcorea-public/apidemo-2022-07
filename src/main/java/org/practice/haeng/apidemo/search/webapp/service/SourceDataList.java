package org.practice.haeng.apidemo.search.webapp.service;

import java.util.List;

import org.practice.haeng.apidemo.search.webapp.api.ConvertedData;

public class SourceDataList<T extends ConvertedData> {

    private SourceType type;
    List<T> convertedDataList;

    public SourceDataList(SourceType type,
        List<T> convertedDataList) {
        this.type = type;
        this.convertedDataList = convertedDataList;
    }

    public SourceType getType() {
        return type;
    }

    public List<T> getList() {
        return convertedDataList;
    }
}
