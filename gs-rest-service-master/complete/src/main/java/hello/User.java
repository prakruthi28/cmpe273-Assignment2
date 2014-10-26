package hello;

import java.util.HashMap;
import java.util.Map;
import hello.IDCard;
import hello.Weblogin;
import hello.BankAccount;

public class User {
	private  Integer id;
	private  String email;
    private  String password;
    private  String c_date;
    private  String m_date;
    private  Map<Integer, IDCard> cardData = new HashMap<Integer, IDCard>();
    private  Map<Integer, Weblogin> webData = new HashMap<Integer, Weblogin>();
    private  Map<Integer, BankAccount> bankData = new HashMap<Integer, BankAccount>();

    public User() {
    }

    public User(Integer id, String email, String password, String c_date, String m_date) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.c_date = c_date;
        this.m_date = m_date;
    }

    public Integer getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getCDate() {
        return c_date;
    }
    public String getMDate() {
        return m_date;
    }
    public void addCardData(IDCard id){
    	this.cardData.put(id.getId(), id);
    }
    public Map<Integer, IDCard> getCardData(){
    	return cardData;
    }
  
    public void addWebData(Weblogin id){
    	this.webData.put(id.getId(), id);
    }
    public Map<Integer, Weblogin> getWebData(){
    	return webData;
    }    
    public void addbankData(BankAccount id){
    	this.bankData.put(id.getba_Id(), id);
    }
    public Map<Integer, BankAccount> getbankData(){
    	return bankData;
    }
    @Override
    public String toString() {
    	return new StringBuffer(" id : ").append(this.id.toString())
                    .append(" email : ").append(this.email)
                    .append(" password : ").append(this.password).append(" created date : ")
                    .append(this.c_date).append("modified date : ").append(this.m_date).toString();
        }
}
