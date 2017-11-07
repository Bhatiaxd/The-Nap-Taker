package com.karan.thenaptaker.napwebservice;

import android.content.Intent;
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

import java.util.ArrayList;

/**
 * {@link WebScrollingActivity} provides list of all benefits of Nap
 */
public  class WebScrollingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList array_list = new ArrayList();
        array_list.add("Napping benefits");
        array_list.add("Power Nap");
        array_list.add("NASA Nap");

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);


        ListView obj = (ListView) findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        Log.d("LIST",obj.getCount()+"-"+arrayAdapter.getCount());

        obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
               int id_To_Search = arg2 + 1;

                Intent intent = new  Intent(getApplicationContext(),WebViews.class);
                switch (id_To_Search){
                    case 2:
                        Site.site= "https://en.wikipedia.org/wiki/Power_nap";
                        break;
                    case 3:
                        Site.site= "https://science.nasa.gov/science-news/science-at-nasa/2005/03jun_naps";
                        break;
                    default:
                        Site.site= "https://sleepfoundation.org/sleep-topics/napping";
                }
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

    }
}
