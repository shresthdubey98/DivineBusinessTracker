package com.example.divinebusinesstracker.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.divinebusinesstracker.Constants;
import com.example.divinebusinesstracker.HomeActivity;
import com.example.divinebusinesstracker.R;
import com.example.divinebusinesstracker.adapters.HistoryEntryAdapter;
import com.example.divinebusinesstracker.models.HistoryEntry;

import org.json.JSONArray;
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
import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
    ProgressBar progressBar;
    ArrayList<HistoryEntry> historyEntries;
    HistoryEntryAdapter historyEntryAdapter;
    ListView listView;
    LinearLayout linearLayout,l2;
    ImageView noRecordImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Constants.setHomevisible(false);
        //change title and subtitle of action bar like this
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Redeem History");
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle(null);
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        progressBar = v.findViewById(R.id.history_selldata_progressbar);
        linearLayout = v.findViewById(R.id.history_linear_layout_sell_data);
        l2 = v.findViewById(R.id.history_l2);
        noRecordImageView = v.findViewById(R.id.history_norecord);
        noRecordImageView.setVisibility(View.GONE);
        listView = v.findViewById(R.id.history_selldata_listView);
        historyEntries = new ArrayList<>();
        FetchData fetchData = new FetchData(getContext());
        fetchData.execute("fetch");
        return v;
    }
    public class FetchData extends AsyncTask<String, String, String> {
        Context context;
        AlertDialog alertDialog;
        private Constants constants;


        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            constants = new Constants(context);
            String fetch_url = Constants.getHistoryFetchUrl();

            if (type.equals("fetch")){
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
                boolean fetchSuccessful = s.contains("date");
                boolean norecord = s.contains("norecord");
                boolean neterror = s.contains("neterror");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
                if (fetchSuccessful) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linearLayout.setVisibility(View.VISIBLE);
                            l2.setVisibility(View.VISIBLE);

                        }
                    }) ;
                    Log.i("loooooog",s);

                    //alertDialog.setMessage("Login Successful!");
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        Log.i("json",jsonArray.toString());
                        for(int i=0; i<jsonArray.length();i++){
                            JSONObject temp = jsonArray.getJSONObject(i);
                            String sno = temp.getString("sno");
                            String id = temp.getString("id");
                            String date = temp.getString("date");
                            String points = temp.getString("points");
                            String status = temp.getString("status");
                            historyEntries.add(new HistoryEntry(sno,id,date,points,status));
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                historyEntryAdapter = new HistoryEntryAdapter(context,historyEntries);
                                listView.setAdapter(historyEntryAdapter);
                            }
                        });


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (norecord) {
                    //TODO:apply no record settings
                    noRecordImageView.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "No records found!", Toast.LENGTH_SHORT).show();
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
}
