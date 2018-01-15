package com.jp.apps.rating_of_things.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.jp.apps.rating_of_things.Config;
import com.jp.apps.rating_of_things.Item;
import com.jp.apps.rating_of_things.R;
import com.jp.apps.rating_of_things.Tag;
import com.jp.apps.rating_of_things.dao.Dao;

import java.util.List;

import static com.jp.apps.rating_of_things.activity.ActivityMain.selectedItem;

public class ActivityItem extends AppCompatActivity {

    private Dao dao;

    private static final int OPEN_ADD_TAG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dao = Config.getDefaultDao();
        selectedItem.setTags(dao.getItemTags(selectedItem));

        setTitle(selectedItem.getName());
        populateTags();
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
        Intent intent = new Intent(this, ActivityAddTag.class);
        startActivityForResult(intent, OPEN_ADD_TAG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_ADD_TAG_REQUEST) {
            populateTags();
        }
    }

    private void populateTags() {
        FlexboxLayout container = findViewById(R.id.activity_item_tags);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (Tag tag : selectedItem.getTags()) {
            for (int i = 0; i < 10; i++) {
                TextView tagView = new TextView(new ContextThemeWrapper(this, R.style.TagView), null, 0);
                tagView.setText(tag.getName());
//            tagView.setLayoutParams(params);
                container.addView(tagView);
            }
        }
    }
}
