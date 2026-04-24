package tournamentSimTests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.TournamentModel;
import model.ViewTransitionModel;
import sprint1.RoundRobinTournament;
import views.MoveReciever;
import views.MoveViewController;

@ExtendWith(ApplicationExtension.class)
public class Test_MovesView
{
    TournamentModel model;
    MoveViewController cont;

    @Start
    private void start(Stage stage) throws IOException
    {
        model = new TournamentModel("localhost", "8082");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ViewTransitionModel.class
            .getResource("../views/MovesView.fxml"));
        BorderPane view = loader.load();
        cont = loader.getController();
        cont.setModel(model);
        MoveReciever.moveViewController = cont;

        stage.setScene(new Scene(view));
        stage.show();
    } 
 
    //default label text on start
    @Test
    void testTourneyLabelStart(FxRobot robot)
    {
        Assertions.assertThat(robot.lookup("#tournamentLabel")
            .queryAs(Label.class)).hasText("Tournament:");
    }

    //label shows tournament name when selected
    @Test
    void testTourneyLabelShowsName(FxRobot robot)
    {
    	Platform.runLater(() -> {
    		RoundRobinTournament t = new RoundRobinTournament();
    		t.name = "RoundRobinTournament0";
    		t.active = true;
            model.selectedTournament = t;
            cont.setModel(model);
    	});
        WaitForAsyncUtils.waitForFxEvents();
        

        Assertions.assertThat(robot.lookup("#tournamentLabel")
            .queryAs(Label.class)).hasText("Tournament: RoundRobinTournament0");
    }

    // updateMove appends to text area
    @Test
    void testUpdateMove(FxRobot robot)
    {
    	Platform.runLater(() -> cont.updateMove("RobotA picked Defect"));
        WaitForAsyncUtils.waitForFxEvents();
        

        Assertions.assertThat(robot.lookup("#liveMoveTextArea")
            .queryAs(TextArea.class)).hasText("RobotA picked Defect\n");
    }

    // multiple moves append in order
    @Test
    void testMultipleMoves(FxRobot robot)
    {

        cont.updateMove("RobotA picked Defect");
        cont.updateMove("RobotB picked Cooperate");
        
        TextArea ta = robot.lookup("#liveMoveTextArea").queryAs(TextArea.class);
        assertTrue(ta.getText().contains("RobotA picked Defect"));
        assertTrue(ta.getText().contains("RobotB picked Cooperate"));
    }

    //moveReciever passes move to controller
    @Test
    void testMoveReceiver(FxRobot robot)
    {
        MoveReciever receiver = new MoveReciever();
        receiver.receiveMove("SameBot picked Defect");
        WaitForAsyncUtils.waitForFxEvents();

        Assertions.assertThat(robot.lookup("#liveMoveTextArea")
            .queryAs(TextArea.class)).hasText("SameBot picked Defect\n");
    }

    //text area is empty on start
    @Test
    void testTextAreaEmptyOnStart(FxRobot robot)
    {
        Assertions.assertThat(robot.lookup("#liveMoveTextArea")
            .queryAs(TextArea.class)).hasText("");
    }

    //exit button clickable
    @Test
    void testExitButton(FxRobot robot)
    {
        assertNotNull(robot.lookup("#exitTourneyButton")
            .queryAs(javafx.scene.control.Button.class));
    }
}
