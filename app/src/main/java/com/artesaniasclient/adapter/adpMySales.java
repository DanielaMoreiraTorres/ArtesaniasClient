package com.artesaniasclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.artesaniasclient.R;
import com.artesaniasclient.certificaciones.GlideApp;
import com.artesaniasclient.model.Craft;
import com.artesaniasclient.model.Order;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class adpMySales extends RecyclerView.Adapter<adpMySales.ViewHolder>
        implements View.OnClickListener {

    private Context ccontext;
    private View.OnClickListener listener;
    private ArrayList<Craft> listCraft;
    private ArrayList<Order> lista;

    public adpMySales(Context context, ArrayList<Order> lista, ArrayList<Craft> listCraft) {
        ccontext = context;
        this.lista = lista;
        this.listCraft = listCraft;
    }


    @Override
    public void onClick(View view) {
        if(listener!=null)
            listener.onClick(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public adpMySales.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_sales,null,false);
        view.setOnClickListener(this);
        return new adpMySales.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adpMySales.ViewHolder holder, int position) {
        try {
            Order order = lista.get(position);
            for(int c = 0; c < listCraft.size(); c++){
                Craft craftModel = listCraft.get(c);
                if(craftModel.getId().equals(order.getCraft())){
                    holder.namecraft.setText(craftModel.getNamecraft());
                    GlideApp.with(ccontext)
                            .load(craftModel.getImageurl())
                            .into(holder.imageurl);
                    break;
                }
            }
            holder.dateorder.setText(order.getOrderdate());
            holder.quantity.setText(order.getQuantity().toString());
            holder.total.setText(Double.toString(order.getPrice()));
            holder.deliverydate.setText(order.getDeliverydate());
            holder.state.setText(order.getState());
        }catch (Exception e){
            String res=e.toString();
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namecraft, dateorder, quantity, total, deliverydate, state;
        ImageView imageurl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namecraft = (TextView) itemView.findViewById(R.id.txtNameCraft2);
            dateorder = (TextView) itemView.findViewById(R.id.txtDateOrder2);
            quantity = (TextView) itemView.findViewById(R.id.txtCantidadOrder2);
            total = (TextView) itemView.findViewById(R.id.txtTotalOrder2);
            deliverydate = (TextView) itemView.findViewById(R.id.txtDeliveryDate2);
            state = (TextView) itemView.findViewById(R.id.txtState2);
            imageurl = (ImageView) itemView.findViewById(R.id.imgCraftOrder2);
        }
    }
}
