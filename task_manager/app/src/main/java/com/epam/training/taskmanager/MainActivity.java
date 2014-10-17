package com.epam.training.taskmanager;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.epam.training.taskmanager.callbacks.SimpleCallback;
import com.epam.training.taskmanager.helper.DataManager;
import com.epam.training.taskmanager.processing.RedirectProcessor;
import com.epam.training.taskmanager.processing.StringProcessor;
import com.epam.training.taskmanager.source.ArrayStringDataSource;
import com.epam.training.taskmanager.source.HttpDataSource;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements DataManager.Callback<ArrayList<String>> {

    private ArrayAdapter<String> mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayStringDataSource dataSource = new ArrayStringDataSource();
        final RedirectProcessor<ArrayList<String>> arrayRedirectProcessor = new RedirectProcessor<ArrayList<String>>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DataManager.loadData(MainActivity.this, null, dataSource, arrayRedirectProcessor);
            }
        });
        DataManager.loadData(this, null, dataSource, arrayRedirectProcessor);

        HttpDataSource httpDataSource = HttpDataSource.get(this);
        SimpleCallback<String> callback = new SimpleCallback<String>() {

            @Override
            public void onDone(Object data) {
                Log.d("MainActivity", "onDone " + data);
            }

        };
        StringProcessor stringProcessor = new StringProcessor();
        DataManager.loadData(
                callback,
                "https://dl.dropboxusercontent.com/u/16403954/test.json",
                httpDataSource,
                stringProcessor
        );

        DataManager.loadData(
                callback,
                "http://google.com",
                httpDataSource,
                stringProcessor);
    }

    @Override
    public void onDataLoadStart() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        }
        findViewById(android.R.id.empty).setVisibility(View.GONE);
    }

    @Override
    public void onDone(ArrayList<String> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        findViewById(android.R.id.progress).setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        }
        AdapterView listView = (AbsListView) findViewById(android.R.id.list);
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<String>(this, R.layout.adapter_item, android.R.id.text1, data) {

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
            listView.setAdapter(mAdapter);
        } else {
            //only for honeycomb
            mAdapter.addAll(data);
        }
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
