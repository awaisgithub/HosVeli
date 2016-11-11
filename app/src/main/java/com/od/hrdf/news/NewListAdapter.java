package com.od.hrdf.news;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.hrdf.R;
import com.od.hrdf.BOs.Article;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by MuhammadMahmoor on 10/23/16.
 */

public class NewListAdapter extends RealmRecyclerViewAdapter<Article, RecyclerView.ViewHolder> {

    Context context;
    View view = null;
    private NewsListAdapterInterface parentNotifier;
    OrderedRealmCollection<Article> data;

    public NewListAdapter(Context context, NewsListAdapterInterface activity, OrderedRealmCollection<Article> data) {
        super(context ,data, true);
        parentNotifier =  activity;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_news, parent, false);
        return NewsListViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Article newsItem = data.get(position);
        final NewsListViewHolder viewHolder = (NewsListViewHolder) holder;

        viewHolder.title.setText(newsItem.getTitle());
        viewHolder.description.setText(Html.fromHtml(newsItem.getDescription()));
        viewHolder.dummyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentNotifier.gotoDetailActivity(newsItem.getId(), view);
            }
        });

        if(newsItem.getArticleSummaryImage() != null) {
            String image = newsItem.getArticleSummaryImage();
            image = image.replaceAll(" ", "%20");
            Uri uri = Uri.parse(image);
            viewHolder.summaryImageView.setImageURI(uri);
            viewHolder.summaryImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            viewHolder.summaryImageView.setController(controller);
        }

        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentNotifier.gotoDetailActivity(newsItem.getId(), view);
            }
        });

    }

    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(
                String id,
                @Nullable ImageInfo imageInfo,
                @Nullable Animatable anim) {

        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {

        }

        @Override
        public void onFailure(String id, Throwable throwable) {

        }
    };
}
