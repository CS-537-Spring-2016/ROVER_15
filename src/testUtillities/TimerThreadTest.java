package testUtillities;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
// to resolve conflict with java.util.Timer

public class TimerThreadTest {
	public static void main(String[] args) {
		ActionListener listener = new TimePrinter();

		// construct a timer that calls the listener
		// once every 10 seconds
		Timer t = new Timer(60000, listener);
		t.start();

		int count = 0;
		while(true){
			System.out.println(count);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
		}
	}
}

class TimePrinter implements ActionListener {
	public void actionPerformed(ActionEvent event) {
		Date now = new Date();
		System.out.println("At the tone, the time is " + now);
		Toolkit.getDefaultToolkit().beep();
	}
}