package com.ds.darknesschat.pages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.DeveloperLabel;
import com.ds.darknesschat.additionalNodes.SettingsOptionButton;
import com.ds.darknesschat.additionalNodes.Tile;
import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.eventListeners.IOnAction;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

import static com.ds.darknesschat.Constants.DEFAULT_BACKGROUND_PATH;
import static com.ds.darknesschat.Constants.DEFAULT_BACKGROUND_VALUE;

public abstract class Page {
    private final Page prevoiusPage;
    private final VBox contentVbox;
    private final String title;
    private Tile tile;
    private boolean isOpen;
    private final boolean createStandardTile;
    private User user;

    protected Page(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile) {
        this.prevoiusPage = prevoiusPage;
        this.contentVbox = contentVbox;
        this.title = title;
        this.createStandardTile = createStandardTile;
    }

    public Page(Page prevoiusPage, VBox contentVbox, String title, boolean createStandardTile, User user) {
        this.prevoiusPage = prevoiusPage;
        this.contentVbox = contentVbox;
        this.title = title;
        this.createStandardTile = createStandardTile;
        this.user = user;
    }

    public void applyDefaultPagePaddings(){
        getContentVbox().setPadding(new Insets(0d, 200d, 0d, 200d));
        Log.info("Default padding to page were applied!");
    }

    public void deleteDefaultPagePaddings(){
        getContentVbox().setSpacing(0d);
        getContentVbox().setPadding(new Insets(0d, 0d, 0d, 0d));
        Log.info("Default padding to page were deleted!");
    }

    private void createTile() {
        try {
            tile = new Tile(Tile.DEFAULT_WIDTH, Tile.DEFAULT_HEIGHT, Constants.TILE_COLOR.getAlpha());
            tile.animate(getUser() != null ? getUser().getId(): Constants.IGNORE_USER_AGREEMENT);

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            VBox.setVgrow(vBox, Priority.ALWAYS);
            vBox.getChildren().add(tile);

            addNodeToPage(vBox);
            Log.info("Tile " + tile + " was created for page " + getTitle());
        }catch (Exception e){
            Log.error(e);
        }
    }

    public void reopen(){
        close();
        open();
    }

    public void open(){
        if(prevoiusPage != null)
            prevoiusPage.close();

        if(isCreateStandardTile())
            createTile();

        onOpen();
        isOpen = true;

        Log.info("Page " + getTitle() + " was opened");
    }

    public void loadDefaultTileSettings(){
        if(getTile() != null) {
            getTile().setSpacing(10d);
            getTile().addTitle(getTitle());

            Log.info("Loaded default settings for tile");
        }else{
            Log.info("Tile in window " + getTitle() + " is null. Default settings for tile could not be applied");
        }
    }

    public void updateBackground(long userId) {
        try {
            String backgroundValueInDatabase = DatabaseService.getValue(DatabaseConstants.BACKGROUND_PATH_ROW, userId);

            assert backgroundValueInDatabase != null;
            if (!backgroundValueInDatabase.equals(DEFAULT_BACKGROUND_VALUE) & new File(backgroundValueInDatabase).exists())
                Utils.changeMainBackground(getContentVbox(), new FileInputStream(backgroundValueInDatabase));
            else
                Utils.changeMainBackground(getContentVbox(), Main.class.getResourceAsStream(DEFAULT_BACKGROUND_PATH));

        }catch (Exception e){
            Log.error(e);
        }
    }


    public void createDeveloperLabelInBottom(){
        VBox developerLabelVbox = new DeveloperLabel().wrapInVbox();
        VBox.setMargin(developerLabelVbox, new Insets(20d));
        addNodeToPage(developerLabelVbox);
    }

    public void close(){
        onClose();
        contentVbox.getChildren().clear();
        isOpen = false;
    }

    public void goToPreviousPage(){
        getPrevoiusPage().open();
    }

    public void addNodeToPage(Node node){
        contentVbox.getChildren().add(node);
    }

    public void addNodeToTile(Node node){
        if(tile != null)
            tile.getChildren().add(node);
        else
            createTile();
    }

    public Stage getStage(){
        return (Stage) getContentVbox().getScene().getWindow();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCreateStandardTile() {
        return createStandardTile;
    }

    public Page getPrevoiusPage() {
        return prevoiusPage;
    }

    public Tile getTile() {
        return tile;
    }

    public VBox getContentVbox() {
        return contentVbox;
    }

    public User getUser() {
        return user;
    }

    public void onClose(){
        Log.info("Page " + getTitle() + " was closed");
    }

    public abstract void onOpen();
}
