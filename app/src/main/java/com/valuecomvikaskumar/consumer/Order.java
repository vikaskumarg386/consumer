package com.valuecomvikaskumar.consumer;

/**
 * Created by vikas on 27/6/18.
 */

public class Order {
    private String list;
    private String date;
    private String consumerId;

    public Order() {
    }

    public Order(String list, String date, String consumerId) {
        this.list = list;
        this.date = date;
        this.consumerId = consumerId;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }
}
