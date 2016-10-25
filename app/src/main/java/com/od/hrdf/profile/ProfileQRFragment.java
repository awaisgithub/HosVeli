package com.od.hrdf.profile;


import android.graphics.Bitmap;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileQRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileQRFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageButton refresh;
    User user;
    public final static int QRcodeWidth = 300;
    Bitmap bitmap;
    ImageView imageView;
    private View rootView;

    public ProfileQRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileQRFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.i("HRDF", " EDITTEXTVALUE " + jsonObject.toString());
            bitmap = TextToImageEncode(jsonObject.toString());
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //REFRESH BUTTON
        refresh = (ImageButton) rootView.findViewById(R.id.imageButton2);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject1 = new JSONObject();
                JSONArray jsonArray1 = new JSONArray();
                try {
                    for (int i = 0; i < user.getEvents().size(); i++) {
                        jsonArray1.put(user.getEvents().get(i).getId());
                    }
                    jsonObject1.put("name", user.getName());
                    jsonObject1.put("email", user.getId());
                    jsonObject1.put("events", jsonArray1.toString());
                    jsonObject1.put("nationality", user.getNationality());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Log.i("HRDF", " EDITTEXTVALUE " + jsonObject1.toString());
                    bitmap = TextToImageEncode(jsonObject1.toString());
                    imageView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
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
}

