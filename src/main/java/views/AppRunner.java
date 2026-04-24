package views;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppRunner
{
    public static void main(String[] args)
    {
        System.setProperty("server.port", "8082");
        SpringApplication.run(AppRunner.class, args);
        Main.main(args);
    } 
}

