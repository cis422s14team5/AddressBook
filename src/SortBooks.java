import java.util.*;

public class SortBooks {

    public ArrayList<HashMap<String, String>> sortByName(ArrayList<HashMap<String, String>> arrayList) {
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

    public ArrayList<HashMap<String, String>> sortByZip(ArrayList<HashMap<String, String>> arrayList) {
        Collections.sort(arrayList, new Comparator<HashMap<String, String>>() {
            public int compare(HashMap<String, String> m1, HashMap<String, String> m2) {
                String zip1 = m1.get("zip");
                String zip2 = m2.get("zip");

                if (zip1 == null) {
                    zip1 = "ZZZZZZZZZZZ";
                }
                if (zip2 == null) {
                    zip2 = "ZZZZZZZZZZZ";
                }

                return (zip1.compareTo(zip2));
            }
        });

        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        for (HashMap<String, String> entry : arrayList) {
            result.add(entry);
        }
        return result;
    }
}