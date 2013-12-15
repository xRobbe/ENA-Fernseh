package ena;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

public class PersisChannel {
	private File file = new File("channel.csv");
	private String picPath;
	private File testFile;
	private FileWriter writer;

	PersisChannel() {
		try {
			// new FileWriter(file) - falls die Datei bereits existiert
		    // wird diese ueberschrieben
		    writer = new FileWriter(file ,false);
			
			BufferedReader in = new BufferedReader(new FileReader("Kanalscan.csv"));
			String zeile = null;
			zeile = in.readLine();		//den ersten Eintrag "loeschen" bzw. ignorieren
			while ((zeile = in.readLine()) != null) {
				String[] spiltLine = zeile.split(";");
				System.out.println("Gelesene Zeile: " + spiltLine[3]);
				picPath = "src/television/" + spiltLine[3] + ".jpg";
				if(!(testFile = new File(picPath)).exists()){
					picPath = "src/television/" + spiltLine[3] + ".png";
					if(!(testFile = new File(picPath)).exists()){
						picPath = "src/television/testbild.png";
					}
				}
				writer.write(spiltLine[1] + ";" + spiltLine[3] + ";" + picPath + "\n");
			}
			writer.flush();		//schreibt den Stream in die Datei
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
