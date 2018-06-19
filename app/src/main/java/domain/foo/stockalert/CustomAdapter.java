package domain.foo.stockalert;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import domain.foo.stockalert.Equity;
import domain.foo.stockalert.R;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class CustomAdapter extends ArrayAdapter<Equity> implements View.OnClickListener{

    private ArrayList<Equity> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView symbol;
        TextView price;
    }



    public CustomAdapter(ArrayList<Equity> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Equity equity = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.symbol = (TextView) convertView.findViewById(R.id.symbol);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;


        viewHolder.symbol.setText(equity.getSymbol());
        viewHolder.price.setText(equity.getPrice());
        // Return the completed view to render on screen
        return convertView;
    }


}
