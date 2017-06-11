package ch.bfh.ti.project1.RPiHSM.GUI.stages;

import ch.bfh.ti.project1.RPiHSM.API.EncryptDecrypt;
import ch.bfh.ti.project1.RPiHSM.API.SerialHelperI;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * <h1>EncryptDecryptCommandStage</h1>
 * An implementation of {@link AbstractStage}. This stage allows the user to encrypt or decrypt a file by using the {@link EncryptDecrypt} class.
 */
public class EncryptDecryptCommandStage extends AbstractStage {
    private Button executeButton, fileChooserButton;
    private Label keySetLabel, fileLabel, fileNameLabel;
    private TextField keySetTextField;
    private FileChooser filechooser;
    private File file;

    /**
     * Generates all the graphical elements for the encrypt or decrypt command.
     *
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     * @param userPath     user home directory on the Raspberry Pi
     * @param encrypt      true to encrypt false to decrypt
     */
    public EncryptDecryptCommandStage(SerialHelperI serialHelper, String userPath, boolean encrypt) {
        super(serialHelper, userPath);

        //creates the scene objects
        if (encrypt) sceneTitle = new Label(b.getString("ENCRYPT_COMMAND_TITLE"));
        else sceneTitle = new Label(b.getString("DECRYPT_COMMAND_TITLE"));
        keySetLabel = new Label(b.getString("KEY_SET"));
        keySetTextField = new TextField();
        fileLabel = new Label(b.getString("FILE"));
        filechooser = new FileChooser();
        fileNameLabel = new Label();
        executeButton = new Button(sceneTitle.getText().contains(b.getString("ENCRYPT_COMMAND_TITLE")) ? b.getString("BUTTON_TEXT_ENCRYPT") : b.getString("BUTTON_TEXT_DECRYPT")); //Choices text for the execute button
        fileChooserButton = new Button(sceneTitle.getText().contains(b.getString("ENCRYPT_COMMAND_TITLE")) ? b.getString("FILE_CHOOSER_BUTTON_ENCRYPT") : b.getString("FILE_CHOOSER_BUTTON_DECRYPT")); //Choices text for the filechooser button

        //On mouse clicked checks, using the stage title,if the file needs to be encrypted or decrypted, then tries to perform the action and displays the success or error messages
        executeButton.setOnMouseClicked(e1 -> {
            if (file == null) { //checks if a file has been selected
                error(b.getString("FILE_CHOOSER_NO_FILE_CHOSEN"));
            } else {
                EncryptDecrypt ed = new EncryptDecrypt(serialHelper, userPath, keySetTextField.getText(), file.getAbsolutePath());

                if (sceneTitle.getText().equals(b.getString("ENCRYPT_COMMAND_TITLE"))) {
                    try { //begin encrypt
                        if (ed.encrypt()) {
                            clearElements();
                            success(b.getString("ENCRYPT_SUCCESS"));
                        } else {
                            error(b.getString("ENCRYPT_NOT_SUCCESS"));
                        }
                    } catch (OperationNotSupportedException e) {
                        error(b.getString("UNSUPPORTED_OPERATION"));
                    } catch (FileNotFoundException e) {
                        error(b.getString("FILE_NOT_FOUND"));
                    }
                } else { //begin decrypt
                    try {
                        if (ed.decrypt()) {
                        	clearElements();
                            success(b.getString("DECRYPT_SUCCESS"));
                        } else {
                            error(b.getString("DECRYPT_NOT_SUCCESS"));
                        }
                    } catch (OperationNotSupportedException e) {
                        error(b.getString("UNSUPPORTED_OPERATION"));
                    } catch (FileNotFoundException e) {
                        error(b.getString("FILE_NOT_FOUND"));
                    }
                }
            }
        });

        //On mouse clicked, sets the file to encrypt/decrypt as the file selected with the file chooser
        fileChooserButton.setOnMouseClicked(e -> {
            try {
                file = filechooser.showOpenDialog(this);
                fileNameLabel.setText(file.getAbsolutePath());
                success(b.getString("FILE_CHOOSER_FILE_CHOSEN"));
            } catch (Exception e2) {
                fileNameLabel.setText(b.getString("FILE_CHOOSER_NO_FILE_CHOSEN"));
            }
        });

        //disables the file chooser if the key set does not exist
        fileChooserButton.disableProperty().bind(messages.textProperty().isEqualTo(b.getString("KEYSET_EXISTS")).not());
        //disables the execute button if no file has been selected using the file chooser
        executeButton.disableProperty().bind(messages.textProperty().isEqualTo(b.getString("FILE_CHOOSER_FILE_CHOSEN")).not());

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
        grid.add(fileLabel, 0, 2);
        grid.add(fileChooserButton, 1, 2);
        grid.add(fileNameLabel, 1, 3);
        grid.add(executeButton, 1, 4);
    }
    
    /**
     * Resets the elements to their default values
     */
    private void clearElements() {
        keySetTextField.clear();
        fileNameLabel.setText(null);
    }
}
