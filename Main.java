import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class Main {


    public static void main(String[] args) throws IOException {

        List<String> stringList = returningMaps("https://api.exchangeratesapi.io/latest", true);

        Scanner kb = new Scanner(System.in);

        System.out.println("What currency do you want to convert from?");
        for (int i  = 0; i < stringList.size(); i++) {
            System.out.println("#"+ i + ": " + stringList.get(i));
        }
        int firstSelection = kb.nextInt();
        String fromCurrency = stringList.get(firstSelection);
        stringList.remove(firstSelection);
        System.out.println("What currency do you want to convert to?");
        for (int i  = 0; i < stringList.size(); i++) {
            System.out.println("#"+ i + ": " + stringList.get(i));
        }
        int SecondChoice = kb.nextInt();
        String toCurrency = stringList.get(SecondChoice);
        String parseURL = "https://api.exchangeratesapi.io/latest?symbols=" + fromCurrency + "," + toCurrency + "&base=" + fromCurrency;

        Double factor = Double.parseDouble((returningMaps(parseURL, false).get(1)));

        System.out.println("How much " + fromCurrency + " are you converting to " + toCurrency + "?");
        Double baseAmount = kb.nextDouble();
        System.out.printf("You have %.2f %s", baseAmount*factor, toCurrency);
    }

    private static String getURL(String URL) throws IOException{
        URL url = new URL(URL);

        URLConnection conn = url.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String finality = br.readLine();

        br.close();

        return finality;
    }
     private static List<String> returningMaps(String requestURL, Boolean keyOrValue) throws IOException{
        String jsonAPI = getURL(requestURL);

        JsonFactory factory = new JsonFactory();

        ObjectMapper mapper = new ObjectMapper(factory);

        JsonNode rootNode = mapper.readTree(jsonAPI);

        Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();

        String lookingInto = null;

        while (fieldsIterator.hasNext()) {

            Map.Entry<String,JsonNode> field = fieldsIterator.next();
            if (field.getKey().equals("rates"))
                lookingInto = field.getValue().toString();
        }
        JsonNode rootNode2 = mapper.readTree(lookingInto);

        Iterator<Map.Entry<String, JsonNode>> Iterate2 = rootNode2.fields();

        Map.Entry<String,JsonNode> field2 = null;

        return  keyOrValueOfMap(keyOrValue, Iterate2);
    }

    private static List<String> keyOrValueOfMap(Boolean KeyOrString, Iterator<Map.Entry<String, JsonNode>> Iterate2){
        Map.Entry<String,JsonNode> field2 = null;
        List<String> stringList = new ArrayList<String>();
        if(KeyOrString) {
            while(Iterate2.hasNext()){
                field2 = Iterate2.next();
                stringList.add(field2.getKey());
            }
            }else{
                while(Iterate2.hasNext()){
                    field2 = Iterate2.next();
                    stringList.add(field2.getValue().toString());
                }
            }
        return stringList;
    }
}