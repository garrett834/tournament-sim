package sprint1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//remote participant in tournament
//on its own server so tournament server can request decisions remotely
@SpringBootApplication
@RestController
public class TournamentClient 
{
	Robot clientBot;

    public TournamentClient()
    {
        this.clientBot = new PrisonerOppositeRobot("ClientBot", 0, 0);
    }

    public TournamentClient(Robot bot)
    {
        this.clientBot = bot;
    }
	
	public static void main(String[] args)
    {
        System.setProperty("server.port", "8081");
        SpringApplication.run(TournamentClient.class, args);
    }
	
	@ResponseStatus(HttpStatus.OK)
    @GetMapping("/resetClient")
    public String reset()
    {
        clientBot = new PrisonerOppositeRobot("ClientBot", 0, 0);
        return "Client bot reset";
    }
	
	@ResponseStatus(HttpStatus.OK)
    @GetMapping("/decision/{opponentsPrevDecision}")
	public String decision(@PathVariable String opponentsPrevDecision)
	{
		if(opponentsPrevDecision.equals("null"))
		{
		    clientBot.opponentsPrevDecision = null;
		}
		else
		{
		    clientBot.opponentsPrevDecision = opponentsPrevDecision;
		}
		
		return clientBot.makeDecision();
	}

}
