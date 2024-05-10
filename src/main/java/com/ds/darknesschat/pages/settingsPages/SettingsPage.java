package com.ds.darknesschat.pages.settingsPages;

import com.ds.darknesschat.additionalNodes.AdditionalButton;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.interfaces.IOnAction;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import static com.ds.darknesschat.Constants.BLACK_COLOR;
import static com.ds.darknesschat.Constants.WHITE_COLOR;

public final class SettingsPage {
    public static void initBackButton(Page page) {
        try{
            AdditionalButton backButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.BACK), 408d, 41d, WHITE_COLOR, BLACK_COLOR,
                    page.getUser().getId());
            backButton.addAction(page::goToPreviousPage);
            VBox.setMargin(backButton, new Insets(100d, 40d, 30d, 40d));
            page.addNodeToTile(backButton);
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static void initResetButton(Page page, IOnAction onAction) {
        try{
            AdditionalButton resetAllButton = new AdditionalButton(StringGetterWithCurrentLanguage.getString(StringsConstants.RESET_ALL), 408d, 41d, new Color(109, 107, 208), WHITE_COLOR,
                    page.getUser().getId());
            resetAllButton.addAction(onAction);
            VBox.setMargin(resetAllButton, new Insets(10d, 0d, 0, 0d));

            page.addNodeToTile(resetAllButton);
        }catch (Exception e){
            Log.error(e);
        }
    }

}
