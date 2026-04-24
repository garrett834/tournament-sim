package tournamentSimTests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

//import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.TournamentModel;
import model.ViewTransitionModel;
import sprint1.RoundRobinTournament;
import sprint1.Tournament;
import sprint2.SpringBootServer;
import sprint3.RemoteClientViewer;
import views.MoveReciever;
import views.MoveViewController;
import views.TournamentsViewController;


@SpringBootTest(
	    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	    classes = SpringBootServer.class
	)


@ExtendWith(ApplicationExtension.class)
class Sprint3_Views_Tests 
{
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	TournamentModel model;
	ViewTransitionModel vm;
    MoveViewController moveController;
    TournamentsViewController tourneyController;

	@Start
	private void start(Stage stage) throws IOException
	{
		model = new TournamentModel("localhost", String.valueOf(port));
        vm = new ViewTransitionModel(stage, model);
        MoveReciever.vm = vm;
        stage.setScene(new Scene(new BorderPane()));
        vm.showTournaments();
 
        stage.show();
	}
	 
	@BeforeEach
	void reset()
	{
	    restTemplate.getForObject("/reset", String.class);
	}
	
	//helper for entering server ip + port stuff
	private void enterIpPort(FxRobot robot,String ip, String port)
	{
		robot.clickOn("#ipField");
        robot.write(ip);
        robot.clickOn("#portField");
        robot.write(port);
		
	}

	//should add move to text area
	@Test
    void testUpdateMove(FxRobot robot)
    {
        Platform.runLater(() -> {
        	vm.showMoves();
        	MoveReciever.moveViewController.updateMove("RobotA picked Defect");
        });
        WaitForAsyncUtils.waitForFxEvents();
        Assertions.assertThat(robot.lookup("#liveMoveTextArea")
            .queryAs(TextArea.class)).hasText("RobotA picked Defect\n");
    }
	
	//multiples moves should add in order
	@Test
    void testMultipleMoves(FxRobot robot)
    {
        Platform.runLater(() -> {
        	vm.showMoves();
        	MoveReciever.moveViewController.updateMove("RobotA picked Defect");
            MoveReciever.moveViewController.updateMove("RobotB picked Cooperate");
        });
        WaitForAsyncUtils.waitForFxEvents();
        TextArea ta = robot.lookup("#liveMoveTextArea").queryAs(TextArea.class);
        assertTrue(ta.getText().contains("RobotA picked Defect"));
        assertTrue(ta.getText().contains("RobotB picked Cooperate"));
    }
	
	//moveReciever calls updateMove on moveViewController which updates text area
    @Test
    void testMoveReceiver(FxRobot robot)
    {
        Platform.runLater(() -> vm.showMoves());
        WaitForAsyncUtils.waitForFxEvents();
        MoveReciever receiver = new MoveReciever();
        receiver.receiveMove("SameBot picked Defect");
        WaitForAsyncUtils.waitForFxEvents();
        Assertions.assertThat(robot.lookup("#liveMoveTextArea")
            .queryAs(TextArea.class)).hasText("SameBot picked Defect\n");
    }
    
 
    //showMoves() switches scene to moves view
    @Test
    void testShowMovesSwithcesSceneRoot(FxRobot robot)
    {
        Platform.runLater(() -> vm.showMoves());
        WaitForAsyncUtils.waitForFxEvents();
        TextArea ta = robot.lookup("#liveMoveTextArea").queryAs(TextArea.class);
        assertNotNull(ta);
    }
 
    //showTournaments()switches scene to tournaments view
    @Test
    void testShowTournamentsSwithcesSceneRoot(FxRobot robot)
    {
        // First go to moves, then back
        Platform.runLater(() -> {
            vm.showMoves();
        });
        WaitForAsyncUtils.waitForFxEvents();
 
        Platform.runLater(() -> vm.showTournaments());
        WaitForAsyncUtils.waitForFxEvents();
 
        ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
        assertNotNull(list);
    }
    
    //pressing connect fills list w/ current tourney on server
    @Test
    void testConnectButtonFillssList(FxRobot robot)
    {
        restTemplate.getForObject("/add/tournament", String.class);
        
        robot.sleep(500);
        enterIpPort(robot,"localhost",String.valueOf(port));

        robot.clickOn("#connectButton");
        //WaitForAsyncUtils.waitForFxEvents();
        ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
        Assertions.assertThat(list).hasExactlyNumItems(1);
    }
    
    //refresh button refills list w/ new tourney
    @Test
    void testRefreshButtonUpdatesList(FxRobot robot)
    {
    	restTemplate.getForObject("/add/tournament", String.class);

    	enterIpPort(robot,"localhost",String.valueOf(port));

    	robot.clickOn("#connectButton");
    	WaitForAsyncUtils.waitForFxEvents();

    	restTemplate.getForObject("/add/tournament", String.class);
    	
    	robot.clickOn("#refreshButton");
    	WaitForAsyncUtils.waitForFxEvents();

    	ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
    	Assertions.assertThat(list).hasExactlyNumItems(2);
    }

    //pressing exit goes back to tourney view
    @Test
    void testExitButtonSwitchesToTournamentsView(FxRobot robot)
    {
        Platform.runLater(() -> {
            RoundRobinTournament t = new RoundRobinTournament();
            t.name = "RoundRobinTournament0";
            t.active = true;
            model.tournaments.add(t);
            model.selectedTournament = t;
            vm.showMoves();
        });
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("#exitTourneyButton");
        WaitForAsyncUtils.waitForFxEvents();

        ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
        assertNotNull(list);
    }
    
    //view moves button switched to moves view
    @Test
    void testViewButtonSwitchesToMovesView(FxRobot robot)
    {
        restTemplate.getForObject("/add/tournament", String.class);
        restTemplate.getForObject("/activate/0", String.class);
        
        robot.sleep(500);
        enterIpPort(robot,"localhost",String.valueOf(port));

        robot.clickOn("#connectButton");
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("#viewMovesButton");
        WaitForAsyncUtils.waitForFxEvents();
        robot.sleep(1500);

        TextArea ta = robot.lookup("#liveMoveTextArea").queryAs(TextArea.class);
        assertNotNull(ta);
    }

    //pressing view moves button then exit should be back to tournaments view
    @Test
    void testExitButtonSwitchesBackAfterViewButton(FxRobot robot)
    {
        restTemplate.getForObject("/add/tournament", String.class);
	    restTemplate.getForObject("/activate/0", String.class);
	    
	    robot.sleep(500);
        enterIpPort(robot,"localhost",String.valueOf(port));

        robot.clickOn("#connectButton");
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("#viewMovesButton");
        WaitForAsyncUtils.waitForFxEvents();
        robot.sleep(1500);

        robot.clickOn("#exitTourneyButton");
        WaitForAsyncUtils.waitForFxEvents();
        robot.sleep(1500);

        ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
        assertNotNull(list);
    }

    @Test
    void testRemoteClientViewerUpdateMoveFail()
    {
        RemoteClientViewer viewer = new RemoteClientViewer("localhost", "9999");
        // should not throw even when server unreachable
        assertDoesNotThrow(() -> viewer.updateMove("RobotA picked Defect"));
    }

    @Test  
    void testRemoteClientViewerUpdateMove()
    {
        // point at the test server which has /receiveMove if MoveReciever is registered
        RemoteClientViewer viewer = new RemoteClientViewer("localhost", String.valueOf(port));
        assertDoesNotThrow(() -> viewer.updateMove("RobotA picked Defect"));
    }
	
}
