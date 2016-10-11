package com.application.closed.testapp;


import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class HistoryActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    final Uri CONTACT_URI = Uri.parse("content://com.application.provider.LinkProvider/links");
    ListView lvLinks;
    Cursor cursor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        lvLinks = (ListView) findViewById(R.id.lvLinks);
        showListView(null);
        lvLinks.setOnItemClickListener(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Sort by");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent();
        intent.setAction("com.example.applicationb.SAVE");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setComponent(new ComponentName("com.example.applicationb", "com.example.applicationb.SaveActivity"));

        Cursor cursor = (Cursor) lvLinks.getItemAtPosition(position);
        String link =
                cursor.getString(cursor.getColumnIndexOrThrow("ref"));
        long idFromDb = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
        int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));


        intent.putExtra("link", link);
        intent.putExtra("id", idFromDb);
        intent.putExtra("status", status);

        try{
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.status_sort:
                showListView("status ASC");
                return true;

            case R.id.time_sort:
                showListView("time DESC");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showListView(String order){
        cursor = getContentResolver().query(CONTACT_URI, null, null,
                null, order);
        startManagingCursor(cursor);
        MyAdapter myAdapter = new MyAdapter(this, cursor);
        lvLinks.setAdapter(myAdapter);
    }
}
