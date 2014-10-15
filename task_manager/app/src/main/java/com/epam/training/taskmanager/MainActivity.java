package com.epam.training.taskmanager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.epam.training.taskmanager.helper.DataManager;

import java.util.List;

public class MainActivity extends ActionBarActivity implements DataManager.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataManager.loadData(this);
    }

    @Override
    public void onDataLoadStart() {
        findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        findViewById(android.R.id.empty).setVisibility(View.GONE);
    }

    @Override
    public void onDone(List<String> data) {
        findViewById(android.R.id.progress).setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        }
        AdapterView listView = (AbsListView) findViewById(android.R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.adapter_item, android.R.id.text1, data) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(MainActivity.this, R.layout.adapter_item, null);
                }
                String item = getItem(position);
                TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
                textView1.setText(item);
                TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
                textView2.setText(item.substring(5));
                return convertView;
            }

        };
        listView.setAdapter(adapter);
    }

    @Override
    public void onError(Exception e) {
        findViewById(android.R.id.progress).setVisibility(View.GONE);
        findViewById(android.R.id.empty).setVisibility(View.GONE);
        TextView errorView = (TextView) findViewById(R.id.error);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(errorView.getText() + "\n" + e.getMessage());
    }

}
