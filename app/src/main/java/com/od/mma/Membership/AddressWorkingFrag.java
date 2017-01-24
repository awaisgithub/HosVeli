package com.od.mma.Membership;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.od.mma.API.Api;
import com.od.mma.BOs.User;
import com.od.mma.CallBack.ServerReadCallBack;
import com.od.mma.R;
import com.od.mma.Utils.MMAConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static com.od.mma.MMAApplication.realm;

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
    boolean spinnerCountryFromServer = false;
    boolean spinnerStateFromServer = false;
    Membership membership;
    ArrayAdapter<String> server;

    public static AddressWorkingFrag newInstance(String text) {

        AddressWorkingFrag f = new AddressWorkingFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    private void populateCountrySpinnerFromServer() {
        User.getSpinnerList(Api.urlDataListData(MMAConstants.list_countries), new ServerReadCallBack() {
            @Override
            public void success(JSONArray response) {
                List<String> title_list = new ArrayList<String>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        title_list.add(response.getJSONObject(i).optString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String[] titleArray = new String[title_list.size()];
                titleArray = title_list.toArray(titleArray);
                server = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, titleArray);
                server.setDropDownViewResource(R.layout.spinner_dropdown_item);
                country.setAdapter(
                        new NoneSelectSpinnerAdapter(server, R.layout.spinner_hint_frag5_1,
                                getActivity()));

                int spinnerPosition;
                spinnerPosition = server.getPosition(membership.getCountry());
                country.setSelection(spinnerPosition + 1);
                spinnerCountryFromServer = true;
            }

            @Override
            public void failure(String response) {
                spinnerCountryFromServer = false;
                if (response.contains(""))
                    Log.i(MMAConstants.TAG_MMA, "No Such List exist");
                else
                    Log.i(MMAConstants.TAG_MMA, "err = " + response.toString());
            }
        });
    }

    private void populateStateSpinnerFromServer() {
        User.getSpinnerList(Api.urlDataListData(MMAConstants.list_states), new ServerReadCallBack() {
            @Override
            public void success(JSONArray response) {
                List<String> title_list = new ArrayList<String>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        title_list.add(response.getJSONObject(i).optString("states"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String[] titleArray = new String[title_list.size()];
                titleArray = title_list.toArray(titleArray);
                ArrayAdapter<String> server = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, titleArray);
                server.setDropDownViewResource(R.layout.spinner_dropdown_item);
                state.setAdapter(
                        new NoneSelectSpinnerAdapter(server, R.layout.spinner_hint_frag5_2,
                                getActivity()));

                reg_state.setAdapter(
                        new NoneSelectSpinnerAdapter(server, R.layout.spinner_hint_frag5_3,
                                getActivity()));
                state.setVisibility(View.GONE);

                int spinnerPosition;
                spinnerPosition = server.getPosition(membership.getState());
                state.setSelection(spinnerPosition + 1);

                int spinnerPosition1;
                spinnerPosition1 = server.getPosition(membership.getRegistrationState());
                reg_state.setSelection(spinnerPosition1 + 1);
                spinnerStateFromServer = true;
            }

            @Override
            public void failure(String response) {
                spinnerStateFromServer = false;
                if (response.contains(""))
                    Log.i(MMAConstants.TAG_MMA, "No Such List exist");
                else
                    Log.i(MMAConstants.TAG_MMA, "err = " + response.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_membership_registration_working_address, container, false);
        initView();

        return rootView;
    }

    private void initView() {
        membership = Membership.getCurrentRegistration(realm, User.getCurrentUser(realm).getId());
        spinnerCountryFromServer = false;
        spinnerStateFromServer = false;
        city = (EditText) rootView.findViewById(R.id.name);
        country = (Spinner) rootView.findViewById(R.id.country);
        state = (Spinner) rootView.findViewById(R.id.state);
        reg_state = (Spinner) rootView.findViewById(R.id.reg_state);
        postal_code = (EditText) rootView.findViewById(R.id.email);
        address = (EditText) rootView.findViewById(R.id.contact_number);
        tel_no = (EditText) rootView.findViewById(R.id.date_pick);

        populateCountrySpinnerFromServer();
        if (!spinnerCountryFromServer) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_nationality, R.layout.spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            country.setPrompt("Select Country");
            country.setAdapter(
                    new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag5_1,
                            getActivity()));
        }

        populateStateSpinnerFromServer();
        if (!spinnerStateFromServer) {
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.id_state, R.layout.spinner_item);
            adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
            state.setPrompt("Select State");
            state.setAdapter(
                    new NoneSelectSpinnerAdapter(adapter1, R.layout.spinner_hint_frag5_2,// R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            getActivity()));
            state.setVisibility(View.GONE);

            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.id_state, R.layout.spinner_item);
            adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
            reg_state.setPrompt("Select Registration State");
            reg_state.setAdapter(
                    new NoneSelectSpinnerAdapter(adapter2, R.layout.spinner_hint_frag5_3,// R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            getActivity()));
        }

        if (!membership.isLoadFromServer()) {
            if (membership.getCountry_pos() != -1) {
                country.setSelection(membership.getCountry_pos());
            }
            if (membership.getState_pos() != -1) {
                state.setSelection(membership.getState_pos());
            }
            if (membership.getReg_branch_pos() != -1) {
                reg_state.setSelection(membership.getReg_branch_pos());
            }
        }
        if (!(membership.getCity().equals("") || membership.getCity() == null || membership.getCity().isEmpty())) {
            city.setText(membership.getCity());
        }
        if (!(membership.getPostCode().equals("") || membership.getPostCode() == null || membership.getPostCode().isEmpty())) {
            postal_code.setText(membership.getPostCode());
        }
        if (!(membership.getAddress().equals("") || membership.getAddress() == null || membership.getAddress().isEmpty())) {
            address.setText(membership.getAddress());
        }
        if (!(membership.getTelephoneNo().equals("") || membership.getTelephoneNo() == null || membership.getTelephoneNo().isEmpty())) {
            tel_no.setText(membership.getTelephoneNo());
        }


        if (membership.isValidation()) {
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
                    } else {
                        state.setVisibility(View.GONE);
                    }
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setCountry_pos(country.getSelectedItemPosition());
                            membership.setCountry(country.getSelectedItem().toString());
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
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setState_pos(state.getSelectedItemPosition());
                            membership.setState(state.getSelectedItem().toString());
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
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setReg_branch_pos(reg_state.getSelectedItemPosition());
                            membership.setRegistrationState(reg_state.getSelectedItem().toString());
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
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        membership.setCity(s.toString());
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
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        membership.setPostCode(s.toString());
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
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        membership.setAddress(s.toString());
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
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        membership.setTelephoneNo(s.toString());
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
                if (state.getSelectedItemPosition() > 0 )
                    error = true;
                else {
                    error = false;
                    if (membership.getValidation_pos() == -1) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setValidation_pos(PagerViewPager.getPos());
                            }
                        });
                    }
                }
            } else
                error = true;
        } else {
            error = false;
            if (membership.getValidation_pos() == -1) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        }
        if(reg_state.getSelectedItemPosition() > 0) {
            error = true;
        }
        else {
            error = false;
            if (membership.getValidation_pos() == -1) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        }

        if (city.getText().toString().trim().equalsIgnoreCase("")) {
            error1 = false;
            if (membership.getValidation_pos() == -1) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        membership.setValidation_pos(PagerViewPager.getPos());
                    }
                });
            }
        } else {
            error1 = true;
            city.setError(null);
        }
        if (address.getText().toString().trim().equalsIgnoreCase("")) {
            error3 = false;
            if (membership.getValidation_pos() == -1) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        membership.setValidation_pos(PagerViewPager.getPos());
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
            } else
                error = true;
        } else {
            error = false;
            showActionSnackBarMessage(getString(R.string.error_country));
        }
        if (reg_state.getSelectedItemPosition() > 0)
            error = true;
        else {
            error = false;
            showActionSnackBarMessage(getString(R.string.error_reg_state));
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
