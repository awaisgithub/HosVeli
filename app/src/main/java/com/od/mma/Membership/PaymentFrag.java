package com.od.mma.Membership;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;

import com.od.mma.R;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;

/**
 * Created by awais on 29/12/2016.
 */

public class PaymentFrag extends Fragment {
    Spinner subs;
    Button next, back;
    GridView payment_method;
    ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;
    FragInterface mem_interface;
    private View rootView;

    public static PaymentFrag newInstance(String text) {
        PaymentFrag f = new PaymentFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_registration_payment, container, false);
        initView();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mem_interface = (FragInterface) context;
    }

    private void initView() {
        payment_method = (GridView) rootView.findViewById(R.id.payment);
        data = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.payment_method)));
        payment_method.setColumnWidth(60);
        payment_method.setHorizontalSpacing(80);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.grid_item, data);
        payment_method.setAdapter(adapter);

        subs = (Spinner) rootView.findViewById(R.id.id_sub);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_year, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        subs.setPrompt("Select Subscription Years For");

        subs.setAdapter(
                new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag13,
                        getActivity()));


        if (PagerViewPager.membership.getPayment_method() != -1) {
            payment_method.setItemChecked(PagerViewPager.membership.getPayment_method(), true);
        }
        if (PagerViewPager.membership.getPayment_sub_year() != -1) {
            subs.setSelection(PagerViewPager.membership.getPayment_sub_year());
        }


        if (PagerViewPager.membership.isValidation()) {
            loadItems();
        }
        listeners();
        navigate();
    }

    private void listeners() {
        payment_method.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setPayment_method(position);
                    }
                });
            }
        });
        subs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (subs.getSelectedItemPosition() > 0) {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setPayment_sub_year(subs.getSelectedItemPosition());
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

    public void validation() {
    }

    private void loadItems() {
    }
}
