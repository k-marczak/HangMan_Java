import java.awt.datatransfer.FlavorEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class WordDB {

    private HashMap<String, String[]> wordList;


    private ArrayList<String> categories;


    public WordDB(){
        try{
            wordList = new HashMap<>();
            categories = new ArrayList<>();

            // get file path
            String filePath = getClass().getClassLoader().getResource(CommonConstants.DATA_PATH).getPath();

            if(filePath.contains("%20")) filePath = filePath.replaceAll("%20", " ");
            BufferedReader reader = new BufferedReader(new FileReader(filePath));


            String line;

            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");

                // the first word of each line represents the caregoeu

                String category = parts[0];
                categories.add(category);

                // the rest of the values from parts be the words rea;tove to the category
                String values[] = Arrays.copyOfRange(parts, 1, parts.length);
                wordList.put(category, values);

            }
        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }


    public String[] loadChallenge(){

        Random rand = new Random();

        // generate random number to choose category
        String category = categories.get(rand.nextInt(categories.size()));

        // generate random number to choose the value from category
        String[] categoryValues = wordList.get(category);
        String word = categoryValues[rand.nextInt(categoryValues.length)];


        // [0]  -> category and [1] -> word
        return new String[] {category.toUpperCase(), word.toUpperCase()};

    }


}
