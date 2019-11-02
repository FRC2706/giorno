package ca.team2706.giorno.sync;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IPSync implements SyncImplementation {

	private static String SERVICE_NAME = "giorno";
	private static String SERVICE_TYPE = "_giorno._tcp";

	private ServerSocket ss;
	private String serviceName = SERVICE_NAME;
	private SyncManager manager;

	// Nsd
	private NsdManager nsdManager;
	private NsdManager.RegistrationListener registrationListener;
	private NsdManager.ResolveListener resolveListener;
	private NsdManager.DiscoveryListener discoveryListener;

	public void startSync(Context context, SyncManager manager) {
		this.manager = manager;

		// Open ServerSocket
		try {
			ss = new ServerSocket(0);
		}
		catch(IOException ex) {
			Log.e("IPSync", "Failed to create server socket");
			return;
		}

		// Create Nsd registration listener
		nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
		if(nsdManager == null) {
			Log.e("IPSync", "Failed to get NsdManager");
			return;
		}
		registrationListener = new NsdManager.RegistrationListener() {

			@Override
			public void onServiceRegistered(NsdServiceInfo serviceInfo) {
				serviceName = serviceInfo.getServiceName();
			}

			@Override
			public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
				Log.e("IPSync", "Failed to register Nsd service");
			}

			@Override
			public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {}

			@Override
			public void onServiceUnregistered(NsdServiceInfo serviceInfo) {}
		};

		// Create Nsd resolve listener
		resolveListener = new NsdManager.ResolveListener() {
			@Override
			public void onServiceResolved(NsdServiceInfo serviceInfo) {
				if(!serviceInfo.getServiceName().equals(serviceName)) {
					try {
						Socket socket = new Socket(serviceInfo.getHost(), serviceInfo.getPort());
						syncWith(socket);
					}
					catch(IOException ex) {
						Log.w("IPSync", "Failed to sync with peer", ex);
					}
				}
			}

			@Override
			public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {}
		};

		// Create Nsd discovery listener
		discoveryListener = new NsdManager.DiscoveryListener() {
			@Override
			public void onStartDiscoveryFailed(String serviceType, int errorCode) {
				Log.e("IPSync", "Failed to start service discovery");
			}

			@Override
			public void onServiceFound(NsdServiceInfo serviceInfo) {
				if(serviceInfo.getServiceType().equals(SERVICE_TYPE)) {
					nsdManager.resolveService(serviceInfo, resolveListener);
				}
			}

			@Override
			public void onServiceLost(NsdServiceInfo serviceInfo) {}

			@Override
			public void onDiscoveryStopped(String serviceType) {}

			@Override
			public void onStopDiscoveryFailed(String serviceType, int errorCode) {}

			@Override
			public void onDiscoveryStarted(String serviceType) {}
		};

		// Register Nsd service
		NsdServiceInfo serviceInfo = new NsdServiceInfo();
		serviceInfo.setServiceName(SERVICE_NAME);
		serviceInfo.setServiceType(SERVICE_TYPE);
		serviceInfo.setPort(ss.getLocalPort());
		nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
		nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);

		// TODO: Accept connections from ss
	}

	private void syncWith(Socket socket) throws IOException {
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		DataInputStream in = new DataInputStream(socket.getInputStream());
		manager.writeHandshake(out);
		manager.writeResponse(out, in);
		manager.readResponse(in);
		socket.close();
	}

	public void stopSync() {
		nsdManager.stopServiceDiscovery(discoveryListener);
		nsdManager.unregisterService(registrationListener);
	}

}
