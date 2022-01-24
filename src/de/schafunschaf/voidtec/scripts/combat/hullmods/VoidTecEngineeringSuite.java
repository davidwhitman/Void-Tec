package de.schafunschaf.voidtec.scripts.combat.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import de.schafunschaf.voidtec.VT_Colors;
import de.schafunschaf.voidtec.scripts.combat.effects.vesai.HullModDataStorage;
import de.schafunschaf.voidtec.scripts.combat.effects.vesai.HullModManager;

import java.awt.Color;
import java.util.Random;

import static de.schafunschaf.voidtec.util.ComparisonTools.isNull;


public class VoidTecEngineeringSuite extends BaseHullMod {
    public static final String HULL_MOD_ID = "voidTec_engineeringSuite";

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        FleetMemberAPI fleetMember = stats.getFleetMember();
        if (isNull(fleetMember)) {
            return;
        }

        disableVanillaSModInstallation(stats, id);

        Random random = new Random(fleetMember.getId().hashCode());
        HullModDataStorage hullModDataStorage = HullModDataStorage.getInstance();
        HullModManager hullmodManager = hullModDataStorage.getHullModManager(fleetMember.getId());

        if (isNull(hullmodManager)) {
            hullmodManager = new HullModManager(fleetMember);
            hullModDataStorage.storeShipID(fleetMember.getId(), hullmodManager);
        }

        hullmodManager.applySlotEffects(stats, id, random);
    }

    private void disableVanillaSModInstallation(MutableShipStatsAPI stats, String id) {
        float maxPermanentHullmods = Global.getSettings().getFloat("maxPermanentHullmods");
        stats.getDynamic().getMod(Stats.MAX_PERMANENT_HULLMODS_MOD).modifyFlat(id, -maxPermanentHullmods);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width,
                                          boolean isForModSpec) {
        FleetMemberAPI fleetMember = ship.getFleetMember();
        if (isNull(fleetMember)) {
            return;
        }

        HullModDataStorage hullModDataStorage = HullModDataStorage.getInstance();
        HullModManager hullmodManager = hullModDataStorage.getHullModManager(fleetMember.getId());
        if (isNull(hullmodManager)) {
            return;
        }

        hullmodManager.generateTooltip(fleetMember.getStats(), HULL_MOD_ID, tooltip, width, false);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        FleetMemberAPI fleetMember = ship.getFleetMember();
        if (isNull(fleetMember)) {
            return;
        }

        HullModDataStorage hullModDataStorage = HullModDataStorage.getInstance();
        HullModManager hullmodManager = hullModDataStorage.getHullModManager(fleetMember.getId());
        if (isNull(hullmodManager)) {
            return;
        }

        hullmodManager.runCombatScript(ship, amount);
    }

    @Override
    public Color getBorderColor() {
        return VT_Colors.VT_COLOR_MAIN;
    }

    @Override
    public Color getNameColor() {
        return VT_Colors.VT_COLOR_MAIN;
    }

    @Override
    public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
        return false;
    }
}
