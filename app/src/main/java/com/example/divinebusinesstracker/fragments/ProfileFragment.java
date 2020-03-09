package com.example.divinebusinesstracker.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    Button logoutButton;
    TextView name,email,phone,dob;
    AlertDialog.Builder builder;
    DialogInterface.OnClickListener dialogClickListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Constants.setHomevisible(false);
        //change title and subtitle of action bar like this
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Profile");
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle(null);
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        logoutButton = v.findViewById(R.id.logout_button);
        name = v.findViewById(R.id.profile_name);
        email = v.findViewById(R.id.profile_email);
        phone = v.findViewById(R.id.profile_phone);
        dob = v.findViewById(R.id.profile_dob);
        Constants constants = new Constants(getContext());
        name.setText(constants.getName());
        email.setText(constants.getEmail());
        phone.setText(constants.getPhone());
        dob.setText(constants.getDob());

        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Constants constants = new Constants(getContext());
                        DeleteToken deleteToken = new DeleteToken(getContext());
                        Log.i("token",constants.getToken());
                        deleteToken.execute("delete",constants.getApiKey(),constants.getToken(),constants.getEmail());
                        constants.logout();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        builder = new AlertDialog.Builder(getContext());


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Are you sure you want to logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        return v;
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
    public class DeleteToken extends AsyncTask<String, String, String> {
        Context context;
        AlertDialog alertDialog;
        String username = "",password = "";
        private Constants constants;
        private String ip;

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            constants = new Constants(context);
            String deleteTokenURL = Constants.getDeleteTokenURL();

            if (type.equals("delete")){
                try {
                    String xapikey = strings[1];
                    String xtoken = strings[2];
                    String xemail = strings[3];
                    Log.i("status","inside the login try catch");
                    URL url = new URL(deleteTokenURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);

                    Log.i("status","Http url connection established properly");

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    Log.i("status","buffer writer working");

                    String post_data = URLEncoder.encode("apikey","UTF-8")+"="+URLEncoder.encode(xapikey,"UTF-8")
                            +"&"+URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(xtoken,"UTF-8")
                            +"&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(xemail,"UTF-8");
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
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            boolean deleteSuccess = s.contains("update successfull");
            boolean deleteUnsuccess = s.contains("update unsuccessfull");
            Log.i("output",s);
            if (deleteSuccess){
                Log.i("status","Token Deleted");
            }else if(deleteUnsuccess){
                Log.i("status","Token Not deleted");
            }else{
                Log.i("status","Something went wrong");
            }
        }

        public DeleteToken(Context ctx) {
            context = ctx;
        }
    }
}
