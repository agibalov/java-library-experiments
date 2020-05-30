package io.agibalov.serializer;

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
