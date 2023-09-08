package ch.hevs.ss1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ch.hevs.kart.AbstractKartControlActivity;
import ch.hevs.kart.Kart;
import ch.hevs.kart.KartListener;
import ch.hevs.kart.utils.Timer;


public class accelerometerControl extends AbstractKartControlActivity implements KartListener {

    ImageView swI ;
    TextView Batterylevel_num1;

    ProgressBar batteryLevel;

    private SensorManager sensorManager;
    private Sensor Accelerometre;
    private SensorEventListener accelerometreEventListener;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_control);
        kart.addKartListener(this);
        sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);
        Accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        batteryLevel = (ProgressBar) findViewById(R.id.batteryLevelID3);
        Batterylevel_num1 = (TextView) findViewById(R.id.batterylevel_numID3);
        swI = (ImageView) findViewById(R.id.swID3);
        accelerometreEventListener =new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float yAxis = sensorEvent.values[1];
                kart.setSteeringTargetPosition((int)(((yAxis*2)/0.075)+280));
                swI.setRotation(((float)((yAxis*2)/0.075)+280));

                float zAxis = sensorEvent.values[2];
                kart.setDriveSpeed((int) zAxis);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

    }
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(accelerometreEventListener,Accelerometre,SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accelerometreEventListener);
    }

    //affichage du niveau de la batterie
    @Override
    public void batteryLevelChanged(@NonNull Kart kart, double level){
        Batterylevel_num1.setText(String.format("%.1f", (level*100)) + "%");
        batteryLevel.setProgress((int)(level*100));
    }
}