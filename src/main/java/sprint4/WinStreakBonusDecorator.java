package sprint4;

import sprint1.Game;
import sprint1.Robot;

public class WinStreakBonusDecorator extends GameDecorator
{
	int streakNum = 3;
	int bonusNum = 5;
	
	
	public WinStreakBonusDecorator(Game game) {
		super(game);
	}
	
	@Override
	public Robot playGame(Robot p1,Robot p2)
	{
		int p1Streak = 0;
		int p2Streak = 0;
		
		for(int i=0;i<gameRounds;i++)
		{
			//get scores before round and then after round
			int p1Before = p1.score;
			int p2Before = p2.score;
			
			decGame.playRound(p1, p2);
			
			int p1After = p1.score - p1Before;
			int p2After = p2.score - p2Before;
			
			if(p1After > p2After)
			{
				p1Streak++;
				p2Streak = 0;
			}
			else if(p2After > p1After)
			{
				p2Streak++;
				p1Streak = 0;
			}
			else
			{
				p1Streak = 0;
				p2Streak = 0;
			}
			
			if(p1Streak == streakNum)
			{
				p1.score += bonusNum;
				p1Streak = 0;
			}
			
			if(p2Streak == streakNum)
			{
				p2.score += bonusNum;
				p2Streak = 0;
			}
		}
		
		notifyScoreObserver(p1.name + " " + "scored" + " " + p1.score);
		notifyScoreObserver(p2.name + " " + "scored" + " " + p2.score);
		
		Robot winner;
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
	

}
