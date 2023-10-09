package com.example.mycontactos20232.Lista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mycontactos20232.Contacto;
import com.example.mycontactos20232.EditContactActivity;
import com.example.mycontactos20232.MainActivity;
import com.example.mycontactos20232.R;

public class ListarContacto extends AppCompatActivity {
    ListView lvcontactos;
    MyAdaptador myAdaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contacto);
        lvcontactos = (ListView)findViewById(R.id.lvcontactos);
        myAdaptador = new MyAdaptador(this);
        lvcontactos.setAdapter(myAdaptador);
        lvcontactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contacto c = MainActivity.miscontactos.get(i);
                String nombre = c.getNombre();
                String alias = c.getAlias();
                int id = c.getIdcontacto();
                String mensaje = "Nombre:"+nombre+" Alias:"+alias+" id:"+id;
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
            }
        });
        registerForContextMenu(lvcontactos);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);  // 'R.menu.context_menu' es el ID de tu archivo XML de menú contextual
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:  // 'R.id.edit' es el ID de tu elemento de menú para editar
                // Aquí va el código para editar el contacto
                Intent intentEdit = new Intent(getApplicationContext(), EditContactActivity.class);
                intentEdit.putExtra("contact_position", info.position);  // Pasas la posición como un extra al Intent
                intentEdit.putExtra("save_type", 0);
                startActivity(intentEdit);
                return true;
            case R.id.delete:  // 'R.id.delete' es el ID de tu elemento de menú para eliminar
                // Aquí va el código para eliminar el contacto
                Contacto contactoEliminar = MainActivity.miscontactos.get(info.position);  // Obtén el contacto a eliminar
                MainActivity.miscontactos.remove(info.position);  // Elimina el contacto de la lista
                myAdaptador = new MyAdaptador(this);  // Crea un nuevo adaptador con los datos actualizados
                lvcontactos.setAdapter(myAdaptador);  // Establece el nuevo adaptador en tu ListView
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Esto limpia todas las actividades excepto MainActivity de la pila
        startActivity(intent);
    }
}