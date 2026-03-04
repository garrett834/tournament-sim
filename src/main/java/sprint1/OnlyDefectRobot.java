package sprint1;
//only defect no matter what
public class OnlyDefectRobot extends Robot
{

	public OnlyDefectRobot(String name, int score, int record) 
	{
		super(name, score, record);
	}
	@Override
	public String makeDecision()
	{
		return "Defect";
	}
	

}
