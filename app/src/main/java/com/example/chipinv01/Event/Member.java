package com.example.chipinv01.Event;

import java.io.Serializable;

public class Member implements Serializable {
    String name;
    String number;
    Long image_id;
    Double amount;
    long id;

    public Member() {
    }

    public Member(String name, String number, Long image_id, long id) {
        this.name = name;
        this.number = number;
        this.image_id = image_id;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getImage_id() {
        return image_id;
    }

    public void setImage_id(Long image_id) {
        this.image_id = image_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
