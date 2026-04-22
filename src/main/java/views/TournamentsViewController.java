package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
//import javafx.scene.layout.BorderPane;
import model.TournamentModel;
import sprint1.Tournament;

public class TournamentsViewController 
{
		TournamentModel model;
		//reference to move view controller for switching scenes
		MoveViewController moveViewController;
	
	    @FXML
	    private TextField ipField;

	    @FXML 
	    private TextField portField;
	    
	    @FXML
	    private ListView<Tournament> tournamentListView;
	    
	    
	    public void setModel(TournamentModel model) 
	    {
	        this.model = model;
	        tournamentListView.setItems(model.getTournaments());
	        //for every row in list, make tourney cell that knows about the list
	        tournamentListView.setCellFactory(view -> new TournamentCell(view, this));
	    }
	    
	    public void setMoveViewController(MoveViewController mvc)
	    {
	        this.moveViewController = mvc;
	    }


	    @FXML
	    void onConnectClicked(ActionEvent event) 
	    {
	    	model.connect(ipField.getText(),portField.getText());
	    	
	    }

	    @FXML
	    void onRefreshClicked(ActionEvent event) 
	    {
	    	model.refreshTournament();
	    }
	    
	    //registers viewer as observer and switches scene
	    public void showTournament(Tournament t) 
	    {
	    	model.selectTournament(t);
	    	
	    	moveViewController.setModel(model);
	        //switch scene
	        Main.primaryStage.getScene().setRoot(Main.moveView);
	    }

	    public void closeTournament(Tournament t) 
	    {
	        model.unselectTournament(t);
	    }

}
