package controlServer;


import java.util.List;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

import common.Coord;
import common.RoverLocations;

// Thanks to this posting for the seed this was constructed from:
// http://stackoverflow.com/questions/30204521/thread-output-to-gui-text-field

public class GUIdisplay extends JPanel implements MyGUIAppendable {
	private JTextArea area;

	public GUIdisplay() {
		area = new JTextArea(55, 110);  //height x width
		JScrollPane scrollPane = new JScrollPane(area);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		//DefaultCaret caret = (DefaultCaret)area.getCaret();
		//caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		add(scrollPane);
	}

	public GUIdisplay(int width, int height) {
		area = new JTextArea(width, height);
		JScrollPane scrollPane = new JScrollPane(area);
		//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
	}

	@Override
	public void append(String text) {
		area.append(text);
	}

	@Override
	public void clearDisplay() {
		area.setText("");
	}

	static void createAndShowGui(MyGUIWorker myWorker, GUIdisplay mainPanel) {

		// add a Prop Change listener here to listen for
		// DONE state then call get() on myWorker
		myWorker.execute();

		JFrame frame = new JFrame("GUIdisplay");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}
}

// #############################################################################################

class MyGUIWorker extends SwingWorker<Void, String> {
	private MyGUIAppendable myAppendable;
	private String msg;

	public MyGUIWorker(MyGUIAppendable myAppendable) {
		this.myAppendable = myAppendable;
	}

	@Override
	protected Void doInBackground() throws Exception {
		// put the conversion code here and then publish it
		String[] lst = new String[5];
		lst[0] = "-----------------";
		lst[1] = "adding to list";
		lst[2] = msg;
		lst[3] = "finshed adding this item";
		lst[4] = "-----------------";
		publish(lst);
		return null;
	}

	public void printOut(String msg) {
		this.msg = msg;
		try {
			doInBackground();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clearDisplay() {
		myAppendable.clearDisplay();
	}
	
	public void displayRovers(RoverLocations roverLoc){
		myAppendable.clearDisplay();
		String[] roverPrint = new String[(50 * 50)];
		for(int j=0; j<50; j++){
			for(int i=0; i<50; i++){
				Coord tcor = new Coord(i, j);
				if(roverLoc.containsCoord(tcor)){
					//myAppendable.append("X");
					
					String rNum = roverLoc.getName(tcor).toString();

					myAppendable.append(rNum.substring(6));
				} else {
					myAppendable.append("  ");
				}
			}
			myAppendable.append("\n");
		}
		//publish(roverPrint);
	}

	@Override
	protected void process(List<String> chunks) {
		System.out.println("GUI: MyGUIWorker process has been called");

		for (String text : chunks) {
			myAppendable.append(text + "\n");
		}
	}
}

interface MyGUIAppendable {
	public void append(String text);

	public void clearDisplay();
}