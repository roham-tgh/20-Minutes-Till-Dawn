package com.untildawn.model.enums;

public enum MonsterType {
    BRAIN(25, "Monsters/Tentacle"),
    TREE(Integer.MAX_VALUE, "Monsters/Tree"),
    EYEBAT(50, "Monsters/Eyebat");

    int hp;

    public String getAssetFolder() {
        return assetFolder;
    }

    public int getHp() {
        return hp;
    }

    String assetFolder;
    MonsterType(int hp, String assetFolder) {
        this.hp = hp;
        this.assetFolder = assetFolder;
    }
}
