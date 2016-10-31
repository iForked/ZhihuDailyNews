package com.yininghuang.zhihudailynews.home;

import com.yininghuang.zhihudailynews.BasePresenter;
import com.yininghuang.zhihudailynews.BaseView;
import com.yininghuang.zhihudailynews.model.ZhihuTheme;

/**
 * Created by Yining Huang on 2016/10/31.
 */

public class ZhihuThemeContract {

    interface View extends BaseView<Presenter> {

        void showStories(ZhihuTheme theme);

        void addHistoryStories(ZhihuTheme theme);

        void setHistoryLoadingStatus(boolean status);

        void setLoadingStatus(boolean status);

        void showLoadError();

    }

    interface Presenter extends BasePresenter {

        void init();

        void reload();

        void queryHistoryStory(int id);

        void setThemeId(int id);
    }
}
