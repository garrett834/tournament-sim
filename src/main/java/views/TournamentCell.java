package views;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import sprint1.Tournament;

public class TournamentCell extends ListCell<Tournament> 
{
	TournamentCellController cont;
    Node node;
    //reference to parent so button click makes scene switch
    TournamentsViewController parent;
    
    public TournamentCell(ListView<Tournament> view, TournamentsViewController parent)
	{
		this.parent = parent;
		
		FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(TournamentCell.class
	        .getResource("../views/TournamentCellView.fxml"));
	    try {
	      node = loader.load(); //store for later
	      
	      cont = loader.getController(); //store for later
	      cont.setModel(this);
	      //cont.setTournamentsViewController(parent);
	      
	    } catch (IOException e) 
	    {
	      e.printStackTrace();
	    }
			
	}
    
    public Tournament getTournament() 
    {
        return getItem();
    }
    
    //notify parent controller to switch to move view
    public void viewClicked() 
    {
    	if (getItem() != null) 
        {
    		parent.showTournament(getItem());
        }
    }
    
    @Override
	protected void updateItem(Tournament tournament, boolean empty)
	{
		super.updateItem(tournament, empty);//really important! always keep!
		
		if(empty || tournament == null)
		{
			setGraphic(null);
			setText(null);
		}
		else 
		{
			cont.setModel(this);
			setText(null);
			setGraphic(node);
		}
		
	}
	
}
