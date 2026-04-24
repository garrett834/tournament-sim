package tournamentSimTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import model.TournamentModel;
import sprint2.SpringBootServer;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
classes = SpringBootServer.class) 

class Sprint3_ServerTests 
{
	@Autowired
    private TestRestTemplate restTemplate;
	
	@LocalServerPort
    private int port;
    
    @BeforeEach
    void reset()
    {
        restTemplate.getForObject("/reset", String.class);
    }
    
    //register viewer to valid tourney
    @Test
    void testRegisterViewer()
    {
    	restTemplate.getForObject("/add/tournament", String.class);
    	String result = restTemplate.getForObject("/registerViewer/0/localhost/9090", String.class);
        assertTrue(result.contains("registered"));
    }
    
    //register to non existing tourney
    @Test
    void testRegisterViewerInvalidIndex()
    {
    	String result = restTemplate.getForObject("/registerViewer/123/localhost/9090", String.class);
        assertTrue(result.contains("Not a valid tournament index"));
    }
    
    //unregister from tourney
    @Test
    void testUnregisterViewer()
    {
    	restTemplate.getForObject("/add/tournament", String.class);
        restTemplate.getForObject("/registerViewer/0/localhost/9090", String.class);
        String result = restTemplate.getForObject("/unregisterViewer/0/localhost/9090", String.class);
        assertTrue(result.contains("unregistered"));
    }
    
    //unregister fail when invalid index
    @Test
    void testUnregisterViewerInvalidIndex()
    {
    	String result = restTemplate.getForObject("/unregisterViewer/12/localhost/9090", String.class);
        assertTrue(result.contains("Not a valid tournament index"));
    }
    
    //connect() fills tournaments list
    @Test
    void testConnectFillsList()
    {
        restTemplate.getForObject("/add/tournament", String.class);
        restTemplate.getForObject("/add/tournament", String.class);

        TournamentModel model = new TournamentModel("localhost", String.valueOf(port));
        model.connect("localhost", String.valueOf(port));

        assertEquals(2, model.tournaments.size());
    }
    
    //refresh fills list with new tourney
    @Test
    void testRefreshRefillsList()
    {
        restTemplate.getForObject("/add/tournament", String.class);

        TournamentModel model = new TournamentModel("localhost", String.valueOf(port));
        model.connect("localhost", String.valueOf(port));
        assertEquals(1, model.tournaments.size());

        restTemplate.getForObject("/add/tournament", String.class);
        model.refreshTournament();
        assertEquals(2, model.tournaments.size());
    }
    
    //connect() returns false when server not reached
    @Test
    void testConnectFailsWithBadServer()
    {
        TournamentModel model = new TournamentModel("localhost", String.valueOf(port));
        boolean result = model.connect("localhost", "9999");
        assertFalse(result);
    }

    //connect() returns true when server reached
    @Test
    void testConnectSucceeds()
    {
        TournamentModel model = new TournamentModel("localhost", String.valueOf(port));
        boolean result = model.connect("localhost", String.valueOf(port));
        assertTrue(result);
    }

    //selectTournament sets selected
    @Test
    void testSelectTournamentSetsSelected()
    {
        restTemplate.getForObject("/add/tournament", String.class);
        TournamentModel model = new TournamentModel("localhost", String.valueOf(port));
        model.connect("localhost", String.valueOf(port));
        model.selectTournament(model.tournaments.get(0));
        assertNotNull(model.getSelectedTournament());
    }

    //unselectTournament clears selectedTournament
    @Test
    void testUnselectTournamentClearsSelected()
    {
        restTemplate.getForObject("/add/tournament", String.class);
        TournamentModel model = new TournamentModel("localhost", String.valueOf(port));
        model.connect("localhost", String.valueOf(port));
        model.selectTournament(model.tournaments.get(0));
        model.unselectTournament(model.tournaments.get(0));
        assertNull(model.getSelectedTournament());
    }

    //list is empty when connect fails
    @Test
    void testListEmptyWhenConnectFails()
    {
        TournamentModel model = new TournamentModel("localhost", String.valueOf(port));
        model.connect("localhost", "9999");
        assertEquals(0, model.tournaments.size());
    }
    
           

}
