package ch.bfh.ti.project1.RPiHSM.GUI.stages;

import ch.bfh.ti.project1.RPiHSM.API.KeyStatus;
import ch.bfh.ti.project1.RPiHSM.API.SerialHelperI;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.naming.OperationNotSupportedException;

/**
 * <h1>PromoteDemoteCommandStage</h1>
 * An implementation of {@link AbstractStage}. This stage allows the user to promote or demote a key by using the {@link KeyStatus} class.
 */
public class PromoteDemoteCommandStage extends AbstractStage {
    private Button executeButton;
    private Label keySetLabel, versionLabel, actionLabel;
    private TextField keySetTextField, versionTextField;
    private ComboBox<String> actionComboBox;

    /**
     * Generates all the graphical elements for the promote/demote key command.
     *
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     * @param userPath     user home directory on the Raspberry Pi
     */
    public PromoteDemoteCommandStage(SerialHelperI serialHelper, String userPath) {
        super(serialHelper, userPath);

        //creates the scene objects
        this.sceneTitle = new Label(Constants.PROMOTE_DEMOTE_COMMAND_TITLE);
        executeButton = new Button(Constants.PROMOTE);
        keySetLabel = new Label(Constants.KEY_SET);
        keySetTextField = new TextField();
        versionLabel = new Label(Constants.VERSION_LABEL);
        versionTextField = new TextField();
        actionComboBox = new ComboBox<>();
        actionLabel = new Label(Constants.ACTION_LABEL);

        actionComboBox.getItems().addAll(Constants.PROMOTE, Constants.DEMOTE);
        actionComboBox.getSelectionModel().selectFirst();

        executeButton.setDisable(true); //disabled at the beginning
        //On mouse clicked checks if the action to perform is promote or demote, then tries to perform the action and displays the success or error messages
        executeButton.setOnMouseClicked(e -> {
            KeyStatus ks = new KeyStatus(serialHelper, userPath, keySetTextField.getText(), Integer.parseInt(versionTextField.getText()));
            if (actionComboBox.getValue().equals(Constants.PROMOTE)) { //Action: promote
                try {
                    if (ks.promote()){
                    	clearElements();
                    	success(Constants.PROMOTE_SUCCESS);
                    }
                    else{
                    	error(Constants.PROMOTE_NOT_SUCCESS);
                    }
                } catch (OperationNotSupportedException e1) {
                    error(Constants.PROMOTE_NOT_SUCCESS);
                }
            } else { //Action: demote
                try {
                    if (ks.demote()){
                    	clearElements();
                    	success(Constants.DEMOTE_SUCCESS);
                    }
                    else{
                    	error(Constants.DEMOTE_NOT_SUCCESS);
                    }
                } catch (OperationNotSupportedException e1) {
                    error(Constants.DEMOTE_NOT_SUCCESS);
                }
            }
        });

        //disables the execute button if the key set does not exist of if the key version is not a number
        versionTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            executeButton.disableProperty().bind(Bindings
                    .when((new SimpleBooleanProperty(
                            versionTextField.getText().matches(Constants.NUMBER_FIELD_PATTERN)))
                            .and(keySetTextField.textProperty().length().greaterThan(2)))
                    .then(false)
                    .otherwise(true).or(messages.textProperty().isEqualTo(Constants.KEYSET_EXISTS).not()));
        });

        //on value changed, changes the text of execute button (promote or demote key)
        actionComboBox.setOnAction(e -> executeButton.setText(actionComboBox.getSelectionModel().getSelectedItem() + " key"));

        //disables version textfield and action combo box if the key set does not exist
        versionTextField.disableProperty().bind(messages.textProperty().isEqualTo(Constants.KEYSET_EXISTS).not());
        actionComboBox.disableProperty().bind(messages.textProperty().isEqualTo(Constants.KEYSET_EXISTS).not());

        //on focus out checks if the key set exists
        keySetTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                this.checkKeyExistence(keySetTextField.getText());
            }
        });

        //places the objects in the Grid
        addHeader(true);
        grid.setGridLinesVisible(false);
        grid.add(actionLabel, 0, 0);
        grid.add(actionComboBox, 1, 0);
        grid.add(keySetLabel, 0, 1);
        grid.add(keySetTextField, 1, 1);
        grid.add(versionLabel, 0, 2);
        grid.add(versionTextField, 1, 2);
        grid.add(executeButton, 1, 4);
    }
    
    /**
     * Resets the elements to their default values
     */
    private void clearElements() {
        keySetTextField.clear();
        versionTextField.clear();
        actionComboBox.getSelectionModel().select(Constants.PROMOTE);
        executeButton.setText(Constants.PROMOTE + " key");
    }
}