package edu.villanova.tkenned8.here;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        final CheckBox c1 = (CheckBox) findViewById(R.id.check_one);
        final CheckBox c5 = (CheckBox) findViewById(R.id.check_five);
        final CheckBox c10 = (CheckBox) findViewById(R.id.check_ten);

        //Custom Text for Choose one & Contacts text
        final EditText message = (EditText) findViewById(R.id.text);
        TextView settingsCustom = (TextView) findViewById(R.id.SettingsText);
        TextView messageCustom = (TextView) findViewById(R.id.enterMessageText);
        TextView destinationCustom = (TextView) findViewById(R.id.changeDistanceText);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/basictitlefont.ttf");
        settingsCustom.setTypeface(myCustomFont);
        messageCustom.setTypeface(myCustomFont);
        destinationCustom.setTypeface(myCustomFont);
        //message.setTypeface(myCustomFont);
        //c1.setTypeface(myCustomFont);
        //c5.setTypeface(myCustomFont);
        //c10.setTypeface(myCustomFont);

        //Adding the Shared Preferences to the text box and check boxes
        message.setText(settings.getString("text", ""));
        c1.setChecked(settings.getBoolean("one", false));
        c5.setChecked(settings.getBoolean("five", false));
        c10.setChecked(settings.getBoolean("ten", false));

        //Saving the text box when changed
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("text", s.toString());
                editor.commit();;
            }
        });

        //Saving the first check box when changed
        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("one", c1.isChecked());
                editor.commit();;
            }
        });

        //Saving the second check box when changed
        c5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("five", c5.isChecked());
                editor.commit();;
            }
        });

        //Saving the third check box when changed
        c10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("ten", c10.isChecked());
                editor.commit();
            }
        });

    }
}
