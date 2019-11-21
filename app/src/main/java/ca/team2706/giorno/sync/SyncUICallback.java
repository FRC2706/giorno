package ca.team2706.giorno.sync;

public interface SyncUICallback {

	void onStart();
	void onStop();
	void onStatus(String status);

}
