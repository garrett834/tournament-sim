package tournamentSimTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sprint1.Game;
import sprint1.MoveObserver;
import sprint1.OnlyDefectRobot;
import sprint1.PrisonerDelimmaGame;
import sprint1.PrisonerOppositeRobot;
import sprint1.PrisonerSameRobot;
import sprint1.Robot;
import sprint1.RoundRobinTournament;
import sprint4.GameDecorator;
import sprint4.OvertimeDecorator;
import sprint4.WinStreakBonusDecorator;


class Sprint4_UnitTests {

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
	void testOTDecWrapsGame() 
	{
		OvertimeDecorator otDec = new OvertimeDecorator(g1);
		assertEquals(g1,otDec.getDecGame());	
	}
	
	@Test
	void testWinStreakWrapsGame()
	{
		WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
		assertEquals(g1,wsDec.getDecGame());
	}
	
	@Test
	void testDecGameRounds()
	{
		WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
		assertEquals(g1.gameRounds,wsDec.gameRounds);
	}
	
	@Test
	void testDecGameObs()
	{
		OvertimeDecorator otDec = new OvertimeDecorator(g1);
		assertSame(g1.MoveObservers,otDec.MoveObservers);
		assertSame(g1.ScoreObservers,otDec.ScoreObservers);	
	}
	
	@Test
	void testMultipleDecs()
	{
		OvertimeDecorator otDec = new OvertimeDecorator(g1);
		WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(otDec);
		assertEquals(otDec,wsDec.getDecGame());
		assertEquals(g1,otDec.getDecGame());	
	}
	
	class FakeMoveObserver implements MoveObserver 
    {
        int callCount;

        @Override
        public void updateMove(String move) 
        {
            callCount++;
        }
    }
	
	@Test
	void testTieTriggersOT()
	{
		OvertimeDecorator otDec = new OvertimeDecorator(g1);
		//register a move observer to otDec game to count calls
		FakeMoveObserver counter = new FakeMoveObserver();
		otDec.registerMoveObserver(counter);
		//2 only defects should tie
		otDec.playGame(p1,p4);
		//should be 27 b/c 20 during regulation then 6 in overtime + 1 that overtime starting
		assertEquals(27, counter.callCount);
		
	}
	
	@Test
	void testMultDecOvertimeTriggered()
	{
	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
	    OvertimeDecorator otDec = new OvertimeDecorator(wsDec);

	    FakeMoveObserver counter = new FakeMoveObserver();
	    otDec.registerMoveObserver(counter);

	    otDec.playGame(p1, p4);

	    assertTrue(counter.callCount > 20);
	}
	
	@Test
	void testNoTieNoOT()
	{
		OvertimeDecorator otDec = new OvertimeDecorator(g1);
		//register a move observer to otDec game to count calls
		FakeMoveObserver counter = new FakeMoveObserver();
		otDec.registerMoveObserver(counter);
		//should be no tie
		otDec.playGame(p1,p3);
		//should be just 20
		assertEquals(20, counter.callCount);	
	}
	
	@Test
	void testRecordAfterOtTie()
	{
		OvertimeDecorator otDec = new OvertimeDecorator(g1);
		Robot p5 = new OnlyDefectRobot("a",0,0);
		Robot p6 = new OnlyDefectRobot("b",0,0);
		Robot winner = otDec.playGame(p5,p6);
		//tie after ot results in no record change but returns p5 winning
		assertEquals(winner,p5);
		assertEquals(0,winner.record);
	
	}
	
	class TestRobot extends Robot
	{
	    String[] decisions;
	    int index = 0;

	    public TestRobot(String name, String[] decisions)
	    {
	        super(name, 0, 0);
	        this.decisions = decisions;
	    }

	    @Override
	    public String makeDecision()
	    {
	        return decisions[index++];
	    }
	}
	
	@Test
	void testP1OvertimeRecordChange()
	{
		String[] p1Moves = {"Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect"};
		String[] p2Moves = {"Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Cooperate","Cooperate","Cooperate"};
		
		TestRobot p7 = new TestRobot("c",p1Moves);
		TestRobot p8 = new TestRobot("d",p2Moves);
		
		OvertimeDecorator otDec = new OvertimeDecorator(g1);
		Robot winner = otDec.playGame(p7,p8);
		assertEquals(p7,winner);
		assertEquals(1,p7.record);
		assertEquals(-1,p8.record);	
		
		
	}
	
	@Test
	void testP2OvertimeRecordChange()
	{
	    // p2 defects in OT, p1 cooperates — p2 wins
	    String[] p1Moves = {"Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Cooperate","Cooperate","Cooperate"};                     
	    String[] p2Moves = {"Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect"};
	                        
	    TestRobot p9 = new TestRobot("c", p1Moves);
	    TestRobot p10 = new TestRobot("d", p2Moves);

	    OvertimeDecorator otDec = new OvertimeDecorator(g1);
	    Robot winner = otDec.playGame(p9, p10);
	    assertEquals(p10, winner);
	    assertEquals(1, p10.record);
	    assertEquals(-1, p9.record);
	}
	
	@Test
	void testWinStreakWinnerRecordInc()
	{
	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
	    Robot winner = wsDec.playGame(p1, p3);
	    assertEquals(1, winner.record);
	}

	@Test
	void testWinStreakLoserRecordDec()
	{
	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
	    wsDec.playGame(p1, p3);
	    assertEquals(-1, p3.record);
	}

	@Test
	void testBonusAwardedOnStreak()
	{
	    String[] p1Moves = {"Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect"};
	    String[] p2Moves = {"Cooperate","Cooperate","Cooperate","Cooperate","Cooperate","Cooperate","Cooperate","Cooperate","Cooperate","Cooperate"};
	                        
	    TestRobot p1 = new TestRobot("a", p1Moves);
	    TestRobot p2 = new TestRobot("b", p2Moves);

	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
	    wsDec.playGame(p1, p2);
	    //50 base points + 15 extra from 3 streaks
	    assertEquals(65, p1.score);
	}

	@Test
	void testBonusNotAwardedWithoutStreak()
	{
	    //p1 and p2 alternate winning
	    String[] p1Moves = {"Defect","Cooperate","Defect","Cooperate","Defect","Cooperate","Defect","Cooperate","Defect","Cooperate"};
	    String[] p2Moves = {"Cooperate","Defect","Cooperate","Defect","Cooperate","Defect","Cooperate","Defect","Cooperate","Defect"};
	                        
	    TestRobot p1 = new TestRobot("a", p1Moves);
	    TestRobot p2 = new TestRobot("b", p2Moves);

	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(new PrisonerDelimmaGame());
	    wsDec.playGame(p1, p2);
	    //no streaks cause alternate so 25 points to each
	    assertEquals(25, p1.score);
	    assertEquals(25, p2.score);
	}

	@Test
	void testStreakResetsAfterBonus()
	{
	    //p1 wins 3 in a row, gets bonus, then streak resets then repeats
	    String[] p1Moves = {"Defect","Defect","Defect","Defect","Defect","Defect","Cooperate","Cooperate","Cooperate","Cooperate"};
	    String[] p2Moves = {"Cooperate","Cooperate","Cooperate","Cooperate","Cooperate","Cooperate", "Cooperate","Cooperate","Cooperate","Cooperate"};

	    TestRobot p1 = new TestRobot("a", p1Moves);
	    TestRobot p2 = new TestRobot("b", p2Moves);

	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
	    wsDec.playGame(p1, p2);
	    assertEquals(52, p1.score);
	}

	@Test
	void testP2BonusAwarded()
	{
	    //p2 wins every round
	    String[] p1Moves = {"Cooperate","Cooperate","Cooperate","Cooperate","Cooperate","Cooperate","Cooperate","Cooperate","Cooperate","Cooperate"};                    
	    String[] p2Moves = {"Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect"};

	    TestRobot p1 = new TestRobot("a", p1Moves);
	    TestRobot p2 = new TestRobot("b", p2Moves);

	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
	    wsDec.playGame(p1, p2);

	    assertEquals(65, p2.score);
	}

	@Test
	void testTieNoRecordChange()
	{
	    // both defect every round
	    String[] p1Moves = {"Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect"};                   
	    String[] p2Moves = {"Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect","Defect"};
	                        
	    TestRobot p1 = new TestRobot("a", p1Moves);
	    TestRobot p2 = new TestRobot("b", p2Moves);

	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
	    Robot winner = wsDec.playGame(p1, p2);

	    assertEquals(p1, winner);
	    assertEquals(0, p1.record);
	    assertEquals(0, p2.record);
	}

	@Test
	void testStackedWithOvertime()
	{
	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
	    OvertimeDecorator otDec = new OvertimeDecorator(wsDec);
	    Robot winner = otDec.playGame(p1, p3);
	    assertNotNull(winner);
	    assertEquals(1, winner.record);
	}
	
	@Test
	void testPlayRoundDelegates()
	{
	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
	    wsDec.playRound(p1, p3);
	    assertTrue(p1.score > 0 || p3.score > 0);
	}
	
	@Test
	void testSetDecGameUpdatesReference()
	{
	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
	    PrisonerDelimmaGame newGame = new PrisonerDelimmaGame();
	    wsDec.setDecGame(newGame);
	    assertEquals(newGame, wsDec.getDecGame());
	}

	@Test
	void testSetDecGameUpdatesGameRounds()
	{
	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(g1);
	    PrisonerDelimmaGame newGame = new PrisonerDelimmaGame();
	    newGame.gameRounds = 5;
	    wsDec.setDecGame(newGame);
	    assertEquals(5, wsDec.gameRounds);
	}
	
	class SimpDecorator extends GameDecorator
	{
	    public SimpDecorator(Game game)
	    {
	        super(game);
	    }
	}
	
	@Test
	void testPlayGameDelegates()
	{
	    SimpDecorator decorator = new SimpDecorator(g1);
	    Robot winner = decorator.playGame(p1, p3);
	    assertNotNull(winner);
	}
	
	@Test
	void testStackWinStreakOnOvertime()
	{
	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(new PrisonerDelimmaGame());
	    OvertimeDecorator otDec = new OvertimeDecorator(wsDec);
	    Robot winner = otDec.playGame(p1, p3);
	    assertNotNull(winner);
	    assertTrue(winner.record > 0);
	}

	@Test
	void testStackOvertimeOnWinStreak()
	{
	    OvertimeDecorator otDec = new OvertimeDecorator(new PrisonerDelimmaGame());
	    WinStreakBonusDecorator wsDec = new WinStreakBonusDecorator(otDec);
	    Robot winner = wsDec.playGame(p1, p3);
	    assertNotNull(winner);
	    assertTrue(winner.record > 0);
	}

}
