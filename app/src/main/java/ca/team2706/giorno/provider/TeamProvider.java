package ca.team2706.giorno.provider;

import java.io.IOException;
import java.util.HashMap;

import ca.team2706.giorno.sync.SyncProvider;

public class TeamProvider implements SyncProvider {

	private HashMap<Short, Team> teams;

	public Team getTeam(short number) {
		return teams.get(number);
	}

	public HashMap<Short, Team> getTeams() {
		return new HashMap<>(teams);
	}

	public void addTeam(Team team) {
		teams.put(team.number, team);
	}

	@Override
	public String getProviderName() {
		return "teams";
	}

	@Override
	public byte[][] getSyncObjects() {
		byte[][] objects = new byte[teams.size()][];
		int i = 0;
		for(Team team: teams.values()) {
			objects[i] = team.encode();
			i++;
		}
		return objects;
	}

	@Override
	public void addSyncObject(byte[] object) throws IOException {
		addTeam(Team.decode(object));
	}

}
