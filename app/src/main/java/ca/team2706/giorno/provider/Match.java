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
		short blue1 = in.readShort();
		short blue2 = in.readShort();
		short blue3 = in.readShort();
		short red1 = in.readShort();
		short red2 = in.readShort();
		short red3 = in.readShort();
		in.close();
		return new Match(number, rematch, blue1, blue2, blue3, red1, red2, red3);
	}

	public final short number;
	public final boolean rematch;
	public final short blue1;
	public final short blue2;
	public final short blue3;
	public final short red1;
	public final short red2;
	public final short red3;

	public Match(short number, boolean rematch, short blue1, short blue2, short blue3, short red1, short red2, short red3) {
		this.number = number;
		this.rematch = rematch;
		this.blue1 = blue1;
		this.blue2 = blue2;
		this.blue3 = blue3;
		this.red1 = red1;
		this.red2 = red2;
		this.red3 = red3;
	}

	byte[] encode() {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(new ByteArrayOutputStream());
			out.writeShort(number);
			out.writeBoolean(rematch);
			out.writeShort(blue1);
			out.writeShort(blue2);
			out.writeShort(blue3);
			out.writeShort(red1);
			out.writeShort(red2);
			out.writeShort(red3);
			out.close();
			return bout.toByteArray();
		}
		catch(IOException ex) {
			Log.wtf("Match" + number, "Failed to encode match", ex);
			return null;
		}
	}

}
