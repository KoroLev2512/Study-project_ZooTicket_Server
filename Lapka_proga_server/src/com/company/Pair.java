package com.company;

import java.io.Serializable;

public class Pair <U, V> implements Serializable {
    private static final long serialVersionUID = 3L;

    public U first;
    public V second;

    public Pair(U first, V second){
        this.first = first;
        this.second = second;
    }
}
