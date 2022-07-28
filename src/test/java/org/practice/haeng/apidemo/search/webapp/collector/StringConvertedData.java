package org.practice.haeng.apidemo.search.webapp.collector;

import org.apache.commons.lang3.StringUtils;
import org.practice.haeng.apidemo.search.webapp.api.ConvertedData;
import org.practice.haeng.apidemo.search.webapp.service.SourceType;

public class StringConvertedData extends ConvertedData {

    private String value;
    private String value2;

    public StringConvertedData(String value, String value2) {
        super(SourceType.UNKNOWN, 0);
        this.value = value;
        this.value2 = value2;
    }

    public StringConvertedData(SourceType sourceType, int order, String value, String value2) {
        super(sourceType, order);
        this.value = value;
        this.value2 = value2;
    }


    public String getValue() {
        return value;
    }

    public String getValue2() {
        return value2;
    }

    @Override
    public String getKey() {
        return value2;
    }

    @Override
    public boolean equalData(Object other) {
        if (!(other instanceof StringConvertedData)) {
            return false;
        }
        StringConvertedData data = (StringConvertedData) other;
        return StringUtils.equals(value, data.getValue()) && StringUtils.equals(value2, data.getValue2());
    }
}
