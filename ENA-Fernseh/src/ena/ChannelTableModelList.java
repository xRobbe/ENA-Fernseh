package ena;

import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class ChannelTableModelList extends AbstractTableModel implements TableModel {
	private List<Channel> list;
	
	public ChannelTableModelList(List<Channel> list) {
		this.list = list;
	}

	public int getColumnCount() {
		return 2;
	}
	
	public int getRowCount() {
		return list.size();
	}
	
	public Object getValueAt(int row, int column) {
		Channel channel = list.get(row);
		switch (column) {
		case 0:
			return channel.getChannelNumber();
		case 1:
			return channel.getChannelName();
		}
		return null;
	}
	
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Channel";
		case 1:
			return "Name";
		}
		return null;
	}
}