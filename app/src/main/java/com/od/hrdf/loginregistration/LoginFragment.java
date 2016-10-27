package com.od.hrdf.loginregistration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.od.hrdf.API.Api;
import com.od.hrdf.BOs.User;
import com.od.hrdf.CallBack.LoginCallBack;
import com.od.hrdf.R;
import com.od.hrdf.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;

import io.realm.Realm;
import io.realm.RealmQuery;

public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View rootView;
    private LoginRegActivityInterface mListener;
    private EditText username;
    private EditText password;
    private Realm realm;
    private ProgressDialog progressDialog;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        initViews();
    }

    private void initViews() {

        username = (EditText) rootView.findViewById(R.id.username);
        password = (EditText) rootView.findViewById(R.id.password);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if(TextUtils.isEmpty(username.getText().toString())) {
                    showActionSnackBarMessage(getString(R.string.login_error_username));
                    return true;
                }

                if(TextUtils.isEmpty(password.getText().toString())) {
                    showActionSnackBarMessage(getString(R.string.reg_error_password));
                    return true;
                }

                showProgressDialog(R.string.login_authenticating);
                performLogin(username.getText().toString(), password.getText().toString());
                return true;

            }
        });
        Button signupButton = (Button) rootView.findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentNav(LoginFragment.this, Util.Navigate.SIGNUP);
            }
        });

        progressDialog = new ProgressDialog(getActivity());
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

    private void performLogin(final String email, final String password) {
        RealmQuery query = realm.where(User.class).equalTo("id", email);
        User.fetchUser(getActivity(), realm, Api.urlUserList(email), query, new LoginCallBack() {
            @Override
            public void fetchDidSucceed(String response) {
                hideProgressDialog();
                if (response != null && response.length() > 0) {
                    realm.beginTransaction();
                    try {
                        JSONArray array = new JSONArray(response);
                        realm.createOrUpdateObjectFromJson(User.class, array.getJSONObject(0));
                        User user = realm.where(User.class).endsWith("id", email).findFirst();
                        if (user != null && user.getPassword().equalsIgnoreCase(password)) {
                            user.setTemp(false);
                            user.setSyncedLocal(true);
                            mListener.onFragmentNav(LoginFragment.this, Util.Navigate.LOGIN);
                        } else {
                            user.setTemp(true);
                            user.setSyncedLocal(false);
                            showActionSnackBarMessage(getString(R.string.login_cred_error));
                        }
                    } catch (JSONException e) {
                        showActionSnackBarMessage(getString(R.string.reg_error_unknown_server));
                        e.printStackTrace();
                    }
                    realm.commitTransaction();
                } else {
                    showActionSnackBarMessage(getString(R.string.login_not_exist));
                }
            }

            @Override
            public void fetchDidFail(Exception e) {
                hideProgressDialog();
                showActionSnackBarMessage(getString(R.string.login_server_error));
            }
        });
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
}
