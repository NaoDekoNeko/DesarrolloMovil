package com.example.myoperaciones;

import static java.lang.Double.parseDouble;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtn1, txtn2;
    Spinner cbooperacion;
    Button btncalcular;
    TextView lblresultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtn1 = findViewById(R.id.txtn1);
        txtn2 = findViewById(R.id.txtn2);
        cbooperacion = findViewById(R.id.cbooperacion);
        lblresultado = findViewById(R.id.lblresultado);
        btncalcular = findViewById(R.id.btncalcular);
        btncalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double n1 = parseDouble(txtn1.getText().toString());
                double n2 = parseDouble(txtn2.getText().toString());
                String operacion = cbooperacion.getSelectedItem().toString();
                double resultado = 0;
                switch (operacion){
                    case "Sumar":
                        resultado = n1+n2;
                        break;
                    case "Restar":
                        resultado = n1-n2;
                        break;
                    case "Multiplicar":
                        resultado = n1*n2;
                        break;
                    case "Dividir":
                        resultado = n1/n2;
                        break;
                    default:
                        resultado = 0;
                        break;
                }
                lblresultado.setText(String.valueOf(resultado));
                Toast.makeText(getApplicationContext(),String.valueOf(resultado),Toast.LENGTH_SHORT).show();
            }
        });
    }
}