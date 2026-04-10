package sprint1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
 
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
                classes = SpringBootServer.class) 
class Sprint_2_Test
{
	//using TestRestTemplate cause I have spring 3.2 and RestTestClient needs 4.
    @Autowired
    private TestRestTemplate restTemplate;
    
    //reset server state before each test cause i was getting inconsistent results
    @BeforeEach
    void reset()
    {
        restTemplate.getForObject("/reset", String.class);
    }
    //new server starts with empty tournament list
    @Test
    void testViewTournamentsEmpty()
    {
        String result = restTemplate.getForObject("/tournaments", String.class);
        assertEquals("Tournaments: []", result);
    }
    //test creation of tournament
    @Test
    void testCreateTournament()
    {
        String result = restTemplate.getForObject("/add/tournament", String.class);
        assertTrue(result.contains("Tournament created at index"));
    }
    //make sure tournament in array after adding
    @Test
    void testViewTournamentsAfterCreate()
    {
        restTemplate.getForObject("/add/tournament", String.class);

        String result = restTemplate.getForObject("/tournaments", String.class);
        assertTrue(result.contains("RoundRobin"));
    }
    @Test
    void testRemoveTournament()
    {
        restTemplate.getForObject("/add/tournament", String.class);

        String result = restTemplate.getForObject("/remove/0", String.class);
        assertTrue(result.contains("removed"));
    }
    //test error handling for bad index given
    @Test
    void testRemoveTournamentInvalidIndex()
    {
        String result = restTemplate.getForObject("/remove/0", String.class);
        assertEquals("Not a valid tournament index", result);
    }
    //check registering remote robot
    @Test
    void testRegisterRemoteRobot()
    {
        restTemplate.getForObject("/add/tournament", String.class);

        String result = restTemplate.getForObject("/register/robot/Gary/localhost/8081/0", String.class);
        assertTrue(result.contains("Gary"));
    }
    //test error for registering remote to out of index tournament
    @Test
    void testRegisterRemoteRobotInvalidIndex()
    {
        String result = restTemplate.getForObject("/register/robot/Gary/localhost/8081/50", String.class);
        assertEquals("Not a valid tournament index", result);
    }
    //make sure same robot doesn't enter tournament twice
    @Test
    void testRegisterRemoteRobotDuplicate()
    {
        restTemplate.getForObject("/add/tournament", String.class);
        restTemplate.getForObject("/register/robot/Gary/localhost/8081/0", String.class);

        String result = restTemplate.getForObject("/register/robot/Gary/localhost/8081/0", String.class);
        assertTrue(result.contains("already registered"));
    }
    @Test
    void testRegisterHumanRobot()
    {
        restTemplate.getForObject("/add/tournament", String.class);

        String result = restTemplate.getForObject("/register/human/Gary/0", String.class);
        assertTrue(result.contains("Gary"));
    }
    @Test
    void testRegisterHumanRobotInvalidIndex()
    {
        String result = restTemplate.getForObject("/register/human/Gary/99", String.class);
        assertEquals("Not a valid tournament index", result);
    }
    //test start tournament runs full torunament and gives a winner
    @Test
    void testStartTournament()
    {
        restTemplate.getForObject("/add/tournament", String.class);
        String result = restTemplate.getForObject("/start/0", String.class);
        assertTrue(result.contains("winner"));
    }
    //start invalid index should error message
    @Test
    void testStartTournamentInvalidIndex()
    {
        String result = restTemplate.getForObject("/start/99", String.class);
        assertEquals("Not a valid tournament index", result);
    }
    //make sure RemoteClientBot data is correct
    @Test
    void testRemoteClientRobot()
    {
        RemoteClientRobot robot = new RemoteClientRobot("Gary", "localhost", "8081");
        assertEquals("Gary", robot.name);
        assertEquals("localhost", robot.getIp());
        assertEquals("8081", robot.getPort());
    }
    //default to cooperate if connection is lost
    @Test
    void testRemoteClientRobotFallback()
    {
        RemoteClientRobot robot = new RemoteClientRobot("Gary", "localhost", "9999");
        assertEquals("Cooperate", robot.makeDecision());
    }
    @Test
    void testRemoteClientRobotWithPrevDecision()
    {
        RemoteClientRobot robot = new RemoteClientRobot("Gary", "localhost", "9999");
        robot.opponentsPrevDecision = "Cooperate";
        assertEquals("Cooperate", robot.makeDecision());
    }
    //null prev. decision should fall back to cooperate
    @Test
    void testRemoteClientRobotNullPrevDecision()
    {
        RemoteClientRobot robot = new RemoteClientRobot("Garrett", "localhost", "9999");
        robot.opponentsPrevDecision = null;
        assertEquals("Cooperate", robot.makeDecision());
    }
    //test correct decision based on prev. decision
    @Test
    void testDecisionWithPrevDecision() {
        String decision = restTemplate.getForObject("/decision/Defect", String.class);
        // OnlyDefectRobot should always defect
        assertEquals("Cooperate", decision);
    }
    //handling of invalid prev. decision input
    @Test
    void testDecisionWithInvalidPrevDecision() {
        String decision = restTemplate.getForObject("/decision/invalid", String.class);
        assertEquals("Cooperate", decision);
    }
    //test human entering defect w/ System.setIn
    @Test
    void testHumanRobotDefectDecision() {
        //simulate user typing "Defect"
        String input = "Defect\n";
        //ByteArrayInputStream converts text into bytes java can read
        InputStream in = new ByteArrayInputStream(input.getBytes());
        //System.setIn tells java to look at in variable instead of user input for testing
        System.setIn(in);

        HumanRobot human = new HumanRobot("Test");
        String decision = human.makeDecision();
        assertEquals("Defect", decision);
    }
    //should defect to cooperate when given gibberish
    @Test
    void testHumanRobotInvalidDecision() {
        String input = "hjgjhuy\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        HumanRobot human = new HumanRobot("Tester");
        String decision = human.makeDecision();

        assertEquals("Cooperate", decision);
    }
    //for opponents prev. decision != null
    @Test
    void testHumanRobotWithPreviousDecision() {
        //human typing "Cooperate"
        String simulatedInput = "Cooperate\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        HumanRobot human = new HumanRobot("Test");
        //set prev. decision to defect
        human.opponentsPrevDecision = "Defect";

        String decision = human.makeDecision();
        assertEquals("Cooperate", decision);
        assertEquals("Defect", human.opponentsPrevDecision);
    }
    //test root page html
    @Test
    void testRootHtml() {
        String result = restTemplate.getForObject("/", String.class);

        String expected = 
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

        assertEquals(expected, result);
    }
}
	