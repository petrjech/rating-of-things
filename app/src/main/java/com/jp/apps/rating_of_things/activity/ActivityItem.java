package com.jp.apps.rating_of_things.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jp.apps.rating_of_things.Config;
import com.jp.apps.rating_of_things.Item;
import com.jp.apps.rating_of_things.R;
import com.jp.apps.rating_of_things.Tag;
import com.jp.apps.rating_of_things.dao.Dao;

import java.util.List;

public class ActivityItem extends AppCompatActivity {

    private Dao dao;
    private Item item;
    private List<Tag> itemTags;

    private static final int OPEN_ADD_TAG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        item = Item.createFromIntent(intent);

        dao = Config.getDefaultDao();
        itemTags = dao.getItemTags(item);

        setTitle(item.getName());

        TextView tv = (TextView) findViewById(R.id.activity_item_test);
        String text = item.getName() + "\n";
        text += item.getName() + "\n";
        text += item.getName() + "\n";
        text += item.getName() + "\n";
        if (itemTags.isEmpty()) {
            text += "Tags: no tags.";
        } else {
            text += "Tags: ";
            for (Tag tag : itemTags) {
                text += tag.getName() + ", ";
            }
            text = text.substring(0, text.length() - 2);
        }
        tv.setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_item, menu);
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

    public void addTag(View view) {
        Intent intent = new Intent(getBaseContext(), ActivityAddTag.class);
        item.populateIntent(intent);
        startActivityForResult(intent, OPEN_ADD_TAG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_ADD_TAG_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast toast = Toast.makeText(getApplicationContext(),"tag added..", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
