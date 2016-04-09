package testUtillities;

import common.Rover;
import enums.RoverName;

public class RoverNameTest {
	public static void main(String[] args) {
		System.out.println("test roverName test running");
		
		String name = "ROVER_00";
				
        RoverName rname = RoverName.getEnum(name); 
        
        System.out.println("SWARM: make a rover name " + rname);
        
        //Rover rover = new Rover(rname);
        
        
        
	}
}
