package com.company.Objects;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Random;

public class Ticket implements Comparable<Ticket>, Serializable {
    private static final long serialVersionUID = 2L;
    private Integer ids;
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Float price; //Поле может быть null, Значение поля должно быть больше 0
    private int discount; //Значение поля должно быть больше 0, Максимальное значение поля: 100
    private TicketType type; //Поле не может быть null
    private Event event; //Поле не может быть null

    public Ticket() {
        this.id = makeId();
        this.name = "Bob";
        this.coordinates = new Coordinates();
        this.creationDate = ZonedDateTime.from(ZonedDateTime.now().withNano(0));
        this.type = TicketType.USUAL;
    }

    public Integer getIds() {
        return ids;
    }

    public void setIds(Integer ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ids=" + ids +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", discount=" + discount +
                ", type=" + type +
                ", event=" + event +
                '}';
    }

    public Ticket(Integer id, String name, Coordinates coordinates, Float price, int discount, TicketType type, Event event) {
        this.id = makeId();
        this.name = name;
        this.coordinates = coordinates;
        creationDate = ZonedDateTime.from(ZonedDateTime.now().withNano(0)); //Change
        this.discount = discount;
        this.price = price;
        this.type = type;
        this.event = event;
    }

    public Integer makeId() {
        long curMill = System.currentTimeMillis();          //Значение должно быть > 0
        Random random = new Random(curMill);
        int id = random.nextInt();
        if (id < 0)
            id = Math.abs(id);
        return id;
    }

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id) {
        this.id = (id > 0) ? id : makeId();
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = (name.equals("")) ? "Bob" : name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = Objects.requireNonNullElseGet(coordinates, () -> new Coordinates());
    }

    public ZonedDateTime getCreationDate() {
        return ZonedDateTime.from(creationDate);
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = Objects.requireNonNullElseGet(creationDate, () -> ZonedDateTime.now().withNano(0));
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        if (price > 0) this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        if (discount > 0 && discount < 100) this.discount = discount;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public int compareTo(Ticket o) {
        return 0;
    }

    public void setTicketType(Object o) {
        setTicketType(o);
    }
}

    