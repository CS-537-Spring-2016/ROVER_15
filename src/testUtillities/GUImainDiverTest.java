package testUtillities;


import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class GUImainDiverTest {
	
	   JFrame frame = new JFrame("GUI");
	    
	    JTextArea messageArea = new JTextArea(40, 40);

	    public GUImainDiverTest(){
		    messageArea.setEditable(false);
	        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
	        frame.pack();
	    }
	    
	    
	    
	    private void run() throws IOException {
	    	for(int count = 22; count < 500; count++){
	    		messageArea.append(count + "\n");
	    	}
	    }
	    
	    public static void main(String[] args) throws Exception {
	    	GUImainDiverTest client = new GUImainDiverTest();
	        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        client.frame.setVisible(true);
	        client.run();
	    }
}
