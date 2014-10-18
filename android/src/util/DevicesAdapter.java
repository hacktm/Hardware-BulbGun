package util;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bulbgun.R;

public class DevicesAdapter extends BaseAdapter {
	private List<BluetoothDevice> pairedDevice;
	private Activity activity;
	private BluetoothDevice lastBluetoothDevice;
	private boolean lightOn = false;

	public DevicesAdapter(Activity activity,
			List<BluetoothDevice> pairedDevice,
			BluetoothDevice lastBluetoothDevice) {
		this.activity = activity;
		this.pairedDevice = pairedDevice;
		this.lastBluetoothDevice = lastBluetoothDevice;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pairedDevice.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pairedDevice.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = convertView;
		// reuse views
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.paired_devices_item, null);
			// configure view holder
			BluetoothDeviceHolder viewHolder = new BluetoothDeviceHolder();
			viewHolder.name = (TextView) rowView.findViewById(R.id.device_name);
			viewHolder.mac = (TextView) rowView.findViewById(R.id.device_mac);
			viewHolder.isConnected = (ImageView) rowView
					.findViewById(R.id.isConnected);
			rowView.setTag(viewHolder);
		}

		// fill data
		BluetoothDeviceHolder holder = (BluetoothDeviceHolder) rowView.getTag();

		holder.name.setText(pairedDevice.get(position).getName());
		holder.mac.setText(pairedDevice.get(position).getAddress());

		if (lastBluetoothDevice != null) {
			if (!pairedDevice.get(position).getAddress()
					.equals(lastBluetoothDevice.getAddress())) {

				Log.d("holder.isConnected", String.valueOf(position));
				holder.isConnected.setVisibility(View.INVISIBLE);
			}
		} else {
			holder.isConnected.setVisibility(View.INVISIBLE);
		}

		rowView.setOnClickListener(clickNews(position, false));

		return rowView;
	}

	public OnClickListener clickNews(final int position, final boolean more_news) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				((ConnectDevice) activity).connectDevice(position);
			}

		};
	}

}

class BluetoothDeviceHolder {
	public TextView name;
	public TextView mac;
	public ImageView isConnected;
}
