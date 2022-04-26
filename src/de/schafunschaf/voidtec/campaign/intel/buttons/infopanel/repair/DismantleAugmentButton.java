package de.schafunschaf.voidtec.campaign.intel.buttons.infopanel.repair;

import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.CutStyle;
import com.fs.starfarer.api.ui.IntelUIAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import de.schafunschaf.voidtec.campaign.crafting.AugmentPartsUtility;
import de.schafunschaf.voidtec.campaign.crafting.parts.CraftingComponent;
import de.schafunschaf.voidtec.campaign.intel.AugmentManagerIntel;
import de.schafunschaf.voidtec.campaign.intel.buttons.DefaultButton;
import de.schafunschaf.voidtec.combat.vesai.augments.AugmentApplier;
import de.schafunschaf.voidtec.combat.vesai.augments.AugmentQuality;
import de.schafunschaf.voidtec.helper.AugmentCargoWrapper;
import de.schafunschaf.voidtec.ids.VT_Strings;
import de.schafunschaf.voidtec.util.VoidTecUtils;
import de.schafunschaf.voidtec.util.ui.ButtonUtils;

import java.awt.Color;

import static de.schafunschaf.voidtec.util.ComparisonTools.isNull;

public class DismantleAugmentButton extends DefaultButton {

    private final AugmentApplier augment;
    private final AugmentCargoWrapper augmentCargoWrapper;
    private final boolean canNotBeDismantled;

    public DismantleAugmentButton(AugmentApplier augment) {
        this.augment = augment;
        this.augmentCargoWrapper = null;
        this.canNotBeDismantled = augment.isDestroyed()
                || augment.getAugmentQuality() == AugmentQuality.CUSTOMISED
                || augment.getInitialQuality() == AugmentQuality.DEGRADED;
    }

    public DismantleAugmentButton(AugmentCargoWrapper augmentCargoWrapper) {
        this.augmentCargoWrapper = augmentCargoWrapper;
        this.augment = augmentCargoWrapper.getAugment();
        this.canNotBeDismantled = augment.isDestroyed()
                || augment.getAugmentQuality() == AugmentQuality.CUSTOMISED
                || augment.getInitialQuality() == AugmentQuality.DEGRADED;
    }

    @Override
    public void buttonPressConfirmed(IntelUIAPI ui) {
        if (!(!isNull(augmentCargoWrapper) && augment.getAugmentQuality() == AugmentQuality.CUSTOMISED)) {
            if (!isNull(augmentCargoWrapper)) {
                AugmentPartsUtility.dismantleAugment(augmentCargoWrapper);
                AugmentManagerIntel.setSelectedAugmentInCargo(null);
            } else {
                AugmentPartsUtility.dismantleAugment(augment);
                AugmentManagerIntel.setSelectedInstalledAugment(null);
            }
        }
    }

    @Override
    public void createConfirmationPrompt(TooltipMakerAPI tooltip) {
        tooltip.addPara(String.format("%s %s?", getName(), augment.getName()), 0f, augment.getAugmentQuality().getColor(),
                        augment.getName());

        if (AugmentPartsUtility.getComponentsForDismantling(augment).isEmpty()) {
            tooltip.addPara("This augment appears to have no usable components left to salvage.", Misc.getGrayColor(), 10f);
            tooltip.addPara("Consider selling instead of trashing it to get some pocket change.", Misc.getGrayColor(), 3f);
        } else {
            tooltip.addPara("This will give you the following components:", 10f, Misc.getHighlightColor(),
                            Misc.getDGSCredits(VoidTecUtils.calcNeededCreditsForRepair(augment)));
            tooltip.addSpacer(3f);
            tooltip.setBulletedListMode(String.format(" %s ", VT_Strings.BULLET_CHAR));
            for (CraftingComponent component : AugmentPartsUtility.getComponentsForDismantling(augment)) {
                Color compCatColor = isNull(component.getPartCategory()) ? Misc.getTextColor() : component.getPartCategory().getColor();
                tooltip.setBulletColor(component.getPartQuality().getColor());
                tooltip.addPara("%s %s %s-Parts", 0f,
                                new Color[]{Misc.getHighlightColor(), component.getPartQuality().getColor(), compCatColor,
                                            Misc.getHighlightColor()}, String.valueOf(component.getAmount()),
                                component.getPartQuality().getName(), component.getName());
            }
        }
    }

    @Override
    public boolean doesButtonHaveConfirmDialog() {
        return !(!isNull(augmentCargoWrapper) && augment.getAugmentQuality() == AugmentQuality.CUSTOMISED && !canNotBeDismantled);
    }

    @Override
    public String getConfirmText() {
        return getName();
    }

    @Override
    public String getCancelText() {
        return "Cancel";
    }

    @Override
    public String getName() {
        if (!isNull(augmentCargoWrapper) && augment.getAugmentQuality() == AugmentQuality.CUSTOMISED) {
            return "------";
        } else {
            if (canNotBeDismantled) {
                return "Remove";
            } else {
                return "Dismantle";
            }
        }
    }

    @Override
    public ButtonAPI addButton(TooltipMakerAPI tooltip, float width, float height) {
        Color bgColor = Misc.scaleColorOnly(Misc.getNegativeHighlightColor(), 0.25f);
        return ButtonUtils.addLabeledButton(tooltip, 90, height, 0f, Misc.getNegativeHighlightColor(), bgColor, CutStyle.BL_TR,
                                            this);
    }
}
