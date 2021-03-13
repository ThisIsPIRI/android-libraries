package com.thisispiri.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**A {@code DialogFragment} for finding and connecting to another Android device on LAN.
 * When connecting as the server, it will show the Wifi adapter's IP address and prompt the user to enable Wifi if there isn't one;
 * however, the actual connection is not dependent on Wifi and will work on other interfaces.
 * The calling {@code Context} must implement {@code DialogListener} to receive the results and call {@code setArguments()} on this.
 * If either isn't fulfilled, it will throw an exception.
 * Needs INTERNET, ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE permissions.*/
public class IpConnectDialogFragment extends ListenerDialogFragment {
	private RadioButton radioClient;
	private EditText ipEditText;
	private ProgressBar progressBar;
	private Button connectButton;
	private Thread runningThread;
	private String isServerString;
	private WifiManager wifi;
	private ConnectivityManager connectivity;
	private final int port;

	/**@param port The port to listen on/connect to.*/
	public IpConnectDialogFragment(final int port) {
		super();
		this.port = port;
	}
	@Override @NonNull public Dialog onCreateDialog(final Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		wifi = ContextCompat.getSystemService(getContext().getApplicationContext(), WifiManager.class);
		connectivity = ContextCompat.getSystemService(getContext().getApplicationContext(), ConnectivityManager.class);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.piri_dialogs_layout_ipconnect, null);
		builder.setView(view);
		builder.setMessage(R.string.piri_dialogs_multiplayerOnLan);
		builder.setNegativeButton(R.string.piri_dialogs_cancel, (dialog, id) -> giveSocket(null, false)); //return to local if the user cancels connection
		radioClient = view.findViewById(R.id.piri_dialogs_connectRadioClient);
		ipEditText = view.findViewById(R.id.piri_dialogs_connectIpEditText);
		progressBar = view.findViewById(R.id.piri_dialogs_connectProgress);
		connectButton = view.findViewById(R.id.piri_dialogs_connectButton);
		connectButton.setOnClickListener(new ConnectButtonListener());
		((RadioGroup)view.findViewById(R.id.piri_dialogs_connectRadioMethod)).setOnCheckedChangeListener(new RadioListener());
		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setOnKeyListener(new BackListener());
		return dialog;
	}
	@Override public void onStart() {
		super.onStart();
		isServerString = getString(R.string.piri_dialogs_isServer);
		if(!radioClient.isChecked()) showIpAddress();
	}
	private void showIpAddress() {
		if(wifi == null)
			ipEditText.setText(R.string.piri_dialogs_noWifiSupport);
		else if(!wifi.isWifiEnabled())
			ipEditText.setText(R.string.piri_dialogs_pleaseEnableWifi);
		else if(!connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected())
			ipEditText.setText(R.string.piri_dialogs_pleaseConnectAp);
		else {
			//TODO: IPv6 support, replace deprecated getIpAddress() and formatIpAddress()
			final int address = wifi.getConnectionInfo().getIpAddress();
			if(address == 0)
				ipEditText.setText(R.string.piri_dialogs_pleaseCheckWifiConnection);
			else
				ipEditText.setText(Formatter.formatIpAddress(address));
		}
	}
	private class RadioListener implements RadioGroup.OnCheckedChangeListener {
		@Override public void onCheckedChanged(RadioGroup group, int id) {
			if(runningThread != null) runningThread.interrupt();
			final int checkedRadioButtonId = group.getCheckedRadioButtonId();
			if(checkedRadioButtonId == R.id.piri_dialogs_connectRadioServer) {
				showIpAddress();
				ipEditText.setFocusable(false);
				ipEditText.setFocusableInTouchMode(false);
				ipEditText.clearFocus();
				connectButton.setText(R.string.piri_dialogs_connectFirst);
			}
			else if(checkedRadioButtonId == R.id.piri_dialogs_connectRadioClient) {
				ipEditText.setText("");
				ipEditText.setFocusable(true);
				ipEditText.setFocusableInTouchMode(true);
				connectButton.setText(R.string.piri_dialogs_connectSecond);
			}
			progressBar.setVisibility(View.GONE);
		}
	}
	private class ConnectButtonListener implements View.OnClickListener {
		@Override public void onClick(final View v) {
			progressBar.setVisibility(View.VISIBLE);
			if(runningThread != null) runningThread.interrupt();
			if(radioClient.isChecked()) {
				runningThread = new Thread() { public void run() {
					try {
						Socket socket = new Socket(ipEditText.getText().toString(), port);
						giveSocket(socket, false);
						dismiss();
					}
					catch(IOException e) {
						giveSocket(null, false);
						getActivity().runOnUiThread(() -> {
							Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
							progressBar.setVisibility(View.GONE);
						});
					}
				}};
			}
			else {
				showIpAddress();
				runningThread = new Thread() { public void run() {
					try(ServerSocket serverSocket = new ServerSocket(port)) {
						//If there's no timeout accept() will block forever even when the Thread is interrupted, never releasing the port.
						serverSocket.setSoTimeout(8000);
						while(!radioClient.isChecked()) {
							Socket socket = null;
							try {
								socket = serverSocket.accept();
							}
							catch(SocketTimeoutException timeout) {}
							if(socket != null) {
								giveSocket(socket, true);
								dismiss();
								break;
							}
							if(interrupted()) break;
						}
					}
					catch(IOException e) {
						getActivity().runOnUiThread(() -> {
							Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
							progressBar.setVisibility(View.GONE);
						});
					}
				}};
			}
			runningThread.start();
		}
	}
	private void giveSocket(final Socket socket, final boolean isServer) {
		getArguments().putBoolean(isServerString, isServer);
		getListener().giveResult(socket, getArguments());
	}
	@Override public void onDestroy() {
		if(runningThread != null) runningThread.interrupt();
		super.onDestroy();
	}
	private class BackListener implements DialogInterface.OnKeyListener {
		@Override public boolean onKey(final DialogInterface dialog, final int keyCode, final KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
				giveSocket(null, false); //return to local if the user cancels connection
			return false;
		}
	}
}
