package de.schafunschaf.voidtec.campaign.intel.buttons;

import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.IntelUIAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import de.schafunschaf.voidtec.campaign.intel.CargoPanel;
import de.schafunschaf.voidtec.campaign.intel.InfoPanel;

public class ManufactureTabButton extends DefaultButton {

    @Override
    public void buttonPressConfirmed(IntelUIAPI ui) {
        CargoPanel.showDestroyedAugments = false;
        CargoPanel.showOnlyRepairable = false;
        InfoPanel.setSelectedTab(InfoPanel.InfoTabs.MANUFACTURE);
    }

    @Override
    public String getName() {
        return "Manufacture";
    }

    @Override
    public ButtonAPI createButton(TooltipMakerAPI uiElement, float width, float height) {
        ButtonAPI areaCheckbox = uiElement.addAreaCheckbox(getName(), this, Misc.getBasePlayerColor(), Misc.getDarkPlayerColor(),
                                                           Misc.getBasePlayerColor(),
                                                           width, height, 0f);

        areaCheckbox.setChecked(isSelected());
        return areaCheckbox;
    }

    private boolean isSelected() {
        return InfoPanel.getSelectedTab() == InfoPanel.InfoTabs.MANUFACTURE;
    }
}
