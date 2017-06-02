package ch.bfh.ti.project1.RPiHSM.GUI.stages;

import ch.bfh.ti.project1.RPiHSM.API.DeleteKeySet;
import ch.bfh.ti.project1.RPiHSM.API.SerialHelperI;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.naming.OperationNotSupportedException;

/**
 * <h1>DeleteKeySetCommandStage</h1>
 * An implementation of {@link AbstractStage}. This stage allows the user to delete a key set by using the {@link DeleteKeySet} class.
 */
public class DeleteKeySetCommandStage extends AbstractStage {
    private Button executeButton;
    private Label nameLabel;
    private TextField nameTextField;

    /**
     * Generates all the graphical elements for the delete key set command.
     *
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     * @param userPath     user home directory on the Raspberry Pi
     */
    public DeleteKeySetCommandStage(SerialHelperI serialHelper, String userPath) {
        super(serialHelper, userPath);

        //creates the scene objects
        this.sceneTitle = new Label(Constants.DELETE_KEYSET_COMMAND_TITLE);
        executeButton = new Button(Constants.BUTTON_TEXT_DELETEKEYSET);
        nameLabel = new Label(Constants.NAME);
        nameTextField = new TextField();

        //On mouse clicked, ties to delete a key set and displays the success or error messages
        executeButton.setOnMouseClicked(e1 -> {
            DeleteKeySet dk = new DeleteKeySet(serialHelper, userPath, nameTextField.getText());
            try {
                if (dk.delete()){
                	clearElements();
                	success(Constants.KEYSET_DELETION_SUCCESS);
                }
                else{
                	error(Constants.KEYSET_DELETION_NOT_SUCCESS);
                }
            } catch (OperationNotSupportedException e) {
                error(Constants.KEYSET_DELETION_NOT_SUCCESS);
            }
        });

        //disables the execute button if the length of the key set entered is smaller than 2 or if the key set does not exist
        executeButton.disableProperty().bind(nameTextField.textProperty().length().greaterThan(2).not().or(messages.textProperty().isEqualTo(Constants.KEYSET_EXISTS).not()));

        //on focus out checks if the key set exists
        nameTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkKeyExistence(nameTextField.getText());
            }
        });

        //places the objects in the Grid
        addHeader(true);
        grid.setGridLinesVisible(false);
        grid.add(nameLabel, 0, 0);
        grid.add(nameTextField, 1, 0);
        grid.add(executeButton, 1, 1);
    }
    
    /**
     * Resets the elements to their default values
     */
    private void clearElements() {
    	nameTextField.clear();
	}
}