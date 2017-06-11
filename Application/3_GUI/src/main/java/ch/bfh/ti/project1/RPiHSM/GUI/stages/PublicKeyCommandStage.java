package ch.bfh.ti.project1.RPiHSM.GUI.stages;

import ch.bfh.ti.project1.RPiHSM.API.Exception.KeySetIsEmptyException;
import ch.bfh.ti.project1.RPiHSM.API.Exception.KeySetNotAsymmetricException;
import ch.bfh.ti.project1.RPiHSM.API.PublicKey;
import ch.bfh.ti.project1.RPiHSM.API.SerialHelperI;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * <h1>PublicKeyCommandStage</h1>
 * An implementation of {@link AbstractStage}. This stage allows the user to generate the public key by using the {@link PublicKey} class.
 */
public class PublicKeyCommandStage extends AbstractStage {
    private Button executeButton, directoryChooserButton;
    private Label keySetLabel;
    private TextField keySetTextField;
    private DirectoryChooser destinationDirectoryChooser;
    private File destinationFolder;


    /**
     * Generates all the graphical elements for the public key generation command.
     *
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     * @param userPath     user home directory on the Raspberry Pi
     */
    public PublicKeyCommandStage(SerialHelperI serialHelper, String userPath) {
        super(serialHelper, userPath);

        //creates the scene objects
        this.sceneTitle = new Label(b.getString("PUBLIC_KEY_COMMAND_TITLE"));
        executeButton = new Button(b.getString("BUTTON_TEXT_PUBLICKEY"));
        directoryChooserButton = new Button(b.getString("DIRECTORY_CHOOSER_SELECT_DESTINATION"));
        destinationDirectoryChooser = new DirectoryChooser();
        destinationFolder = new File("");
        keySetLabel = new Label(b.getString("KEY_SET"));
        keySetTextField = new TextField();

        //disables the file chooser if the key set does not exist
        directoryChooserButton.disableProperty().bind(messages.textProperty().isEqualTo(b.getString("KEYSET_EXISTS")).not());
        //disables the execute button if no file has been selected
        executeButton.disableProperty().bind(messages.textProperty().isEqualTo(b.getString("FILE_CHOOSER_DESTINATION_SELECTED")).not());

        //On mouse click, deletes all the files of the destination folder and tries to get all public keys of the given key set
        executeButton.setOnMouseClicked(e -> {
            //delete all files of the destination folder
            File[] keysToDelete = destinationFolder.listFiles();
            for (int i = 0; i < keysToDelete.length; i++) {
                keysToDelete[i].delete();
            }

            PublicKey pk = new PublicKey(serialHelper, userPath, keySetTextField.getText(), destinationFolder.getAbsolutePath());

            try {
                if (pk.generate()) {
                    clearElements();
                    success(b.getString("PUBLIC_KEY_SUCCESS"));
                } else {
                    error(b.getString("PUBLIC_KEY_NOT_SUCCESS"));
                }
            } catch (OperationNotSupportedException e1) {
                error(b.getString("UNSUPPORTED_OPERATION"));
            } catch (KeySetIsEmptyException keySetIsEmpty) {
                error(b.getString("KEY_SET_IS_EMPTY"));
            } catch (FileNotFoundException e1) {
                error(b.getString("FILE_NOT_FOUND"));
            } catch (KeySetNotAsymmetricException e1) {
                error(b.getString("KEY_SET_NOT_ASYMMETRIC"));
            }
        });

        //On mouse clicked, sets the destination directory as the directory selected with the DirectoryChooser
        directoryChooserButton.setOnMouseClicked(e -> {
            File temp = destinationDirectoryChooser.showDialog(this);
            if (temp != null) {
                destinationFolder = temp;
                success(b.getString("FILE_CHOOSER_DESTINATION_SELECTED"));
            } else {
                error(b.getString("FILE_CHOOSER_DESTINATION_NOT_SELECTED"));
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
        grid.add(keySetLabel, 0, 0);
        grid.add(keySetTextField, 1, 0);
        grid.add(directoryChooserButton, 1, 1);
        grid.add(executeButton, 1, 2);
    }

    /**
     * Resets the elements to their default values
     */
    private void clearElements() {
        keySetTextField.clear();
    }
}
