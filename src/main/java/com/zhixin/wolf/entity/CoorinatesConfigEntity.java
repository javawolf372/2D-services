package com.zhixin.wolf.entity;

/**
 * Created by wolf on 2019/10/19.
 */
public class CoorinatesConfigEntity {

    private Integer roomId;
    private String roomName;
    private Integer leftTopX;
    private Integer leftTopY;
    private Integer leftDownX;
    private Integer leftDownY;
    private Integer rightTopX;
    private Integer rightTopY;
    private Integer rightDownX;
    private Integer rightDownY;
    private Integer centerX;
    private Integer centerY;
    private Integer mainNumR;//小人横向最大数
    private Integer mainNumC;//小人纵向最大数

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getLeftTopX() {
        return leftTopX;
    }

    public void setLeftTopX(Integer leftTopX) {
        this.leftTopX = leftTopX;
    }

    public Integer getLeftTopY() {
        return leftTopY;
    }

    public void setLeftTopY(Integer leftTopY) {
        this.leftTopY = leftTopY;
    }

    public Integer getLeftDownX() {
        return leftDownX;
    }

    public void setLeftDownX(Integer leftDownX) {
        this.leftDownX = leftDownX;
    }

    public Integer getLeftDownY() {
        return leftDownY;
    }

    public void setLeftDownY(Integer leftDownY) {
        this.leftDownY = leftDownY;
    }

    public Integer getRightTopX() {
        return rightTopX;
    }

    public void setRightTopX(Integer rightTopX) {
        this.rightTopX = rightTopX;
    }

    public Integer getRightTopY() {
        return rightTopY;
    }

    public void setRightTopY(Integer rightTopY) {
        this.rightTopY = rightTopY;
    }

    public Integer getRightDownX() {
        return rightDownX;
    }

    public void setRightDownX(Integer rightDownX) {
        this.rightDownX = rightDownX;
    }

    public Integer getRightDownY() {
        return rightDownY;
    }

    public void setRightDownY(Integer rightDownY) {
        this.rightDownY = rightDownY;
    }

    public Integer getCenterX() {
        return centerX;
    }

    public void setCenterX(Integer centerX) {
        this.centerX = centerX;
    }

    public Integer getCenterY() {
        return centerY;
    }

    public void setCenterY(Integer centerY) {
        this.centerY = centerY;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "{" +
                "roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", leftTopX=" + leftTopX +
                ", leftTopY=" + leftTopY +
                ", leftDownX=" + leftDownX +
                ", leftDownY=" + leftDownY +
                ", rightTopX=" + rightTopX +
                ", rightTopY=" + rightTopY +
                ", rightDownX=" + rightDownX +
                ", rightDownY=" + rightDownY +
                ", centerX=" + centerX +
                ", centerY=" + centerY +
                ", mainNumR=" + mainNumR +
                ", mainNumC=" + mainNumC +
                '}';
    }

    public Integer getMainNumR() {
        return mainNumR;
    }

    public void setMainNumR(Integer mainNumR) {
        this.mainNumR = mainNumR;
    }

    public Integer getMainNumC() {
        return mainNumC;
    }

    public void setMainNumC(Integer mainNumC) {
        this.mainNumC = mainNumC;
    }
}
