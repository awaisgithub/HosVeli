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

import com.od.mma.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;

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

    public static BachelorDegreeFrag newInstance(String text) {

        BachelorDegreeFrag f = new BachelorDegreeFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
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
        uni_name = (EditText) rootView.findViewById(R.id.name);
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

        degree = (Spinner) rootView.findViewById(R.id.id_bach);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_bachelor, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        degree.setPrompt("Select Employment Status");
        degree.setAdapter(
                new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag8_1,
                        getActivity()));


        uni = (Spinner) rootView.findViewById(R.id.id_uni_malaysia);                                  //change array here
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.id_bachelor, R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        uni.setPrompt("Select Employment Status");
        uni.setAdapter(
                new NoneSelectSpinnerAdapter(adapter2, R.layout.spinner_hint_frag8_3,
                        getActivity()));


        country = (Spinner) rootView.findViewById(R.id.id_uni);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.id_nationality, R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        country.setPrompt("Select Country");
        country.setAdapter(
                new NoneSelectSpinnerAdapter(adapter1, R.layout.spinner_hint_frag8_2,// R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getActivity()));


        if (PagerViewPager.membership.getBachelor_country() == -1) {
            country.setSelection(1);
            PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    PagerViewPager.membership.setBachelor_country(country.getSelectedItemPosition());
                }
            });
        }

        if (PagerViewPager.membership.getBachelor_degree() != -1) {
            degree.setSelection(PagerViewPager.membership.getBachelor_degree());
        }
        if (!(PagerViewPager.membership.getBachelor_uni() == null)) {
            uni_name.setText(PagerViewPager.membership.getBachelor_uni());
        }
        if (PagerViewPager.membership.getBachelor_country() != -1) {
            country.setSelection(PagerViewPager.membership.getBachelor_country());
        }
        if (!(PagerViewPager.membership.getBachelor_qualification_date() == null)) {
            date_pik.setText(PagerViewPager.membership.getBachelor_qualification_date());
        }
        if (PagerViewPager.membership.getBachelor_uni_malay() != -1) {
            uni.setSelection(PagerViewPager.membership.getBachelor_uni_malay());
        }


        if (PagerViewPager.membership.isValidation()) {
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
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setBachelor_degree(degree.getSelectedItemPosition());
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
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setBachelor_uni_malay(uni.getSelectedItemPosition());
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
                        PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                PagerViewPager.membership.setBachelor_country(country.getSelectedItemPosition());
                                PagerViewPager.membership.setBachelor_uni("");
                            }
                        });
                    } else {
                        uni_name.setVisibility(View.VISIBLE);
                        uni.setVisibility(View.GONE);
                        PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                PagerViewPager.membership.setBachelor_country(country.getSelectedItemPosition());
                                PagerViewPager.membership.setBachelor_uni_malay(-1);
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
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PagerViewPager.membership.setBachelor_uni(s.toString());
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
                if (PagerViewPager.membership.getMain_category().equals("Student")) {
                    if (s.toString().equals("") || s.toString().isEmpty())
                        date_pik.setError("Enter Qualification Date");
                    else {
                        PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                PagerViewPager.membership.setBachelor_qualification_date(s.toString());
                            }
                        });
                        date_pik.setError(null);
                    }
                } else {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setBachelor_qualification_date(s.toString());
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
        if (PagerViewPager.membership.getMain_category().equals("Student")) {

            if (country.getSelectedItemPosition() > 0) {
                error2 = true;
            } else {
                error2 = false;
                if (PagerViewPager.membership.getValidation_pos() == -1) {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            PagerViewPager.membership.setValidation_pos(PagerViewPager.getPos());
                        }
                    });
                }
            }

            if (country.getSelectedItemPosition() == 1) {
                if (uni.getSelectedItemPosition() > 0) {
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
            } else if (country.getSelectedItemPosition() > 1) {
                if (uni_name.getText().toString().trim().equalsIgnoreCase("")) {
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
                    uni_name.setError(null);
                }
            }


            if (degree.getSelectedItemPosition() > 0) {
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


            if (PagerViewPager.membership.getBachelor_qualification_date().trim().equalsIgnoreCase("")) {
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
                date_pik.setError(null);
            }

        }

    }

    private void loadItems() {
        if (PagerViewPager.membership.getMain_category().equals("Student")) {

            if (degree.getSelectedItemPosition() > 0) {
                error = true;
            } else {
                error = false;
                showActionSnackBarMessage(getString(R.string.error_degree));
            }


            if (!(PagerViewPager.membership.getBachelor_uni().equals("") || PagerViewPager.membership.getBachelor_uni().isEmpty())) {
                if (uni_name.getText().toString().trim().equalsIgnoreCase("")) {
                    error1 = false;
                    uni_name.setError("Enter University");
                } else {
                    error1 = true;
                    uni_name.setError(null);
                }
            } else if (PagerViewPager.membership.getBachelor_uni_malay() != -1) {
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
            if (PagerViewPager.membership.getBachelor_qualification_date().trim().equalsIgnoreCase("")) {
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

