package area;

import database.IUser;
import com.mongodb.MongoClient;
import database.MongoDBModule;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import modules.IModules;
import org.bson.Document;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import scheduler.SchedulerTask;

import java.lang.annotation.Documented;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class Application {
    public static MongoClient mongoClient = new MongoClient( "127.0.0.1" , 27017 );
    public static List<IUser> LoggedUser = new LinkedList<>();
    public static List<IModules> lModules = new LinkedList<>();
    public static String Addr;

    @Autowired
    public Application(@Value("${server.current_addr}") String addr) {
        Addr = addr;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        try {
            new SchedulerTask().start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        MongoDBModule.LoadAllModules();
        System.out.println(Addr);
    }
}
