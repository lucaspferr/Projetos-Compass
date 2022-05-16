package com.MS.shopstyle.model;

public enum Sex {

    FEMININO("Feminino"),
    MASCULINO("Masculino");

    private String nameSex;

    private Sex(String sex) {
        this.nameSex = sex;
    }

    @Override
    public String toString() {return nameSex;}
}

