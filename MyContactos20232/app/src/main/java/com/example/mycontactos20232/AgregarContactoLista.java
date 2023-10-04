package com.example.mycontactos20232;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mycontactos20232.API.ContactoAPI;
import com.example.mycontactos20232.API.RetrofitContacto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarContactoLista extends AppCompatActivity {
    EditText txtnombre,txtalias;
    Spinner spntipo, spnguardado;
    Button btnagregarcontacto,btncancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto_lista);
        txtnombre = findViewById(R.id.txtnombre);
        txtalias = findViewById(R.id.txtalias);
        spntipo = findViewById(R.id.spntipo);
        spnguardado = findViewById(R.id.spnGuardado);
        btnagregarcontacto = findViewById(R.id.btnAgregarContacto);
        btncancelar = findViewById(R.id.btncancelar);
        btnagregarcontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtnombre.getText().toString();
                String alias = txtalias.getText().toString();
                int tipo = spntipo.getSelectedItemPosition();
                String successfulmsg ="Contacto Agregado", failmsg = "Error al agregar al contacto",
                        emptymsg ="Ingrese un nombre", failsavetypemsg = "Error al seleccionar tipo de guardado.";
                Contacto c;
                Intent intent;
                long id;
                switch (spnguardado.getSelectedItemPosition()){
                    case 0:
                        id = MainActivity.contador;
                        c = new Contacto((int) id,nombre,alias,tipo);
                        MainActivity.miscontactos.add(c);
                        MainActivity.contador++;

                        Toast.makeText(getApplicationContext(),successfulmsg,Toast.LENGTH_SHORT).show();
                        txtnombre.setText("");
                        txtalias.setText("");
                        intent = new Intent(getApplicationContext(),ListarContacto.class);
                        startActivity(intent);
                        break;
                    case 1:
                        if(!nombre.isEmpty()){
                            id = MainActivity.dbHelper.insertarContacto(nombre, alias, tipo);
                            if (id != -1) {
                                c = new Contacto((int) id, nombre, alias, tipo);
                                MainActivity.miscontactosSQLite.add(c);

                                Toast.makeText(getApplicationContext(),successfulmsg,Toast.LENGTH_SHORT).show();
                                txtnombre.setText("");
                                txtalias.setText("");
                                intent = new Intent(getApplicationContext(),ListarContactoSQLite.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),failmsg,Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),emptymsg,Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        ContactoAPI contactoAPI = RetrofitContacto.getInstance().create(ContactoAPI.class);
                        c = new Contacto(nombre,alias,tipo);
                        Call<Contacto> call = contactoAPI.setContacto(c);
                        call.enqueue(new Callback<Contacto>() {
                            @Override
                            public void onResponse(Call<Contacto> call, Response<Contacto> response) {
                                if(!response.isSuccessful()){
                                    Toast.makeText(AgregarContactoLista.this,successfulmsg,Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(AgregarContactoLista.this,failmsg,Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Contacto> call, Throwable t) {
                                Toast.makeText(AgregarContactoLista.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });


                        Toast.makeText(getApplicationContext(),successfulmsg,Toast.LENGTH_LONG).show();
                        txtnombre.setText("");
                        txtalias.setText("");
                        intent = new Intent(getApplicationContext(), MainListarweb.class);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),failsavetypemsg,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}