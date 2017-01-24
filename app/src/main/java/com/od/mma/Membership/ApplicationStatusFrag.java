package com.od.mma.Membership;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.od.mma.API.Api;
import com.od.mma.BOs.User;
import com.od.mma.CallBack.StatusCallBack;
import com.od.mma.R;
import com.od.mma.Utils.MMAConstants;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;

import static com.od.mma.MMAApplication.realm;

/**
 * Created by awais on 29/12/2016.
 */

public class ApplicationStatusFrag extends Fragment {
    private View rootView;
    Button submit, back;
    private ProgressDialog progressDialog;
    FragInterface mem_interface;
    Membership membership;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_registration_application_details, container, false);
        initView();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mem_interface = (FragInterface) context;
    }

    private void initView() {
        user = User.getCurrentUser(realm);
        membership = Membership.getCurrentRegistration(realm, User.getCurrentUser(realm).getId());
        progressDialog = new ProgressDialog(getActivity());
        submit = (Button) rootView.findViewById(R.id.next_button1);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(R.string.reg_validation);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        hideProgressDialog();

                        if (!membership.isValidation()) {
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    membership.setValidation(true);
                                }
                            });
                        }

                        PagerViewPager.getPager().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int pos = membership.getValidation_pos();
                                if (pos != -1 && !membership.isLoadFromServer()) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            membership.setValidation_pos(-1);
                                        }
                                    });
                                    mem_interface.NavigateTo(pos);
                                } else if (pos == -1 || membership.isLoadFromServer()) {
                                    //call the next activity afer membershipUpdation, as validation is correct and user is good to go ahead

                                    try {
                                        User.performMembershipUpdation(User.getCurrentUser(realm), Membership.getCurrentRegistration(realm, User.getCurrentUser(realm).getId()), Api.urlMembershipUpdate(), new StatusCallBack() {
                                            @Override
                                            public void success(JSONObject response) {
                                                String status = response.optString("status");
                                                if (status.equals("1")) {
                                                    showActionSnackBarMessage("Membership Updated");
                                                } else {
                                                    showActionSnackBarMessage("Membership Not Updated... Error");
                                                }
                                            }

                                            @Override
                                            public void failure(String response) {

                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.i(MMAConstants.TAG_MMA, "MEMBERSHIP JSON EXCEPTION ");
                                    }

                                }
                            }
                        }, 100);


                    }

                }, 1000);
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

    public static ApplicationStatusFrag newInstance(String text) {

        ApplicationStatusFrag f = new ApplicationStatusFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
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

}
