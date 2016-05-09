package controlServer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

class CountDownDisplay
{
    JTextField countdownClock = new JTextField();
    Timer timer;
    
    public CountDownDisplay()
    {
        countdownClock.setColumns( 7 );
        countdownClock.setFont( new Font( "sansserif", Font.PLAIN, 50 ) );
        countdownClock.setHorizontalAlignment( JTextField.CENTER );
        countdownClock.setBackground( Color.LIGHT_GRAY );
        countdownClock.setBorder( null );
        countdownClock.setEditable( false );
        
        // create a 1 seconds delay
        timer = new Timer(1000, new ActionListener() {
            //private long time = 60 * 1000; //60 seconds
            private long time = 600 * 1000; // 10 mintes
            

            public void actionPerformed(ActionEvent e) {
                if (time >= 0) {
                    long s = ((time / 1000) % 60);
                    long m = (((time / 1000) / 60) % 60);
                    long h = ((((time / 1000) / 60) / 60) % 60);
                    countdownClock.setText(h + " h " + m + " m " + s + " s");
                    
                    time -= 1000;
                }
            }
        });
        timer.start();
    }
    
    public static void main(String[] args)
    {
    	CountDownDisplay countDown = new CountDownDisplay();
    	JFrame frame = new JFrame("GUIdisplay");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(countDown.countdownClock);
		frame.setSize(100, 100); 			//is this used?
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
    }
}