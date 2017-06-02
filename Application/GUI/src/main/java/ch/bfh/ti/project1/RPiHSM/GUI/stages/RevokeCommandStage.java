package ch.bfh.ti.project1.RPiHSM.GUI.stages;

import ch.bfh.ti.project1.RPiHSM.API.KeyStatus;
import ch.bfh.ti.project1.RPiHSM.API.SerialHelperI;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.naming.OperationNotSupportedException;

/**
 * <h1>RevokeCommandStage</h1>
 * An implementation of {@link AbstractStage}. This stage allows the user to revoke a key by using the {@link KeyStatus} class.
 */
public class RevokeCommandStage extends AbstractStage {
    private Button executeButton;
    private Label keySetLabel, versionLabel;
    private TextField keySetTextField, versionTextField;

    /**
     * Generates all the graphical elements for the revoke key command.
     *
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     * @param userPath     user home directory on the Raspberry Pi
     */
    public RevokeCommandStage(SerialHelperI serialHelper, String userPath) {
        super(serialHelper, userPath);

        //creates the scene objects
        this.sceneTitle = new Label(Constants.REVOKE_COMMAND_TITLE);
        executeButton = new Button(Constants.BUTTON_TEXT_REVOKE);
        keySetLabel = new Label(Constants.KEY_SET);
        keySetTextField = new TextField();
        versionLabel = new Label(Constants.VERSION_LABEL);
        versionTextField = new TextField();
        executeButton.setDisable(true); //disabled at begin

        //On mouse click, tries to revoke the given version key in the given key set and displays the success or error messages
        executeButton.setOnMouseClicked(e -> {
            KeyStatus ks = new KeyStatus(serialHelper, userPath, keySetTextField.getText(), Integer.parseInt(versionTextField.getText()));
            try {
                if (ks.revoke()){
                	clearElements();
                	success(Constants.REVOKE_SUCCESS);
                }
                else{
                	error(Constants.REVOKE_NOT_SUCCESS);
                }
            } catch (OperationNotSupportedException e1) {
                error(Constants.REVOKE_NOT_SUCCESS);
            }
        });

        //disables the version textfield if the key set does not exist
        versionTextField.disableProperty().bind(messages.textProperty().isEqualTo(Constants.KEYSET_EXISTS).not());

        //disables the execute button if the key set does not exist of if the key version is not a number
        versionTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            executeButton.disableProperty().bind(Bindings
                    .when((new SimpleBooleanProperty(
                            versionTextField.getText().matches(Constants.NUMBER_FIELD_PATTERN)))
                            .and(keySetTextField.textProperty().length().greaterThan(2)))
                    .then(false)
                    .otherwise(true).or(messages.textProperty().isEqualTo(Constants.KEYSET_EXISTS).not()));
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
        grid.add(versionLabel, 0, 1);
        grid.add(versionTextField, 1, 1);
        grid.add(executeButton, 1, 2);
    }
    
    /**
     * Resets the elements to their default values
     */
    private void clearElements() {
        keySetTextField.clear();
        versionTextField.clear();
    }
}
