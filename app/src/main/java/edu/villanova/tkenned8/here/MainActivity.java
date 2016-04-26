package edu.villanova.tkenned8.here;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Custom Text for Choose one & Contacts text
        TextView chooseoneCustom = (TextView) findViewById(R.id.textViewChooseOne);
        TextView contactsCustom = (TextView) findViewById(R.id.textViewContacts);
        TextView nextCustom = (TextView) findViewById(R.id.nextButton);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/basictitlefont.ttf");
        Typeface myBarFont = Typeface.createFromAsset(getAssets(),"fonts/Bubblegum.ttf");
        chooseoneCustom.setTypeface(myCustomFont);
        contactsCustom.setTypeface(myCustomFont);
        nextCustom.setTypeface(myBarFont);

        //Keeps keyboard below search bar
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Gear Button set up
        ImageButton addPinButton = (ImageButton) findViewById(R.id.gearButton);
        assert addPinButton != null;
        addPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent2, 1);
            }
        });

        //Set up search bar for contacts
        setupSearchView();
    }

    //Integrates searchview and contacts
    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) findViewById(R.id.searchContacts);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
    }

    //display Name from Contact
    private String getDisplayNameForContact(Intent intent) {
        Cursor phoneCursor = getContentResolver().query(intent.getData(), null, null, null, null);
        phoneCursor.moveToFirst();
        int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        String name = phoneCursor.getString(idDisplayName);
        phoneCursor.close();
        return name;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
            //handles suggestion clicked query
            final String displayName = getDisplayNameForContact(intent);

            SearchView searchView = (SearchView) findViewById(R.id.searchContacts);
            searchView.setQuery("",false);
            searchView.clearFocus(); //clears search after click

            //Add textview when select contact
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mainActivity_layout);
            RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(250,870,200,100); //left, top, right, bottom
            TextView tv =new TextView(this);
            tv.setLayoutParams(lparams);
            tv.setTextSize(15);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            Typeface myChalkFont = Typeface.createFromAsset(getAssets(),"fonts/Bubblegum.ttf");
            tv.setTypeface(myChalkFont);
            tv.setText(displayName);
            relativeLayout.addView(tv);

            //Add contact book image
            RelativeLayout.LayoutParams lparamsContact = new RelativeLayout.LayoutParams(
                    150, 150);
            lparamsContact.setMargins(75,820,200,100);
            ImageView contactImage = new ImageView(this);
            contactImage.setBackgroundResource(R.drawable.contact_book);
            contactImage.setLayoutParams(lparamsContact);
            relativeLayout.addView(contactImage);

            //Add checkbox item
            RelativeLayout.LayoutParams lparamsCheckBox = new RelativeLayout.LayoutParams(
                    150, 150);
            lparamsCheckBox.setMargins(905,820,0,0);
            final CheckBox checkbox = new CheckBox(this);
            checkbox.setLayoutParams(lparamsCheckBox);
            relativeLayout.addView(checkbox);

            //Buttons that trigger new activities
            Button mapButton = (Button) findViewById(R.id.nextButton);
            assert mapButton != null;
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);

                    //Get contact phone number
                    Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER};
                    Cursor people = getContentResolver().query(uri, projection, null, null, null);
                    int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                    people.moveToFirst();
                    String name ="";
                    String phoneNumber = "";
                    //Loop through contacts until find name selected and obtain phone number
                    while (people.isLast() != true) {
                        name = people.getString(indexName);
                        if (name.equals(displayName)) {
                            phoneNumber = people.getString(indexNumber);
                            break;
                        }
                        people.moveToNext();
                    }

                    Log.d("MyApp:","Here is my phoneNumber " + phoneNumber + " and name = " + name);

                    //Find value of slider whether picking up or arriving
                    Switch s = (Switch) findViewById(R.id.switch_Arrive_Pickup);
                    String destinationType;
                    if (s.isChecked())
                    {
                        destinationType = "arriving"; //1 for arriving
                    }
                    else {
                        destinationType = "pickingUp"; //0 for picking up
                    }

                    //Find contact to pass to next activity
                    if (checkbox.isChecked())
                    {
                       intent.putExtra("contactName",displayName);
                        intent.putExtra("phoneNumber",phoneNumber);
                    }
                    intent.putExtra("destinationType",destinationType);

                    Log.d("MyApp:","Show destinationType = " + destinationType + " and contactName " + displayName + " phone number = " + phoneNumber);

                    startActivity(intent);
                }
            });

        }
    }
}
