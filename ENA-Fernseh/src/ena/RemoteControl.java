package ena;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

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

import java.io.IOException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class RemoteControl {

	private JFrame frame;
	private Screen screen;
	private Settings settings;
	private JTable tableRemoteStations;
	private Persistent persistent;
	private static RemoteControl window;
	private TvElectronics electronics = null;
	private boolean switchActive;

	private ArrayList<Channel> channelList;

	private JToggleButton btnRemotePiPSwitch;
	private JButton btnRemotePiPActivate;
	private JButton btnRemoteTimeshiftStop;
	private JToggleButton btnRemoteTimeshiftPlayPause;
	private JButton btnRemoteTimeshiftForwards;
	private JScrollPane scrollPaneRemoteStations;

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
	// Konstruktor
	public RemoteControl() {
		//PersisChannel persisChannel = new PersisChannel();
		channelList = Channel.exampleFill();
		persistent = new Persistent();
		initialize();
		updateButtonLayout();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		try {
			// Window Frame
			frame = new JFrame();
			frame.setResizable(false);
			frame.setBounds(1300, 100, 366, 666);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(null);
			// Remote Panel
			JPanel panelRemoteControl = new JPanel();
			panelRemoteControl.setBounds(0, 0, 360, 640);
			frame.getContentPane().add(panelRemoteControl);
			panelRemoteControl.setLayout(null);
			// Power Button
			JButton btnRemotePower = new JButton();
			btnRemotePower.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_OnOff.png")));
			// Erstellt den Bildschirm und TvElectronics
			btnRemotePower.addActionListener(new RunnableActionListener() {
				public void run() {
					if (screen == null || !screen.isVisible()) {
						screen = new Screen(persistent, scrollPaneRemoteStations);
						screen.setVisible(true);
						electronics = screen.getElectronics();
					} else {
						screen.setVisible(false);
						screen.dispose();
						electronics.killTimeshift();
					}
				}
			});
			btnRemotePower.setFocusPainted(false);
			btnRemotePower.setToolTipText("Turn Screen On/Off");
			btnRemotePower.setBounds(271, 10, 77, 77);
			panelRemoteControl.add(btnRemotePower);
			// Pip Switch Button
			btnRemotePiPSwitch = new JToggleButton();
			btnRemotePiPSwitch.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_PiPch.png")));
			btnRemotePiPSwitch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (btnRemotePiPSwitch.isSelected())
						switchActive = true;
					else
						switchActive = false;
				}
			});
			btnRemotePiPSwitch.setFocusPainted(false);
			btnRemotePiPSwitch.setToolTipText("Switch Picture in Picture Screen");
			btnRemotePiPSwitch.setBounds(184, 10, 77, 77);
			panelRemoteControl.add(btnRemotePiPSwitch);
			// PiP Activation Button
			btnRemotePiPActivate = new JButton();
			btnRemotePiPActivate.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_PiP.png")));
			btnRemotePiPActivate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (electronics == null)
						System.out.println("Kein screen vorhanden");
					else {
						if (btnRemotePiPSwitch.isEnabled()) {
							btnRemotePiPSwitch.setEnabled(false);
							btnRemotePiPSwitch.setSelected(false);
							switchActive = false;
							electronics.setPictureInPicture(false);
						} else {
							btnRemotePiPSwitch.setEnabled(true);
							electronics.setPictureInPicture(true);
						}
					}
				}
			});
			btnRemotePiPActivate.setFocusPainted(false);
			btnRemotePiPActivate.setToolTipText("Picture in Picture");
			btnRemotePiPActivate.setBounds(97, 10, 77, 77);
			panelRemoteControl.add(btnRemotePiPActivate);
			// Settings Button
			JButton btnRemoteSettings = new JButton();
			btnRemoteSettings.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_options.png")));
			btnRemoteSettings.setSelectedIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_options.png")));
			// übergibt Screen, Persistent, TvElectronics und RemoteControl an
			// Settings
			btnRemoteSettings.addActionListener(new RunnableActionListener() {
				public void run() {
					if (screen != null) {
						if (settings == null) {
							settings = new Settings(window, persistent, electronics, screen);
							settings.setVisible(true);
						} else {
							if (settings.isVisible()) {
								settings.setVisible(false);
								settings.dispose();
							} else {
								settings = new Settings(window, persistent, electronics, screen);
								settings.setVisible(true);
							}
						}
					}
				}
			});
			btnRemoteSettings.setFocusPainted(false);
			btnRemoteSettings.setToolTipText("Settings");
			btnRemoteSettings.setBounds(10, 10, 77, 77);
			panelRemoteControl.add(btnRemoteSettings);
			// ChannelList ScrollPane
			scrollPaneRemoteStations = new JScrollPane();
			scrollPaneRemoteStations.setBounds(10, 98, 338, 408);
			panelRemoteControl.add(scrollPaneRemoteStations);
			// Channel Table
			tableRemoteStations = new JTable();
			// schaltet den Kanal um mit Hilfe der Funktion setChannel von
			// TvElectronics um
			tableRemoteStations.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					try {
						if (screen != null) {
							if (screen.isVisible()) {
								electronics.setChannel(String.valueOf(tableRemoteStations.getValueAt(
										tableRemoteStations.getSelectedRow(), 0)), switchActive, String
										.valueOf(tableRemoteStations.getValueAt(tableRemoteStations.getSelectedRow(), 1)),
										screen, channelList.get(tableRemoteStations.getSelectedRow())
												.getChannelPicturePath());
								if ((persistent.getRatio() == 1) || (persistent.getRatio() == 2))
									electronics.setZoom(true);
								// screen.changePicture(channelList.get(
								// tableRemoteStations.getSelectedRow())
								// .getChannelPicturePath());
								// persistent.setProgramm(tableRemoteStations.getSelectedRow());
								// System.out.println(tableRemoteStations.getValueAt(tableRemoteStations.getSelectedRow(),
								// 1).toString());
								// screen.setLabel(tableRemoteStations
								// .getValueAt(
								// tableRemoteStations
								// .getSelectedRow(), 1)
								// .toString());
							} else
								System.out.println("Kein Fernseher vorhanden");
						} else
							System.out.println("Kein Fernseher vorhanden");
					} catch (Exception e4) {
						e4.printStackTrace();
					}
				}
			});
			tableRemoteStations.setFont(new Font("Tahoma", Font.PLAIN, 15));
			tableRemoteStations.setAutoCreateRowSorter(true);
			tableRemoteStations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableRemoteStations.setModel(new ChannelTableModelList(channelList));
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			tableRemoteStations.setRowSelectionInterval(0, 0);
			tableRemoteStations.getColumnModel().getColumn(0).setPreferredWidth(70);
			tableRemoteStations.getColumnModel().getColumn(0).setMinWidth(70);
			tableRemoteStations.getColumnModel().getColumn(0).setMaxWidth(70);
			tableRemoteStations.setRowHeight(31);
			tableRemoteStations.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			tableRemoteStations.getColumnModel().getColumn(1).setPreferredWidth(250);
			tableRemoteStations.getColumnModel().getColumn(1).setMinWidth(200);
			scrollPaneRemoteStations.setViewportView(tableRemoteStations);
			scrollPaneRemoteStations.setVisible(false);
			// TimeShift Stop Button
			btnRemoteTimeshiftStop = new JButton();
			btnRemoteTimeshiftStop.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_stop.png")));
			btnRemoteTimeshiftStop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// screen.getTimeshiftThread().interrupt();
					if (screen == null) {
						System.out.println("Kein Screen für die Aufnahme vorhanden.");
						btnRemoteTimeshiftPlayPause.setSelected(false);
					} else {
						if (!screen.isVisible()) {
							System.out.println("Kein Screen für die Aufnahme vorhanden.");
							btnRemoteTimeshiftPlayPause.setSelected(false);
						} else {
							try {
								electronics.recordTimeShift(false, btnRemoteTimeshiftPlayPause, screen);

								(screen.getPanelMain()).remove(screen.getProgressBar());
								electronics.setIsRecording();
								btnRemoteTimeshiftPlayPause.setSelected(false);
								btnRemoteTimeshiftPlayPause.setIcon(new ImageIcon(RemoteControl.class
										.getResource("/picture/p_paus.png")));
								(screen.getPanelMain()).repaint();
								screen.setProgressBar(new JProgressBar());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			});
			btnRemoteTimeshiftStop.setFocusPainted(false);
			btnRemoteTimeshiftStop.setToolTipText("Stop Timeshift");
			btnRemoteTimeshiftStop.setBounds(10, 552, 77, 77);
			panelRemoteControl.add(btnRemoteTimeshiftStop);
			// TimeShift Play/Pause Button
			// benutzt die Funktion recordTimeShift von TvElectronics
			// lässt in dem Fernseher ein ProgressBar erscheinen
			btnRemoteTimeshiftPlayPause = new JToggleButton();
			btnRemoteTimeshiftPlayPause.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_paus.png")));
			btnRemoteTimeshiftPlayPause.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (screen == null) {
							System.out.println("Kein Screen für die Aufnahme vorhanden.");
							btnRemoteTimeshiftPlayPause.setSelected(false);
						} else {
							if (!screen.isVisible()) {
								System.out.println("Kein Screen für die Aufnahme vorhanden.");
								btnRemoteTimeshiftPlayPause.setSelected(false);
							} else {
								if (btnRemoteTimeshiftPlayPause.isSelected()) {
									btnRemoteTimeshiftPlayPause.setIcon(new ImageIcon(RemoteControl.class
											.getResource("/picture/p_play.png")));
									if (!electronics.isRecording())
										electronics.recordTimeShift(true, btnRemoteTimeshiftPlayPause, screen);
									else
										electronics.playTimeShift(false, screen.getProgressBar().getValue());
									System.out.println("Pause");
								} else {
									btnRemoteTimeshiftPlayPause.setIcon(new ImageIcon(RemoteControl.class
											.getResource("/picture/p_paus.png")));
									electronics.playTimeShift(true, screen.getProgressBar().getValue());
									System.out.println("Play");

								}
							}
						}
					} catch (Exception e6) {
						e6.printStackTrace();
					}
				}
			});
			btnRemoteTimeshiftPlayPause.setFocusPainted(false);
			btnRemoteTimeshiftPlayPause.setToolTipText("Start/Pause Timeshift");
			btnRemoteTimeshiftPlayPause.setBounds(141, 552, 77, 77);
			panelRemoteControl.add(btnRemoteTimeshiftPlayPause);
			// TimeShift Forward Button
			btnRemoteTimeshiftForwards = new JButton();
			btnRemoteTimeshiftForwards.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_ffarw.png")));
			btnRemoteTimeshiftForwards.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (screen == null) {
						System.out.println("Kein Screen Vorhanden");
					} else {
						if (!screen.isVisible()) {
							System.out.println("Kein Screen Vorhanden");
						} else {

							screen.getTimeshiftThread().stop();
							new Thread(new Runnable() {
								@Override
								public void run() {

									while ((electronics.getProgressBar()).getValue() < (electronics.getProgressBar())
											.getMaximum()) {
										(electronics.getProgressBar()).setValue((electronics.getProgressBar()).getValue() + 1);
										try {
											electronics.setProgressBarValue((electronics.getProgressBar()).getValue());
											Thread.sleep(30);
										} catch (InterruptedException ie) {
											ie.printStackTrace();
										}
									}
								}
							}).start();
						}
					}
				}
			});
			btnRemoteTimeshiftForwards.setFocusPainted(false);
			btnRemoteTimeshiftForwards.setToolTipText("FastForward Timeshift");
			btnRemoteTimeshiftForwards.setBounds(271, 552, 77, 77);
			panelRemoteControl.add(btnRemoteTimeshiftForwards);
			// Volume Slider
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
			// Volume Down Button
			JButton btnRemoteVolumeDown = new JButton();
			btnRemoteVolumeDown.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_volD.png")));
			// ruft die Funktion setVolume von TvElectronics auf
			btnRemoteVolumeDown.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (sliderRemoteVolume.getMinimum() < sliderRemoteVolume.getValue()) {
							if (electronics != null)
								electronics.setVolume(sliderRemoteVolume.getValue() - 1, sliderRemoteVolume);
							else
								System.out.println("Keine TvElectronics erstellt");
						}
					} catch (Exception e6) {
						e6.printStackTrace();
					}
				}
			});
			btnRemoteVolumeDown.setFocusPainted(false);
			btnRemoteVolumeDown.setToolTipText("Decrease Volume");
			btnRemoteVolumeDown.setBounds(10, 516, 25, 25);
			panelRemoteControl.add(btnRemoteVolumeDown);
			// Volume Up Button
			JButton btnRemoteVolumeUp = new JButton();
			btnRemoteVolumeUp.setIcon(new ImageIcon(RemoteControl.class.getResource("/picture/p_volU.png")));
			// ruft die Funktion setVolume von TvElectronics auf
			btnRemoteVolumeUp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if (sliderRemoteVolume.getMaximum() > sliderRemoteVolume.getValue()) {
							if (electronics != null)
								electronics.setVolume(sliderRemoteVolume.getValue() + 1, sliderRemoteVolume);
							else
								System.out.println("Keine TvElectronics erstellt");
						}
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

	// Deactivate PiP/Timeshift
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
	
	public JTable getTableRemoteStations(){
		return tableRemoteStations;
	}
	
	public JScrollPane getScrollPaneRemoteStations(){
		return scrollPaneRemoteStations;
	}
}
