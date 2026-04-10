package sprint1;

import java.io.FileWriter;
import java.io.IOException;

public class ScoreLoggingSystem implements ScoreObserver {

	String score_file_path = "scores_file.txt";
	
	@Override
    public void updateScore(String score) 
    {
        try (FileWriter scoresFile = new FileWriter(score_file_path, true)) 
        {
            scoresFile.write(score + System.lineSeparator());
        } 
        catch (IOException e) 
        {
            System.out.println("Error occurred");
            e.printStackTrace();
        }
    }
}
