package ena;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RemoteControl {

	private JFrame frame;
	private Screen screen;
	private Settings settings;
	private JTable tableRemoteStations;
	private Persistent persistent;
	private static RemoteControl window;

	private JButton btnRemotePiPSwitch;
	private JButton btnRemotePiPActivate;
	private JButton btnRemoteTimeshiftStop;
	private JButton btnRemoteTimeshiftPlayPause;
	private JButton btnRemoteTimeshiftForwards;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new RemoteControl();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RemoteControl() {
		initialize();
		updateButtonLayout();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		try {
			persistent = new Persistent();

			frame = new JFrame();
			frame.setResizable(false);
			frame.setBounds(100, 100, 366, 666);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(null);

			JPanel panelRemoteControl = new JPanel();
			panelRemoteControl.setBounds(0, 0, 360, 640);
			frame.getContentPane().add(panelRemoteControl);
			panelRemoteControl.setLayout(null);

			JButton btnRemotePower = new JButton();
			btnRemotePower.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_OnOff.png")));
			btnRemotePower.addActionListener(new RunnableActionListener() {
				public void run() {
					if (screen == null) {
						screen = new Screen();
						screen.setVisible(true);
					} else {
						if (screen.isVisible()) {
							screen.setVisible(false);
							screen.dispose();
						} else {
							screen = new Screen();
							screen.setVisible(true);
						}
					}
				}
			});
			btnRemotePower.setFocusPainted(false);
			btnRemotePower.setToolTipText("Turn Screen On/Off");
			btnRemotePower.setBounds(271, 10, 77, 77);
			panelRemoteControl.add(btnRemotePower);
			

			btnRemotePiPSwitch = new JButton();
			btnRemotePiPSwitch.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_PiPch.png")));
			btnRemotePiPSwitch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			btnRemotePiPSwitch.setFocusPainted(false);
			btnRemotePiPSwitch.setToolTipText("Switch Picture in Picture Screen");
			btnRemotePiPSwitch.setBounds(184, 10, 77, 77);
			panelRemoteControl.add(btnRemotePiPSwitch);

			btnRemotePiPActivate = new JButton();
			btnRemotePiPActivate.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_PiP.png")));
			btnRemotePiPActivate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (btnRemotePiPSwitch.isEnabled())
						btnRemotePiPSwitch.setEnabled(false);
					else
						btnRemotePiPSwitch.setEnabled(true);
				}
			});
			btnRemotePiPActivate.setFocusPainted(false);
			btnRemotePiPActivate.setToolTipText("Picture in Picture");
			btnRemotePiPActivate.setBounds(97, 10, 77, 77);
			panelRemoteControl.add(btnRemotePiPActivate);

			JButton btnRemoteSettings = new JButton();
			btnRemoteSettings.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_options.png")));
			btnRemoteSettings.setSelectedIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_options.png")));
			btnRemoteSettings.addActionListener(new RunnableActionListener() {
				public void run() {
					if (settings == null) {
						settings = new Settings(window, persistent);
						settings.setVisible(true);
					} else {
						if (settings.isVisible()) {
							settings.setVisible(false);
							settings.dispose();
						} else {
							settings = new Settings(window, persistent);
							settings.setVisible(true);
						}
					}
				}
			});
			btnRemoteSettings.setFocusPainted(false);
			btnRemoteSettings.setToolTipText("Settings");
			btnRemoteSettings.setBounds(10, 10, 77, 77);
			panelRemoteControl.add(btnRemoteSettings);

			JScrollPane scrollPaneRemoteStations = new JScrollPane();
			scrollPaneRemoteStations.setBounds(10, 98, 338, 408);
			panelRemoteControl.add(scrollPaneRemoteStations);

			tableRemoteStations = new JTable();
			tableRemoteStations.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					try {
						persistent.setProgramm(tableRemoteStations.getSelectedRow());
						System.out.println(tableRemoteStations.getValueAt(tableRemoteStations.getSelectedRow(), 1)
								.toString());
						screen.setLabel(tableRemoteStations.getValueAt(tableRemoteStations.getSelectedRow(), 1)
								.toString());
					} catch (Exception e4) {
						e4.printStackTrace();
					}
				}
			});
			tableRemoteStations.setFont(new Font("Tahoma", Font.PLAIN, 15));
			tableRemoteStations.setAutoCreateRowSorter(true);
			tableRemoteStations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableRemoteStations.setModel(new DefaultTableModel(new Object[][] { { "1", "ARD" }, { "2", "ZDF" },
					{ "3", "RTL" }, { "4", "SAT1" }, { "5", "Pro7" }, }, new String[] { "Channel", "Name" }) {
				boolean[] columnEditables = new boolean[] { false, false };

				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			tableRemoteStations.getColumnModel().getColumn(0).setPreferredWidth(70);
			tableRemoteStations.getColumnModel().getColumn(0).setMinWidth(70);
			tableRemoteStations.getColumnModel().getColumn(0).setMaxWidth(70);
			tableRemoteStations.setRowHeight(31);
			tableRemoteStations.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			tableRemoteStations.getColumnModel().getColumn(1).setPreferredWidth(250);
			tableRemoteStations.getColumnModel().getColumn(1).setMinWidth(200);
			scrollPaneRemoteStations.setViewportView(tableRemoteStations);

			btnRemoteTimeshiftStop = new JButton();
			btnRemoteTimeshiftStop.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_stop.png")));
			btnRemoteTimeshiftStop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			btnRemoteTimeshiftStop.setFocusPainted(false);
			btnRemoteTimeshiftStop.setToolTipText("Stop Timeshift");
			btnRemoteTimeshiftStop.setBounds(10, 552, 77, 77);
			panelRemoteControl.add(btnRemoteTimeshiftStop);

			btnRemoteTimeshiftPlayPause = new JButton();
			btnRemoteTimeshiftPlayPause.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_paus.png")));
			btnRemoteTimeshiftPlayPause.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnRemoteTimeshiftPlayPause.setIcon(new ImageIcon(RemoteControl.class
							.getResource("/picture/p_play.png")));
				}
			});
			btnRemoteTimeshiftPlayPause.setFocusPainted(false);
			btnRemoteTimeshiftPlayPause.setToolTipText("Start/Pause Timeshift");
			btnRemoteTimeshiftPlayPause.setBounds(184, 552, 77, 77);
			panelRemoteControl.add(btnRemoteTimeshiftPlayPause);
			
			btnRemoteTimeshiftForwards = new JButton();
			btnRemoteTimeshiftForwards.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_ffarw.png")));
			btnRemoteTimeshiftForwards.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			btnRemoteTimeshiftForwards.setFocusPainted(false);
			btnRemoteTimeshiftForwards.setToolTipText("FastForward Timeshift");
			btnRemoteTimeshiftForwards.setBounds(271, 552, 77, 77);
			panelRemoteControl.add(btnRemoteTimeshiftForwards);

			final JSlider sliderRemoteVolume = new JSlider();
			sliderRemoteVolume.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					try {
						persistent.setVolume(sliderRemoteVolume.getValue());
					} catch (Exception e3) {
						e3.printStackTrace();
					}
				}
			});
			sliderRemoteVolume.setToolTipText("Change Volume");
			sliderRemoteVolume.setBounds(40, 516, 278, 25);
			sliderRemoteVolume.setValue(persistent.getVolume());
			panelRemoteControl.add(sliderRemoteVolume);

			JButton btnRemoteVolumeDown = new JButton();
			btnRemoteVolumeDown.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_volD.png")));
			btnRemoteVolumeDown.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (sliderRemoteVolume.getMinimum() <= sliderRemoteVolume.getValue())
							sliderRemoteVolume.setValue(sliderRemoteVolume.getValue() - 1);
						persistent.setVolume(sliderRemoteVolume.getValue());
					} catch (Exception e6) {
						e6.printStackTrace();
					}
				}
			});
			btnRemoteVolumeDown.setFocusPainted(false);
			btnRemoteVolumeDown.setToolTipText("Decrease Volume");
			btnRemoteVolumeDown.setBounds(10, 516, 25, 25);
			panelRemoteControl.add(btnRemoteVolumeDown);

			JButton btnRemoteVolumeUp = new JButton();
			btnRemoteVolumeUp.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_volU.png")));
			btnRemoteVolumeUp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if (sliderRemoteVolume.getMaximum() >= sliderRemoteVolume.getValue())
							sliderRemoteVolume.setValue(sliderRemoteVolume.getValue() + 1);
						persistent.setVolume(sliderRemoteVolume.getValue());
					} catch (Exception e5) {
						e5.printStackTrace();
					}
				}
			});
			btnRemoteVolumeUp.setFocusPainted(false);
			btnRemoteVolumeUp.setToolTipText("Increase Volume");
			btnRemoteVolumeUp.setBounds(323, 516, 25, 25);
			panelRemoteControl.add(btnRemoteVolumeUp);

			JPanel panelRemoteSettings = new JPanel();
			panelRemoteSettings.setBounds(0, 0, 360, 640);
			frame.getContentPane().add(panelRemoteSettings);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	
	//TODO Deactivate PiP/Timeshift 
	public void updateButtonLayout() {
		try {
			switch (persistent.getUsermode()) {
			case 0:
				btnRemotePiPSwitch.setVisible(false);
				btnRemotePiPActivate.setVisible(false);
				btnRemoteTimeshiftStop.setVisible(false);
				btnRemoteTimeshiftPlayPause.setVisible(false);
				btnRemoteTimeshiftForwards.setVisible(false);
				break;

			case 1:
				btnRemotePiPSwitch.setVisible(false);
				btnRemotePiPActivate.setVisible(false);
				btnRemoteTimeshiftStop.setVisible(true);
				btnRemoteTimeshiftPlayPause.setVisible(true);
				btnRemoteTimeshiftForwards.setVisible(true);
				break;

			case 2:
				btnRemotePiPSwitch.setVisible(true);
				btnRemotePiPSwitch.setEnabled(false);
				btnRemotePiPActivate.setVisible(true);
				btnRemoteTimeshiftStop.setVisible(true);
				btnRemoteTimeshiftPlayPause.setVisible(true);
				btnRemoteTimeshiftForwards.setVisible(true);
				break;
			}
		} catch (Exception e7) {
			e7.printStackTrace();
		}
	}

	public JButton getPiPActive() {
		return btnRemotePiPActivate;
	}

}
