package ch.hevs.ss1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ch.hevs.kart.AbstractKartControlActivity;

public class MyKartRemote extends AbstractKartControlActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_kart_remote);
    }
}