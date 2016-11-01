package com.od.hrdf.news;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.BOs.Article;

public class NewsDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PARAM_ID = "detail:_id";
    private SimpleDraweeView mHeaderImageView;
    private TextView mHeaderTitle;
    private Article article;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Latest News");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String newsId = getIntent().getStringExtra(EXTRA_PARAM_ID);
        article = Article.getArticle(HRDFApplication.realm, newsId);
        mHeaderImageView = (SimpleDraweeView) findViewById(R.id.imageview_header);
        mHeaderTitle = (TextView) findViewById(R.id.textview_title);
        mHeaderTitle.setText(article.getTitle());
        WebView description = (WebView) findViewById(R.id.description);
        description.setBackgroundColor(0x00000000);
        description.loadData(article.getDescription(), "text/html; charset=utf-8", "UTF-8");
        loadFullSizeImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        MenuItem shareItem = menu.findItem(R.id.share_item);
        Drawable icon = shareItem.getIcon();
        icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        Intent shareIntent = article.createShareIntent();
        mShareActionProvider.setShareIntent(shareIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void loadFullSizeImage() {

        if(article.getArticleContentImage() != null) {
            String image = article.getArticleContentImage();
            image = image.replaceAll(" ", "%20");
            Uri uri = Uri.parse(image);
            mHeaderImageView.setImageURI(uri);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            mHeaderImageView.setController(controller);
        }
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
