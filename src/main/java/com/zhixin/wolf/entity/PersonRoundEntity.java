package com.zhixin.wolf.entity;

import java.util.List;

/**
 * Created by wolf on 2019/10/23.
 */
public class PersonRoundEntity {

    private String personName;
    private String cuffNo;
    private Integer personSex;
    private List<RoundInfoEntity> rounds;

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

    public List<RoundInfoEntity> getRounds() {
        return rounds;
    }

    public void setRounds(List<RoundInfoEntity> rounds) {
        this.rounds = rounds;
    }
}
