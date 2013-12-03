package ena;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Persistent {
	private final static String Filepath = "config.properties";
	private final static String STATION = "Station";
	private final static String VOLUME = "Volume";
	private final static String RATIO = "Ratio";
	private final static String USERMODE = "Usermode";
	private final static int MAX_VOLUME = 100;
	private final static int MAX_USERMODE = 2;
	private final static int MAX_RATIO = 2;

	private Properties properties = new Properties();
	private String config = Filepath;
	private FileInputStream in;

	public Persistent() {
		try {
			if (!((new File(Filepath)).exists()))
				new FileOutputStream(config);

			in = new FileInputStream(config);
			properties.load(in);

			if (!(checkConfig())) {
				properties.setProperty(VOLUME, "50");
				properties.setProperty(STATION, "1");
				properties.setProperty(USERMODE, "0");
				properties.setProperty(RATIO, "0");
				properties.store(out(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setVolume(int volume) throws Exception {
		properties.setProperty(VOLUME, String.valueOf(volume));
		properties.store(out(), null);
	}

	public int getVolume() throws Exception {
		return Integer.parseInt(properties.getProperty(VOLUME));
	}

	public void setProgramm(int programm) throws Exception {
		properties.setProperty(STATION, String.valueOf(programm));
		properties.store(out(), null);
	}

	public int getProgramm() throws Exception {
		return Integer.parseInt(properties.getProperty(STATION));
	}

	public void setUsermode(int usermode) throws Exception {
		properties.setProperty(USERMODE, String.valueOf(usermode));
		properties.store(out(), null);
	}

	public int getUsermode() throws Exception {
		return Integer.parseInt(properties.getProperty(USERMODE));
	}

	public void setRatio(int ratio) throws Exception {
		properties.setProperty(RATIO, String.valueOf(ratio));
		properties.store(out(), null);
	}

	public int getRatio() throws Exception {
		return Integer.parseInt(properties.getProperty(RATIO));
	}

	private boolean checkConfig() {
		if (!(properties.containsKey(VOLUME)))
			return false;
		if (Integer.parseInt(properties.getProperty(VOLUME)) > MAX_VOLUME)
			return false;
		if (Integer.parseInt(properties.getProperty(VOLUME)) < 0)
			return false;

		if (!(properties.containsKey(STATION)))
			return false;
		if (Integer.parseInt(properties.getProperty(STATION)) < 0)
			return false;

		if (!(properties.containsKey(USERMODE)))
			return false;
		if (Integer.parseInt(properties.getProperty(USERMODE)) > MAX_USERMODE)
			return false;
		if (Integer.parseInt(properties.getProperty(USERMODE)) < 0)
			return false;

		if (!(properties.containsKey(RATIO)))
			return false;
		if (Integer.parseInt(properties.getProperty(RATIO)) > MAX_RATIO)
			return false;
		if (Integer.parseInt(properties.getProperty(RATIO)) < 0)
			return false;

		return true;
	}

	private FileOutputStream out() throws Exception {
		return new FileOutputStream(config);
	}
}
