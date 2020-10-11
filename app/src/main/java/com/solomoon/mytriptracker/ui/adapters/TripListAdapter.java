package com.solomoon.mytriptracker.ui.adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.solomoon.mytriptracker.App;
import com.solomoon.mytriptracker.R;
import com.solomoon.mytriptracker.models.Trip;
import com.solomoon.mytriptracker.ui.MapsActivity;
import com.solomoon.mytriptracker.utils.DateTimeConvertor;

import java.util.List;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {

    private SortedList<Trip> sortedList;

    public TripListAdapter() {

        sortedList = new SortedList<>(Trip.class, new SortedList.Callback<Trip>() {
            @Override
            public int compare(Trip o1, Trip o2) {
                if ((o2.getSartTimestamp() == o1.getSartTimestamp())) {
                    return 0;
                } else
                    return -1;
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Trip oldItem, Trip newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Trip item1, Trip item2) {
                return item1.getSartTimestamp() == item2.getSartTimestamp();
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TripViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        holder.bind(sortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public void setItems(List<Trip> trips) {
        sortedList.replaceAll(trips);
    }


    static class TripViewHolder extends RecyclerView.ViewHolder {

        TextView tripName;
        TextView tripTimeStamp;
        View delete;

        Trip trip;

        boolean silentUpdate;

        public TripViewHolder(@NonNull final View itemView) {
            super(itemView);

            tripName = itemView.findViewById(R.id.txtTripName);
            tripTimeStamp = itemView.findViewById(R.id.txtTripTimestamp);
            delete = itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(view -> MapsActivity.start((Activity) itemView.getContext(), trip.getId()));

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    App.getInstance().getDatabase().tripDao().delete(trip);
                }
            });

//            completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                    if (!silentUpdate) {
//                        note.done = checked;
//                        App.getInstance().getNoteDao().update(note);
//                    }
//                    updateStrokeOut();
//                }
//            });

        }

        public void bind(Trip trip) {
            this.trip = trip;

            tripName.setText(trip.getName());
            tripTimeStamp.setText(buildTripTimeStamp(trip));
            buildTripTimeStamp(trip);

        }

        private String buildTripTimeStamp(Trip trip) {
            if (trip.getEndTimestamp() == 0) {
                return "Started at: " + DateTimeConvertor.millisecToDateTime(trip.getSartTimestamp()) + " - Trip active now";
            } else {
                return "Started at: " + DateTimeConvertor.millisecToDateTime(trip.getSartTimestamp()) + " - "
                        + "Completed at: " + DateTimeConvertor.millisecToDateTime(trip.getEndTimestamp());
            }

        }

    }
}
