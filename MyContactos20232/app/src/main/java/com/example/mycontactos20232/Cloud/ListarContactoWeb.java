package com.example.mycontactos20232.Cloud;

import static com.example.mycontactos20232.MainActivity.codigo;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycontactos20232.Cloud.API.ContactoAPI;
import com.example.mycontactos20232.Cloud.API.RetrofitContacto;
import com.example.mycontactos20232.Contacto;
import com.example.mycontactos20232.EditContactActivity;
import com.example.mycontactos20232.MainActivity;
import com.example.mycontactos20232.R;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ListarContactoWeb extends AppCompatActivity {
    ListView lvcontactoweb;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MyAdaptadorWeb myAdaptadorWeb;
    ContactoAPI contactoAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listarweb);
        lvcontactoweb = (ListView) findViewById(R.id.lvcontactoweb);

        Retrofit retrofit = RetrofitContacto.getInstance();
        contactoAPI = retrofit.create(ContactoAPI.class);
        cargardatos();
        lvcontactoweb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contacto c = MainActivity.miscontactosweb.get(i); // Usamos la lista local
                String nombre = c.getNombre();
                String alias = c.getAlias();
                int id = c.getIdcontacto();
                String codigo = c.getCodigo();
                String mensaje = "Nombre:" + nombre + " Alias:" + alias + " id:" + id + " codigo: " + codigo;
                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });
        registerForContextMenu(lvcontactoweb);
    }

    private void cargardatos() {
        ContactoAPI contactoAPI = RetrofitContacto.getInstance().create(ContactoAPI.class);
        compositeDisposable.add(contactoAPI.getContacto().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<List<Contacto>>() {
                    @Override
                    public void accept(List<Contacto> contactos) throws Exception {
                        MainActivity.miscontactosweb.clear();
                        MainActivity.miscontactosweb.addAll(contactos);
                        mostrardatos();
                    }
                }));
    }

    private void mostrardatos() {
        myAdaptadorWeb = new MyAdaptadorWeb(this); // Pasamos la lista local al adaptador
        lvcontactoweb.setAdapter(myAdaptadorWeb);
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
        Contacto aux = MainActivity.miscontactosweb.get(info.position);
        String msgPriv = "Solo el telefono que agregó el contacto puede realizar cambios";
        switch (item.getItemId()) {
            case R.id.edit:
                if(codigo.equals(aux.getCodigo())) {
                    Intent intentEdit = new Intent(getApplicationContext(), EditContactActivity.class);
                    intentEdit.putExtra("contact_position", info.position);  // Pasas la posición como un extra al Intent
                    intentEdit.putExtra("save_type", 2);
                    startActivity(intentEdit);
                }
                else{
                    Toast.makeText(ListarContactoWeb.this, msgPriv, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.delete:
                if(codigo.equals(aux.getCodigo())) {
                    ContactoAPI contactoAPI = RetrofitContacto.getInstance().create(ContactoAPI.class);
                    Call<Void> deletecall = contactoAPI.deleteContacto(aux);

                    String successfulmsg = "Contacto Actualizado", failmsg = "Error al actualizar contacto";
                    deletecall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ListarContactoWeb.this, successfulmsg, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ListarContactoWeb.this, failmsg, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ListarContactoWeb.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    MainActivity.miscontactosweb.remove(aux);
                    myAdaptadorWeb.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(ListarContactoWeb.this, msgPriv, Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}