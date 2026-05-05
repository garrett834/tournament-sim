package views;

import java.net.InetAddress;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.TournamentModel;
import model.ViewTransitionModel;

public class Main extends Application 
{
	public static Stage primaryStage;
    public static BorderPane tournamentsView;
    public static BorderPane moveView;

	@Override
	public void start(Stage stage) throws Exception 
	{
		String viewerIP;
		try
		{
			viewerIP = InetAddress.getLocalHost().getHostAddress();
		}
		catch(Exception e)
		{
			viewerIP = "localhost";
		}
		TournamentModel model = new TournamentModel(viewerIP, "8082");

	    ViewTransitionModel vm = new ViewTransitionModel(stage, model);
	    MoveReciever.vm = vm;

	    stage.setScene(new Scene(new BorderPane()));
	    vm.showTournaments();

	    stage.setOnCloseRequest(event -> System.exit(0));
	    stage.show();	
	} 
	
	public static void main(String[] args)
	{
		launch(args);
	}

}
