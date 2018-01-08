package com.jp.apps.rating_of_things.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jp.apps.rating_of_things.Config;
import com.jp.apps.rating_of_things.Item;
import com.jp.apps.rating_of_things.ItemListAdapter;
import com.jp.apps.rating_of_things.R;
import com.jp.apps.rating_of_things.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    private ItemListAdapter itemListAdapter;
    private final ArrayList<Item> searchItemsResult = new ArrayList<>();
    private String searchItemCache = "";
    private Dao dao;

    private static final int OPEN_ITEM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();

        itemListAdapter = new ItemListAdapter(this, searchItemsResult);

        ListView search_items_result_list = (ListView) findViewById(R.id.search_items_result_list);
        search_items_result_list.setAdapter(itemListAdapter);

        search_items_result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) parent.getItemAtPosition(position);
                openItemActivity(item);
            }
        });

        EditText search_input_widget = (EditText) findViewById(R.id.activity_main_search_input);
        search_input_widget.addTextChangedListener(searchItemWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        Context applicationContext = this.getApplicationContext();
        dao = Config.initializeDefaultDao(applicationContext);
    }

    public void createItem(@SuppressWarnings("UnusedParameters") View view) {
        EditText search_input_widget = (EditText) findViewById(R.id.activity_main_search_input);
        String itemName = search_input_widget.getText().toString().trim();
        if (itemName.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.activity_main_create_item_empty), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return;
        }

        boolean itemExists = dao.itemExists(itemName);
        if (itemExists) {
            Toast toast = Toast.makeText(getApplicationContext(), itemName + " " + getString(R.string.activity_main_item_exists), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return;
        }
        Item item = new Item(itemName);
        item.setId(dao.createItem(item));
        openItemActivity(item);
    }


    private void openItemActivity(Item item) {
        Intent intent = new Intent(getBaseContext(), ActivityItem.class);
        item.populateIntent(intent);
        startActivityForResult(intent, OPEN_ITEM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_ITEM_REQUEST) {
            if (resultCode == RESULT_OK) {
                EditText search_input_widget = (EditText) findViewById(R.id.activity_main_search_input);
                search_input_widget.setText("");
            }
        }
    }


    private final TextWatcher searchItemWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable searchItemString) {
            String search = searchItemString.toString().trim();
            if (search.equals(searchItemCache)) return;

            if (search.length() == 0) {
                searchItemString.clear();
                searchItemCache = "";
                itemListAdapter.notifyDataSetChanged();
                return;
            }
            searchItemCache = search;
            List<String> includingTags = new ArrayList<>();
            List<String> excludingTags = new ArrayList<>();
            List<Item> items = dao.searchItems(search, includingTags, excludingTags);
            searchItemsResult.clear();
            searchItemsResult.addAll(items);
            itemListAdapter.notifyDataSetChanged();
        }
    };
}
