package ena;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class RunnableActionListener implements ActionListener, Runnable {
	
	public void actionPerformed(ActionEvent arg0) {
		new Thread(this).start();
	}
	
	public abstract void run();
}
