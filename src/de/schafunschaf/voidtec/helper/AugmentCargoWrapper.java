package de.schafunschaf.voidtec.helper;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import de.schafunschaf.voidtec.campaign.items.augments.AugmentItemPlugin;
import de.schafunschaf.voidtec.combat.vesai.augments.AugmentApplier;
import de.schafunschaf.voidtec.util.CargoUtils;
import lombok.Getter;

@Getter
public class AugmentCargoWrapper {

    private final AugmentApplier augment;
    private final CargoStackAPI augmentCargoStack;
    private final CargoSource cargoSource;
    private final CargoAPI sourceCargo;

    public AugmentCargoWrapper(CargoStackAPI augmentCargoStack, CargoSource cargoSource, CargoAPI sourceCargo) {
        this.augmentCargoStack = augmentCargoStack;
        this.cargoSource = cargoSource;
        this.sourceCargo = sourceCargo;
        this.augment = CargoUtils.getAugmentFromStack(augmentCargoStack);
    }

    public AugmentItemPlugin getPlugin() {
        return ((AugmentItemPlugin) augmentCargoStack.getPlugin());
    }

    public enum CargoSource {
        PLAYER_FLEET,
        CARGO_CHEST,
        LOCAL_STORAGE
    }
}
