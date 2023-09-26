package com.example.tiendazapatillas;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import empresa.android.bean.ProductoBean;
import empresa.android.dao.ProductoDAO;

public class MainActivity extends AppCompatActivity {

    Spinner spnmarca, spntalla;
    EditText txtnumeropares;
    TextView txtresultado;

    ProductoBean objProductoBean;
    ProductoDAO objProductoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spnmarca = findViewById(R.id.spnmarca);
        spntalla = findViewById(R.id.spntalla);
        txtnumeropares = findViewById(R.id.txtnumeropares);
        txtresultado = findViewById(R.id.txtresultado);

    }

    public void calcular(View view){
        int marca = spnmarca.getSelectedItemPosition();
        int talla = spntalla.getSelectedItemPosition();
        int numPares = parseInt(txtnumeropares.getText().toString());
        objProductoBean = new ProductoBean();
        objProductoBean.setMarca(marca);
        objProductoBean.setTalla(talla);
        objProductoBean.setNumPares(numPares);

        objProductoDAO = new ProductoDAO(objProductoBean);
        String mensaje = objProductoDAO.calcularOperacion();
        txtresultado.setText(mensaje);
    }
}