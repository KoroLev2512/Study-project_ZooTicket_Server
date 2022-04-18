package com.company.Objects;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 4L;

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = (y > 370) ? 370 : y;
    }

    private Integer x; //Поле не может быть null
    private Integer y; //Максимальное значение поля: 370, Поле не может быть null

    public Coordinates() {
        this.x = x;
        this.y = (y > 370) ? 370 : y;
    }

    @Override
    public String toString() {
        return x.toString() + ";" + y.toString();
    }
}
