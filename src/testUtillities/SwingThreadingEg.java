//package testUtillities;
//
//import java.io.File;
//import java.util.List;
//import java.util.Scanner;
//
//import javax.swing.*;
//
//public class SwingThreadingEg extends JPanel implements MyAppendable {
//   private JTextArea area = new JTextArea(30, 50);
//
//   public SwingThreadingEg() {
//      JScrollPane scrollPane = new JScrollPane(area);
//      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//      add(scrollPane);
//   }
//
//   @Override
//   public void append(String text) {
//      area.append(text);
//   }
//
//   static void createAndShow(MyWorker myWorker, SwingThreadingEg mainPanel) {
//
//      // add a Prop Change listener here to listen for 
//      // DONE state then call get() on myWorker
//      myWorker.execute();
//
//      JFrame frame = new JFrame("SwingThreadingEg");
//      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//      frame.getContentPane().add(mainPanel);
//      frame.pack();
//      frame.setLocationByPlatform(true);
//      frame.setVisible(true);
//   }
//}
//
//class MyWorker extends SwingWorker<Void, String> {
//   private MyAppendable myAppendable;
//
//   public MyWorker(MyAppendable myAppendable) {
//      this.myAppendable = myAppendable;
//   }
//
//   
//   @Override
//   protected Void doInBackground() throws Exception {
//	   //put the conversion code here and then publish it
//	   
//      return null;
//   }
//
//	public void printOut(String msg){
//		publish(msg);
//	}
//
//@Override
//   protected void process(List<String> chunks) {
//      for (String text : chunks) {
//         myAppendable.append(text + "\n");
//      }
//   }
//}
//
//interface MyAppendable {
//   public void append(String text);
//}