package com.od.mma.Membership;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.od.mma.R;

import io.realm.Realm;

/**
 * Created by awais on 29/12/2016.
 */

public class ApplicationStatusFrag extends Fragment {
    private View rootView;
    Button submit, back;
    private ProgressDialog progressDialog;
    FragInterface mem_interface;

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

                        if(!PagerViewPager.membership.isValidation()) {
                            PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    PagerViewPager.membership.setValidation(true);
                                }
                            });
                        }

                        PagerViewPager.getPager().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                int pos = PagerViewPager.membership.getValidation_pos();
                                if(pos != -1) {
                                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            PagerViewPager.membership.setValidation_pos(-1);
                                        }
                                    });
                                    mem_interface.NavigateTo(pos);
                                }
                                else {
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

}
