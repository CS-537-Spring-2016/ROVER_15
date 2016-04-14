package json;

/*	
 * Created by: 	Jonathan Young
 * Date: 		May 14, 2015
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GlobalReader {
	
	private JSONObject thatObject = null;
	
	public GlobalReader(int groupNumber) {
		
		// You no longer need to specify a filepath, only a filename
		String myFilePath = groupNumber + ".json";
		
		JSONParser parser = new JSONParser();
		
		try {
			Object obj = parser.parse(new FileReader(myFilePath));
			JSONObject jsonObject = (JSONObject) obj;
			this.thatObject = jsonObject;
		} catch (FileNotFoundException e) {
			System.out.println("No file found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O exception found.");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Parse exception found.");
			e.printStackTrace();
		}
	}
	
	public GlobalReader(String fileName) {
		
		// You no longer need to specify a filepath, only a filename
		String myFilePath = fileName + ".json";
		
		JSONParser parser = new JSONParser();
		
		try {
			Object obj = parser.parse(new FileReader(myFilePath));
			JSONObject jsonObject = (JSONObject) obj;
			this.thatObject = jsonObject;
		} catch (FileNotFoundException e) {
			System.out.println("No file found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O exception found.");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Parse exception found.");
			e.printStackTrace();
		}
	}
	
	public JSONObject getJSONObject() {
		return this.thatObject;
	}
	
}
