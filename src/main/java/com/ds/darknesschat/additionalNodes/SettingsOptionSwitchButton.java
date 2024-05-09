package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.interfaces.IOnSwitch;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.List;

public class SettingsOptionSwitchButton extends SettingsOption {
    private final List<String> switchValues;
    private int currentValue;
    private Label valueLabel;

    public SettingsOptionSwitchButton(double width, double height, String text, List<String> switchValues, int startValue) {
        super(width, height);
        this.switchValues = switchValues;
        this.currentValue = startValue;
        
        createLabel(text, Color.WHITE, Pos.CENTER_LEFT);
        createSwitchValueLabel();
    }

    public void setOnClick(IOnSwitch onSwitch) {
        Utils.addActionToNode(this, () -> {
            int currentOptionId = currentValue;
            if((currentOptionId + 1) != switchValues.size()){
                currentOptionId++;
            }else
                currentOptionId = 0;

            currentValue = currentOptionId;
            valueLabel.setText(switchValues.get(currentValue));

            onSwitch.onSwitch(switchValues.get(currentValue));
        });
    }

    private void createSwitchValueLabel() {
        valueLabel = createLabel(switchValues.get(currentValue), Color.rgb(56, 108, 243), Pos.CENTER_RIGHT);
    }


}
