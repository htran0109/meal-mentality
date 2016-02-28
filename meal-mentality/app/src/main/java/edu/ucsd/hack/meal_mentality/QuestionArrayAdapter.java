package edu.ucsd.hack.meal_mentality;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hung on 2/27/2016.
 */
public class QuestionArrayAdapter extends ArrayAdapter<Question> {

    private class ViewHolder {
        private TextView line1;
        private EditText val;
        private int rb_checked;

        private ViewHolder() {
        }
    }

    private static final String TAG = "QuestionArrayAdapter";
    private List<Question> questList = new ArrayList<Question>();
    public QuestionArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Question object) {
        questList.add(object);
        super.add(object);
    }
    @Override
    public int getCount() {
        return this.questList.size();
    }

    @Override
    public int getViewTypeCount() {
        //Count=Size of ArrayList.
        return questList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final ViewHolder viewHolder;

        if (row == null) {
            Question card = getItem(position);
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(card.type == "YN") {
                row = inflater.inflate(R.layout.yesnoquestion, parent, false);

                RadioGroup radioGroup = (RadioGroup) row.findViewById(R.id.yes_no);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int index = group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
                        if (index == 0)
                            Log.d("DEBUG", "Value changed");
                        viewHolder.rb_checked = checkedId;
                    }
                });
                ((RadioGroup) row.findViewById(R.id.yes_no)).check(viewHolder.rb_checked);
            }
            else {
                row = inflater.inflate(R.layout.number, parent, false);

            }
            viewHolder.line1 = (TextView) row.findViewById(R.id.question);
            row.setTag(viewHolder);
            viewHolder.line1.setText(card.question);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        return row;
    }




    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
