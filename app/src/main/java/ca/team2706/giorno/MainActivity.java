package ca.team2706.giorno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void syncActivity(View view) {
        startActivity(new Intent(this, SyncActivity.class));
    }

    public void settings_onClick(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

}


