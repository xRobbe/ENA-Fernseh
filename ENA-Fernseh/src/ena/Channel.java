package ena;

import java.util.ArrayList;

public class Channel {
	private String channelNumber;
	private String channelName;
	private String channelPicturePath;
	
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
		list.add(new Channel("37a", "ARD", "src/television/dasErste.jpg"));
		list.add(new Channel("22a","ZDF","src/television/zdf.jpg"));
		list.add(new Channel("34a","RTL","src/television/rtl.jpg"));
		list.add(new Channel("54d","SAT1","src/television/sat1.jpg"));
		list.add(new Channel("54c","Pro7","src/television/pro7.jpg"));
		return list;
	}
}

