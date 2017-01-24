package com.od.mma.Membership;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

import static com.od.mma.MMAApplication.realm;

/**
 * Created by awais on 02/01/2017.
 */

public class BachelorDegreeFrag extends Fragment {
    Spinner degree;
    Spinner country;
    Spinner uni;
    Button next, back;
    Calendar myCalendar;
    EditText uni_name;
    EditText date_pik;
    boolean error = false;
    boolean error1 = false;
    boolean error2 = false;
    boolean error3 = false;
    FragInterface mem_interface;
    private View rootView;
    boolean spinnerDegreeFromServer = false;
    boolean spinnerUniFromServer = false;
    boolean spinnerCountryFromServer = false;
    Membership membership;

    public static BachelorDegreeFrag newInstance(String text) {

        BachelorDegreeFrag f = new BachelorDegreeFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    private void populateDegreeSpinnerFromServer() {
        User.getSpinnerList(Api.urlDataListData(MMAConstants.list_basic_degree), new ServerReadCallBack() {
            @Override
            public void success(JSONArray response) {
                List<String> title_list = new ArrayList<String>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        title_list.add(response.getJSONObject(i).optString("description"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String[] titleArray = new String[title_list.size()];
                titleArray = title_list.toArray(titleArray);
                ArrayAdapter<String> server = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, titleArray);
                server.setDropDownViewResource(R.layout.spinner_dropdown_item);
                degree.setAdapter(
                        new NoneSelectSpinnerAdapter(server, R.layout.spinner_hint_frag8_1,
                                getActivity()));
                if (membership.getBachelor_degree() != -1) {
                    degree.setSelection(membership.getBachelor_degree());
                }
                spinnerDegreeFromServer = true;
            }

            @Override
            public void failure(String response) {
                spinnerDegreeFromServer = false;
                if (response.contains(""))
                    Log.i(MMAConstants.TAG_MMA, "No Such List exist");
                else
                    Log.i(MMAConstants.TAG_MMA, "err = " + response.toString());
            }
        });
    }

    private void populateUniSpinnerFromServer() {
        User.getSpinnerList(Api.urlDataListData(MMAConstants.list_university), new ServerReadCallBack() {
            @Override
            public void success(JSONArray response) {
                List<String> title_list = new ArrayList<String>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        title_list.add(response.getJSONObject(i).optString("c_universityname"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String[] titleArray = new String[title_list.size()];
                titleArray = title_list.toArray(titleArray);
                ArrayAdapter<String> server = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, titleArray);
                server.setDropDownViewResource(R.layout.spinner_dropdown_item);
                uni.setAdapter(
                        new NoneSelectSpinnerAdapter(server, R.layout.spinner_hint_frag8_3,
                                getActivity()));
                if (!(membership.getBachelor_uni() == null)) {
                    uni_name.setText(membership.getBachelor_uni());
                }
                if (membership.getBachelor_uni_malay() != -1) {
                    uni.setSelection(membership.getBachelor_uni_malay());
                }
                spinnerUniFromServer = true;
            }

            @Override
            public void failure(String response) {
                spinnerUniFromServer = false;
                if (response.contains(""))
                    Log.i(MMAConstants.TAG_MMA, "No Such List exist");
                else
                    Log.i(MMAConstants.TAG_MMA, "err = " + response.toString());
            }
        });
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
                ArrayAdapter<String> server = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, titleArray);
                server.setDropDownViewResource(R.layout.spinner_dropdown_item);
                country.setAdapter(
                        new NoneSelectSpinnerAdapter(server, R.layout.spinner_hint_frag5_1,
                                getActivity()));
                if (membership.getBachelor_country() != -1) {
                    country.setSelection(membership.getBachelor_country());
                }
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_membership_qualification, container, false);
        initView();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mem_interface = (FragInterface) context;
    }

    private void initView() {
        membership = Membership.getCurrentRegistration(realm, User.getCurrentUser(realm).getId());
        spinnerDegreeFromServer = false;
        spinnerUniFromServer = false;
        spinnerCountryFromServer = false;
        uni_name = (EditText) rootView.findViewById(R.id.name);
        degree = (Spinner) rootView.findViewById(R.id.id_bach);
        country = (Spinner) rootView.findViewById(R.id.id_uni);
        uni = (Spinner) rootView.findViewById(R.id.id_uni_malaysia);
        date_pik = (EditText) rootView.findViewById(R.id.date_qualify);
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date_pik.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        populateDegreeSpinnerFromServer();
        if (!spinnerDegreeFromServer) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_bachelor, R.layout.spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            degree.setPrompt("Select Employment Status");
            degree.setAdapter(
                    new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag8_1,
                            getActivity()));
        }

        populateUniSpinnerFromServer();
        if (!spinnerUniFromServer) {
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.id_bachelor, R.layout.spinner_item);
            adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
            uni.setPrompt("Select Employment Status");
            uni.setAdapter(
                    new NoneSelectSpinnerAdapter(adapter2, R.layout.spinner_hint_frag8_3,
                            getActivity()));
        }

        populateCountrySpinnerFromServer();
        if (!spinnerCountryFromServer) {
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.id_nationality, R.layout.spinner_item);
            adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
            country.setPrompt("Select Country");
            country.setAdapter(
                    new NoneSelectSpinnerAdapter(adapter1, R.layout.spinner_hint_frag8_2,// R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            getActivity()));
        }


        if (membership.getBachelor_country() == -1) {
            country.setSelection(13);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    membership.setBachelor_country(country.getSelectedItemPosition());
                    membership.setDegree_bachelor_country(country.getSelectedItem().toString());
                }
            });
        }

        if (membership.getBachelor_degree() != -1) {
            degree.setSelection(membership.getBachelor_degree());
        }
        if (!(membership.getBachelor_uni() == null)) {
            uni_name.setText(membership.getBachelor_uni());
        }
        if (membership.getBachelor_country() != -1) {
            country.setSelection(membership.getBachelor_country());
        }
        if (!(membership.getBachelor_qualification_date() == null)) {
            date_pik.setText(membership.getBachelor_qualification_date());
        }
        if (membership.getBachelor_uni_malay() != -1) {
            uni.setSelection(membership.getBachelor_uni_malay());
        }


        if (membership.isValidation()) {
            loadItems();
        }


        listeners();
        navigate();

    }

    private void listeners() {
        degree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setBachelor_degree(degree.getSelectedItemPosition());
                            membership.setDegree_bachelor(degree.getSelectedItem().toString());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setBachelor_uni_malay(uni.getSelectedItemPosition());
                            membership.setBachelor_uni(uni.getSelectedItem().toString());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (position == 1 || country.getSelectedItem().toString().equals("MALAYSIA")) {
                        uni.setVisibility(View.VISIBLE);
                        uni_name.setVisibility(View.GONE);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setBachelor_country(country.getSelectedItemPosition());
                                membership.setDegree_bachelor_country(country.getSelectedItem().toString());
                            }
                        });
                    } else {
                        uni_name.setVisibility(View.VISIBLE);
                        uni.setVisibility(View.GONE);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setBachelor_country(country.getSelectedItemPosition());
                                membership.setDegree_bachelor_country(country.getSelectedItem().toString());
                                membership.setBachelor_uni_malay(-1);
                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uni_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        membership.setBachelor_uni(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        date_pik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (membership.getMain_category().equals("Student")) {
                    if (s.toString().equals("") || s.toString().isEmpty())
                        date_pik.setError("Enter Qualification Date");
                    else {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setBachelor_qualification_date(s.toString());
                            }
                        });
                        date_pik.setError(null);
                    }
                } else {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setBachelor_qualification_date(s.toString());
                        }
                    });
                }
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
        if (membership.getMain_category().equals("Student")) {

            if (country.getSelectedItemPosition() > 0) {
                error2 = true;
            } else {
                error2 = false;
                if (membership.getValidation_pos() == -1) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setValidation_pos(PagerViewPager.getPos());
                        }
                    });
                }
            }

            if (country.getSelectedItemPosition() == 1) {
                if (uni.getSelectedItemPosition() > 0) {
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
            } else if (country.getSelectedItemPosition() > 1) {
                if (uni_name.getText().toString().trim().equalsIgnoreCase("")) {
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
                    uni_name.setError(null);
                }
            }


            if (degree.getSelectedItemPosition() > 0) {
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


            if (membership.getBachelor_qualification_date().trim().equalsIgnoreCase("")) {
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
                date_pik.setError(null);
            }

        }

    }

    private void loadItems() {
        if (membership.getMain_category().equals("Student")) {

            if (degree.getSelectedItemPosition() > 0) {
                error = true;
            } else {
                error = false;
                showActionSnackBarMessage(getString(R.string.error_degree));
            }


            if (!(membership.getBachelor_uni().equals("") || membership.getBachelor_uni().isEmpty())) {
                if (uni_name.getText().toString().trim().equalsIgnoreCase("")) {
                    error1 = false;
                    uni_name.setError("Enter University");
                } else {
                    error1 = true;
                    uni_name.setError(null);
                }
            } else if (membership.getBachelor_uni_malay() != -1) {
                if (uni.getSelectedItemPosition() > 0) {
                    error = true;
                } else {
                    error = false;
                    showActionSnackBarMessage(getString(R.string.error_uni));
                }
            }


            if (country.getSelectedItemPosition() > 0) {
                error2 = true;
            } else {
                error2 = false;
                showActionSnackBarMessage(getString(R.string.error_country));
            }
            if (membership.getBachelor_qualification_date().trim().equalsIgnoreCase("")) {
                error3 = false;
                date_pik.setError("Enter Qualification Date");
            } else {
                error3 = true;
                date_pik.setError(null);
            }

        } else {
            date_pik.setError(null);
        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_pik.setText(sdf.format(myCalendar.getTime()));
    }
}

