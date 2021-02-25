package com.gildedrose;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GildedRoseItem extends Item {
    private ItemGroup group;

    public GildedRoseItem(ItemGroup itemGroup, String name, int sellIn, int quality) {
        super(name, sellIn, quality);
        group = itemGroup;
    }

    public String getName() {
        return super.name;
    }

    public int getExpireInDays() {
        return super.sellIn;
    }

    public void setExpireInDays(int expirationDays) {
        super.sellIn = expirationDays;
    }

    public int getQuality() {
        return super.quality;
    }

    public void setQuality(int quality) {
        super.quality = quality;
    }
}
