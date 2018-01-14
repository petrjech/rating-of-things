package com.jp.apps.rating_of_things.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.jp.apps.rating_of_things.Config;
import com.jp.apps.rating_of_things.R;
import com.jp.apps.rating_of_things.Tag;
import com.jp.apps.rating_of_things.TagListAdapter;
import com.jp.apps.rating_of_things.dao.Dao;

import java.util.ArrayList;
import java.util.List;

import static com.jp.apps.rating_of_things.activity.ActivityMain.selectedItem;

public class ActivityAddTag extends AppCompatActivity {

    private TagListAdapter tagListAdapter;
    private final ArrayList<Tag> searchTagsResult = new ArrayList<>();
    private String searchTagCache = "";
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.activity_add_tag_title) + selectedItem.getName());

        populateTags();

        dao = Config.getDefaultDao();

        tagListAdapter = new TagListAdapter(this, searchTagsResult);

        ListView search_tags_result_list = (ListView) findViewById(R.id.search_tags_result_list);
        search_tags_result_list.setAdapter(tagListAdapter);

        search_tags_result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tag tag = (Tag) parent.getItemAtPosition(position);
                addTag(tag);
            }
        });

        EditText search_input_widget = (EditText) findViewById(R.id.activity_add_tag_search_input);
        search_input_widget.addTextChangedListener(searchTagWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_add_tag, menu);
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

    private void addTag(Tag tag) {
        selectedItem.getTags().add(tag);
        boolean success = dao.addItemTag(selectedItem, tag);
        if (!success) {
            Toast toast = Toast.makeText(getApplicationContext(), "Add tag to this item failed.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
        populateTags();
    }

    private void populateTags() {
        TextView tagsView = (TextView) findViewById(R.id.activity_add_tag_tags);
        if (selectedItem.getTags().isEmpty()) {
            tagsView.setText(getString(R.string.activity_add_tag_tags_empty));
        } else {
            String text = "";
            for (Tag tag : selectedItem.getTags()) {
                text += tag.getName() + ", ";
            }
            text = text.substring(0, text.length() - 2) + ".";
            tagsView.setText(text);
        }
    }

    private final TextWatcher searchTagWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable searchTagString) {
            String search = searchTagString.toString().trim();
            if (search.equals(searchTagCache)) return;

            if (search.length() == 0) {
                searchTagString.clear();
                searchTagCache = "";
                tagListAdapter.notifyDataSetChanged();
                return;
            }
            searchTagCache = search;
            List<Tag> tags = dao.searchTags(search);
            searchTagsResult.clear();
            searchTagsResult.addAll(tags);
            tagListAdapter.notifyDataSetChanged();
        }
    };

    public void createTag(View view) {
        EditText search_input_widget = (EditText) findViewById(R.id.activity_add_tag_search_input);
        String tagName = search_input_widget.getText().toString().trim();
        if (tagName.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.activity_add_tag_create_tag_empty), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return;
        }

        boolean tagExists = dao.tagExists(tagName);
        if (tagExists) {
            Toast toast = Toast.makeText(getApplicationContext(), tagName + " " + getString(R.string.activity_add_tag_exists), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return;
        }
        Tag tag = new Tag(tagName);
        tag.setId(dao.createTag(tag));
        search_input_widget.setText("");
        addTag(tag);
    }
}
