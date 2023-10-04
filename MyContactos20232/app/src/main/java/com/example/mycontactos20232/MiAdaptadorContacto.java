package com.example.mycontactos20232;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MiAdaptadorContacto  extends BaseAdapter {
    Context context;
    List<Contacto> contactos;
    MiAdaptadorContacto(Context c,List<Contacto> cs){
        context = c;
        this.contactos = cs;
    }
    @Override
    public int getCount(){return contactos.size();}
    @Override
    public Object getItem(int position){
        return contactos.get(position);
    }
    @Override
    public long getItemId(int position){return 0;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String auxTipo;
        int tipo = contactos.get(position).getTipo();
        LayoutInflater layoutInflater;
        View fila;
        if (convertView==null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            fila = layoutInflater.inflate(R.layout.misfilas,null);
        }else{
            fila = convertView;
        }

        TextView txtminombre = fila.findViewById(R.id.txtminombre);
        TextView txtmialias = fila.findViewById(R.id.txtmialias);
        ImageView imgImagen = fila.findViewById(R.id.imgimagen);
        TextView txttipo = fila.findViewById(R.id.txtTipo);
        String nombre = contactos.get(position).getNombre();
        String alias = contactos.get(position).getAlias();
        txtminombre.setText(nombre);
        txtmialias.setText(alias);
        imgImagen.setImageResource(R.drawable.sample);
        switch (tipo) {
            case 1:
                auxTipo = "Vendedor";
                break;
            case 0:
                auxTipo = "Cliente";
                break;
            default:
                auxTipo = "No definido";
                break;
        }

        txttipo.setText(auxTipo);
        return  fila;
    }
}
