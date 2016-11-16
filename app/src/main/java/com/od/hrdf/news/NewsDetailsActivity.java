package com.od.hrdf.news;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.hrdf.API.Api;
import com.od.hrdf.BOs.NewsItem;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.BOs.Article;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.abouts.AboutUs;

import java.util.ArrayList;

import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.od.hrdf.HRDFApplication.realm;

public class NewsDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PARAM_ID = "detail:_id";
    private SimpleDraweeView mHeaderImageView;
    private TextView mHeaderTitle;
    private WebView description;
    private Article article;
    private ShareActionProvider mShareActionProvider;
    private ProgressDialog progressDialog;
    private AboutUs aboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String newsId = getIntent().getStringExtra(EXTRA_PARAM_ID);
        aboutUs = AboutUs.getAboutUs(realm);
        article = Article.getArticle(realm, newsId);
        mHeaderImageView = (SimpleDraweeView) findViewById(R.id.imageview_header);
        mHeaderTitle = (TextView) findViewById(R.id.textview_title);
        description = (WebView) findViewById(R.id.description);
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(HRDFConstants.LAUNCH_TYPE_KEY)) {
            String launchType = intent.getStringExtra(HRDFConstants.LAUNCH_TYPE_KEY);
            if (launchType.equalsIgnoreCase(HRDFConstants.LAUNCH_TYPE_GCM)) {
                String articleId = intent.getStringExtra(HRDFConstants.KEY_GCM_ITEM_ID);
                showProgressDialog(R.string.news_loading_detail);
                fetchArticleById(articleId);
            } else {
                setInfo();
            }
        } else {
            setInfo();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        MenuItem shareItem = menu.findItem(R.id.share_item);
        Drawable icon = shareItem.getIcon();
        icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        //Intent shareIntent = aboutUs.createShareIntent();
        mShareActionProvider.setShareIntent(chooserIntent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setInfo() {
        mHeaderTitle.setText(article.getTitle());
        description = (WebView) findViewById(R.id.description);
        description.setBackgroundColor(0x00000000);
        description.getSettings().setJavaScriptEnabled(true);

        //String htmlContent = "<!DOCTYPE html><html lang='en'><head><meta charset='utf-8'><title>HRDF EVENTS</title><meta name='viewport' content='width=device-width, initial-scale=1'><link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'></head><body style='background-color: transparent;color:#000000;font-family:Helvetica Neue;font-weight:400;font-size:14px;font-color:#000000;'>"+article.getDescription()+"</body></html>";
        description.getSettings().setStandardFontFamily("sans-serif");
        description.getSettings().setFixedFontFamily("sans-serif");
        description.getSettings().setFantasyFontFamily("sans-serif");
        description.getSettings().setDefaultFontSize(15);
        description.loadData(article.getDescription(), "text/html; charset=utf-8", "UTF-8");
        loadFullSizeImage();
    }

    private void loadFullSizeImage() {

        if (article.getArticleContentImage() != null) {
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

    private String fixLinks(String body) {
        String regex = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        body = body.replaceAll(regex, "<a href=\"$0\">$0</a>");
        Log.d(HRDFConstants.TAG, body);
        return body;
    }

    private void fetchArticleById(final String articleId) {
        RealmQuery query = realm.where(Article.class);
        Article.fetchArticles(this, realm, Api.urlArticleById(articleId), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                hideProgressDialog();
                Article articleItem = Article.getArticle(realm, articleId);
                if (articleItem != null) {
                    article = articleItem;
//                    AboutUs aboutUs = AboutUs.getAboutUs(realm);
//                    Intent shareIntent = aboutUs.createShareIntent();
//                    mShareActionProvider.setShareIntent(shareIntent);
                    setInfo();
                }
            }

            @Override
            public void fetchDidFail(Exception e) {
                hideProgressDialog();
                showMessage(R.string.server_error);
            }
        });
    }

    private void showMessage(int message) {
        RelativeLayout messageLayout = (RelativeLayout) findViewById(R.id.error_layout);
        TextView messageView = (TextView) findViewById(R.id.label);
        messageView.setText(message);
        messageLayout.setVisibility(View.VISIBLE);
        description.setVisibility(View.GONE);
    }

    private void hideMessage() {
        RelativeLayout messageLayout = (RelativeLayout) findViewById(R.id.error_layout);
        messageLayout.setVisibility(View.GONE);
        description.setVisibility(View.VISIBLE);
    }

    private void showProgressDialog(int message) {
        progressDialog.setMessage(getResources().getString(message));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    private Intent chooserIntent() {
        Drawable mDrawable = getResources().getDrawable(R.drawable.share_image, null);
        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, aboutUs.getSocialMediaShareText() + " \n" + aboutUs.getSocialMediaShareLink());
//        if(path != null) {
//            Uri uri = Uri.parse(path);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        }
        shareIntent.setType("text/plain");
        return shareIntent;
    }
}
