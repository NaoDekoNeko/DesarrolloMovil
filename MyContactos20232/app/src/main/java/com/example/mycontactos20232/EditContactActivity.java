package com.example.mycontactos20232;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycontactos20232.Cloud.API.ContactoAPI;
import com.example.mycontactos20232.Cloud.API.RetrofitContacto;
import com.example.mycontactos20232.Cloud.ListarContactoWeb;
import com.example.mycontactos20232.Lista.ListarContacto;
import com.example.mycontactos20232.SQLite.ListarContactoSQLite;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditContactActivity extends AppCompatActivity {
    EditText txtnombre2,txtalias2;
    Spinner spntipo2;
    Button btneditarcontacto,btncancelar2;
    int contact_position, save_type;
    Contacto c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contacto_listado);
        txtnombre2 = (EditText)findViewById(R.id.txtnombre2);
        txtalias2 = (EditText)findViewById(R.id.txtalias2);
        spntipo2 = (Spinner) findViewById(R.id.spntipo2);
        btneditarcontacto = (Button)findViewById(R.id.btneditarcontacto);
        btncancelar2 = (Button)findViewById(R.id.btncancelar2);
        contact_position = getIntent().getIntExtra("contact_position", -1);
        save_type = getIntent().getIntExtra("save_type", -1);
        switch (save_type) {
            case 0:
                c = MainActivity.miscontactos.get(contact_position);
                break;
            case 1:
                c = MainActivity.miscontactosSQLite.get(contact_position);
                break;
            case 2:
                c = MainActivity.miscontactosweb.get(contact_position);
        }
        txtnombre2.setText(c.getNombre());
        txtalias2.setText(c.getAlias());
        btneditarcontacto.setOnClickListener(view -> {
            String nombre = txtnombre2.getText().toString();
            String alias = txtalias2.getText().toString();
            int tipo = spntipo2.getSelectedItemPosition();
            String successfulmsg ="Contacto Actualizado", failmsg ="Error al actualizar contacto";

            switch (save_type) {
                case 0:
                    MainActivity.miscontactos.set(contact_position, new Contacto(nombre, alias, tipo));
                    Toast.makeText(EditContactActivity.this, successfulmsg, Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(EditContactActivity.this, ListarContacto.class);
                    startActivity(intent1);
                    break;
                case 1:
                    int resultado = MainActivity.dbHelper.actualizarContacto(c.getIdcontacto(), nombre, alias, tipo);

                    if (resultado > 0) {
                        c.setNombre(nombre);
                        c.setAlias(alias);
                        c.setTipo(tipo);

                        Toast.makeText(getApplicationContext(),successfulmsg,Toast.LENGTH_SHORT).show();

                        Intent intent2 = new Intent(getApplicationContext(), ListarContactoSQLite.class);
                        startActivity(intent2);
                    } else {
                        Toast.makeText(getApplicationContext(),failmsg,Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    nombre = txtnombre2.getText().toString();
                    alias = txtalias2.getText().toString();
                    tipo = spntipo2.getSelectedItemPosition();
                    ContactoAPI contactoAPI = RetrofitContacto.getInstance().create(ContactoAPI.class);
                    c.setNombre(nombre);
                    c.setAlias(alias);
                    c.setTipo(tipo);
                    Call<Void> call = contactoAPI.updateContacto(c);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(EditContactActivity.this,successfulmsg,Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(EditContactActivity.this,failmsg,Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(EditContactActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent3 = new Intent(getApplicationContext(), ListarContactoWeb.class);
                    startActivity(intent3);
                    break;
            }
        });
        btncancelar2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        });
    }
}
