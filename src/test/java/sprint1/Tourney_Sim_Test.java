package sprint1;

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
		t1.participants.add(p1);
		t1.participants.add(p2);
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
	 void testSingleParticipantTournament()
	 {
		 //tourney w/ one robot
	     t1.participants.add(p1);
	     t1.game = g1;

	     Robot winner = t1.runTournament();
	     assertEquals(p1, winner);
	     assertEquals(0,p1.record); //no change in record
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

	    //fake move observer just for testing that game notifies observers w/out writing to file
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

}
