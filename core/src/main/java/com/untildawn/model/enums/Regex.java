package com.untildawn.model.enums;

public enum Regex {
    USERNAME("^.{3,}$"),
    PASSWORD("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_()*&%$#@).{8,}$")
    ;
    private final String pattern;

    Regex(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
