package com.corgi.example.chapter5.example1.domain;

public enum Level {

    GOLD(3, null),
    SILVER(2, GOLD),
    BASIC(1, SILVER);

    private final int value;
    private final Level next;

    Level(int value, Level next) {
        this.value = value;
        this.next = next;
    }

    public int intValue() {
        return this.value;
    }

    public Level nextLevel() {
        return this.next;
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
