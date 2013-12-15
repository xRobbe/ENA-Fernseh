package ena;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//Arraylist f�r die verschiedenen Sender
public class Channel {
	private String channelNumber;
	private String channelName;
	private String channelPicturePath;
	private static File testFile;
	private static String[] splitLine;
	private static String picPath;

	Channel(String channelNumber, String channelName, String channelPicturePath) {
		this.setChannelNumber(channelNumber);
		this.setChannelName(channelName);
		this.setChannelPicturePath(channelPicturePath);
	}

	public String getChannelNumber() {
		return channelNumber;
	}

	public void setChannelNumber(String channelNumber) {
		this.channelNumber = channelNumber;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelPicturePath() {
		return channelPicturePath;
	}

	public void setChannelPicturePath(String channelPicturePath) {
		this.channelPicturePath = channelPicturePath;
	}

	public static ArrayList<Channel> exampleFill() {
		ArrayList<Channel> list = new ArrayList<Channel>();
		try {
			BufferedReader in = new BufferedReader(new FileReader("channel.csv"));
			String zeile = null;
			while ((zeile = in.readLine()) != null){
				splitLine = zeile.split(";");
				String picPath = "src/television/" + splitLine[3] + ".jpg";
				if(!(testFile = new File(picPath)).exists()){
					picPath = "src/television/" + splitLine[3] + ".png";
					if(!(testFile = new File(picPath)).exists()){
						picPath = "src/television/testbild.png";
					}
				}
				list.add(new Channel (splitLine[1], splitLine[3], picPath));
			}
			/*
			 * list.add(new Channel("37a", "ARD",
			 * "src/television/dasErste.jpg")); list.add(new Channel("22a",
			 * "ZDF", "src/television/zdf.jpg")); list.add(new Channel("34a",
			 * "RTL", "src/television/rtl.jpg")); list.add(new Channel("54d",
			 * "SAT1", "src/television/sat1.jpg")); list.add(new Channel("54c",
			 * "Pro7", "src/television/pro7.jpg"));
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
