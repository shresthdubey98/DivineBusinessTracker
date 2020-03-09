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
import com.example.divinebusinesstracker.models.Entry;

import java.util.ArrayList;

public class EntryAdapter extends ArrayAdapter<Entry> {
    public EntryAdapter(Context context, ArrayList<Entry> entry) {
        super(context, 0, entry);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Entry entry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
//        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView = inflater.inflate(R.layout.list_element, parent, false);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element, parent, false);
        }
        // Lookup view for data population
        TextView sno = convertView.findViewById(R.id.serial_no_textview);
        TextView name = convertView.findViewById(R.id.product_true_name);
        TextView qty = convertView.findViewById(R.id.qty_sold);
        TextView amt = convertView.findViewById(R.id.amount_sold);
        LinearLayout cardColor = convertView.findViewById(R.id.complete_entry_linear_layout);
        // Populate the data into the template view using the data object
        if(Integer.parseInt(entry.getSno())%2!=0){
            cardColor.setBackgroundColor(Color.parseColor("#ffe86e"));
        }else{
            cardColor.setBackgroundColor(Color.parseColor("#fffff9"));
        }

        sno.setText(entry.getSno());
        name.setText(entry.getProductName());
        qty.setText(entry.getQuantity());
        amt.setText(entry.getAmount());
        // Return the completed view to render on screen
        return convertView;
    }
}
