package ca.team2706.giorno;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.team2706.giorno.sync.IPSync;
import ca.team2706.giorno.sync.SyncManager;
import ca.team2706.giorno.sync.SyncUICallback;

public class SyncActivity extends AppCompatActivity {

	private SyncManager syncManager;
	private IPSync sync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync);
		syncManager = new SyncManager();
		GiornoApplication app = (GiornoApplication) getApplication();
		syncManager.registerProvider(app.competitionProvider);
		syncManager.registerProvider(app.teamProvider);
		sync = new IPSync();
		sync.startSync(getApplicationContext(), syncManager, new SyncUICallback() {
			@Override
			public void onStart() {

			}

			@Override
			public void onStop() {

			}

			@Override
			public void onStatus(String s) {
				final String status = s;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.i("SyncActivity", "Status update from UI thread: " + status);
						TextView v = findViewById(R.id.status);
						v.setText(status);
					}
				});
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sync.stopSync();
	}
}
