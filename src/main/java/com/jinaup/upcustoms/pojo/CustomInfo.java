package com.jinaup.upcustoms.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 邓子迪
 * @Description TODO
 * @time 2019/5/9
 */
public class CustomInfo implements Serializable {
    private static final long serialVersionUID = -2051918573135601366L;

    private String SerssionID;

    private String orderNo;

    private BigDecimal totalAmount;

    private String payTransactionId;

    private String tradingTime;

    private String payCode;

    private String guid;

    private String initalRequest;

    private String initalResponse;

    private String verDept;

    private String payType;

    private String note;

    private String goodsInfo;

    private String signValue;

    private String certNo;

    private long serviceTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSerssionID() {
        return SerssionID;
    }

    public void setSerssionID(String serssionID) {
        SerssionID = serssionID;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPayTransactionId() {
        return payTransactionId;
    }

    public void setPayTransactionId(String payTransactionId) {
        this.payTransactionId = payTransactionId;
    }

    public String getTradingTime() {
        return tradingTime;
    }

    public void setTradingTime(String tradingTime) {
        this.tradingTime = tradingTime;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getInitalRequest() {
        return initalRequest;
    }

    public void setInitalRequest(String initalRequest) {
        this.initalRequest = initalRequest;
    }

    public String getInitalResponse() {
        return initalResponse;
    }

    public void setInitalResponse(String initalResponse) {
        this.initalResponse = initalResponse;
    }

    public String getVerDept() {
        return verDept;
    }

    public void setVerDept(String verDept) {
        this.verDept = verDept;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public String getSignValue() {
        return signValue;
    }

    public void setSignValue(String signValue) {
        this.signValue = signValue;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public long getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(long serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
        return "CustomInfo{" +
                "SerssionID='" + SerssionID + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", totalAmount=" + totalAmount +
                ", payTransactionId='" + payTransactionId + '\'' +
                ", tradingTime='" + tradingTime + '\'' +
                ", payCode='" + payCode + '\'' +
                ", guid='" + guid + '\'' +
                ", initalRequest='" + initalRequest + '\'' +
                ", initalResponse='" + initalResponse + '\'' +
                ", verDept='" + verDept + '\'' +
                ", payType='" + payType + '\'' +
                ", note='" + note + '\'' +
                ", goodsInfo='" + goodsInfo + '\'' +
                ", signValue='" + signValue + '\'' +
                ", certNo='" + certNo + '\'' +
                '}';
    }
}
