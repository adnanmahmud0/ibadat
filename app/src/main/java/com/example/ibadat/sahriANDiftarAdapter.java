package com.example.ibadat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class sahriANDiftarAdapter extends RecyclerView.Adapter<sahriANDiftarAdapter.MyViewHolder>{
    private Context context;
    private List<sahriANDiftarModel> sahriANDiftarModelList;

    public sahriANDiftarAdapter(Context context) {
        this.context = context;
        sahriANDiftarModelList = new ArrayList<>();
    }

    public void add(sahriANDiftarModel sahriANDiftarModel){
        sahriANDiftarModelList.add(sahriANDiftarModel);
        notifyDataSetChanged();
    }

    public void clear(){
        sahriANDiftarModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sahriandiftar_views,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        sahriANDiftarModel sahriANDiftarModel = sahriANDiftarModelList.get(position);
        holder.bindView(sahriANDiftarModel);
    }

    @Override
    public int getItemCount() {
        return sahriANDiftarModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView fajr, maghrib, date;

        public  MyViewHolder(@NonNull View itemView){
            super(itemView);
            fajr = itemView.findViewById(R.id.fajrTv);

            maghrib = itemView.findViewById(R.id.maghribTv);

            date = itemView.findViewById(R.id.dateTv);
        }

        public void bindView(sahriANDiftarModel sahriANDiftarModel) {
            fajr.setText(sahriANDiftarModel.getFajr());

            maghrib.setText(sahriANDiftarModel.getMaghrib());

            date.setText(sahriANDiftarModel.getDate());
        }
    }
}
