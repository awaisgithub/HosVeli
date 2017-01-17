package com.od.mma.Membership;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.od.mma.R;

import io.realm.Realm;

/**
 * Created by awais on 29/12/2016.
 */

public class SpouseFrag extends Fragment {
    Spinner id_type;
    Button next, back;
    RadioGroup joint;
    RadioButton last;
    EditText nric_no;
    EditText fName;
    EditText email;
    boolean error = false;
    boolean error1 = false;
    boolean error2 = false;
    boolean error3 = false;
    boolean error4 = false;
    FragInterface mem_interface;
    private View rootView;

    public static SpouseFrag newInstance(String text) {

        SpouseFrag f = new SpouseFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_membership_spouse, container, false);
        initView();

        return rootView;
    }

    private void initView() {
        last = (RadioButton) rootView.findViewById(R.id.radioButtonYes);
        nric_no = (EditText) rootView.findViewById(R.id.name);
        fName = (EditText) rootView.findViewById(R.id.email);
        email = (EditText) rootView.findViewById(R.id.date_pick);
        joint = (RadioGroup) rootView.findViewById(R.id.joint_account);
        if (PagerViewPager.membership.getMain_category().equals("Ordinary") || PagerViewPager.membership.getMain_category().equals("Life Time Membership")) {
            if (PagerViewPager.membership.getJoint_account() != -1) {
                final int id = PagerViewPager.membership.getJoint_account();
                View radioButton = joint.findViewById(id);
                int radioId = joint.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) joint.getChildAt(radioId);
                btn.setChecked(true);
            }
            joint.setVisibility(View.VISIBLE);
        } else
            joint.setVisibility(View.GONE);
        id_type = (Spinner) rootView.findViewById(R.id.id_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_type, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        id_type.setPrompt("Select ID");
        id_type.setAdapter(
                new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag4_1,
                        getActivity()));


        if (PagerViewPager.membership.getSpouse_id_type() != -1) {
            id_type.setSelection(PagerViewPager.membership.getSpouse_id_type());
        }
        if (!(PagerViewPager.membership.getNric_no() == null)) {
            nric_no.setText(PagerViewPager.membership.getNric_no());
        }
        if (!(PagerViewPager.membership.getSpouse_fName() == null)) {
            fName.setText(PagerViewPager.membership.getSpouse_fName());
        }
        if (!(PagerViewPager.membership.getSpouse_email() == null)) {
            email.setText(PagerViewPager.membership.getSpouse_email());
        }


        if (PagerViewPager.membership.isValidation()) {
            loadItems();
        }


        listeners();
        navigate();


    }

    private void listeners() {
        joint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (joint.getCheckedRadioButtonId() != -1) {
                    if (PagerViewPager.membership.isValidation())
                        last.setError(null);
                    final int id = joint.getCheckedRadioButtonId();
                    View radioButton = joint.findViewById(id);
                    int radioId = joint.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) joint.getChildAt(radioId);

                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setJoint_account(id);
                        }
                    });
                } else {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setJoint_account(-1);
                        }
                    });
                }
            }
        });

        id_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    nric_no.setHint(id_type.getSelectedItem().toString() + " No.");
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setSpouse_id_type(id_type.getSelectedItemPosition());
                        }
                    });
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        nric_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setNric_no(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setSpouse_fName(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setSpouse_email(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    private void showActionSnackBarMessage(String message) {
        View view = rootView.findViewById(R.id.parent_layout);
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setMaxLines(2);
    }

    public void validation() {
        if (PagerViewPager.membership.getMain_category().equals("Ordinary") || PagerViewPager.membership.getMain_category().equals("Life Time Membership")) {

            if (PagerViewPager.membership.getJoint_account() != -1) {
                if (last.isChecked()) {
                    if (id_type.getSelectedItemPosition() > 0) {
                        error = true;
                    } else {
                        error = false;
                        if (PagerViewPager.membership.getValidation_pos() == -1) {
                            PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                                }
                            });
                        }
                    }
                    if (nric_no.getText().toString().trim().equalsIgnoreCase("")) {
                        error1 = false;
                        if (PagerViewPager.membership.getValidation_pos() == -1) {
                            PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                                }
                            });
                        }
                    } else {
                        error1 = true;
                        nric_no.setError(null);
                    }
                    if (fName.getText().toString().trim().equalsIgnoreCase("")) {
                        error2 = false;
                        if (PagerViewPager.membership.getValidation_pos() == -1) {
                            PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                                }
                            });
                        }
                    } else {
                        error2 = true;
                        fName.setError(null);
                    }
                    if (email.getText().toString().trim().equalsIgnoreCase("")) {
                        error3 = false;
                        if (PagerViewPager.membership.getValidation_pos() == -1) {
                            PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                                }
                            });
                        }
                    } else {
                        error3 = true;
                        email.setError(null);
                    }
                }
            } else {
                if (PagerViewPager.membership.getValidation_pos() == -1) {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mem_interface = (FragInterface) context;
    }

    private void loadItems() {
        if (PagerViewPager.membership.getMain_category().equals("Ordinary") || PagerViewPager.membership.getMain_category().equals("Life Time Membership")) {

            if (PagerViewPager.membership.getJoint_account() != -1) {
                if (last.isChecked()) {
                    if (id_type.getSelectedItemPosition() > 0) {
                        error = true;
                    } else {
                        error = false;
                        showActionSnackBarMessage(getString(R.string.error_id_type));
                    }
                    if (nric_no.getText().toString().trim().equalsIgnoreCase("")) {
                        error1 = false;
                        nric_no.setError("Enter NRIC Number");
                    } else {
                        error1 = true;
                        nric_no.setError(null);
                    }
                    if (fName.getText().toString().trim().equalsIgnoreCase("")) {
                        error2 = false;
                        fName.setError("Enter First Name");
                    } else {
                        error2 = true;
                        fName.setError(null);
                    }
                    if (email.getText().toString().trim().equalsIgnoreCase("")) {
                        error3 = false;
                        email.setError("Enter Email");
                    } else {
                        error3 = true;
                        email.setError(null);
                    }

                }
            } else {
                last.setError("Select Joint Account");
            }
        }


    }
}
