package com.od.hrdf.event.floorplan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.hrdf.R;
import com.od.hrdf.event.floorplan.FloorImageFragment;

import java.io.InputStream;

import static android.R.attr.mode;
import static android.view.Menu.NONE;
import static com.od.hrdf.R.id.start;

/**
 * Created by Awais on 10/31/2016.
 */

public class FragmentFloorImageZoom extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_floor_image_zoom);
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        Bundle bundle = getIntent().getExtras();
        String zoom_name = bundle.getString("floor_plan_name");
        String zoom_url = bundle.getString("floor_plan_image_url");
        String imageUrl =  zoom_url;
        Toolbar toolbar_back = (Toolbar) findViewById(R.id.toolbar_back);
        toolbar_back.setTitle(zoom_name);
        setSupportActionBar(toolbar_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (imageUrl != null) {
            imageUrl = imageUrl.replaceAll(" ", "%20");
        }
        WebView wv = (WebView) findViewById(R.id.zoom_floor);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.loadUrl(imageUrl);
    }
}

