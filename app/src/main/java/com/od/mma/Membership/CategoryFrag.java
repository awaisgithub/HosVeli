package com.od.mma.Membership;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.od.mma.BOs.User;
import com.od.mma.R;

import io.realm.Realm;

import static com.od.mma.MMAApplication.realm;

/**
 * Created by awais on 29/12/2016.
 */

public class CategoryFrag extends Fragment {
    Button next, back;
    Spinner year;
    RadioGroup category;
    RadioButton first;
    RadioButton second;
    RadioButton third;
    RadioButton fourth;
    RadioButton fifth;
    RadioButton seventh;
    RadioButton last;
    RadioGroup lifetime;
    RadioButton yes;
    RadioButton no;
    boolean error = true;
    boolean err = false;
    FragInterface mem_interface;
    private View rootView;
    Membership membership;

    public static CategoryFrag newInstance(String text) {
        CategoryFrag f = new CategoryFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_membership_category, container, false);

        initView();
        return rootView;
    }

    private void initView() {
        membership = Membership.getCurrentRegistration(realm, User.getCurrentUser(realm).getId());
        year = (Spinner) rootView.findViewById(R.id.year);
        first = (RadioButton) rootView.findViewById(R.id.radioButton);
        second = (RadioButton) rootView.findViewById(R.id.radioButton1);
        third = (RadioButton) rootView.findViewById(R.id.radioButton2);
        fourth = (RadioButton) rootView.findViewById(R.id.radioButton3);
        fifth = (RadioButton) rootView.findViewById(R.id.radioButton4);
        seventh = (RadioButton) rootView.findViewById(R.id.radioButton6);
        last = (RadioButton) rootView.findViewById(R.id.radioButton7);
        yes = (RadioButton) rootView.findViewById(R.id.radioButtonYes);
        no = (RadioButton) rootView.findViewById(R.id.radioButtonNo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_year, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        year.setPrompt("Select Year");
        year.setAdapter(
                new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag2,
                        getActivity()));
        category = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        lifetime = (RadioGroup) rootView.findViewById(R.id.lifetime);


        if (!membership.isLoadFromServer()) {
            if (membership.getCategory() != -1) {
                final int id = membership.getCategory();
                View radioButton = category.findViewById(id);
                int radioId = category.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) category.getChildAt(radioId);
                btn.setChecked(true);
                final String selection = (String) btn.getText();
                if (selection.equals("Medical Officer Membership") || selection.equalsIgnoreCase("Medical Officer Membership")) {
                    if (membership.getYear() != -1) {
                        year.setSelection(membership.getYear());
                    }
                    year.setVisibility(View.VISIBLE);
                } else {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setYear(-1);
                            membership.setYearOfService("");
                        }
                    });
                    year.setSelection(0);
                    year.setVisibility(View.GONE);
                }
            }
        } else {
            if (membership.getMembershipCategory().equalsIgnoreCase("Associate Membership")) {
                first.setChecked(true);
                year.setVisibility(View.GONE);
                year.setSelection(0);
            } else if (membership.getMembershipCategory().equalsIgnoreCase("Life Time Membership")) {
                second.setChecked(true);
                year.setVisibility(View.GONE);
                year.setSelection(0);
            } else if (membership.getMembershipCategory().equalsIgnoreCase("House Doctor Membership")) {
                lifetime.setVisibility(View.VISIBLE);
                if (membership.getHouseDoctorIsLifeTime().equalsIgnoreCase("Yes")) {
                    yes.setChecked(true);
                } else if (membership.getHouseDoctorIsLifeTime().equalsIgnoreCase("No")) {
                    no.setChecked(true);
                }
                third.setChecked(true);
                year.setVisibility(View.GONE);
                year.setSelection(0);
            } else if (membership.getMembershipCategory().equalsIgnoreCase("Ordinary Membership")) {
                fourth.setChecked(true);
                year.setVisibility(View.GONE);
                year.setSelection(0);
            } else if (membership.getMembershipCategory().equalsIgnoreCase("Overseas Ordinary Membership")) {
                fifth.setChecked(true);
                year.setVisibility(View.GONE);
                year.setSelection(0);
            } else if (membership.getMembershipCategory().equalsIgnoreCase("Medical Officer Membership")) {
                seventh.setChecked(true);
                if (!(membership.getYearOfService().equalsIgnoreCase("") || membership.getYearOfService().isEmpty())) {
                    int spinnerPosition = adapter.getPosition(membership.getYearOfService());
                    year.setSelection(spinnerPosition + 1);
                    year.setVisibility(View.VISIBLE);
                }
            } else if (membership.getMembershipCategory().equalsIgnoreCase("Student Membership")) {
                last.setChecked(true);
                year.setVisibility(View.GONE);
                year.setSelection(0);
            }
        }


        if (membership.isValidation()) {
            loadItems();
        }
        listeners();
        navigate();
    }

    private void listeners() {
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (year.getSelectedItemPosition() > 0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setYear(year.getSelectedItemPosition());
                            membership.setYearOfService(year.getSelectedItem().toString());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (category.getCheckedRadioButtonId() != -1) {
                    if (membership.isValidation())
                        last.setError(null);
                    if (category.getCheckedRadioButtonId() != membership.getCategory()) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setDateOfRegistrationMMC("");
                                membership.setMmc_certificate(null);
                            }
                        });
                    }
                    final int id = category.getCheckedRadioButtonId();
                    View radioButton = category.findViewById(id);
                    int radioId = category.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) category.getChildAt(radioId);
                    final String selection = (String) btn.getText();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setCategory(id);
                            membership.setMembershipCategory(selection);
                        }
                    });
                    if (checkedId == R.id.radioButton6) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMain_category("");
                            }
                        });
                        year.setVisibility(View.VISIBLE);
                        third.setVisibility(View.GONE);
                        err = true;
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMedical_off_mem(true);
                            }
                        });
                    } else if (checkedId == R.id.radioButton1) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMain_category("Life Time Membership");
                            }
                        });
                        year.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        err = false;
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMedical_off_mem(false);
                            }
                        });
                    } else if (checkedId == R.id.radioButton3) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMain_category("Ordinary");
                            }
                        });
                        year.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        err = false;
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMedical_off_mem(false);
                            }
                        });
                    } else if (checkedId == R.id.radioButton7) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMain_category("Student");
                            }
                        });
                        year.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        err = false;
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMedical_off_mem(false);
                            }
                        });
                    } else if (checkedId == R.id.radioButton2) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMain_category("Student");
                            }
                        });
                        year.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        err = false;
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMedical_off_mem(false);
                            }
                        });
                    } else {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMain_category("");
                            }
                        });
                        year.setVisibility(View.GONE);
                        err = false;
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setMedical_off_mem(false);
                            }
                        });
                    }
                }
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
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mem_interface = (FragInterface) context;
    }

    public void validation() {
        if (!(category.getCheckedRadioButtonId() == -1)) {
            last.setError(null);
            if (membership.isMedical_off_mem()) {
                if (year.getSelectedItemPosition() > 0)
                    error = true;
                else {
                    error = false;
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setValidation_pos(PagerViewPager.getPos());
                        }
                    });
                }
            } else {
                error = true;
            }
            if (error) {
                error = true;
            }
            if (membership.getMembershipCategory().equalsIgnoreCase("House Doctor Membership")) {
                if (membership.getHouseDoctorIsLifeTime().equalsIgnoreCase("Yes") || membership.getHouseDoctorIsLifeTime().equalsIgnoreCase("No")) {
                    error = true;
                } else {
                    error = false;
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setValidation_pos(PagerViewPager.getPos());
                        }
                    });
                }
            }
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    membership.setValidation_pos(PagerViewPager.getPos());
                }
            });
        }
    }

    public void loadItems() {
        if (!(category.getCheckedRadioButtonId() == -1)) {
            last.setError(null);
            if (membership.isMedical_off_mem()) {
                if (year.getSelectedItemPosition() > 0)
                    error = true;
                else {
                    error = false;
                    showActionSnackBarMessage(getString(R.string.error_year));
                }
            } else {
                error = true;
            }
            if (error) {
                error = true;
            }
        } else
            last.setError("Select a Category");
        if (membership.getMembershipCategory().equalsIgnoreCase("House Doctor Membership")) {
            if (membership.getHouseDoctorIsLifeTime().equalsIgnoreCase("Yes") || membership.getHouseDoctorIsLifeTime().equalsIgnoreCase("No")) {
                error = true;
            } else {
                showActionSnackBarMessage(getString(R.string.error_member));
            }
        }


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
