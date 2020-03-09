package com.example.divinebusinesstracker.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.divinebusinesstracker.Constants;
import com.example.divinebusinesstracker.HomeActivity;
import com.example.divinebusinesstracker.R;
import com.example.divinebusinesstracker.SellData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Local Variable declaration
    private View v;
    private Button btn;
    private Spinner monthSpinner,yearSpinner;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private static AlertDialog.Builder builder;
    private static DialogInterface.OnClickListener dialogClickListener;
    private static String downloadURL = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Constants.setHomevisible(true);
        //change title and subtitle of action bar like this
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Welcome");
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("to Divine Business Tracker");
        v = inflater.inflate(R.layout.fragment_home,null);
        btn = v.findViewById(R.id.tonextbtn);

        //check for update
        builder = new AlertDialog.Builder(getContext());
        CheckForUpgrade checkForUpgrade = new CheckForUpgrade(getContext());
        checkForUpgrade.execute("check");
        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Log.i("button","positive");
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadURL));
                        startActivity(browserIntent);
                        getActivity().finish();
                        System.exit(0);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        Log.i("button","negative");
                        getActivity().finish();
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
                    getActivity().finish();
                    System.exit(0);
                    return true;
                }
                return false;
            }
        });


        //spinners stuff
        monthSpinner = v.findViewById(R.id.month_spinner);
        yearSpinner = v.findViewById(R.id.year_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinnertext);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(spinnerAdapter);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.country_arrays, R.layout.spinnertext);
        monthSpinner.setAdapter(adapter);

        Log.i("month",String.valueOf(month));
        monthSpinner.setSelection(month-1);
        for(int i = 2019;i<=year;i++){
            spinnerAdapter.add(String.valueOf(i));
        }
        spinnerAdapter.notifyDataSetChanged();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SellData.class);
                i.putExtra("month",String.valueOf(convertMonth(monthSpinner.getSelectedItem().toString())));
                i.putExtra("year",yearSpinner.getSelectedItem().toString());
                startActivity(i);
            }
        });
        return v;
    }
    private int convertMonth(String m){
        m = m.toLowerCase();
        switch (m){
            case "january":{
                return 1;
            }
            case "february": {
                return 2;
                
            }
            case "march":{
                return 3;
                
            }
            case "april":{
                return 4;
                
            }
            case "may":{
                return 5;
                
            }
            case "june":{
                return 6;
                
            }
            case "july":{
                return 7;
                
            }
            case "august":{
                return 8;
                
            }
            case "september":{
                return 9;
                
            }
            case "october":{
                return 10;
                
            }
            case "november":{
                return 11;
                
            }
            case "december":{
                return 12;
                
            }
            default:
                return 0;
        }
    }
    private String convertMonth(int m){
        switch (m) {
            case 1:
                return "january";
            case 2:
                return "february";
            case 3:
                return "march";
            case 4:
                return "april";
            case 5:
                return "may";
            case 6:
                return "june";
            case 7:
                return "july";
            case 8:
                return "august";
            case 9:
                return "september";
            case 10:
                return "october";
            case 11:
                return "november";
            case 12:
                return "december";
            default:
                return "invalid";
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
