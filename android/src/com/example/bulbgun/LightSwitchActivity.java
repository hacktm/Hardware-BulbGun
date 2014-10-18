package com.example.bulbgun;

import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;

import util.BluetoothInteraction;
import util.BluetoothSetup;
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
import android.widget.Toast;
import android.widget.ToggleButton;

public class LightSwitchActivity extends Activity implements
		BluetoothInteraction {

	private final static int REQUEST_ENABLE_BT = 1;
	private List<BluetoothDevice> myDevices;
	private CharSequence mTitle;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice lastBluetoothDevice;
	private Button connect_btn;

	private BluetoothSetup bs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light_switch);

		mTitle = getTitle();
		bs = new BluetoothSetup(this, this);
		connect_btn = (Button) findViewById(R.id.connect_btn);
		connect_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						DevicesActivity.class);
				if (lastBluetoothDevice != null)
					i.putExtra("last_mac", lastBluetoothDevice.getAddress());
				startActivityForResult(i, 1);
			}
		});
	}

	public void onToggleClicked(View view) {
		// Is the toggle on?
		boolean on = ((ToggleButton) view).isChecked();
		if (lastBluetoothDevice == null) {
			Toast.makeText(this, "Please connect first!", Toast.LENGTH_LONG)
					.show();
			((ToggleButton) view).setChecked(!on);
		} else {

			turnLight(on);
		}
	}

	public void turnLight(boolean on) {
		Log.d("turn light:", String.valueOf(on));
		if (bs != null) {
			try {
				if (on) {
					bs.sendData("1");

				} else {
					bs.sendData("0");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first
		Log.d("onResume", "--------------------");

		turnBluetoothOn();

	}

	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first

	}

	@Override
	public void onDestroy() {
		super.onDestroy(); // Always call the superclass method first
		try {
			bs.closeBT();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void turnBluetoothOn() {
		Log.d("turnBluetoothOn", "--------------------");
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
		myDevices = BluetoothSetup.findPairedDevices();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.d("onActivityResult", "--------------------");

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("selected_mac");
				bs = new BluetoothSetup(this, this);
				for (BluetoothDevice b : myDevices) {
					if (b.getAddress().equals(result))
						lastBluetoothDevice = b;
				}
				if (lastBluetoothDevice != null) {
					try {
						bs.closeBT();
						lastBluetoothDevice = bs.openBT(lastBluetoothDevice);
						if (lastBluetoothDevice == null) {
							Toast.makeText(this, "Connection failed",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(
									this,
									"Connect to device: "
											+ lastBluetoothDevice.getName(),
									Toast.LENGTH_SHORT).show();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}// onActivityResult

	@Override
	public void display(String msg) {
		// TODO Auto-generated method stub
		ToggleButton tb = (ToggleButton) findViewById(R.id.toggle);
		if (msg.equals("0")) {
			tb.setChecked(false);
		} else if (msg.equals("1")) {
			tb.setChecked(true);
		}
	}

}
