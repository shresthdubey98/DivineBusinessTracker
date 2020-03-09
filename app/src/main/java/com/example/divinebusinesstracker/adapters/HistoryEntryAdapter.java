package com.example.divinebusinesstracker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.divinebusinesstracker.R;
import com.example.divinebusinesstracker.models.HistoryEntry;

import java.util.ArrayList;

public class HistoryEntryAdapter extends ArrayAdapter<HistoryEntry> {
    public HistoryEntryAdapter(Context context, ArrayList<HistoryEntry> entry) {
        super(context, 0, entry);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HistoryEntry entry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
//        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView = inflater.inflate(R.layout.list_element, parent, false);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_list_element, parent, false);
        }
        // Lookup view for data population
        TextView id = convertView.findViewById(R.id.history_id);
        TextView date = convertView.findViewById(R.id.history_date);
        TextView amt = convertView.findViewById(R.id.history_amt);
        TextView status = convertView.findViewById(R.id.history_status);
        LinearLayout cardColor = convertView.findViewById(R.id.history_complete_entry_linear_layout);
        // Populate the data into the template view using the data object
        if(Integer.parseInt(entry.getSno())%2!=0){
            cardColor.setBackgroundColor(Color.parseColor("#B5FFEF"));
        }else{
            cardColor.setBackgroundColor(Color.parseColor("#fffff9"));
        }
        if(entry.getStatus().toLowerCase().equals("pending")){
            status.setTextColor(Color.parseColor("#C61920"));
        }else if(entry.getStatus().toLowerCase().equals("approved")){
            status.setTextColor(Color.parseColor("#1D2EA8"));
        }else if(entry.getStatus().toLowerCase().equals("fulfilled")){
            status.setTextColor(Color.parseColor("#009000"));
        }
        id.setText(entry.getId());
        amt.setText(entry.getPoints());
        date.setText(entry.getDate());
        status.setText(entry.getStatus());
        // Return the completed view to render on screen
        return convertView;
    }
}
