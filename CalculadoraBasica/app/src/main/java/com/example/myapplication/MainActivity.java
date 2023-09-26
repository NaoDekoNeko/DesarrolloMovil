package com.example.myapplication;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNumber1;
    private EditText editTextNumber2;
    private double operacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNumber1 = findViewById(R.id.editTextNumber1);
        editTextNumber2 = findViewById(R.id.editTextNumber2);

    }

    //Linked to button
    public void sumar(View view){
        try {
            operacion = parseDouble(editTextNumber1.getText().toString()) + parseDouble(editTextNumber2.getText().toString());
            mostrarResultado();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Operacion invalida", Toast.LENGTH_SHORT).show();
        }
    }

    //Linked to button2
    public void restar(View view){
        try {
            operacion = parseDouble(editTextNumber1.getText().toString()) - parseDouble(editTextNumber2.getText().toString());
            mostrarResultado();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Operacion invalida", Toast.LENGTH_SHORT).show();
        }
    }

    //Linked to button3
    public void multiplicar(View view){
        try {
            operacion = parseDouble(editTextNumber1.getText().toString()) * parseDouble(editTextNumber2.getText().toString());
            mostrarResultado();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Operacion invalida", Toast.LENGTH_SHORT).show();
        }
    }

    //Linked to button4
    public void dividir(View view){
        try {
            operacion = parseDouble(editTextNumber1.getText().toString()) / parseDouble(editTextNumber2.getText().toString());
            mostrarResultado();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Operacion invalida", Toast.LENGTH_SHORT).show();
        }
    }

    public void mostrarResultado(){
        String mensaje = "El resultado es: ";
        Toast.makeText(getApplicationContext(),mensaje + String.valueOf(operacion),Toast.LENGTH_SHORT).show();
    }
}