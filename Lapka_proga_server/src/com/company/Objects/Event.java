package com.company.Objects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

public class Event implements Serializable {
    private static final long serialVersionUID = 3L;

    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private LocalDateTime date; //Поле не может быть null
    private long ticketsCount; //Значение поля должно быть больше 0
    private String description; //Строка не может быть пустой, Поле не может быть null

    public Event() {
        this.id = 1;
        this.name = "Halloween";
        this.date = LocalDateTime.now().withNano(0);
        this.ticketsCount = 400;
        this.description = "Entrance ticket";
    }

    public Event(int id, String name, LocalDateTime date, long ticketsCount, String description) {
        this.id = id;
        this.name = name;
        date = LocalDateTime.now().withNano(0);
        this.ticketsCount = ticketsCount;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.equals(""))
            this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = (id > 0) ? id : makeId();
    }

    public Integer makeId() {
        long curMill = System.currentTimeMillis();          //Значение должно быть > 0
        Random random = new Random(curMill);
        int id = random.nextInt();
        if (id < 0)
            id = Math.abs(id);
        return id;
    }

    public long getTicketsCount() {
        return ticketsCount;
    }

    public void setTicketsCount(long ticketsCount) {
        if (ticketsCount > 0)  this.ticketsCount = ticketsCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime creationDate) {
        this.date = Objects.requireNonNullElseGet(creationDate, () -> LocalDateTime.now().withNano(0));
    }
}
