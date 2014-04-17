import java.util.HashMap;


//Holds a map containing a persons contact info as well
//as a toStr method that delimits data by tabs
public class MapAddress {

	protected HashMap<String, String> map;

    public MapAddress() {
        map = new HashMap<String, String>();
    }
	
	//This is the constructor for adding a new Address
//	public MapAddress() {
//        map = null;
//		EntryWindow window = new EntryWindow();
//		map = window.RunEntryWindow(map); //?(Map)
//	}
//
//	public void edit(){
//		EntryWindow window = new EntryWindow();
//		map = window.RunEntryWindow(map);
//	}

    public void put(String key, String value) {
        map.put(key, value);
    }

    @Override
	public String toString() {
		String returnStr = "";
		returnStr += map.get("firstName");
		returnStr += "\t";
		returnStr += map.get("lastName");
		returnStr += "\t";
		returnStr += map.get("street");
		returnStr += "\t";
		returnStr += map.get("city");
		returnStr += "\t";
		returnStr += map.get("zip");
		returnStr += "\t";
		returnStr += map.get("phone");
		returnStr += "\t";
		returnStr += map.get("email");
		returnStr += "\t";

		return returnStr;
	}


}