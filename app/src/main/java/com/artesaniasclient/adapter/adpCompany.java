package com.artesaniasclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.artesaniasclient.R;
import com.artesaniasclient.model.Company;

import java.util.ArrayList;

public class adpCompany extends RecyclerView.Adapter<adpCompany.ViewHolder>
        implements View.OnClickListener {

    private Context ccontext;
    private View.OnClickListener listener;
    private ArrayList<Company> lista;

    public adpCompany(Context context, ArrayList<Company> lista) {
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
        TextView bussinessname, ruc, city, address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bussinessname = (TextView) itemView.findViewById(R.id.txtbussinessname);
            ruc = (TextView) itemView.findViewById(R.id.txtruc);
            city = (TextView) itemView.findViewById(R.id.txtcity);
            address = (TextView) itemView.findViewById(R.id.txtaddress);
        }
    }

    @NonNull
    @Override
    public adpCompany.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_company,null,false);
        view.setOnClickListener(this);
        return new adpCompany.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adpCompany.ViewHolder holder, int position) {
        try {
            double uno=0.0;
            Company company = lista.get(position);
            holder.bussinessname.setText(company.getBusinessname());
            holder.ruc.setText(company.getRuc());
            holder.city.setText(company.getCity());
            holder.address.setText(company.getAddress());
        }catch (Exception e){
            String res=e.toString();
        }
    }

    @Override
    public int getItemCount() {return lista.size(); }
}
