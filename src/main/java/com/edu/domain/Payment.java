package com.edu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * 微信支付分配的终端设备号
     */
    private String deviceInfo;

    /**
     * 错误代码
     */
    private String errCode;

    /**
     * 错误代码描述
     */
    private String errCodeDes;

    /**
     * 用户标识
     */
    private String openId;

    /**
     * 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
     */
    private String isSubsribe = "Y";

    /**
     * 交易类型
     * JSAPI、NATIVE、APP
     */
    private String tradeType;

    /**
     * 付款银行
     */
    private String bankType;

    /**
     * 终端IP
     * APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
     */
    private String spBillCreateIp;

    /**
     * 订单总金额，单位为分
     */
    private int totalFee = 0;

    /**
     * 应结订单金额
     * 应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     */
    private int settlementTotalFee = 0;

    /**
     * 货币种类
     * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    private String feeType;

    /**
     * 现金支付金额
     * 现金支付金额订单现金支付金额，详见支付金额
     */
    private int cashFee = 0;

    /**
     * 现金支付货币类型
     * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    private String cashFeeType;

    /**
     * 总代金券金额
     * 代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
     */
    private int couponFee = 0;

    /**
     * 代金券使用数量
     */
    private int couponCount = 0;

    @JsonIgnore
    @OneToMany(mappedBy="payment")
    private Set<Coupon> coupons = new HashSet<>();

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 商家数据包
     */
    private String attach;

    /**
     * 交易起始时间
     * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
     */
    private String timeStart;

    /**
     * 支付完成时间
     * 格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
     */
    private String timeEnd;

    @OneToOne(mappedBy = "payment")
    private Order order;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refund_id")
    private Refund refund; // 不支持部分退款

    public long getId() {
        return id;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getIsSubsribe() {
        return isSubsribe;
    }

    public void setIsSubsribe(String isSubsribe) {
        isSubsribe = isSubsribe;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getSpBillCreateIp() {
        return spBillCreateIp;
    }

    public void setSpBillCreateIp(String spBillCreateIp) {
        this.spBillCreateIp = spBillCreateIp;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getSettlementTotalFee() {
        return settlementTotalFee;
    }

    public void setSettlementTotalFee(int settlementTotalFee) {
        this.settlementTotalFee = settlementTotalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public int getCashFee() {
        return cashFee;
    }

    public void setCashFee(int cashFee) {
        this.cashFee = cashFee;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
    }

    public int getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(int couponFee) {
        this.couponFee = couponFee;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public Set<Coupon> getCoupons() {
        return coupons;
    }

    public void addCoupon(Coupon coupon) {
        this.coupons.add(coupon);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Refund getRefund() {
        return refund;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }
}
