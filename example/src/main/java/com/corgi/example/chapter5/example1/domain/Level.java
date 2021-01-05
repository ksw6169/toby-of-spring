package com.corgi.example.chapter5.example1.domain;

public enum Level {

    BASIC(1),
    SILVER(2),
    GOLD(3);

    private final int value;

    Level(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public static Level valueOf(int value) {
        for (Level level : Level.values()) {
            if (level.intValue() == value) {
                return level;
            }
        }

        throw new AssertionError("Unknown value: " + value);
    }
}
