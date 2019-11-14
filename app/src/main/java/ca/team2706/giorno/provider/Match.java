package ca.team2706.giorno.provider;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class Match {

	static Match decode(byte[] data) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		short number = in.readShort();
		boolean rematch = in.readBoolean();
		byte numTeams = in.readByte();
		short[] teams = new short[numTeams];
		for(int i = 0; i < numTeams; i++) {
			teams[i] = in.readShort();
		}
		in.close();
		return new Match(number, rematch, teams);
	}

	public final short number;
	public final boolean rematch;
	public final short[] teams;

	public Match(short number, boolean rematch, short[] teams) {
		this.number = number;
		this.rematch = rematch;
		this.teams = teams;
	}

	byte[] encode() {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(new ByteArrayOutputStream());
			out.writeShort(number);
			out.writeBoolean(rematch);
			out.writeByte(teams.length);
			for(short team: teams) {
				out.writeShort(team);
			}
			out.close();
			return bout.toByteArray();
		}
		catch(IOException ex) {
			Log.wtf("Match" + number, "Failed to encode match", ex);
			return null;
		}
	}

}
