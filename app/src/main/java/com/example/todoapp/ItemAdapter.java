package com.example.todoapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

// responsable for displaying the data from the model into each row
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener onClickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = onClickListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //use layout inflator to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        // wrap it inside a viewHolder and return it
        return new viewHolder(todoView);
    }

    // responable for binding data to a particular view Holder
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        // grab item at the  position
        String item = items.get(position);
        // bind item into the specified viewholder
        holder.bind(item);
    }

    // tells the recycler how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //container to provide easy access to
    class viewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // updates the view inside of the view holder with this string data
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //when item is long pressed we are notifying the listener what position it is in
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
