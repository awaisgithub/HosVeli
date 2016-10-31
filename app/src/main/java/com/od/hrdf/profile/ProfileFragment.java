package com.od.hrdf.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.od.hrdf.BOs.User;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.Utils.Util;
import com.od.hrdf.event.EventListFragment;
import com.od.hrdf.landingtab.TabFragActivityInterface;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;

import static com.od.hrdf.event.EventFragment.LIST_TYPE_FAV_EVENTS;
import static com.od.hrdf.event.EventFragment.LIST_TYPE_MY_EVENTS;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static int CAPTURE_IMAGE = 1;
    private static int ATTACH_IMAGE = 2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View rootView;
    private ViewPager mViewPager;
    private User user;
    private Realm realm;
    private File imageFile;
    private CircleImageView profileImageView;
    private TabFragActivityInterface mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_logout, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mListener.onFragmentNav(ProfileFragment.this, Util.Navigate.LOGOUT);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        user = User.getCurrentUser(realm);
        initViews();
    }

    private void initViews() {

        profileImageView = (CircleImageView) rootView.findViewById(R.id.user_profile_image);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachImage();
            }
        });

        TextView userName = (TextView) rootView.findViewById(R.id.user_profile_name);
        userName.setText(user.getName());

        TextView email = (TextView) rootView.findViewById(R.id.user_profile_email);
        email.setText(user.getId());

        final SegmentedGroup segmentedGroup = (SegmentedGroup) rootView.findViewById(R.id.profile_segment_control);
        segmentedGroup.setTintColor(ContextCompat.getColor(getActivity(), R.color.colorTabs), Color.WHITE);
        ((RadioButton) segmentedGroup.findViewById(R.id.qr_code)).setOnClickListener(this);
        ((RadioButton) segmentedGroup.findViewById(R.id.my_profile)).setOnClickListener(this);
        ((RadioButton) segmentedGroup.findViewById(R.id.my_events)).setOnClickListener(this);
        ((RadioButton) segmentedGroup.findViewById(R.id.fav_events)).setOnClickListener(this);
        mViewPager = (ViewPager) rootView.findViewById(R.id.profile_viewpager);
        //mViewPager.setOffscreenPageLimit(4);
        setupViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) segmentedGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ProfileQRFragment.newInstance("", ""), "One");
        adapter.addFragment(ProfileMyProfileFragment.newInstance("", ""), "Two");
        adapter.addFragment(EventListFragment.newInstance(LIST_TYPE_MY_EVENTS), "Upcoming");
        adapter.addFragment(EventListFragment.newInstance(LIST_TYPE_FAV_EVENTS), "Favourite Events");
        viewPager.setAdapter(adapter);
    }

    private void attachImage() {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            checkCameraPermission();
        } else {
            selectImageIfHaveCameraPermission();
        }
    }

    private void checkCameraPermission() {

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    HRDFConstants.MY_PERMISSIONS_REQUEST_READ_CAMERA);
        } else {
            selectImageIfHaveCameraPermission();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case HRDFConstants.MY_PERMISSIONS_REQUEST_READ_CAMERA: {
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

    // Attach image methods
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
                    Log.i("filepath", "filePath = " + filePath);
                    File imageFile = new File(filePath);
                    Bitmap bitmap = decodeSampledBitmapFromResource(false, imageFile, 800, 800);
                    bitmap = rotateImageIfRequired(bitmap, imageFile.getAbsolutePath());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    // NFCInfoBO.INSTANCE.receiptImageName = imageFile.getName();
                    byte[] imageByteArray = stream.toByteArray();
                    //NFCInfoBO.INSTANCE.receiptOS = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
                    profileImageView.setImageBitmap(bitmap);
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
                    byte[] imageByteArray = stream.toByteArray();
                    // NFCInfoBO.INSTANCE.receiptOS = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
                    profileImageView.setImageBitmap(bitmap);
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
        Log.i("inSampleSize", "inSampleSize = " + btmapOptions.inSampleSize);

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
        Log.i("myAttribute", "orientation = " + orientation);
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

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @Override
    public void onClick(View view) {

        int viewId = view.getId();
        switch (viewId) {
            case R.id.qr_code:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.my_profile:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.my_events:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.fav_events:
                mViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TabFragActivityInterface) {
            mListener = (TabFragActivityInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener.onFragmentNav(ProfileFragment.this, Util.Navigate.LOGOUT);
        mListener = null;
    }

}
