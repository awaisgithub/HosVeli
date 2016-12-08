package com.od.mma.event.exhibitor;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.od.mma.BOs.EventExhibitor;
import com.od.mma.MMAApplication;
import com.od.mma.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class EventExhibitorListAdapter extends RealmRecyclerViewAdapter<EventExhibitor, RecyclerView.ViewHolder> {
    private Context context;
    private OrderedRealmCollection<EventExhibitor> data;
    private ExhibitorListParentInterface exhibitorListParentInterface;
    private EventExhibitorDetailListAdapter eventExhibitorDetailListAdapter;
    private View view;

    public EventExhibitorListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<EventExhibitor> data, ExhibitorListParentInterface exhibitorListParentInterface, boolean autoUpdate) {
        super(context, data, true);
        this.data = data;
        this.exhibitorListParentInterface = exhibitorListParentInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_event_exhibitor, parent, false);
        return EventExhibitorViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final EventExhibitorViewHolder viewHolder = (EventExhibitorViewHolder) holder;
        final EventExhibitor eventExhibitor = data.get(position);
        viewHolder.title.setText(eventExhibitor.getExhibitorcategoryname());

        viewHolder.parent.setTag(false);
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExpanded = (boolean) view.getTag();
                if (isExpanded) {
                    view.setTag(false);
                    viewHolder.sessionListView.setVisibility(View.GONE);
                    viewHolder.arrowButton.setImageResource(R.drawable.expand);
                } else {
                    view.setTag(true);
                    viewHolder.sessionListView.setVisibility(View.VISIBLE);
                    viewHolder.arrowButton.setImageResource(R.drawable.expanded);
                    final RealmResults realmResults = EventExhibitor.getExhibitorForCategory(MMAApplication.realm, eventExhibitor.getExhibitorCategoryName(), eventExhibitor.getEvent());
                    new LoadSubItems(viewHolder.sessionListView, context, realmResults, exhibitorListParentInterface).execute();
                }
            }
        });
    }

    class LoadSubItems extends AsyncTask<Void, Void, EventExhibitorDetailListAdapter> {

        RecyclerView recyclerView;
        private Context context;
        private OrderedRealmCollection<EventExhibitor> data;
        private ExhibitorListParentInterface exhibitorListParentInterface;

        public LoadSubItems(RecyclerView recyclerView, Context context, OrderedRealmCollection<EventExhibitor> realmResults, ExhibitorListParentInterface exhibitorListParentInterface) {
            this.context = context;
            data = realmResults;
            this.exhibitorListParentInterface = exhibitorListParentInterface;
            this.recyclerView = recyclerView;
        }

        @Override
        protected EventExhibitorDetailListAdapter doInBackground(Void... voids) {
            EventExhibitorDetailListAdapter eventExhibitorDetailListAdapter = new EventExhibitorDetailListAdapter(context, data, exhibitorListParentInterface, true);

            return eventExhibitorDetailListAdapter;
        }

        @Override
        protected void onPostExecute(EventExhibitorDetailListAdapter eventExhibitorDetailListAdapter) {
            super.onPostExecute(eventExhibitorDetailListAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
                @Override
                protected int getExtraLayoutSpace(RecyclerView.State state) {
                    return 300;
                }
            };
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(eventExhibitorDetailListAdapter);
        }
    }
}
