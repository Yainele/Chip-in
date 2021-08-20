package com.example.chipinv01.Event;

import java.io.Serializable;

public class Data implements Serializable {
    String name;
    String number;
    Long image_id;
    Double amount;
    long id;

    public Data() {
    }

    public Data(String name, String number, Long image_id, long id) {
        this.name = name;
        this.number = number;
        this.image_id = image_id;
        this.id = id;
    }
}
