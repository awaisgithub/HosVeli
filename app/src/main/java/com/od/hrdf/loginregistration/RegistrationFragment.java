package com.od.hrdf.loginregistration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.od.hrdf.API.Api;
import com.od.hrdf.BOs.User;
import com.od.hrdf.CallBack.CheckCallBack;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.CallBack.StatusCallBack;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.Utils.Util;

import org.json.JSONObject;
import org.w3c.dom.Text;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Realm realm;
    private User user;

    private LoginRegActivityInterface mListener;
    private View rootView;
    private EditText nameTV;
    private EditText emailTV;
    private EditText contactTV;
    private EditText nationalityTV;
    private EditText passwordTV;
    private EditText confirmPasswordTV;
    private ProgressDialog progressDialog;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
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
        rootView = inflater.inflate(R.layout.fragment_registration, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        user = new User();
        initViews();
    }

    private void initViews() {

        nameTV = (EditText) rootView.findViewById(R.id.name);
        emailTV = (EditText) rootView.findViewById(R.id.email);
        contactTV = (EditText) rootView.findViewById(R.id.contact_number);
        nationalityTV = (EditText) rootView.findViewById(R.id.nationality);
        passwordTV = (EditText) rootView.findViewById(R.id.password);
        confirmPasswordTV = (EditText) rootView.findViewById(R.id.confirm_password);
        Button submitButton = (Button) rootView.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                validateAndSubmitRegistration();
            }
        });

        progressDialog = new ProgressDialog(getActivity());
    }

    private void validateAndSubmitRegistration() {
        final String name = nameTV.getText().toString();
        final String email = emailTV.getText().toString();
        final String contact = contactTV.getText().toString();
        final String nationality = nationalityTV.getText().toString();
        final String password = passwordTV.getText().toString();
        final String confirmPassword = confirmPasswordTV.getText().toString();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                user.setId(email);
                user.setName(name);
                user.setContactNumber(contact);
                user.setNationality(nationality);
                user.setPassword(password);
                user.setConfirmPassword(confirmPassword);
            }
        });

        Pair<Boolean, String> resultPair = user.validate();
        if (!resultPair.first) {
            showActionSnackBarMessage(resultPair.second);
        } else {
            showProgressDialog(R.string.reg_checking_duplicate);
            checkDuplicateAndPerformReg();
        }

    }

    private void checkDuplicateAndPerformReg() {
        User.checkDuplicate(Api.urlUserList(user.getId()), new CheckCallBack() {
            @Override
            public void checkDuplicateUser(boolean isDuplicate) {
                hideProgressDialog();
                Log.i(HRDFConstants.TAG, "checkDuplicate =" + isDuplicate);
                if (isDuplicate) {
                    showActionSnackBarMessage(getString(R.string.reg_error_duplicate));
                } else {
                    showProgressDialog(R.string.reg_registering);
                    performReg();
                }
            }

            @Override
            public void checkFail(Exception e) {
                e.printStackTrace();
                Log.i(HRDFConstants.TAG, "checkDuplicate Exception=");
                hideProgressDialog();
                showActionSnackBarMessage(getActivity().getResources().getString(R.string.reg_error_unknown_server));
            }
        });
    }

    private void performReg() {
        user.performUserRegistration(user, Api.urlJogetCRUD(), new StatusCallBack() {
            @Override
            public void success(JSONObject response) {
                hideProgressDialog();
                if (response.has("status")) {
                    String status = response.optString("status");
                    if (status.equalsIgnoreCase("1")) {
                        user.setTemp(false);
                        user.setSynced(true);
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(user);
                        realm.commitTransaction();
                        onRegistrationSubmitDialog();
                    } else {
                        showActionSnackBarMessage(getActivity().getResources().getString(R.string.reg_error_unknown_server));
                    }
                } else {
                    showActionSnackBarMessage(getActivity().getResources().getString(R.string.reg_error_unknown_server));
                }
            }

            @Override
            public void failure(String response) {
                hideProgressDialog();
                showActionSnackBarMessage(getActivity().getResources().getString(R.string.reg_error_server_not_reached));
                Log.i(HRDFConstants.TAG, "performReg failure=" + response);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginRegActivityInterface) {
            mListener = (LoginRegActivityInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showActionSnackBarMessage(String message) {
        View view = rootView.findViewById(R.id.parent_layout);
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setMaxLines(2);
        textView.setGravity(Gravity.CENTER);
        snackbar.show();
    }

    private void showProgressDialog(int message) {
        progressDialog.setMessage(getResources().getString(message));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    private void onRegistrationSubmitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.ThemeDialogCustom);
        builder.setTitle(R.string.dialog_congrats_title);
        builder.setMessage(R.string.reg_registered_message);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mListener.onFragmentNav(RegistrationFragment.this, Util.Navigate.LOGIN);
            }
        });
        builder.show();
    }
}
