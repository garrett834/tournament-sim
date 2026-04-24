package model;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import views.MoveReciever;
import views.MoveViewController;
import views.TournamentsViewController;

public class ViewTransitionModel implements ViewTransitionModelInterface
{
	Stage stage;
	TournamentModel model;
	
	public ViewTransitionModel(Stage stage, TournamentModel model) 
	{
		this.stage = stage;
		this.model = model;
	}
	
	//load and displaus tournament view
	@Override
	public void showTournaments()
	{
		FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(ViewTransitionModel.class
	        .getResource("../views/TournamentsView.fxml"));
	    try {
	      BorderPane view = loader.load();
	      TournamentsViewController cont = loader.getController();
	      cont.setModel(this);
	      stage.getScene().setRoot(view);	      
	      
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	}
	
	//load and displays move view
	@Override
	public void showMoves()
	{
		FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(ViewTransitionModel.class
	        .getResource("../views/MovesView.fxml"));
	    try {
	      BorderPane view = loader.load();
	      MoveViewController cont = loader.getController();
	      cont.setModel(model);
	      MoveReciever.moveViewController = cont;
	      stage.getScene().setRoot(view);
	      
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	}

	/**
	 * @return the model
	 */
	public TournamentModel getModel() {
		return model;
	}
		
	

}
