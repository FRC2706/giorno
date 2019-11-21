package ca.team2706.giorno.provider;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class Competition {

	static Competition decode(byte[] data) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		String name = in.readUTF();
		String location = in.readUTF();
		String description = in.readUTF();
		byte numTeams = in.readByte();
		short[] teams = new short[numTeams];
		for(int i = 0; i < numTeams; i++) {
			teams[i] = in.readShort();
		}
		short numMatches = in.readShort();
		long startTime = in.readLong();
		long endTime = in.readLong();
		in.close();
		return new Competition(name, location, description, teams, numMatches, startTime, endTime);
	}

	public final String name;
	public final String location;
	public final String description;
	public final short[] teams;
	public final short numMatches;
	public final long startTime;
	public final long endTime;

	public Competition(String name, String location, String description, short[] teams, short numMatches, long startTime, long endTime) {
		this.name = name;
		this.location = location;
		this.description = description;
		this.teams = teams;
		this.numMatches = numMatches;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	byte[] encode() {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(new ByteArrayOutputStream());
			out.writeUTF(name);
			out.writeUTF(location);
			out.writeUTF(description);
			out.writeByte(teams.length);
			for(short team: teams) {
				out.writeShort(team);
			}
			out.writeShort(numMatches);
			out.writeLong(startTime);
			out.writeLong(endTime);
			out.close();
			return bout.toByteArray();
		}
		catch(IOException ex) {
			Log.wtf("Competition" + name, "Failed to encode competition", ex);
			return null;
		}
	}

}
