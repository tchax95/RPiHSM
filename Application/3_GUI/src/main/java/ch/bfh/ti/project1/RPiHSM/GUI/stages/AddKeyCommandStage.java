package ch.bfh.ti.project1.RPiHSM.GUI.stages;

import ch.bfh.ti.project1.RPiHSM.API.CreateKey;
import ch.bfh.ti.project1.RPiHSM.API.SerialHelperI;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.naming.OperationNotSupportedException;

/**
 * <h1>AddKeyCommandStage</h1>
 * An implementation of {@link AbstractStage}. This stage allows the user to add a new key by using the {@link CreateKey} class.
 */
public class AddKeyCommandStage extends AbstractStage {
    private Button executeButton;
    private Label keySetLabel, statusLabel, sizeLabel;
    private TextField keySetTextField;
    private ComboBox<String> statusComboBox;
    private TextField sizeTextField;

    /**
     * Generates all the graphical elements for the add key command.
     *
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     * @param userPath     user home directory on the Raspberry Pi
     */
    public AddKeyCommandStage(SerialHelperI serialHelper, String userPath) {
        super(serialHelper, userPath);

        //creates the scene objects
        this.sceneTitle = new Label(Constants.ADD_KEY_COMMAND_STAGE_TITLE);
        executeButton = new Button(Constants.BUTTON_TEXT_ADDKEY);
        keySetLabel = new Label(Constants.NAME);
        statusLabel = new Label(Constants.STATUS);
        sizeLabel = new Label(Constants.SIZE + "( 0 = default )");
        keySetTextField = new TextField();
        statusComboBox = new ComboBox<>();
        sizeTextField = new TextField("0");
        statusComboBox.getItems().addAll(Constants.PRIMARY, Constants.ACTIVE, Constants.INACTIVE);
        statusComboBox.getSelectionModel().selectFirst();

        //On mouse clicked, tries to create a key and displays the success or error messages
        executeButton.setOnMouseClicked(e1 -> {
            CreateKey ck = new CreateKey(serialHelper, userPath, keySetTextField.getText(), statusComboBox.getValue(), Integer.parseInt(sizeTextField.getText()));
            try {
                if (ck.create()) {
                	clearElements();
                    success(Constants.KEY_SUCCESS);
                } else {
                    error(Constants.KEY_NOT_SUCCESS);
                }
            } catch (OperationNotSupportedException e) {
                error(Constants.UNSUPPORTED_OPERATION);
            }
        });

        //bind to size text field disable property
        sizeTextField.disableProperty().bind(messages.textProperty().isEqualTo(Constants.KEYSET_EXISTS).not());
        executeButton.disableProperty().bind(sizeTextField.disabledProperty());
        statusComboBox.disableProperty().bind(sizeTextField.disabledProperty());

        //disables the execute button if the key size is not a number, is odd, if the key set does not exist or if length of the key set entered is smaller than 2
        sizeTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            executeButton.disableProperty().bind(Bindings
                    .when((new SimpleBooleanProperty(
                            sizeTextField.getText().matches(Constants.KEY_SIZES_FIELD_PATTERN) && Integer.parseInt(sizeTextField.getText()) % 2 == 0))
                            .and(keySetTextField.textProperty().length().greaterThan(2)))
                    .then(false)
                    .otherwise(true).or(sizeTextField.disabledProperty()));
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
        grid.add(statusLabel, 0, 2);
        grid.add(statusComboBox, 1, 2);
        grid.add(sizeLabel, 0, 3);
        grid.add(sizeTextField, 1, 3);
        grid.add(executeButton, 1, 4);
    }

    /**
     * Resets the elements to their default values
     */
    private void clearElements() {
    	keySetTextField.clear();
        statusComboBox.getSelectionModel().select(Constants.PRIMARY);
        sizeTextField.setText("0");
	}
}
