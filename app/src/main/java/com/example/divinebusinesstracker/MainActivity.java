package com.example.divinebusinesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText etEmail,etPassward;
    Button btnLognin;
    TextView btnsignup,forgetPassword;
    ProgressBar progressBar;
    Constants constants;
    String downloadURL = null;
    private static AlertDialog.Builder builder;
    private static DialogInterface.OnClickListener dialogClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constants = new Constants(this);
        if(constants.isLogedIn()){
            Intent i = new Intent(this,HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }else{
            builder = new AlertDialog.Builder(this);
            CheckForUpgrade checkForUpgrade = new CheckForUpgrade(this);
            checkForUpgrade.execute("check");
            dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadURL));
                            startActivity(browserIntent);
                            finish();
                            System.exit(0);
                            Log.i("button","positive");
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            Log.i("button","negative");
                            finish();
                            System.exit(0);
                            break;
                    }
                }
            };
            builder.setCancelable(false);
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK &&
                            event.getAction() == KeyEvent.ACTION_UP &&
                            !event.isCanceled()) {
                        finish();
                        System.exit(0);
                        return true;
                    }
                    return false;
                }
            });
        }
        etEmail = findViewById(R.id.mainactivity_edittext_email);
        etPassward = findViewById(R.id.mainactivity_edittext_password);
        btnLognin = findViewById(R.id.mainactivity_button_login);
        progressBar = findViewById(R.id.login_progressBar);

        btnLognin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign in request.
                BackgroundWorker backgroundWorker = new BackgroundWorker(getApplication());
                backgroundWorker.execute("login",etEmail.getText().toString(),etPassward.getText().toString());
            }
        });
    }


    public class BackgroundWorker extends AsyncTask<String, String, String> {
        Context context;
        AlertDialog alertDialog;
        String username = "",password = "";
        private Constants constants;
        private String ip;

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            constants = new Constants(context);
            String login_url = constants.getLoginUrl();

            if (type.equals("login")){
                try {
                    username = strings[1];
                    password = strings[2];
                    Log.i("status","inside the login try catch");
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);

                    Log.i("status","Http url connection established properly");

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    Log.i("status","buffer writer working");

                    String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")
                            +"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")
                    +"&"+URLEncoder.encode("apikey","UTF-8")+"="+URLEncoder.encode(constants.getApiKey(),"UTF-8");
                    Log.i("status","string post_data concatenation successful");

                    bufferedWriter.write(post_data);
                    Log.i("status","bufferedWriter.write(post_data) executed successfully");

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    Log.i("status","now reading feedback");


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result = "";
                    String line  = "";
                    while ((line = bufferedReader.readLine())!=null){
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "neterror";
        }

        @Override
        protected void onPreExecute() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnLognin.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
            alertDialog = new AlertDialog.Builder(getApplication()).create();
            alertDialog.setTitle("LoginStatus");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                boolean loginSuccessful = s.contains("loginsuccess");
                boolean loginUnsuccessful = s.contains("incorrectpassword");
                boolean userdosenotexist = s.contains("userdoesnotexist");
                boolean vneed = s.contains("vneed");

                boolean neterror = s.contains("neterror");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnLognin.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
                System.out.println(s);
                if (loginSuccessful) {

                    //alertDialog.setMessage("Login Successful!");
                    try {
                        JSONObject jsonObj = new JSONObject(s);
                        constants.setEmail(jsonObj.getString("email"));
                        constants.setDob(jsonObj.getString("dob"));
                        constants.setGender(jsonObj.getString("gender"));
                        constants.setName(jsonObj.getString("name"));
                        constants.setPhone(jsonObj.getString("phone"));
                        constants.setCity(jsonObj.getString("city"));
                        constants.setState(jsonObj.getString("state"));
                        constants.setAddress(jsonObj.getString("address"));
                        constants.setToken(jsonObj.getString("token"));
                        constants.setLogedIn(true);
                        System.out.println("details" +
                                "\n" +
                                "token:" +constants.getToken()+"\n"+
                                "name:" +constants.getName()+"\n"+
                                "email:"+constants.getEmail());
                        Intent i = new Intent(context,HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(context, "Login Successful !", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (loginUnsuccessful) {
                    Toast.makeText(context, "Incorrect Password !", Toast.LENGTH_SHORT).show();
                } else if(userdosenotexist){
                    Toast.makeText(context, "User dose not exist !", Toast.LENGTH_SHORT).show();
                }else if (neterror) {
                    Toast.makeText(context, "Unknown Error !", Toast.LENGTH_SHORT).show();

                }
            }catch (Exception e){
                e.printStackTrace();
                Log.i("Exception","e");
            }
        }

        public BackgroundWorker(Context ctx) {
            context = ctx;
        }
    }
    private class CheckForUpgrade extends AsyncTask<String, String, String> {
        Context context;
        AlertDialog alertDialog;
        private Constants constants;

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            constants = new Constants(context);
            String login_url = Constants.getCheckUpgradeURL();

            if (type.equals("check")){
                try {
                    Log.i("status","inside the login try catch");
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoOutput(true);

                    Log.i("status","Http url connection established properly");

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result = "";
                    String line  = "";
                    while ((line = bufferedReader.readLine())!=null){
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "neterror";
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                System.out.println(s);

                //TODO:apply alert dialogue settings
//                builder.setTitle("Update Required").setMessage("Please Update the application to continue.").setPositiveButton("Update", dialogClickListener)
//                        .setNegativeButton("Cancel", dialogClickListener).show();
                //alertDialog.setMessage("Login Successful!");
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    String version = jsonObj.getString("version");
                    if(!version.equals(Constants.getAppVersion())){
                        Constants.setForceUpgrade(true);
                        builder.setTitle("Update Required").setMessage("Please Update the application to continue.").setPositiveButton("Update", dialogClickListener)
                                .setNegativeButton("Cancel", dialogClickListener).show();
                        downloadURL = jsonObj.getString("url");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }catch (Exception e){
                e.printStackTrace();
                Log.i("Exception","e");
            }
        }

        public CheckForUpgrade(Context ctx) {
            context = ctx;
        }
    }
}
