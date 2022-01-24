package de.schafunschaf.voidtec.campaign.intel.buttons;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.ui.IntelUIAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import de.schafunschaf.voidtec.campaign.intel.AugmentManagerIntel;
import de.schafunschaf.voidtec.scripts.combat.effects.vesai.AugmentApplier;
import de.schafunschaf.voidtec.scripts.combat.effects.vesai.AugmentSlot;
import lombok.RequiredArgsConstructor;

import static de.schafunschaf.voidtec.util.ComparisonTools.isNull;

@RequiredArgsConstructor
public class InstallAugmentButton implements IntelButton {
    private final AugmentSlot augmentSlot;

    @Override
    public void buttonPressCancelled(IntelUIAPI ui) {

    }

    @Override
    public void buttonPressConfirmed(IntelUIAPI ui) {
        boolean success = augmentSlot.installAugment(AugmentManagerIntel.selectedAugmentInCargo.getAugment());
        if (success)
            removeAugmentFromCargo();

        AugmentManagerIntel.selectedAugmentInCargo = null;
    }

    @Override
    public void createConfirmationPrompt(TooltipMakerAPI tooltip) {
        AugmentApplier augment = AugmentManagerIntel.selectedAugmentInCargo.getAugment();
        String bullet = "• ";

        tooltip.addPara("Install the augment in this slot (%s)?", 0f, augmentSlot.getSlotCategory().getColor(), augmentSlot.getSlotCategory().getName());
        if (!isNull(augment))
            tooltip.addPara(bullet + augment.getName(), augment.getAugmentQuality().getColor(), 10f);
    }

    @Override
    public boolean doesButtonHaveConfirmDialog() {
        return true;
    }

    @Override
    public String getConfirmText() {
        return "Install";
    }

    @Override
    public String getCancelText() {
        return "Cancel";
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getShortcut() {
        return 0;
    }

    private void removeAugmentFromCargo() {
        CargoAPI cargo = AugmentManagerIntel.selectedAugmentInCargo.getSourceCargo();
        for (CargoStackAPI cargoStackAPI : cargo.getStacksCopy()) {
            if (cargoStackAPI.getData() == AugmentManagerIntel.selectedAugmentInCargo.getAugmentCargoStack().getData()) {
                cargoStackAPI.setSize(cargoStackAPI.getSize() - 1);
                cargo.removeEmptyStacks();
                return;
            }
        }
    }
}