package sprint1;

import java.util.ArrayList;

public class PrisonerDelimmaGame extends Game
{
	private ArrayList<String> choices;
	
	public PrisonerDelimmaGame() 
	{
		this.choices = new ArrayList<>();
		choices.add("Cooperate");
		choices.add("Defect");
		this.gameRounds = 10;
	}

	@Override
	public Robot playGame(Robot p1, Robot p2) 
	{
		for(int i = 0;i<gameRounds;i++)
		{
			String movep1 = p1.makeDecision();
			String movep2 = p2.makeDecision();
			notifyMoveObserver(p1.name + " picked " + movep1);
			notifyMoveObserver(p2.name + " picked " + movep2);
			
			pdCalcScore(p1,p2,movep1,movep2);
			
			p1.opponentsPrevDecision = movep2;
			p2.opponentsPrevDecision = movep1;
			

	        //try {
			//	Thread.sleep(5000);
			//} catch (InterruptedException e) {
				//TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
	        
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
	//helper function for robot score calculations
	public void pdCalcScore(Robot p1,Robot p2,String movep1,String movep2)
	{
		if(movep1.equals("Cooperate") && movep2.equals("Cooperate"))
		{
			p1.score = p1.score + 3;
			p2.score = p2.score + 3;
		}
		if(movep1.equals("Cooperate") && movep2.equals("Defect"))
		{
			p2.score = p2.score + 5;
		}
		if(movep1.equals("Defect") && movep2.equals("Cooperate"))
		{
			p1.score = p1.score + 5;
		}
		if(movep1.equals("Defect") && movep2.equals("Defect"))
		{
			p1.score = p1.score + 1;
			p2.score = p2.score + 1;
		}
		
	}

}
