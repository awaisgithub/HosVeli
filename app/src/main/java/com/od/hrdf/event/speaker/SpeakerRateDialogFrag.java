package com.od.hrdf.event.speaker;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.hrdf.API.Api;
import com.od.hrdf.BOs.Speaker;
import com.od.hrdf.BOs.User;
import com.od.hrdf.CallBack.StatusCallBack;
import com.od.hrdf.Payload.JSONPayloadManager;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;

import org.json.JSONObject;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;

import static com.od.hrdf.HRDFApplication.realm;

/**
 * Created by Muhammad Mahmoor on 2/16/2015.
 */
public class SpeakerRateDialogFrag extends SupportBlurDialogFragment {

    static final float DEFAULT_BLUR_DOWN_SCALE_FACTOR = 4.0f;

    /**
     * Radius used to blur the background
     */
    static final int DEFAULT_BLUR_RADIUS = 8;

    /**
     * Default dimming policy.
     */
    static final boolean DEFAULT_DIMMING_POLICY = false;

    /**
     * Default debug policy.
     */
    static final boolean DEFAULT_DEBUG_POLICY = false;

    /**
     * Default action bar blurred policy.
     */
    static final boolean DEFAULT_ACTION_BAR_BLUR = false;

    /**
     * Default use of RenderScript.
     */
    static final boolean DEFAULT_USE_RENDERSCRIPT = false;

    View view = null;
    boolean isDismissed;
    private String speakerId;
    private String eventId;
    private float rating = 0.0f;

    public SpeakerRateDialogFrag() {
    }

    // Fragment life-cyle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_rate_speaker, container, false);
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
        final Speaker speaker = Speaker.getSpeaker(speakerId, realm);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;
            }
        });

        final EditText remarks = (EditText) view.findViewById(R.id.remarks);

        Button submit = (Button) view.findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rating == 0.0f) {
                    showActionSnackBarMessage("You need to rate speaker before submission.");
                    return;
                }

                User user = User.getCurrentUser(realm);
                realm.beginTransaction();
                speaker.setRating(rating);
                speaker.setRated(true);
                realm.commitTransaction();
                String rating = "0";
                try {
                    rating = String.valueOf(speaker.getRating());
                } catch (Exception ex) {

                }
                SpeakerRatingBO speakerRatingBO =  new SpeakerRatingBO();
                speakerRatingBO.setEmail(user.getId());
                speakerRatingBO.setEvent(eventId);
                speakerRatingBO.setSpeaker(speaker.getId());
                speakerRatingBO.setRating(rating);
                speakerRatingBO.setRemarks(remarks.getText().toString());
                rateSpeaker(speakerRatingBO);
                dismiss();
            }
        });
    }

    private void rateSpeaker(SpeakerRatingBO speakerRatingBO) {
        Speaker.rateSpeaker(Api.urlSubmitUserRating(), speakerRatingBO, new StatusCallBack() {
            @Override
            public void success(JSONObject response) {

            }

            @Override
            public void failure(String response) {

            }
        });
    }

    public void setSpeakerDetails(String speakerId, String eventId) {
        this.speakerId = speakerId;
        this.eventId = eventId;
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    protected float getDownScaleFactor() {
        // Allow to customize the down scale factor.
        return 8.0f;
    }

    @Override
    protected int getBlurRadius() {
        // Allow to customize the blur radius factor.
        return 8;
    }

    @Override
    protected boolean isActionBarBlurred() {
        // Enable or disable the blur effect on the action bar.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isDimmingEnable() {
        // Enable or disable the dimming effect.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isRenderScriptEnable() {
        // Enable or disable the use of RenderScript for blurring effect
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isDebugEnable() {
        // Enable or disable debug mode.
        // False by default.
        return false;
    }

    private void showActionSnackBarMessage(String message) {
        View localView = view.findViewById(R.id.parentView);
        final Snackbar snackbar = Snackbar.make(localView, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setMaxLines(2);
        snackbar.show();
    }

    public class SpeakerRatingBO {
        String email;
        String event;
        String speaker;
        String rating;
        String remarks;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getSpeaker() {
            return speaker;
        }

        public void setSpeaker(String speaker) {
            this.speaker = speaker;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }

}


