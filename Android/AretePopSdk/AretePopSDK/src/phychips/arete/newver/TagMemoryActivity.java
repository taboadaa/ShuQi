package phychips.arete.newver;

import phychips.arete.newver.R;

import com.phychips.rcp.*;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


/**
 * TagmemoryActivity extends Activity. It implements the GUI of the tag memory
 * screen (read, write, blockwrite, erase, lock, kill).
 * 
 * @author jeje0247
 * 
 */
public class TagMemoryActivity extends Activity implements iRcpEvent {
	TextView tagMemory, result;
	Button readmem, writemem, lock, kill;
	EditText password1, password2, password3, address;
	EditText length, data1;
	CheckBox[] mask = new CheckBox[20];
	Spinner target_memory;

	String string;

	private static Handler m_Handler;

	/**
	 * This is called when starting the tagmemory activity. It defines the GUI
	 * of the tagmemory activity.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		String tagstring;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tagmemory);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		RcpApi.setRcpEvent(this);

		TabHost tabhost = (TabHost) findViewById(R.id.tabhost);
		tabhost.setup();

		TabHost.TabSpec spec;
		spec = tabhost.newTabSpec("read/write/erase");
		spec.setContent(R.id.tagmemory_tab_read);
		spec.setIndicator("read/write/erase");
		tabhost.addTab(spec);

		spec = tabhost.newTabSpec("lock");
		spec.setContent(R.id.tagmemory_tab_lock);
		spec.setIndicator("lock");
		tabhost.addTab(spec);

		spec = tabhost.newTabSpec("kill");
		spec.setContent(R.id.tagmemory_tab_kill);
		spec.setIndicator("kill");
		tabhost.addTab(spec);
		tabhost.setCurrentTab(0);

		Intent intent = getIntent();

		tagstring = intent.getStringExtra("tagitem");

		tagMemory = (TextView) findViewById(R.id.selected_tag);
		result = (TextView) findViewById(R.id.result);

		tagMemory.setText(tagstring);
		result.setText("");

		target_memory = (Spinner) findViewById(R.id.target_memory);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				this, R.array.target_memory, R.layout.spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		target_memory.setAdapter(adapter1);

		password1 = (EditText) findViewById(R.id.password1);
		password2 = (EditText) findViewById(R.id.password2);
		password3 = (EditText) findViewById(R.id.password3);
		address = (EditText) findViewById(R.id.address);
		length = (EditText) findViewById(R.id.length);
		data1 = (EditText) findViewById(R.id.data1);

		password1.setText("00000000");
		password2.setText("00000000");
		password3.setText("00000000");
		address.setText("0");
		length.setText("0");
		data1.setText("");

		mask[0] = (CheckBox) findViewById(R.id.mask1);
		mask[1] = (CheckBox) findViewById(R.id.mask2);
		mask[2] = (CheckBox) findViewById(R.id.mask3);
		mask[3] = (CheckBox) findViewById(R.id.mask4);
		mask[4] = (CheckBox) findViewById(R.id.mask5);
		mask[5] = (CheckBox) findViewById(R.id.mask6);
		mask[6] = (CheckBox) findViewById(R.id.mask7);
		mask[7] = (CheckBox) findViewById(R.id.mask8);
		mask[8] = (CheckBox) findViewById(R.id.mask9);
		mask[9] = (CheckBox) findViewById(R.id.mask10);

		mask[10] = (CheckBox) findViewById(R.id.action1);
		mask[11] = (CheckBox) findViewById(R.id.action2);
		mask[12] = (CheckBox) findViewById(R.id.action3);
		mask[13] = (CheckBox) findViewById(R.id.action4);
		mask[14] = (CheckBox) findViewById(R.id.action5);
		mask[15] = (CheckBox) findViewById(R.id.action6);
		mask[16] = (CheckBox) findViewById(R.id.action7);
		mask[17] = (CheckBox) findViewById(R.id.action8);
		mask[18] = (CheckBox) findViewById(R.id.action9);
		mask[19] = (CheckBox) findViewById(R.id.action10);

		readmem = (Button) findViewById(R.id.read_tag);
		writemem = (Button) findViewById(R.id.write_tag);
		lock = (Button) findViewById(R.id.lock_tag);
		kill = (Button) findViewById(R.id.kill_tag);

		

		readmem.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (RcpApi.isOpen) {
					try {
						String epc = tagMemory.getText().toString();
						int data_length = Integer.parseInt(length.getText()
								.toString(), 16);
						RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2,
								data_length);

						tag.password = Long.parseLong(password1.getText()
								.toString(), 16);

						tag.epc = RcpLib.convertStringToByteArray(epc);

						tag.start_address = Integer.parseInt(address.getText()
								.toString(), 16);

						if (((String) target_memory.getSelectedItem())
								.equalsIgnoreCase("RFU") == true) {
							tag.memory_bank = 0;
						} else if (((String) target_memory.getSelectedItem())
								.equalsIgnoreCase("EPC") == true) {
							tag.memory_bank = 1;
						} else if (((String) target_memory.getSelectedItem())
								.equalsIgnoreCase("TID") == true) {
							tag.memory_bank = 2;
						} else if (((String) target_memory.getSelectedItem())
								.equalsIgnoreCase("USER") == true) {
							tag.memory_bank = 3;
						}

						RcpApi.readFromTagMemory(tag);
					} catch (Exception e) {
						result.setText(e.toString());
						Toast.makeText(TagMemoryActivity.this, e.toString(),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					// toast
					// warning_msg();
				}
			}
		});

		writemem.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (RcpApi.isOpen) {
					try {
						String epc = tagMemory.getText().toString();
						String data = data1.getText().toString();
						int data_length = Integer.parseInt(length.getText()
								.toString(), 16);
						RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2,
								data_length);

						tag.password = Long.parseLong(password1.getText()
								.toString(), 16);

						tag.epc = RcpLib.convertStringToByteArray(epc);

						tag.start_address = Integer.parseInt(address.getText()
								.toString(), 16);
						tag.data = RcpLib.convertStringToByteArray(data);

						if (((String) target_memory.getSelectedItem())
								.equalsIgnoreCase("RFU") == true) {
							tag.memory_bank = 0;
						} else if (((String) target_memory.getSelectedItem())
								.equalsIgnoreCase("EPC") == true) {
							tag.memory_bank = 1;
						} else if (((String) target_memory.getSelectedItem())
								.equalsIgnoreCase("TID") == true) {
							tag.memory_bank = 2;
						} else if (((String) target_memory.getSelectedItem())
								.equalsIgnoreCase("USER") == true) {
							tag.memory_bank = 3;
						}

						RcpApi.writeToTagMemory(tag);
					} catch (Exception e) {
						result.setText(e.toString());
						Toast.makeText(TagMemoryActivity.this, e.toString(),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					// toast
					// warning_msg();
				}
			}
		});

		lock.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (RcpApi.isOpen) {
					try {
						String epc = tagMemory.getText().toString();
						int i;
						RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2, 1);

						System.out.println("pass : "
								+ password2.getText().toString());

						tag.password = Long.parseLong(password2.getText()
								.toString(), 16);

						tag.epc = RcpLib.convertStringToByteArray(epc);

						tag.lock_mask = 0;

						for (i = 0; i < 20; i++) {
							if (mask[i].isChecked() == true) {
								tag.lock_mask |= (1 << (19 - i));
							}
						}

						RcpApi.lockTagMemory(tag);
					} catch (Exception e) {
						result.setText(e.toString());
						Toast.makeText(TagMemoryActivity.this, e.toString(),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					// toast
					// warning_msg();
				}
			}
		});

		kill.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (RcpApi.isOpen) {
					try {
						String epc = tagMemory.getText().toString();
						RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2, 1);

						tag.password = Long.parseLong(password3.getText()
								.toString(), 16);

						tag.epc = RcpLib.convertStringToByteArray(epc);

						tag.recom = 0;

						RcpApi.killTag(tag);
					} catch (Exception e) {
						result.setText(e.toString());
						Toast.makeText(TagMemoryActivity.this, e.toString(),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					// toast
					// warning_msg();
				}

			}
		});

		m_Handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					// result.setText("Success");
					Toast.makeText(TagMemoryActivity.this, "Success",
							Toast.LENGTH_SHORT).show();
					break;
				case 1:
					data1.setText(string);
					Toast.makeText(TagMemoryActivity.this, "Success",
							Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(TagMemoryActivity.this, "Failure",
							Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Toast.makeText(TagMemoryActivity.this, "Reader Opened",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				} // End switch
			}
		};
	}

	// protected void warning_msg()
	// {
	// Toast toast = new Toast(getApplicationContext());
	// LayoutInflater inflater = getLayoutInflater();
	// View view = inflater.inflate(R.layout.warning_toast,
	// (ViewGroup) findViewById(R.id.warning_toast_layout_id));
	// TextView text = (TextView) view.findViewById(R.id.warning_msg);
	// text.setTextColor(Color.WHITE);
	// toast.setView(view);
	// toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	// toast.setDuration(Toast.LENGTH_SHORT);
	// toast.show();
	// }

	@Override
	protected void onResume() {
		super.onResume();

		setVolumeMax();

		RcpApi.setRcpEvent(this);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);

		pref.getBoolean("RCPProtocal", false);
	}

	private void setVolumeMax() {
		AudioManager AudioManager = (AudioManager) getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);

		AudioManager
				.setStreamVolume(
						android.media.AudioManager.STREAM_MUSIC,
						AudioManager
								.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC),
						0);
	}

	@Override
	public void onTagReceived(int[] data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRegionReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSelectParamReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onQueryParamReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChannelReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFhLbtReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTxPowerLevelReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTagMemoryReceived(int[] data) {
		// TODO Auto-generated method stub

		string = RcpLib.int2str(data);

		m_Handler.sendEmptyMessage(1);
	}

	@Override
	public void onHoppingTableReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onModulationParamReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnticolParamReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTempReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRssiReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegistryItemReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessReceived(int[] data) {
		// TODO Auto-generated method stub
		m_Handler.sendEmptyMessage(0);
	}

	@Override
	public void onReaderInfoReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailureReceived(int[] data) {
		// TODO Auto-generated method stub
		m_Handler.sendEmptyMessage(2);
	}

	@Override
	public void onResetReceived(int[] data) {
		// TODO Auto-generated method stub
		m_Handler.sendEmptyMessage(3);
	}

	@Override
	public void onAuthenticat(int[] dest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBeepStateReceived(int[] dest) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onTestFerPacketReceived(int[] dest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdcReceived(int[] dest)
	{
	    // TODO Auto-generated method stub
	    
	}
}
