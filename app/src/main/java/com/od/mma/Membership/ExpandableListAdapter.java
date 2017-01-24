package com.od.mma.Membership;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.od.mma.BOs.User;
import com.od.mma.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

import static com.od.mma.MMAApplication.realm;

/**
 * Created by awais on 11/01/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ExpandableListView exp;
    ImageButton delete;
    FragPostGradInterface mem;
    Membership membership;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, ExpandableListView exp, PostgradDegreeFrag pos) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.exp = exp;
        mem = (FragPostGradInterface) pos;
        membership = Membership.getCurrentRegistration(realm, User.getCurrentUser(realm).getId());
    }

    public ExpandableListAdapter() {
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        //
        exp.setDividerHeight(0);
        //
        if (isLastChild)
            exp.setDividerHeight(20);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }


        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        //
        exp.setDividerHeight(20);
        //

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        delete = (ImageButton) convertView.findViewById(R.id.delete);
        delete.setFocusable(false);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (membership.getPos_count() == 1) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setPos_degree("");
                            membership.setPos_country("");
                            membership.setPos_uni("");
                            membership.setPos_qof_date("");

                            membership.setPos_count(0);
                        }
                    });
                } else if (membership.getPos_count() == 2) {
                    if (groupPosition == 1) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setPos_degree1("");
                                membership.setPos_country1("");
                                membership.setPos_uni1("");
                                membership.setPos_qof_date1("");

                                membership.setPos_count(1);
                            }
                        });
                    } else if (groupPosition == 0) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setPos_degree(membership.getPos_degree1());
                                membership.setPos_country(membership.getPos_country1());
                                membership.setPos_uni(membership.getPos_uni1());
                                membership.setPos_qof_date(membership.getPos_qof_date1());


                                membership.setPos_degree1("");
                                membership.setPos_country1("");
                                membership.setPos_uni1("");
                                membership.setPos_qof_date1("");

                                membership.setPos_count(1);
                            }
                        });
                    }
                } else if (membership.getPos_count() == 3) {
                    if (groupPosition == 2) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setPos_degree2("");
                                membership.setPos_country2("");
                                membership.setPos_uni2("");
                                membership.setPos_qof_date2("");

                                membership.setPos_count(2);
                            }
                        });
                    } else if (groupPosition == 1) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setPos_degree1(membership.getPos_degree2());
                                membership.setPos_country1(membership.getPos_country2());
                                membership.setPos_uni1(membership.getPos_uni2());
                                membership.setPos_qof_date1(membership.getPos_qof_date2());

                                membership.setPos_degree2("");
                                membership.setPos_country2("");
                                membership.setPos_uni2("");
                                membership.setPos_qof_date2("");

                                membership.setPos_count(2);
                            }
                        });
                    } else if (groupPosition == 0) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setPos_degree(membership.getPos_degree1());
                                membership.setPos_country(membership.getPos_country1());
                                membership.setPos_uni(membership.getPos_uni1());
                                membership.setPos_qof_date(membership.getPos_qof_date1());

                                membership.setPos_degree1(membership.getPos_degree2());
                                membership.setPos_country1(membership.getPos_country2());
                                membership.setPos_uni1(membership.getPos_uni2());
                                membership.setPos_qof_date1(membership.getPos_qof_date2());

                                membership.setPos_degree2("");
                                membership.setPos_country2("");
                                membership.setPos_uni2("");
                                membership.setPos_qof_date2("");

                                membership.setPos_count(2);
                            }
                        });
                    }
                }
                prepareListData();
                //   mem.Added();
            }
        });

        return convertView;
    }

    private void prepareListData() {
        _listDataHeader.clear();
        _listDataHeader = new ArrayList<String>();
        _listDataChild.clear();
        _listDataChild = new HashMap<String, List<String>>();


        if (membership.getPos_count() > 0) {

            switch (membership.getPos_count()) {
                case 1:
                    _listDataHeader.add(membership.getPos_degree());
                    List<String> child = new ArrayList<String>();
                    child.add(membership.getPos_uni());
                    child.add(membership.getPos_country());
                    child.add(membership.getPos_qof_date());
                    _listDataChild.put(_listDataHeader.get(0), child);

                    break;
                case 2:
                    _listDataHeader.add(membership.getPos_degree());
                    List<String> child1 = new ArrayList<String>();
                    child1.add(membership.getPos_uni());
                    child1.add(membership.getPos_country());
                    child1.add(membership.getPos_qof_date());
                    _listDataChild.put(_listDataHeader.get(0), child1);

                    _listDataHeader.add(membership.getPos_degree1());
                    List<String> child1_1 = new ArrayList<String>();
                    child1_1.add(membership.getPos_uni1());
                    child1_1.add(membership.getPos_country1());
                    child1_1.add(membership.getPos_qof_date1());
                    _listDataChild.put(_listDataHeader.get(1), child1_1);

                    break;
                case 3:
                    _listDataHeader.add(membership.getPos_degree());
                    List<String> child2 = new ArrayList<String>();
                    child2.add(membership.getPos_uni());
                    child2.add(membership.getPos_country());
                    child2.add(membership.getPos_qof_date());
                    _listDataChild.put(_listDataHeader.get(0), child2);

                    _listDataHeader.add(membership.getPos_degree1());
                    List<String> child2_1 = new ArrayList<String>();
                    child2_1.add(membership.getPos_uni1());
                    child2_1.add(membership.getPos_country1());
                    child2_1.add(membership.getPos_qof_date1());
                    _listDataChild.put(_listDataHeader.get(1), child2_1);

                    _listDataHeader.add(membership.getPos_degree2());
                    List<String> child2_2 = new ArrayList<String>();
                    child2_2.add(membership.getPos_uni2());
                    child2_2.add(membership.getPos_country2());
                    child2_2.add(membership.getPos_qof_date2());
                    _listDataChild.put(_listDataHeader.get(2), child2_2);

                    break;
                default:
            }
        } else {
            mem.ShowError();
//            add_info.setVisibility(View.VISIBLE);
//            expListView.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}

