package com.od.mma.event.speaker.uploads;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.mma.BOs.Document;
import com.od.mma.R;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class SpeakerUploadGridAdapter extends RealmRecyclerViewAdapter<Document, RecyclerView.ViewHolder> {
    private Context context;
    private RealmResults<Document> data;
    private View view;
    SpeakerDocNotifier notifier;

    public SpeakerUploadGridAdapter(@NonNull Context context, @Nullable RealmResults<Document> data, SpeakerDocNotifier notifier, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.data = data;
        this.notifier = notifier;
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = inflater.inflate(R.layout.adapter_speaker_uploads_grid, parent, false);
        return new SpeakerUploadGridViewHolder((RelativeLayout) v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SpeakerUploadGridViewHolder documentGridViewHolder = (SpeakerUploadGridViewHolder) holder;
        final Document document = data.get(position);

        documentGridViewHolder.name.setText(document.getDesc());
        //documentGridViewHolder.photoView.setImageResource(R.drawable.notepad);

//        if(sponsor.getImage() != null) {
//            String image = sponsor.getImage();
//            image = image.replaceAll(" ", "%20");
//            Uri uri = Uri.parse(image);
//            sponsorGridViewHolder.photoView.setImageURI(uri);
//            sponsorGridViewHolder.photoView.setScaleType(ImageView.ScaleType.FIT_XY);
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setControllerListener(controllerListener)
//                    .setUri(uri)
//                    .build();
//            sponsorGridViewHolder.photoView.setController(controller);
//        }
//
        documentGridViewHolder.parent.setTag(document);
        documentGridViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Document document = (Document) view.getTag();
                //notifier.downloadFile(document.getFile(), document.getDesc(), document.getFiletype());
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(document.getFile()));
                try {
                    context.startActivity(i);
                } catch (ActivityNotFoundException e) {
                    // Chrome is probably not installed
                    // Try with the default browser
                    i.setPackage(null);
                    context.startActivity(i);
                }
            }
        });
    }
}
