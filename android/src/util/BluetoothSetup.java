package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class BluetoothSetup {
	private Activity act;

	BluetoothSocket mmSocket;
	OutputStream mmOutputStream;
	InputStream mmInputStream;

	byte[] readBuffer;
	int readBufferPosition;

	Thread workerThread;
	volatile boolean stopWorker;

	private BluetoothInteraction bi;

	public BluetoothSetup(Activity act, BluetoothInteraction bi) {
		super();
		this.act = act;
		this.bi = bi;
	}

	private final static int REQUEST_ENABLE_BT = 1;

	public BluetoothDevice openBT(BluetoothDevice mmDevice) throws IOException {
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // Standard
		try { // SerialPortService
				// ID
			mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
			mmSocket.connect();
			mmOutputStream = mmSocket.getOutputStream();
			mmInputStream = mmSocket.getInputStream();

			beginListenForData();

		} catch (IOException e) {
			Log.d("BluetoothSetup", "Bluetooth Opened - fail");
			return null;
		}

		Log.d("BluetoothSetup", "Bluetooth Opened");
		return mmDevice;

	}

	void beginListenForData() {
		final Handler handler = new Handler();
		final byte delimiter = 10; // This is the ASCII code for a newline
									// character

		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[1024];
		workerThread = new Thread(new Runnable() {
			public void run() {
				while (!Thread.currentThread().isInterrupted() && !stopWorker) {
					try {
						int bytesAvailable = mmInputStream.available();
						if (bytesAvailable > 0) {
							byte[] packetBytes = new byte[bytesAvailable];
							mmInputStream.read(packetBytes);
							for (int i = 0; i < bytesAvailable; i++) {
								byte b = packetBytes[i];
								if (b == delimiter) {
									byte[] encodedBytes = new byte[readBufferPosition];
									System.arraycopy(readBuffer, 0,
											encodedBytes, 0,
											encodedBytes.length);
									final String data = new String(
											encodedBytes, "US-ASCII");
									readBufferPosition = 0;

									handler.post(new Runnable() {
										public void run() {
											Log.d("BluetoothSetup",
													"Data Recieved: " + data);
											bi.display(data);
										}
									});
								} else {
									readBuffer[readBufferPosition++] = b;
								}
							}
						}
					} catch (IOException ex) {

						stopWorker = true;
					}
				}
			}
		});

		workerThread.start();
	}

	public void sendData(String msg) throws IOException {

		msg += "\n";
		mmOutputStream.write(msg.getBytes());

		Log.d("BluetoothSetup", "Data Sent");
	}

	public void closeBT() throws IOException {
		stopWorker = true;
		try {
			mmOutputStream.close();
			mmInputStream.close();
			mmSocket.close();
		} catch (NullPointerException e) {
			Log.d("BluetoothSetup", "Bluetooth Closed already");
		}
		Log.d("BluetoothSetup", "Bluetooth Closed");

	}

	// return a list of paired devices
	public static ArrayList<BluetoothDevice> findPairedDevices() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();

		ArrayList<BluetoothDevice> myDevices = new ArrayList<BluetoothDevice>();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView
				if (device != null) {
					// mArrayAdapter.add(device.getName() + "\n"
					// + device.getAddress());
					Log.d(device.getName().toString(), device.getAddress()
							.toString());

					myDevices.add(device);
				}
			}
			return myDevices;
		}
		return null;
	}

}
