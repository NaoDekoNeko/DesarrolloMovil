package com.example.mycontactos20232.SQLite;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycontactos20232.Contacto;
import com.example.mycontactos20232.EditContactActivity;
import com.example.mycontactos20232.MainActivity;
import com.example.mycontactos20232.R;

public class ListarContactoSQLite extends AppCompatActivity {
    ListView lvcontactossql;
    MyAdaptadorSQLite myAdaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contacto_sqlite);

        MainActivity.miscontactosSQLite =MainActivity.dbHelper.obtenerTodosLosContactos();

        lvcontactossql = (ListView)findViewById(R.id.lvlistarcontactosqlite);
        myAdaptador = new MyAdaptadorSQLite(this);
        lvcontactossql.setAdapter(myAdaptador);

        lvcontactossql.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contacto c = MainActivity.miscontactosSQLite.get(i);
                String nombre = c.getNombre();
                String alias = c.getAlias();
                int id = c.getIdcontacto();
                String mensaje = "Nombre:"+nombre+" Alias:"+alias+" id:"+id;
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
            }
        });

        registerForContextMenu(lvcontactossql);  // Registra 'lvcontactos' para el menú contextual
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
                intentEdit.putExtra("save_type", 1);
                startActivity(intentEdit);
                return true;
            case R.id.delete:  // 'R.id.delete' es el ID de tu elemento de menú para eliminar
                // Aquí va el código para eliminar el contacto
                Contacto c = MainActivity.miscontactosSQLite.get(info.position);
                int resultado = MainActivity.dbHelper.eliminarContacto(c.getIdcontacto());
                if (resultado > 0) {
                    MainActivity.miscontactosSQLite.remove(info.position);  // Eliminas el contacto de la lista
                    myAdaptador.notifyDataSetChanged();  // Notificas al adaptador sobre los cambios en los datos
                } else {
                    String mensaje ="Error al eliminar contacto";
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.miscontactosSQLite = MainActivity.dbHelper.obtenerTodosLosContactos();
        myAdaptador.notifyDataSetChanged();
    }
}
