package ca.team2706.giorno.sync;

import java.io.IOException;

public interface SyncProvider {

	String getProviderName();
	byte[][] getSyncObjects();
	void addSyncObject(byte[] object) throws IOException;

}
