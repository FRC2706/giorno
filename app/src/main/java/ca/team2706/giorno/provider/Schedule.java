package ca.team2706.giorno.provider;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class Schedule {

	static Schedule decode(byte[] data) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		String name = in.readUTF();
		byte numTeams = in.readByte();
		short[] teams = new short[numTeams];
		for(int i = 0; i < numTeams; i++) {
			teams[i] = in.readShort();
		}
		in.close();
		return new Schedule(name, teams);
	}

	public final String name;
	public final short[] teams;

	public Schedule(String name, short[] teams) {
		this.name = name;
		this.teams = teams;
	}

	byte[] encode() {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(new ByteArrayOutputStream());
			out.writeUTF(name);
			out.writeByte(teams.length);
			for(short team: teams) {
				out.writeShort(team);
			}
			out.close();
			return bout.toByteArray();
		}
		catch(IOException ex) {
			Log.wtf("Schedule" + name, "Failed to encode schedule", ex);
			return null;
		}
	}

}
