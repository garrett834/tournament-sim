package sprint1;
//choose same as opponents previous decision
public class PrisonerSameRobot extends Robot
{
	String oppsPrevDecision;
	public PrisonerSameRobot(String name, int score, int record) 
	{
		super(name, score, record);
		
	}
	@Override
	public String makeDecision() 
	{
		if(this.oppsPrevDecision == null) 
		{
			return "Cooperate";
		}
		if(this.oppsPrevDecision.equals("Defect"))
		{
			return "Defect";
		}
		else //if(this.oppsPrevDecision.equals("Cooperate"))
		{
			return "Cooperate";
		}
	}
	
	
	
	

}
