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

public class AddressResidentialFrag extends Fragment {
    Spinner country;
    Spinner state;
    Button next, back;
    EditText city;
    EditText postal_code;
    EditText address;
    EditText tel_no;
    RadioGroup corresponce_addr;
    RadioButton last;
    boolean error = false;
    boolean error1 = false;
    boolean error2 = false;
    boolean error3 = false;
    boolean error4 = false;
    boolean error5 = false;
    FragInterface mem_interface;
    private View rootView;

    public static AddressResidentialFrag newInstance(String text) {

        AddressResidentialFrag f = new AddressResidentialFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_membership_registration_residential_address, container, false);
        initView();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mem_interface = (FragInterface) context;
    }

    private void initView() {
        last = (RadioButton) rootView.findViewById(R.id.radioButton1);
        city = (EditText) rootView.findViewById(R.id.name);
        postal_code = (EditText) rootView.findViewById(R.id.email);
        address = (EditText) rootView.findViewById(R.id.contact_number);
        tel_no = (EditText) rootView.findViewById(R.id.date_pick);
        corresponce_addr = (RadioGroup) rootView.findViewById(R.id.radioGroup);

        state = (Spinner) rootView.findViewById(R.id.state);
        state.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.id_state, R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        state.setPrompt("Select State");
        state.setAdapter(
                new NoneSelectSpinnerAdapter(adapter1, R.layout.spinner_hint_frag5_2,// R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getActivity()));

        country = (Spinner) rootView.findViewById(R.id.country);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_nationality, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        country.setPrompt("Select Country");
        country.setAdapter(
                new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag5_1,
                        getActivity()));

        if (PagerViewPager.membership.getR_country() != -1) {
            country.setSelection(PagerViewPager.membership.getR_country());
        }
        if (PagerViewPager.membership.getR_state() != -1) {
            state.setSelection(PagerViewPager.membership.getR_state());
        }
        if (!(PagerViewPager.membership.getR_city().equals("") || PagerViewPager.membership.getR_city() == null || PagerViewPager.membership.getR_city().isEmpty())) {
            city.setText(PagerViewPager.membership.getR_city());
        }
        if (!(PagerViewPager.membership.getR_postal_code().equals("") || PagerViewPager.membership.getR_postal_code() == null || PagerViewPager.membership.getR_postal_code().isEmpty())) {
            postal_code.setText(PagerViewPager.membership.getR_postal_code());
        }
        if (!(PagerViewPager.membership.getR_address().equals("") || PagerViewPager.membership.getR_address() == null || PagerViewPager.membership.getR_address().isEmpty())) {
            address.setText(PagerViewPager.membership.getR_address());
        }
        if (!(PagerViewPager.membership.getR_tel_no().equals("") || PagerViewPager.membership.getR_tel_no() == null || PagerViewPager.membership.getR_tel_no().isEmpty())) {
            tel_no.setText(PagerViewPager.membership.getR_tel_no());
        }
        if (PagerViewPager.membership.getCorrespondence_address() != -1) {
            final int id = PagerViewPager.membership.getCorrespondence_address();
            View radioButton = corresponce_addr.findViewById(id);
            int radioId = corresponce_addr.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) corresponce_addr.getChildAt(radioId);
            btn.setChecked(true);
        }


        if (PagerViewPager.membership.isValidation()) {
            loadItems();
        }


        listeners();
        navigate();


    }

    private void listeners() {
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String result = country.getSelectedItem().toString();
                    if ((result.equals("MALAYSIA") || result.equalsIgnoreCase("MALAYSIA") || result.contentEquals("MALAYSIA") || result == "MALAYSIA"))
                        state.setVisibility(View.VISIBLE);
                    else
                        state.setVisibility(View.GONE);
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setR_country(country.getSelectedItemPosition());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        corresponce_addr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (corresponce_addr.getCheckedRadioButtonId() != -1) {
                    if (PagerViewPager.membership.isValidation())
                        last.setError(null);

                    final int id = corresponce_addr.getCheckedRadioButtonId();
                    View radioButton = corresponce_addr.findViewById(id);
                    int radioId = corresponce_addr.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) corresponce_addr.getChildAt(radioId);

                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setCorrespondence_address(id);
                        }
                    });
                }
            }
        });

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setR_state(state.getSelectedItemPosition());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setR_city(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        postal_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setR_postal_code(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setR_address(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tel_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setR_tel_no(s.toString());
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
        if (country.getSelectedItemPosition() > 0) {
            String result = country.getSelectedItem().toString();
            if ((result.equals("MALAYSIA") || result.equalsIgnoreCase("MALAYSIA") || result.contentEquals("MALAYSIA") || result == "MALAYSIA")) {
                if (state.getSelectedItemPosition() > 0)
                    error = true;
                else {
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
            } else
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

        if (city.getText().toString().trim().equalsIgnoreCase("")) {
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
            city.setError(null);
        }
        if (address.getText().toString().trim().equalsIgnoreCase("")) {
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
            address.setError(null);
        }
        if (tel_no.getText().toString().trim().equalsIgnoreCase("")) {
            error4 = false;
            if (PagerViewPager.membership.getValidation_pos() == -1) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        } else {
            error4 = true;
            tel_no.setError(null);
        }
        if (!(corresponce_addr.getCheckedRadioButtonId() == -1)) {
            last.setError(null);
            error5 = true;
        } else {
            if (PagerViewPager.membership.getValidation_pos() == -1) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
            error5 = false;
        }
    }

    private void loadItems() {
        if (country.getSelectedItemPosition() > 0) {
            String result = country.getSelectedItem().toString();
            if ((result.equals("MALAYSIA") || result.equalsIgnoreCase("MALAYSIA") || result.contentEquals("MALAYSIA") || result == "MALAYSIA")) {
                if (state.getSelectedItemPosition() > 0)
                    error = true;
                else {
                    error = false;
                    showActionSnackBarMessage(getString(R.string.error_state));
                }
            } else
                error = true;
        } else {
            error = false;
            showActionSnackBarMessage(getString(R.string.error_country));
        }

        if (city.getText().toString().trim().equalsIgnoreCase("")) {
            error1 = false;
            city.setError("Enter City");
        } else {
            error1 = true;
            city.setError(null);
        }
        if (address.getText().toString().trim().equalsIgnoreCase("")) {
            error3 = false;
            address.setError("Enter Address");
        } else {
            error3 = true;
            address.setError(null);
        }
        if (tel_no.getText().toString().trim().equalsIgnoreCase("")) {
            error4 = false;
            tel_no.setError("Enter Telephone Number");
        } else {
            error4 = true;
            tel_no.setError(null);
        }
        if (!(corresponce_addr.getCheckedRadioButtonId() == -1)) {
            last.setError(null);
            error5 = true;
        } else {
            last.setError("Select Correspondence Address");
            error5 = false;
        }

    }
}
