package com.dius.game.bowling;

public class BowlingPlay implements Play {

	private Frame current;
	private Frame start;
	private Frame end;

	//initialize the bowling game with 10 frames and each frame is linked to the previous and next frame
	public BowlingPlay() {
		start = new Frame(1);
		Frame prev = start;
		int i = 2;
		while (i <= 9) {
			Frame curr = new Frame(i);
			prev.next = curr;
			curr.prev = prev;
			prev = curr;
			i++;
		}
		
		Frame frame = new  EndFrame(10);
		frame.prev = prev;
		end = frame;
		prev.next = end;
		current = start;
		System.out.println("Game set. Start rolling");
	}

	
	private class Frame {
		boolean isStrike;
		boolean isSpare;
		int try1Score = -1;
		private int try2Score = -1;
		Frame next;
		Frame prev;
		private int frameNo;
		
		Frame(int i)
		{
			this.frameNo = i;
		}

		void acceptRoll(int noOfPins) {
			// ensure noOfPins is less than equal to 10 and greater than equal to 0

			// following if else determines the try being played
			System.out.println(String.format("Playing frame number %d. Roll no %d",frameNo,try1Score==-1?1:2));
			if (try1Score == -1) {
				try1Score = noOfPins;
				if (noOfPins == 10) {
					System.out.println("Congrats you scored a strike");
					isStrike = true;
					loadNextPlay();
				}
				System.out.println("You rolled a "+try1Score);
			} else {
				if (try1Score + noOfPins > 10) {
					throw new IllegalArgumentException("A frame has maximum of 10 pins");
				}
				try2Score = noOfPins;
				if (try1Score + try2Score == 10) {
					isSpare = true;
					System.out.println("Congrats you scored a spare in your second trys");
				}
				System.out.println("You rolled a "+try2Score);
				loadNextPlay();
			}

		}

		void loadNextPlay() {
			current = next;
		}

	}
	
	
	private class EndFrame extends Frame{
				
		EndFrame(int i) {
			super(i);
		}



		private boolean additionalBowls = false;
		private boolean additionalBowlsPlayed = false;
		private int additionaBowl1 = -1;
		private int additionaBowl2 = -1;
		
		@Override
		void acceptRoll(int noOfPins) {
			if(additionalBowls)
			{
				if(super.isSpare)
				{
					additionaBowl1  = noOfPins;
					additionalBowlsPlayed = true;
					loadNextPlay();
				}
				else
				{
					if(additionaBowl1 != -1)
					{
						additionaBowl2  = noOfPins;
						additionalBowlsPlayed = true;
						loadNextPlay();
					}
					else 
					{
						additionaBowl1  = noOfPins;
					}
				}
			}
			else
			{
				super.acceptRoll(noOfPins);
			}
		}



		void loadNextPlay() {
			if((super.isSpare || super.isStrike ) && !additionalBowlsPlayed)
			{
				additionalBowls = true;
				
			}
			else
			{
				current = null;
				System.out.println("All frames Played . Thanks for playing");
			}
		}

	}
	
	
	


	@Override
	public void roll(int noOfPins) {
		if (current!=null) {

			if (noOfPins > 10) {
				throw new IllegalArgumentException("A roll has maximum of 10 pins");
			}
			if (noOfPins < 0) {
				throw new IllegalArgumentException("A roll has can not have less than 10 ponts");
			}

			current.acceptRoll(noOfPins);	
		}
		else
		{
			System.out.println("All frames have been played and game is over");
		}

	}

	@Override
	public int score() {
		Frame curr_frame = start;
		int score = 0;
		while(curr_frame!=null)
		{
			
			if(curr_frame instanceof EndFrame)
			{
				///End frame score calculation will be different
				score = endFrameScore(curr_frame, score);
			}
			else if (curr_frame.isStrike)
			{
				score = calculateStrikeFrameScore(curr_frame, score);
			}
			else if(curr_frame.isSpare)
			{
				score = score += 10;
				
				//add next bowl score
				score = score + (curr_frame.next!=null && curr_frame.next.try1Score >= 0 ? curr_frame.next.try1Score : 0);
			}
			else
			{
				score = score + (curr_frame.try1Score >= 0 ? curr_frame.try1Score : 0);
				score = score + (curr_frame.try2Score >= 0 ? curr_frame.try2Score : 0);
			}
			System.out.println(String.format("Score after end of frames %d is %d",curr_frame.frameNo,score));
			curr_frame = curr_frame.next;
			
		}
		return score;
	}

	private int calculateStrikeFrameScore(Frame curr_frame, int score) {
		score = score += 10;
		
		if(curr_frame.next!=null)
		{
			//in case even next bowl is a strike
			if(curr_frame.next.isStrike)
			{
				score = score + 10;
				
				//special case calculation in case the next frame is 10th frame
				if(curr_frame.next instanceof EndFrame)
				{
					EndFrame ef = (EndFrame)curr_frame.next;
					score = score + (ef.additionaBowl1!=-1?ef.additionaBowl1:0); 	
				}
				else
				{
					//get the 3rd frame 1st try score
					score = score + (curr_frame.next.next!=null && curr_frame.next.next.try1Score >= 0 ? curr_frame.next.next.try1Score : 0);
				}
				
			}
			else
			{
				//add next 2 bowl scores
				score = score + (curr_frame.next!=null && curr_frame.next.try1Score >= 0 ? curr_frame.next.try1Score : 0);
				score = score + (curr_frame.next!=null && curr_frame.next.try2Score >= 0 ? curr_frame.next.try2Score : 0);
			}
		}
		return score;
	}

	private int endFrameScore(Frame curr_frame, int score) {
		EndFrame ef = (EndFrame)curr_frame;
		score = score + (curr_frame.try1Score!=-1?curr_frame.try1Score:0);
		score = score + (curr_frame.try2Score!=-1?curr_frame.try2Score:0);
		score = score + (ef.additionaBowl1!=-1?ef.additionaBowl1:0);
		score = score + (ef.additionaBowl2!=-1?ef.additionaBowl2:0);
		return score;
	}

}
