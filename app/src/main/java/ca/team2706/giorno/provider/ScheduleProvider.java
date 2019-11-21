package ca.team2706.giorno.provider;

import java.io.IOException;
import java.util.HashMap;

import ca.team2706.giorno.sync.SyncProvider;

public class ScheduleProvider implements SyncProvider {

	private HashMap<String, Schedule> schedules;

	public Schedule getSchedule(String name) {
		return schedules.get(name);
	}

	public HashMap<String, Schedule> getSchedules() {
		return new HashMap<>(schedules);
	}

	public void addSchedule(Schedule schedule) {
		schedules.put(schedule.name, schedule);
	}

	@Override
	public String getProviderName() {
		return "schedules";
	}

	@Override
	public byte[][] getSyncObjects() {
		byte[][] objects = new byte[schedules.size()][];
		int i = 0;
		for(Schedule schedule: schedules.values()) {
			objects[i] = schedule.encode();
			i++;
		}
		return objects;
	}

	@Override
	public void addSyncObject(byte[] object) throws IOException {
		addSchedule(Schedule.decode(object));
	}

}
