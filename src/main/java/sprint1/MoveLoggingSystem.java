package sprint1;

import java.io.FileWriter;
import java.io.IOException;

public class MoveLoggingSystem implements MoveObserver {

	String move_file_path = "move_file.txt";
	
	@Override
	public void updateMove(String move) 
	{
		try (FileWriter moveFile = new FileWriter(move_file_path, true)) 
        {
            moveFile.write(move + System.lineSeparator());
        } 
        catch (IOException e) 
        {
            System.out.println("Error occurred");
            e.printStackTrace();
        }
		
		
	}

}
