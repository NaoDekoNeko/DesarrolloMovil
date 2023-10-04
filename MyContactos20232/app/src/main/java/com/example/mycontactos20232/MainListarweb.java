package com.example.mycontactos20232;

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

import com.example.mycontactos20232.API.ContactoAPI;
import com.example.mycontactos20232.API.RetrofitContacto;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainListarweb extends AppCompatActivity {
    ContactoAPI contactoAPI;
    ListView lvcontactoweb;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<Contacto> cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listarweb);
        lvcontactoweb = ( ListView) findViewById(R.id.lvcontactoweb);
        // conexion a la nube
        Retrofit retrofit = RetrofitContacto.getInstance();
        contactoAPI = retrofit.create(ContactoAPI.class);
        cargardatos();
        registerForContextMenu(lvcontactoweb);
    }
    private void cargardatos(){
        compositeDisposable.add(contactoAPI.getContacto().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<List<Contacto>>() {
                    @Override
                    public void accept(List<Contacto> contactos) throws Exception {
                        mostrardatos(contactos);
                        cs = contactos;
                    }
                }));

    }
    private void mostrardatos(List<Contacto> contactos){
        MiAdaptadorContacto miAdaptadorContacto = new MiAdaptadorContacto(this,contactos);
        lvcontactoweb.setAdapter(miAdaptadorContacto);
    }
    @Override
    protected void onStop(){
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Esto limpia todas las actividades excepto MainActivity de la pila
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position; // Obtén la posición del elemento seleccionado

        switch (item.getItemId()) {
            case R.id.edit: // Suponiendo que 'edit' es el id de la opción de edición en tu menú
                // Aquí puedes obtener el contacto que deseas editar usando el índice
                Contacto contacto = cs.get(index); // Suponiendo que 'cs' es tu lista de contactos
                // Luego puedes pasar este contacto a tu actividad/fragmento de edición
                Intent intent = new Intent(MainListarweb.this, EditContactActivity.class); // Suponiendo que 'EditContactActivity' es tu actividad de edición
                intent.putExtra("contactoid", contacto.getIdcontacto());
                intent.putExtra("save_type", 2);
                startActivity(intent);
                break;

            case R.id.delete: // Suponiendo que 'delete' es el id de la opción de eliminación en tu menú
                // Aquí puedes obtener y eliminar el contacto usando el índice
                Contacto contactoEliminar = cs.get(index);
                Call<Void> call = contactoAPI.deleteContacto(contactoEliminar.getIdcontacto());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MainListarweb.this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                            cargardatos();  // Actualiza los datos después de eliminar
                        } else {
                            // Maneja el caso en que la respuesta no sea exitosa
                            Toast.makeText(MainListarweb.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Imprime el mensaje de la excepción
                        String errorMsg = "Error al realizar la llamada a la API: " + t.getMessage();
                        Toast.makeText(MainListarweb.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }

                });

                break;

            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
}
