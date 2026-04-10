package sprint1;
//choose the opposite of whatever other robot did
public class PrisonerOppositeRobot extends Robot
{
	//String oppsPrevDecision;
	public PrisonerOppositeRobot(String name, int score, int record) 
	{
		super(name, score, record);
		
	}
	@Override
	public String makeDecision()
	{
		if (this.opponentsPrevDecision == null)
		{
			return "Cooperate";
		}
		if (this.opponentsPrevDecision.equals("Cooperate"))
		{
			return "Defect";
		}
		else {
			return "Cooperate";
		}
	}
	

}
