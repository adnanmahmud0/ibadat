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

public class NamazAdapter extends RecyclerView.Adapter<NamazAdapter.MyViewHolder>{
    private Context context;
    private List<NamazModel> namazModelList;

    public NamazAdapter(Context context) {
        this.context = context;
        namazModelList = new ArrayList<>();
    }

    public void add(NamazModel namazModel){
        namazModelList.add(namazModel);
        notifyDataSetChanged();
    }

    public void clear(){
        namazModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.day_views,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NamazModel namazModel= namazModelList.get(position);
        holder.bindView(namazModel);
    }

    @Override
    public int getItemCount() {
        return namazModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView fajr, sunrise, dhuhr, asr, sunset, maghrib, isha, midnight, date;

        public  MyViewHolder(@NonNull View itemView){
            super(itemView);
            fajr = itemView.findViewById(R.id.fajrTv);
            sunrise = itemView.findViewById(R.id.sunriseTv);
            dhuhr = itemView.findViewById(R.id.dhuhrTv);
            asr = itemView.findViewById(R.id.asrTv);
            sunset = itemView.findViewById(R.id.sunsetTv);
            maghrib = itemView.findViewById(R.id.maghribTv);
            isha = itemView.findViewById(R.id.ishaTv);
            midnight = itemView.findViewById(R.id.midnightTv);
            date = itemView.findViewById(R.id.dateTv);
        }

        public void bindView(NamazModel namazModel) {
            fajr.setText(namazModel.getFajr());
            sunrise.setText(namazModel.getSunrise());
            dhuhr.setText(namazModel.getDhuhr());
            asr.setText(namazModel.getAsr());
            sunset.setText(namazModel.getSunset());
            maghrib.setText(namazModel.getMaghrib());
            isha.setText(namazModel.getIsha());
            midnight.setText(namazModel.getMidnight());
            date.setText(namazModel.getDate());
        }
    }
}
