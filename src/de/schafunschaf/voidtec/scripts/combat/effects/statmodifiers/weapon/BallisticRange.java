package de.schafunschaf.voidtec.scripts.combat.effects.statmodifiers.weapon;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import de.schafunschaf.voidtec.scripts.combat.effects.statmodifiers.BaseStatMod;
import de.schafunschaf.voidtec.scripts.combat.effects.statmodifiers.StatModValue;
import de.schafunschaf.voidtec.scripts.combat.effects.vesai.AugmentQuality;
import de.schafunschaf.voidtec.util.ComparisonTools;

import java.awt.Color;
import java.util.Random;

public class BallisticRange extends BaseStatMod {
    @Override
    public void apply(MutableShipStatsAPI stats, String id, StatModValue<Float, Float, Boolean> statModValue,
                      Random random, AugmentQuality quality) {
        stats.getBallisticWeaponRangeBonus().modifyPercent(id, generateModValue(statModValue, random, quality));
    }

    @Override
    public void remove(MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponRangeBonus().unmodify(id);
    }

    @Override
    public void generateTooltipEntry(MutableShipStatsAPI stats, String id, TooltipMakerAPI tooltip, Color bulletColor) {
        MutableStat.StatMod statMod = stats.getBallisticWeaponRangeBonus().getPercentBonus(id);
        if (ComparisonTools.isNull(statMod)) {
            return;
        }

        String description = "Ballistic weapon range %s by %s";
        generateTooltip(tooltip, statMod, description, bulletColor, false);
    }

    @Override
    public void generateStatDescription(TooltipMakerAPI tooltip, Color bulletColor, float avgModValue) {
        boolean isPositive = avgModValue >= 0;
        String incDec = isPositive ? "Increases" : "Lowers";
        String hlString1 = "range";
        String hlString2 = "ballistic weapons";
        String description = String.format("the %s of all %s", hlString1, hlString2);

        generateStatDescription(tooltip, description, incDec, bulletColor, isPositive, hlString1, hlString2);
    }
}
