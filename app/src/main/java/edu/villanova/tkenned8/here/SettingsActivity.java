package edu.villanova.tkenned8.here;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckBox c1 = (CheckBox) findViewById(R.id.check_one);
        CheckBox c5 = (CheckBox) findViewById(R.id.check_five);
        CheckBox c10 = (CheckBox) findViewById(R.id.check_ten);

        //Custom Text for Choose one & Contacts text
        TextView settingsCustom = (TextView) findViewById(R.id.SettingsText);
        TextView messageCustom = (TextView) findViewById(R.id.enterMessageText);
        TextView destinationCustom = (TextView) findViewById(R.id.changeDistanceText);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/basictitlefont.ttf");
        settingsCustom.setTypeface(myCustomFont);
        messageCustom.setTypeface(myCustomFont);
        destinationCustom.setTypeface(myCustomFont);
    }
}
