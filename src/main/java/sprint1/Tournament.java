package sprint1;

import java.util.ArrayList;
//abstract tournament class w/ arr. of robots & run tourney method 
public abstract class Tournament 
{
	public ArrayList<Robot> participants;
	public Game game;
	public String name;
	public boolean active;
	public boolean isActive()
	{
		return active; 
	}
	public boolean completed = false;

	public Tournament() 
	{
		this.participants = new ArrayList<>();
	}

	public abstract Robot runTournament();

	@Override
	public String toString() 
	{
		if(completed)
		{
			return name + " -" + " Completed";
		}
		else if(active)
		{
			return name + " -" + " Active";
		}
		else {
			return name + " -" + " Registration";
		}
	    
	}

}
