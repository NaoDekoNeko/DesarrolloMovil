package com.example.myopencv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.objdetect.CascadeClassifier;


public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    // Creamos los atributos para estas clases
    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    // Creamos una variable tipo pixel (matriz)
    private Mat mRGB;
    private Mat mat1;
    private int menu_option = 0; //Creamos un menu para el tipo de tonalidad que queremos en ese momento
    private int mCameraId=0;
    private CascadeClassifier faceCascade;
    private CascadeClassifier faceDetector, narizDetector;
    private float symmetryValue = 0.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Aqui comprobamos si la camara esta libre para ser usada
        if (OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(), "OK Opencv", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Error Opencv", Toast.LENGTH_SHORT).show();
        }
        //Casteamos las camaras
        cameraBridgeViewBase = findViewById(R.id.mycameraview);//se va a insertar lo que vea la camara
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE); //si es visible
        cameraBridgeViewBase.setCvCameraViewListener(this);
        baseLoaderCallback=new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                if (status == BaseLoaderCallback.SUCCESS) {
                    cameraBridgeViewBase.enableView(); //si al habilitar la camara esta bien, entonces habilitamos la camara
                }
                super.onManagerConnected(status);
            }
        };

        ImageView flip_camera = findViewById(R.id.flip_camera);
        flip_camera.setOnClickListener(v -> swapCamera());

        cargarDependencias();
    }

    private void swapCamera() {
        mCameraId = mCameraId^1;
        cameraBridgeViewBase.disableView();
        cameraBridgeViewBase.setCameraIndex(mCameraId);
        cameraBridgeViewBase.enableView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(), "Problema Opencv", Toast.LENGTH_SHORT).show();
        }else{
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGB = new Mat(width,height, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRGB.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat originalFrame = inputFrame.rgba();
        switch (menu_option){
            case 0:
                mat1 = originalFrame; // Imagen original
                break;
            case 1:
                mat1 = inputFrame.gray(); // Imagen en escala de grises
                break;
            case 2:
                mat1 = inputFrame.gray();
                Core.bitwise_not(mat1, mat1); // Inverso de la imagen en escala de grises
                break;
            case 3:
                mat1 = applyBorders(originalFrame);
                break;
            case 4:
                // Implementa la lógica para mostrar la imagen como radiografía
                mat1 = applyXRayEffect(originalFrame);
                break;
            case 5:
                // Implementa la lógica para mostrar la imagen en el inverso de color
                mat1 = applyInverseColorEffect(originalFrame);
                break;
            case 6:
                // Implementa la lógica para mostrar solo el canal R
                mat1 = showRedChannel(originalFrame);
                break;
            case 7:
                // Implementa la lógica para mostrar solo el canal G
                mat1 = showGreenChannel(originalFrame);
                break;
            case 8:
                // Implementa la lógica para mostrar solo el canal B
                mat1 = showBlueChannel(originalFrame);
                break;
            case 9:
                // Implementa la lógica para resaltar el color piel en la imagen
                mat1 = highlightSkinColor(originalFrame);
                break;
            case 10:
                // Implementa la lógica para resaltar una hoja en la imagen
                mat1 = highlightLeafColor(originalFrame);
                break;
            case 11: // Nueva opción para remarcar rostros
                detectAndDrawFaces(originalFrame);
                mat1 = originalFrame; // Actualiza mat1 con la imagen que tiene los rostros resaltados
                break;
            case 12:
                mat1 = symmetry(inputFrame);
                displaySymmetryValue(originalFrame);
                break;
            default:
                break;
        }

        return mat1;
    }

    private void displaySymmetryValue(Mat frame) {
        int centerX = frame.width() / 24;
        int centerY = frame.height() / 24;
        Size textSize = Imgproc.getTextSize("NIVEL DE SIMETRIA: " + symmetryValue, Core.FONT_HERSHEY_SIMPLEX, 1, 1, null);
        int textX = centerX - (int) (textSize.width / 2);
        int textY = centerY + (int) (textSize.height / 2);
        Imgproc.putText(frame, "NIVEL DE SIMETRIA: " + symmetryValue, new Point(textX, textY), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 0, 0), 2);
    }
    private Mat symmetry(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        float ojo1x = 0;
        float ojo2x = 1;
        float ojo1y = 0;
        float ojo2y = 1;
        float narizx = 0;
        float narizy = 1;
        MatOfRect rostros = new MatOfRect();
        Rect[] facesArray;
        Mat mat0 = inputFrame.rgba();
        Mat mat2 = inputFrame.gray();
        Imgproc.equalizeHist(mat2,mat2);
        faceDetector.detectMultiScale(mat2,rostros,1.3,2,0|2,new Size(30,30),new Size(mat0.width(),mat0.height()));
        facesArray = rostros.toArray();

        MatOfRect nariz = new MatOfRect();
        Rect[] narizArray;
        narizDetector.detectMultiScale(mat2,nariz,1.3,2,0|2,new Size(30,30),new Size(mat0.width(),mat0.height()));
        narizArray = nariz.toArray();
        // num de caras

        for( int i=0;i<facesArray.length;i++){
            int x = facesArray[i].x;
            int y = facesArray[i].y;
            int width = facesArray[i].width;
            int height = facesArray[i].height;
            Point center = new Point((x+width*0.5),(y+height*0.5));
            Imgproc.rectangle(mat0,
                    new Point(x,y),
                    new Point(x+width,y+height),
                    new Scalar(123,213,23,220)
            );
            if(i%2==0){
                ojo1x = (float) ((float)x+width*0.5);
                ojo1y = (float) ((float)y+height*0.5);
            }else{
                ojo2x = (float) ((float)x+width*0.5);
                ojo2y = (float) ((float)y+height*0.5);
            }
            Imgproc.putText(mat0,"OJO: "+(i+1),new Point(x,y-20),1,1,new Scalar(255,255,255));

        }
        for( int i=0;i<narizArray.length;i++){
            int x = narizArray[i].x;
            int y = narizArray[i].y;
            int width = narizArray[i].width;
            int height = narizArray[i].height;
            Point center = new Point((x+width*0.5),(y+height*0.5));
            Imgproc.rectangle(mat0,
                    new Point(x,y),
                    new Point(x+width,y+height),
                    new Scalar(123,213,23,220)
            );
            narizx = (float) ((float)x+width*0.5);
            narizy = (float) ((float)y+height*0.5);
            Imgproc.putText(mat0,"NARIZ: "+(i+1),new Point(x,y-20),1,1,new Scalar(255,255,255));

        }
        float d1 = (ojo1x - narizx)*(ojo1x - narizx) + (ojo1y-narizy)*(ojo1y-narizy);
        float d2 = (ojo2x - narizx)*(ojo2x - narizx) + (ojo2y-narizy)*(ojo2y-narizy);
        symmetryValue = (d1-d2)/1000;
        if(symmetryValue<0){
            symmetryValue = symmetryValue*(-1);
        }

        if(symmetryValue>100){
            symmetryValue = 100;
        }

        symmetryValue = 100 - symmetryValue;

        if(symmetryValue == 100){
            symmetryValue = 0;
        }

        return mat0;
    }

    private void detectAndDrawFaces(Mat frame) {
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(frame, faces);
        Rect[] facesArray = faces.toArray();
        for (Rect faceRect : facesArray) {
            Imgproc.rectangle(frame, faceRect.tl(), faceRect.br(), new Scalar(255, 0, 0), 2);
        }
    }

    private Mat applyBorders(Mat inputFrame) {
        Mat borderMat = new Mat();
        Imgproc.Canny(inputFrame,borderMat,80,100);
        return borderMat;
    }

    public Mat applyXRayEffect(Mat inputFrame) {
        // Convertir la imagen a escala de grises
        Mat grayImage = new Mat();
        Imgproc.cvtColor(inputFrame, grayImage, Imgproc.COLOR_BGR2GRAY);
        // Invertir escala de grises
        Core.bitwise_not(grayImage,grayImage);

        // Encontrar bordes
        Mat edges = new Mat();
        Imgproc.Canny(grayImage,edges,80,100);

        // Sumar imagenes
        Mat res = new Mat();
        Core.add(grayImage,edges,res);

        return res;
    }

    private Mat applyInverseColorEffect(Mat inputFrame) {
        Mat inverseColorMat = new Mat();
        // Invertir los colores
        Core.bitwise_not(inputFrame, inverseColorMat);
        return inverseColorMat;
    }

    private Mat showRedChannel(Mat inputFrame) {
        Mat redChannelMat = new Mat();
        // Implementa la lógica para mostrar solo el canal rojo en "redChannelMat"
        Core.extractChannel(inputFrame, redChannelMat, 0); // Extracción del canal rojo
        return redChannelMat;
    }

    private Mat showGreenChannel(Mat inputFrame) {
        Mat greenChannelMat = new Mat();
        // Implementa la lógica para mostrar solo el canal verde en "greenChannelMat"
        Core.extractChannel(inputFrame, greenChannelMat, 1); // Extracción del canal verde
        return greenChannelMat;
    }

    private Mat showBlueChannel(Mat inputFrame) {
        Mat blueChannelMat = new Mat();
        // Implementa la lógica para mostrar solo el canal azul en "blueChannelMat"
        Core.extractChannel(inputFrame, blueChannelMat, 2); // Extracción del canal azul
        return blueChannelMat;
    }

    private Mat highlightSkinColor(Mat inputFrame) {
        
        Mat skinFrame = new Mat();
        // Convertir la imagen de RGBA a BGR
        Imgproc.cvtColor(inputFrame, skinFrame, Imgproc.COLOR_RGBA2BGR);

        // Convertir la imagen de BGR a HSV
        Imgproc.cvtColor(skinFrame, skinFrame, Imgproc.COLOR_BGR2HSV);

        // Definir el rango de valores de tono y saturación para el color de piel
        Scalar lowerBound = new Scalar(0, 20, 70);  // Valores mínimos de tono, saturación y valor para el color de piel
        Scalar upperBound = new Scalar(30, 150, 255);  // Valores máximos de tono, saturación y valor para el color de piel

        // Crear una máscara que resalte los píxeles de color piel
        Core.inRange(skinFrame, lowerBound, upperBound, skinFrame);

        return skinFrame;
    }

    private Mat highlightLeafColor(Mat inputFrame) {
        Mat leafColorMat = new Mat();
        // Convertir la imagen a espacio de color HSV
        Imgproc.cvtColor(inputFrame, leafColorMat, Imgproc.COLOR_BGR2HSV);
        // Definir el rango de colores para el color de las hojas
        Scalar lower = new Scalar(36, 0, 0);
        Scalar upper = new Scalar(86, 255, 255);
        // Aplicar una máscara para resaltar los píxeles correspondientes al color de las hojas
        Core.inRange(leafColorMat, lower, upper, leafColorMat);
        return leafColorMat;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mimenu,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menuoriginal:
                menu_option = 0; break;
            case R.id.menugris:
                menu_option = 1; break;
            case R.id.menuinversogris:
                menu_option = 2; break;
            case R.id.menubordes:
                menu_option = 3; break;
            case R.id.menuradiografia:
                menu_option = 4; break;
            case R.id.inverso_color:
                menu_option = 5;break;
            case R.id.canal_r:
                menu_option = 6;break;
            case R.id.canal_g:
                menu_option = 7;break;
            case R.id.canal_b:
                menu_option = 8;break;
            case R.id.resaltar_piel:
                menu_option = 9;break;
            case R.id.resaltar_hoja:
                menu_option = 10;break;
            case R.id.remarcar_rostro:
                menu_option = 11;break;
            case R.id.simetria:
                menu_option = 12;break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargarDependencias() {
        try {
            InputStream is2 = getResources().openRawResource(R.raw.haarcascade_frontalface_alt2);
            File cascadeDir2 = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile2 = new File(cascadeDir2, "haarcascade_frontalface_alt2.xml");
            FileOutputStream os2 = new FileOutputStream(mCascadeFile2);
            byte[] buffer2 = new byte[4096];
            int bytesRead2;
            while ((bytesRead2 = is2.read(buffer2)) != -1) {
                os2.write(buffer2, 0, bytesRead2);
            }
            is2.close();
            os2.close();

            faceCascade = new CascadeClassifier(mCascadeFile2.getAbsolutePath());

            InputStream is = getResources().openRawResource(R.raw.haarcascade_eye_tree_eyeglasses);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir,"haarcascade_eye_tree_eyeglasses.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead=is.read(buffer))!=-1){
                os.write(buffer,0,bytesRead);
            }
            is.close();
            os.close();
            faceDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());

            InputStream is1 = getResources().openRawResource(R.raw.haarcascade_mcs_nose);
            File cascadeDir1 = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile1 = new File(cascadeDir1,"haarcascade_mcs_nose.xml");
            FileOutputStream os1 = new FileOutputStream(mCascadeFile1);
            byte[] buffer1 = new byte[4096];
            int bytesRead1;
            while ((bytesRead1=is1.read(buffer1))!=-1){
                os1.write(buffer1,0,bytesRead1);
            }
            is1.close();
            os1.close();
            narizDetector = new CascadeClassifier(mCascadeFile1.getAbsolutePath());

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error Haar",Toast.LENGTH_LONG).show();
        }
    }
}