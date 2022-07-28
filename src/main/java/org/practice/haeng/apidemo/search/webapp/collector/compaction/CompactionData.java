package org.practice.haeng.apidemo.search.webapp.collector.compaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.practice.haeng.apidemo.search.webapp.api.ConvertedData;
import org.practice.haeng.apidemo.search.webapp.collector.sort.SourceDataOrder;
import org.practice.haeng.apidemo.search.webapp.collector.sort.SourceDataType;
import org.practice.haeng.apidemo.search.webapp.service.SourceType;
import org.springframework.util.Assert;

public class CompactionData<T extends ConvertedData> implements SourceDataType, SourceDataOrder, Compaction {

    private T priorityData;
    private int compactionRatio;

    private List<T> sourceDataList;

    public CompactionData(T convertedData) {
        Assert.notNull(convertedData, "need value");
        sourceDataList = new ArrayList<>(Arrays.asList(convertedData));
        priorityData = convertedData;
        compactionRatio = 1;
    }

    //같은 데이터일 경우, 하나로 통합한다.
    public boolean compaction(T item) {
        if(!priorityData.equalData(item)) {
            return false;
        }
        compactionRatio++;
        if (item.getSourceType().getPriority() < priorityData.getSourceType().getPriority()) {
            priorityData = item;
        }
        sourceDataList.add(item);
        return true;
    }

    public T getPriorityData() {
        return priorityData;
    }

    @Override
    public int getRatio() {
        return compactionRatio;
    }

    @Override
    public int getOrder() {
        return priorityData.getOrder();
    }

    @Override
    public SourceType getSourceType() {
        return priorityData.getSourceType();
    }
}
