package com.od.mma.event.floorplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.od.mma.R;

/**
 * Created by Awais on 10/31/2016.
 */

public class FragmentFloorImageZoom extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_floor_image_zoom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (imageUrl != null) {
            imageUrl = imageUrl.replaceAll(" ", "%20");
        }

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(zoom_name);
        WebView wv = (WebView) findViewById(R.id.zoom_floor);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.loadUrl(imageUrl);
    }
}

