package com.company;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    private static final long serialVersionUID = 10L;

    private final ArrayList<String> strings = new ArrayList<>();

    public ArrayList<String> getStrings() {
        return strings;
    }

    public void Add(String str){
        strings.add(str);
    }
}
