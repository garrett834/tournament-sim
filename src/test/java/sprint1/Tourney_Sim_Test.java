package sprint1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Tourney_Sim_Test 
{
	OnlyDefectRobot p1;
	PrisonerOppositeRobot p2;
	PrisonerSameRobot p3;
	OnlyDefectRobot p4;
	PrisonerDelimmaGame g1;
	RoundRobinTournament t1;

	@BeforeEach
	void setUp() throws Exception 
	{
		p1 = new OnlyDefectRobot("Joe",0,0);
		p2 = new PrisonerOppositeRobot("Gary",0,0);
		p3 = new PrisonerSameRobot("Whit",0,0);
		p4 = new OnlyDefectRobot("Tom",0,0);
		g1 = new PrisonerDelimmaGame();
		t1 = new RoundRobinTournament();
	}

	@Test
	void testGame() 
	{
		Robot winner = g1.playGame(p1, p2);
		//winner should never be null
		assertNotNull(winner);
		//winner should be p1 for this game
		assertTrue(winner == p1);
		//test correct scoring
		assertEquals(50,p1.score);
		assertEquals(0,p2.score);
		//test record updating
		assertEquals(1,p1.record);
		assertEquals(-1,p2.record);
		assertEquals("Gary", p1.getOpponentName(p2));
	    assertEquals("Joe", p2.getOpponentName(p1));
	}
	@Test
	void testTieGame()
	{
		//test tie. should give the win to p1 but not update records
		Robot winnerTie = g1.playGame(p1,p4);
		//give tie game win to first player which will not affect rr tourney results
		assertTrue(winnerTie == p1);
		//still should update scores
		assertEquals(10,p1.score);
		assertEquals(10,p4.score);
		//should not update records
		assertEquals(0,p1.record);
		assertEquals(0,p4.record);
	}
	
	@Test
	void testTournament()
	{
		t1.participants.add(p2);
		t1.participants.add(p1);
		t1.participants.add(p3);
		t1.game = g1;
		
		Robot tournamentWinner = t1.runTournament();
		assertEquals(p1,tournamentWinner);
		//joe the only defect robot won both games
		assertEquals(2,p1.record);
		//opposite robot lost one game and tied the other
		assertEquals(-1,p2.record);
		//same robot lost one game and tied the other
		assertEquals(-1,p3.record);
	}
	
	 @Test
	 void testSinglePartTournament()
	 {
		 //tourney w/ one robot
	     t1.participants.add(p1);
	     t1.game = g1;

	     Robot winner = t1.runTournament();
	     assertEquals(p1, winner);
	     assertEquals(0,p1.record); //no record change
	 }
	 
	 //fake move observer just for testing that game notifies observers w/out having to write to file
	 class FakeScoreObserver implements ScoreObserver 
	 {
	        String lastScore;

	        @Override
	        public void updateScore(String score) 
	        {
	            lastScore = score;
	        }
	 }

	    @Test
	    void testScoreObserver()
	    {
	        FakeScoreObserver fakeObserver = new FakeScoreObserver();
	        g1.registerScoreObserver(fakeObserver);

	        g1.playGame(p1, p2);

	        //make sure observer was notified
	        assertNotNull(fakeObserver.lastScore);
	        //make sure observer gets string that contains correct scoring info 
	        assertTrue(fakeObserver.lastScore.contains("Joe scored 5") || fakeObserver.lastScore.contains("Gary scored 0"));
	    }
	    
	    @Test
	    void testUnregisterScoreObserver() 
	    {
	        FakeScoreObserver fakeObserver = new FakeScoreObserver();
	        g1.registerScoreObserver(fakeObserver);

	        //remove observer
	        g1.unregisterScoreObserver(fakeObserver);

	        // call update
	        g1.notifyScoreObserver("Test score");

	        //should not get notified
	        assertNull(fakeObserver.lastScore);
	    }

	    //fake move observer for testing that game notifies observers w/out writing to file
	    class FakeMoveObserver implements MoveObserver 
	    {
	        String lastMove;

	        @Override
	        public void updateMove(String move) 
	        {
	            lastMove = move;
	        }
	    }

	    @Test
	    void testMoveObserver()
	    {
	        FakeMoveObserver fakeMoveObserver = new FakeMoveObserver();
	        g1.registerMoveObserver(fakeMoveObserver);

	        g1.playGame(p1, p2);
	        
	        //make sure observer was notified
	        assertNotNull(fakeMoveObserver.lastMove);
	        //make sure move observer gets move info
	        assertTrue(fakeMoveObserver.lastMove.contains("Joe picked Defect") || fakeMoveObserver.lastMove.contains("Gary picked Cooperate"));
	    }
	    
	    @Test
	    void testUnregisterMoveObserver() 
	    {
	        FakeMoveObserver fakeObserver = new FakeMoveObserver();
	        g1.registerMoveObserver(fakeObserver);
	        //remove
	        g1.unregisterMoveObserver(fakeObserver);
	        //notify
	        g1.notifyMoveObserver("Test move");
	        //shouldn't notify
	        assertNull(fakeObserver.lastMove);
	    }
	    	    
	    @Test
	    void testMoveLogging() throws FileNotFoundException
	    {
	    	MoveLoggingSystem logger = new MoveLoggingSystem();

	        String expectedMove = "Robot1: Cooperate";

	        //call move update 
	        logger.updateMove(expectedMove);

	        //open the move file 
	        File file = new File("move_file.txt");
	        Scanner scanner = new Scanner(file);

	        boolean found = false;

	        //check if expected move is in file
	        while(scanner.hasNextLine())
	        {
	            if(scanner.nextLine().equals(expectedMove))
	            {
	                found = true;
	                break;
	            }
	        }

	        scanner.close();

	        assertTrue(found);
	    }
	    
	    @Test
	    void testScoreLogging() throws FileNotFoundException
	    {
	        ScoreLoggingSystem logger = new ScoreLoggingSystem();

	        String expectedScore = "Robot1: 10 Robot2: 5";

	        //call score update
	        logger.updateScore(expectedScore);

	        //open score file
	        File file = new File("scores_file.txt");
	        Scanner scanner = new Scanner(file);

	        boolean found = false;

	        //search file for expected score
	        while(scanner.hasNextLine())
	        {
	            if(scanner.nextLine().equals(expectedScore))
	            {
	                found = true;
	                break;
	            }
	        }

	        scanner.close();

	        assertTrue(found);
	    }
	    
	    @Test
	    void testSameRobot()
	    {
	    	//no previous decision
	        assertEquals("Cooperate", p3.makeDecision());
	        
	        p3.oppsPrevDecision = "Cooperate";
	        assertEquals("Cooperate", p3.makeDecision());
	        
	        p3.oppsPrevDecision = "Defect";

	        assertEquals("Defect", p3.makeDecision());
	    }
	    
	    @Test
	    void testOppositeRobot()
	    {
	    	//no previous decision
	        assertEquals("Cooperate", p2.makeDecision());
	        
	        p2.oppsPrevDecision = "Cooperate";
	        assertEquals("Defect", p2.makeDecision());
	        
	        p2.oppsPrevDecision = "Defect";

	        assertEquals("Cooperate", p2.makeDecision());
	        
	        
	    }
	    
	    @Test
	    void testPlayerTwoWinning()
	    {
	    	Robot winner = g1.playGame(p2, p1);

	        assertEquals(p1, winner);
	        assertTrue(p1.score > p2.score);
	    }
	    
}
