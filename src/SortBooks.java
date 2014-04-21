import java.util.*;

public class SortBooks {

    public ArrayList<HashMap<String, String>> sortByValue(ArrayList<HashMap<String, String>> arrayList) {
        Collections.sort(arrayList, new Comparator<HashMap<String, String>>() {
            public int compare(HashMap<String, String> m1, HashMap<String, String> m2) {
                return (m1.get("lastName")).compareTo(m2.get("lastName"));
            }
        });

        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        for (HashMap<String, String> entry : arrayList) {
            result.add(entry);
        }
        return result;
    }
}