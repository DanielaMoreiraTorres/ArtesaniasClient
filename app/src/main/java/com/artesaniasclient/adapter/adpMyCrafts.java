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

import java.util.ArrayList;

public class adpMyCrafts extends RecyclerView.Adapter<adpMyCrafts.ViewHolder>
        implements View.OnClickListener {

    private Context ccontext;
    private View.OnClickListener listener;

    private ArrayList<Craft> lista;

    public adpMyCrafts(Context context, ArrayList<Craft> lista) {
        ccontext = context;
        this.lista=lista;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null)
            listener.onClick(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namecraft, price, description;
        ImageView imageurl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namecraft = (TextView) itemView.findViewById(R.id.txtNameCraft2);
            price = (TextView) itemView.findViewById(R.id.txtPrice2);
            description = (TextView) itemView.findViewById(R.id.txtDescription2);
            imageurl = (ImageView) itemView.findViewById(R.id.imgCraft2);
        }
    }

    @NonNull
    @Override
    public adpMyCrafts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_my_crafts,null,false);
        view.setOnClickListener(this);
        return new adpMyCrafts.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adpMyCrafts.ViewHolder holder, int position) {
        try {
            Craft craft = lista.get(position);
            holder.namecraft.setText(craft.getNamecraft());
            holder.price.setText(Double.toString(craft.getPrice()));
            holder.description.setText(craft.getDescription());
            GlideApp.with(ccontext)
                    .load(craft.getImageurl())
                    .into(holder.imageurl);
        }catch (Exception e){
            String res=e.toString();
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

}
