package edu.ucsd.hack.meal_mentality;

/**
 * Created by chirag on 2/27/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CardArrayAdapter  extends ArrayAdapter<Food> {
    private static final String TAG = "CardArrayAdapter";
    private List<Food> cardList = new ArrayList<Food>();

    static class CardViewHolder {
        TextView line1;
        TextView line2;
        TextView line3;
        TextView line4;
        ImageView line5;
    }

    public CardArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Food object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Food getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listviewitem, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.timeOfDay);
            viewHolder.line2 = (TextView) row.findViewById(R.id.date);
            viewHolder.line3 = (TextView) row.findViewById(R.id.name);
            viewHolder.line4 = (TextView) row.findViewById(R.id.name_calories);
            viewHolder.line5 = (ImageView) row.findViewById(R.id.foodImage);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        Food card = getItem(position);
        viewHolder.line1.setText(card.timeOfDay);

        SimpleDateFormat format1 = new SimpleDateFormat("EEEE, d MMM yyyy");
        viewHolder.line2.setText(format1.format(card.date.getTime()));
        viewHolder.line3.setText(card.name);
        viewHolder.line4.setText(card.calories + " calories");

        Picasso.with(getContext()).load(card.url).into(viewHolder.line5);



        return row;
    }




    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}