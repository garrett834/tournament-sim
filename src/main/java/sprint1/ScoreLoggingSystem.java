package sprint1;

import java.io.FileWriter;
import java.io.IOException;

public class ScoreLoggingSystem implements ScoreObserver {

	@Override
    public void updateScore(String score) 
    {
        try (FileWriter scoresFile = new FileWriter("scores_file.txt", true)) 
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
