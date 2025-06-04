package com.untildawn.model.enums;

import java.util.regex.Pattern;

public enum Regex {
    USERNAME("^.{3,}$"),
    PASSWORD("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_()*&%$#@]).+$");
    private final String pattern;

    Regex(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean matches(String input) {
        return Pattern.compile(pattern).matcher(input).matches();
    }
}
