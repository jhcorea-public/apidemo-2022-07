package org.practice.haeng.apidemo.search.webapp.api.kakao.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KaKaoApiResponse {

    @JsonProperty("documents")
    private List<Document> documents;
    @JsonProperty("meta")
    private Meta meta;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
