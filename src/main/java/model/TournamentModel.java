package model;

import org.springframework.web.client.RestClient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sprint1.RoundRobinTournament;
import sprint1.Tournament;

public class TournamentModel 
{
	public ObservableList<Tournament> tournaments = FXCollections.observableArrayList();
	public Tournament selectedTournament;
	String serverIP;
	String serverPort;
	String viewerIP;
	String viewerPort;
	RestClient restClient = RestClient.create();
	
	public TournamentModel(String viewerIP, String viewerPort)
    {
        this.viewerIP = viewerIP;
        this.viewerPort = viewerPort;
    }
	
	/**
	 * @return the selectedTournament
	 */
	public Tournament getSelectedTournament() {
		return selectedTournament;
	}

	/**
	 * @return the tournaments
	 */
	public ObservableList<Tournament> getTournaments() {
		return tournaments;
	}
	
	//connect to server and load tournament list, true if succesful, false if not
	public boolean connect(String serverIP, String serverPort)
	{
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		try
        {
            String baseuri = "http://" + serverIP + ":" + serverPort ;
            //send get request and retrieve decision
            restClient.get()
    				.uri(baseuri)
    				.retrieve()
    				.body(String.class);
            refreshTournament();
            return true;
        }

		catch (Exception e)
		{
			System.out.println("Couldn't connect to server. Error: " + e.getMessage());
			return false;
		}
		
	}
	
	//when server clicks view tourney button, server starts sending moves to this viewer
	public void selectTournament(Tournament t)
	{
		selectedTournament = t;
		try
        {
            String baseuri = "http://" + serverIP + ":" + serverPort + "/registerViewer/" + tournaments.indexOf(t) + "/" + viewerIP + "/" + viewerPort;
            //send get request and retrieve decision
            restClient.get()
    				.uri(baseuri)
    				.retrieve()
    				.body(String.class);
        }

		catch (Exception e)
		{
			System.out.println("Couldn't register viewer. Error: " + e.getMessage());
		}
		
	}
	
	//when exit tournament clicked
	public void unselectTournament(Tournament t)
	{
		try
        {
            String baseuri = "http://" + serverIP + ":" + serverPort + "/unregisterViewer/" + tournaments.indexOf(t) + "/" + viewerIP + "/" + viewerPort;
            //send get request and retrieve decision
            restClient.get()
    				.uri(baseuri)
    				.retrieve()
    				.body(String.class);
        }
		//if can't contact remote viewer client
		catch (Exception e)
		{
			System.out.println("Couldn't unregister viewer. Error: " + e.getMessage());
		}
		selectedTournament = null;
		
	}
	
	//gets latest tournament list and updates list view
	public void refreshTournament()
	{
		try
        {
            String baseuri = "http://" + serverIP + ":" + serverPort + "/tournaments";
            //send get request and retrieve decision
            String response = restClient.get()
    				.uri(baseuri)
    				.retrieve()
    				.body(String.class);
            //clear list to repopulate
            tournaments.clear();
            
            //strip bracket and stuff before
            response = response.replace("Tournaments: ","").replace("[", "").replace("]", "");
            if (response.isEmpty())
            {
            	return;
            }
            String[] parts = response.split(", ");
            for(String part : parts)
            {
            	//find dash and extract the name on left and status on right
            	int dash = part.indexOf(" - ");
            	String name = part.substring(0,dash).trim();
            	String status =part.substring(dash + 3).trim();
            	
            	Tournament t = new RoundRobinTournament();
            	t.name = name;
            	t.active = status.equals("Active");
            	t.completed = status.equals("Completed");
            	//add to list
            	tournaments.add(t);
            }
                  
        }
		catch (Exception e)
		{
			System.out.println("Couldn't load tounraments. Error: " + e.getMessage());
		}
			
	}
}
