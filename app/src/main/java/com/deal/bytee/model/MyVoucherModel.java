package com.deal.bytee.model;

public class MyVoucherModel {

    String cv_id,cv_status,cv_code,cv_price,cv_min_purchase_amount,cv_business_id,cv_business_title,cv_expiry_date,cv_discount;

    int discountamount,cv_amount_applied;

    public int getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(int discountamount) {
        this.discountamount = discountamount;
    }

    public int getCv_amount_applied() {
        return cv_amount_applied;
    }

    public void setCv_amount_applied(int cv_amount_applied) {
        this.cv_amount_applied = cv_amount_applied;
    }

    public String getCv_discount() {
        return cv_discount;
    }

    public void setCv_discount(String cv_discount) {
        this.cv_discount = cv_discount;
    }

    public String getCv_business_title() {
        return cv_business_title;
    }

    public void setCv_business_title(String cv_business_title) {
        this.cv_business_title = cv_business_title;
    }

    public String getCv_id() {
        return cv_id;
    }

    public void setCv_id(String cv_id) {
        this.cv_id = cv_id;
    }

    public String getCv_status() {
        return cv_status;
    }

    public void setCv_status(String cv_status) {
        this.cv_status = cv_status;
    }

    public String getCv_code() {
        return cv_code;
    }

    public void setCv_code(String cv_code) {
        this.cv_code = cv_code;
    }

    public String getCv_price() {
        return cv_price;
    }

    public void setCv_price(String cv_price) {
        this.cv_price = cv_price;
    }

    public String getCv_min_purchase_amount() {
        return cv_min_purchase_amount;
    }

    public void setCv_min_purchase_amount(String cv_min_purchase_amount) {
        this.cv_min_purchase_amount = cv_min_purchase_amount;
    }

    public String getCv_business_id() {
        return cv_business_id;
    }

    public void setCv_business_id(String cv_business_id) {
        this.cv_business_id = cv_business_id;
    }

    public String getCv_expiry_date() {
        return cv_expiry_date;
    }

    public void setCv_expiry_date(String cv_expiry_date) {
        this.cv_expiry_date = cv_expiry_date;
    }
}
