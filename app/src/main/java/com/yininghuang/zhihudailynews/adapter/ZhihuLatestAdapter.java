package com.yininghuang.zhihudailynews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;
import com.yininghuang.zhihudailynews.utils.ImageLoader;
import com.yininghuang.zhihudailynews.widget.PosterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yining Huang on 2016/10/18.
 */

public class ZhihuLatestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ZhihuLatestNews> mLatestNewsList = new ArrayList<>();
    private List<ZhihuLatestNews.ZhihuStory> mZhihuStoryList = new ArrayList<>();
    private List<String> mReadIdList = new ArrayList<>();
    private boolean isLoadingComplete = false;

    private static final int TYPE_LOADING = -1;
    private static final int TYPE_POSTER = 0;
    private static final int TYPE_ITEM = 1;

    private OnItemClickListener mOnItemClickListener;

    public ZhihuLatestAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_POSTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster_layout, parent, false);
            return new PosterHolder(view);
        } else if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_status, parent, false);
            return new LoadingHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhihu_lastest, parent, false);
            return new NewsHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PosterHolder) {
            PosterView posterView = ((PosterHolder) holder).posterView;
            if (mLatestNewsList.size() > 0) {
                posterView.initPosters(mLatestNewsList.get(0).getTopStories());
                posterView.setOnPosterClickListener(story -> {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onPosterClick(story);
                });
                posterView.getAdapter().notifyDataSetChanged();
            }
        } else if (holder instanceof NewsHolder) {
            ZhihuLatestNews.ZhihuStory itemData = mZhihuStoryList.get(position - 1);
            NewsHolder newsHolder = (NewsHolder) holder;
            newsHolder.title.setText(itemData.getTitle());
            holder.itemView.setOnClickListener(view -> {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onNewsClick(mZhihuStoryList.get(holder.getAdapterPosition() - 1));
            });

            if (UserSettingConstants.DARK_THEME) {
                if (mReadIdList.contains(String.valueOf(itemData.getId())))
                    newsHolder.title.setTextColor(mContext.getResources().getColor(R.color.colorSecondTextDark));
                else
                    newsHolder.title.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryTextDark));
            } else {
                if (mReadIdList.contains(String.valueOf(itemData.getId())))
                    newsHolder.title.setTextColor(mContext.getResources().getColor(R.color.colorSecondText));
                else
                    newsHolder.title.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText));
            }

            if (itemData.getImages().size() == 0)
                return;
            ImageLoader.load(mContext, newsHolder.imageView, itemData.getImages().get(0));
        }
    }

    public void addNews(ZhihuLatestNews news) {
        mLatestNewsList.add(news);
        mZhihuStoryList.addAll(news.getStories());
    }

    public List<String> getReadIdList() {
        return mReadIdList;
    }

    public void addNewsList(List<ZhihuLatestNews> news) {
        for (ZhihuLatestNews data : news) {
            addNews(data);
        }
    }

    public String getOldestNewsDate() {
        if (mLatestNewsList.size() != 0)
            return mLatestNewsList.get(mLatestNewsList.size() - 1).getDate();
        return null;
    }

    public void setLoadComplete() {
        isLoadingComplete = true;
        notifyDataSetChanged();
    }

    public List<ZhihuLatestNews> getLatestNewsList() {
        return mLatestNewsList;
    }

    public List<ZhihuLatestNews.ZhihuStory> getZhihuStoryList() {
        return mZhihuStoryList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (mZhihuStoryList.size() == 0 || isLoadingComplete)
            return mZhihuStoryList.size() + 1;
        return mZhihuStoryList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_POSTER;
        else if (position == mZhihuStoryList.size() + 1)
            return TYPE_LOADING;
        else
            return TYPE_ITEM;
    }

    public interface OnItemClickListener {

        void onPosterClick(ZhihuLatestNews.ZhihuTopStory topStory);

        void onNewsClick(ZhihuLatestNews.ZhihuStory story);
    }

    public static class PosterHolder extends RecyclerView.ViewHolder {
        PosterView posterView;

        public PosterHolder(View itemView) {
            super(itemView);
            posterView = (PosterView) itemView.findViewById(R.id.posterView);
        }
    }

    public static class NewsHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;

        public NewsHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            imageView = (ImageView) itemView.findViewById(R.id.pic);
        }
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {

        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }
}
