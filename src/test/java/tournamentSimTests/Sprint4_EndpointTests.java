package tournamentSimTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import sprint2.SpringBootServer;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
classes = SpringBootServer.class)
class Sprint4_EndpointTests 
{
	@Autowired
    private TestRestTemplate restTemplate;
    
    //reset server state before each test
    @BeforeEach
    void reset()
    {
        restTemplate.getForObject("/reset", String.class);
    }
    
    @Test
    void testAddWinStreakReturnsConfirm()
    {
        restTemplate.getForObject("/add/tournament", String.class);
        String result = restTemplate.getForObject("/decorate/winstreak/0", String.class);
        assertTrue(result.contains("winstreak"));
    }
    
    @Test
    void testAddOvertimeReturnsConfirm()
    {
        restTemplate.getForObject("/add/tournament", String.class);
        String result = restTemplate.getForObject("/decorate/overtime/0", String.class);
        assertTrue(result.contains("overtime"));
    }
    
    @Test
    void testAddDecInvalidIndex()
    {
        String result = restTemplate.getForObject("/decorate/winstreak/99", String.class);
        assertEquals("Not a valid tournament index", result);
    }
    
    @Test
    void testAddUnknownDecReturnsError()
    {
        restTemplate.getForObject("/add/tournament", String.class);
        String result = restTemplate.getForObject("/decorate/unknown/0", String.class);
        assertTrue(result.contains("Unknown"));
    }
    
    @Test
    void testStartWithWinStreakRuns()
    {
        restTemplate.getForObject("/add/tournament", String.class);
        restTemplate.getForObject("/decorate/winstreak/0", String.class);
        String result = restTemplate.getForObject("/start/0", String.class);
        assertTrue(result.contains("winner"));
    }

    @Test
    void testStartWithOTRuns()
    {
        restTemplate.getForObject("/add/tournament", String.class);
        restTemplate.getForObject("/decorate/overtime/0", String.class);
        String result = restTemplate.getForObject("/start/0", String.class);
        assertTrue(result.contains("winner"));
    }

    @Test
    void testStartWithBothDecsRuns()
    {
        restTemplate.getForObject("/add/tournament", String.class);
        restTemplate.getForObject("/decorate/winstreak/0", String.class);
        restTemplate.getForObject("/decorate/overtime/0", String.class);
        String result = restTemplate.getForObject("/start/0", String.class);
        assertTrue(result.contains("winner"));
    }
    
    
    
    
    

}
