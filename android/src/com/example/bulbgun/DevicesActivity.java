package com.example.bulbgun;

import java.util.ArrayList;
import java.util.Set;

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
import android.view.MenuItem;
import android.widget.ListView;

public class DevicesActivity extends Activity implements ConnectDevice {

	private ListView devices_list;
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayList<BluetoothDevice> myDevices;
	private DevicesAdapter devicesAdapter;
	private String lastBluetoothDeviceMac;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_devices);

		devices_list = (ListView) findViewById(R.id.devices_list);
		myDevices = BluetoothSetup.findPairedDevices();

		Intent intent = getIntent();
		String lastBluetoothDeviceMac = intent.getStringExtra("last_mac");

		devicesAdapter = new DevicesAdapter(this, myDevices,
				lastBluetoothDeviceMac);
		devices_list.setAdapter(devicesAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.devices, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void connectDevice(int position) {

		Intent returnIntent = new Intent();
		returnIntent.putExtra("selected_mac", myDevices.get(position)
				.getAddress());
		setResult(RESULT_OK, returnIntent);
		finish();

	}

}
