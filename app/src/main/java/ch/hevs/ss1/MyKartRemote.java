package ch.hevs.ss1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    TextView gasLevelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_kart_remote);
        kart.addKartListener(this);

        Button positionLightButton = (Button) findViewById(R.id.positionLightButton);
        Button chassisLightButton = (Button) findViewById(R.id.chassisLightButton);
        gasBar = (SeekBar)findViewById(R.id.gasBarID);
        gasLevelText = (TextView)findViewById(R.id.powerLevelTextID);

        gasBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            gasLevelText.setText("Power: " + String.valueOf(i) +" %");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("SEEKBAR", "User touch the gasBar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("SEEKBAR", "User release the gasBar");
            }
        });



        positionLightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTON", "User tapped the positionLightButton");
            }
        });
        chassisLightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTON", "User tapped the chassisLightButton");
            }
        });

    }

    //public void steeringPositionChanged(@NonNull Kart kart, int position){}
    //permet un affichage constant des valeurs



}