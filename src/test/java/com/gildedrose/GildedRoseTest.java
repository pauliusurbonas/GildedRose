package com.gildedrose;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GildedRoseTest {
    GildedRose app = null;

    private void iterateDays(int days) {
        while(days > 0) {
            app.updateQuality();
            days--;
        }
    }

    private void testItem(String name, ItemGroup group, int sellIn, int quality, int iterateDays, int expectExpireInDays, int expectedQuality) {
        app = new GildedRose();
        GildedRoseItem item = new GildedRoseItem(group, name, sellIn, quality);
        app.addItem(item);
        iterateDays(iterateDays);
        GildedRoseItem agedItem =  app.getItem(name);
        Assertions.assertEquals(expectExpireInDays, agedItem.getExpireInDays(), "expected days before expiration of " + name);
        Assertions.assertEquals(expectedQuality, agedItem.getQuality(), "expected quality of" + name);
    }

    @Test
    void test() {
        //min quality
        testItem("+5 Dexterity Vest", ItemGroup.REGULAR_ITEM, 10, 20, 20,-10, 0);
        testItem("Regular item", ItemGroup.REGULAR_ITEM, 10, 30, 10,0, 20);
        //increase quality
        testItem("Aged Brie", ItemGroup.AGED_BRIE, 2, 1, 5,-3, 6);
        testItem("Aged Brie", ItemGroup.AGED_BRIE, 2, 0, 1,1, 1);
        //everlasting item max quality
        testItem("Sulfuras, Hand of Ragnaros", ItemGroup.SULFURAS, 0, 80, 85,-85, 80);
        testItem("Sulfuras, Hand of Ragnaros", ItemGroup.SULFURAS, 10, 100, 5,5, 80);
        testItem("Sulfuras, Hand of Ragnaros", ItemGroup.SULFURAS, -3, -5, 1,-4, 80);
        testItem("Backstage passes to a TAFKAL80ETC concert", ItemGroup.BACKSTAGE_PASSES, 15, 20, 12, 3, 20 + 5*1 + 5*2 + 2*3);
        //max quality
        testItem("Backstage passes to a TAFKAL80ETC concert", ItemGroup.BACKSTAGE_PASSES, 25, 10, 20, 5, 10 + 15*1 + 5*2);
        //quality drops to 0 after the concert
        testItem("Backstage passes to a TAFKAL80ETC concert", ItemGroup.BACKSTAGE_PASSES, 9, 99, 10, -1, 0);
        //double aged
        testItem("Conjured Mana Cake", ItemGroup.CONJURED, 3, 6, 5, -2,0);
    }
}
