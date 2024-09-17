package com.example.xdygq3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {

    public static class tuple {
        String first;
        String second;
        String third;
        tuple(String _f, String _s, String _t) {
            this.first = _f;
            this.second = _s;
            this.third = _t;
        }
    }

    private List<tuple> data;
    private Context context;

    public MyAdapter2(Context context, List<tuple> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        tuple tmp = data.get(position);
        holder.textView.setText(tmp.first);
        holder.editText.setText(tmp.second);
        holder.editText.setTag(tmp.third);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        EditText editText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView2);
            editText = itemView.findViewById(R.id.editText2);
        }
    }
}
