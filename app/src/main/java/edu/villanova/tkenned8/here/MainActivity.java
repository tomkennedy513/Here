package edu.villanova.tkenned8.here;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private ArrayList<String> strArr;
    private ArrayAdapter<String> adapter;

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

        lv = (ListView) findViewById(R.id.contactList);
        strArr = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.contacts_list_item,strArr);

        setupSearchView();

        //Buttons that trigger new activities
        Button mapButton = (Button) findViewById(R.id.nextButton);
        assert mapButton != null;
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        /*
        Button addPinButton = (Button) findViewById(R.id.gearButton);
        assert addPinButton != null;
        addPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, locationSearchActivity.class);
                startActivityForResult(intent2, 1);
            }
        });*/
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) findViewById(R.id.searchContacts);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
    }

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
        //TextView resultText = (TextView) findViewById(R.id.searchViewResult);
        if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
            //handles suggestion clicked query
            String displayName = getDisplayNameForContact(intent);
            //resultText.setText(displayName);
            strArr.add(displayName);
            adapter.notifyDataSetChanged();
            SearchView searchView = (SearchView) findViewById(R.id.searchContacts);
            searchView.setQuery("",false);
            searchView.clearFocus();

            //ListAdapter adapter = new SimpleCursorAdapter(
            //this,R.layout.contacts_list_item,,new String [] {displayName}, new int [] {R.id.textList},0);
            //setListAdapter(adapter);


        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            //resultText.setText("should search for query: '" + query + "'...");
        }
    }
}
