package com.example.wifiology;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> {

    NodeData[] nodesList;
    Context context;

    public NodeAdapter(NodeData[] nets, Context con) {
        nodesList = nets;
        context = con;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.networks_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final NodeData node = nodesList[position];
        holder.nameText.setText(node.getName());
        holder.subLayout.setVisibility(View.GONE);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (node.getId() >= 0) {
                    Intent intent = new Intent(context, DataActivity.class);
                    intent.putExtra(NodeActivity.EXTRA_NODE_ID, node.getId());
                    intent.putExtra(NodeActivity.EXTRA_NODE_LOC, node.getLocation());
                    intent.putExtra(NodeActivity.EXTRA_NODE_DESC, node.getDescription());
                    intent.putExtra(NodeActivity.EXTRA_NODE_NAME, node.getName());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return nodesList.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameText;//, timeBegin, timeEnd, location;
        public LinearLayout layout;
        public LinearLayout subLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.networkSSIDText);
            layout = itemView.findViewById(R.id.linearLayoutNetworks);
            subLayout = itemView.findViewById(R.id.linearLayoutNetworksExpanded);
        }
    }
}