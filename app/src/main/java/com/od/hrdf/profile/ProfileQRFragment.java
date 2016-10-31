package com.od.hrdf.profile;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.od.hrdf.BOs.User;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmResults;

import static android.R.id.button2;
import static com.od.hrdf.HRDFApplication.realm;
import static com.od.hrdf.R.id.imageView;

public class ProfileQRFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static Bitmap bitmap;
    ImageButton refresh;
    User user;
    public final static int QRcodeWidth = 300;
    ImageView imageView;
    private View rootView;

    public ProfileQRFragment() {
        // Required empty public constructor
    }

    public static ProfileQRFragment newInstance(String param1, String param2) {
        ProfileQRFragment fragment = new ProfileQRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile_qr, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageView = (ImageView) rootView.findViewById(R.id.imageView3);
        user = User.getCurrentUser(realm);
        if(bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

        //REFRESH BUTTON
        refresh = (ImageButton) rootView.findViewById(R.id.imageButton2);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GenerateQRCode().execute(getPayloadForQR());
            }
        });

        new GenerateQRCode().execute(getPayloadForQR());
    }

    private String getPayloadForQR() {
        String jsonString = "";
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < user.getEvents().size(); i++) {
                jsonArray.put(user.getEvents().get(i).getId());
            }
            jsonObject.put("name", user.getName());
            jsonObject.put("email", user.getId());
            jsonObject.put("events", jsonArray.toString());
            jsonObject.put("nationality", user.getNationality());
            jsonString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor) : getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        Log.i(HRDFConstants.TAG, "Check= width=" + bitMatrixWidth + "     height=" + bitMatrixHeight);
        bitmap.setPixels(pixels, 0, 300, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    class GenerateQRCode extends AsyncTask<String, Integer, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                return TextToImageEncode(strings[0]);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmapp) {
            super.onPostExecute(bitmapp);
            if(bitmapp != null) {
                bitmap = bitmapp;
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}

