package com.od.mma.news;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.od.mma.API.Api;
import com.od.mma.CallBack.FetchCallBack;
import com.od.mma.R;
import com.od.mma.Utils.MMAConstants;
import com.od.mma.BOs.Article;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class NewsFragment extends Fragment implements NewsListAdapterInterface {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private Realm realm;

    public NewsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        initViews();
        fetchNews();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.news_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        RealmResults realmResults = Article.getArticlesResultController(realm);
        Log.i(MMAConstants.TAG, "size = "+realmResults.size());
        NewListAdapter newListAdapter = new NewListAdapter(getContext(), this, realmResults);
        recyclerView.setAdapter(newListAdapter);

    }

    private void fetchNews() {
        RealmQuery query = realm.where(Article.class);
        Article.fetchArticles(getActivity(), realm, Api.urlArticleList(), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                RealmResults realmResults = Article.getArticlesResultController(realm);
                //RealmResults realmResults = realm.where(Article.class).findAll();
                Log.i(MMAConstants.TAG, "fetchDidSucceed = "+realmResults.size());
            }

            @Override
            public void fetchDidFail(Exception e) {
            }
        });
    }

    @Override
    public void gotoDetailActivity(String id, View view) {
        Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
        intent.putExtra(NewsDetailsActivity.EXTRA_PARAM_ID, id);
        getActivity().startActivity(intent);

    }
}
