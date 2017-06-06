package ch.bfh.ti.project1.RPiHSM.GUI.stages;

import ch.bfh.ti.project1.RPiHSM.API.SerialHelperI;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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

        //Sets the buttons width
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

        //places the objects in the Grid
        addHeader(false);
        grid.add(addKeyButton, 0, 1);
        grid.add(createKeySetButton, 1, 1);
        grid.add(deleteKeySetButton, 2, 1);
        grid.add(decryptButton, 0, 2);
        grid.add(encryptButton, 1, 2);
        grid.add(promoteDemoteButton, 2, 2);
        grid.add(pubKeyButton, 0, 3);
        grid.add(revokeButton, 1, 3);
        grid.add(signButton, 2, 3);
        grid.add(verifyButton, 1, 4);
        grid.setHgap(30);
        grid.setVgap(30);
    }


}
