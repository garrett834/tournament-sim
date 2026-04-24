package views;

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
		TournamentModel model = new TournamentModel("localhost", "8082");

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
