package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import EMPRESA.ANDROID.BEAN.ProductoBean;
import EMPRESA.ANDROID.DAO.ProductoDAO;

public class MainActivity extends AppCompatActivity {
    Spinner spnmarca,spntalla;
    EditText txtnumeropares,txtresultado;
    Button btncalcular,btnlistar;
    ProductoBean objproductoBean;
    ProductoDAO objproductoDAO;
    public static ArrayList<ProductoDAO> compras = new ArrayList<ProductoDAO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spnmarca = (Spinner) findViewById(R.id.spnmarca);
        spntalla = (Spinner) findViewById(R.id.spntalla);
        txtnumeropares = (EditText) findViewById(R.id.txtnumeropares);
        txtresultado = (EditText) findViewById(R.id.txtresultado);
        btncalcular = (Button) findViewById(R.id.btncalcular);
        btncalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calcular();
            }
        });
        btnlistar = (Button) findViewById(R.id.btnlistar);
        btnlistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ListarCompras.class);
                startActivity(intent);
            }
        });
    }
    public void Calcular(){
        int marca = spnmarca.getSelectedItemPosition();
        int talla = spntalla.getSelectedItemPosition();
        String numparescad = txtnumeropares.getText().toString();
        int numpares = Integer.parseInt(numparescad);
        objproductoBean = new ProductoBean();
        objproductoBean.setMarca(marca);
        objproductoBean.setTalla(talla);
        objproductoBean.setNumpares(numpares);

        objproductoDAO = new ProductoDAO();
        String mensaje = objproductoDAO.CalcularOperacion(objproductoBean);
        txtresultado.setText(mensaje);
        compras.add(objproductoDAO);

        /*
        Intent intent = new Intent(getApplicationContext(),MainReporte.class);
        intent.putExtra("mensaje",mensaje);
        intent.putExtra("otromensaje","otromensaje");
        startActivity(intent);
        */
    }
}