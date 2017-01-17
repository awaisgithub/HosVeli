package com.od.mma.Membership;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.od.mma.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by awais on 11/01/2017.
 */

public class PostgradDegreeFrag extends Fragment implements FragPostGradInterface {
    private View rootView;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    Context context1;
    FloatingActionButton add;
    TextView add_info;
    Button next, back;
    FragInterface mem_interface;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_membership_qualification_postgraduate, container, false);

        add_info = (TextView) rootView.findViewById(R.id.err_no_degree);
        add = (FloatingActionButton) rootView.findViewById(R.id.fab);
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

        prepareListData();

        listeners();
        navigate();
        return rootView;
    }

    private void listeners() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PagerViewPager.membership.getPos_count() < 3) {
                    PostgradDegreeDialogFrag dialog = PostgradDegreeDialogFrag.newInstance();
                    dialog.show(getActivity().getFragmentManager(), "fragmentDialog");
                    dialog.context_interface(PostgradDegreeFrag.this);
                } else {
                    showActionSnackBarMessage("You can add a maximum of 3 Degrees only.");
                }

            }
        });


        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }

    private void navigate() {
        next = (Button) rootView.findViewById(R.id.next_button1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void prepareListData() {
        if (PagerViewPager.membership.getPos_count() > 0) {
            add_info.setVisibility(View.GONE);
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            switch (PagerViewPager.membership.getPos_count()) {
                case 1:
                    listDataHeader.add(PagerViewPager.membership.getPos_degree());
                    List<String> child = new ArrayList<String>();
                    child.add(PagerViewPager.membership.getPos_uni());
                    child.add(PagerViewPager.membership.getPos_country());
                    child.add(PagerViewPager.membership.getPos_qof_date());
                    listDataChild.put(listDataHeader.get(0), child);

                    break;
                case 2:
                    listDataHeader.add(PagerViewPager.membership.getPos_degree());
                    List<String> child1 = new ArrayList<String>();
                    child1.add(PagerViewPager.membership.getPos_uni());
                    child1.add(PagerViewPager.membership.getPos_country());
                    child1.add(PagerViewPager.membership.getPos_qof_date());
                    listDataChild.put(listDataHeader.get(0), child1);

                    listDataHeader.add(PagerViewPager.membership.getPos_degree1());
                    List<String> child1_1 = new ArrayList<String>();
                    child1_1.add(PagerViewPager.membership.getPos_uni1());
                    child1_1.add(PagerViewPager.membership.getPos_country1());
                    child1_1.add(PagerViewPager.membership.getPos_qof_date1());
                    listDataChild.put(listDataHeader.get(1), child1_1);

                    break;
                case 3:
                    listDataHeader.add(PagerViewPager.membership.getPos_degree());
                    List<String> child2 = new ArrayList<String>();
                    child2.add(PagerViewPager.membership.getPos_uni());
                    child2.add(PagerViewPager.membership.getPos_country());
                    child2.add(PagerViewPager.membership.getPos_qof_date());
                    listDataChild.put(listDataHeader.get(0), child2);

                    listDataHeader.add(PagerViewPager.membership.getPos_degree1());
                    List<String> child2_1 = new ArrayList<String>();
                    child2_1.add(PagerViewPager.membership.getPos_uni1());
                    child2_1.add(PagerViewPager.membership.getPos_country1());
                    child2_1.add(PagerViewPager.membership.getPos_qof_date1());
                    listDataChild.put(listDataHeader.get(1), child2_1);

                    listDataHeader.add(PagerViewPager.membership.getPos_degree2());
                    List<String> child2_2 = new ArrayList<String>();
                    child2_2.add(PagerViewPager.membership.getPos_uni2());
                    child2_2.add(PagerViewPager.membership.getPos_country2());
                    child2_2.add(PagerViewPager.membership.getPos_qof_date2());
                    listDataChild.put(listDataHeader.get(2), child2_2);

                    break;
                default:
            }

        } else {
            add_info.setVisibility(View.VISIBLE);
        }


        listAdapter = new ExpandableListAdapter(context1, listDataHeader, listDataChild, expListView, this);
        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context1 = context;
        mem_interface = (FragInterface) context;
    }

    public static PostgradDegreeFrag newInstance(String text) {

        PostgradDegreeFrag f = new PostgradDegreeFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    private void prepareNewListData() {
        listDataHeader.clear();
        listDataHeader = new ArrayList<String>();
        listDataChild.clear();
        listDataChild = new HashMap<String, List<String>>();


        if (PagerViewPager.membership.getPos_count() > 0) {
            add_info.setVisibility(View.GONE);
            expListView.setVisibility(View.VISIBLE);

            switch (PagerViewPager.membership.getPos_count()) {
                case 1:
                    listDataHeader.add(PagerViewPager.membership.getPos_degree());
                    List<String> child = new ArrayList<String>();
                    child.add(PagerViewPager.membership.getPos_uni());
                    child.add(PagerViewPager.membership.getPos_country());
                    child.add(PagerViewPager.membership.getPos_qof_date());
                    listDataChild.put(listDataHeader.get(0), child);

                    break;
                case 2:
                    listDataHeader.add(PagerViewPager.membership.getPos_degree());
                    List<String> child1 = new ArrayList<String>();
                    child1.add(PagerViewPager.membership.getPos_uni());
                    child1.add(PagerViewPager.membership.getPos_country());
                    child1.add(PagerViewPager.membership.getPos_qof_date());
                    listDataChild.put(listDataHeader.get(0), child1);

                    listDataHeader.add(PagerViewPager.membership.getPos_degree1());
                    List<String> child1_1 = new ArrayList<String>();
                    child1_1.add(PagerViewPager.membership.getPos_uni1());
                    child1_1.add(PagerViewPager.membership.getPos_country1());
                    child1_1.add(PagerViewPager.membership.getPos_qof_date1());
                    listDataChild.put(listDataHeader.get(1), child1_1);

                    break;
                case 3:
                    listDataHeader.add(PagerViewPager.membership.getPos_degree());
                    List<String> child2 = new ArrayList<String>();
                    child2.add(PagerViewPager.membership.getPos_uni());
                    child2.add(PagerViewPager.membership.getPos_country());
                    child2.add(PagerViewPager.membership.getPos_qof_date());
                    listDataChild.put(listDataHeader.get(0), child2);

                    listDataHeader.add(PagerViewPager.membership.getPos_degree1());
                    List<String> child2_1 = new ArrayList<String>();
                    child2_1.add(PagerViewPager.membership.getPos_uni1());
                    child2_1.add(PagerViewPager.membership.getPos_country1());
                    child2_1.add(PagerViewPager.membership.getPos_qof_date1());
                    listDataChild.put(listDataHeader.get(1), child2_1);

                    listDataHeader.add(PagerViewPager.membership.getPos_degree2());
                    List<String> child2_2 = new ArrayList<String>();
                    child2_2.add(PagerViewPager.membership.getPos_uni2());
                    child2_2.add(PagerViewPager.membership.getPos_country2());
                    child2_2.add(PagerViewPager.membership.getPos_qof_date2());
                    listDataChild.put(listDataHeader.get(2), child2_2);

                    break;
                default:
            }

        } else {
            add_info.setVisibility(View.VISIBLE);
            expListView.setVisibility(View.INVISIBLE);
        }


        //  listAdapter.notifyDataSetChanged();
        listAdapter = new ExpandableListAdapter(context1, listDataHeader, listDataChild, expListView, this);
//        listAdapter.notifyDataSetChanged();
        // setting list adapter
//        listAdapter.notifyDataSetChanged();
        expListView.setAdapter(listAdapter);
    }

    public void showActionSnackBarMessage(String message) {
        View view = rootView.findViewById(R.id.parent_layout_posfrag);
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setMaxLines(2);
        snackbar.show();
    }

    @Override
    public void Added() {
        prepareNewListData();
    }

    @Override
    public void ShowError() {
        add_info.setVisibility(View.VISIBLE);
        expListView.setVisibility(View.INVISIBLE);
    }
}
