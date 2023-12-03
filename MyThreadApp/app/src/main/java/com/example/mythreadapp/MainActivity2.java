package com.example.mythreadapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    float width = 0, height = 0;
    Button btnT, btnE;
    TextView tv;
    int score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnE = findViewById(R.id.buttonE);
        btnT = findViewById(R.id.buttonT);
        tv = findViewById(R.id.tv);
        tv.setText("Shot");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height= size.y;

        btnT.setOnClickListener(v -> {
            Moving moving = new Moving();
            moving.start();
        });

        btnE.setOnClickListener(v -> {
            tv.setX(btnE.getX());
            tv.setY(btnE.getY());

            Shooting shooting = new Shooting();
            shooting.start();
        });
    }

    private class Moving extends Thread {
        float x = btnT.getX();
        @Override
        public void run(){
            boolean isRight = true;
            while (true){
                if(x<=0 || x>=width-btnT.getWidth()) isRight = !isRight;
                x += isRight ? 1 : -1;
                runOnUiThread(()-> {btnT.setX(x);});
                try{
                    Thread.sleep(2);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    /*
    private class Shooting extends Thread {
        @Override
        public void run(){
            int xt = (int) tv.getX();
            int yt = (int) tv.getY();

            int xe = (int) btnE.getX();
            int ye = (int) btnE.getY();

            for(int y = yt; y>0;y--){
                if(yt>=ye && yt<=(ye+btnE.getHeight()) && xt>=xe && xt<=(xe+btnE.getWidth())){
                    runOnUiThread(() -> {btnE.setText(""+(++score));});
                    break;
                }
                try{
                    Thread.sleep(1);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                int finalY = y;
                runOnUiThread(() -> {tv.setY(finalY);});
            }
            runOnUiThread(() -> {
                tv.setX(btnE.getX());
                tv.setY(btnE.getY());
            });
        }
    }

     */

    private class Shooting extends Thread {
        @Override
        public void run(){
            int xt = (int) tv.getX();
            int yt = (int) tv.getY();

            int xe = (int) btnE.getX();
            int ye = (int) btnE.getY();

            // Añade una variable para controlar la dirección del movimiento
            boolean isDown = true;

            while (true) {
                // Comprueba si el TextView ha chocado con btnT
                if(yt >= btnT.getY() && yt <= (btnT.getY() + btnT.getHeight()) && xt >= btnT.getX() && xt <= (btnT.getX() + btnT.getWidth())){
                    // Si choca, aumenta el tamaño de btnT y suma 1 al puntaje
                    runOnUiThread(() -> {
                        btnT.setScaleX(btnT.getScaleX() * 1.1f);
                        btnT.setScaleY(btnT.getScaleY() * 1.1f);
                        btnE.setText("Score: "+(++score));

                    });
                    // regresa el TextView a su posición inicial
                    runOnUiThread(() -> {
                        tv.setX(btnE.getX()*0.9f);
                        tv.setY(btnE.getY());
                    });
                    break;
                }

                // Comprueba si el TextView ha chocado con btnE
                if(yt >= ye && yt <= (ye + btnE.getHeight()) && xt >= xe && xt <= (xe + btnE.getWidth())){
                    // Si choca con btnE, no hace nada ya que se maneja el choque con btnT en el bloque anterior
                }

                // Cambia la dirección del movimiento cuando el TextView llega a los bordes de la pantalla
                if(yt >= height - tv.getHeight() || yt <= 0) {
                    isDown = !isDown;
                }

                // Mueve el TextView según la dirección
                yt = isDown ? yt + 1 : yt - 1;

                try{
                    Thread.sleep(1); // Ajusta el valor de sleep según tus necesidades
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }

                int finalYt = yt;
                runOnUiThread(() -> {tv.setY(finalYt);});
            }
        }
    }

}