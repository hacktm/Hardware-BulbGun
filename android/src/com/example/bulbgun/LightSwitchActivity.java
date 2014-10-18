package com.example.bulbgun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TooManyListenersException;

import util.BluetoothInteraction;
import util.BluetoothSetup;
import util.ConnectDevice;
import util.DevicesAdapter;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LightSwitchActivity extends Activity implements ConnectDevice,
		BluetoothInteraction {

	private final static int REQUEST_ENABLE_BT = 1;
	private List<BluetoothDevice> myDevices;
	private CharSequence mTitle;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice lastBluetoothDevice;
	private Button connect_btn;
	private RelativeLayout connect_layout;
	private ListView devices_list;
	private DevicesAdapter devicesAdapter;
	private RelativeLayout switch_layout;
	private BluetoothSetup bs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light_switch);

		mTitle = getTitle();

		switch_layout = (RelativeLayout) findViewById(R.id.switch_layout);
		connect_layout = (RelativeLayout) findViewById(R.id.connect_layout);
		devices_list = (ListView) findViewById(R.id.devices_list);

		bs = new BluetoothSetup(this, this);

		connect_btn = (Button) findViewById(R.id.connect_btn);
		connect_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				findPairedDevices();
				showList();
			}
		});
	}

	public void onToggleClicked(View view) {
		// Is the toggle on?
		boolean on = ((ToggleButton) view).isChecked();
		turnLight(on);
	}

	public void turnLight(boolean on) {
		Log.d("turn light:", String.valueOf(on));
	}

	protected void showSwitch() {
		// TODO Auto-generated method stub
		switch_layout.setVisibility(View.VISIBLE);
		connect_layout.setVisibility(View.INVISIBLE);

	}

	protected void showList() {
		// TODO Auto-generated method stub
		devicesAdapter = new DevicesAdapter(this, myDevices,
				lastBluetoothDevice);
		devices_list.setAdapter(devicesAdapter);
		switch_layout.setVisibility(View.INVISIBLE);
		connect_layout.setVisibility(View.VISIBLE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first
		turnBluetoothOn();

	}

	@Override
	public void onDestroy() {
		super.onDestroy(); // Always call the superclass method first

	}

	public void turnBluetoothOn() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(getApplicationContext(),
					"Device does not support Bluetooth", Toast.LENGTH_LONG)
					.show();
		}

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

	}

	public void findPairedDevices() {
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();

		myDevices = new ArrayList<BluetoothDevice>();
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
		}
	}

	@Override
	public void connectDevice(int position) {

		showSwitch();

		try {
			bs.closeBT();
			lastBluetoothDevice = bs.openBT(myDevices.get(position));
			if (lastBluetoothDevice == null) {
				Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(
						this,
						"Connect to device: "
								+ myDevices.get(position).getName(),
						Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void display(String msg) {
		// TODO Auto-generated method stub

	}

}
