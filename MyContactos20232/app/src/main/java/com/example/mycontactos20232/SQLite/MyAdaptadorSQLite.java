package com.example.mycontactos20232.SQLite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycontactos20232.MainActivity;
import com.example.mycontactos20232.R;

public class MyAdaptadorSQLite extends BaseAdapter {
    Context context;
    MyAdaptadorSQLite(Context c){
        context = c;
    }
    @Override
    public int getCount(){return MainActivity.miscontactosSQLite.size();}
    @Override
    public Object getItem(int position){
        return MainActivity.miscontactosSQLite.get(position);
    }
    @Override
    public long getItemId(int position){return 0;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String auxTipo;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.misfilas,null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtminombre = convertView.findViewById(R.id.txtminombre);
            viewHolder.txtmialias = convertView.findViewById(R.id.txtmialias);
            viewHolder.imgImagen = convertView.findViewById(R.id.imgimagen);
            viewHolder.txtTipo = convertView.findViewById(R.id.txtTipo);

            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        String nombre = MainActivity.miscontactosSQLite.get(position).getNombre();
        String alias = MainActivity.miscontactosSQLite.get(position).getAlias();
        int tipo = MainActivity.miscontactosSQLite.get(position).getTipo();
        holder.txtminombre.setText(nombre);
        holder.txtmialias.setText(alias);

        switch (tipo) {
            case 1:
                auxTipo = "Vendedor";
                holder.imgImagen.setImageResource(R.drawable.sample1);
                break;
            case 0:
                auxTipo = "Cliente";
                holder.imgImagen.setImageResource(R.drawable.sample2);
                break;
            default:
                auxTipo = "No definido";
                holder.imgImagen.setImageResource(R.drawable.sampledef);
                break;
        }

        holder.txtTipo.setText(auxTipo);
        return  convertView;
    }

    static class ViewHolder {
        TextView txtminombre;
        TextView txtmialias;
        ImageView imgImagen;
        TextView txtTipo;
    }
}
