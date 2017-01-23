package area;

import database.IUser;
import com.mongodb.MongoClient;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import modules.IModules;
import org.quartz.SchedulerException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import scheduler.SchedulerTask;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Application {
    public static MongoClient mongoClient = new MongoClient( "127.0.0.1" , 27017 );
    public static List<IUser> LoggedUser = new LinkedList<>();
    public static List<IModules> lModules = new LinkedList<>();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        try {
            new SchedulerTask().start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
