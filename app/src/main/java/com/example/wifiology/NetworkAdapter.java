package com.example.wifiology;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.ViewHolder>{

    public static final String KEY_SSID = "ssid";

    NetworkData[] networksList;
    Context context;

    public NetworkAdapter(NetworkData[] nets, Context con){
        networksList = nets;
        context = con;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.networks_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final NetworkData network = networksList[position];
        holder.ssidText.setText(network.getSsid());
        if (network.getEarliestTime() != null) {
            String textBegin = "Time of first measurement: " + network.getEarliestTime().toString();
            holder.timeBegin.setText(textBegin);
        }
        if (network.getLatestTime() != null) {
            String textEnd = "Time of last measurement: " + network.getLatestTime().toString();
            holder.timeEnd.setText(textEnd);
        }
        boolean expanded = network.getExpanded();
        holder.subLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                network.setExpanded(!network.getExpanded());
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount(){
        return networksList.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ssidText;
        public TextView timeBegin;
        public TextView timeEnd;
        public LinearLayout layout;
        public LinearLayout subLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ssidText = itemView.findViewById(R.id.networkSSIDText);
            timeBegin = itemView.findViewById(R.id.timeBegin);
            timeEnd = itemView.findViewById(R.id.timeEnd);
            layout = itemView.findViewById(R.id.linearLayoutNetworks);
            subLayout = itemView.findViewById(R.id.linearLayoutNetworksExpanded);
        }
    }

}
