import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.controllers.AdminController;
import org.datafx.controller.flow.Flow;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.FlowView;
import org.junit.Test;
import org.testfx.api.FxRobotException;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;



public class AdminTest extends ApplicationTest {

    AdminController controller;
    Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent mainNode = FXMLLoader.load(getClass().getResource("/views/application.fxml"));
        scene = new Scene(mainNode);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.toFront();
    }

    @Test(expected = FxRobotException.class)
    public void clickOnBogusElement(){
        clickOn("#thisElementDoesntExist");
    }

    @Test(expected = FxRobotException.class)
    public void clickOnNormalElement(){
        clickOn("#zalogujButton");
    }

    @Test
    public void loggingWithWrongCredentialsWontAllowLogginIn(){
        clickOn("#zalogujButton");
        Label label = (Label) scene.lookup("#bladLogowaniaLabel");
        Assertions.assertThat(label).isVisible();
    }


}
