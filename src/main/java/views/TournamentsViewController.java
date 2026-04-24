package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
//import javafx.scene.layout.BorderPane;
import model.TournamentModel;
import model.ViewTransitionModel;
import model.ViewTransitionModelInterface;
import sprint1.Tournament;

public class TournamentsViewController 
{
		ViewTransitionModelInterface vModel;
		TournamentModel model;
		
	
	    @FXML
	    private TextField ipField;

	    @FXML 
	    private TextField portField;
	    
	    @FXML
	    private ListView<Tournament> tournamentListView;
	    
	    @FXML
	    private Button connectButton;
	    
	    @FXML
	    private Button refreshButton;
	    
	    
	    public void setModel(ViewTransitionModelInterface newvModel) 
	    {
	        this.vModel = newvModel;
	        this.model = ((ViewTransitionModel) newvModel).getModel();
	        tournamentListView.setItems(model.getTournaments());
	        //for every row in list, make tourney cell that knows about the list
	        tournamentListView.setCellFactory(view -> new TournamentCell(view, this));
	    }
	   

	    @FXML
	    void onConnectClicked(ActionEvent event) 
	    {
	    	String ip = ipField.textProperty().get();
	        String port = portField.textProperty().get();
	        model.connect(ip, port);
	    	
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
	    	vModel.showMoves();
	    }

	    public void closeTournament(Tournament t) 
	    {
	        model.unselectTournament(t);
	    }

}
