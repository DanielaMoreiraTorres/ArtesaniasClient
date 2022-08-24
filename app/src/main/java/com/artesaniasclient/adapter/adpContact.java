package com.artesaniasclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.artesaniasclient.R;
import com.artesaniasclient.model.Contacts;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class adpContact extends RecyclerView.Adapter<adpContact.ViewHolder> {

    private List<Contacts> data;
    public adpContact(List<Contacts> data){this.data = data;}

    public static boolean showShimmer = true;
    int cantShimmer = 4;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmerFrameLayout;
        TextView txtContact;
        ImageView btnShimmer;
        TextView txtEmail;
        ImageView btnShimmer1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer);
            txtContact = (TextView) itemView.findViewById(R.id.txtContactName);
            btnShimmer = itemView.findViewById(R.id.imageViewliPart);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            btnShimmer1 = itemView.findViewById(R.id.imageViewliEm);
        }

        public void add_data(Contacts valor) {
            txtContact.setText(valor.getName());
            txtEmail.setText(valor.getEmail());
        }
    }

    @NonNull
    @Override
    public adpContact.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adpContact.ViewHolder holder, int position) {
        try {
            if(showShimmer){
                holder.shimmerFrameLayout.startShimmer();
            }
            else
            {
                holder.shimmerFrameLayout.stopShimmer();
                holder.shimmerFrameLayout.setShimmer(null);
                holder.txtContact.setBackground(null);
                holder.txtEmail.setBackground(null);
                holder.add_data(data.get(position));
            }
        }
        catch (Exception e){
        }
    }

    @Override
    public int getItemCount() {
        try{
            return showShimmer ? cantShimmer : data.size();
        }catch (Exception e) {return cantShimmer;}
    }
}
