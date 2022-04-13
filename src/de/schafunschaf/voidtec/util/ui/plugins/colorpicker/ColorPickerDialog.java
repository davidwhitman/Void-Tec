package de.schafunschaf.voidtec.util.ui.plugins.colorpicker;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomDialogDelegate;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;
import de.schafunschaf.voidtec.util.ui.UIUtils;
import lombok.RequiredArgsConstructor;

import java.awt.Color;

import static de.schafunschaf.voidtec.util.ComparisonTools.isNull;

@RequiredArgsConstructor
public class ColorPickerDialog implements CustomDialogDelegate {

    private final InteractionDialogAPI dialog;
    private final PickableColor pickableColor;
    private final EveryFrameScript onCloseAction;
    private TextFieldAPI fieldRed = null;
    private TextFieldAPI fieldGreen = null;
    private TextFieldAPI fieldBlue = null;
    private TextFieldAPI fieldAlpha = null;

    @Override
    public void createCustomDialog(CustomPanelAPI panel) {
        Color currentColor = pickableColor.getCurrentColor();

        TooltipMakerAPI uiElement = panel.createUIElement(200, 70, false);
        uiElement.setParaFont(Fonts.INSIGNIA_LARGE);
        uiElement.addPara("Pick a new color", 0f).setAlignment(Alignment.MID);
        uiElement.getPrev().getPosition().setYAlignOffset(-5f);
        UIUtils.addVerticalSeparator(uiElement, 200, 1, Misc.getBasePlayerColor()).getPosition().setXAlignOffset(-5f).setYAlignOffset(-3f);
        uiElement.addSpacer(0f).getPosition().setXAlignOffset(-5f).setYAlignOffset(5f);
        fieldRed = addColorField("Red (0-255)", currentColor.getRed(), Color.RED, uiElement);
        fieldGreen = addColorField("Green (0-255)", currentColor.getGreen(), Color.GREEN, uiElement);
        fieldBlue = addColorField("Blue (0-255)", currentColor.getBlue(), Color.BLUE, uiElement);
        fieldAlpha = addColorField("Alpha (0-255)", currentColor.getAlpha(), Color.WHITE, uiElement);
        uiElement.addSpacer(10f);
        panel.addUIElement(uiElement);
    }

    private TextFieldAPI addColorField(String text, int value, Color color, TooltipMakerAPI uiElement) {
        CustomPanelAPI panel = Global.getSettings().createCustom(180, 30, null);
        TooltipMakerAPI fieldElement = panel.createUIElement(180, 30, false);
        TextFieldAPI field = fieldElement.addTextField(60, 10f);
        field.setColor(color);
        field.setText(String.valueOf(value));
        field.setUndoOnEscape(true);
        field.setMidAlignment();
        field.setBorderColor(color);
        field.setMaxChars(3);
        UIComponentAPI prev = fieldElement.getPrev();
        fieldElement.setParaFont(Fonts.INSIGNIA_LARGE);
        fieldElement.addPara(text, color, 0f).getPosition().rightOfMid(prev, 10f).setYAlignOffset(-1f);

        panel.addUIElement(fieldElement).inTL(0, 0);
        uiElement.addCustom(panel, 3f);
        return field;
    }

    @Override
    public boolean hasCancelButton() {
        return false;
    }

    @Override
    public String getConfirmText() {
        return "Set Color";
    }

    @Override
    public String getCancelText() {
        return null;
    }

    @Override
    public void customDialogConfirm() {
        applyColor();
        closeDialog();
    }

    private void applyColor() {
        Color color = generateColor();
        pickableColor.setColor(color);
    }

    public Color generateColor() {
        int redValue = getColorValue(fieldRed);
        int greenValue = getColorValue(fieldGreen);
        int blueValue = getColorValue(fieldBlue);
        int alphaValue = getColorValue(fieldAlpha);

        return new Color(redValue, greenValue, blueValue, alphaValue);
    }

    private int getColorValue(TextFieldAPI textField) {
        int parsedValue = 0;
        try {
            parsedValue = Integer.parseInt(textField.getText());

        } catch (NumberFormatException exception) {
            Global.getLogger(ColorPickerDialog.class).error(exception);
        }

        return Math.max(Math.min(parsedValue, 255), 0);
    }

    @Override
    public void customDialogCancel() {
        closeDialog();
    }

    @Override
    public CustomUIPanelPlugin getCustomPanelPlugin() {
        return new ColorPickerPanelPlugin(this);
    }

    private void closeDialog() {
        if (!isNull(onCloseAction)) {
            Global.getSector().addTransientScript(onCloseAction);
        }

        dialog.dismiss();
    }
}
