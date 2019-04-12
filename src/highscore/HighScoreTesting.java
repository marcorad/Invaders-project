package highscore;

import java.io.File;
import java.util.Scanner;
import java.io.*;


/**
 * @author Rita
 * 
 * 
 *
 */
public class HighScoreTesting {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		File oldhsfilex = new File("C:/Users/test01/git/Invaders-project/highscoredata/savedhs.txt");
		try
		{
			int currentscore = 0; 
			Scanner scantextx = new Scanner(oldhsfilex);
			int hsoldx = 0;
			hsoldx = scantextx.nextInt();
			System.out.print("High score was " + hsoldx + "\n");
			LoadHighscore.saveScoreIfGreater(currentscore);
			
			File newhsfilex = new File("C:/Users/test01/git/Invaders-project/highscoredata/savedhs.txt");
			Scanner scantextnew = new Scanner(newhsfilex);
			int hsnewx = scantextnew.nextInt();
			System.out.print("High score is " + hsnewx + "\n");
			scantextx.close();
			scantextnew.close();
		} catch(FileNotFoundException e) {
			System.out.print("Error compiling");
			
		}
		
	}
}
