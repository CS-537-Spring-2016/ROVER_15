package testUtillities;

import enums.Science;

public class ParseStrings {
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		int xInt;
		int yInt;
		String xStr;
		String yStr;
		String sStr;
		
		String loc0 = "LOCx5y8";
		
		System.out.println(loc0.lastIndexOf(" "));
		
		String loc1 = "LOC 2 7";
		String loc2 = "LOC 22 3";
		String loc3 = "LOC 6 237";
		String loc4 = "LOC 025 19";
		
		sStr = loc1.substring(4);
		
		System.out.println("sStr " + sStr);
		//extractPos(loc0);
		extractPos(loc1.substring(4));
		extractPos(loc2.substring(4));
		extractPos(loc3.substring(4));
		extractPos(loc4.substring(4));
    	
		
		System.out.println(Science.NONE.toString());
		
	}
	
	public static void extractPos(String sStr){
		if(sStr.lastIndexOf(" ") != -1){
			String xStr = sStr.substring(0, sStr.lastIndexOf(" "));
			System.out.println("xStr " + xStr);
			
			String yStr = sStr.substring(sStr.lastIndexOf(" ") + 1);
			System.out.println("yStr " + yStr);
		}
	}
}
