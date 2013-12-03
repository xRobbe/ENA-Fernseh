package ena;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//ActionListener, um einen neues Thread zu starten, wenn eine Aktion getätigt wurde
public abstract class RunnableActionListener implements ActionListener,
		Runnable {

	public void actionPerformed(ActionEvent arg0) {
		new Thread(this).start();
	}

	public abstract void run();
}
