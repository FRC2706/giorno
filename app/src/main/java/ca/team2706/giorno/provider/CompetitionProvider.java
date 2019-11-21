package ca.team2706.giorno.provider;

import java.io.IOException;
import java.util.HashMap;

import ca.team2706.giorno.sync.SyncProvider;

public class CompetitionProvider implements SyncProvider {

	private HashMap<String, Competition> comps;

	public Competition getCompetition(String name) {
		return comps.get(name);
	}

	public HashMap<String, Competition> getCompetitions() {
		return new HashMap<>(comps);
	}

	public void addCompetition(Competition comp) {
		comps.put(comp.name, comp);
	}

	@Override
	public String getProviderName() {
		return "competitions";
	}

	@Override
	public byte[][] getSyncObjects() {
		byte[][] objects = new byte[comps.size()][];
		int i = 0;
		for(Competition comp: comps.values()) {
			objects[i] = comp.encode();
			i++;
		}
		return objects;
	}

	@Override
	public void addSyncObject(byte[] object) throws IOException {
		addCompetition(Competition.decode(object));
	}

}
