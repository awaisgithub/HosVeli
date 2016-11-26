package com.od.hrdf.abouts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.od.hrdf.API.Api;
import com.od.hrdf.BOs.Agenda;
import com.od.hrdf.BOs.Branches;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.event.agenda.AgendaListAdapter;
import com.od.hrdf.event.agenda.AgendsMainActivity;

import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.od.hrdf.HRDFApplication.realm;


public class OfficeListFragment extends Fragment implements BranchesListAdapter.BranchParentNotifier {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View rootView;
    private AboutUsParentNotifier mListener;
    public OfficeListFragment() {
        // Required empty public constructor
    }

    public static OfficeListFragment newInstance(String param1, String param2) {
        OfficeListFragment fragment = new OfficeListFragment();
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
        rootView = inflater.inflate(R.layout.fragment_offices, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        fetchBranches();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.branches_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        RealmResults realmResults = Branches.getBranches(realm);
        realmResults.addChangeListener(new RealmChangeListener<RealmResults>() {
            @Override
            public void onChange(RealmResults element) {
                if (element.size() > 0) {
                    hideMessage();
                } else {
                    showMessage(R.string.aboutus_no_branches);
                }
            }
        });

        if (realmResults.size() < 1) {
            showMessage(R.string.aboutus_loading_branches);
        }

        BranchesListAdapter branchListAdapter = new BranchesListAdapter(getActivity(), realmResults, this, true);
        recyclerView.setAdapter(branchListAdapter);
    }

    private void fetchBranches() {
        RealmQuery query = realm.where(Branches.class);
        Branches.fetchBranches(getActivity(), realm, Api.urlBranches(), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {

            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, "!!!!NO   ===   !!!!= " + e.toString());
                showMessage(R.string.aboutus_error_branches);
            }
        });

    }

    private void showMessage(int message) {
        RelativeLayout messageLayout = (RelativeLayout) rootView.findViewById(R.id.error_layout);
        TextView messageView = (TextView) rootView.findViewById(R.id.label);
        messageView.setText(message);
        messageLayout.setVisibility(View.VISIBLE);
    }

    private void hideMessage() {
        RelativeLayout messageLayout = (RelativeLayout) rootView.findViewById(R.id.error_layout);
        messageLayout.setVisibility(View.GONE);
    }

    @Override
    public void dialNumber(String number) {
        if(number.contains(",")) {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.aboutus_dialer_dialog_title)
                    .items(number.split(","))
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + text));
                            startActivity(intent);
                        }
                    })
                    .negativeText(R.string.button_cancel)
                    .show();
        } else {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        }
    }
}
