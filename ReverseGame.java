// Name: Reverse
// Purpose: Puzzle game that requires the user to sort numbers back into ascending order
// Programmer: Maxx Mudd on 6/3/2015

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import sun.audio.*;
public class ReverseGame extends JFrame implements ActionListener
{
	// arrays for reference and play
	int referenceArray[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
	int gameArray[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
	
	// frame components
	JLabel numbersLabel = new JLabel(numbers);
	JButton button = new JButton("Reverse");
	JTextField field = new JTextField("", 1);
	Font bigFont = new Font("Calibri", Font.BOLD, 32);
	static String numbers = "";
	Container con = getContentPane();
	
	// timer variables
	static long timeStart;
	static long timeEnd;
	static long time;
	static double elapsedSeconds;
	
	public ReverseGame()
	{
		// add elements to frame
		con.setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(button);
		button.setBounds(190, 120, 100, 25);
		add(field);
		field.setBounds(228, 84, 25, 25);
		
		// shuffle anbd display numbers
		shuffle();
		add(numbersLabel);
		numbersLabel.setFont(bigFont);
		numbersLabel.setBounds(111, 50, 270, 25);
		
		// action listener
		button.addActionListener(this);
	}
	public void paint(Graphics g)
	{
		super.paint(g);
	}
	public void actionPerformed(ActionEvent event)
	{
		String moveString =  field.getText();
		
		try
		{
			// get move choice
			int move =  Integer.parseInt(moveString);
			move -= 1;
			
			// throw exception if input is invalid
			if(move < 0 || move > 8)
			{
				// play error sound, throw exception
				errorSound();
				throw(new Exception());
			}
		
			// reverse numbers
			reverse(move);
	
			// add numbers to string
			numbers = "";
			for(int i = 0; i < gameArray.length; ++i)
			{
				numbers += gameArray[i];
				numbers += "  ";
			}
			
			// play swap sound
			swapSound();
			
			// set label and clear field
			numbersLabel.setText(numbers);
			checkWin();
			field.setText("");
			field.requestFocus();
			repaint();
		}
		catch(Exception exception)
		{
			// input validation
			JOptionPane.showMessageDialog(null, "Please enter a valid number (1 - 9).", "Reverse", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void shuffle()
	{
		// Fisher-Yates shuffle
		Random rand = new Random();
		int index, temp;
		numbers = "";
		
		for(int i = 0; i < gameArray.length; ++i)
		{
			index = rand.nextInt(i + 1);
			temp = gameArray[index];
			gameArray[index] = gameArray[i];
			gameArray[i] = temp;
			
		}
		// add shuffled numbers to string
		for(int i = 0; i < gameArray.length; ++i)
		{
			numbers += gameArray[i];
			numbers += "  ";
		}
		
		// set label and clear field
		numbersLabel.setText(numbers);
	}
	public void reverse(int m)
	{
		// create array with move pieces in reverse order
		int reverseArray[] = new int[m + 1];
		
		for(int i = 0; i < reverseArray.length; ++i)
		{
			reverseArray[i] = gameArray[m];
			--m;
		}
		
		// assign newly ordered pieces to game array
		for(int i = 0; i < reverseArray.length; ++i)
		{
			gameArray[i] = reverseArray[i];
		}
	}
	public void checkWin()
	{
		int winCounter = 0;
		
		// check to see if user has won
		for(int i = 0; i < gameArray.length; ++i)
		{
			if(referenceArray[i] == gameArray[i])
			{
				++winCounter;
			}
		}
		
		// if game is won, ask user if they want to play again
		if(winCounter == 9)
		{
			// stop timer, calculate elapsed time
			timeEnd = System.currentTimeMillis();
			time = timeEnd - timeStart;
			elapsedSeconds = time / 1000;
			
			// play win sound and display messages
			winSound();
			JOptionPane.showMessageDialog( null, "Congratulations! You completed the puzzle in " + elapsedSeconds + " seconds!", "Reverse", JOptionPane.INFORMATION_MESSAGE);
			int choice = JOptionPane.showConfirmDialog(null, "Play again?", "Reverse", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if(choice == JOptionPane.YES_OPTION)
			{
				// if yes, shuffle and reset timers
				timeStart = 0;
				timeEnd = 0;
				time = 0;
				elapsedSeconds = 0;
				shuffle();
				timeStart = System.currentTimeMillis();
			}
			else
			{
				// if no, exit
				System.exit(0);
			}
		}
	}
	public void errorSound()
	{
		try
		{
			// create audio stream for error sound
			String errorFile = "error.au";
			InputStream errorIn = new FileInputStream(errorFile);
			AudioStream errorStream = new AudioStream(errorIn);
			
			// play error sound
			AudioPlayer.player.start(errorStream);
		}
		catch(Exception e)
		{
			// if file is not found, display message and quit
			JOptionPane.showMessageDialog(null, "Could not find audio file.", "Reverse", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	public void swapSound()
	{
		try
		{
			// create audio stream for swap sound
			String swapFile = "swap.au";
			InputStream swapIn = new FileInputStream(swapFile);
			AudioStream swapStream = new AudioStream(swapIn);
			
			// play swap sound
			AudioPlayer.player.start(swapStream);
		}
		catch(Exception e)
		{
			// if file is not found, display message and quit
			JOptionPane.showMessageDialog(null, "Could not find audio file.", "Reverse", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	public void winSound()
	{
		try
		{
			// create audio stream for win sound
			String winFile = "win.au";
			InputStream winIn = new FileInputStream(winFile);
			AudioStream winStream = new AudioStream(winIn);
			
			// play win sound
			AudioPlayer.player.start(winStream);
		}
		catch(Exception e)
		{
			// if file is not found, display message and quit
			JOptionPane.showMessageDialog(null, "Could not find audio file.", "Reverse", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	public static void main(String[] args)
	{
		// display rules
		JOptionPane.showMessageDialog(null, "Rules: \n- Arrange the numbers in ascending order, from left to right."
				+ "\n- To move, enter how many numbers to reverse, starting from the left."
				+ "\n For example: 4 3 2 1 5 6 7 8 9"
				+ "\n Reverse the first 4 and the result is: 1 2 3 4 5 6 7 8 9"
				+ "\n YOU WIN!", "Reverse", JOptionPane.INFORMATION_MESSAGE);
		
		// set frame to center of screen
		ReverseGame frame = new ReverseGame();
		Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
		Dimension frameSize = new Dimension ( 500, 200 );
		frame.setBounds (screenSize.width / 2 - frameSize.width / 2, screenSize.height / 2 - frameSize.height / 2, frameSize.width, frameSize.height);
		frame.setLocationRelativeTo(null);
		frame.setSize(500, 220);
		frame.setTitle("Reverse");
		frame.setVisible(true);
		
		// start Timer
		timeStart = System.currentTimeMillis();
	}
}
