package com.epam.training.taskmanager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.epam.training.image.MySuperImageLoader;
import com.epam.training.taskmanager.bo.Friend;
import com.epam.training.taskmanager.bo.NoteGsonModel;
import com.epam.training.taskmanager.helper.DataManager;
import com.epam.training.taskmanager.processing.FriendArrayProcessor;
import com.epam.training.taskmanager.source.HttpDataSource;
import com.epam.training.taskmanager.source.VkDataSource;

import java.util.List;

public class MainActivity extends ActionBarActivity implements DataManager.Callback<List<Friend>> {

    public static final int LOADER_ID = 0;
    public static final int COUNT = 50;

    private ArrayAdapter mAdapter;

    private FriendArrayProcessor mFriendArrayProcessor = new FriendArrayProcessor();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private MySuperImageLoader mMySuperImageLoader;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.activity_main);
        mMySuperImageLoader = MySuperImageLoader.get(MainActivity.this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        final HttpDataSource dataSource = getHttpDataSource();
        final FriendArrayProcessor processor = getProcessor();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update(dataSource, processor);
            }
        });
        update(dataSource, processor);
    }

    private FriendArrayProcessor getProcessor() {
        return mFriendArrayProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return VkDataSource.get(MainActivity.this);
    }

    private void update(HttpDataSource dataSource, FriendArrayProcessor processor) {
        DataManager.loadData(MainActivity.this,
                getUrl(COUNT, 0),
                dataSource,
                processor);
    }

    private String getUrl(int count, int offset) {
        return Api.FRIENDS_GET + "&count="+count+"&offset="+offset;
    }

    @Override
    public void onDataLoadStart() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        }
        findViewById(android.R.id.empty).setVisibility(View.GONE);
    }

    private List<Friend> mData;

    private boolean isPagingEnabled = true;

    private View footerProgress;

    private boolean isImageLoaderControlledByDataManager = false;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(List<Friend> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        findViewById(android.R.id.progress).setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        }
        ListView listView = (ListView) findViewById(android.R.id.list);
        footerProgress = View.inflate(MainActivity.this, R.layout.view_footer_progress, null);
        refreshFooter();
        if (mAdapter == null) {
            mData = data;
            mAdapter = new ArrayAdapter<Friend>(this, R.layout.adapter_item, android.R.id.text1, data) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(MainActivity.this, R.layout.adapter_item, null);
                    }
                    Friend item = getItem(position);
                    TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
                    textView1.setText(item.getName());
                    TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
                    textView2.setText(item.getNickname());
                    convertView.setTag(item.getId());
                    final String url = item.getPhoto();
                    final ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
                    mMySuperImageLoader.loadAndDisplay(url, imageView);
                    return convertView;
                }

            };
            listView.setFooterDividersEnabled(true);
            listView.addFooterView(footerProgress, null, false);
            listView.setAdapter(mAdapter);
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                private int previousTotal = 0;

                private int visibleThreshold = 5;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            if (!isImageLoaderControlledByDataManager) {
                                mMySuperImageLoader.resume();
                            }
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                            if (!isImageLoaderControlledByDataManager) {
                                mMySuperImageLoader.pause();
                            }
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            if (!isImageLoaderControlledByDataManager) {
                                mMySuperImageLoader.pause();
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    ListAdapter adapter = view.getAdapter();
                    int count = getRealAdapterCount(adapter);
                    if (count == 0) {
                        return;
                    }
                    if (previousTotal != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        previousTotal = totalItemCount;
                        isImageLoaderControlledByDataManager = true;
                        DataManager.loadData(new DataManager.Callback<List<Friend>>() {
                                                 @Override
                                                 public void onDataLoadStart() {
                                                     mMySuperImageLoader.pause();
                                                 }

                                                 @Override
                                                 public void onDone(List<Friend> data) {
                                                     updateAdapter(data);
                                                     refreshFooter();
                                                     mMySuperImageLoader.resume();
                                                     isImageLoaderControlledByDataManager = false;
                                                 }

                                                 @Override
                                                 public void onError(Exception e) {
                                                     MainActivity.this.onError(e);
                                                     mMySuperImageLoader.resume();
                                                     isImageLoaderControlledByDataManager = false;
                                                 }
                                             },
                                getUrl(COUNT, count),
                                getHttpDataSource(),
                                getProcessor());
                    }
                }

            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    Friend item = (Friend) mAdapter.getItem(position);
                    NoteGsonModel note = new NoteGsonModel(item.getId(), item.getFirstName(), item.getLastName());
                    intent.putExtra("item", note);
                    startActivity(intent);
                }
            });
            if (data != null && data.size() == COUNT) {
                isPagingEnabled = true;
            } else {
                isPagingEnabled = false;
            }
        } else {
            mData.clear();
            updateAdapter(data);
        }
        refreshFooter();
    }

    private void updateAdapter(List<Friend> data) {
        ListView listView = (ListView) findViewById(android.R.id.list);
        if (data != null && data.size() == COUNT) {
            isPagingEnabled = true;
            listView.addFooterView(footerProgress, null, false);
        } else {
            isPagingEnabled = false;
            listView.removeFooterView(footerProgress);
        }
        if (data != null) {
            mData.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
    }

    public static int getRealAdapterCount(ListAdapter adapter) {
        if (adapter == null) {
            return 0;
        }
        int count = adapter.getCount();
        if (adapter instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
            count = count - headerViewListAdapter.getFootersCount() - headerViewListAdapter.getHeadersCount();
        }
        return count;
    }

    private void refreshFooter() {
        if (footerProgress != null) {
            if (isPagingEnabled) {
                footerProgress.setVisibility(View.VISIBLE);
            } else {
                footerProgress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        findViewById(android.R.id.progress).setVisibility(View.GONE);
        findViewById(android.R.id.empty).setVisibility(View.GONE);
        TextView errorView = (TextView) findViewById(R.id.error);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(errorView.getText() + "\n" + e.getMessage());
    }

}
