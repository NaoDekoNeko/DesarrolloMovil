package com.example.mycontactos20232;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdaptador extends BaseAdapter {
    Context context;
    MyAdaptador(Context c){
        context = c;
    }
    @Override
    public int getCount(){return MainActivity.miscontactos.size();}
    @Override
    public Object getItem(int position){
        return MainActivity.miscontactos.get(position);
    }
    @Override
    public long getItemId(int position){return 0;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.misfilas,null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtminombre = convertView.findViewById(R.id.txtminombre);
            viewHolder.txtmialias = convertView.findViewById(R.id.txtmialias);
            viewHolder.imgImagen = convertView.findViewById(R.id.imgimagen);

            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        String nombre = MainActivity.miscontactos.get(position).getNombre();
        String alias = MainActivity.miscontactos.get(position).getAlias();
        holder.txtminombre.setText(nombre);
        holder.txtmialias.setText(alias);

        return  convertView;
    }

    static class ViewHolder {
        TextView txtminombre;
        TextView txtmialias;
        ImageView imgImagen;
    }
}
