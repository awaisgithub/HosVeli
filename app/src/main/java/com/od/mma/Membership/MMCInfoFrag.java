package com.od.mma.Membership;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.od.mma.API.Api;
import com.od.mma.BOs.User;
import com.od.mma.CallBack.ServerReadCallBack;
import com.od.mma.R;
import com.od.mma.Utils.HosVeliConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

import static com.od.mma.MMAApplication.realm;

/**
 * Created by awais on 29/12/2016.
 */

public class MMCInfoFrag extends Fragment {
    private static int CAPTURE_IMAGE = 1;
    private static int ATTACH_IMAGE = 2;
    Button next, back;
    TextView image_title;
    //    TextView title;
    Spinner id_uni;
    Calendar myCalendar;
    EditText mmc_reg_no;
    EditText date_pik;
    EditText mmc_tpc_reg_no;
    boolean error = false;
    boolean error1 = false;
    boolean error2 = false;
    FragInterface mem_interface;
    private View rootView;
    private ImageView mmc_certificate;
    private File imageFile;
    Membership membership;
    boolean spinnerUniFromServer = false;
    String pic_url = "";

    public static MMCInfoFrag newInstance(String text) {
        MMCInfoFrag f = new MMCInfoFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }

    public static Bitmap getRoundCornerBitmap(Bitmap bitmap, int radius) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final RectF rectF = new RectF(0, 0, w, h);

        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rectF, paint);

        /**
         * here to define your corners, this is for left bottom and right bottom corners
         */
        final Rect clipRect = new Rect(0, radius, w, h - radius);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawRect(clipRect, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rectF, paint);

        bitmap.recycle();

        return output;
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_membership_mmc_info, container, false);
        initView();

        return rootView;
    }

    private void populateUniSpinnerFromServer() {
        User.getSpinnerList(Api.urlDataListData(HosVeliConstants.list_university), new ServerReadCallBack() {
            @Override
            public void success(JSONArray response) {
                List<String> title_list = new ArrayList<String>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        title_list.add(response.getJSONObject(i).optString("c_universityname"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String[] titleArray = new String[title_list.size()];
                titleArray = title_list.toArray(titleArray);
                ArrayAdapter<String> server = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, titleArray);
                server.setDropDownViewResource(R.layout.spinner_dropdown_item);
                id_uni.setAdapter(
                        new NoneSelectSpinnerAdapter(server, R.layout.spinner_hint_frag11,
                                getActivity()));
                int spinnerPosition;
                spinnerPosition = server.getPosition(membership.getStudentMemberUniversity());
                id_uni.setSelection(spinnerPosition + 1);

                spinnerUniFromServer = true;
            }

            @Override
            public void failure(String response) {
                spinnerUniFromServer = false;
                if (response.contains(""))
                    Log.i(HosVeliConstants.TAG_HosVeli, "No Such List exist");
                else
                    Log.i(HosVeliConstants.TAG_HosVeli, "err = " + response.toString());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mem_interface = (FragInterface) context;
    }

    private void initView() {
        spinnerUniFromServer = false;
        membership = Membership.getCurrentRegistration(realm, User.getCurrentUser(realm).getId());
        image_title = (TextView) rootView.findViewById(R.id.user_profile_name2);
        mmc_tpc_reg_no = (EditText) rootView.findViewById(R.id.tpc_regno);
        //   title = (TextView) rootView.findViewById(R.id.title);
        id_uni = (Spinner) rootView.findViewById(R.id.id_uni);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_uni, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        id_uni.setPrompt("Select University Name");
        id_uni.setAdapter(
                new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag11,
                        getActivity()));
        mmc_reg_no = (EditText) rootView.findViewById(R.id.name);
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        date_pik = (EditText) rootView.findViewById(R.id.date_register);
        date_pik.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String imageName = getString(R.string.app_name) + String.valueOf(System
                .currentTimeMillis()) + ".jpg";
        imageFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), imageName);
        mmc_certificate = (ImageView) rootView.findViewById(R.id.user_profile_nric);
        mmc_certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachImage();
            }
        });

        Log.i(HosVeliConstants.TAG_HosVeli, "category = " + membership.getMain_category());
        if (membership.getMembershipCategory().equalsIgnoreCase("Student")) {
            populateUniSpinnerFromServer();
            //  title.setText("University Info");
            id_uni.setVisibility(View.VISIBLE);
            mmc_reg_no.setVisibility(View.GONE);
            mmc_tpc_reg_no.setVisibility(View.GONE);
            date_pik.setHint("Year of Degree Completion");
            image_title.setText("Letter from University OR Student Card");
        } else {
            //   title.setText("MMC Registration Info");
            id_uni.setVisibility(View.GONE);
            mmc_reg_no.setVisibility(View.VISIBLE);
            mmc_tpc_reg_no.setVisibility(View.VISIBLE);
            date_pik.setText("Date of Registration");
            image_title.setText("Upload MMC Certificate");
        }

        if (!membership.isLoadFromServer() && membership.getMembershipCategory().equalsIgnoreCase("Student")) {
            if (membership.getUni_no() != -1) {
                id_uni.setSelection(membership.getUni_no());
            }
        }
        if (!(membership.getApplicantMMCRegistrationNo() == null)) {
            mmc_reg_no.setText(membership.getApplicantMMCRegistrationNo());
        }
        if (!(membership.getTpcRegistrationNo() == null)) {
            mmc_tpc_reg_no.setText(membership.getTpcRegistrationNo());
        }
        if (!(membership.getDateOfRegistrationMMC() == null)) {
            date_pik.setText(membership.getDateOfRegistrationMMC());
        }
        if (!membership.isLoadFromServer()) {

            if (image_title.getText().toString().equalsIgnoreCase("Upload MMC Certificate")) {
                if (!(membership.getMmc_certificate() == null || membership.getMmc_certificate().length == 0)) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(membership.getMmc_certificate(), 0, membership.getMmc_certificate().length);
                    mmc_certificate.setImageBitmap(getRoundCornerBitmap(bitmap, 40));
                }
            } else {
                pic_url = membership.getApplicantMMCRegistrationCopy();
                if (pic_url.equalsIgnoreCase("") || pic_url.isEmpty())
                    pic_url = membership.getStudentMemberUniversityLetter();
                new GenerateImage().execute();
            }
            if(image_title.getText().toString().equalsIgnoreCase("Letter from University OR Student Card")) {
                if(!(membership.getUni_student_card() == null || membership.getUni_student_card().length == 0)) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(membership.getUni_student_card(), 0, membership.getUni_student_card().length);
                    mmc_certificate.setImageBitmap(getRoundCornerBitmap(bitmap, 40));
                }
            }
            else {
                pic_url = membership.getApplicantMMCRegistrationCopy();
                if (pic_url.equalsIgnoreCase("") || pic_url.isEmpty())
                    pic_url = membership.getStudentMemberUniversityLetter();
                new GenerateImage().execute();
            }
            }



        if (membership.isValidation()) {
            loadItems();
        }


        listeners();
        navigate();

    }

    class GenerateImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(pic_url);
                pic_url = "";
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmapp) {
            super.onPostExecute(bitmapp);
            if (bitmapp != null) {
                mmc_certificate.setImageBitmap(getRoundCornerBitmap(bitmapp, 40));
            }
        }
    }

    private void listeners() {
        id_uni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, final long id) {
                if (position > 0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setUni_no(id_uni.getSelectedItemPosition());
                            membership.setStudentMemberUniversity(id_uni.getSelectedItem().toString());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mmc_reg_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        membership.setApplicantMMCRegistrationNo(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mmc_tpc_reg_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        membership.setTpcRegistrationNo(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        date_pik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (!(s.toString().equals("") || s.toString().isEmpty())) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setDateOfRegistrationMMC(s.toString());
                        }
                    });
                    date_pik.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void navigate() {
        next = (Button) rootView.findViewById(R.id.next_button1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
                mem_interface.onFragmentNav(FragInterface.MembershipToNav.NEXT);
            }
        });

        back = (Button) rootView.findViewById(R.id.back_button1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mem_interface.onFragmentNav(FragInterface.MembershipToNav.BACK);
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_pik.setText(sdf.format(myCalendar.getTime()));
    }

    private void showActionSnackBarMessage(String message) {
        View view = rootView.findViewById(R.id.parent_layout);
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setMaxLines(2);
    }

    public void validation() {
        if (membership.getMain_category().equals("Student")) {
            if (id_uni.getSelectedItemPosition() > 0) {
                error = true;
            } else {
                error = false;
                if (membership.getValidation_pos() == -1) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setValidation_pos(PagerViewPager.getPos());
                        }
                    });
                }
            }
            if (membership.getDateOfRegistrationMMC().trim().equalsIgnoreCase("")) {
                error1 = false;
                if (membership.getValidation_pos() == -1) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setValidation_pos(PagerViewPager.getPos());
                        }
                    });
                }
            } else {
                error1 = true;
                date_pik.setError(null);
            }
        } else { //MMC
            if (mmc_reg_no.getText().toString().trim().equalsIgnoreCase("")) {
                error = false;
                if (membership.getValidation_pos() == -1) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setValidation_pos(PagerViewPager.getPos());
                        }
                    });
                }
            } else {
                error = true;
                mmc_reg_no.setError(null);
            }
            if (membership.getTpcRegistrationNo().equals("")) {
                error = false;
                if (membership.getValidation_pos() == -1) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setValidation_pos(PagerViewPager.getPos());
                        }
                    });
                }
            } else {
                error = true;
                mmc_tpc_reg_no.setError(null);
            }
            if (membership.getDateOfRegistrationMMC().trim().equalsIgnoreCase("")) {
                error1 = false;
                if (membership.getValidation_pos() == -1) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setValidation_pos(PagerViewPager.getPos());
                        }
                    });
                }
            } else {
                error1 = true;
                date_pik.setError(null);
            }
        }
    }

    private void loadItems() {
        if (membership.getMain_category().equals("Student")) {
            if (id_uni.getSelectedItemPosition() > 0) {
                error = true;
            } else {
                error = false;
                showActionSnackBarMessage(getString(R.string.error_id_type));
            }
            if (membership.getDateOfRegistrationMMC().trim().equalsIgnoreCase("")) {
                error1 = false;
                date_pik.setError("Enter Date Of Registration");
            } else {
                error1 = true;
                date_pik.setError(null);
            }
            if (membership.getMmc_certificate() == null || membership.getMmc_certificate().length == 0) {
                error2 = false;
                showActionSnackBarMessage(getString(R.string.error_image_mmc));
            } else {
                error2 = true;
            }
        } else { //MMC
            if (mmc_reg_no.getText().toString().trim().equalsIgnoreCase("")) {
                error = false;
                mmc_reg_no.setError("Enter MMC Registration No.");
            } else {
                error = true;
                mmc_reg_no.setError(null);
            }
            if (membership.getTpcRegistrationNo().trim().equalsIgnoreCase("")) {
                error = false;
                mmc_tpc_reg_no.setError("Enter MMC TPC Registration No.");
            } else {
                error = true;
                mmc_tpc_reg_no.setError(null);
            }
            if (membership.getDateOfRegistrationMMC().trim().equalsIgnoreCase("")) {
                error1 = false;
                date_pik.setError("Enter Date Of Registration");
            } else {
                error1 = true;
                date_pik.setError(null);
            }
            if (membership.getMmc_certificate() == null || membership.getMmc_certificate().length == 0) {
                error2 = false;
                showActionSnackBarMessage(getString(R.string.error_image_mmc));
            } else {
                error2 = true;
            }
        }

    }

    private void attachImage() {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            checkCameraPermission();
        } else {
            selectImageIfHaveCameraPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case HosVeliConstants.MY_PERMISSIONS_REQUEST_READ_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    selectImageIfHaveCameraPermission();
                } else {
                    selectImageIfNoCameraPermission();
                }
                return;
            }
        }
    }

    private void checkCameraPermission() {

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    HosVeliConstants.MY_PERMISSIONS_REQUEST_READ_CAMERA);
        } else {
            selectImageIfHaveCameraPermission();
        }

    }

    private void selectImageIfHaveCameraPermission() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.ThemeDialogCustom);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String imageName = getString(R.string.app_name) + String.valueOf(System
                            .currentTimeMillis()) + ".jpg";
                    imageFile = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), imageName);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                    startActivityForResult(intent, CAPTURE_IMAGE);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, ATTACH_IMAGE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImageIfNoCameraPermission() {

        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.ThemeDialogCustom);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, ATTACH_IMAGE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == ATTACH_IMAGE) {
                if (data == null)
                    return;
                try {
                    Uri selectedImage = data.getData();
                    String filePath = getFileNameFromUri(selectedImage);
                    File imageFile = new File(filePath);
                    Bitmap bitmap = decodeSampledBitmapFromResource(false, imageFile, 800, 800);
                    bitmap = rotateImageIfRequired(bitmap, imageFile.getAbsolutePath());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    // NFCInfoBO.INSTANCE.receiptImageName = imageFile.getName();
                    final byte[] imageByteArray = stream.toByteArray();
                    //NFCInfoBO.INSTANCE.receiptOS = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if (image_title.getText().toString().equalsIgnoreCase("Upload MMC Certificate")) {
                                membership.setMmc_certificate(imageByteArray);
                                membership.setApplicantMMCRegistrationCopy(Base64.encodeToString(imageByteArray, Base64.NO_WRAP));
                            } else {
                                membership.setUni_student_card(imageByteArray);
                                membership.setStudentMemberUniversityLetter(Base64.encodeToString(imageByteArray, Base64.NO_WRAP));
                            }
                        }
                    });
                    mmc_certificate.setImageBitmap(getRoundCornerBitmap(bitmap, 40));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAPTURE_IMAGE) {
                try {
                    Bitmap bitmap = getPic();//decodeSampledBitmapFromResource(true, imageFile, 800, 800);
                    bitmap = rotateImageIfRequired(bitmap, imageFile.getAbsolutePath());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    //NFCInfoBO.INSTANCE.receiptImageName = imageFile.getName();
                    final byte[] imageByteArray = stream.toByteArray();
                    // NFCInfoBO.INSTANCE.receiptOS = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if (image_title.getText().toString().equalsIgnoreCase("Upload MMC Certificate")) {
                                membership.setMmc_certificate(imageByteArray);
                                membership.setApplicantMMCRegistrationCopy(Base64.encodeToString(imageByteArray, Base64.NO_WRAP));
                            } else {
                                membership.setUni_student_card(imageByteArray);
                                membership.setStudentMemberUniversityLetter(Base64.encodeToString(imageByteArray, Base64.NO_WRAP));
                            }
                        }
                    });
                    mmc_certificate.setImageBitmap(getRoundCornerBitmap(bitmap, 40));
                    galleryAddPic(imageFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap getPic() {
        // Get the dimensions of the View
        int targetW = 500;
        int targetH = 500;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        return bitmap;
    }

    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public Bitmap decodeSampledBitmapFromResource(boolean isCaptured, File imageFile, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
        btmapOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageFile.getAbsolutePath(),
                btmapOptions);

        // Calculate inSampleSize
        btmapOptions.inSampleSize = calculateInSampleSize(btmapOptions, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        btmapOptions.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),
                btmapOptions);

        return bitmap;
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap rotateImageIfRequired(Bitmap img, String selectedImage) throws IOException {
        ExifInterface exif = new ExifInterface(selectedImage);
        int orientation = Integer.valueOf(exif.getAttribute(ExifInterface.TAG_ORIENTATION));
        // int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String file_path = null;
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                //Uri filePathUri = Uri.parse(cursor.getString(column_index));
                file_path = cursor.getString(column_index);//filePathUri.getPath();
                return file_path;
            }
        }

        return uri.getPath();
    }
}
