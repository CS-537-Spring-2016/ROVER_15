package enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// thank you to this post: http://stackoverflow.com/questions/3054247/how-to-define-properties-for-enum-items

public enum RoverName {
	// (Drive type, accessory slot 1, accessory slot 2)
	NONE,
	
	ROVER_01 ("WALKER", "DRILL", "SPECTRAL_SENSOR"),
	ROVER_02 ("WALKER", "CHEMICAL_SENSOR", "RADIATION_SENSOR"),
	ROVER_03 ("TREADS", "DRILL", "EXCAVATOR"),
	ROVER_04 ("WALKER", "DRILL", "RADAR_SENSOR"),
	ROVER_05 ("WHEELS", "RANGE_BOOTER", "SPECTRAL_SENSOR"),
	ROVER_06 ("WHEELS", "RANGE_BOOTER", "RADIATION_SENSOR"),
	ROVER_07 ("TREADS", "EXCAVATOR", "RADAR_SENSOR"),
	ROVER_08 ("TREADS", "EXCAVATOR", "SPECTRAL_SENSOR"),
	ROVER_09 ("WALKER", "CHEMICAL_SENSOR", "DRILL"),
	ROVER_10 ("WHEELS", "RANGE_BOOTER", "RADIATION_SENSOR"),
	ROVER_11 ("WALKER", "DRILL", "EXCAVATOR"),
	ROVER_12 ("WHEELS", "RANGE_BOOTER", "SPECTRAL_SENSOR"),
	ROVER_13 ("TREADS", "EXCAVATOR", "CHEMICAL_SENSOR"),
	ROVER_14 ("WHEELS", "RANGE_BOOTER", "CHEMICAL_SENSOR"),
	ROVER_15 ("TREADS", "DRILL", "EXCAVATOR"),
	ROVER_16 ("WALKER", "DRILL", "RADIATION_SENSOR"),
	ROVER_17 ("WHEELS", "RANGE_BOOTER", "RADAR_SENSOR"),
	ROVER_18 ("WHEELS", "EXCAVATOR", "RADAR_SENSOR"),
	
	//not currently being used
	ROVER_19 ("NONE", "NONE", "NONE"),
	ROVER_20 ("NONE", "NONE", "NONE"),
	
	// sample test rovers
	ROVER_00 ("WHEELS", "RADIATION_SENSOR", "RADAR_SENSOR"),
	ROVER_99 ("TREADS", "SPECTRAL_SENSOR", "CHEMICAL_SENSOR");
	
    private final List<String> members;
    private RoverName(String... members){
        this.members=Arrays.asList(members);
    }
    public List<String> getMembers(){
        // defensive copy, because the original list is mutable
        return new ArrayList<String>(members);
    }
    
    public static RoverName getEnum(String input){
    	RoverName output;
    	
    	switch(input){
    	case "ROVER_00":
    		 System.out.println("ROVERNAME: case ROVER_00");
    		output = RoverName.ROVER_00;
    		break;
    	case "ROVER_01":
    		output = RoverName.ROVER_01;
    		break;
    	case "ROVER_02":
    		output = RoverName.ROVER_02;
    		break;
    	case "ROVER_03":
    		output = RoverName.ROVER_03;
    		break;
    	case "ROVER_04":
    		output = RoverName.ROVER_04;
    		break;
    	case "ROVER_05":
    		output = RoverName.ROVER_05;
    		break;
    	case "ROVER_06":
    		output = RoverName.ROVER_06;
    		break;
    	case "ROVER_07":
    		output = RoverName.ROVER_07;
    		break;
    	case "ROVER_08":
    		output = RoverName.ROVER_08;
    		break;
    	case "ROVER_09":
    		output = RoverName.ROVER_09;
    		break;
    	case "ROVER_10":
    		output = RoverName.ROVER_10;
    		break;
    	case "ROVER_11":
    		output = RoverName.ROVER_11;
    		break;
    	case "ROVER_12":
    		output = RoverName.ROVER_12;
    		break;    		
    	case "ROVER_13":
    		output = RoverName.ROVER_13;
    		break;
    	case "ROVER_14":
    		output = RoverName.ROVER_14;
    		break;
    	case "ROVER_15":
    		output = RoverName.ROVER_15;
    		break;
    	case "ROVER_16":
    		output = RoverName.ROVER_16;
    		break;
    	case "ROVER_17":
    		output = RoverName.ROVER_17;
    		break;
    	case "ROVER_18":
    		output = RoverName.ROVER_18;
    		break;
    	case "ROVER_19":
    		output = RoverName.ROVER_19;
    		break;
    	case "ROVER_20":
    		output = RoverName.ROVER_20;
    		break;
    	case "ROVER_99":
    		output = RoverName.ROVER_99;
    		break;
    	default:
    		output = RoverName.NONE;
    	}	
    	return output;
    }
}
