package edu.villanova.tkenned8.here;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
        
        if(c1.isChecked())
        {

        }
        else if (c5.isChecked())
        {

        }
        else if (c10.isChecked())
        {

        }



        //Custom Text for Choose one & Contacts text
        TextView settingsCustom = (TextView) findViewById(R.id.SettingsText);
        TextView messageCustom = (TextView) findViewById(R.id.enterMessageText);
        TextView destinationCustom = (TextView) findViewById(R.id.changeDistanceText);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/basictitlefont.ttf");
        settingsCustom.setTypeface(myCustomFont);
        messageCustom.setTypeface(myCustomFont);
        destinationCustom.setTypeface(myCustomFont);
    }
/*
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.check_one:
                if (checked)
                // Put some meat on the sandwich
                else
                // Remove the meat
                break;
            case R.id.check_five:
                if (checked)
                // Cheese me
                else
                // I'm lactose intolerant
            case R.id.check_ten:
                if (checked)
                //
                else
                //
                break;
        }
    }
    */
}
