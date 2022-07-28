package org.practice.haeng.apidemo.search.webapp.api.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Meta {

    @JsonProperty("total_count")
    private Integer totalCount;
    @JsonProperty("pageable_count")
    private Integer pageableCount;
    @JsonProperty("is_end")
    private Boolean isEnd;
    @JsonProperty("same_name")
    private RegionInfo sameName;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageableCount() {
        return pageableCount;
    }

    public void setPageableCount(Integer pageableCount) {
        this.pageableCount = pageableCount;
    }

    public Boolean getEnd() {
        return isEnd;
    }

    public void setEnd(Boolean end) {
        isEnd = end;
    }

    public RegionInfo getSameName() {
        return sameName;
    }

    public void setSameName(RegionInfo sameName) {
        this.sameName = sameName;
    }
}
