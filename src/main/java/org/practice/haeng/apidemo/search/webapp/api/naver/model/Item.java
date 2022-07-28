package org.practice.haeng.apidemo.search.webapp.api.naver.model;

public class Item {

    private String title;
    private String link;
    private String category;
    private String telephone;
    private String address;
    private String loadAddress;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLoadAddress() {
        return loadAddress;
    }

    public void setLoadAddress(String loadAddress) {
        this.loadAddress = loadAddress;
    }
}
