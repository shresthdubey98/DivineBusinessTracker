package com.example.divinebusinesstracker.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.divinebusinesstracker.Constants;
import com.example.divinebusinesstracker.HomeActivity;
import com.example.divinebusinesstracker.R;


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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RedeemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RedeemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RedeemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View fragView,homeView;
    private OnFragmentInteractionListener mListener;


    public RedeemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RedeemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RedeemFragment newInstance(String param1, String param2) {
        RedeemFragment fragment = new RedeemFragment();
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
    View v;
    TextView avipts;
    TextView reqpts;
    Button sendReqBtn;
    EditText etAmt;
    ProgressBar progressBar;
    String finalBalance;
    String lockedBalance;
    private LinearLayout previousLayout;
    private TextView finalTextview;
    int availablePoints = -1;
    int requestedPoints = -1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Constants.setHomevisible(false);
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_redeem,null);
        //change title and subtitle of action bar like this
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Redeem");
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle(null);

        avipts = fragView.findViewById(R.id.aviredeempts);
        reqpts = fragView.findViewById(R.id.reqpts);
        sendReqBtn = fragView.findViewById(R.id.send_request_button);
        etAmt = fragView.findViewById(R.id.et_amt);
        progressBar = fragView.findViewById(R.id.redeem_progressbar);
        previousLayout = fragView.findViewById(R.id.redeem_before_preview);
        finalTextview = fragView.findViewById(R.id.redeem_after_note);
        EntryExist entryExist = new EntryExist(getContext());
        entryExist.execute("check");
        sendReqBtn.setEnabled(false);

        etAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")){
                    if(Integer.parseInt(s.toString())!=0) {
                        if(availablePoints!=-1){
                            if(Integer.parseInt(s.toString())<=availablePoints) {
                                etAmt.setBackgroundResource(R.drawable.border);
                                etAmt.setTextColor(Color.parseColor("#ff669900"));
                                sendReqBtn.setEnabled(true);
                            }else{
                                sendReqBtn.setEnabled(false);
                                etAmt.setError("Please enter a valid value");
                                etAmt.setBackgroundResource(R.drawable.border_red);
                                etAmt.setTextColor(Color.parseColor("#ffff4444"));
                                etAmt.requestFocus();
                            }
                        }else{
                            etAmt.setText("");
                        }
                    }
                }else{
                    sendReqBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestRedeem requestRedeem = new RequestRedeem(getContext());
                requestRedeem.execute("request",etAmt.getText().toString());
            }
        });
        GetAviPoints getAviPoints = new GetAviPoints(getActivity());
        getAviPoints.execute("get");
        return fragView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class GetAviPoints extends AsyncTask<String, String, String> {
        Context context;
        private Constants constants;


        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            constants = new Constants(context);
            String fetch_url = constants.getGetavipoints();

            if (type.equals("get")){
                try {
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
                            +"&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(constants.getEmail(),"UTF-8");
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    sendReqBtn.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                boolean fetchSuccessful = s.contains("fetchsuccess");
                boolean neterror = s.contains("neterror");
                if (fetchSuccessful) {
                    try {
                        final JSONObject jsonObject = new JSONObject(s);
                        finalBalance = jsonObject.getString("avipts");
                        lockedBalance = jsonObject.getString("pointsreq");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                reqpts.setText(lockedBalance);
                                avipts.setText(finalBalance);
                                availablePoints = Integer.parseInt(finalBalance);
                                requestedPoints = Integer.parseInt(lockedBalance);
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if (neterror) {
                    //TODO:apply net error settings
                    Toast.makeText(context, "Network Error! Please try again", Toast.LENGTH_SHORT).show();

                }
            }catch (Exception e){
                e.printStackTrace();
                Log.i("Exception","e");
            }
        }

        public GetAviPoints(Context ctx) {
            context = ctx;
        }
    }
    public class EntryExist extends AsyncTask<String, String, String> {
        Context context;
        private Constants constants;


        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String fetch_url = Constants.getCheckRedeemEntryExist();
            constants = new Constants(context);
            if (type.equals("check")){
                try {
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
                            +"&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(constants.getEmail(),"UTF-8");
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    sendReqBtn.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                boolean entryExist = s.contains("entryexist");
                boolean entryNotExist = s.contains("notexist");
                boolean neterror = s.contains("error");
                if (entryExist) {
//                    Toast.makeText(context, "exist", Toast.LENGTH_SHORT).show();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            previousLayout.setVisibility(View.GONE);
                            finalTextview.setVisibility(View.VISIBLE);

                        }
                    });
                }else if (entryNotExist) {
                    //TODO:apply net error settings
                    previousLayout.setVisibility(View.VISIBLE);
//                    Toast.makeText(context, "not exist", Toast.LENGTH_SHORT).show();
                }else if(neterror){
                    Toast.makeText(context, "Net Error", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.i("Exception","e");
            }
        }

        public EntryExist(Context ctx) {
            context = ctx;
        }
    }
    public class RequestRedeem extends AsyncTask<String, String, String> {
        Context context;
        private Constants constants;
        String points;

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String redeem_url = Constants.getRequestRedeemurl();
            constants = new Constants(context);
            if (type.equals("request")){
                points = strings[1];
                try {
                    URL url = new URL(redeem_url);
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
                            +"&"+URLEncoder.encode("points","UTF-8")+"="+URLEncoder.encode(points,"UTF-8");

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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    sendReqBtn.setVisibility(View.GONE);
                }
            });
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                boolean insertSuccessfull = s.contains("insertsuccess");
                boolean insertUnsuccessfull = s.contains("insertnotsuccess");
                boolean neterror = s.contains("error");
                if (insertSuccessfull) {
//                    Toast.makeText(context, "exist", Toast.LENGTH_SHORT).show();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            previousLayout.setVisibility(View.GONE);
                            finalTextview.setVisibility(View.VISIBLE);
                            availablePoints  -= Integer.parseInt(points);
                            requestedPoints += Integer.parseInt(points);
                            Log.i("final data",availablePoints+" & "+requestedPoints);
                            reqpts.setText(String.valueOf(requestedPoints));
                            avipts.setText(String.valueOf(availablePoints));
                        }
                    });
                }else if (insertUnsuccessfull) {
                    //TODO:apply net error settings
                    progressBar.setVisibility(View.GONE);
                    previousLayout.setVisibility(View.VISIBLE);
                    finalTextview.setVisibility(View.GONE);
                }else if(neterror){
                    sendReqBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    previousLayout.setVisibility(View.VISIBLE);
                    finalTextview.setVisibility(View.GONE);
                    Toast.makeText(context, "Network Error Detected", Toast.LENGTH_SHORT).show();
                }else{
                    sendReqBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    previousLayout.setVisibility(View.VISIBLE);
                    finalTextview.setVisibility(View.GONE);
                    Toast.makeText(context, "something Went Wrong !", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.i("Exception","e");
            }
        }

        public RequestRedeem(Context ctx) {
            context = ctx;
        }
    }
}
