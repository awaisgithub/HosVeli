package com.od.mma.profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.od.mma.API.Api;
import com.od.mma.BOs.User;
import com.od.mma.CallBack.StatusCallBack;
import com.od.mma.R;
import com.od.mma.Utils.MMAConstants;

import org.json.JSONObject;

import io.realm.Realm;

import static com.od.mma.MMAApplication.realm;

public class ProfileMyProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View rootView;
    TextView name;
    TextView email;
    TextView nationality;
    TextView contact_number;
    TextView organisation;
    TextView designation;
    User user;
    ViewSwitcher viewSwitcher;
    TextView edit;
    ImageButton back;
    Button submit_button;
    private EditText nameTV;
    private EditText emailTV;
    private EditText contactTV;
    private EditText nationalityTV;
    private EditText organisationTV;
    private EditText designationTV;

    public ProfileMyProfileFragment() {
    }

    public static ProfileMyProfileFragment newInstance(String param1, String param2) {
        ProfileMyProfileFragment fragment = new ProfileMyProfileFragment();
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
        rootView = inflater.inflate(R.layout.fragment_profile_my_profile, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = User.getCurrentUser(realm);

        viewSwitcher = (ViewSwitcher) rootView.findViewById(R.id.edit_switcher);
        edit = (TextView) rootView.findViewById(R.id.textView5);
        back = (ImageButton) rootView.findViewById(R.id.back);
        submit_button = (Button) rootView.findViewById(R.id.submit_button);

        name = (TextView) rootView.findViewById(R.id.profile_name);
        email = (TextView) rootView.findViewById(R.id.profile_email);
        nationality = (TextView) rootView.findViewById(R.id.profile_nationality);
        contact_number = (TextView) rootView.findViewById(R.id.profile_contact_no);
        organisation = (TextView) rootView.findViewById(R.id.profile_organisation);
        designation = (TextView) rootView.findViewById(R.id.profile_designation);

        nameTV = (EditText) rootView.findViewById(R.id.name);
        emailTV = (EditText) rootView.findViewById(R.id.email);
        contactTV = (EditText) rootView.findViewById(R.id.contact_number);
        nationalityTV = (EditText) rootView.findViewById(R.id.nationality);
        organisationTV = (EditText) rootView.findViewById(R.id.organisation);
        designationTV = (EditText) rootView.findViewById(R.id.designation);

        setProfileValues();

        submit_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                edit.setVisibility(View.VISIBLE);
                validateAndSubmitUpdation();
                viewSwitcher.showNext();
                //      clearEditFields();
                setProfileValues();
            }

        });

        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                edit.setVisibility(View.INVISIBLE);
                setEditValues();
                viewSwitcher.showNext();
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.VISIBLE);
                viewSwitcher.showPrevious();
                //    clearEditFields();
            }
        });

    }

    private void validateAndSubmitUpdation() {
        final String edit_name = nameTV.getText().toString();
        final String edit_contact = contactTV.getText().toString();
        final String edit_nationality = nationalityTV.getText().toString();
        final String edit_organisation = organisationTV.getText().toString();
        final String edit_designation = designationTV.getText().toString();

        if (edit_name == null && edit_contact == null && edit_nationality == null && edit_organisation == null && edit_designation == null) {
            //Toast or snackbar
        } else {
            if (edit_name != null) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        user.setName(edit_name);
                    }
                });
            }
            if (edit_contact != null) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        user.setContactNumber(edit_contact);
                    }
                });
            }
            if (edit_nationality != null) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        user.setNationality(edit_nationality);
                    }
                });
            }
            if (edit_organisation != null) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        user.setCompany(edit_organisation);
                    }
                });
            }
            if (edit_designation != null) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        user.setDesignation(edit_designation);
                    }
                });
            }
            performUpdate();
        }
    }

    private void setProfileValues() {
        name.setText(user.getName());
        email.setText(user.getId());
        nationality.setText(user.getNationality());
        contact_number.setText(user.getContactNumber());
        organisation.setText(user.getCompany());
        designation.setText(user.getDesignation());
    }

    private void setEditValues() {
        nameTV.setText(user.getName());
        emailTV.setText(user.getId());
        nationalityTV.setText(user.getNationality());
        contactTV.setText(user.getContactNumber());
        organisationTV.setText(user.getCompany());
        designationTV.setText(user.getDesignation());
    }

    private void performUpdate() {
        Log.i("AWAIS1", "URL= " + Api.urlJogetCRUD());
        user.performUserUpdation(user, Api.urlJogetCRUD(), new StatusCallBack() {
            @Override
            public void success(JSONObject response) {
                Log.i("AWAIS1", " status(success)=" + response.toString());
                if (response.has("status")) {
                    String status = response.optString("status");
                    if (status.equalsIgnoreCase("1")) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(user);
                        realm.commitTransaction();
                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Update Unsuccessfull", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Update Unsuccessfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(String response) {
                Log.i("AWAIS1", " status(failure)=" + response.toString());
                Log.i(MMAConstants.TAG, "performReg failure=" + response);
                Toast.makeText(getContext(), "Update Failure. Please, Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
