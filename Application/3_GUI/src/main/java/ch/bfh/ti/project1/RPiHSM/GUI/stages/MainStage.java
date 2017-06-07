package ch.bfh.ti.project1.RPiHSM.GUI.stages;

import ch.bfh.ti.project1.RPiHSM.API.SerialHelperI;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * <h1>MainStageMainStage</h1>
 * An implementation of {@link AbstractStage}. This is the main stage of the application where the are the link with all the supported operations.
 */
public class MainStage extends AbstractStage {

    private Button addKeyButton, createKeySetButton, deleteKeySetButton, decryptButton, encryptButton, promoteDemoteButton, pubKeyButton, revokeButton, signButton, verifyButton;

    /**
     * Generates all the graphical elements for the supported operations.
     *
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     * @param userPath     user home directory on the Raspberry Pi
     */
    public MainStage(SerialHelperI serialHelper, String userPath) {
        super(serialHelper, userPath);

        //creates the scene objects
        this.sceneTitle = new Label(Constants.MAIN_STAGE_TITLE);
        addKeyButton = new Button(Constants.MAIN_STAGE_BUTTON_ADDKEY);
        createKeySetButton = new Button(Constants.MAIN_STAGE_BUTTON_CREATEKEYSET);
        deleteKeySetButton = new Button(Constants.MAIN_STAGE_BUTTON_DELETEKEYSET);
        decryptButton = new Button(Constants.MAIN_STAGE_BUTTON_DECRYPT);
        encryptButton = new Button(Constants.MAIN_STAGE_BUTTON_ENCRYPT);
        promoteDemoteButton = new Button(Constants.MAIN_STAGE_BUTTON_PROMOTE_DEMOTE);
        pubKeyButton = new Button(Constants.MAIN_STAGE_BUTTON_PUBLICKEY);
        revokeButton = new Button(Constants.MAIN_STAGE_BUTTON_REVOKE);
        signButton = new Button(Constants.MAIN_STAGE_BUTTON_SIGN);
        verifyButton = new Button(Constants.MAIN_STAGE_BUTTON_VERIFY);
        
        //creates container titles
        Label titleContainer1 = new Label(Constants.MAIN_STAGE_CONTAINER1_TITLE);
        Label titleContainer2 = new Label(Constants.MAIN_STAGE_CONTAINER2_TITLE);
        Label titleContainer3 = new Label(Constants.MAIN_STAGE_CONTAINER3_TITLE);
        Label titleContainer4 = new Label(Constants.MAIN_STAGE_CONTAINER4_TITLE);
        
        //creates subcontainers
        HBox subContainer1 = new HBox();
        HBox subContainer2 = new HBox();
        HBox subContainer3 = new HBox();
        HBox subContainer4 = new HBox();
        
        //Places the buttons in the right sub container
        subContainer1.getChildren().addAll(createKeySetButton,deleteKeySetButton,pubKeyButton);
        subContainer2.getChildren().addAll(addKeyButton, promoteDemoteButton, revokeButton);
        subContainer3.getChildren().addAll(encryptButton, decryptButton);
        subContainer4.getChildren().addAll(signButton, verifyButton);
        
        //creates the 4 sections
        VBox container1 = new VBox(titleContainer1,subContainer1);
        VBox container2 = new VBox(titleContainer2,subContainer2);
        VBox container3 = new VBox(titleContainer3,subContainer3);
        VBox container4 = new VBox(titleContainer4,subContainer4);
        
        //Creates the main container
        VBox masterContainer = new VBox(container1,container2,container3,container4);
        
        //Sets the paddings
        masterContainer.setPadding(new Insets(Constants.PADDING_SMALL));
        container1.setPadding(new Insets(Constants.PADDING_SMALL));
        container2.setPadding(new Insets(Constants.PADDING_SMALL));
        container3.setPadding(new Insets(Constants.PADDING_SMALL));
        container4.setPadding(new Insets(Constants.PADDING_SMALL));
        
        //Sets the vertical spacing
        masterContainer.setSpacing(Constants.PADDING_SMALL);
        container1.setSpacing(10);
        container2.setSpacing(10);
        container3.setSpacing(10);
        container4.setSpacing(10);
        
        //sets the horizontal spacing
        subContainer1.setSpacing(Constants.PADDING_SMALL);
        subContainer2.setSpacing(Constants.PADDING_SMALL);
        subContainer3.setSpacing(Constants.PADDING_SMALL);
        subContainer4.setSpacing(Constants.PADDING_SMALL);
        
        //Sets the buttons size
        addKeyButton.setMinWidth(Constants.MAIN_MENU_BUTTON_SIZE);
        createKeySetButton.setMinWidth(Constants.MAIN_MENU_BUTTON_SIZE);
        deleteKeySetButton.setMinWidth(Constants.MAIN_MENU_BUTTON_SIZE);
        decryptButton.setMinWidth(Constants.MAIN_MENU_BUTTON_SIZE);
        encryptButton.setMinWidth(Constants.MAIN_MENU_BUTTON_SIZE);
        promoteDemoteButton.setMinWidth(Constants.MAIN_MENU_BUTTON_SIZE);
        pubKeyButton.setMinWidth(Constants.MAIN_MENU_BUTTON_SIZE);
        revokeButton.setMinWidth(Constants.MAIN_MENU_BUTTON_SIZE);
        signButton.setMinWidth(Constants.MAIN_MENU_BUTTON_SIZE);
        verifyButton.setMinWidth(Constants.MAIN_MENU_BUTTON_SIZE);
        
        //Sets the buttons style
        addKeyButton.setStyle(Constants.BUTTONS_STYLE);
        createKeySetButton.setStyle(Constants.BUTTONS_STYLE);
        deleteKeySetButton.setStyle(Constants.BUTTONS_STYLE);
        decryptButton.setStyle(Constants.BUTTONS_STYLE);
        encryptButton.setStyle(Constants.BUTTONS_STYLE);
        promoteDemoteButton.setStyle(Constants.BUTTONS_STYLE);
        pubKeyButton.setStyle(Constants.BUTTONS_STYLE);
        revokeButton.setStyle(Constants.BUTTONS_STYLE);
        signButton.setStyle(Constants.BUTTONS_STYLE);
        verifyButton.setStyle(Constants.BUTTONS_STYLE);
        
        //Sets the containers style
        container1.setStyle(Constants.CONTAINERS_STYLE);
        container2.setStyle(Constants.CONTAINERS_STYLE);
        container3.setStyle(Constants.CONTAINERS_STYLE);
        container4.setStyle(Constants.CONTAINERS_STYLE);

        //Sets the actions for each button
        //each button opens a new stage
        addKeyButton.setOnMouseClicked(e -> {
            this.hide();
            new AddKeyCommandStage(serialHelper, userPath);
        });
        createKeySetButton.setOnMouseClicked(e -> {
            this.hide();
            new CreateKeySetCommandStage(serialHelper, userPath);
        });
        deleteKeySetButton.setOnMouseClicked(e -> {
            this.hide();
            new DeleteKeySetCommandStage(serialHelper, userPath);
        });
        encryptButton.setOnMouseClicked(e -> {
            this.hide();
            new EncryptDecryptCommandStage(serialHelper, userPath, true);
        });
        decryptButton.setOnMouseClicked(e -> {
            this.hide();
            new EncryptDecryptCommandStage(serialHelper, userPath, false);
        });
        promoteDemoteButton.setOnMouseClicked(e -> {
            this.hide();
            new PromoteDemoteCommandStage(serialHelper, userPath);
        });
        pubKeyButton.setOnMouseClicked(e -> {
            this.hide();
            new PublicKeyCommandStage(serialHelper, userPath);
        });
        revokeButton.setOnMouseClicked(e -> {
            this.hide();
            new RevokeCommandStage(serialHelper, userPath);
        });
        signButton.setOnMouseClicked(e -> {
            this.hide();
            new SignCommandStage(serialHelper, userPath);
        });
        verifyButton.setOnMouseClicked(e -> {
            this.hide();
            new VerifyCommandStage(serialHelper, userPath);
        });
        
        //creates the header and adds the master container to the stage grid
        addHeader(false);
        grid.add(masterContainer, 0, 0);
    }


}
