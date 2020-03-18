package com.zhixin.wolf.entity;

/**
 * Created by wolf on 2019/10/19.
 * 从Webservice接口得到的当前嫌疑人信息数据
 */
public class WSPersonLocationEntity {

    private Integer serialId;
    private String PersonName;
    private Integer currentRoomId;
    private Integer personSex;
    private String cuffNo;
    private String inTimeStr;
    private String caseType;

    public Integer getSerialId() {
        return serialId;
    }

    public void setSerialId(Integer serialId) {
        this.serialId = serialId;
    }

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    public Integer getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(Integer currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public Integer getPersonSex() {
        return personSex;
    }

    public void setPersonSex(Integer personSex) {
        this.personSex = personSex;
    }

    public String getCuffNo() {
        return cuffNo;
    }

    public void setCuffNo(String cuffNo) {
        this.cuffNo = cuffNo;
    }

    public String getInTimeStr() {
        return inTimeStr;
    }

    public void setInTimeStr(String inTimeStr) {
        this.inTimeStr = inTimeStr;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }
}
