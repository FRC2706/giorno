package ca.team2706.giorno.provider;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Team {

	static Team decode(byte[] data) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		short number = in.readShort();
		String name = in.readUTF();
		String location = in.readUTF();
		String description = in.readUTF();
		short rookieYear = in.readShort();
		in.close();
		return new Team(number, name, location, description, rookieYear);
	}

	public final short number;
	public final String name;
	public final String location;
	public final String description;
	public final short rookieYear;

	public Team(short number, String name, String location, String description, short rookieYear) {
		this.number = number;
		this.name = name;
		this.location = location;
		this.description = description;
		this.rookieYear = rookieYear;
	}

	byte[] encode() {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(new ByteArrayOutputStream());
			out.writeShort(number);
			out.writeUTF(name);
			out.writeUTF(location);
			out.writeUTF(description);
			out.writeShort(rookieYear);
			out.close();
			return bout.toByteArray();
		}
		catch(IOException ex) {
			Log.wtf("Team" + number, "Failed to encode team", ex);
			return null;
		}
	}

}
