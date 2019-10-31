package ca.team2706.giorno;

public interface SyncProvider {

	String getProviderName();
	byte[][] getSyncObjects();
	void addSyncObject(byte[] object);

}
