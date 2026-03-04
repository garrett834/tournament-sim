package sprint1;

import java.util.ArrayList;

//Abstract game class w/ subject methods and abstract playgame method
public abstract class Game 
{
	ArrayList<ScoreObserver> ScoreObservers;
	ArrayList<MoveObserver> MoveObservers;

	int gameRounds;
	
	public Game() {
		this.ScoreObservers = new ArrayList<>();
		MoveObservers = new ArrayList<>();
	}
	
	public void registerScoreObserver(ScoreObserver s)
	{
		ScoreObservers.add(s);
	}
	public void registerMoveObserver(MoveObserver m)
	{
		MoveObservers.add(m);
	}
	public void unregisterScoreObserver(ScoreObserver s)
	{
		ScoreObservers.remove(s);
	}
	public void unregisterMoveObserver(MoveObserver m)
	{
		MoveObservers.remove(m);
	}
	public void notifyScoreObserver(String scoreInfo)
	{
		for(ScoreObserver s : ScoreObservers)
		{
			s.updateScore(scoreInfo);
		}
	}
	public void notifyMoveObserver(String moveInfo)
	{
		for(MoveObserver m: MoveObservers)
		{
			m.updateMove(moveInfo);
		}
	}
	public abstract Robot playGame(Robot p1, Robot p2);
	

}
