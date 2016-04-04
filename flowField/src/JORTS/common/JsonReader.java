package JORTS.common;
import static JORTS.common.Constants.DEFINITIONS_DIRECTORY;
import static JORTS.common.Constants.MAP_DIRECTORY;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import JORTS.core.Main;
import JORTS.core.Player;
import JORTS.gameElements.buildings.BuildingDefinition;
import JORTS.gameElements.buildings.resources.Resource;
import JORTS.gameElements.map.TerrainDefinition;
import JORTS.gameElements.map.TerrainFactory;
import JORTS.gameElements.units.UnitDefinition;
import JORTS.uiComponents.textFields.ResourceList;
import JORTS.uiComponents.uiManagers.KeyboardManager;
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

	public static void readTerrainDefinition(String fileName) 
	{
		JSONObject jsonObject = readFile("terrains//"+fileName);
		TerrainFactory.defaultTerrain = (String)jsonObject.getOrDefault("default", "");
		JSONArray array = (JSONArray) jsonObject.get("definitions");
		for(Object j:array)
		{
			new TerrainDefinition((JSONObject)j);
		}
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
	public static void readScenarioFile(String fileName)
	{
		String path = System.getProperty("user.dir") + "//defs";
		Scanner fileReader;
		try {
			fileReader = new Scanner(new File(path+"//scenarios//"+fileName));
			String s="";
			while(fileReader.hasNext())
			{
				String temp=fileReader.nextLine();
				if(temp.contains("//"))
					{
						System.out.println("edited"+temp.substring(0,temp.indexOf("//")));
						temp=temp.substring(0,temp.indexOf("//"));
					}
						
				s+=temp+";";
			}
			s=s.replaceAll("/\\*.*?\\*/","");
			s=s.replaceAll(";+", ";");
			s=s.replaceAll(" +", " ");
			Main.initScript=s;
			System.out.println(s);
			} catch (FileNotFoundException e) {
			e.printStackTrace();}
	}
	public static void readResourcesdefinition(JSONArray array)
	{
		for(Object o:array)
		{
			JSONObject resource = (JSONObject)o;
			ResourceList.resources.add((String)resource.get("name"));
			if((String)resource.get("icon")!=null)
				Resource.addTexture((String)resource.get("name"),(String)resource.get("icon"));
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
			readUnitDefinition((String)s);
		}
		for(Object s:buildingDefs)
		{
			readBuildingDefinition((String)s);
		}
		
		readTerrainDefinition((String)jsonObject.get("terrain"));
		
		Main.worldMap.loadFile(new File(MAP_DIRECTORY+"//"+map));
		
		String KeyMappingFile=(String)jsonObject.get("keyMappings");
		readKeyMappings(KeyMappingFile);
		
		JSONArray resources = (JSONArray)jsonObject.get("resources");
		readResourcesdefinition(resources);
		/*for(Object s:resources)
		{
			ResourceList.resources.add((String)s);
		}*/
		readScenarioFile((String)jsonObject.get("scenario"));
		
		JSONArray players = (JSONArray)jsonObject.get("players");
		for(Object o:players)
		{
			Main.players.add(new Player((String)o));
		}
		
	}
}
