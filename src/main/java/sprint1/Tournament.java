package sprint1;

import java.util.ArrayList;
//abstract tournament class w/ arr. of robots & run tourney method 
public abstract class Tournament 
{
	ArrayList<Robot> participants;
	Game game;

	public Tournament() 
	{
		this.participants = new ArrayList<>();
	}

	public abstract Robot runTournament();

}
