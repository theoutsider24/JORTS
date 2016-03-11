package common;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import FYP.Main;
import gameElements.buildings.BuildingDefinition;
import gameElements.units.UnitDefinition;
import uiComponents.uiManagers.KeyboardManager;

import static common.Constants.*;
public class JsonReader {
	public static JSONObject readFile(String fileName)
	{
		String path = System.getProperty("user.dir") + "//defs";
		
		JSONParser parser = new JSONParser();
		 
        try { 
            Object obj = parser.parse(new FileReader(
            		DEFINITIONS_DIRECTORY+"//"+fileName));            
            JSONObject jsonObject = (JSONObject) obj;
            return jsonObject;
 
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not find "+fileName);
        }
        return null;
	}
	public static void readUnitDefinition(String fileName)
	{
		JSONObject jsonObject = readFile("units//"+fileName);
		if(jsonObject!=null)
			new UnitDefinition(jsonObject);
	}
	public static void readBuildingDefinition(String fileName)
	{
		JSONObject jsonObject = readFile("buildings//"+fileName);
		if(jsonObject!=null)
			new BuildingDefinition(jsonObject);
	}
	public static void readKeyMappings(String fileName)
	{
		JSONObject jsonObject = readFile("keyMappings//"+fileName);
		if(jsonObject!=null)
		{
			JSONArray mappings =  (JSONArray)jsonObject.get("mappings");
			for(Object s:mappings)
			{
				String key = (String)((JSONObject)s).get("key");
				String cmd = (String)((JSONObject)s).get("command");
				String mod = (String)((JSONObject)s).get("modifier");
				
				KeyboardManager.loadMapping(key, cmd,mod);
				//System.out.println(key+","+cmd);
			}
		}
	}
	public static void readGameDefinition(String fileName)
	{
		JSONObject jsonObject = readFile("games//"+fileName);
		JSONArray unitDefs = (JSONArray)jsonObject.get("units");
		JSONArray buildingDefs = (JSONArray)jsonObject.get("buildings");
		String map=(String)jsonObject.get("map");
		for(Object s:unitDefs)
		{
			JsonReader.readUnitDefinition((String)s);
		}
		for(Object s:buildingDefs)
		{
			JsonReader.readBuildingDefinition((String)s);
		}
		Main.worldMap.loadFile(new File(MAP_DIRECTORY+"//"+map));
		
		String KeyMappingFile=(String)jsonObject.get("keyMappings");
		JsonReader.readKeyMappings(KeyMappingFile);
	}
}
