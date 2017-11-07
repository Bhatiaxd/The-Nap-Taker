package com.karan.thenaptaker.naplist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karan.thenaptaker.R;
import com.karan.thenaptaker.napdatabase.DBHelper;
import com.karan.thenaptaker.napdetail.NapDetail;

import java.util.ArrayList;


/**
 * {@link ScrollingActivity} provides common implementation to {@link EditScrollingActivity} and
 * {@link StartScrollingActivity}
 */
public abstract class ScrollingActivity extends AppCompatActivity {

    /**
     * obj stores list of all Nap user can edit or start
     */
    private ListView obj;

    /**
     * mydb is used to retrieve list of all Nap user can edit or start from database
     */
    DBHelper mydb;
    public ArrayList array_list;
    public int idEU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mydb = new DBHelper(this);
        array_list = mydb.getAllNapDetails();
        updateList();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);


        obj = (ListView)findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);

        Log.d("LIST",obj.getCount()+"-"+arrayAdapter.getCount());
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                int id_To_Search = arg2 + 1 + idEU;
                Intent intent = setIntents();
                SharedPreferences settingNap = getSharedPreferences("Id", MODE_PRIVATE);
                SharedPreferences.Editor editorNap = settingNap.edit();
                editorNap.putInt("id",id_To_Search);
                editorNap.commit();
               // intent.putExtras(dataBundle);
                startActivity(intent);
                mydb.close();
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setting = new Intent(getApplicationContext(),NapDetail.class);
                startActivity(setting);
                mydb.close();
                finish();
            }
        });

    }

    public void updateList(){

    }

    /**
     * setIntents return intent according to selected activity
     */
    public abstract Intent setIntents();
}
