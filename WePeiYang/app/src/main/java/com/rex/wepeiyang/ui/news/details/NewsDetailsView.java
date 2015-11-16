package com.rex.wepeiyang.ui.news.details;

import com.rex.wepeiyang.bean.News;

/**
 * Created by sunjuntao on 15/11/16.
 */
public interface NewsDetailsView {
    void showProgress();
    void hideProgress();
    void bindData(News news);
    void toastMessage(String msg);
}
