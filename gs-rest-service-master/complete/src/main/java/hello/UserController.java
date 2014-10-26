package hello;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import hello.User;
import hello.IDCard;
import hello.Weblogin;
import hello.BankAccount;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

   // MongoClient mongo = new MongoClient("localhost", 27017);
    
   Map<Integer, User> userData = new HashMap<Integer, User>();

//  Map<Integer, Weblogin> webData = new HashMap<Integer, Weblogin>();
    
    //Map<Integer, Map<Integer, IDCard>> idCardData = new HashMap<Integer, HashMap<Integer, IDCard>>();
    
    int userId = 0;
    int cardId = 0;
    int webId = 0;
    int baId = 0;
  
    @RequestMapping(value = "/v1/users", method = RequestMethod.POST)
    public User CreateUser(@RequestBody User user) {
    	    	logger.info("Start create user.");
		userId++;
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
		
		String formattedDate = dateFormat.format(date);
		User new_user = new User(userId, user.getEmail(), user.getPassword(), formattedDate, "");
		//userData.put(userId, new_user);
		DB db = Application.db;
		DBCollection table = db.getCollection("users");

		/**** Insert ****/
		// create a document to store key and value
		BasicDBObject document = new BasicDBObject();
		document.put("id", userId);
		document.put("email", new_user.getEmail());
		document.put("password", new_user.getPassword());
		document.put("created_at", new_user.getCDate());
		document.put("modified_at", new_user.getCDate());
		table.insert(document);
		
		return new_user;
    }

    private User userfromdb(int userId) {
    	DB db = Application.db;
		DBCollection table = db.getCollection("users");

    	/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		
		searchQuery.put("id", userId);
        //MongoTemplate template;
		DBCursor cursor = table.find(searchQuery);
		JSONObject json;
		User new_user = null;
		try {
		 json = new JSONObject(cursor.next().toString());
		 
		 new_user = new User(json.getInt("id"),
				 			json.getString("email"),
				 			json.getString("password"),
				 			json.getString("created_at"),
				 			json.getString("modified_at"));
		}
		catch (JSONException e)
			{
			e.printStackTrace();
		}
    	return new_user;
    }
    
    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.GET)
    public User ViewUser(@PathVariable("id") int userId) {
    	logger.info("Start view.");
    	
		return userfromdb(userId);
    }
    
    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.PUT)
    public User UpdateUser(@PathVariable("id") int userId, @RequestBody User user) {
    	logger.info("Start update.");

    	DB db = Application.db;
		DBCollection table = db.getCollection("users");

    	BasicDBObject query = new BasicDBObject();
		query.put("id", userId);

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("email", user.getEmail());
		newDocument.put("password", user.getPassword());
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
		String formattedDate = dateFormat.format(date);
		newDocument.put("modified_at", formattedDate);

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);

		table.update(query, updateObj);

    	return userfromdb(userId);
    }

    @RequestMapping(value = "/v1/users/{id}/idcards", method = RequestMethod.POST)
    public IDCard CreateID(@PathVariable("id") int userId, @RequestBody IDCard idcard) {
    	logger.info("Start create id card.");
		cardId++;
		IDCard new_id = new IDCard(cardId, idcard.getName(), idcard.getNumber(), idcard.getDate());
		//userData.put(userId, new_user);
		DB db = Application.db;
		DBCollection table = db.getCollection("idcards");

		/**** Insert ****/
		// create a document to store key and value
		BasicDBObject document = new BasicDBObject();
		document.put("id", userId);
		document.put("card_id", cardId);
		document.put("card_name",idcard.getName() );
		document.put("card_number", idcard.getNumber());
		document.put("created_at", idcard.getDate());
		document.put("modified_at", idcard.getDate());
		table.insert(document);
		
		//User user = userData.get(userId);
		//user.addCardData(new_id);
		return new_id;
    }
    @RequestMapping(value = "/v1/users/{id}/idcards", method = RequestMethod.GET)
    public String ListID(@PathVariable("id") int userId) {
      	logger.info("Start get id card.");
    	DB db = Application.db;
		DBCollection table = db.getCollection("idcards");

    	/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		
		searchQuery.put("id", userId);
        //MongoTemplate template;
		DBCursor cursor = table.find(searchQuery);
		JSONObject json;
		IDCard new_card = null;
		String cardString = null;

		while (cursor.hasNext()) {
		try {
		 json = new JSONObject(cursor.next().toString());
		 new_card = new IDCard(json.getInt("card_id"),
				 				json.getString("card_name"),
				 				json.getString("card_number"),
				 				json.getString("created_at"));
		}
		catch (JSONException e)
			{
			e.printStackTrace();
		}
		cardString += new_card.toString();
		}
    	return cardString;
    }
  	//MongoTemplate mongoTemplate;
    @RequestMapping(value = "/v1/users/{id}/idcards/{cid}", method = RequestMethod.DELETE)
    public String deleteID(@PathVariable("id") int userId, @PathVariable("cid") int cardId) {
      	logger.info("Start delete id card.");

      	//IDCard id =     userData.get(userId).getCardData().remove(cardId);
    	DB db = Application.db;
		DBCollection table = db.getCollection("idcards");

    	/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		
		searchQuery.put("id", userId);
		searchQuery.put("card_id", cardId);
        //MongoTemplate template;
		DBCursor cursor = table.find(searchQuery);
		JSONObject json;
		IDCard new_card = null;
		String cardString = null;

		while (cursor.hasNext()) {
			try {
				json = new JSONObject(cursor.next().toString());
				new_card = new IDCard(json.getInt("card_id"),
						json.getString("card_name"),
						json.getString("card_number"),
						json.getString("created_at"));
				}	
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			cardString += new_card.toString();
			table.findAndRemove(cursor.getQuery());
		}
      	//mongoTemplate.remove(cardId);
      	return cardString;
    }
    @RequestMapping(value = "/v1/users/{id}/weblogins", method = RequestMethod.POST)
    public Weblogin CreateWeblogin(@PathVariable("id") int userId, @RequestBody Weblogin weblogin) {
    	logger.info("Start create web login.");
		webId++;
		Weblogin new_weblogin = new Weblogin(webId, weblogin.geturl(), weblogin.getlogin(), weblogin.getpassword());
	//	User webuser = userData.get(userId);
	//	webuser.addWebData(new_weblogin);
		
		DB db = Application.db;
		DBCollection table = db.getCollection("weblogins");

		/**** Insert ****/
		// create a document to store key and value
		BasicDBObject document = new BasicDBObject();
		document.put("id", userId);
		document.put("web_id", webId);
		document.put("url",weblogin.geturl());
		document.put("login", weblogin.getlogin());
		document.put("password", weblogin.getpassword());
		table.insert(document);
		
		//User user = userData.get(userId);
		//user.addCardData(new_id);
		return new_weblogin;
    }
    @RequestMapping(value = "/v1/users/{id}/weblogins", method = RequestMethod.GET)
    public String ListWeblogin(@PathVariable("id") int userId) {
      	logger.info("Start get web login.");
      	DB db = Application.db;
		DBCollection table = db.getCollection("weblogins");

    	/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		
		searchQuery.put("id", userId);
        //MongoTemplate template;
		DBCursor cursor = table.find(searchQuery);
		JSONObject json;
		Weblogin new_weblogin = null;
		String webloginString = null;

		while (cursor.hasNext()) {
		try {
		 json = new JSONObject(cursor.next().toString());
		 new_weblogin = new Weblogin(json.getInt("web_id"),
				 				json.getString("url"),
				 				json.getString("login"),
				 				json.getString("password"));
		}
		catch (JSONException e)
			{
			e.printStackTrace();
		}
		webloginString += new_weblogin.toString();
		}
    	return webloginString;
      	      	
    	// return userData.get(userId).getWebData().toString();
    }
    @RequestMapping(value = "/v1/users/{id}/weblogins/{login_id}", method = RequestMethod.DELETE)
    public String deleteWeblogin(@PathVariable("id") int userId, @PathVariable("login_id") int loginID) {
      	logger.info("Start delete web login.");
     // 	public String deleteID(@PathVariable("id") int userId, @PathVariable("cid") int cardId) {
       //   	logger.info("Start delete id card.");
 
          	//IDCard id =     userData.get(userId).getCardData().remove(cardId);
        	DB db = Application.db;
    		DBCollection table = db.getCollection("weblogins");

        	/**** Find and display ****/
    		BasicDBObject searchQuery = new BasicDBObject();
    		
    		searchQuery.put("id", userId);
    		searchQuery.put("login_id", loginID);
            //MongoTemplate template;
    		DBCursor cursor = table.find(searchQuery);
    		JSONObject json;
    		Weblogin new_weblogin = null;
    		String webloginString = null;

    		while (cursor.hasNext()) {
    			try {
    				json = new JSONObject(cursor.next().toString());
    				new_weblogin = new Weblogin(json.getInt("web_id"),
    						json.getString("url"),
    						json.getString("login"),
    						json.getString("password"));
    				}	
    				catch (JSONException e)
    				{
    					e.printStackTrace();
    				}
    			webloginString += new_weblogin.toString();
    			table.findAndRemove(cursor.getQuery());
    		}
          	//mongoTemplate.remove(cardId);
          	return webloginString;

 }
    
    @RequestMapping(value = "/v1/users/{id}/bankaccounts", method = RequestMethod.POST)
    public BankAccount CreateBankAccount(@PathVariable("id") int userId, @RequestBody BankAccount bankacc) {
    	logger.info("Start create Bank Account.");
		baId++;
		BankAccount new_bankaccount = new BankAccount(baId, bankacc.getaccount_name(), bankacc.getrouting_number(), bankacc.getaccount_number());

		//userData.put(userId, new_user);
		DB db = Application.db;
		DBCollection table = db.getCollection("bankaccount");

		int responseCode = 404;
		String acc_name =  bankacc.getaccount_name();
		String url = "https://www.routingnumbers.info/api/data.json?rn="+bankacc.getrouting_number();
		logger.info(url);
		try {
			URL obj = new URL(url);
			HttpURLConnection con = null;
			con  = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			JSONObject json = new JSONObject(response.toString());
		//	logger.info(""+responseCode);
			logger.info(response.toString());
			responseCode = json.getInt("code");
			if (responseCode == 200) {
				acc_name = json.getString("customer_name");
			} else {
			}
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
		//User user = userData.get(userId);
		//user.addCardData(new_id);
		/**** Insert ****/
		// create a document to store key and value
		BasicDBObject document = new BasicDBObject();
		document.put("id", userId);
		document.put("ba_id", baId);
		document.put("account_name", acc_name);
		document.put("routing_number", bankacc.getrouting_number());
		document.put("account_number",  bankacc.getaccount_number());
		table.insert(document);

		//User bauser = userData.get(userId);
		//bauser.addbankData(new_bankaccount);
		return new_bankaccount;
    }
    
    @RequestMapping(value = "/v1/users/{id}/bankaccounts", method = RequestMethod.GET)
    public String Listbankaccount(@PathVariable("id") int userId) {
      	logger.info("Start get bank account.");
      	DB db = Application.db;
		DBCollection table = db.getCollection("bankaccount");

    	/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		
		searchQuery.put("id", userId);
        //MongoTemplate template;
		DBCursor cursor = table.find(searchQuery);
		JSONObject json;
		BankAccount new_bankaccount = null;
		String bankaccountString = null;

		while (cursor.hasNext()) {
		try {
		 json = new JSONObject(cursor.next().toString());
		 new_bankaccount = new BankAccount(json.getInt("bank_id"),
				 				json.getString("account_name"),
				 				json.getString("routing_number"),
				 				json.getString("account_number"));
	      //	logger.info(bankaccountString);

		}
		catch (JSONException e)
			{
			e.printStackTrace();
		}
		bankaccountString += new_bankaccount.toString();
		}
    	return bankaccountString;
      	      	
    	// return userData.get(userId).getbankData().toString();
    }
    
    @RequestMapping(value = "/v1/users/{id}/bankaccounts/{bankacc_id}", method = RequestMethod.DELETE)
    public String deletebankaccount(@PathVariable("id") int userId, @PathVariable("bankacc_id") int bankacID) {
      	logger.info("Start delete bank account.");
           	DB db = Application.db;
     		DBCollection table = db.getCollection("bankaccounts");

         	/**** Find and display ****/
     		BasicDBObject searchQuery = new BasicDBObject();
     		
     		searchQuery.put("id", userId);
     		searchQuery.put("bank_id", bankacID);
             //MongoTemplate template;
     		DBCursor cursor = table.find(searchQuery);
     		JSONObject json;
     		BankAccount new_bankaccount = null;
     		String bankaccountString = null;

     		while (cursor.hasNext()) {
     			try {
     				json = new JSONObject(cursor.next().toString());
     				new_bankaccount = new BankAccount(json.getInt("bank_id"),
     						json.getString("account_name"),
     						json.getString("routing_number"),
     						json.getString("account_number"));
     				}	
     				catch (JSONException e)
     				{
     					e.printStackTrace();
     				}
     			bankaccountString += new_bankaccount.toString();
     			table.findAndRemove(cursor.getQuery());
     		}
           	//mongoTemplate.remove(cardId);
       return bankaccountString;

      //	return bid;
    }
}
