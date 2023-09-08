package ch.hevs.ss1;

import static java.lang.Math.abs;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
    SeekBar gasBar;
    SeekBar directionBar;
    Switch swi_uSonic;
    TextView txt_uSonic_distance;
    ImageView sw;
    ImageButton parameterButton;

    TextView angleText;
    TextView speedLevelText;
    TextView Batterylevel_num;
    ProgressBar batteryLevel;
    ImageButton positionLightButton;
    ImageButton clignot_droit_button;
    ImageButton clignot_gauche_button;
    ImageButton feuxdepanne_button;
    ImageButton backToFuture_button;
    int seekGasIncrease = 15;
    int beeppauseTime;
    int prevSpeedValue; // valeur de vitesse precedent la nouvelle mesure
    double thepositionangle;
    int Ledtoblink;
    boolean isledBlinkerindicatoractive = false;
    int theLedorangeLedToBlink = 1;
    int steeringMiddlePosition;
    private boolean isLedBlinkerActive;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beeppauseTime = 0;
        thepositionangle = 0;
        Ledtoblink = 6;

        //initialisation des variables pour le on/off du buzzer
        isLedBlinkerActive = false;


        //Initialisation des variables
        setContentView(R.layout.activity_my_kart_remote);
        kart.addKartListener(this);
        positionLightButton = (ImageButton) findViewById(R.id.positionLightButtonID);
        clignot_droit_button = (ImageButton) findViewById(R.id.clignot_droit_buttonID);
        clignot_gauche_button = (ImageButton) findViewById(R.id.clignot_gauche_buttonID);
        feuxdepanne_button = (ImageButton) findViewById(R.id.feuxdepanne_buttonID);
        backToFuture_button = (ImageButton) findViewById(R.id.backToFuture_buttonID);

        gasBar = (SeekBar)findViewById(R.id.gasBarID);




        //initialisation pour la rotation
        directionBar = (SeekBar)findViewById(R.id.directionBarID);
        directionBar.setMax((kart.setup().getSteeringMaxPosition()));
        steeringMiddlePosition =(kart.setup().getSteeringCenterPosition());
        directionBar.setProgress(steeringMiddlePosition);


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
                kart.setDriveSpeed(i-seekGasIncrease);
                Log.d("DRIVE MOTOR", String.valueOf(kart.getDriveSpeed()));
                if(i < 16) {
                    kart.setLedState(3, true); //LEDs freins
                    kart.setLedState(4, false); //LEDs gaz
                }
                if(i >= 16){
                    kart.setLedState(3,false); //LEDs freins
                    kart.setLedState(4,true); //LEDs gaz
                }

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
                //Log.d("SEEKBAR", "move");
                kart.setSteeringTargetPosition(i);
                //System.out.println("angle " + i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("SEEKBAR", "User touch the directionBar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("SEEKBAR", "User release the directionBar");
                directionBar.setProgress(steeringMiddlePosition);
            }
        });

        parameterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKartSetupDialog();
            }
        });

        //bouton qui active les feux de position avant et arrière
        positionLightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!kart.getLedState(0)){
                    positionLightButton.setBackgroundTintList(ColorStateList.valueOf(995299971));
                    kart.toggleLed(0);
                }
                else {
                    positionLightButton.setBackgroundTintList(ColorStateList.valueOf(0x00FFFFFF));
                    kart.toggleLed(0);
                }
            }
        });

        //bouton de feux de panne
        feuxdepanne_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!kart.getLedState(2)){
                    feuxdepanne_button.setBackgroundTintList(ColorStateList.valueOf(0xFFFF1100));
                    kart.setLedState(1,true);
                    kart.setLedState(2,true);
                }
                else {
                    kart.setLedState(1,false);
                    kart.setLedState(2,false);
                    feuxdepanne_button.setBackgroundTintList(ColorStateList.valueOf(0x00FFFFFF));
                }
            }
        });

        //bouton clignot droit
        clignot_droit_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(!kart.getLedState(1)){
                    kart.setLedState(1,true);
                    clignot_droit_button.setBackgroundTintList(ColorStateList.valueOf(0xFFFF8C00));
                }
                else {
                    kart.setLedState(1,false);
                    clignot_droit_button.setBackgroundTintList(ColorStateList.valueOf(0x00FFFFFF));
                }
            }
        });

        //bouton clignot gauche
        clignot_gauche_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!kart.getLedState(2)){
                    kart.setLedState(2,true);
                    clignot_gauche_button.setBackgroundTintList(ColorStateList.valueOf(0xFFFF8C00));
                }
                else {
                    kart.setLedState(2,false);
                    clignot_gauche_button.setBackgroundTintList(ColorStateList.valueOf(0x00FFFFFF));
                }
            }
        });

        //bouton avec logo du film
        backToFuture_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!kart.getLedState(4)){
                    backToFuture_button.setBackgroundTintList(ColorStateList.valueOf(0xFF03A9F4));
                    kart.toggleLed(4);
                }
                else {
                    backToFuture_button.setBackgroundTintList(ColorStateList.valueOf(0x00FFFFFF));
                    kart.toggleLed(4);
                }
            }
        });

    }


    //beep beep arriere et capteur ultrasonic
    //@Override
    public void ultrasonicRangerDistanceChanged(@NonNull Kart kart, double distance) {
        //KartListener.super.ultrasonicRangerDistanceChanged(kart, distance);

        /*Timer ledBlinker = new Timer() {        // timer de marche du beepeur
            @Override
            public void onTimeout() {
                kart.setLedState(7,false);
                isLedBlinkerActive = false;
            }
        };*/
        //détermine si il faut recomencer a faire le beep beep
        System.out.println(distance);
        txt_uSonic_distance.setText(distance + "m");
        System.out.print(kart.getLedState(7));
        if(distance <= 0.2 && !isLedBlinkerActive){
            beeppauseTime = (int)(distance*5000);
            kart.setLedState(7,true);
            //ledBlinker.scheduleOnce(beeppauseTime);
            isLedBlinkerActive = true;
        }else if (distance< 1 || !swi_uSonic.isChecked()){
            //ledBlinker.stop();
            isLedBlinkerActive = false;
        }
    }


    // capteur de distance
    @Override
    public void hallSensorCountChanged(@NonNull Kart kart, int hallSensorNb, int value){
        if(hallSensorNb==0){
            speedLevelText.setText(String.valueOf(value-prevSpeedValue));
            prevSpeedValue = value;
        }
    }

    //affichage de l'angle actuel sur l'écran et active les clignotant en fonction de la direction choisie
    @Override
    public void steeringPositionChanged(@NonNull Kart kart, int position){
        System.out.println("Steering changed: " + position + ", middle: " + kart.setup().getSteeringCenterPosition() + ", max: " + kart.setup().getSteeringMaxPosition());
        thepositionangle = ((double) (position - steeringMiddlePosition)) * 0.075;
        angleText.setText(String.format("Angle: %.0f°",thepositionangle));
        sw.setRotation((float)(thepositionangle));
        Timer ledBlinkerindicator = new Timer() {
            @Override
            public void onTimeout() {
                kart.toggleLed(theLedorangeLedToBlink);

            }
        };
       if (thepositionangle < 5 || thepositionangle > -5){
            if (!isledBlinkerindicatoractive){
                ledBlinkerindicator.schedulePeriodically(500);
                isledBlinkerindicatoractive = true;
            }
            if(thepositionangle<5){
                theLedorangeLedToBlink = 2;
                kart.setLedState(1,false);
            } else if (thepositionangle>5) {
                theLedorangeLedToBlink = 1;
                kart.setLedState(2,false);
            }
            else {
                ledBlinkerindicator.stop();
                kart.setLedState(1,false);
                kart.setLedState(2,false);
                isledBlinkerindicatoractive=false;
            }
        }
        else {
            ledBlinkerindicator.stop();
            kart.setLedState(1,false);
            kart.setLedState(2,false);
            isledBlinkerindicatoractive=false;
        }

    }

    //affichage du niveau de la batterie
    @Override
    public void batteryLevelChanged(@NonNull Kart kart, double level){
        Batterylevel_num.setText(String.format("%.1f%", (level*100)));
        batteryLevel.setProgress((int)(level*100));
    }



}