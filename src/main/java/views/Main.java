package views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.TournamentModel;
//import views.MoveViewController;
//import views.TournamentsViewController;

public class Main extends Application 
{
	public static Stage primaryStage;
    public static BorderPane tournamentsView;
    public static BorderPane moveView;

	@Override
	public void start(Stage stage) throws Exception 
	{
		primaryStage = stage;
		
		//viewers ip and port must match appRunners springboots port so client viewer knows where to send updates 
        TournamentModel model = new TournamentModel("localhost", "8082");
		
        //load tounrey list view
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("TournamentsView.fxml"));
		tournamentsView = loader.load();
		TournamentsViewController controller = loader.getController();
        controller.setModel(model);
        
        //load move view 
        FXMLLoader moveLoader = new FXMLLoader();
        moveLoader.setLocation(Main.class.getResource("MovesView.fxml"));
        moveView = moveLoader.load();
        MoveViewController moveController = moveLoader.getController();
        moveController.setModel(model);
        
        controller.setMoveViewController(moveController);
        
        MoveReciever.moveViewController = moveController;
        
		
		Scene s = new Scene(tournamentsView);
		stage.setScene(s);
		
		//so springboot shuts down cleanly cause i was having issues when testing
		stage.setOnCloseRequest(event -> {
		    System.exit(0);
		});
		stage.show();	
	} 
	
	public static void main(String[] args)
	{
		launch(args);
	}

}
