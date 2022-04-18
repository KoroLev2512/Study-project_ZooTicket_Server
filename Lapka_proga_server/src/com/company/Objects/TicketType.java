package com.company.Objects;

import java.util.List;
import java.util.Map;

public enum TicketType {
    VIP("VIP"),
    USUAL("USUAL"),
    BUDGETARY("BUDGETARY"),
    CHEAP("CHEAP");

    private final String value;
    private String ticketType;

    TicketType(String value){
        this.value = value;
    }

    public static Iterable<? extends Map.Entry<String, List<TicketType>>> entrySet() {
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }
}
