package org.practice.haeng.apidemo.search.webapp.api;

import org.practice.haeng.apidemo.search.webapp.collector.compaction.DataEqualChecker;
import org.practice.haeng.apidemo.search.webapp.collector.sort.SourceDataOrder;
import org.practice.haeng.apidemo.search.webapp.collector.sort.SourceDataType;
import org.practice.haeng.apidemo.search.webapp.service.SourceType;

public abstract class ConvertedData implements DataEqualChecker, SourceDataOrder, SourceDataType {

    private SourceType sourceType;
    private int order;

    public ConvertedData(SourceType sourceType, int order) {
        this.sourceType = sourceType;
        this.order = order;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public SourceType getSourceType() {
        return sourceType;
    }
}
