package com.ds.darknesschat.utils;

import com.ds.darknesschat.utils.eventListeners.IOnAction;
import com.ds.darknesschat.utils.log.Log;

import java.awt.*;

public class NotificationsSender {
    public static void send(String caption, String message, TrayIcon.MessageType messageType, IOnAction actionEvent){
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

            TrayIcon trayIcon = new TrayIcon(image);
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);

            trayIcon.displayMessage(caption, message, messageType);
            trayIcon.addActionListener(e -> actionEvent.onAction());
        }catch (Exception e){
            Log.error(e);
        }
    }
}
