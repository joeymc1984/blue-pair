package co.aurasphere.bluepair.view;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.aurasphere.bluepair.R;
import co.aurasphere.bluepair.bluetooth.BluetoothController;
import co.aurasphere.bluepair.bluetooth.BluetoothDiscoveryDeviceListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BluetoothDevice} and makes a call to the
 * specified {@link ListInteractionListener} when the element is tapped.
 *
 * @author Donato Rimenti
 */
public class DeviceRecyclerViewAdapter
        extends RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder>
        implements BluetoothDiscoveryDeviceListener {

    /**
     * The devices shown in this {@link RecyclerView}.
     */
    private final List<BluetoothDevice> devices;

    /**
     * Callback for handling interaction events.
     */
    private final ListInteractionListener<BluetoothDevice> listener;

    /**
     * Controller for Bluetooth functionalities.
     */
    private BluetoothController bluetooth;

    /**
     * Instantiates a new DeviceRecyclerViewAdapter.
     *
     * @param listener an handler for interaction events.
     */
    public DeviceRecyclerViewAdapter(ListInteractionListener<BluetoothDevice> listener) {
        this.devices = new ArrayList<>();
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_device_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = devices.get(position);
        holder.mImageView.setImageResource(getDeviceIcon(devices.get(position)));
        holder.mDeviceNameView.setText(devices.get(position).getName());
        holder.mDeviceAddressView.setText(devices.get(position).getAddress());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onItemClick(holder.mItem);
                }
            }
        });
    }

    /**
     * Returns the icon shown on the left of the device inside the list.
     *
     * @param device the device for the icon to get.
     * @return a resource drawable id for the device icon.
     */
    private final int getDeviceIcon(BluetoothDevice device) {
        if (bluetooth.isAlreadyPaired(device)) {
            return R.drawable.ic_bluetooth_connected_black_24dp;
        } else {
            return R.drawable.ic_bluetooth_disabled_black_24dp;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return devices.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDeviceDiscovered(BluetoothDevice device) {
        listener.onLoadingEnded(false);
        devices.add(device);
        notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDeviceDiscoveryStarted() {
        devices.clear();
        notifyDataSetChanged();
        listener.onLoadingStarted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBluetoothController(BluetoothController bluetooth) {
        this.bluetooth = bluetooth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiscoveryEnd() {
        listener.onLoadingEnded(true);
    }

    /**
     * ViewHolder for a BluetoothDevice.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The inflated view of this ViewHolder.
         */
        final View mView;

        /**
         * The icon of the device.
         */
        final ImageView mImageView;

        /**
         * The name of the device.
         */
        final TextView mDeviceNameView;

        /**
         * The MAC address of the BluetoothDevice.
         */
        final TextView mDeviceAddressView;

        /**
         * The item of this ViewHolder.
         */
        BluetoothDevice mItem;

        /**
         * Instantiates a new ViewHolder.
         *
         * @param view the inflated view of this ViewHolder.
         */
        ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.device_icon);
            mDeviceNameView = (TextView) view.findViewById(R.id.device_name);
            mDeviceAddressView = (TextView) view.findViewById(R.id.device_address);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return super.toString() + " '" + BluetoothController.deviceToString(mItem) + "'";
        }
    }
}
