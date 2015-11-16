package com.rex.wepeiyang.ui.news.details;

import android.content.Context;

import com.rex.wepeiyang.bean.News;
import com.rex.wepeiyang.interactor.NewsDetailsInteractor;

/**
 * Created by sunjuntao on 15/11/16.
 */
public class NewsDetailsPresenterImpl implements NewsDetailsPresenter, OnGetNewsDetailsCallback {

    private NewsDetailsView view;
    private NewsDetailsInteractor interactor;

    public NewsDetailsPresenterImpl(NewsDetailsView view, NewsDetailsInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void loadNewsDetails(int index) {
        view.showProgress();
        interactor.getNewsDetails(index, this);
    }

    @Override
    public void onSuccess(News news) {
        view.hideProgress();
        view.bindData(news);
    }

    @Override
    public void onFailure(String errorMsg) {
        view.hideProgress();
        view.toastMessage(errorMsg);
    }
}
