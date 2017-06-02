package ch.bfh.ti.project1.RPiHSM.GUI.stages;

import ch.bfh.ti.project1.RPiHSM.API.SerialHelperI;
import ch.bfh.ti.project1.RPiHSM.API.Sign;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <h1>SignCommandStage</h1>
 * An implementation of {@link AbstractStage}. This stage allows the user to sign a file by using the {@link Sign} class.
 */
public class SignCommandStage extends AbstractStage {
    private Button executeButton, fileChooserButton;
    private Label keySetLabel, fileLabel, fileNameLabel;
    private TextField keySetTextField;
    private FileChooser filechooser;
    private File originalFile, signatureFile;

    /**
     * Generates all the graphical elements for the sign command.
     *
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     * @param userPath     user home directory on the Raspberry Pi
     */
    public SignCommandStage(SerialHelperI serialHelper, String userPath) {
        super(serialHelper, userPath);

        //creates the scene objects
        this.sceneTitle = new Label(Constants.SIGN_COMMAND_TITLE);
        executeButton = new Button(Constants.BUTTON_TEXT_SIGN);
        fileChooserButton = new Button(Constants.FILE_CHOOSER_BUTTON_SIGN);
        keySetLabel = new Label(Constants.KEY_SET);
        keySetTextField = new TextField();
        fileLabel = new Label(Constants.FILE);
        filechooser = new FileChooser();
        fileNameLabel = new Label();

        //On mouse click, copies the selected file in the same directory, then sends the file to be signed.
        //The success or error messages are displayed
        executeButton.setOnMouseClicked(e -> {
            int index = originalFile.getAbsolutePath().lastIndexOf('.');
            signatureFile = new File(originalFile.getAbsolutePath().substring(0, index) + "-signature.bin");
            try {
                FileUtils.copyFile(originalFile, signatureFile); //file copy
            } catch (IOException e2) {
                error(Constants.FILE_COPY_ERROR);
            }

            Sign s = new Sign(serialHelper, userPath, keySetTextField.getText(), signatureFile.getAbsolutePath());
            try {
                if (s.sign()){
                	clearElements();
                	success(Constants.SIGN_SUCCESS); //tries to sign
                }
                else{
                	error(Constants.SIGN_NOT_SUCCESS);
                }
            } catch (OperationNotSupportedException | FileNotFoundException e1) {
                error(Constants.SIGN_NOT_SUCCESS);
            }
        });

        //disables the file chooser if the key set does not exist
        fileChooserButton.disableProperty().bind(messages.textProperty().isEqualTo(Constants.KEYSET_EXISTS).not());
        //disables the execute button if no file to sign has been selected
        executeButton.disableProperty().bind(messages.textProperty().isEqualTo(Constants.FILE_CHOOSER_FILE_CHOSEN).not());

        //On mouse clicked, sets the file to sign as the file selected with the file chooser and displays that a file has been selected
        fileChooserButton.setOnMouseClicked(e -> {
            try {
                originalFile = filechooser.showOpenDialog(this);
                fileNameLabel.setText(originalFile.getAbsolutePath());
                success(Constants.FILE_CHOOSER_FILE_CHOSEN);
            } catch (Exception e2) {
                fileNameLabel.setText(Constants.FILE_CHOOSER_NO_FILE_CHOSEN);
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
