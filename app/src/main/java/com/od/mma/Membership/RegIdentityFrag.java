package com.od.mma.Membership;

import android.Manifest;
import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.od.mma.R;
import com.od.mma.Utils.MMAConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;

/**
 * Created by awais on 29/12/2016.
 */

public class RegIdentityFrag extends Fragment {
    private static int CAPTURE_IMAGE = 1;
    private static int ATTACH_IMAGE = 2;
    Button next, back;
    Spinner id_type;
    Spinner nationality;
    GridView race;
    ArrayList<String> race_data = new ArrayList<>();
    ArrayAdapter<String> race_adapter;
    GridView religion;
    ArrayList<String> religion_data = new ArrayList<>();
    ArrayAdapter<String> religion_adapter;
    EditText id_hint;
    EditText Other;
    EditText Others;
    boolean error = false;
    boolean error1 = false;
    boolean error2 = false;
    boolean error3 = false;
    boolean error4 = false;
    boolean error5 = false;
    TextView race_text;
    TextView religion_text;
    ImageView imageView;
    FragInterface mem_interface;
    private View rootView;
    private File imageFile;

    public static RegIdentityFrag newInstance(String text) {

        RegIdentityFrag f = new RegIdentityFrag();
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
        rootView = inflater.inflate(R.layout.fragment_membership_registration_identity, container, false);
        initView();

        return rootView;
    }

    private void initView() {
        race_text = (TextView) rootView.findViewById(R.id.race1);
        religion_text = (TextView) rootView.findViewById(R.id.religion1);
        Other = (EditText) rootView.findViewById(R.id.Other);
        Other.setVisibility(View.GONE);
        Others = (EditText) rootView.findViewById(R.id.Others);
        Others.setVisibility(View.GONE);
        String imageName = getString(R.string.app_name) + String.valueOf(System
                .currentTimeMillis()) + ".jpg";
        imageFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), imageName);
        imageView = (ImageView) rootView.findViewById(R.id.user_profile_nric);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachImage();
            }
        });
        id_hint = (EditText) rootView.findViewById(R.id.id_no);
        id_type = (Spinner) rootView.findViewById(R.id.id_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_type, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        id_type.setPrompt("Select ID");
        id_type.setAdapter(
                new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag4_1,
                        getActivity()));
        nationality = (Spinner) rootView.findViewById(R.id.nationality);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.id_nationality, R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        nationality.setPrompt("Select Nationality");
        nationality.setAdapter(
                new NoneSelectSpinnerAdapter(adapter1, R.layout.spinner_hint_frag4_2,// R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getActivity()));
        race = (GridView) rootView.findViewById(R.id.race);
        race_data = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.race)));
        race.setColumnWidth(60);
        race.setHorizontalSpacing(80);
        race_adapter = new ArrayAdapter<String>(getActivity(), R.layout.grid_item, race_data);
        race.setAdapter(race_adapter);

        religion = (GridView) rootView.findViewById(R.id.religion);
        religion_data = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.religion)));
        religion.setColumnWidth(60);
        religion.setHorizontalSpacing(80);
        religion_adapter = new ArrayAdapter<String>(getActivity(), R.layout.grid_item, religion_data);
        religion.setAdapter(religion_adapter);


        if (PagerViewPager.membership.getId_type() != -1) {
            id_type.setSelection(PagerViewPager.membership.getId_type());
        }
        if (!(PagerViewPager.membership.getId_no().equals("") || PagerViewPager.membership.getId_no() == null || PagerViewPager.membership.getId_no().isEmpty())) {
            id_hint.setText(PagerViewPager.membership.getId_no());
        }
        if (!(PagerViewPager.membership.getNric_pic() == null || PagerViewPager.membership.getNric_pic().length == 0)) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(PagerViewPager.membership.getNric_pic(), 0, PagerViewPager.membership.getNric_pic().length);
            imageView.setImageBitmap(getRoundCornerBitmap(bitmap, 40));
        }
        if (PagerViewPager.membership.getNationality() != -1) {
            nationality.setSelection(PagerViewPager.membership.getNationality());
        }
        if (PagerViewPager.membership.getRace() != -1) {
            race.setItemChecked(PagerViewPager.membership.getRace(), true);
            if (PagerViewPager.membership.getRace() == 3) {
                Other.setVisibility(View.VISIBLE);
                Other.setText(PagerViewPager.membership.getOther_race());
            }
        }
        if (PagerViewPager.membership.getReligion() != -1) {
            religion.setItemChecked(PagerViewPager.membership.getReligion(), true);
            if (PagerViewPager.membership.getReligion() == 3) {
                Others.setVisibility(View.VISIBLE);
                Others.setText(PagerViewPager.membership.getOther_religion());
            }
        }


        if (PagerViewPager.membership.isValidation()) {
            loadItems();
        }


        listeners();
        navigate();


    }

    private void listeners() {
        race.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                race_text.setError(null);
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setRace(position);
                    }
                });
                switch (position) {
                    case 0:
                        Other.setVisibility(View.GONE);
                        break;
                    case 1:
                        Other.setVisibility(View.GONE);
                        break;
                    case 2:
                        Other.setVisibility(View.GONE);
                        break;
                    case 3:
                        Other.setVisibility(View.VISIBLE);
                        break;
                    default:
                }
            }
        });

        religion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                religion_text.setError(null);
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setReligion(position);
                    }
                });
                switch (position) {
                    case 0:
                        Others.setVisibility(View.GONE);
                        break;
                    case 1:
                        Others.setVisibility(View.GONE);
                        break;
                    case 2:
                        Others.setVisibility(View.GONE);
                        break;
                    case 3:
                        Others.setVisibility(View.VISIBLE);
                        break;
                    default:
                }
            }
        });


        id_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    id_hint.setHint(id_type.getSelectedItem().toString() + " No.");
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setId_type(id_type.getSelectedItemPosition());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        id_hint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setId_no(s.toString());
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Other.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setOther_race(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Others.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setOther_religion(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setNationality(nationality.getSelectedItemPosition());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mem_interface = (FragInterface) context;
    }

    public void validation() {
        if (id_type.getSelectedItemPosition() > 0) {
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
        if (id_hint.getText().toString().trim().equalsIgnoreCase("")) {
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
            id_hint.setError(null);
        }
        if (nationality.getSelectedItemPosition() > 0) {
            error3 = true;
        } else {
            error3 = false;
            if (PagerViewPager.membership.getValidation_pos() == -1) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        }
        if (PagerViewPager.membership.getRace() == -1) {
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
            if (PagerViewPager.membership.getRace() == 3) {
                if (Other.getText().toString().equals("") || Other.getText().toString().isEmpty()) {
                    error4 = false;
                    if (PagerViewPager.membership.getValidation_pos() == -1) {
                        PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                            }
                        });
                    }
                } else
                    error4 = true;

            } else
                error4 = true;
        }
        if (PagerViewPager.membership.getReligion() == -1) {
            error5 = false;
            if (PagerViewPager.membership.getValidation_pos() == -1) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        } else {
            if (PagerViewPager.membership.getReligion() == 3) {
                if (Others.getText().toString().equals("") || Others.getText().toString().isEmpty()) {
                    error5 = false;
                    if (PagerViewPager.membership.getValidation_pos() == -1) {
                        PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                            }
                        });
                    }
                } else
                    error5 = true;

            } else
                error5 = true;
        }
    }

    private void loadItems() {
        if (id_type.getSelectedItemPosition() > 0) {
            error = true;
        } else {
            error = false;
            showActionSnackBarMessage(getString(R.string.error_id_type));
        }
        if (id_hint.getText().toString().trim().equalsIgnoreCase("")) {
            error1 = false;
            id_hint.setError("Enter ID No.");
        } else {
            error1 = true;
            id_hint.setError(null);
        }
        if (nationality.getSelectedItemPosition() > 0) {
            error3 = true;
        } else {
            error3 = false;
            showActionSnackBarMessage(getString(R.string.error_nationality));
        }
        if (PagerViewPager.membership.getRace() == -1) {
            error4 = false;
            race_text.setError(getString(R.string.error_race));
        } else {
            if (PagerViewPager.membership.getRace() == 3) {
                if (Other.getText().toString().equals("") || Other.getText().toString().isEmpty()) {
                    error4 = false;
                    race_text.setError(getString(R.string.error_race));
                } else
                    error4 = true;

            } else
                error4 = true;
        }
        if (PagerViewPager.membership.getReligion() == -1) {
            error5 = false;
            religion_text.setError(getString(R.string.error_religion));
        } else {
            if (PagerViewPager.membership.getReligion() == 3) {
                if (Others.getText().toString().equals("") || Others.getText().toString().isEmpty()) {
                    error5 = false;
                    religion_text.setError(getString(R.string.error_religion));
                } else
                    error5 = true;

            } else
                error5 = true;
        }

    }

    private void showActionSnackBarMessage(String message) {
        View view = rootView.findViewById(R.id.parent_layout);
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setMaxLines(2);
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
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setNric_pic(imageByteArray);
                        }
                    });
                    imageView.setImageBitmap(getRoundCornerBitmap(bitmap, 40));

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
                            PagerViewPager.membership.setNric_pic(imageByteArray);
                        }
                    });
                    imageView.setImageBitmap(getRoundCornerBitmap(bitmap, 40));
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
