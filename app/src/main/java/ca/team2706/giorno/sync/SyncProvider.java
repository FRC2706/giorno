package ca.team2706.giorno.sync;

public interface SyncProvider {

	String getProviderName();
	byte[][] getSyncObjects();
	void addSyncObject(byte[] object);

}
