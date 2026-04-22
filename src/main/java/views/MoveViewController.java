package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.TournamentModel;
import sprint1.Tournament;

public class MoveViewController
{
		TournamentModel model;
		
	    @FXML
	    private Label tournamentLabel;
	    
	    @FXML
	    private TextArea liveMoveTextArea;
	    
	    @FXML
	    private Button exitTourneyButton;
	    
	    //sets model and updates tournament label w/ tournament name
	    public void setModel(TournamentModel model)
	    {
	    	this.model = model;
	    	Tournament t = model.getSelectedTournament();
	    	if (t != null) 
	    	{
	            tournamentLabel.setText("Tournament: " + t.name);
	        }
	    }

	    //unregister viewer and switch back to tourney list view
	    @FXML
	    void onExitClicked(ActionEvent event) 
	    {
	    	
	    	Tournament t = model.getSelectedTournament();
	    	if (t!=null)
	    	{
	    		model.unselectTournament(t);
	    	}
	    	
	    	Main.primaryStage.getScene().setRoot(Main.tournamentsView);
	    	
	    }
	    
	    //called by MoveReciever when update from server
	    public void updateMove(String move)
	    {
	    	liveMoveTextArea.appendText(move + "\n");
	    }

		/**
		 * @return the tournamentLabel
		 */
		public Label getTournamentLabel() {
			return tournamentLabel;
		}

}
