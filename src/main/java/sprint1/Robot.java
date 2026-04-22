package sprint1;

public abstract class Robot 
{
	public String name;
	public int score;
	public int record;
	public String opponentsPrevDecision = null;
	
	public Robot(String name, int score, int record) 
	{
		this.name = name;
		this.score = 0;
		this.record = 0;
	}
	
	public abstract String makeDecision();
	
	public String getOpponentName(Robot r)
	{
		return r.name;
	}
	
}
