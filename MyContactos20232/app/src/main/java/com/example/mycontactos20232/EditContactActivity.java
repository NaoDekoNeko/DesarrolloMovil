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
    private EditText txtnombre2, txtalias2;
    private Spinner spntipo2;
    private Button btneditarcontacto, btncancelar2;
    private int contact_position, save_type;
    private Contacto c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contacto_listado);

        initViews();
        loadContactInfo();
        setupButtons();
    }

    private void initViews() {
        txtnombre2 = findViewById(R.id.txtnombre2);
        txtalias2 = findViewById(R.id.txtalias2);
        spntipo2 = findViewById(R.id.spntipo2);
        btneditarcontacto = findViewById(R.id.btneditarcontacto);
        btncancelar2 = findViewById(R.id.btncancelar2);
    }

    private void loadContactInfo() {
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
                break;
        }

        txtnombre2.setText(c.getNombre());
        txtalias2.setText(c.getAlias());
    }

    private void setupButtons() {
        btneditarcontacto.setOnClickListener(view -> {
            String nombre = txtnombre2.getText().toString();
            String alias = txtalias2.getText().toString();
            int tipo = spntipo2.getSelectedItemPosition();
            String successfulMsg = "Contacto Actualizado";
            String failMsg = "Error al actualizar contacto";

            switch (save_type) {
                case 0:
                    updateContactAndNavigate(new Contacto(nombre, alias, tipo), successfulMsg, ListarContacto.class);
                    break;
                case 1:
                    int resultado = MainActivity.dbHelper.actualizarContacto(c.getIdcontacto(), nombre, alias, tipo);

                    if (resultado > 0) {
                        c.setNombre(nombre);
                        c.setAlias(alias);
                        c.setTipo(tipo);
                        updateContactAndNavigate(c, successfulMsg, ListarContactoSQLite.class);
                    } else {
                        Toast.makeText(getApplicationContext(), failMsg, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    updateWebContact(nombre, alias, tipo, successfulMsg, failMsg);
                    break;
            }
        });

        btncancelar2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    private void updateContactAndNavigate(Contacto updatedContact, String successMsg, Class<?> destinationClass) {
        MainActivity.miscontactos.set(contact_position, updatedContact);
        Toast.makeText(EditContactActivity.this, successMsg, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditContactActivity.this, destinationClass);
        startActivity(intent);
    }

    private void updateWebContact(String nombre, String alias, int tipo, String successMsg, String failMsg) {
        ContactoAPI contactoAPI = RetrofitContacto.getInstance().create(ContactoAPI.class);
        c.setNombre(nombre);
        c.setAlias(alias);
        c.setTipo(tipo);
        Call<Void> call = contactoAPI.updateContacto(c);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditContactActivity.this, successMsg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditContactActivity.this, failMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditContactActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(getApplicationContext(), ListarContactoWeb.class);
        startActivity(intent);
    }
}
