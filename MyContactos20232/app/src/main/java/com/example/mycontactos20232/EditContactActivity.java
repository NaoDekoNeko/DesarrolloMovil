package com.example.mycontactos20232;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycontactos20232.API.ContactoAPI;
import com.example.mycontactos20232.API.RetrofitContacto;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.reactivex.disposables.CompositeDisposable;


public class EditContactActivity extends AppCompatActivity {
    EditText txtnombre2,txtalias2;
    Spinner spntipo2;
    Button btneditarcontacto,btncancelar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contacto_listado);
        txtnombre2 = (EditText)findViewById(R.id.txtnombre2);
        txtalias2 = (EditText)findViewById(R.id.txtalias2);
        spntipo2 = (Spinner) findViewById(R.id.spntipo2);
        btneditarcontacto = (Button)findViewById(R.id.btneditarcontacto);
        btncancelar2 = (Button)findViewById(R.id.btncancelar2);
        int contact_position = getIntent().getIntExtra("contact_position", -1);

        btneditarcontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtnombre2.getText().toString();
                String alias = txtalias2.getText().toString();
                int tipo = spntipo2.getSelectedItemPosition();
                String successfulmsg ="Contacto Actualizado", failmsg ="Error al actualizar contacto";
                Intent intent = getIntent();
                int contact_position = getIntent().getIntExtra("contact_position", -1);
                Contacto c;
                switch (intent.getIntExtra("save_type",0))
                {
                    case 0:
                        c = MainActivity.miscontactos.get(contact_position);
                        // Encuentra el contacto existente y actualiza sus detalles
                        txtnombre2.setText(c.getNombre());
                        txtalias2.setText(c.getAlias());
                        MainActivity.miscontactos.set(contact_position, new Contacto(nombre, alias, tipo));
                        Toast.makeText(EditContactActivity.this, successfulmsg, Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(EditContactActivity.this, ListarContacto.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Contacto s = MainActivity.miscontactosSQLite.get(contact_position);
                        txtnombre2.setText(s.getNombre());
                        txtalias2.setText(s.getAlias());
                        int resultado = MainActivity.dbHelper.actualizarContacto(s.getIdcontacto(), nombre, alias, tipo);

                        if (resultado > 0) {
                            s.setNombre(nombre);
                            s.setAlias(alias);
                            s.setTipo(tipo);


                            Toast.makeText(getApplicationContext(),successfulmsg,Toast.LENGTH_SHORT).show();

                            Intent intent2 = new Intent(getApplicationContext(),ListarContactoSQLite.class);
                            startActivity(intent2);
                        } else {
                            Toast.makeText(getApplicationContext(),failmsg,Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        int idaux = getIntent().getIntExtra("contactoid",-1);
                        ContactoAPI contactoAPI = RetrofitContacto.getInstance().create(ContactoAPI.class);

                        // Crear un nuevo objeto Contacto con los datos actualizados
                        nombre = txtnombre2.getText().toString();
                        alias = txtalias2.getText().toString();
                        tipo = spntipo2.getSelectedItemPosition();
                        Contacto contactoActualizado = new Contacto(nombre, alias, tipo);

                        // Realizar la solicitud de actualización
                        Call<Void> call = contactoAPI.updateContacto(idaux, contactoActualizado);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(EditContactActivity.this, "Contacto actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                    // Aquí puedes manejar el caso en que la actualización sea exitosa
                                } else {
                                    // Aquí puedes manejar el caso en que la respuesta no sea exitosa
                                    String errorMsg = "Error al actualizar el contacto. Código de estado HTTP: " + response.code();
                                    Toast.makeText(EditContactActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // Aquí puedes manejar el caso en que la llamada falle
                                String errorMsg = "Error al realizar la llamada a la API: " + t.getMessage();
                                Toast.makeText(EditContactActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                }

            }
        });
        btncancelar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
