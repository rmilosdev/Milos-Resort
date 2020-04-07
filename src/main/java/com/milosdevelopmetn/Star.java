package com.milosdevelopmetn;

public enum Star {

    ONE("Jedna", "jedna"),
    TWO("Dve", "dve"),
    THREE("Tri", "tri"),
    FOUR("Cetri", "cetri"),
    FIVE("Pet", "pet");


    private final String name;
    private final String value;

    Star(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
