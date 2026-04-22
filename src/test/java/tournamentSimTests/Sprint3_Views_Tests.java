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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.TournamentModel;
import sprint1.RoundRobinTournament;
import sprint1.Tournament;
import views.Main;
import views.MoveReciever;
import views.MoveViewController;
import views.TournamentsViewController;

@ExtendWith(ApplicationExtension.class)
class Sprint3_Views_Tests 
{
	TournamentModel model;
    MoveViewController moveController;
    TournamentsViewController tourneyController;

	@Start
	private void start(Stage stage) throws IOException
	{
		model = new TournamentModel("localhost", "9000");
		
		FXMLLoader tourneyLoader = new FXMLLoader();
		tourneyLoader.setLocation(Main.class.getResource("../views/TournamentsView.fxml"));
		BorderPane tourneyView = tourneyLoader.load();
		tourneyController = tourneyLoader.getController();
        tourneyController.setModel(model);
        
        
        FXMLLoader moveLoader = new FXMLLoader();
        moveLoader.setLocation(Main.class.getResource("../views/MovesView.fxml"));
        BorderPane moveView = moveLoader.load();
        moveController = moveLoader.getController();
        moveController.setModel(model);
        
        tourneyController.setMoveViewController(moveController);
        MoveReciever.moveViewController = moveController;

        Main.primaryStage = stage;
        Main.tournamentsView = tourneyView;
        Main.moveView = moveView;

        Scene s = new Scene(tourneyView);
        stage.setScene(s);
        stage.show();
	}
	
	//list should be empty before server connection
	@Test
    void testListEmptyStart(FxRobot robot)
    {
        ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
        Assertions.assertThat(list).isEmpty();
    }
	
	//ip field should be empty on start
	@Test
    void testIpEmptyStart(FxRobot robot)
    {
        Assertions.assertThat(robot.lookup("#ipField").queryAs(javafx.scene.control.TextField.class)).hasText("");
    }
	
	//port field should be empty on start
	@Test
    void testPortEmptyStart(FxRobot robot)
    {
        Assertions.assertThat(robot.lookup("#portField").queryAs(javafx.scene.control.TextField.class)).hasText("");
    }
	
	//add tourney to model shows up in list view
	@Test
	void testListAddssWhenTourneyAdded(FxRobot robot)
	{
		Platform.runLater(()->{
			RoundRobinTournament t = new RoundRobinTournament();
            t.name = "RoundRobinTournament0";
            t.active = true;
            model.tournaments.add(t);
		  });
		  WaitForAsyncUtils.waitForFxEvents();
		  ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
	      Assertions.assertThat(list).hasExactlyNumItems(1);	
	}
	
	//two tourneys both show up
	@Test
    void testListUpdatesAnotherTourneyAdded(FxRobot robot)
    {
        Platform.runLater(() -> {
            RoundRobinTournament t0 = new RoundRobinTournament();
            t0.name = "RoundRobinTournament0";
            t0.active = false;
            model.tournaments.add(t0);

            RoundRobinTournament t1 = new RoundRobinTournament();
            t1.name = "RoundRobinTournament1";
            t1.active = true;
            model.tournaments.add(t1);
        });
        WaitForAsyncUtils.waitForFxEvents();
        ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
        Assertions.assertThat(list).hasExactlyNumItems(2);
    }
	
	//default label text on start
	@Test
    void testTourneyLabelStart(FxRobot robot)
    {
        Platform.runLater(() -> Main.primaryStage.getScene().setRoot(Main.moveView));
        WaitForAsyncUtils.waitForFxEvents();
        Assertions.assertThat(robot.lookup("#tournamentLabel")
            .queryAs(Label.class)).hasText("Tournament:");
    }
	
	//should append move to text area
	@Test
    void testUpdateMove(FxRobot robot)
    {
        Platform.runLater(() -> {
            Main.primaryStage.getScene().setRoot(Main.moveView);
            moveController.updateMove("RobotA picked Defect");
        });
        WaitForAsyncUtils.waitForFxEvents();
        Assertions.assertThat(robot.lookup("#liveMoveTextArea")
            .queryAs(TextArea.class)).hasText("RobotA picked Defect\n");
    }
	
	//multiples moves should append in order
	@Test
    void testMultipleMoves(FxRobot robot)
    {
        Platform.runLater(() -> {
            Main.primaryStage.getScene().setRoot(Main.moveView);
            moveController.updateMove("RobotA picked Defect");
            moveController.updateMove("RobotB picked Cooperate");
        });
        WaitForAsyncUtils.waitForFxEvents();
        TextArea ta = robot.lookup("#liveMoveTextArea").queryAs(TextArea.class);
        assertTrue(ta.getText().contains("RobotA picked Defect"));
        assertTrue(ta.getText().contains("RobotB picked Cooperate"));
    }
	
	//MoveReciever calls updateMove on moveViewController and updates text area
    @Test
    void testMoveReceiver(FxRobot robot)
    {
        Platform.runLater(() -> Main.primaryStage.getScene().setRoot(Main.moveView));
        WaitForAsyncUtils.waitForFxEvents();
        MoveReciever receiver = new MoveReciever();
        receiver.receiveMove("SameBot picked Defect");
        WaitForAsyncUtils.waitForFxEvents();
        Assertions.assertThat(robot.lookup("#liveMoveTextArea")
            .queryAs(TextArea.class)).hasText("SameBot picked Defect\n");
    }
    
    //view button is disabled when tournament is in registration phase
    @Test
    void testViewButtonDisabledWhenRegistration(FxRobot robot)
    {
        Platform.runLater(() -> {
            RoundRobinTournament t = new RoundRobinTournament();
            t.name = "RoundRobinTournament0";
            t.active = false;
            model.tournaments.add(t);
        });
        WaitForAsyncUtils.waitForFxEvents();

        Button btn = robot.lookup("#viewButton").queryAs(Button.class);
        assertTrue(btn.isDisabled());
    }

    // view button is enabled when tournament is active
    @Test
    void testViewButtonEnabledWhenActive(FxRobot robot)
    {
        Platform.runLater(() -> {
            RoundRobinTournament t = new RoundRobinTournament();
            t.name = "RoundRobinTournament0";
            t.active = true;
            model.tournaments.add(t);
        });
        WaitForAsyncUtils.waitForFxEvents();

        Button btn = robot.lookup("#viewButton").queryAs(Button.class);
        assertFalse(btn.isDisabled());
    }

    
	
	
}
