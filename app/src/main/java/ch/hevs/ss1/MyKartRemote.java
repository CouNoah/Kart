package ch.hevs.ss1;

import static java.lang.Math.abs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ch.hevs.kart.AbstractKartControlActivity;
import ch.hevs.kart.Kart;
import ch.hevs.kart.KartListener;
import ch.hevs.kart.utils.Timer;

public class MyKartRemote extends AbstractKartControlActivity implements KartListener {
//actions - réaction
    String lightButtonColor = "E1E1E1E1";
    SeekBar gasBar;
    SeekBar directionBar;
    Switch swi_accelero_on_off;
    Switch swi_uSonic;
    TextView txt_uSonic_distance;
    TextView gasLevelText;
    ImageView sw;
    ImageButton parameterButton;
    TextView angleText;
    TextView speedLevelText;
    TextView Batterylevel_num;
    ProgressBar batteryLevel;
    int seekGasIncrease = 15;
    int seekSteeringIncrease = 280;
    int beeppauseTime;
    int prevSpeedValue; // valeur de vitesse precedent la nouvelle mesure
    private boolean isLedBlinkerActive;
    private boolean isLedBlinkeronpause;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beeppauseTime = 0;
        //initialisation des variables pour le on/off du buzzer
        isLedBlinkerActive = false;
        isLedBlinkeronpause = false;
        // Initialisation des variables
        setContentView(R.layout.activity_my_kart_remote);
        kart.addKartListener(this);
        Button positionLightButton = (Button) findViewById(R.id.positionLightButtonID);
        Button chassisLightButton = (Button) findViewById(R.id.chassisLightButton);
        gasBar = (SeekBar)findViewById(R.id.gasBarID);
        directionBar = (SeekBar)findViewById(R.id.directionBarID);
        gasLevelText = (TextView)findViewById(R.id.powerLevelTextID);
        sw = (ImageView)findViewById(R.id.swID);
        parameterButton = (ImageButton)findViewById((R.id.parameterButtonID));
        angleText = (TextView)findViewById(R.id.angleTextID);
        txt_uSonic_distance = (TextView) findViewById(R.id.txt_uSonic_distanceID);
        speedLevelText = (TextView) findViewById(R.id.powerLevelTextID2);
        swi_uSonic = (Switch) findViewById(R.id.swi_uSonicID);
        Batterylevel_num = (TextView) findViewById(R.id.Batterylevel_numID);
        batteryLevel = (ProgressBar) findViewById(R.id.batteryLevelID);
        prevSpeedValue = 0;
        //initialisation des leds
        kart.setLedState(0,false); //feux de posistions avant et arriere
        kart.setLedState(1,false); //clignotant droit
        kart.setLedState(2,false); //clignotant gauche
        kart.setLedState(3,false); //LEDs freins
        kart.setLedState(4,false); //LEDs gaz
        kart.setLedState(5,false); //rien pour l'instant
        kart.setLedState(6,false); //rien pour l'instant
        kart.setLedState(7,false); //buzzer on/off

        gasBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            gasLevelText.setText("Power: " + String.valueOf(abs(i*(100/seekGasIncrease)-100)) +" %");
            kart.setDriveSpeed(i-seekGasIncrease);
            Log.d("DRIVE MOTOR", String.valueOf(kart.getDriveSpeed()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("SEEKBAR", "User touch the gasBar");

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("SEEKBAR", "User release the gasBar");
                gasBar.setProgress(seekGasIncrease);
            }
        });


        directionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("SEEKBAR", "move");
                sw.setRotation(kart.getSteeringPosition());
                //angleText.setText(" Angle: " + i + String.valueOf(kart.getSteeringPosition()) + String.valueOf(kart.getSteeringPositionNormalized()));
                kart.setSteeringTargetPosition(i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("SEEKBAR", "User touch the directionBar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("SEEKBAR", "User release the directionBar");
                directionBar.setProgress(seekSteeringIncrease);
            }
        });

        parameterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKartSetupDialog();
            }
        });

        positionLightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTON", "User tapped the positionLightButton");
                positionLightButton.setBackgroundColor(0xFFFF0000);
                kart.increaseDriveSpeed();
            }
        });
        chassisLightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTON", "User tapped the chassisLightButton");
                kart.decreaseDriveSpeed();
            }
        });

    }



    //public void steeringPositionChanged(@NonNull Kart kart, int position){}
    //permet un affichage constant des valeurs


    //beep beep arriere et capteur ultrasonic
    @Override
    public void ultrasonicRangerDistanceChanged(@NonNull Kart kart, double distance) {
        //KartListener.super.ultrasonicRangerDistanceChanged(kart, distance);

        Timer ledpause = new Timer() {// timer de pause du beepeur
            @Override
            public void onTimeout() {
                isLedBlinkeronpause= false;
            }
        };
        Timer ledBlinker = new Timer() {        // timer de marche du beepeur
            @Override
            public void onTimeout() {
                kart.setLedState(7,false);
                isLedBlinkerActive = false;
                isLedBlinkeronpause= true;
                ledpause.scheduleOnce(beeppauseTime);
            }
        };
        //détermine si il faut recomencer a faire le beep beep
        System.out.println(distance);
        txt_uSonic_distance.setText(String.format("%.2f",distance + "m"));
        System.out.print(kart.getLedState(7));
        if(distance<= 1 && !isLedBlinkerActive && !isLedBlinkeronpause){
            beeppauseTime = (int)(distance*5000);
            kart.setLedState(7,true);
            ledBlinker.scheduleOnce(500);
            isLedBlinkerActive = true;
        }else if (distance< 1 || !swi_uSonic.isChecked()){
            ledBlinker.stop();
            ledpause.stop();
        }
    }

    // capteur de distance
    @Override
    public void hallSensorCountChanged(@NonNull Kart kart, int hallSensorNb, int value){
        if(hallSensorNb==0){
            speedLevelText.setText("Speed : "+ String.format("%.2f",(((double)value/4)*(70)*(3.1415*60/63360))));
        }
        //active les LEDs de frein et de gaz en fonction de l'acceleration ou deceleration
        if (value > prevSpeedValue){
            kart.setLedState(4,true);
            kart.setLedState(3,false);
        }else if (value < prevSpeedValue){
            kart.setLedState(4,false);
            kart.setLedState(3,true);
        }else {
            kart.setLedState(4,false);
            kart.setLedState(3,false);
        }

        prevSpeedValue = value;
    }

    //affichage de l'angle actuel sur l'écran
    @Override
    public void steeringPositionChanged(@NonNull Kart kart, int position){
        angleText.setText(" Angle: " + String.format("%.1f",(((((double)position)-280)*0.075))) + "°");
    }

    //affichage du niveau de la batterie
    @Override
    public void batteryLevelChanged(@NonNull Kart kart, double level){
        Batterylevel_num.setText(String.format("%.1f", (level*100)) + "%");
        batteryLevel.setProgress((int)(level*100));
    }



}