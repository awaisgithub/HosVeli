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
import android.widget.Spinner;
import android.widget.TextView;

import com.od.mma.R;

import io.realm.Realm;

/**
 * Created by awais on 29/12/2016.
 */

public class AddressWorkingFrag extends Fragment {
    Spinner country;
    Spinner state;
    Spinner reg_state;
    Button next, back;
    EditText city;
    EditText postal_code;
    EditText address;
    EditText tel_no;
    boolean error = false;
    boolean error1 = false;
    boolean error2 = false;
    boolean error3 = false;
    boolean error4 = false;
    FragInterface mem_interface;
    private View rootView;

    public static AddressWorkingFrag newInstance(String text) {

        AddressWorkingFrag f = new AddressWorkingFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_membership_registration_working_address, container, false);
        initView();

        return rootView;
    }

    private void initView() {
        city = (EditText) rootView.findViewById(R.id.name);
        postal_code = (EditText) rootView.findViewById(R.id.email);
        address = (EditText) rootView.findViewById(R.id.contact_number);
        tel_no = (EditText) rootView.findViewById(R.id.date_pick);

        state = (Spinner) rootView.findViewById(R.id.state);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.id_state, R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        state.setPrompt("Select State");
        state.setAdapter(
                new NoneSelectSpinnerAdapter(adapter1, R.layout.spinner_hint_frag5_2,// R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getActivity()));
        state.setVisibility(View.GONE);

        reg_state = (Spinner) rootView.findViewById(R.id.reg_state);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.id_state, R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        reg_state.setPrompt("Select Registration State");
        reg_state.setAdapter(
                new NoneSelectSpinnerAdapter(adapter2, R.layout.spinner_hint_frag5_3,// R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getActivity()));
        reg_state.setVisibility(View.GONE);

        country = (Spinner) rootView.findViewById(R.id.country);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_nationality, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        country.setPrompt("Select Country");
        country.setAdapter(
                new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag5_1,
                        getActivity()));


        if (PagerViewPager.membership.getCountry() != -1) {
            country.setSelection(PagerViewPager.membership.getCountry());
        }
        if (PagerViewPager.membership.getState() != -1) {
            state.setSelection(PagerViewPager.membership.getState());
        }
        if (PagerViewPager.membership.getReg_state() != -1) {
            reg_state.setSelection(PagerViewPager.membership.getReg_state());
        }
        if (!(PagerViewPager.membership.getCity().equals("") || PagerViewPager.membership.getCity() == null || PagerViewPager.membership.getCity().isEmpty())) {
            city.setText(PagerViewPager.membership.getCity());
        }
        if (!(PagerViewPager.membership.getPostal_code().equals("") || PagerViewPager.membership.getPostal_code() == null || PagerViewPager.membership.getPostal_code().isEmpty())) {
            postal_code.setText(PagerViewPager.membership.getPostal_code());
        }
        if (!(PagerViewPager.membership.getAddress().equals("") || PagerViewPager.membership.getAddress() == null || PagerViewPager.membership.getAddress().isEmpty())) {
            address.setText(PagerViewPager.membership.getAddress());
        }
        if (!(PagerViewPager.membership.getTel_no().equals("") || PagerViewPager.membership.getTel_no() == null || PagerViewPager.membership.getTel_no().isEmpty())) {
            tel_no.setText(PagerViewPager.membership.getTel_no());
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
                    if ((result.equals("MALAYSIA") || result.equalsIgnoreCase("MALAYSIA") || result.contentEquals("MALAYSIA") || result == "MALAYSIA")) {
                        state.setVisibility(View.VISIBLE);
                        reg_state.setVisibility(View.VISIBLE);
                    } else {
                        state.setVisibility(View.GONE);
                        reg_state.setVisibility(View.GONE);
                    }
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setCountry(country.getSelectedItemPosition());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setState(state.getSelectedItemPosition());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reg_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setReg_state(reg_state.getSelectedItemPosition());
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
                        PagerViewPager.membership.setCity(s.toString());
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
                        PagerViewPager.membership.setPostal_code(s.toString());
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
                        PagerViewPager.membership.setAddress(s.toString());
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
                        PagerViewPager.membership.setTel_no(s.toString());
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mem_interface = (FragInterface) context;
    }

    public void validation() {
        if (country.getSelectedItemPosition() > 0) {
            String result = country.getSelectedItem().toString();
            if ((result.equals("MALAYSIA") || result.equalsIgnoreCase("MALAYSIA") || result.contentEquals("MALAYSIA") || result == "MALAYSIA")) {
                if (state.getSelectedItemPosition() > 0 && reg_state.getSelectedItemPosition() > 0)
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
                if (reg_state.getSelectedItemPosition() > 0)
                    error = true;
                else {
                    error = false;
                    showActionSnackBarMessage(getString(R.string.error_reg_state));
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

    }

    private void showActionSnackBarMessage(String message) {
        View view = rootView.findViewById(R.id.parent_layout);
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setMaxLines(2);
    }
}