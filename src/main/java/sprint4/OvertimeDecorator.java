package sprint4;

import sprint1.Game;
import sprint1.Robot;

public class OvertimeDecorator extends GameDecorator 
{
	int otRounds = 3;

	public OvertimeDecorator(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Robot playGame(Robot p1, Robot p2) 
	{
		//run wrapped game first
		Robot winner = decGame.playGame(p1,p2);
		
		if(p1.score == p2.score)
		{
			notifyMoveObserver("Game is tied. Beginning Overtime");
			
			for(int i=0;i<otRounds;i++)
			{
				decGame.playRound(p1, p2);
			}
			
			notifyScoreObserver(p1.name + " " + "scored" + " " + p1.score);
			notifyScoreObserver(p2.name + " " + "scored" + " " + p2.score);
			
			if (p1.score > p2.score)
			{
				p1.record += 1;
				p2.record -= 1;
				winner = p1;
			}
			else if (p2.score > p1.score)
			{
				p1.record -= 1;
				p2.record += 1;
				winner = p2;
			}
			else
			{
				winner = p1;
			}
					
			return winner;
		}
		return winner;
		
	}

}
