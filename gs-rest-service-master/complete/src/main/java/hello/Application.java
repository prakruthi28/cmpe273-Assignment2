package hello;


import java.net.UnknownHostException;
import java.util.Date;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;

@Configuration
@ComponentScan
@EnableAutoConfiguration


public class Application {

	static public DB db ; 

	//@SuppressWarnings("deprecation")
	public static void main(String[] args) {
    	
    	try {

    	// Connect to mongolab
    		String textUri = "mongodb://root:root@ds049170.mongolab.com:49170/walletdb";
    		MongoClientURI uri = new MongoClientURI(textUri);
    		MongoClient mongo = new MongoClient(uri);
    		db = mongo.getDB("walletdb");
    		
			/**** Connect to MongoDB ****/
			// Since 2.10.0, uses MongoClient
			//MongoClient mongo = new MongoClient("localhost", 27017);

			/**** Get database ****/
			// if database doesn't exists, MongoDB will create it for you
			//db = mongo.getDB("wallet");
					
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}

    	 SpringApplication.run(Application.class, args);
    } 
}
