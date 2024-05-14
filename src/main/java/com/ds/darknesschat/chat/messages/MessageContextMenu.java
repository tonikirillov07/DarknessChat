package com.ds.darknesschat.chat.messages;

import com.ds.darknesschat.utils.Animations;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

import java.awt.datatransfer.Transferable;

public class MessageContextMenu extends ContextMenu{
    private final Transferable transferable;
    private final Node node;
    private final Pane nodeParent;
    private final long userId;

    public MessageContextMenu(Transferable transferable, Node node, Pane nodeParent, long userId) {
        this.transferable = transferable;
        this.node = node;
        this.nodeParent = nodeParent;
        this.userId = userId;

        init();
    }

    private void init() {
        loadItems();
    }

    private void loadItems() {
        MenuItem copyMenuItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.COPY));
        MenuItem deleteMenuItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.DELETE_MESSAGE));

        copyMenuItem.setOnAction(actionEvent -> Utils.copyTransferable(transferable));
        deleteMenuItem.setOnAction(actionEvent -> nodeParent.getChildren().remove(node));

        getItems().addAll(copyMenuItem, deleteMenuItem);
    }
}
