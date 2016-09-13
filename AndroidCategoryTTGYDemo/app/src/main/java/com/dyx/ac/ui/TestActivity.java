package com.dyx.ac.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.dyx.ac.R;
import com.dyx.ac.adapter.ItemAdapter;
import com.dyx.ac.adapter.TestBaseAdapter;
import com.dyx.ac.lib.StickyListHeadersListView;
import com.dyx.ac.view.DividerItemDecoration;

import java.util.Arrays;
import java.util.List;


/**
 *
 */
public class TestActivity extends Activity implements
        AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {

    private TestBaseAdapter mAdapter;
    private boolean fadeHeader = true;
    private StickyListHeadersListView stickyList;

    private RecyclerView leftRecyclerView;
    private ItemAdapter mItemAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /**
         * ListView相关
         */
        mAdapter = new TestBaseAdapter(this);
        stickyList = (StickyListHeadersListView) findViewById(R.id.list);
        stickyList.setOnItemClickListener(this);
        stickyList.setOnHeaderClickListener(this);
        stickyList.setOnStickyHeaderChangedListener(this);
        stickyList.setOnStickyHeaderOffsetChangedListener(this);
        stickyList.addHeaderView(getLayoutInflater().inflate(R.layout.list_header, null));
        stickyList.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        stickyList.setEmptyView(findViewById(R.id.empty));
        stickyList.setDrawingListUnderStickyHeader(true);
        stickyList.setAreHeadersSticky(true);
        stickyList.setAdapter(mAdapter);
        stickyList.setStickyHeaderTopOffset(-20);
        stickyList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount,
                                 int totalItemCount) {
                int selectionPos = mAdapter.getSectionForPosition(firstVisibleItem);
                leftRecyclerView.scrollToPosition(selectionPos);
//                View view = leftRecyclerView.getChildAt(selectionPos);
//                TextView item = (TextView) view.findViewById(R.id.tv_rv);
//                item.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        });

        /**
         * RecyclerView相关
         */
        mItemAdapter = new ItemAdapter(this, getDatas());
        leftRecyclerView = (RecyclerView) findViewById(R.id.rv_left);
        leftRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        leftRecyclerView.setHasFixedSize(true);
        leftRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        leftRecyclerView.setAdapter(mItemAdapter);
        mItemAdapter.setOnRvItemClickListener(new ItemAdapter.OnRvItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int needPos = mAdapter.getPositionForSection(position);
                mItemAdapter.setSelectPosition(position);
                stickyList.smoothScrollToPosition(needPos + 1);
            }
        });
    }

    private List<String> getDatas() {
        List<String> results = Arrays.asList(getResources().getStringArray(R.array.titles));
        return results;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Item " + position + " clicked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
        Toast.makeText(this, "Header " + headerId + " currentlySticky ? " + currentlySticky, Toast.LENGTH_SHORT).show();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
        if (fadeHeader && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        header.setAlpha(1);
    }

}