package org.practice.haeng.apidemo.search.webapp.api;

import org.apache.commons.lang3.StringUtils;
import org.practice.haeng.apidemo.search.webapp.service.SourceType;

// 지역api 호출 모델
public class LocalConvertedData extends ConvertedData {

    public LocalConvertedData(SourceType sourceType, int order) {
        super(sourceType, order);
    }

    private String addressForKey;

    private String name;
    private String phone;
    private String address;
    private String roadAddress;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    //도로명 주소는 없는 경우가 있기 떄문에 지번주소로 진행
    public void setKey() {
        addressForKey = StringUtils.trim(address);
    }

    // compaction 작업을 위한 key
    @Override
    public String getKey() {
        return addressForKey;
    }

    @Override
    public boolean equalData(Object other) {

        if (!(other instanceof LocalConvertedData)) {
            return false;
        }

        LocalConvertedData otherData = (LocalConvertedData) other;

        if (!StringUtils.equals(this.getKey(), otherData.getKey())) {
            return false;
        }

        //코싸인 유사도 고려할만한 부분
        if (StringUtils.equals(replaceName(this.getName()), replaceName(otherData.getName()))) {
            return true;
        }
        return false;
    }

    public static String replaceName(String name) {
        return name.replaceAll("\\s+|\\t+|<br>+|</br>+|<br/>+", "");
    }

    public static LocalConvertedData create(SourceType sourceType, int order, String name, String phone, String address, String roadAddress) {
        LocalConvertedData result = new LocalConvertedData(sourceType, order);
        result.name = name;
        result.phone = phone;
        result.address = address;
        result.roadAddress = roadAddress;
        result.setKey();
        return result;
    }
}
