package ch.hevs.ss1;

import static java.lang.Math.abs;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import ch.hevs.kart.AbstractKartControlActivity;
import ch.hevs.kart.Kart;
import ch.hevs.kart.KartListener;

public class MyKartRemote extends AbstractKartControlActivity implements KartListener {
//actions - réaction
    String lightButtonColor = "E1E1E1E1";
    SeekBar gasBar;
    SeekBar directionBar;
    TextView gasLevelText;
    ImageView sw;
    ImageButton parameterButton;
    TextView angleText;
    int seekGasIncrease = 15;
    int seekSteeringIncrease = 280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialisation des variables
        setContentView(R.layout.activity_my_kart_remote);
        kart.addKartListener(this);
        Button positionLightButton = (Button) findViewById(R.id.positionLightButton);
        Button chassisLightButton = (Button) findViewById(R.id.chassisLightButton);
        gasBar = (SeekBar)findViewById(R.id.gasBarID);
        directionBar = (SeekBar)findViewById(R.id.directionBarID);
        gasLevelText = (TextView)findViewById(R.id.powerLevelTextID);
        sw = (ImageView)findViewById(R.id.swID);
        parameterButton = (ImageButton)findViewById((R.id.parameterButtonID));
        angleText = (TextView)findViewById(R.id.angleTextID);

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
                angleText.setText(" Angle: " + i + String.valueOf(kart.getSteeringPosition()));
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



}