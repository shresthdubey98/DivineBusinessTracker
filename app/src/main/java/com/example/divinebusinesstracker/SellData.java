package com.example.divinebusinesstracker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.divinebusinesstracker.adapters.EntryAdapter;
import com.example.divinebusinesstracker.models.Entry;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SellData extends AppCompatActivity {
    ProgressBar progressBar;
    ArrayList<Entry> entries;
    EntryAdapter entryAdapter;
    ListView listView;
    LinearLayout linearLayout,l2,l3;
    TextView total_textview,pts_textview;

    //toolbar stuff deceleration
    Toolbar toolbar;
    ImageView noRecordImageView;
    View logo_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.setHomevisible(false);
        setContentView(R.layout.activity_sell_data);
        progressBar = findViewById(R.id.selldata_progressbar);
        linearLayout = findViewById(R.id.linear_layout_sell_data);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        noRecordImageView = findViewById(R.id.norecord);
        noRecordImageView.setVisibility(View.GONE);
        // toolbar stuff
        toolbar = findViewById(R.id.buySell_toolbar);
        logo_back = toolbar.getChildAt(1);
        logo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        listView = findViewById(R.id.selldata_listView);
        total_textview = findViewById(R.id.amt);
        pts_textview = findViewById(R.id.pts);
        entries = new ArrayList<>();
        Intent i = getIntent();
        String month = i.getStringExtra("month");
        String year = i.getStringExtra("year");
        System.out.println("month:"+month+"\nYear:"+year);
        FetchData fetchData = new FetchData(this);
        fetchData.execute("fetch",month,year);
    }
    public class FetchData extends AsyncTask<String, String, String> {
        Context context;
        AlertDialog alertDialog;
        private Constants constants;


        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            constants = new Constants(context);
            String fetch_url = constants.getFetchData();

            if (type.equals("fetch")){
                try {
                    String month = strings[1];
                    String year = strings[2];
                    URL url = new URL(fetch_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);

                    Log.i("status","Http url connection established properly");

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    Log.i("status","buffer writer working");

                    String post_data = URLEncoder.encode("apikey","UTF-8")+"="+URLEncoder.encode(constants.getApiKey(),"UTF-8")
                            +"&"+URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(constants.getToken(),"UTF-8")
                            +"&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(constants.getEmail(),"UTF-8")
                            +"&"+URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode(month,"UTF-8")
                            +"&"+URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8");
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
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("LoginStatus");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                boolean fetchSuccessful = s.contains("fetchsuccessfull");
                boolean norecord = s.contains("norecord");
                boolean neterror = s.contains("neterror");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
                if (fetchSuccessful) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linearLayout.setVisibility(View.VISIBLE);
                            l2.setVisibility(View.VISIBLE);

                            l3.setVisibility(View.VISIBLE);

                        }
                    }) ;
                    Log.i("loooooog",s);

                    //alertDialog.setMessage("Login Successful!");
                    try {
                        JSONObject jsonObj = new JSONObject(s);
                        JSONObject detailObj = new JSONObject(jsonObj.getString("details"));
                        JSONArray productsArray = new JSONArray(jsonObj.getString("products"));

                        Log.i("json",productsArray.toString());
                        for(int i=0; i<productsArray.length();i++){
                            JSONObject temp = productsArray.getJSONObject(i);
                            String sno = String.valueOf(i+1);
                            String name = temp.getString("name");
                            String qty = temp.getString("qty");
                            String amt = String.valueOf(round(Float.parseFloat(temp.getString("amt")),2));
                            entries.add(new Entry(sno,name,qty,amt));
                        }
                        final String total = String.valueOf(round(Float.parseFloat(detailObj.getString("total")),2));
                        int a = (int)round(Float.parseFloat(detailObj.getString("pts")),0);

                        final String pts = String.valueOf(a);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                entryAdapter = new EntryAdapter(context,entries);
                                listView.setAdapter(entryAdapter);
                                total_textview.setText(total);
                                pts_textview.setText(pts);
                            }
                        });


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (norecord) {
                    //TODO:apply no record settings
                    noRecordImageView.setVisibility(View.VISIBLE);
//                    Toast.makeText(context, "No records found!", Toast.LENGTH_SHORT).show();
                }else if (neterror) {
                    //TODO:apply net error settings
                    Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();

                }
            }catch (Exception e){
                e.printStackTrace();
                Log.i("Exception","e");
            }
        }
        public FetchData(Context ctx) {
            context = ctx;
        }
    }
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
