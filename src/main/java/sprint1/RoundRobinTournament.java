package sprint1;

public class RoundRobinTournament extends Tournament
{
	@Override
	public Robot runTournament()
	{
		active = true;
		for(int i=0;i<participants.size();i++)
		{
			for(int j=i+1;j<participants.size();j++)
			{
				Robot p1 = participants.get(i);
				Robot p2 = participants.get(j);
				//reset scores prior to each game
				p1.score = 0;
				p2.score = 0;
				//set opponents previous decision to null before each game
				p1.opponentsPrevDecision = null;
				p2.opponentsPrevDecision = null;
				
				game.playGame(p1,p2);
				
			}
		}
		active = false;
		return getWinner();
	}
	
	//helper to get overall tourney winner
	public Robot getWinner()
	{
		Robot winner = participants.get(0);
		
		for(Robot r: participants)
		{
			if(r.record > winner.record)
			{
				winner = r;
			}
		}
		return winner;
	}

}
