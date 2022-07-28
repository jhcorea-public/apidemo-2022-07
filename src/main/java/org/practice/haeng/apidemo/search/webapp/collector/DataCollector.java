package org.practice.haeng.apidemo.search.webapp.collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.practice.haeng.apidemo.search.webapp.api.ConvertedData;
import org.practice.haeng.apidemo.search.webapp.collector.compaction.CompactionData;
import org.practice.haeng.apidemo.search.webapp.service.SourceDataList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public abstract class DataCollector<T extends ConvertedData> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCollector.class);

    public List<T> collect(List<SourceDataList<T>> sourceDatumLists, int pageSize, Comparator<CompactionData> comparator) {
        List<CompactionData<T>> compactionDataList = compaction(sourceDatumLists);
        return compactionDataList.stream().sorted(comparator).limit(pageSize).map(CompactionData::getPriorityData).collect(Collectors.toList());
    }

    protected List<CompactionData<T>> compaction(List<SourceDataList<T>> apiResult) {

        if (CollectionUtils.isEmpty(apiResult)) {
            return Collections.EMPTY_LIST;
        } else if (apiResult.size() == 1) {
            return apiResult.get(0).getList().stream().map(CompactionData::new).collect(Collectors.toList());
        }

        List<T> baseList = apiResult.get(0).getList();

        Map<String, List<CompactionData<T>>> baseMap = new HashMap<>();
        for (T item : baseList) {
            if (baseMap.containsKey(item.getKey())) {
                baseMap.get(item.getKey()).add(new CompactionData<>(item));
            } else {
                List<CompactionData<T>> newKeyList = new ArrayList<>();
                newKeyList.add(new CompactionData<>(item));
                baseMap.put(item.getKey(), new ArrayList<>(Arrays.asList(new CompactionData<>(item))));
            }
        }

        apiResult.stream().skip(1).forEach(otherSourceDataList -> {
            compactionTwo(baseMap, otherSourceDataList.getList());
        });

        return baseMap.entrySet().stream().map(Map.Entry::getValue).flatMap(List::stream).collect(Collectors.toList());
    }

    protected void compactionTwo(Map<String, List<CompactionData<T>>> base, List<T> other) {

        if (CollectionUtils.isEmpty(other)) {
            return;
        }

        for (T item : other) {
            String itemKey = item.getKey();
            if (base.containsKey(itemKey)) {
                List<CompactionData<T>> existCompactionList = base.get(itemKey);
                boolean compactionResult = false;
                for (CompactionData<T> existData : existCompactionList) {
                    compactionResult = existData.compaction(item);
                    if (compactionResult) {
                        break;
                    }
                }
                if (!compactionResult) {
                    existCompactionList.add(new CompactionData<>(item));
                }
            } else {
                List<CompactionData<T>> newKeyData = new ArrayList<>();
                newKeyData.add(new CompactionData<>(item));
                base.put(itemKey, newKeyData);
            }
        }

    }
}
