package com.twt.service.ui.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.bean.NewsItem;
import com.twt.service.interactor.NoticeInteractorImpl;
import com.twt.service.ui.BaseActivity;
import com.twt.service.ui.common.OnRcvScrollListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NoticeActivity extends BaseActivity implements NoticeView {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.rv_notice)
    RecyclerView rvNotice;
    @InjectView(R.id.srl_notice)
    SwipeRefreshLayout srlNotice;
    private NoticePresenter presenter;
    private NoticeAdapter adapter = new NoticeAdapter(this);

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NoticeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new NoticePresenterImpl(this, new NoticeInteractorImpl());
        srlNotice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshNoticeItems();
            }
        });
        srlNotice.setColorSchemeColors(getResources().getColor(R.color.news_primary_color));
        rvNotice.setOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                presenter.loadMoreNoticeItems();
            }
        });
        rvNotice.setAdapter(adapter);
        rvNotice.setLayoutManager(new LinearLayoutManager(this));
        presenter.refreshNoticeItems();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.news_primary_color));
        }
    }


    @Override
    public void showProgress() {
        srlNotice.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        srlNotice.setRefreshing(false);
    }

    @Override
    public void useFooter() {
        adapter.setUseFooter(true);
    }

    @Override
    public void hideFooter() {
        adapter.setUseFooter(false);
    }

    @Override
    public void toastMessage(String msg) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void refreshItems(List<NewsItem> items) {
        adapter.refreshItems(items);
    }

    @Override
    public void loadMoreItems(List<NewsItem> items) {
        adapter.addItems(items);
    }

    @Override
    public void setRefreshEnable(boolean refreshEnable) {
        if (srlNotice != null) {
            srlNotice.setRefreshing(refreshEnable);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
