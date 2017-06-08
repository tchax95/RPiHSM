package ch.bfh.ti.project1.RPiHSM.GUI.stages;

import ch.bfh.ti.project1.RPiHSM.API.Exception.SerialPortException;
import ch.bfh.ti.project1.RPiHSM.API.KeyExistence;
import ch.bfh.ti.project1.RPiHSM.API.SerialHelperI;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.naming.OperationNotSupportedException;

/**
 * <h1>AbstractStage</h1>
 * The base stage of the application that have all the common elements.
 */
public abstract class AbstractStage extends Stage {

    private VBox pane;

    protected Label messages;
    protected GridPane grid;
    protected Label sceneTitle;
    protected String userPath;
    protected SerialHelperI serialHelper;

    /**
     * Generates all the common elements of the stages.
     *
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     * @param userPath     user home directory on the Raspberry Pi
     */
    public AbstractStage(SerialHelperI serialHelper, String userPath) {
        super();

        this.userPath = userPath;
        this.serialHelper = serialHelper;

        this.setTitle(Constants.TITLE); //Application title

        //Creates the Label for error and success messages
        messages = new Label();
        messages.setTextAlignment(TextAlignment.CENTER);
        messages.setPadding(new Insets(10));
        messages.prefWidth(Constants.WIDTH);

        //Creates the GridPane to place the objects on the stage
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(Constants.PADDING_SMALL);
        grid.setVgap(Constants.PADDING_SMALL);
        grid.setPadding(new Insets(Constants.PADDING_MEDIUM));
        

        //Creates the VBox
        pane = new VBox();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.getChildren().add(this.messages);
        pane.getChildren().add(grid);
        pane.setStyle(Constants.PANE_BACKGROUND);
        grid.setPrefWidth(Constants.WIDTH);

        //sets and shows the scene
        this.setScene(new Scene(pane, Constants.WIDTH, Constants.HEIGHT));
        this.show();

        this.setOnCloseRequest(e -> {
            if (serialHelper != null) {
                try {
                    serialHelper.closeConnection(); //closes the serial connection while closing the application
                } catch (SerialPortException e1) {
                    error(Constants.UNSUPPORTED_COM_OPERATION);
                }
            }
        });
    }


    /**
     * Generates the header elements of the application.
     *
     * @param backToMenu true if a button to go back in the menu must be displayed otherwise false
     */
    protected void addHeader(boolean backToMenu) {
        sceneTitle.setFont(Font.font(Constants.FONT, FontWeight.NORMAL, Constants.TITLE_FONT_SIZE));
        sceneTitle.setTextFill(Paint.valueOf(Constants.HEADER_TITLE_COLOR));
        ImageView logo = new ImageView(this.getClass().getResource(Constants.HEADER_IMAGE_PATH).toExternalForm());

        //Creates the GridPane for the header structure
        GridPane header = new GridPane();
        header.setMinSize(Constants.WIDTH, Constants.HEADER_HEIGHT);
        header.setPadding(new Insets(15, 12, 15, 12));
        header.setStyle(Constants.HEADER_BACKGROUND);
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        col1.setPercentWidth(25);
        col2.setPercentWidth(50);
        col3.setPercentWidth(23);

        header.add(logo, 0, 0);
        header.add(sceneTitle, 1, 0);
        header.getColumnConstraints().addAll(col1, col2, col3);

        GridPane.setHalignment(logo, HPos.LEFT);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);

        if (backToMenu) { //Creates the backToMenu button if needed
            Button menuButton = new Button(Constants.BACK_BUTTON_TEXT);
            menuButton.setPrefSize(Constants.BACK_BUTTON_WIDTH, Constants.BACK_BUTTON_HEIGHT);
            menuButton.setOnMouseClicked(e -> {
                this.hide();
                new MainStage(serialHelper, userPath);
            });
            menuButton.setStyle(Constants.BUTTONS_STYLE);
            header.add(menuButton, 2, 0);
            GridPane.setHalignment(menuButton, HPos.RIGHT);
        } else {
            header.add(new Label(""), 2, 0);
        }

        pane.getChildren().add(0, header); //adds the header on first position (on top)
    }

    /**
     * Shows an error in the {@link AbstractStage#messages} element.
     *
     * @param message to display
     */
    protected void error(String message) {
        messages.setTextFill(Color.FIREBRICK);
        messages.setText(message); //prints the error message in Firebrick (red)
    }

    /**
     * Shows a success message in the {@link AbstractStage#messages} element.
     *
     * @param message to display
     */
    protected void success(String message) {
        messages.setTextFill(Color.GREEN);
        messages.setText(message); //prints the success message in green
    }

    /**
     * Checks if a given key set exist using the {@link KeyExistence} class.
     *
     * @param keySet name to check
     */
    protected void checkKeyExistence(String keySet) {
        KeyExistence ke = new KeyExistence(serialHelper, userPath, keySet);
        try {
            if (ke.keyexist()) success(Constants.KEYSET_EXISTS); //checks if the key set exists
            else error(Constants.KEYSET_NOT_EXISTS);
        } catch (OperationNotSupportedException e) {
            error(Constants.UNSUPPORTED_OPERATION);
        }
    }


}
