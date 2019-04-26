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
        if (network.getBssids() != null && network.getBssids().size() > 0) {
            String res = "BSSIDs: ";
            for (int i = 0; i < network.getBssids().size(); i++){
                if (i > 0){
                    res += "            ";
                }
                res += network.getBssids().get(i);
                if (i < network.getBssids().size() -1) {
                    res += "\n";
                }
            }
            holder.bssidText.setText(res);
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

        public TextView ssidText,bssidText;//,location;
        public LinearLayout layout;
        public LinearLayout subLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ssidText = itemView.findViewById(R.id.networkSSIDText);
            bssidText = itemView.findViewById(R.id.bssids);
            layout = itemView.findViewById(R.id.linearLayoutNetworks);
            subLayout = itemView.findViewById(R.id.linearLayoutNetworksExpanded);
        }
    }

}
