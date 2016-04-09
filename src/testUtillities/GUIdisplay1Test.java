package testUtillities;

import javax.swing.SwingUtilities;

public class GUIdisplay1Test {
	
	static GUIdisplay1 mainPanel;
	static MyGUIWorker myWorker;

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		mainPanel = new GUIdisplay1();
		myWorker = new MyGUIWorker(mainPanel);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUIdisplay1.createAndShowGui(myWorker, mainPanel);
			}
		});

		
		for (int i = 2; i < 29; i++) {
			System.out.println(i);
			myWorker.printOut(Integer.toString(i));

			Thread.sleep(500); // Time delay to sync output

		}

	}

}