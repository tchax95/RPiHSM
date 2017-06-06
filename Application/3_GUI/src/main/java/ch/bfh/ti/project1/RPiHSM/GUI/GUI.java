package ch.bfh.ti.project1.RPiHSM.GUI;

import ch.bfh.ti.project1.RPiHSM.GUI.stages.Constants;
import ch.bfh.ti.project1.RPiHSM.GUI.stages.ErrorStage;
import ch.bfh.ti.project1.RPiHSM.GUI.stages.LoginStage;
import ch.bfh.ti.project1.RPiHSM.API.SerialHelper;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * <h1>GUI</h1>
 * Graphical application that use the RPiHSM-API.
 *
 * @author Noli Manzoni, Sandro Tiago Carlao
 * @version 0.1
 * @since 13.04.2017
 */
public class GUI extends Application {
    /**
     * Start point of the GUI application.
     * An instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI} is created. If errors occurs, the error message is displayed.
     * If the creation of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI} is successful the {@link LoginStage} is opened.
     *
     * @param primaryStage the primary stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        SerialHelper serialHelper;

        try {
            serialHelper = new SerialHelper();
            new LoginStage(serialHelper); //creates the first stage
        } catch (PortInUseException | UnsupportedCommOperationException e) {
            new ErrorStage(Constants.PORT_IN_USE); //if the port is in use
        } catch (Exception e) {
            new ErrorStage(Constants.PORT_NOT_CONNECTED); //if the port is not connected
        }
    }

    /**
     * Used to start the GUI by calling the {@link Application#launch(String...)}.
     *
     * @param args no arguments are need for this application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
