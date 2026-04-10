package sprint1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
                classes = TournamentClient.class)

class ClientServerTest
{
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void reset()
    {
        restTemplate.getForObject("/resetClient", String.class);
    }

    //null prev. decision should cooperate
    @Test
    void testDecisionNullPrev()
    {
        String result = restTemplate.getForObject("/decision/null", String.class);
        assertEquals("Cooperate", result);
    }

    //opponent cooperated last, should defect
    @Test
    void testDecisionOpponentCooperated()
    {
        String result = restTemplate.getForObject("/decision/Cooperate", String.class);
        assertEquals("Defect", result);
    }

    //opponent defected last, should cooperate
    @Test
    void testDecisionOpponentDefected()
    {
        String result = restTemplate.getForObject("/decision/Defect", String.class);
        assertEquals("Cooperate", result);
    }

    //reset should return confirm message
    @Test
    void testResetMessage()
    {
        String result = restTemplate.getForObject("/resetClient", String.class);
        assertEquals("Client bot reset", result);
    }

    //should be null after reset, so should defualt to cooperate
    @Test
    void testStateCleanAfterReset()
    {
        restTemplate.getForObject("/decision/Cooperate", String.class);
        restTemplate.getForObject("/reset", String.class);
        String result = restTemplate.getForObject("/decision/null", String.class);
        assertEquals("Cooperate", result);
    }
    
    @Test
    void testTournamentClientRobotConstructor()
    {
        TournamentClient client = new TournamentClient(new OnlyDefectRobot("Whit", 0, 0));
        assertNotNull(client.clientBot);
        assertEquals("Whit", client.clientBot.name);
    }
}
