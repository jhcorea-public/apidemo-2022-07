package org.practice.haeng.apidemo.search.webapp.api.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegionInfo {

    @JsonProperty("region")
    private String[] region;
    @JsonProperty("keyword")
    private String keyword;
    @JsonProperty("selected_region")
    private String selectedRegion;

    public String[] getRegion() {
        return region;
    }

    public void setRegion(String[] region) {
        this.region = region;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSelectedRegion() {
        return selectedRegion;
    }

    public void setSelectedRegion(String selectedRegion) {
        this.selectedRegion = selectedRegion;
    }
}
