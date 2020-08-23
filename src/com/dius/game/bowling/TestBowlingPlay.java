package com.dius.game.bowling;

public class TestBowlingPlay {
public static void main(String[] args) {
	Play play = new BowlingPlay();
	///mock playing 10 frames
	
	//	play.roll(10);
	//	play.roll(10);
	//	play.roll(10);
	//	play.roll(10);
	//	play.roll(10);
	//	play.roll(10);
	//	play.roll(10);
	//	play.roll(10);
	//	play.roll(10);
	//	play.roll(10);
	//	play.roll(10);
	//	play.roll(10);
	//	
	//	System.out.println("Your score is "+play.score());

	//1st Use case
	
	play = new BowlingPlay();
	
	play.roll(4);
	play.roll(4);
	
	System.out.println("Your score is when bowler rolls (4,4) :: "+play.score());
	
	//2nd Use case
	play = new BowlingPlay();
	
	play.roll(4);
	play.roll(6);
	
	play.roll(5);
	play.roll(0);
	
	System.out.println("Your score is when bowler rolls (4,6 | 5, 0) :: "+play.score());
	
	//3rd Use case
	play = new BowlingPlay();
	
	play.roll(10);
	
	play.roll(5);
	play.roll(4);
	
	System.out.println("Your score is when bowler rolls (10 | 5, 4) :: "+play.score());

}
}
