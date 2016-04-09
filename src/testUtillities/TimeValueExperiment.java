package testUtillities;

public class TimeValueExperiment {
	public static void main(String[] args) {

		long time1 = System.currentTimeMillis();
		
		System.out.println("time1 is " + time1);
		
		while(true){
			
			System.out.println("current time is " + System.currentTimeMillis()); 
			
			System.out.println("time difference is " + (System.currentTimeMillis() - time1));
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
