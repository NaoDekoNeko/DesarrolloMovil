package com.example.myapplication3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import EMPRESA.ANDROID.DAO.ProductoDAO;

public class Adaptador extends BaseAdapter {
    Context context;
    ArrayList<ProductoDAO> compras = new ArrayList<ProductoDAO>();
    LayoutInflater inflater;
    public Adaptador(Context ctx,ArrayList<ProductoDAO> compras){
        this.context = ctx;
        this.compras = compras;
        inflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return compras.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.compaslistview,null);
        TextView lblmensaje = (TextView) view.findViewById(R.id.lblmensaje);
        ImageView imgicono = (ImageView) view.findViewById(R.id.imgicono);
        lblmensaje.setText(compras.get(i).getMensaje());
        return view;
    }
}
