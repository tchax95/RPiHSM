package ch.bfh.ti.project1.RPiHSM.GUI.stages;


import ch.bfh.ti.project1.RPiHSM.API.SerialHelperI;
import ch.bfh.ti.project1.RPiHSM.API.Verify;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * <h1>VerifyCommandStage</h1>
 * An implementation of {@link AbstractStage}. This stage allows the user to verify a signature by using the {@link Verify} class.
 */
public class VerifyCommandStage extends AbstractStage {
    private Button executeButton, signFileChooserButton, verifyFileChooserButton;
    private Label keySetLabel, signFileLabel, verifyFileLabel, signatureFilePathLabel, verifyFilePathLabel;
    private TextField keySetTextField;
    private FileChooser filechooser;
    private File signFile, verifyFile;

    /**
     * Generates all the graphical elements for the signature verification command.
     *
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     * @param userPath     user home directory on the Raspberry Pi
     */
    public VerifyCommandStage(SerialHelperI serialHelper, String userPath) {
        super(serialHelper, userPath);

        //creates the scene objects
        this.sceneTitle = new Label(b.getString("VERIFY_COMMAND_TITLE"));
        executeButton = new Button(b.getString("BUTTON_TEXT_VERIFY"));
        signFileChooserButton = new Button(b.getString("FILE_CHOOSER_BUTTON_SIGNED"));
        verifyFileChooserButton = new Button(b.getString("FILE_CHOOSER_BUTTON_VERIFY"));
        keySetLabel = new Label(b.getString("KEY_SET"));
        keySetTextField = new TextField();
        signFileLabel = new Label(b.getString("SIGN_FILE_LABEL"));
        verifyFileLabel = new Label(b.getString("VERIFY_FILE_LABEL"));
        filechooser = new FileChooser();
        signatureFilePathLabel = new Label();
        verifyFilePathLabel = new Label();

        //disables the sign file chooser if the key set does not exist
        signFileChooserButton.disableProperty().bind(messages.textProperty().isEqualTo(b.getString("KEYSET_EXISTS")).not());
        //disables the verify file chooser if the signature file has not been selected
        verifyFileChooserButton.disableProperty().bind(messages.textProperty().isEqualTo(b.getString("FILE_CHOOSER_SIGNATURE_CHOSEN")).not());
        //disables the execute button if the verify file has not been selected
        executeButton.disableProperty().bind(messages.textProperty().isEqualTo(b.getString("FILE_CHOOSER_FILE_AND_SIGNATURE_CHOSEN")).not());

        //On mouse clicked, sets the signature file as the file selected with the filechooser
        signFileChooserButton.setOnMouseClicked(e -> {
            try {
                signFile = filechooser.showOpenDialog(this);
                signatureFilePathLabel.setText(signFile.getAbsolutePath());
                if (messages.getText().equals(b.getString("FILE_CHOOSER_FILE_CHOSEN")))
                    success(b.getString("FILE_CHOOSER_FILE_AND_SIGNATURE_CHOSEN"));
                else success(b.getString("FILE_CHOOSER_SIGNATURE_CHOSEN"));
            } catch (Exception e2) {
                signatureFilePathLabel.setText(b.getString("FILE_CHOOSER_NO_FILE_CHOSEN"));
            }
        });

        //On mouse clicked, sets the verify file as the file selected with the filechooser
        verifyFileChooserButton.setOnMouseClicked(e -> {
            try {
                verifyFile = filechooser.showOpenDialog(this);
                verifyFilePathLabel.setText(verifyFile.getAbsolutePath());
                if (messages.getText().equals(b.getString("FILE_CHOOSER_SIGNATURE_CHOSEN")))
                    success(b.getString("FILE_CHOOSER_FILE_AND_SIGNATURE_CHOSEN"));
                else success(b.getString("FILE_CHOOSER_FILE_CHOSEN"));
            } catch (Exception e2) {
                verifyFilePathLabel.setText(b.getString("FILE_CHOOSER_NO_FILE_CHOSEN"));
            }
        });

        //On mouse click, tries to send booth files to the RPiHSM to verify the signature. Then displays the success or error messages
        executeButton.setOnMouseClicked(e -> {
            Verify v = new Verify(serialHelper, userPath, keySetTextField.getText(), verifyFile.getAbsolutePath(), signFile.getAbsolutePath());
            try {
                if (v.verify()) {
                    clearElements();
                    success(b.getString("VERIFY_SUCCESS"));
                } else {
                    error(b.getString("VERIFY_NOT_SUCCESS"));
                }
            } catch (OperationNotSupportedException e1) {
                error(b.getString("UNSUPPORTED_OPERATION"));
            } catch (FileNotFoundException e1) {
                error(b.getString("FILE_NOT_FOUND"));
            }
        });

        //on focus out checks if the key set exists
        keySetTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                this.checkKeyExistence(keySetTextField.getText());
            }
        });

        //places the objects in the Grid
        addHeader(true);
        grid.setGridLinesVisible(false);
        grid.add(keySetLabel, 0, 1);
        grid.add(keySetTextField, 1, 1);
        grid.add(signFileLabel, 0, 2);
        grid.add(signFileChooserButton, 1, 2);
        grid.add(signatureFilePathLabel, 1, 3);
        grid.add(verifyFileLabel, 0, 4);
        grid.add(verifyFileChooserButton, 1, 4);
        grid.add(verifyFilePathLabel, 1, 5);
        grid.add(executeButton, 1, 6);
    }

    /**
     * Resets the elements to their default values
     */
    private void clearElements() {
        keySetTextField.clear();
        signatureFilePathLabel.setText(null);
        verifyFilePathLabel.setText(null);
    }
}
