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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.od.mma.R;
import com.od.mma.Utils.MMAConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by awais on 29/12/2016.
 */

public class RegNameFrag extends Fragment {
    private static int CAPTURE_IMAGE = 1;
    private static int ATTACH_IMAGE = 2;
    Button next, back;
    EditText date_pik;
    EditText name;
    EditText email;
    Calendar myCalendar;
    Spinner title;
    RadioGroup gender;
    boolean error = false;
    boolean error1 = false;
    boolean error2 = false;
    boolean error3 = false;
    boolean error4 = false;
    boolean error5 = false;
    boolean error6 = false;
    RadioButton last;
    GridView marital_status;
    ArrayList<String> marital_status_data = new ArrayList<>();
    ArrayAdapter<String> marital_adapter;
    TextView marital;
    FragInterface mem_interface;
    private View rootView;
    private ImageView profileImageView;
    private File imageFile;

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

    public static RegNameFrag newInstance(String text) {

        RegNameFrag f = new RegNameFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
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
        rootView = inflater.inflate(R.layout.fragment_membership_registration_name, container, false);
        initView();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mem_interface = (FragInterface) context;
    }

    private void initView() {
        marital = (TextView) rootView.findViewById(R.id.marital);
        gender = (RadioGroup) rootView.findViewById(R.id.gender);
        last = (RadioButton) rootView.findViewById(R.id.radioButton1);
        name = (EditText) rootView.findViewById(R.id.name);
        email = (EditText) rootView.findViewById(R.id.email);
        date_pik = (EditText) rootView.findViewById(R.id.date_pick);
        title = (Spinner) rootView.findViewById(R.id.title1);
        marital_status = (GridView) rootView.findViewById(R.id.marital_status);
        marital_status_data = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.marital)));
        marital_status.setColumnWidth(60);
        marital_status.setHorizontalSpacing(80);
        marital_adapter = new ArrayAdapter<String>(getActivity(), R.layout.grid_item, marital_status_data);
        marital_status.setAdapter(marital_adapter);

        marital_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                marital.setError(null);
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setMarital_stat(position);
                    }
                });
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_title, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        title.setPrompt("Select Title");
        title.setAdapter(
                new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag3,
                        getActivity()));

        String imageName = getString(R.string.app_name) + String.valueOf(System
                .currentTimeMillis()) + ".jpg";
        imageFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), imageName);
        profileImageView = (ImageView) rootView.findViewById(R.id.user_profile_image);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachImage();
            }
        });

        if (PagerViewPager.membership.getTitle() != -1) {
            title.setSelection(PagerViewPager.membership.getTitle());
        }
        if (!(PagerViewPager.membership.getfName().equals("") || PagerViewPager.membership.getfName() == null || PagerViewPager.membership.getfName().isEmpty())) {
            name.setText(PagerViewPager.membership.getfName());
        }
        if (!(PagerViewPager.membership.getlName().equals("") || PagerViewPager.membership.getlName() == null || PagerViewPager.membership.getlName().isEmpty())) {
            email.setText(PagerViewPager.membership.getlName());
        }
        if (!(PagerViewPager.membership.getDob().equals("") || PagerViewPager.membership.getDob() == null || PagerViewPager.membership.getDob().isEmpty())) {
            date_pik.setText(PagerViewPager.membership.getDob());
        }
        if (PagerViewPager.membership.getGender() != -1) {
            final int id = PagerViewPager.membership.getGender();
            View radioButton = gender.findViewById(id);
            int radioId = gender.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) gender.getChildAt(radioId);
            btn.setChecked(true);
            last.setError(null);
        }
        if (!(PagerViewPager.membership.getPersonal_pic() == null || PagerViewPager.membership.getPersonal_pic().length == 0)) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(PagerViewPager.membership.getPersonal_pic(), 0, PagerViewPager.membership.getPersonal_pic().length);
            profileImageView.setImageBitmap(getRoundCornerBitmap(bitmap, 40));
        }
        if (PagerViewPager.membership.getMarital_stat() != -1) {
            marital_status.setItemChecked(PagerViewPager.membership.getMarital_stat(), true);
            marital.setError(null);
        }


        if (PagerViewPager.membership.isValidation()) {
            loadItems();
        }


        listeners();
        navigate();
    }

    private void listeners() {
        title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (title.getSelectedItemPosition() > 0) {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setTitle(title.getSelectedItemPosition());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setfName(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setlName(s.toString());
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
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setDob(s.toString());
                        }
                    });
                    date_pik.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (PagerViewPager.membership.isValidation())
                    last.setError(null);
                if (gender.getCheckedRadioButtonId() != -1) {
                    final int id = gender.getCheckedRadioButtonId();
                    View radioButton = gender.findViewById(id);
                    int radioId = gender.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) gender.getChildAt(radioId);

                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setGender(id);
                        }
                    });
                }
            }
        });


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

        date_pik.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

    public void validation() {
        if (title.getSelectedItemPosition() > 0) {
            error = true;
        } else {
            error = false;
            if (PagerViewPager.membership.getValidation_pos() == -1) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        }
        if (name.getText().toString().trim().equalsIgnoreCase("")) {
            error1 = false;
            if (PagerViewPager.membership.getValidation_pos() == -1) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        } else {
            error1 = true;
            name.setError(null);
        }
        if (email.getText().toString().trim().equalsIgnoreCase("")) {
            error2 = false;
            if (PagerViewPager.membership.getValidation_pos() == -1) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        } else {
            error2 = true;
            email.setError(null);
        }
        if (PagerViewPager.membership.getDob().trim().equalsIgnoreCase("")) {
            error3 = false;
            if (PagerViewPager.membership.getValidation_pos() == -1) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        } else {
            error3 = true;
            date_pik.setError(null);
        }
        if ((gender.getCheckedRadioButtonId() == -1)) {
            error4 = false;
            if (PagerViewPager.membership.getValidation_pos() == -1) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        } else {
            error4 = true;
            last.setError(null);
        }
        if (PagerViewPager.membership.getMarital_stat() == -1) {
            error6 = false;
            if (PagerViewPager.membership.getValidation_pos() == -1) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        } else {
            error6 = true;
        }
    }

    private void loadItems() {
        if (title.getSelectedItemPosition() > 0) {
            error = true;
        } else {
            error = false;
            showActionSnackBarMessage(getString(R.string.error_title));
        }
        if (name.getText().toString().trim().equalsIgnoreCase("")) {
            error1 = false;
            name.setError("Enter First Name");
        } else {
            error1 = true;
            name.setError(null);
        }
        if (email.getText().toString().trim().equalsIgnoreCase("")) {
            error2 = false;
            email.setError("Enter Email");
        } else {
            error2 = true;
            email.setError(null);
        }
        if (PagerViewPager.membership.getDob().trim().equalsIgnoreCase("")) {
            error3 = false;
            date_pik.setError("Enter Date Of Birth");
        } else {
            error3 = true;
            date_pik.setError(null);
        }
        if ((gender.getCheckedRadioButtonId() == -1)) {
            error4 = false;
            last.setError("Select Gender");
        } else {
            error4 = true;
            last.setError(null);
        }
        if (PagerViewPager.membership.getMarital_stat() == -1) {
            error6 = false;
            marital.setError("Please Select Marital Status.");
        } else {
            error6 = true;
        }

    }

    private void showActionSnackBarMessage(String message) {
        View view = rootView.findViewById(R.id.parent_layout);
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setMaxLines(2);
        snackbar.show();
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date_pik.setText(sdf.format(myCalendar.getTime()));
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
            case MMAConstants.MY_PERMISSIONS_REQUEST_READ_CAMERA: {
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
                    MMAConstants.MY_PERMISSIONS_REQUEST_READ_CAMERA);
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
                    final Uri selectedImage = data.getData();
                    final String filePath = getFileNameFromUri(selectedImage);
                    File imageFile = new File(filePath);
                    Bitmap bitmap = decodeSampledBitmapFromResource(false, imageFile, 800, 800);
                    bitmap = rotateImageIfRequired(bitmap, imageFile.getAbsolutePath());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    // NFCInfoBO.INSTANCE.receiptImageName = imageFile.getName();
                    final byte[] imageByteArray = stream.toByteArray();
                    //NFCInfoBO.INSTANCE.receiptOS = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
                    //
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setPersonal_pic(imageByteArray);
                        }
                    });
                    profileImageView.setImageBitmap(getRoundCornerBitmap(bitmap, 40));

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

                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setPersonal_pic(imageByteArray);
                        }
                    });
                    profileImageView.setImageBitmap(getRoundCornerBitmap(bitmap, 40));

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
