package ca.team2706.giorno;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SyncManager {

	public static int MD5_LENGTH = 16;

	private HashMap<String, SyncProvider> providers = new HashMap<>();

	public void registerProvider(SyncProvider provider) {
		providers.put(provider.getProviderName(), provider);
	}

	public void writeHandshake(DataOutputStream out) throws IOException {
		out.writeInt(providers.size());
		for(SyncProvider provider: providers.values()) {
			out.writeUTF(provider.getProviderName());
			byte[][] objects = provider.getSyncObjects();
			out.writeInt(objects.length);
			for(byte[] object: objects) {
				out.write(generateChecksum(object));
			}
		}
	}

	public void writeResponse(DataOutputStream out, DataInputStream in) throws IOException {

		// Collect objects we have to send them
		HashMap<String, ArrayList<byte[]>> toSend = new HashMap<>();
		for(SyncProvider provider: providers.values()) {
			toSend.put(provider.getProviderName(), new ArrayList<>(Arrays.asList(provider.getSyncObjects())));
		}

		// Parse handshake to eliminate objects they already have
		int numProviders = in.readInt();
		for(int i = 0; i < numProviders; i++) {
			String name = in.readUTF();
			int numObjects = in.readInt();
			if(providers.containsKey(name)) {
				for(int j = 0; j < numObjects; j++) {
					byte[] checksum = new byte[MD5_LENGTH];
					in.readFully(checksum);
					ArrayList<byte[]> objects = toSend.get(name);
					for(byte[] object: objects) {
						if(Arrays.equals(checksum, generateChecksum(object))) {
							objects.remove(object);
							if(objects.size() < 1) {
								toSend.remove(objects);
							}
						}
					}
				}
			}
			else {
				in.skipBytes(numObjects * MD5_LENGTH);
			}
		}

		// Send objects
		out.writeInt(toSend.size());
		for(String name: toSend.keySet()) {
			out.writeUTF(name);
			ArrayList<byte[]> objects = toSend.get(name);
			out.writeInt(objects.size());
			for(byte[] object: objects) {
				out.writeInt(object.length);
				out.write(object);
			}
		}
	}

	public void readResponse(DataInputStream in) throws IOException {

		// Calculate checksums of the objects we have
		HashMap<String, ArrayList<byte[]>> checksums = new HashMap<>();
		for(SyncProvider provider: providers.values()) {
			ArrayList<byte[]> sums = new ArrayList<>();
			for(byte[] object: provider.getSyncObjects()) {
				sums.add(generateChecksum(object));
			}
			checksums.put(provider.getProviderName(), sums);
		}

		// Parse objects
		int numProviders = in.readInt();
		for(int i = 0; i < numProviders; i++) {
			String name = in.readUTF();
			int numObjects = in.readInt();
			for(int j = 0; j < numObjects; j++) {
				int length = in.readInt();
				if(providers.containsKey(name)) {
					byte[] object = new byte[length];
					in.readFully(object);
					boolean isDuplicate = false;
					for(byte[] sum: checksums.get(name)) {
						if(Arrays.equals(generateChecksum(object), sum)) {
							isDuplicate = true;
							break;
						}
					}
					if(!isDuplicate) {
						providers.get(name).addSyncObject(object);
					}
				}
				else {
					in.skipBytes(length);
				}
			}
		}
	}

	private byte[] generateChecksum(byte[] data) {
		MessageDigest mdEnc;
		try {
			mdEnc = MessageDigest.getInstance("MD5");
		}
		catch(NoSuchAlgorithmException ex) {
			Log.wtf("GenerateChecksum", "Could not get MD5 digest");
			return new byte[MD5_LENGTH];
		}
		mdEnc.update(data, 0, data.length);
		return mdEnc.digest();
	}

}
