package com.gildedrose;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GildedRose {
    static final int MIN_QUALITY = 0; //the Quality of an item is never negative
    static final int MAX_QUALITY = 50; //the Quality of an item is never more than 50
    static final int EVERLASTING_ITEM_QUALITY = 80; //legendary item quality is 80 and it never alters

    private final Map<String, GildedRoseItem> items;
    private final Map<QualityGroup, QualityRule> qualityRules;
    private final Map<ItemGroup, List<QualityGroup>> groupRules;

    public GildedRose() {
        items = new HashMap<>();
        qualityRules = new HashMap<>();
        groupRules = new HashMap<>();
        addQualityGroups();
        addGroupQualityRules();
    }

    private void addQualityGroups() {
        //all items have a Quality value which denotes how valuable the item is
        qualityRules.put(QualityGroup.REGULAR_AGED, new QualityRule(Integer.MAX_VALUE, -1));
        //once the sell by date has passed, Quality degrades twice as fast
        qualityRules.put(QualityGroup.DOUBLE_AGED_EXPIRED, new QualityRule(0, -1));
        //degrade in Quality twice as fast as normal items
        qualityRules.put(QualityGroup.DOUBLE_AGED, new QualityRule(Integer.MAX_VALUE, -2));
        //legendary item, never has to be sold or decreases in Quality
        qualityRules.put(QualityGroup.EVERLASTING, new QualityRule(Integer.MAX_VALUE, 0));
        //increases in Quality the older it gets
        qualityRules.put(QualityGroup.MATURED, new QualityRule(Integer.MAX_VALUE, 1));
        //quality increases by 2 when there are 10 days or less
        qualityRules.put(QualityGroup.DOUBLE_MATURED_REMAINING_DAYS_10, new QualityRule(10, 2));
        //quality increases by 3 when there are 5 days or less
        qualityRules.put(QualityGroup.TRIPLE_MATURED_REMAINING_DAYS_5, new QualityRule(5, 3));
    }

    private void addGroupQualityRules() {
        groupRules.put(ItemGroup.REGULAR_ITEM, Arrays.asList(QualityGroup.REGULAR_AGED));
        groupRules.put(ItemGroup.AGED_BRIE, Arrays.asList(QualityGroup.MATURED));
        groupRules.put(ItemGroup.BACKSTAGE_PASSES, Arrays.asList(
                QualityGroup.MATURED,
                QualityGroup.DOUBLE_MATURED_REMAINING_DAYS_10,
                QualityGroup.TRIPLE_MATURED_REMAINING_DAYS_5
        ));
        groupRules.put(ItemGroup.SULFURAS, Arrays.asList(QualityGroup.EVERLASTING));
        groupRules.put(ItemGroup.CONJURED, Arrays.asList(QualityGroup.DOUBLE_AGED));
    }

    public void addItem(GildedRoseItem item) {
        items.put(item.getName(), item);
    }

    public GildedRoseItem getItem(String name) {
        return items.get(name);
    }

    private void applyQualityLimits(GildedRoseItem item) {
        //legendary item quality
        if(ItemGroup.SULFURAS.equals(item.getGroup())) {
            if(item.getQuality() != EVERLASTING_ITEM_QUALITY) {
                item.setQuality(EVERLASTING_ITEM_QUALITY);
            }

            return;
        }
        //the Quality of an item is never negative or BACKSTAGE_PASSES quality drops to 0 after the concert
        if(item.getQuality() < MIN_QUALITY
                || (item.getGroup().equals(ItemGroup.BACKSTAGE_PASSES) && item.getExpireInDays() <= 0)) {
            item.setQuality(MIN_QUALITY);
            return;
        }
        //the Quality of an item is never more than
        if(item.getQuality() > MAX_QUALITY) {
            item.setQuality(MAX_QUALITY);
        }


    }

    private int calculateNextQualityRate(GildedRoseItem item) {
        int qualityRate = 0;
        int threshold = Integer.MAX_VALUE;

        List<QualityGroup> rules = groupRules.get(item.getGroup());
        for(QualityGroup qualityGroup : rules) {
            QualityRule qualityRule = qualityRules.get(qualityGroup);

            if(item.getExpireInDays() <= qualityRule.getExpireInDaysTreshold()
                    && qualityRule.getExpireInDaysTreshold() <= threshold) {
                threshold = qualityRule.getExpireInDaysTreshold();
                qualityRate = qualityRule.getQualityRate();
            }
        }

        return qualityRate;
    }

    private void applyQualityRules(GildedRoseItem item) {
        int rate = calculateNextQualityRate(item);
        item.setQuality(item.getQuality() + rate);
        applyQualityLimits(item);
    }

    public void updateQuality() {
        for(Map.Entry<String,GildedRoseItem> entry : items.entrySet()) {
            GildedRoseItem item = entry.getValue();
            applyQualityRules(item);
            //all items have a SellIn value which denotes the number of days we have to sell the item
            item.setExpireInDays(item.getExpireInDays() - 1);
        }
    }
}