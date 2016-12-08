package com.od.mma.event.agenda;

import android.content.Context;
import android.net.ParseException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.od.mma.BOs.Agenda;
import com.od.mma.MMAApplication;
import com.od.mma.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class AgendaListAdapter extends RealmRecyclerViewAdapter<Agenda, RecyclerView.ViewHolder> {
    private Context context;
    private OrderedRealmCollection<Agenda> data;
    private View view;

    public AgendaListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Agenda> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_agenda_list, parent, false);
        return AgendaListViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AgendaListViewHolder viewHolder = (AgendaListViewHolder) holder;
        Agenda agenda = data.get(position);
        String titleString = "";
        titleString = agenda.getTitle();
        if(titleString.isEmpty()) {
            titleString = agenda.getSession();
        }

//        titleString = titleString.toLowerCase();
//        titleString = titleString.substring(0,1).toUpperCase() + titleString.substring(1);
        viewHolder.title.setText(titleString);
        String startTime = null;
        String endTime = null;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("hhmm");
            Date timeObj = formatter.parse(agenda.getAgendaStartTime());
            startTime = new SimpleDateFormat("hh:mm a").format(timeObj);
            startTime = startTime.replace(".","");
            Date endTimeObj = formatter.parse(agenda.getAgendaEndTime());
            endTime = new SimpleDateFormat("hh:mm a").format(endTimeObj);
            endTime = endTime.replace(".","");
        } catch (ParseException e) {
            e.printStackTrace();
            startTime = "";
            endTime = "";
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            startTime = "";
            endTime = "";
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            startTime = "";
            endTime = "";
        }

        viewHolder.startTime.setText(startTime);
        viewHolder.endTime.setText(endTime);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        viewHolder.sessionListView.setLayoutManager(linearLayoutManager);
        RealmResults realmResults = agenda.getSessionsForAgenda(MMAApplication.realm);
        AgendaSessionAdapter agendaSessionAdapter = new AgendaSessionAdapter(context, realmResults, true);
        viewHolder.sessionListView.setAdapter(agendaSessionAdapter);

        if(realmResults.size() < 1) {
            viewHolder.arrowButton.setVisibility(View.GONE);
        } else {
            viewHolder.arrowButton.setVisibility(View.VISIBLE);
        }

        viewHolder.parent.setTag(false);
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExpanded = (boolean) view.getTag();
                if(isExpanded) {
                    view.setTag(false);
                    viewHolder.sessionListView.setVisibility(View.GONE);
                    viewHolder.arrowButton.setImageResource(R.drawable.expand);
                } else {
                    view.setTag(true);
                    viewHolder.sessionListView.setVisibility(View.VISIBLE);
                    viewHolder.arrowButton.setImageResource(R.drawable.expanded);
                }
            }
        });

        viewHolder.title.setTag(false);
        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExpanded = (boolean) view.getTag();
                if(isExpanded) {
                    view.setTag(false);
                    viewHolder.sessionListView.setVisibility(View.GONE);
                    viewHolder.arrowButton.setImageResource(R.drawable.expand);
                } else {
                    view.setTag(true);
                    viewHolder.sessionListView.setVisibility(View.VISIBLE);
                    viewHolder.arrowButton.setImageResource(R.drawable.expanded);
                }
            }
        });

        viewHolder.arrowButton.setTag(false);
        viewHolder.arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExpanded = (boolean) view.getTag();
                if(isExpanded) {
                    view.setTag(false);
                    viewHolder.sessionListView.setVisibility(View.GONE);
                    viewHolder.arrowButton.setImageResource(R.drawable.expand);
                } else {
                    view.setTag(true);
                    viewHolder.sessionListView.setVisibility(View.VISIBLE);
                    viewHolder.arrowButton.setImageResource(R.drawable.expanded);
                }
            }
        });
    }
}
