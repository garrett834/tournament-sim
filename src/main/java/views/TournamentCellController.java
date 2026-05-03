package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sprint1.Tournament;


public class TournamentCellController
{
	private TournamentCell model;

    //updates the label and enables/disables button based on tourney status
    public void setModel(TournamentCell model)
    {
        this.model = model;
        Tournament t = model.getTournament();
        if(t!=null)
        {
        	tourneyLabel.setText(t.toString());
        	if(t.completed)
        	{
        		viewButton.setDisable(true);
        	}
        	else if(t.active)
        	{
        		viewButton.setDisable(false);
        	}
        	else
        	{
        		viewButton.setDisable(true);
        	}
            
        }
         
    }	
	
	@FXML
	private Label tourneyLabel;

	@FXML
	private Button viewButton;
	
	
	//delegates to cell w/ notifies TourneyViewController
	@FXML
	void onViewMovesClicked(ActionEvent event) 
	{
		System.out.println("cell button clicked");
		if (model != null)
		{
			model.viewClicked();
		} else {
	        System.out.println("model is null in cell controller");
	    }
            

	}

}
