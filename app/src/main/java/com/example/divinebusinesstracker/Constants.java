package com.example.divinebusinesstracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Constants {
    SharedPreferences sharedPreferences;
    Context context;
//    private static String ip = "192.168.1.7";
    private static String ip = "divinedrug.live";
    private static boolean forceUpgrade = false;
    private static final String appVersion = "1.0.0";
    //urls
    private String loginUrl = "http://"+ip+"/app/login.php";
    private String fetchData = "http://"+ip+"/app/getdata.php";
    private String getavipoints = "http://"+ip+"/app/getavipoints.php";
    private static String checkRedeemEntryExist = "http://"+ip+"/app/redeemrecexist.php";
    private static String requestRedeemurl = "http://"+ip+"/app/requestredeem.php";
    private static String historyFetchUrl = "http://"+ip+"/app/fetchhistory.php";
    private static String deleteTokenURL = "http://"+ip+"/app/deletetoken.php";
    private static String checkUpgradeURL = "http://"+ip+"/app/upgrade.php";
    public static String getCheckUpgradeURL() {
        return checkUpgradeURL;
    }



    //constants
    private String name = "";
    private String dob = "";
    private String gender = "";
    private String email = "";
    private String phone = "";
    private String city = "";
    private String state = "";
    private String address = "";
    private String token = "";
    private String apiKey = "22b91c6cc6031393a0beda6b4467b3";
    boolean logedIn = false;
    private static boolean homevisible = false;

    public static String getAppVersion() {
        return appVersion;
    }

    public static String getDeleteTokenURL() {
        return deleteTokenURL;
    }

    public static String getHistoryFetchUrl() {
        return historyFetchUrl;
    }

    public static String getRequestRedeemurl() {
        return requestRedeemurl;
    }

    public String getGetavipoints() {
        return getavipoints;
    }

    public static String getCheckRedeemEntryExist() {
        return checkRedeemEntryExist;
    }

    public String getFetchData() {
        return fetchData;
    }

    public static boolean isForceUpgrade() {
        return forceUpgrade;
    }

    public static void setForceUpgrade(boolean forceUpgrade) {
        Constants.forceUpgrade = forceUpgrade;
    }

    public static boolean isHomevisible() {
        return homevisible;
    }

    public static void setHomevisible(boolean homevisible) {
        Constants.homevisible = homevisible;
    }

    public boolean isLogedIn() {

        if(sharedPreferences.getString("logedin","").equals("true")){
            this.logedIn = true;
        }else{
            this.logedIn = false;
        }
        return logedIn;
    }

    public void setLogedIn(boolean logedIn) {
        this.logedIn = logedIn;
        if(logedIn){
            sharedPreferences.edit().putString("logedin","true").commit();
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    public void logout(){
        sharedPreferences.edit().putString("name","").commit();
        sharedPreferences.edit().putString("dob","").commit();
        sharedPreferences.edit().putString("gender","").commit();
        sharedPreferences.edit().putString("email","").commit();
        sharedPreferences.edit().putString("phone","").commit();
        sharedPreferences.edit().putString("city","").commit();
        sharedPreferences.edit().putString("state","").commit();
        sharedPreferences.edit().putString("address","").commit();
        sharedPreferences.edit().putString("token","").commit();
        sharedPreferences.edit().putString("logedin","").commit();
        Intent i = new Intent(context,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public String getToken() {
        token = sharedPreferences.getString("token","");
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        sharedPreferences.edit().putString("token",token).commit();


    }

    public String getCity() {
        city = sharedPreferences.getString("city","");

        return city;
    }

    public void setCity(String city) {
        this.city = city;
        sharedPreferences.edit().putString("city",city).commit();

    }

    public String getState() {
        state = sharedPreferences.getString("state","");

        return state;
    }

    public void setState(String state) {
        this.state = state;
        sharedPreferences.edit().putString("state",state).commit();

    }

    public String getAddress() {
        address = sharedPreferences.getString("address","");

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        sharedPreferences.edit().putString("address",address).commit();

    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getName() {
        name = sharedPreferences.getString("name","");
        return name;
    }

    public void setName(String name) {
        this.name = name;
        sharedPreferences.edit().putString("name",name).commit();

    }

    public String getDob() {
        dob = sharedPreferences.getString("dob","");
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
        sharedPreferences.edit().putString("dob",dob).commit();

    }

    public String getGender() {
        gender = sharedPreferences.getString("gender","");

        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        sharedPreferences.edit().putString("gender",gender).commit();

    }

    public String getEmail() {
        email = sharedPreferences.getString("email","");
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("email",email).commit();

    }

    public String getPhone() {
        phone = sharedPreferences.getString("phone","");
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        sharedPreferences.edit().putString("phone",phone).commit();

    }

    public Constants(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
    }
}
