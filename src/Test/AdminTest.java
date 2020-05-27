import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.controllers.AdminController;
import org.datafx.controller.flow.Flow;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.FlowView;
import org.junit.Test;
import org.testfx.api.FxRobotException;
import org.testfx.framework.junit.ApplicationTest;



public class AdminTest extends ApplicationTest {

    AdminController controller;

    @Override
    public void start(Stage stage) throws Exception {
        Flow flow = new Flow(AdminController.class);

        // create a handler to initialize a view and a sceneRoot.
        FlowHandler handler = flow.createHandler();
        StackPane sceneRoot = handler.start();

        // retrieve the injected controller from the view.
        FlowView view = handler.getCurrentView();
        controller = (AdminController) view.getViewContext().getController();

        // attach the sceneRoot to stage.
        stage.setScene(new Scene(sceneRoot));
        stage.show();
    }

    @Test(expected = FxRobotException.class)
    public void clickOnBogusElement(){
        clickOn("#thisElementDoesntExist");
    }



}
