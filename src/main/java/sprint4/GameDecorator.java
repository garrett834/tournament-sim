package sprint4;

import sprint1.Game;
import sprint1.Robot;

public abstract class GameDecorator extends Game 
{
	public Game decGame;
	
	public GameDecorator(Game game) 
	{
		this.decGame = game;
		this.gameRounds = game.gameRounds;
		this.MoveObservers = game.MoveObservers;
		this.ScoreObservers = game.ScoreObservers;
	}
	
	@Override
	public void playRound(Robot p1,Robot p2)
	{
		decGame.playRound(p1, p2);	
	}

	@Override
	public Robot playGame(Robot p1,Robot p2)
	{
		return decGame.playGame(p1,p2);
	}

	/**
	 * @return the decGame
	 */
	public Game getDecGame() {
		return decGame;
	}

	/**
	 * @param decGame the decGame to set
	 */
	public void setDecGame(Game game) {
		this.decGame = game;
		this.gameRounds = game.gameRounds;
	}
	
	

}
