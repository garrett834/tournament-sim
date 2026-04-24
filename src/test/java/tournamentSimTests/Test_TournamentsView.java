package tournamentSimTests;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
//import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.TournamentModel;
import model.ViewTransitionModel;
import sprint1.RoundRobinTournament;
import sprint1.Tournament;
import views.TournamentsViewController;

@ExtendWith(ApplicationExtension.class)
public class Test_TournamentsView
{
	TournamentModel model;
	
	@Start
	private void start(Stage stage)
	{
		this.model = new TournamentModel("localhost","8082");
		
		FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(ViewTransitionModel.class
	        .getResource("../views/TournamentsView.fxml"));
	    try {
	      BorderPane view = loader.load();
	      TournamentsViewController cont = loader.getController();
	      ViewTransitionModel vm = new ViewTransitionModel(stage,model);
	      cont.setModel(vm);
	            
	      Scene s = new Scene(view);
	      stage.setScene(s);
	      stage.show();
	      
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
		
	}
	
	//list empty before server connection
	@Test
	void testListEmptyStart(FxRobot robot)
	{
	    ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
	    Assertions.assertThat(list).isEmpty();
	}
		
    //ip field empty on start
	@Test
	void testIpEmptyStart(FxRobot robot)
	{
	    Assertions.assertThat(robot.lookup("#ipField").queryAs(javafx.scene.control.TextField.class)).hasText("");
	}
	
	//port empty at start
	@Test
	void testPortEmptyStart(FxRobot robot)
	{
	    Assertions.assertThat(robot.lookup("#portField")
	        .queryAs(javafx.scene.control.TextField.class)).hasText("");
	}
	
	//ip field takes input
	@Test
	void testIpFieldInput(FxRobot robot)
	{
	    robot.clickOn("#ipField");
	    robot.write("ggg");

	    Assertions.assertThat(robot.lookup("#ipField")
	        .queryAs(javafx.scene.control.TextField.class)).hasText("ggg");
	}

	//port field takes input
	@Test
	void testPortFieldInput(FxRobot robot)
	{
		TextField port = robot.lookup("#portField").queryAs(TextField.class);
	    robot.clickOn(port);
	    robot.write("8080");

	    Assertions.assertThat(port).hasText("8080");
	}
	
	//add tourney to model shows up in list view
	@Test
	void testAddOneTourney(FxRobot robot)
	{
		RoundRobinTournament t = new RoundRobinTournament();
        t.name = "RoundRobinTournament0";
        t.active = true;
        model.tournaments.add(t);
		 
        ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
	    Assertions.assertThat(list).hasExactlyNumItems(1);	
	}	
		
	//two tourneys both show up
	@Test
	void testAddMultipleTourneys(FxRobot robot)
	{

		RoundRobinTournament t0 = new RoundRobinTournament();
		t0.name = "RoundRobinTournament0";
		t0.active = false;
		model.tournaments.add(t0);

		RoundRobinTournament t1 = new RoundRobinTournament();
		t1.name = "RoundRobinTournament1";
		t1.active = true;
		model.tournaments.add(t1);
		        
		ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
		Assertions.assertThat(list).hasExactlyNumItems(2);
	}
	
	//registration tourney not clickable
	@Test
    void testViewButtonDisabled(FxRobot robot)
    {
		Platform.runLater(()->{
			RoundRobinTournament t = new RoundRobinTournament();
			t.name = "RoundRobinTournament0";
			t.active = false;
			model.tournaments.add(t);
		});
		WaitForAsyncUtils.waitForFxEvents();

        Button btn = robot.lookup("#viewMovesButton").queryAs(Button.class);
        Assertions.assertThat(btn.isDisabled());
    }
	
	//active tourney clickable
	@Test
    void testViewButtonEnabled(FxRobot robot)
    {
		Platform.runLater(()->{
			RoundRobinTournament t = new RoundRobinTournament();
			t.name = "RoundRobinTournament0";
			t.active = true;
			model.tournaments.add(t);
		});
		WaitForAsyncUtils.waitForFxEvents();
        

        Button btn = robot.lookup("#viewMovesButton").queryAs(Button.class);
        Assertions.assertThat(!btn.isDisabled());
    }
	
	//pressing connect fills listview
	@Test
	void testConnectButton(FxRobot robot)
	{
  
        RoundRobinTournament t = new RoundRobinTournament();
        t.name = "RoundRobinTournament0";
        t.active = true;
        model.tournaments.add(t);
        

        robot.clickOn("#connectButton");
        WaitForAsyncUtils.waitForFxEvents();
        
        ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
        Assertions.assertThat(list).hasExactlyNumItems(1);
	}
	
	//refresh repops. list
	@Test
	void testRefreshButton(FxRobot robot)
	{
	  
	    RoundRobinTournament t = new RoundRobinTournament();
	    t.name = "RoundRobinTournament0";
	    t.active = true;
	    model.tournaments.add(t);

	    ListView<Tournament> list = robot.lookup("#tournamentListView").queryListView();
	    Assertions.assertThat(list).hasExactlyNumItems(1);

	    RoundRobinTournament t1 = new RoundRobinTournament();
	    t1.name = "RoundRobinTournament1";
	    t1.active = false;
	    model.tournaments.add(t1);
	    
	    robot.clickOn("#refreshButton");
	    WaitForAsyncUtils.waitForFxEvents();

	    list = robot.lookup("#tournamentListView").queryListView();
	    Assertions.assertThat(list).hasExactlyNumItems(2);
	}
	
	
}
