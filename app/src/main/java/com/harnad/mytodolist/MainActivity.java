package com.harnad.mytodolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends Activity {
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    String messageText;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent();
                intent.setClass(MainActivity.this,EditMessageClass.class);
                intent.putExtra(Intent_Constants.INTENT_MESSAGE_DATA, arrayList.get(position).toString());
                intent.putExtra(Intent_Constants.INTENT_ITEM_POSITION, position);
                startActivityForResult(intent,Intent_Constants.INTENT_REQUEST_CODE_TWO);
            }
        });

        try{
            Scanner sc= new Scanner(openFileInput("todo.txt"));
            while(sc.hasNextLine())
            {
                String data=sc.nextLine();
                arrayAdapter.add(data);
            }
            sc.close();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            PrintWriter pw= new PrintWriter(openFileOutput("todo.txt", Context.MODE_PRIVATE));
            for(String data : arrayList)
            {
                pw.println(data);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finish();
    }

    public void onClick(View v)
    {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,EditFieldClass.class);
        startActivityForResult(intent,Intent_Constants.INTENT_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode==Intent_Constants.INTENT_REQUEST_CODE)
       {
            messageText=data.getStringExtra(Intent_Constants.INTENT_MESSAGE_FIELD);
            arrayList.add(messageText);
            arrayAdapter.notifyDataSetChanged();
       }
       else if(resultCode==Intent_Constants.INTENT_REQUEST_CODE_TWO)
       {
           messageText=data.getStringExtra(Intent_Constants.INTENT_CHANGED_MESSAGE);
           position= data.getIntExtra(Intent_Constants.INTENT_ITEM_POSITION,-1);
           arrayList.remove(position);
           arrayList.add(position,messageText);
           arrayAdapter.notifyDataSetChanged();
       }


    }
}