package ena;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.Window.Type;

import javax.swing.JButton;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;

import javax.swing.JProgressBar;

public class Settings {

	private JFrame frmSettings;
	private JProgressBar progressBarSettingsStationScan;
	private Persistent persistent;
	private RemoteControl remote;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					Settings window = new Settings(window, persistent);
//					window.frmSettings.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Settings(RemoteControl remote, Persistent p) {
		  this.remote = remote;
		  this.persistent = p;
		  initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try{
		//persistent = new Persistent();
		
		frmSettings = new JFrame();
		frmSettings.setTitle("Settings");
		frmSettings.setBounds(100, 100, 400, 200);
		frmSettings.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmSettings.getContentPane().setLayout(null);

		final JComboBox<String> comboBoxSettingsUsermode = new JComboBox<String>();
		comboBoxSettingsUsermode.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Easy", "Normal", "Expert" }));
		comboBoxSettingsUsermode.setBounds(212, 8, 90, 20);
		frmSettings.getContentPane().add(comboBoxSettingsUsermode);
		comboBoxSettingsUsermode.setSelectedIndex(persistent.getUsermode());
		
		final JComboBox<String> comboBoxSettingsAspectRatio = new JComboBox<String>();
		comboBoxSettingsAspectRatio.setModel(new DefaultComboBoxModel<String>(
				new String[] { "16 : 9", "4 : 3", "2.35 : 1" }));
		comboBoxSettingsAspectRatio.setBounds(212, 36, 90, 20);
		frmSettings.getContentPane().add(comboBoxSettingsAspectRatio);
		comboBoxSettingsAspectRatio.setSelectedIndex(persistent.getRatio());
		
		final JButton btnSettingsSave = new JButton("Save");
		btnSettingsSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				persistent.setUsermode(comboBoxSettingsUsermode.getSelectedIndex());
				persistent.setRatio(comboBoxSettingsAspectRatio.getSelectedIndex());
				remote.updateScreen();
				frmSettings.dispose();
				}catch(Exception e3){
					e3.printStackTrace();
				}
			}
		});
		btnSettingsSave.setBounds(212, 126, 90, 25);
		frmSettings.getContentPane().add(btnSettingsSave);

		JButton btnSettingsCancel = new JButton("Cancel");
		btnSettingsCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmSettings.dispose();
			}
		});
		btnSettingsCancel.setBounds(82, 126, 90, 25);
		frmSettings.getContentPane().add(btnSettingsCancel);

		JLabel lblSettingsUsermode = new JLabel("Usermodus");
		lblSettingsUsermode.setBounds(82, 11, 90, 14);
		frmSettings.getContentPane().add(lblSettingsUsermode);

		
//		JButton btnStationScan = new JButton("Station Scan");
//		btnStationScan.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					btnSettingsSave.setEnabled(false);
//					while (progressBarSettingsStationScan.getValue() < progressBarSettingsStationScan
//							.getMaximum()) {
//						progressBarSettingsStationScan
//								.setValue(progressBarSettingsStationScan
//										.getValue() + 1);
//						Thread.sleep(50);
//					}
//					btnSettingsSave.setEnabled(true);
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}
//		});

		final JButton btnStationScan = new JButton("Station Scan");
		btnStationScan.addActionListener(new RunnableActionListener() {
			public void run() {
				try {
					btnSettingsSave.setEnabled(false);
					btnStationScan.setEnabled(false);
					while (progressBarSettingsStationScan.getValue() < progressBarSettingsStationScan
							.getMaximum()) {
						progressBarSettingsStationScan
								.setValue(progressBarSettingsStationScan
										.getValue() + 1);
						Thread.sleep(50);
					}
					btnStationScan.setEnabled(true);
					btnSettingsSave.setEnabled(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnStationScan.setBounds(137, 64, 110, 23);
		frmSettings.getContentPane().add(btnStationScan);

		progressBarSettingsStationScan = new JProgressBar();
		progressBarSettingsStationScan.setBounds(10, 98, 364, 14);
		frmSettings.getContentPane().add(progressBarSettingsStationScan);

		JLabel lblAspectRatio = new JLabel("Aspect Ratio");
		lblAspectRatio.setBounds(82, 39, 90, 14);
		frmSettings.getContentPane().add(lblAspectRatio);
		}catch(Exception e2){
			e2.printStackTrace();
		}

	}
	
	public void setVisible(boolean visible) {
		frmSettings.setVisible(visible);
	}
	public boolean isVisible() {
		return frmSettings.isVisible();
	}
	public void dispose() {
		frmSettings.dispose();
	}
}
