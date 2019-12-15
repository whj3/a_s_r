package com.prototypes.availableshotresolutions;

import android.content.Context;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Size;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "myLogs";
    String[] myCameras = null;
    private CameraManager mCameraManager    = null;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String text_container;
        text_container = "Hi there! \n";

        float ar;

        TextView myAwesomeTextView = (TextView)findViewById(R.id.resolutions);
        myAwesomeTextView.setMovementMethod(new ScrollingMovementMethod());

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{
            // Получение списка камер с устройства

            myCameras = new String[mCameraManager.getCameraIdList().length];

            // выводим информацию по камере
            for (String cameraID : mCameraManager.getCameraIdList()) {
                //Log.i(LOG_TAG, "cameraID: "+cameraID);
                text_container = myAwesomeTextView.getText().toString()+"cameraID: "+cameraID+"\n";
                myAwesomeTextView.setText(text_container);
                int id = Integer.parseInt(cameraID);

                // Получениe характеристик камеры
                CameraCharacteristics cc = mCameraManager.getCameraCharacteristics(cameraID);
                // Получения списка выходного формата, который поддерживает камера
                StreamConfigurationMap configurationMap =
                        cc.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                //  Определение какая камера куда смотрит
                int Faceing = cc.get(CameraCharacteristics.LENS_FACING);

                if (Faceing ==  CameraCharacteristics.LENS_FACING_FRONT)
                {
                    //Log.i(LOG_TAG,"Camera with ID: "+cameraID +  "  is FRONT CAMERA  ");
                    text_container = myAwesomeTextView.getText().toString()+"\n\n\n Camera with ID: "+cameraID +  "  is FRONT CAMERA  "+"\n";
                    myAwesomeTextView.setText(text_container);
                }

                if (Faceing ==  CameraCharacteristics.LENS_FACING_BACK)
                {
                    //Log.i(LOG_TAG,"Camera with: ID "+cameraID +  " is BACK CAMERA  ");
                    text_container = myAwesomeTextView.getText().toString()+"Camera with: ID "+cameraID +  " is BACK CAMERA  "+"\n";
                    myAwesomeTextView.setText(text_container);
                }


                // Получения списка разрешений которые поддерживаются для формата jpeg
                Size[] sizesJPEG = configurationMap.getOutputSizes(ImageFormat.JPEG);

                if (sizesJPEG != null) {
                    for (Size item:sizesJPEG) {
                        //Log.i(LOG_TAG, "w:"+item.getWidth()+" h:"+item.getHeight());
                        ar = (float)item.getWidth()/(float)item.getHeight();
                        text_container = myAwesomeTextView.getText().toString()+"w:"+item.getWidth()+" h:"+item.getHeight()+"     a_r: "+String.format(java.util.Locale.US,"%.2f", ar)+"\n";
                        myAwesomeTextView.setText(text_container);
                    }
                }  else {
                    Log.i(LOG_TAG, "camera don`t support JPEG");
                }
            }
        }
        catch(CameraAccessException e){
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }
    }
}
