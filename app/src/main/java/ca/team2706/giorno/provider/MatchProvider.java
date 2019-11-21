package ca.team2706.giorno.provider;

import java.io.IOException;
import java.util.HashMap;

import ca.team2706.giorno.sync.SyncProvider;

public class MatchProvider implements SyncProvider {

	private HashMap<Short, Match> matches;

	public Match getMatch(Short number) {
		return matches.get(number);
	}

	public HashMap<Short, Match> getMatches() {
		return new HashMap<>(matches);
	}

	public void addMatch(Match match) {
		matches.put(match.number, match);
	}

	@Override
	public String getProviderName() {
		return "matches";
	}

	@Override
	public byte[][] getSyncObjects() {
		byte[][] objects = new byte[matches.size()][];
		int i = 0;
		for(Match match: matches.values()) {
			objects[i] = match.encode();
			i++;
		}
		return objects;
	}

	@Override
	public void addSyncObject(byte[] object) throws IOException {
		addMatch(Match.decode(object));
	}

}
