package me.loki2302.constraintvalidatorinjection

class MyValidationService {
    boolean isGoodName(String name) {
        return name != null && name.contains('loki')
    }
}