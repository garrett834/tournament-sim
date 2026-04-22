package sprint2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import sprint1.HumanRobot;
import sprint1.MoveObserver;
import sprint1.PrisonerDelimmaGame;
import sprint1.PrisonerOppositeRobot;
import sprint1.PrisonerSameRobot;
import sprint1.Robot;
import sprint1.RoundRobinTournament;
import sprint1.Tournament;
import sprint3.RemoteClientViewer;


@RestController
@RequestMapping()
public class TournamentServer 
{
	List<Tournament> tournaments;
	Map<String, Integer> clients;
	
	public TournamentServer() 
	{
		this.tournaments = new ArrayList<>();
		this.clients = new HashMap<>();
	}
	
	//server home page
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("")
	public String home() 
	{
		return
		"<html>" +
		"<body>" +
		"<h1>Tournament Server</h1>" +
		"<ol>" +
		"<li> <a href='/tournaments'>/tournaments </a> - List of all available tournaments. </li>" +
		"<li>/register/robot/{name}/{ip}/{port}/{tourneyIndex} - Add robot to a tournament.</li>" +
		"<li>/add/tournament - Create a new round robin tournament. </li>" +
	    "<li>/remove/{tourneyIndex} - Remove tournament.</li>" +
		"<li>/register/human/{name}/{tourneyIndex} - Add human to a tournament.</li>" +
		"<li>/start/{tourneyIndex} - Start tournament.</li>" +
		"</ol>" +
		"</body>" +
		"</html>";	   
	}
	 
	//return list of all tournaments
	@ResponseStatus(HttpStatus.OK)
    @GetMapping("/tournaments")
	public String viewTournaments()
	{
		return "Tournaments: " + tournaments;
	}
	
	//register remote robot into tournament
	@ResponseStatus(HttpStatus.OK)
    @GetMapping("/register/robot/{name}/{ip}/{port}/{tourneyIndex}")
	public String registerRemoteRobot(@PathVariable String name, @PathVariable String ip, @PathVariable String port, @PathVariable int tourneyIndex)
	{
		if (tourneyIndex >= tournaments.size())
		{
			return "Not a valid tournament index"; 
		}
		
		if(clients.containsKey(name))
		{
			return name + " is already registered in tournament.";
		}
		//add to tournament participants list
		tournaments.get(tourneyIndex).participants.add(new RemoteClientRobot(name, ip, port));
		//add to clients list
		clients.put(name, tourneyIndex);
		
		return "Remote Robot " + name + " registered at " + ip + ":" + port + " added to tournament " + tourneyIndex; 	
	}
	
	//register human robot into tournament
	@ResponseStatus(HttpStatus.OK)
    @GetMapping("/register/human/{name}/{tourneyIndex}")
	public String registerHumanRobot(@PathVariable String name, @PathVariable int tourneyIndex)
	{
		if (tourneyIndex >= tournaments.size())
		{
			return "Not a valid tournament index"; 
		}
		//add to tournament participants list
		tournaments.get(tourneyIndex).participants.add(new HumanRobot(name));
		
		return "Human Robot " + name + " added to tournament " + tourneyIndex; 	
	}
	
	@ResponseStatus(HttpStatus.OK)
    @GetMapping("/start/{tourneyIndex}")
	public String startTournament(@PathVariable int tourneyIndex)
	{
		if (tourneyIndex >= tournaments.size())
		{
			return "Not a valid tournament index"; 
		}
		
		Robot winner = tournaments.get(tourneyIndex).runTournament();
		return "Tournament complete. The winner is " + winner.name;
	}
	
	//adds roundrobin tournament with prisoner dilemma games and two robots. 
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/add/tournament")
	public String addTournament()
	{
	    RoundRobinTournament rrTourney = new RoundRobinTournament();
	    rrTourney.name = "RoundRobinTournament" + tournaments.size(); 
	    rrTourney.game = new PrisonerDelimmaGame();
	    rrTourney.participants.add(new PrisonerSameRobot("SameBot", 0, 0));
	    rrTourney.participants.add(new PrisonerOppositeRobot("OppositeBot", 0, 0));
	    tournaments.add(rrTourney);
	    return "Tournament created at index " + (tournaments.size() - 1);
	}
	
	//remove a tournament from array
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/remove/{tourneyIndex}")
	public String removeTournament(@PathVariable int tourneyIndex)
	{
	    if(tourneyIndex >= tournaments.size())
	    {
	        return "Not a valid tournament index";
	    }
	    
	    tournaments.remove(tourneyIndex);
	    return "Tournament at index" + tourneyIndex + " removed.";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/reset")
	public String reset() 
	{
	    tournaments.clear();
	    return "reset";
	}
	
	//creates viewer and adds to its game observers
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/registerViewer/{tourneyIndex}/{ip}/{port}")
	public String registerViewer(@PathVariable int tourneyIndex,@PathVariable String ip, @PathVariable String port)
	{
		if (tourneyIndex >= tournaments.size())
		{
			return "Not a valid tournament index";	
		}
	        

	    RemoteClientViewer viewer = new RemoteClientViewer(ip, port);
	    tournaments.get(tourneyIndex).game.registerMoveObserver(viewer);
	    return "Viewer " + ip + ":" + port + " registered to tournament at index" + tourneyIndex;
	}
	
	//finds viewer w/ ip and port in observer list and removes
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/unregisterViewer/{tourneyIndex}/{ip}/{port}")
	public String unregisterViewer(@PathVariable int tourneyIndex,@PathVariable String ip, @PathVariable String port)
	{
		if (tourneyIndex >= tournaments.size())
		{
			 return "Not a valid tournament index";
		}
		
		MoveObserver oneRemoving = null;
	    for (int i = 0; i < tournaments.get(tourneyIndex).game.MoveObservers.size(); i++)
	    {
	        MoveObserver o = tournaments.get(tourneyIndex).game.MoveObservers.get(i);
	        if (o instanceof RemoteClientViewer viewer && viewer.getIp().equals(ip)&& viewer.getPort().equals(port))
	        {
	            oneRemoving = o;
	            break;
	        }
	    }

	    if (oneRemoving != null)
	    {
	        tournaments.get(tourneyIndex).game.unregisterMoveObserver(oneRemoving);
	        return "Viewer " + ip + ":" + port + " unregistered from tournament " + tourneyIndex;
	    }
	    return "Viewer with that ip and port not found";
	}
				

}
