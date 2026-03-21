package sprint1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Sprint_2_Test 
{
	TournamentServer server; 

    @BeforeEach
    public void setup()
    {
        server = new TournamentServer();
    }

    
    @Test
    public void testViewTournamentsEmpty()
    {
        String result = server.viewTournaments();
        //new tournament array should be empty
        assertEquals("Tournaments: []", result);
    }

    @Test
    public void testCreateTournament()
    {
        server.addTournament();
        //adding tournament should show in array
        assertEquals(1, server.tournaments.size());
    }

    @Test
    public void testViewTournamentsAfterCreate()
    {
        server.addTournament();
        String result = server.viewTournaments();
        assertTrue(result.contains("RoundRobin"));
    }

    @Test
    public void testRemoveTournament()
    {
        server.addTournament();
        server.removeTournament(0);
        assertEquals(0, server.tournaments.size());
    }

    @Test
    public void testRemoveTournamentInvalidIndex()
    {
        String result = server.removeTournament(0);
        assertEquals("Not a valid tournament index", result);
    }

    @Test
    public void testRegisterRemoteRobot()
    {
        server.addTournament();
        String result = server.registerRemoteRobot("Gary", "localhost", "8081", 0);
        assertTrue(result.contains("Gary"));
        assertEquals(1, server.clients.size());
    }

    @Test
    public void testRegisterRemoteRobotInvalidIndex()
    {
        String result = server.registerRemoteRobot("Gary", "localhost", "8081", 50);
        assertEquals("Not a valid tournament index", result);
    }

    @Test
    public void testRegisterRemoteRobotDuplicate()
    {
        server.addTournament();
        server.registerRemoteRobot("Gary", "localhost", "8081", 0);
        String result = server.registerRemoteRobot("Gary", "localhost", "8081", 0);
        assertTrue(result.contains("already registered"));
    }

    @Test
    public void testRegisterHumanRobot()
    {
        server.addTournament();
        String result = server.registerHumanRobot("Gary", 0);
        assertTrue(result.contains("Gary"));
    }

    @Test
    public void testRegisterHumanRobotInvalidIndex()
    {
        String result = server.registerHumanRobot("Gary", 99);
        assertEquals("Not a valid tournament index", result);
    }

    @Test
    public void testStartTournament()
    {
        server.addTournament();
        String result = server.startTournament(0);
        assertTrue(result.contains("winner"));
    }

    @Test
    public void testStartTournamentInvalidIndex()
    {
        String result = server.startTournament(99);
        assertEquals("Not a valid tournament index", result);
    }


    @Test
    public void testRemoteClientRobot()
    {
        RemoteClientRobot robot = new RemoteClientRobot("Gary", "localhost", "8081");
        //shoudl store name, ip, and port
        assertEquals("Gary", robot.name);
        assertEquals("localhost", robot.getIp());
        assertEquals("8081", robot.getPort());
    }

    @Test
    public void testRemoteClientRobotFallback()
    {
        //no client running so should fallback to cooperate
        RemoteClientRobot robot = new RemoteClientRobot("Gary", "localhost", "9999");
        assertEquals("Cooperate", robot.makeDecision());
    }
    
    @Test
    public void testRemoteClientRobotWithPrevDecision()
    {
        RemoteClientRobot robot = new RemoteClientRobot("Gary", "localhost", "9999");
        robot.opponentsPrevDecision = "Cooperate";
        // no client on 9999 so falls back to cooperate
        assertEquals("Cooperate", robot.makeDecision());
    }
    
    @Test
    public void testRemoteClientRobotNullPrevDecision()
    {
        RemoteClientRobot robot = new RemoteClientRobot("Garrett", "localhost", "9999");
        robot.opponentsPrevDecision = null;
        // no client running on 9999 so falls back to cooperate
        assertEquals("Cooperate", robot.makeDecision());
    }
    

}