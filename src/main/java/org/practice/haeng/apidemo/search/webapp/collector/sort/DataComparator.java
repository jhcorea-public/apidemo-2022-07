package org.practice.haeng.apidemo.search.webapp.collector.sort;

import java.util.Comparator;

import org.practice.haeng.apidemo.search.webapp.collector.compaction.CompactionData;

public interface DataComparator {

    Comparator<CompactionData> COMPACTION_RATIO = Comparator.comparingInt(a -> -a.getRatio());
    Comparator<CompactionData> SOURCE_TYPE = Comparator.comparingInt(a -> a.getSourceType().getPriority());
    Comparator<CompactionData> SOURCE_ORDER = Comparator.comparingInt(a -> a.getOrder());

    Comparator<CompactionData> COMPACTION_DATA_COMPARATOR = COMPACTION_RATIO.thenComparing(SOURCE_TYPE).thenComparing(SOURCE_ORDER);

}
