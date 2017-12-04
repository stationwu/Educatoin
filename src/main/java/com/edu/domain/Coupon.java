package com.edu.domain;

import javax.persistence.*;

@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int couponIndex = 0;

    private String couponId;

    private String couponType;

    private int couponFee = 0;

    @ManyToOne
    @JoinColumn(name="payment_id")
    private Payment payment;

    public Coupon() {
        this.couponIndex = 0;
        this.couponFee = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCouponIndex() {
        return couponIndex;
    }

    public void setCouponIndex(int couponIndex) {
        this.couponIndex = couponIndex;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public int getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(int couponFee) {
        this.couponFee = couponFee;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
