package sprint1;

import java.util.Scanner;

public class HumanRobot extends Robot
{
	private Scanner scanner;

	public HumanRobot(String name) 
	{
		super(name, 0, 0);
		this.scanner = new Scanner(System.in);
	}
	
	@Override
    public String makeDecision()
    {

        // show opponents previous decision if there is one
        if(opponentsPrevDecision != null)
        {
            System.out.println("Opponent's previous decision: " + opponentsPrevDecision);
        }
        else
        {
            System.out.println("This is the first round.");
        }

        System.out.println("Enter your decision " + name + " (Cooperate/Defect): ");
        String decision = scanner.nextLine().trim();
        
        //if the input is invalid, default to cooperate
        if(!decision.equals("Cooperate") && !decision.equals("Defect"))
        {
            System.out.println("Invalid. Defaulting to Cooperate.");
            return "Cooperate";
        }
        return decision;
    }
}
	 


