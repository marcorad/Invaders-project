package highscore;

import java.io.File;
import java.util.Scanner;
import java.io.*;


//load single highscore from txt
//allow to save highscore if the input paramater is greater than current highscore saveScoreIfGreater(int hs)
//get highscore from file

public class LoadHighscore {
	public static void saveScoreIfGreater (int hs)// throws IOException
	{
		try
		{
			File oldhsfile = new File("C:/Users/test01/git/Invaders-project/highscoredata/savedhs.txt");
			Scanner scantext = new Scanner(oldhsfile);
			int hsold = 0;
			hsold = scantext.nextInt();
			if (hs>hsold)
			{
				oldhsfile.delete();
				File newhssave = new File("C:/Users/test01/git/Invaders-project/highscoredata/savedhs.txt");
				
				try
				{
					FileWriter newhs = new FileWriter(newhssave, false);
					newhs.write(hs + "");
					newhs.close();
					
				} catch (IOException e){
					
				}
			}
			else
			{
				scantext.close();
				return ;
			}	
			
			scantext.close();
		} catch(FileNotFoundException e) {
			
		}

}
}
