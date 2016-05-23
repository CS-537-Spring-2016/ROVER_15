

package common;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by samskim on 5/12/16.
 */
public class Communication {

    private String url;
    JSONParser parser;
    private String rovername;
    private String corp_secret;

    public Communication(String url, String rovername, String corp_secret) {
        this.url = url;
        this.parser = new JSONParser();
        this.rovername = rovername;
        this.corp_secret = corp_secret;

    }

    public String postScanMapTiles(Coord currentLoc, MapTile[][] scanMapTiles) {
        JSONArray data = convertScanMapTiles(currentLoc, scanMapTiles);

        String charset = "UTF-8";
        URL obj = null;
        try {
            obj = new URL(url + "/global");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Rover-Name", rovername);
            con.setRequestProperty("Corp-Secret", corp_secret);
            con.setRequestProperty("Content-Type", "application/json");

            byte[] jsonBytes = data.toString().getBytes("UTF-8");

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(jsonBytes);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private JSONArray convertScanMapTiles(Coord currentLoc, MapTile[][] scanMapTiles) {
        int edgeSize = scanMapTiles.length;
        int centerIndex = (edgeSize - 1) / 2;

        JSONArray tiles = new JSONArray();
        for (int row = 0; row < scanMapTiles.length; row++) {
            for (int col = 0; col < scanMapTiles[row].length; col++) {

                MapTile mapTile = scanMapTiles[col][row];

                int xp = currentLoc.xpos - centerIndex + col;
                int yp = currentLoc.ypos - centerIndex + row;
                Coord coord = new Coord(xp, yp);
                JSONObject tile = new JSONObject();
                tile.put("x", xp);
                tile.put("y", yp);
                tile.put("terrain", mapTile.getTerrain().toString());
                tile.put("science", mapTile.getScience().toString());
                tiles.add(tile);
            }
        }
        return tiles;
    }

    // for requesting global map
    public JSONArray getGlobalMap() {

        URL obj = null;
        String responseStr = "";
        try {
            obj = new URL(url + "/science/all");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestProperty("Rover-Name", rovername);
            con.setRequestProperty("Corp-Secret", corp_secret);
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            responseStr = response.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseResponseStr(responseStr);
    }

    public JSONArray parseResponseStr(String response) {
        JSONArray data = null;
        try {
            data = (JSONArray) parser.parse(response);

            for (Object obj : data) {
                JSONObject json = (JSONObject) obj;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return data;
    }


}

