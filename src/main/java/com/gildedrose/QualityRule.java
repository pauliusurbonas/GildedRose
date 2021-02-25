package com.gildedrose;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class QualityRule {
    private int expireInDaysTreshold;
    private int qualityRate;
}
