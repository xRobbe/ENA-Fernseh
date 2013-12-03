package ena;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;

public class Screen {

	private JFrame frame;
	private JPanel panelWelcome;
	private JPanel panelMainScreen;
	private JProgressBar progressBarScreenWelcome;
	private JProgressBar progressBarTimeShift;
	private TvElectronics electronics;
	private Persistent persisten;
	private JLabel picLabelMain;
	private JLabel picLabelPiP;
	private JPanel panelScreenPiP;

	private JLabel lblEPG = new JLabel("Das Erste");
	private JPanel panelScreenEPG;

	// Screen Konstruktor
	public Screen(Persistent persistent) {
		this.persisten = persistent;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// neues Fenster erstellen
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBounds(0, 0, 1286, 746);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		// Panel für Willkommenbildschirm
		panelWelcome = new JPanel();
		panelWelcome.setBounds(0, 0, 1280, 720);
		frame.getContentPane().add(panelWelcome);
		panelWelcome.setLayout(null);

		JLabel lblScreenWelcomeMessage = new JLabel("Welcome");
		lblScreenWelcomeMessage.setFont(new Font("Tahoma", Font.BOLD, 50));
		lblScreenWelcomeMessage.setBounds(440, 98, 400, 100);
		lblScreenWelcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
		panelWelcome.add(lblScreenWelcomeMessage);
		// Ladebalken für Willkommenbildschirm
		progressBarScreenWelcome = new JProgressBar();
		progressBarScreenWelcome.setBounds(320, 333, 640, 38);
		panelWelcome.add(progressBarScreenWelcome);

		JLabel lblScreenWelcomeStatus = new JLabel("Your TV is getting ready");
		lblScreenWelcomeStatus.setFont(new Font("Tahoma", Font.PLAIN, 50));
		lblScreenWelcomeStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblScreenWelcomeStatus.setBounds(320, 223, 640, 63);
		panelWelcome.add(lblScreenWelcomeStatus);
		// Panel für den Hauptbildschirm
		panelMainScreen = new JPanel();
		panelMainScreen.setVisible(false);
		panelMainScreen.setBackground(new Color(0, 0, 0));
		panelMainScreen.setBounds(0, 0, 1280, 720);
		frame.getContentPane().add(panelMainScreen);
		panelMainScreen.setLayout(null);
		// Progressbar für die Timeshift Funktion
		progressBarTimeShift = new JProgressBar();
		progressBarTimeShift.setBounds(320, 600, 640, 38);
		progressBarTimeShift.setVisible(false);
		panelMainScreen.add(progressBarTimeShift);
		// Ein EPG an der unteren Bildschirmseite (ist ausgeblendet)
		panelScreenEPG = new JPanel();
		panelScreenEPG.setBackground(new Color(164, 164, 164));
		panelScreenEPG.setBounds(256, 720, 768, 128);
		panelMainScreen.add(panelScreenEPG);
		panelScreenEPG.setVisible(false);
		panelScreenEPG.setLayout(new BorderLayout(0, 0));
		lblEPG.setForeground(Color.BLACK);
		panelScreenEPG.add(lblEPG, BorderLayout.CENTER);
		lblEPG.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblEPG.setHorizontalAlignment(SwingConstants.CENTER);
		// Panel für das PiP Fenster
		panelScreenPiP = new JPanel();
		panelScreenPiP.setBounds(886, 11, 384, 216);
		panelMainScreen.add(panelScreenPiP);
		panelScreenPiP.setVisible(false);
		panelScreenPiP.setLayout(null);
		// ScrollPane für die Senderliste an der linken Seite (ausgeblendet)
		final JScrollPane scrollPaneScreenStations = new JScrollPane();
		scrollPaneScreenStations.setBorder(null);
		scrollPaneScreenStations.setBackground(new Color(0, 0, 0));
		scrollPaneScreenStations.setBounds(new Rectangle(-256, 0, 256, 720));
		panelMainScreen.add(scrollPaneScreenStations);
		// Senderliste für das ScrollPane
		final JList<String> listScreenStations = new JList<String>();
		listScreenStations.setForeground(new Color(216, 0, 116));
		listScreenStations.setBackground(new Color(164, 164, 164));
		listScreenStations.setBorder(null);
		scrollPaneScreenStations.setViewportView(listScreenStations);
		listScreenStations.setFont(new Font("Tahoma", Font.BOLD, 20));
		listScreenStations.setModel(new AbstractListModel<String>() {
			String[] values = new String[] { "1 ARD", "2 ZDF", "3 RTL",
					"4 SAT1", "5 PRO7", "6 RTL2", "7 SUPER RTL", "8 KIKA",
					"9 ARTE", "10 Comedy Central", "11 Nickelodeon",
					"12 Kabel 1", "13 VOX", "14 MTV", "15 VIVA", "16 NTV",
					"17 N24", "18 HR3", "19 123TV", "20 MotorvisionTV",
					"21 Sport 1", "22 DMAX", "23 ASTRA TV", "24 ", "25", "26",
					"27", "28", "29", "30" };

			public int getSize() {
				return values.length;
			}

			public String getElementAt(int index) {
				return values[index];
			}
		});
		/*
		 * für Debuggen JButton btnSenderliste = new JButton("Senderliste");
		 * btnSenderliste.setBounds(377, 293, 85, 23);
		 * panelMainScreen.add(btnSenderliste);
		 * 
		 * JButton btnEpg = new JButton("EPG"); btnEpg.setBounds(377, 327, 85,
		 * 23); panelMainScreen.add(btnEpg);
		 * 
		 * JButton btnPip = new JButton("PiP"); btnPip.setBounds(377, 361, 85,
		 * 23); panelMainScreen.add(btnPip); btnPip.addActionListener(new
		 * ActionListener() { public void actionPerformed(ActionEvent e) { if
		 * (panelScreenPiP.isVisible()) panelScreenPiP.setVisible(false); else
		 * panelScreenPiP.setVisible(true);
		 * 
		 * try { BufferedImage newPicture; newPicture = ImageIO.read(new File(
		 * "src/television/dasErste.jpg"));
		 * panelMainScreen.remove(picLabelMain); picLabelMain = new JLabel(new
		 * ImageIcon(newPicture)); picLabelMain.setBounds(0, 0, 1280, 720);
		 * panelMainScreen.add(picLabelMain);
		 * 
		 * panelMainScreen.repaint();
		 * 
		 * } catch (IOException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } } });
		 * 
		 * btnEpg.addActionListener(new RunnableActionListener() { public void
		 * run() { try { scrollPanelY(panelScreenEPG, 720, 592); } catch
		 * (InterruptedException ie) { ie.printStackTrace(); } }
		 * 
		 * public void actionPerformed(ActionEvent e) { new
		 * Thread(this).start(); } }); btnSenderliste.addActionListener(new
		 * RunnableActionListener() { public void actionPerformed(ActionEvent
		 * arg0) { new Thread(this).start(); }
		 * 
		 * public void run() { try { scrollPanelX(scrollPaneScreenStations, 0,
		 * -256); } catch (InterruptedException ie) { ie.printStackTrace(); } }
		 * });
		 */
		// erstellt ein Objekt von TvElectronics
		if (electronics == null) {
			electronics = new TvElectronics(panelMainScreen, panelScreenPiP,
					persisten, this);
			System.out.println("TvElectronics wurde erstellt");
		} else
			System.out.println("TvElectronics ist schon vorhanden");
		// Standart Bilder und Sender für das Haupt- und PiP-Fenster
		BufferedImage myPicture;
		try {
			myPicture = ImageIO.read(new File("src/television/dasErste.jpg"));
			picLabelMain = new JLabel(new ImageIcon(myPicture));
			picLabelMain.setBounds(0, 0, 1280, 720);
			picLabelMain.setOpaque(true);
			picLabelMain.setVisible(true);
			panelMainScreen.add(picLabelMain);
			if ((persisten.getRatio() == 1) || (persisten.getRatio() == 2))
				electronics.setZoom(true);

			myPicture = ImageIO.read(new File("src/television/dasErste.jpg"));
			myPicture = resize(myPicture, 384, 216);
			picLabelPiP = new JLabel(new ImageIcon(myPicture));
			picLabelPiP.setBounds(0, 0, 384, 216);
			picLabelPiP.setOpaque(true);
			picLabelPiP.setVisible(true);
			panelScreenPiP.add(picLabelPiP);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		// startet den Durchlauf des Ladebalkens
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (progressBarScreenWelcome.getValue() < progressBarScreenWelcome
						.getMaximum()) {
					progressBarScreenWelcome.setValue(progressBarScreenWelcome
							.getValue() + 1);
					try {
						Thread.sleep(30);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}
				panelWelcome.setVisible(false);
				panelMainScreen.setVisible(true);

			}
		}).start();

	}

	// Anpassung der Größe des Bildes für die Fenster
	public BufferedImage resize(BufferedImage img, int newW, int newH) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		return dimg;
	}

	public TvElectronics getElectronics() {
		return electronics;
	}

	// lässt panel auf der y-Achse bewegen
	public void scrollPanelY(JComponent panel, int max, int min)
			throws InterruptedException {
		int time = 300 / Math.abs(max - min);
		if (panel.getBounds().y == max) {
			panel.setVisible(true);
			while (panel.getBounds().y > min) {
				panel.setBounds(panel.getBounds().x, panel.getBounds().y - 1,
						panel.getBounds().width, panel.getBounds().height);
				Thread.sleep(time);

			}
		} else if (panel.getBounds().y == min) {
			while (panel.getBounds().y < max) {
				panel.setBounds(panel.getBounds().x, panel.getBounds().y + 1,
						panel.getBounds().width, panel.getBounds().height);
				Thread.sleep(time);
			}
			panel.setVisible(false);
		}
	}

	// lässt panel auf der x-Achse bewegen
	private void scrollPanelX(JComponent panel, int max, int min)
			throws InterruptedException {
		int time = 300 / Math.abs(max - min);
		if (panel.getBounds().x == max) {
			while (panel.getBounds().x > min) {
				panel.setBounds(panel.getBounds().x - 1, panel.getBounds().y,
						panel.getBounds().width, panel.getBounds().height);
				Thread.sleep(time);
			}
			panel.setVisible(false);
		} else if (panel.getBounds().x == min) {
			panel.setVisible(true);
			while (panel.getBounds().x < max) {
				panel.setBounds(panel.getBounds().x + 1, panel.getBounds().y,
						panel.getBounds().width, panel.getBounds().height);
				Thread.sleep(time);
			}
		}
	}

	// diverse getter und setter
	public void setEpgLabel(String sName) {
		lblEPG.setText(sName);
	}

	public JLabel getEpgLabel() {
		return lblEPG;
	}

	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	public boolean isVisible() {
		return frame.isVisible();
	}

	public void dispose() {
		frame.dispose();
	}

	public JPanel getPanelScreenEPG() {
		return panelScreenEPG;
	}

	public JPanel getPanelMain() {
		return panelMainScreen;
	}

	public JLabel getMainLabel() {
		return picLabelMain;
	}

	public void setMainLabel(JLabel picLabelMain) {
		this.picLabelMain = picLabelMain;
	}

	public JProgressBar getProgressBar() {
		return progressBarTimeShift;
	}

	public void setProgressBarValue(int bar) {
		(this.progressBarTimeShift).setValue(bar);
	}

	// das Bild in dem Haupt- oder PiP-Fenster ändern
	// Hauptfenster, wenn choosePiP = false
	public void changePicture(String channelPicturePath, boolean choosePiP)
			throws IOException {
		BufferedImage newPicture;
		newPicture = ImageIO.read(new File(channelPicturePath));
		if (!(choosePiP)) {
			panelMainScreen.remove(picLabelMain);
			picLabelMain = new JLabel(new ImageIcon(newPicture));
			picLabelMain.setBounds(0, 0, 1280, 720);
			panelMainScreen.add(picLabelMain);
			panelMainScreen.repaint();
		} else {
			newPicture = resize(newPicture, 384, 216);
			panelScreenPiP.remove(picLabelPiP);
			picLabelPiP = new JLabel(new ImageIcon(newPicture));
			picLabelPiP.setBounds(0, 0, 384, 216);
			panelScreenPiP.add(picLabelPiP);
			panelScreenPiP.repaint();
		}
	}
}
