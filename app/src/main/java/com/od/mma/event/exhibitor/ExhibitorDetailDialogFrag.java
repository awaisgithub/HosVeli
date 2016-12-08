package com.od.mma.event.exhibitor;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.mma.R;
import com.od.mma.Utils.MMAConstants;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;

/**
 * Created by Muhammad Mahmoor on 2/16/2015.
 */
public class ExhibitorDetailDialogFrag extends SupportBlurDialogFragment {

    View view = null;
    boolean isDismissed;
    //private WebView descriptionView;
    private TextView descriptionView;
    private TextView nameView;
    private SimpleDraweeView imageView;
    private String imageURL;
    private String name;
    private String description;

    public ExhibitorDetailDialogFrag() {
    }

    // Fragment life-cyle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_exhibitor_detail, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isDismissed = true;
            }
        });
        final Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void init() {
        imageView = (SimpleDraweeView) view.findViewById(R.id.image);

        if (imageURL != null) {
            imageURL = imageURL.replaceAll(" ", "%20");
            Log.i(MMAConstants.TAG, "ExhibitorImageURL ="+imageURL);
            Uri uri = Uri.parse(imageURL);
            imageView.setImageURI(uri);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            imageView.setController(controller);
        }

        nameView = (TextView) view.findViewById(R.id.exhibitor_name);
        nameView.setText(name);

        if(description.isEmpty())
            description = getString(R.string.event_exhibitor_not_detail);

        descriptionView = (TextView) view.findViewById(R.id.description_wv);
        descriptionView.setText(Html.fromHtml(description));
        descriptionView.setMovementMethod(new ScrollingMovementMethod());
        //descriptionView.loadData(description, "text/html; charset=utf-8", "UTF-8");
        //descriptionView.setVerticalScrollBarEnabled(false);
    }

    public void setExhibitorDetails(String imageURL, String description, String name) {
        this.imageURL = imageURL;
        this.description = description;
        this.name = name;
    }

    @Override
    public Context getContext() {
        return null;
    }

    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(
                String id,
                @Nullable ImageInfo imageInfo,
                @Nullable Animatable animimageView) {
                imageView.setAspectRatio(1.1f);
            int color = getResources().getColor(R.color.colorPrimaryDark);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);
            roundingParams.setBorder(color, 2);
            imageView.getHierarchy()
                    .setRoundingParams(roundingParams);

        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {

        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            imageView.setAspectRatio(1.1f);
            int color = getResources().getColor(R.color.colorPrimaryDark);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);
            roundingParams.setBorder(color, 1.0f);
            imageView.getHierarchy().setRoundingParams(roundingParams);
            imageView.setImageResource(R.drawable.placeholder);
        }
    };

    @Override
    protected boolean isActionBarBlurred() {
        // Enable or disable the blur effect on the action bar.
        // Disabled by default.
        return true;
    }

    @Override
    protected float getDownScaleFactor() {
        // Allow to customize the down scale factor.
        return 9.0f;
    }

    @Override
    protected int getBlurRadius() {
        // Allow to customize the blur radius factor.
        return 8;
    }

}


