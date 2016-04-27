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
        assert settingsCustom != null;
        settingsCustom.setTypeface(myCustomFont);
        assert messageCustom != null;
        messageCustom.setTypeface(myCustomFont);
        assert destinationCustom != null;
        destinationCustom.setTypeface(myCustomFont);

        //Adding the Shared Preferences to the text box and check boxes
        assert message != null;
        message.setText(settings.getString("text", ""));
        assert c1 != null;
        c1.setChecked(settings.getBoolean("one", false));
        assert c5 != null;
        c5.setChecked(settings.getBoolean("five", false));
        assert c10 != null;
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
                editor.apply();
            }
        });

        //Saving the first check box when changed
        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("one", c1.isChecked());
                editor.apply();
            }
        });

        //Saving the second check box when changed
        c5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("five", c5.isChecked());
                editor.apply();
            }
        });

        //Saving the third check box when changed
        c10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("ten", c10.isChecked());
                editor.apply();
            }
        });

    }
}
