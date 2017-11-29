package me.loki2302.serializer;

import me.loki2302.serializer.HasIntValue;

public enum GoodGender implements HasIntValue {
    Female(0),
    Male(1),
    Unknown(2);

    private final int value;

    GoodGender(int value) {
        this.value = value;
    }

    @Override
    public int getIntValue() {
        return value;
    }

}
