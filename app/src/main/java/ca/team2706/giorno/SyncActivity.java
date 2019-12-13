package ca.team2706.giorno;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ca.team2706.giorno.sync.IPSync;
import ca.team2706.giorno.sync.SyncManager;

public class SyncActivity extends AppCompatActivity {

	private SyncManager syncManager;
	private IPSync sync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync);
		syncManager = new SyncManager();
		sync = new IPSync();
		sync.startSync(getApplicationContext(), syncManager);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sync.stopSync();
	}
}
