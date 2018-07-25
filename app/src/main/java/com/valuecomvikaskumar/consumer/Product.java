package com.valuecomvikaskumar.consumer;

/**
 * Created by vikas on 18/6/18.
 */

public class Product {


    String productName;
    String vendorUserId;
    String price;
    String sizeOrWeight;
    String barcode;
    String date;
    String imageUrl1;
    String imageUrl2;
    String imageUrl3;
    String imageUrl4;
    String imageUrl5;
    public Product() {
    }

    public Product(String productName, String vendorUserId, String price, String sizeOrWeight, String barcode, String date, String imageUrl1, String imageUrl2, String imageUrl3, String imageUrl4, String imageUrl5) {
        this.productName = productName;
        this.vendorUserId = vendorUserId;
        this.price = price;
        this.sizeOrWeight = sizeOrWeight;
        this.barcode = barcode;
        this.date = date;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.imageUrl4 = imageUrl4;
        this.imageUrl5 = imageUrl5;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVendorUserId() {
        return vendorUserId;
    }

    public void setVendorUserId(String vendorUserId) {
        this.vendorUserId = vendorUserId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSizeOrWeight() {
        return sizeOrWeight;
    }

    public void setSizeOrWeight(String sizeOrWeight) {
        this.sizeOrWeight = sizeOrWeight;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3;
    }

    public String getImageUrl4() {
        return imageUrl4;
    }

    public void setImageUrl4(String imageUrl4) {
        this.imageUrl4 = imageUrl4;
    }

    public String getImageUrl5() {
        return imageUrl5;
    }

    public void setImageUrl5(String imageUrl5) {
        this.imageUrl5 = imageUrl5;
    }
}
