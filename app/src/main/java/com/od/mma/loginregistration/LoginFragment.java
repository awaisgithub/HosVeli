package com.od.mma.loginregistration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.od.mma.API.Api;
import com.od.mma.BOs.User;
import com.od.mma.CallBack.LoginCallBack;
import com.od.mma.CallBack.StatusCallBack;
import com.od.mma.MMAApplication;
import com.od.mma.R;
import com.od.mma.Utils.MMAConstants;
import com.od.mma.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;

/**
 * Created by awais on 16/01/2017.
 */

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
    private TextView textView;

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

    public static void forgotPassword(String email, final String url, final StatusCallBack callBack) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        JsonObjectRequest forgotPass = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.failure(error.toString());
            }
        });
        MMAApplication.getInstance().addToRequestQueue(forgotPass);
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

        textView = (TextView) rootView.findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(username.getText().toString())) {
                    showActionSnackBarMessage(getString(R.string.login_error_username));
                } else {
                        User.forgotPassword(Api.urlForgotPassword(username.getText().toString()), getActivity(), new StatusCallBack() {
                            @Override
                            public void success(JSONObject response) {
                                String forgot_status = response.optString("status");
                                String forgot_error = response.optString("error");

                                if (forgot_status.contains("SUCCEEDED")) {
                                    showActionSnackBarMessage("An email with link to reset password has been sent to the above email.");
                                }
                                if (forgot_error.contains("Sorry email not found")) {
                                    showActionSnackBarMessage("No account with that email address exist. Please sign up as a new user.");
                                } else if (forgot_error.contains("Error sending email")) {
                                    showActionSnackBarMessage("Error sending email. Please try again.");
                                }
                            }

                            @Override
                            public void failure(String response) {
                                showActionSnackBarMessage(getString(R.string.login_server_error));

                            }
                        });
                }
            }
        });


        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if (TextUtils.isEmpty(username.getText().toString())) {
                    showActionSnackBarMessage(getString(R.string.login_error_username));
                    return true;
                }

                if (TextUtils.isEmpty(password.getText().toString())) {
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
        User.performLogin(Api.urlLogin(email, password), getActivity(), new LoginCallBack() {
            @Override
            public void fetchDidSucceed(JSONObject response) {
                User user = realm.where(User.class).equalTo("id", email).findFirst();
                //  User user1 = realm.where(User.class).equalTo("id", email).findFirst();

                realm.beginTransaction();
                String login_status = response.optString("loginStatus");
                if (login_status.contains("SUCCEEDED")) {

                    if (user != null) {
                        Log.i(MMAConstants.TAG_MMA, "LOGIN usre!=null if(1) = ");
                        user.setTemp(false);
                        user.setSyncedLocal(true);

                        mListener.onFragmentNav(LoginFragment.this, Util.Navigate.LOGIN);

                    } else {
                        Log.i(MMAConstants.TAG_MMA, "LOGIN usre!=null else(1) = ");
                        User new_user = new User();
                        new_user.setId(email);
                        new_user.setTemp(false);
                        new_user.setSyncedLocal(true);


                        realm.copyToRealmOrUpdate(new_user);

                        mListener.onFragmentNav(LoginFragment.this, Util.Navigate.LOGIN);
                    }
                } else if (login_status.contains("USERNAME_IS_INCORRECT")) {
                    showActionSnackBarMessage(getString(R.string.login_not_exist));
                } else if (login_status.contains("PASSWORD_IS_INCORRECT")) {
                    showActionSnackBarMessage(getString(R.string.login_not_exist_wrong_pass));
                } else if (login_status.contains("error")) {
                    showActionSnackBarMessage(getString(R.string.login_error_unknown_server));
                }
                realm.commitTransaction();
                hideProgressDialog();
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
