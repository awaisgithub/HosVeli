package com.od.hrdf;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.od.hrdf.BOs.Event;
import com.od.hrdf.BOs.Speaker;
import com.od.hrdf.BOs.User;
import com.od.hrdf.Utils.Util;
import com.od.hrdf.loginregistration.LoginRegistrationActivity;
import io.realm.Realm;
import io.realm.RealmQuery;

public class LandingActivity extends AppCompatActivity {
    private Realm realm;
    String link;
    private String jsonResponse;
    private Event event;
    private Speaker speaker;
    private User user;
    private String bitbucketPullTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        event = new Event();
        speaker = new Speaker();
        RealmQuery query;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        realm = Realm.getDefaultInstance();
        User user = User.getCurrentUser(realm);

        // Added by mahmoor
        gotoActivity(Util.Activity.LOGIN_REG);

        //   WORKING
/*
        Log.i(HRDFConstants.TAG, Api.urlExhibitor("8c253bfe-7f000010-9bb09e10-ebd2fe00"));
        query = realm.where(Exhibitor.class).equalTo("id", "8c253bfe-7f000010-9bb09e10-ebd2fe00");
        Exhibitor.fetchExhibitor(this, realm, Api.urlExhibitor("8c253bfe-7f000010-9bb09e10-ebd2fe00"), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "!!!!RESULTLIST!!!!= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, " FAIL----FAIL-----FAIL== " + e.toString());
            }
        });

        Log.i(HRDFConstants.TAG, Api.urlSponsor("8be4c14b-7f000010-9bb09e10-8cc19433"));
        query = realm.where(Sponsor.class).equalTo("id", "8be4c14b-7f000010-9bb09e10-8cc19433");
        Sponsor.fetchSponsor(this, realm, Api.urlSponsor("8be4c14b-7f000010-9bb09e10-8cc19433"), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "!!!!RESULTLIST!!!!= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, " FAIL----FAIL-----FAIL== " + e.toString());

            }
        });

        Log.i(HRDFConstants.TAG, Api.urlUserList("farooq.zaman@me.com"));
        query = realm.where(User.class).equalTo("email", "farooq.zaman@me.com");
        User.fetchUser(this, realm, Api.urlUserList("farooq.zaman@me.com"), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "!!!!RESULTLIST!!!!= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, " FAIL----FAIL-----FAIL== " + e.toString());

            }
        });

        query = realm.where(Speaker.class);
        Speaker.fetchAllSpeakers(this, realm, Api.urlSpeakerList(), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "!!!!RESULTLIST!!!!= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, " FAIL----FAIL-----FAIL== " + e.toString());
            }
        });

        performUserRegistration

        query = realm.where(Speaker.class).equalTo("event", "b3358590-7f000010-9bb09e10-17cd6d26");
        Speaker.fetchEventSpeakers(this, realm, Api.urlEventSpeakerList("b3358590-7f000010-9bb09e10-17cd6d26"), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "@@@@@@@@@@@@EVENTSPEAKER@@@@@@@@= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, " Event Speaker FAIL----FAIL-----FAIL== " + e.toString());
            }
        });

        query = realm.where(Sponsor.class).equalTo("event", "6eaff16c-7f000010-9bb09e10-e19925f5");
        Sponsor.fetchEventSponsors(this, realm, Api.urlEventSponsorsList("6eaff16c-7f000010-9bb09e10-e19925f5"), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "@@@@@@@@@@@@EVENTSPONSOR@@@@@@@@= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, " Event Sponsor FAIL-----FAIL-----FAIL== " + e.toString());
            }
        });

        Log.i(HRDFConstants.TAG, Api.urlEventExhibitorsList("6eaff16c-7f000010-9bb09e10-e19925f5"));
        query = realm.where(Exhibitor.class).equalTo("event", "6eaff16c-7f000010-9bb09e10-e19925f5");
        Exhibitor.fetchEventExhibitors(this, realm, Api.urlEventExhibitorsList("6eaff16c-7f000010-9bb09e10-e19925f5"), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "@@@@@@@@@@@@EVENTExhibitor@@@@@@@@= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, " Event Sponsor FAIL-----FAIL-----FAIL== " + e.toString());
            }
        });

        query = realm.where(Article.class);
        Article.fetchArticles(this, realm, Api.urlArticleList(), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "@@@@@@@@@@@@EVENTArticle@@@@@@@@= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, " Article FAIL-----FAIL-----FAIL== " + e.toString());
            }
        });

        //GetEventLocation
        try {
            Event.fetchEventLocation(this, Api.urlReverseGeoCoding("KLCC"), "KLCC", new LocationCallBack() {
                @Override
                public void locationCordinates(String cordinates) {

                }

                @Override
                public void locationFail(Exception e) {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }




//ENDofWorking



        Event.bookEvent(this, Api.urlJogetCRUD(), new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {

            }

            @Override
            public void fetchDidFail(Exception e) {

            }
        });



        query = realm.where(Event.class);
        Event.fetchAllEvents(this, realm, Api.urlEventList(), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "!!!!RESULTLIST!!!!= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, " FAIL----FAIL-----FAIL== " + e.toString());
            }
        });


        */


        /*
        //Data-Type Issue while Fetching
        Log.i(HRDFConstants.TAG, Api.urlUserEventList("farahrohaiza@hrdf.com.my"));
        query = realm.where(Event.class).equalTo("userObj.email", "farahrohaiza@hrdf.com.my");
        Event.fetchUserEvents(this, realm, Api.urlUserEventList("farahrohaiza@hrdf.com.my"), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                //         Log.i(HRDFConstants.TAG, "!!!!RESULT LIST!!!!= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, " FAIL----FAIL-----FAIL== " + e.toString());
            }
        });
*/


/*                              //ORGANIZATION//
        Log.i(HRDFConstants.TAG, "ORganiZATIOn   =   =  "+ Api.urlAboutHRDF());
         query = realm.where(Organization.class);
        Organization.fetchAboutHRDF(this, realm, Api.urlAboutHRDF(), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "!!!!RESULT LIST   YES!!!!= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, "!!!!NO   ===   !!!!= " + e.toString());
            }
        });
*/

        /*
                                //FLOORPLAN//
        Log.i(HRDFConstants.TAG, "Floor PLan   =   = " + Api.urlEventFloorplan("6ebe9417-7f000010-9bb09e10-63d8d7e4"));
        query = realm.where(Floorplan.class).equalTo("event","6ebe9417-7f000010-9bb09e10-63d8d7e4");
        Floorplan.fetchEventFloorPlan(this, realm, Api.urlEventFloorplan("6ebe9417-7f000010-9bb09e10-63d8d7e4"), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "!!!!RESULT LIST   YES!!!!= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, "!!!!NO   ===   !!!!= " + e.toString());
            }
        });
*/

 /*                             //AGENDA
       Log.i(HRDFConstants.TAG, "AGENDA =    = " + Api.urlEventAgenda("6eaff16c-7f000010-9bb09e10-e19925f5"));
        query = realm.where(Agenda.class).equalTo("event", "6eaff16c-7f000010-9bb09e10-e19925f5");
        Agenda.fetchEventAgenda(this, realm, Api.urlEventAgenda("6eaff16c-7f000010-9bb09e10-e19925f5"), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "!!!!RESULT LIST   YES!!!!= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, "!!!!NO   ===   !!!!= " + e.toString());
            }
        });
        */

        /*                        //FORGOT PASSWORD//
        User.forgotPassword(this, "http://www.mypams.net/jw/web/json/plugin/org.joget.hrdf.forgetPasswordWS/service", "wengyew@opendynamics.com.my");
        */

//        Log.i(HRDFConstants.TAG,Api.urlJogetCRUD());
//
//        User.performUserRegistration(this, Api.urlJogetCRUD(), new FetchCallBack() {
//            @Override
//            public void fetchDidSucceed(RealmResults fetchedItems) {
//                Log.i(HRDFConstants.TAG, "WORKING   = " + fetchedItems.toString());
//            }
//
//            @Override
//            public void fetchDidFail(Exception e) {
//                Log.i(HRDFConstants.TAG, " FAILURE in FETCHDIDFAIL =  " + e.toString());
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Added By Mahmoor

    private void gotoActivity(Util.Activity activity) {

        switch (activity) {
            case LOGIN_REG:
                Intent intent = new Intent(this, LoginRegistrationActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
