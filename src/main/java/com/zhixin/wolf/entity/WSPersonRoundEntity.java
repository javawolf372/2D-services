package com.zhixin.wolf.entity;

import java.util.List;

/**
 * Created by wolf on 2019/10/23.
 */
public class WSPersonRoundEntity {

    private String personName;
    private String cuffNo;
    private Integer personSex;
    private List<WSRoundInfoEntity> wsries;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCuffNo() {
        return cuffNo;
    }

    public void setCuffNo(String cuffNo) {
        this.cuffNo = cuffNo;
    }

    public Integer getPersonSex() {
        return personSex;
    }

    public void setPersonSex(Integer personSex) {
        this.personSex = personSex;
    }

    public List<WSRoundInfoEntity> getWsries() {
        return wsries;
    }

    public void setWsries(List<WSRoundInfoEntity> wsries) {
        this.wsries = wsries;
    }
}
