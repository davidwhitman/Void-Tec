package de.schafunschaf.voidtec.combat.vesai.augments;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import de.schafunschaf.voidtec.combat.vesai.AugmentSlot;
import de.schafunschaf.voidtec.combat.vesai.RightClickAction;
import de.schafunschaf.voidtec.combat.vesai.SlotCategory;
import de.schafunschaf.voidtec.combat.vesai.statmodifiers.StatApplier;
import de.schafunschaf.voidtec.combat.vesai.statmodifiers.StatModValue;
import de.schafunschaf.voidtec.helper.TextWithHighlights;

import java.awt.Color;
import java.util.List;

public interface AugmentApplier {

    void applyToShip(MutableShipStatsAPI stats, String id, int slotIndex);

    void applyToFighter(MutableShipStatsAPI stats, String id);

    void generateTooltip(MutableShipStatsAPI stats, String id, TooltipMakerAPI tooltip, float width, SlotCategory slotCategory,
                         Color bulletColorOverride);

    void generateStatDescription(TooltipMakerAPI tooltip, float padding, Boolean isPrimary, Color bulletColorOverride);

    void generateStatDescription(TooltipMakerAPI tooltip, float padding, Boolean isPrimary, Color bulletColorOverride,
                                 AugmentQuality quality);

    void applyBeforeCreation(MutableShipStatsAPI stats, String id);

    void applyAfterCreation(ShipAPI ship, String id);

    void runCombatScript(ShipAPI ship, float amount);

    String getAugmentID();

    String getName();

    TextWithHighlights getDescription();

    String getManufacturer();

    SlotCategory getPrimarySlot();

    List<SlotCategory> getSecondarySlots();

    List<StatApplier> getPrimaryStatMods();

    List<StatModValue<Float, Float, Boolean, Boolean>> getPrimaryStatValues();

    List<StatApplier> getSecondaryStatMods();

    List<StatModValue<Float, Float, Boolean, Boolean>> getSecondaryStatValues();

    AugmentQuality getAugmentQuality();

    AugmentQuality getInitialQuality();

    TextWithHighlights getAdditionalDescription();

    List<StatApplier> getActiveStatMods();

    RightClickAction getRightClickAction();

    void runRightClickAction();

    void collectAppliedStats(MutableShipStatsAPI stats, String id);

    boolean isRepairable();

    boolean isDestroyed();

    AugmentApplier damageAugment(int numLevelsDamaged);

    AugmentApplier repairAugment(int numLevelsRepaired);

    void installAugment(AugmentSlot augmentSlot);

    AugmentSlot getInstalledSlot();

    void uninstall();

    void updateFighterStatValue(String id, float value);

    Float getFighterStatValue(String id);

    boolean isInPrimarySlot();

    boolean isUniqueMod();

    boolean isStackable();
}
