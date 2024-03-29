package ena;

import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Diese Klasse kapselt und simuliert die Audio- und Video-Elektronik des
 * Fernsehers. Steuerbefehle werden simuliert durch Ausgabe auf die Konsole.
 * 
 * @author Bernhard Kreling
 * @version 1.0
 * @version 1.1 public now()
 */
public class TvElectronics {

	protected JPanel mainDisplay;
	protected JPanel pipDisplay;
	private boolean isRecording; // der TimeShift-Recorder nimmt momentan auf
	private long recordingStartTime; // zu diesem Zeitpunkt hat die
										// TimeShift-Aufnahme begonnen (in
										// Sekunden seit 1.1.1970)
	private Persistent persistent;
	private String channelPicPath = "src/television/dasErste.jpg";
	private JPanel panelMainScreen;
	private JLabel picLabelMain;
	private Screen screen;
	private Thread threadTimeshiftBar = null;
	private boolean isPlaying = false;

	/**
	 * Der Konstruktur übernimmt Referenzen auf die beiden JPanel-Objekte, die
	 * die Displays repräsentieren.
	 * 
	 * @param mainDisplay
	 *            dieses Panel repräsentiert das Haupt-Display
	 * @param pipDisplay
	 *            dieses Panel repräsentiert das PictureInPicture-Display
	 */
	TvElectronics(JPanel mainDisplay, JPanel pipDisplay, Persistent persistent, Screen screen) {
		this.mainDisplay = mainDisplay;
		this.pipDisplay = pipDisplay;
		this.isRecording = false;
		this.recordingStartTime = 0;
		this.persistent = persistent;
		this.screen = screen;
	}

	/**
	 * Liefert den aktuellen Zeitpunkt.
	 * 
	 * @return die aktuelle Zeit in Sekunden seit 1.1.1970
	 */
	public long now() {
		return Calendar.getInstance().getTimeInMillis() / 1000;
	}

	/**
	 * Führt den Kanalscan aus und liefert die verfügbaren Kanäle.
	 * 
	 * @return die Daten aus Kanalscan.csv
	 */
	public ArrayList<Object> scanChannels() {
		ArrayList<Object> channels = new ArrayList<Object>();

		// TO DO (Aufgabe 5): Implementieren Sie hier das Einlesen von
		// Kanalscan.csv!
		
		System.out.println("All channels scanned");
		return channels;
	}

	/**
	 * Waehlt einen Kanal fuer die Wiedergabe aus.
	 * 
	 * @param channel
	 *            Kanalnummer als Zahl im Bereich 1..99 gefolgt von einem
	 *            Buchstaben a..d (vgl. Kanalscan.csv)
	 * @param forPiP
	 *            true: Wiedergabe im PictureInPicture-Display; false:
	 *            Wiedergabe im Haupt-Display
	 * @throws Exception
	 *             wenn der Wert von "channel" nicht gültig ist
	 */
	public void setChannel(String channel, boolean forPiP, String channelName, Screen screen, String channelList)
			throws Exception {
		String errmsg = "Illegal format for channel: " + channel;
		int channelNumber;
		try {
			channelNumber = Integer.parseInt(channel.substring(0, channel.length() - 1));
		} catch (NumberFormatException n) {
			throw new Exception(errmsg);
		}
		String subChannel = channel.substring(channel.length() - 1, channel.length());
		if (channelNumber < 1 || channelNumber > 99 || new String("abcd").indexOf(subChannel) < 0)
			throw new Exception(errmsg);
		System.out.println((forPiP ? "PiP" : "Main") + " channel = " + channel);

		// TO DO (Aufgabe 4): Schalten Sie hier verschiedene statische Bilder
		// für die verschiedenen Kanäle
		// im jeweiligen Display!
		// Die meisten Bilder sollen im Format 16:9 sein, ein paar auch in 4:3
		// und in 2,35:1
		if (!(forPiP))
			this.channelPicPath = channelList;
		screen.setEpgLabel(channelName);
		screen.changePicture(channelList, forPiP);
		screen.showPanelEPG(screen.getPanelScreenEPG());
	}

	/**
	 * Stellt die Lautstaerke des Fernsehers ein.
	 * 
	 * @param volume
	 *            Einstellwert fuer die Lautstaerke im Bereich 0..100 (0 = aus,
	 *            100 = volle Lautstärke)
	 * @throws Exception
	 *             wenn der Wert von "volume" außerhalb des zulässigen
	 *             Bereichs ist
	 */
	public void setVolume(int volume, JSlider slider) throws Exception {
		if (volume < 0 || volume > 100)
			throw new Exception("Volume out of range 0..100: " + volume);
		System.out.println("Volume = " + volume);
		slider.setValue(volume);
		persistent.setVolume(volume);
	}

	/**
	 * Vergrößert bei Aktivierung das aktuelle Bild des Main-Display auf 133%
	 * und stellt es zentriert dar, d.h. die Ränder des vergrößerten Bildes
	 * werden abgeschnitten. Dadurch verschwinden die schwarzen Balken rechts
	 * und links bei 4:3 Sendungen, bzw. die schwarzen Balken oben und unten bei
	 * Cinemascope Filmen.
	 * 
	 * @param on
	 *            true: Vergrößerung auf 133%; false: Normalgröße 100%
	 */
	public void setZoom(boolean on) {
		System.out.println("Zoom = " + (on ? "133%" : "100%"));

		// TO DO (Aufgabe 4): Vergrößern Sie hier das aktuelle Bild des
		// Main-Display, abhängig von "on"!
		panelMainScreen = screen.getPanelMain();
		picLabelMain = screen.getMainLabel();
		BufferedImage myPicture;
		try {
			if (on) {
				myPicture = ImageIO.read(new File(channelPicPath));
				myPicture = resize(myPicture, (int) (myPicture.getWidth() * 1.333333),
						(int) (myPicture.getHeight() * 1.333333));
				panelMainScreen.remove(picLabelMain);
				picLabelMain = new JLabel(new ImageIcon(myPicture));
				picLabelMain.setBounds(0, 0, 1280, 720);
				screen.setMainLabel(picLabelMain);
				screen.scrollPanelY(screen.getMainLabel(), 720, 592);
				panelMainScreen.add(picLabelMain);
				panelMainScreen.repaint();
			} else {
				myPicture = ImageIO.read(new File(channelPicPath));
				panelMainScreen.remove(picLabelMain);
				picLabelMain = new JLabel(new ImageIcon(myPicture));
				picLabelMain.setBounds(0, 0, 1280, 720);
				screen.setMainLabel(picLabelMain);
				panelMainScreen.add(picLabelMain);
				panelMainScreen.repaint();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

	}

	/**
	 * Aktiviert bzw. deaktiviert die PictureInPicture-Darstellung.
	 * 
	 * @param show
	 *            true: macht das kleine Bild sichtbar; false: macht das kleine
	 *            Bild unsichtbar
	 */
	public void setPictureInPicture(boolean show) {
		System.out.println("PiP = " + (show ? "visible" : "hidden"));

		// TO DO (Aufgabe 4)
		this.pipDisplay.setVisible(show);
	}

	/**
	 * Startet die Aufnahme auf den TimeShift-Recorder bzw. beendet sie wieder.
	 * Das Beenden der Aufnahme beendet gleichzeitig eine eventuell laufende
	 * Wiedergabe.
	 * 
	 * @param start
	 *            true: Start; false: Stopp
	 * @throws Exception
	 *             wenn der Wert von "start" nicht zum aktuellen Zustand passt
	 */
	public void recordTimeShift(boolean start, final JToggleButton play, final Screen screen) throws Exception {
		if (this.isRecording == start)
			throw new Exception("TimeShift is already " + (this.isRecording ? "recording" : "stopped"));
		if (!start) {
			this.playTimeShift(false, 0);
		} else {
			(screen.getProgressBar()).setVisible(true);
			Thread progress = new Thread(new Runnable() {
				@Override
				public void run() {
					while (isRecording) {
						screen.setMaxProgressBar(((int) (now() - recordingStartTime)));
					}
				}
			});
			progress.start();
			screen.setTimeshiftThread(progress);
		}
		this.isRecording = start;
		this.recordingStartTime = now();
		System.out.println((start ? "Start" : "Stop") + " timeshift recording");
	}

	public JProgressBar getProgressBar() {
		return screen.getProgressBar();
	}

	public void setProgressBarValue(int a) {
		screen.setProgressBarValue(a);
	}

	/**
	 * Startet die Wiedergabe vom TimeShift-Recorder bzw. beendet sie wieder.
	 * 
	 * @param start
	 *            true: Start; false: Stopp
	 * @param offset
	 *            der Zeitversatz gegenüber der Aufnahme in Sekunden (nur
	 *            relevant bei Start)
	 * @throws Exception
	 *             wenn keine Aufzeichnung läuft oder noch nicht genug
	 *             gepuffert ist
	 */
	public void playTimeShift(final boolean start, int offset) throws Exception {
		if (start && !this.isRecording)
			throw new Exception("TimeShift is not recording");
		if (start && this.recordingStartTime + offset > now())
			throw new Exception("TimeShift has not yet buffered " + offset + " seconds");
		System.out.println((start ? "Start" : "Stop") + " timeshift playing"
				+ (start ? " (offset " + offset + " seconds)" : ""));
		if (start)
			isPlaying = true;
		else
			isPlaying = false;
		if (threadTimeshiftBar != null) {
			if (!threadTimeshiftBar.isAlive()) {
				threadTimeshiftBar = new Thread(new Runnable() {
					@Override
					public void run() {
						while (isPlaying) {
							try {
								Thread.sleep(1000);
								screen.addToProgressBar(1);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
				});
				threadTimeshiftBar.start();
			}
		} else {
			threadTimeshiftBar = new Thread(new Runnable() {
				@Override
				public void run() {
					while (isPlaying) {
						try {
							Thread.sleep(1000);
							screen.addToProgressBar(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
			});
			threadTimeshiftBar.start();
		}
	}

	public BufferedImage resize(BufferedImage img, int newW, int newH) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		return dimg;
	}

	public void setIsRecording() {
		isRecording = false;
	}

	public void killTimeshift() {
		threadTimeshiftBar.stop();
	}

	public boolean isRecording() {
		return isRecording;
	}

	// ======================================================================================================
	/**
	 * Testumgebung mit Aufrufbeispielen für die nicht-statischen Methoden der
	 * Klasse. Diese Testumgebung wird im Fernseher nicht aufgerufen.
	 * 
	 * @param args
	 *            Aufrufparameter werden ignoriert
	 */
	// public static void main(String[] args) {
	// try {
	// TvElectronics tvEl = new TvElectronics(new JPanel(), new JPanel());
	//
	// ArrayList<Object> channels = tvEl.scanChannels();
	// tvEl.setChannel("37a", false);
	// tvEl.setChannel("54d", true);
	// tvEl.setPictureInPicture(true);
	// tvEl.setVolume(47);
	// tvEl.setZoom(true);
	// tvEl.recordTimeShift(true);
	// / while (tvEl.recordingStartTime + 3 > tvEl.now())
	// ; // provisorische Warteschleife (Thread wäre ordentlicher)
	// tvEl.playTimeShift(true, 2);
	// tvEl.playTimeShift(false, 0);
	// tvEl.playTimeShift(true, 3);
	// tvEl.recordTimeShift(false);
	// }
	// catch (Exception e) {
	// System.out.println("ERROR: " + e.getMessage());
	// }
	//
	// }

}
