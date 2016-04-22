package edu.villanova.tkenned8.here;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
