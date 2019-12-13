package ca.team2706.giorno.sync;

import android.content.Context;

public interface SyncImplementation {

	void startSync(Context context, SyncManager manager);
	void stopSync();

}
