package com.od.mma.event.speaker.uploads;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.od.mma.API.Api;
import com.od.mma.BOs.Document;
import com.od.mma.BOs.SpeakerDocument;
import com.od.mma.CallBack.FetchCallBack;
import com.od.mma.R;

import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.od.mma.MMAApplication.realm;

public class SpeakerUploadsFragment extends Fragment implements SpeakerDocNotifier {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String eventId;
    private String speakerId;
    private View rootView;
    private RealmResults realmResults = null;

    public SpeakerUploadsFragment() {
        // Required empty public constructor
    }

    public static SpeakerUploadsFragment newInstance(String eventId, String speakerId) {
        SpeakerUploadsFragment fragment = new SpeakerUploadsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, eventId);
        args.putString(ARG_PARAM2, speakerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_PARAM1);
            speakerId = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_speaker_uploads, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        fetchSpeakerUploads();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.speaker_uploads_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        realmResults = SpeakerDocument.getSpeakerAllDocument(realm, speakerId);
        realmResults.addChangeListener(new RealmChangeListener<RealmResults>() {
            @Override
            public void onChange(RealmResults element) {
                if(element.size() > 0) {
                    hideMessage();
                }
            }
        });

        if(realmResults.size() < 1) {
            showMessage(R.string.sp_upload_not_available);
        }

        SpeakerUploadListAdapter speakerTopicListAdapter = new SpeakerUploadListAdapter(getActivity(), realmResults, this, true);
        recyclerView.setAdapter(speakerTopicListAdapter);
    }

    private void fetchSpeakerUploads() {
        RealmQuery query = realm.where(SpeakerDocument.class).equalTo("speaker", speakerId);
        SpeakerDocument.fetchSpeakerDocuments(getActivity(), realm, Api.urlDocumentIds(speakerId), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                for(int i=0; i<fetchedItems.size(); i++) {
                    fetchDocumentById(((SpeakerDocument)fetchedItems.get(0)).getId());
                }
            }

            @Override
            public void fetchDidFail(Exception e) {
                if(realmResults.size() < 1) {
                    showMessage(R.string.server_error);
                }
            }
        });
    }

    private void fetchDocumentById(final String docId) {
        RealmQuery query = realm.where(Document.class).equalTo("documentId", docId);
        Document.fetchDocument(getActivity(), realm, Api.urlDocumentList(docId), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
            }

            @Override
            public void fetchDidFail(Exception e) {
            }
        });
    }

    @Override
    public void downloadFile(String file, final String desc, final String type) {
    }

    private void showMessage(int message){
        RelativeLayout messageLayout = (RelativeLayout) rootView.findViewById(R.id.error_layout);
        TextView messageView = (TextView) rootView.findViewById(R.id.label);
        messageView.setText(message);
        messageLayout.setVisibility(View.VISIBLE);
    }

    private void hideMessage() {
        RelativeLayout messageLayout = (RelativeLayout) rootView.findViewById(R.id.error_layout);
        messageLayout.setVisibility(View.GONE);
    }
}
