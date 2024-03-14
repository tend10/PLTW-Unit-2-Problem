import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.ArrayList;

// TODO: Scan reviews.csv, use a target list of words to return a list of target usernames.

public class DataAnalyzer {
  
  private static HashMap<String, Double> targetWords = new HashMap<String, Double>();
  
  private static ArrayList<Integer> sentimentValues = new ArrayList<Integer>();

  private static ArrayList<String> usernames = new ArrayList<String>();

  // Parses the reviews.csv file and stores the target words in a hashmap, then uses the reviews to map them to specific sentiment values using words from words.txt
  public static ArrayList<Integer> getSentimentValues(String fileName) {
    try {
      Scanner scanner = new Scanner(new File(fileName));
      scanner.nextLine();
      while (scanner.hasNext()) {
        int sentimentCount = 0;
        String line = scanner.nextLine();
        String[] lineSplit = line.split("~");
        String review = lineSplit[2];
        for (String word : review.split(" ")) {
          String formattedWord = removePunctuation(word);
          try {
            sentimentCount += targetWords.get(formattedWord.toLowerCase());
          } catch (Exception e) {
            sentimentCount += 0.0;
          }
        }
        sentimentValues.add(sentimentCount);

      }
    } catch (Exception e) {
      System.out.println("File not found.");
    }
    return sentimentValues;
  }

  // Parses reviews.csv and then stores the usernames in an ArrayList
  public static ArrayList<String> getUsernames(String fileName) {
    try {
      Scanner scanner = new Scanner(new File(fileName));
      scanner.nextLine();
      while (scanner.hasNext()) {
        String line = scanner.nextLine();
        String[] lineSplit = line.split("~");
        usernames.add(lineSplit[0]);
      }
    } catch (Exception e) {
      System.out.println("File not found.");
    }
    return usernames;
  }

  // Parses words.txt and stores the target words in a hashmap
  public static void targetWordsFormatter(String fileName) {
    try {
      Scanner input = new Scanner(new File(fileName));

      while (input.hasNextLine()) {
        String[] temp = input.nextLine().split(",");
        targetWords.put(temp[0], Double.parseDouble(temp[1]));
      }
      input.close();
    } catch (Exception e) {
      System.out.println("Error reading or parsing file");
    }
  }

  // Removes its punctuation by returning only alphabetic characters
  public static String removePunctuation(String word) {
    while (word.length() > 0 && !Character.isAlphabetic(word.charAt(0))) {
      word = word.substring(1);
    }
    while (word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length() - 1))) {
      word = word.substring(0, word.length() - 1);
    }
    return word;

  }

  // Gives specific movie recommendations based on the usernames and sentiment values
  public static ArrayList<String> giveRecommendations() {
    // Contains the various ads from the txt file.
    ArrayList<String> ads = new ArrayList<String>();
    try {
      // Goes through file line by line and adds it to ads list.
      File advertisements = new File ("advertisements.txt");
      Scanner sc = new Scanner(advertisements);
      while (sc.hasNextLine()) {
        ads.add(sc.nextLine());
      }
    } catch(FileNotFoundException e){
      System.out.println("Error");
    }
    
    ArrayList<Integer> sentimentVals = DataAnalyzer.getSentimentValues("reviews.csv");
    ArrayList<String> userNames = DataAnalyzer.getUsernames("reviews.csv");
    ArrayList<String> recommendations = new ArrayList<String>();
    
    for (int i = 0; i < sentimentVals.size(); i++) {
      if (sentimentVals.get(i) > 0) {
        recommendations.add("Hi, " + userNames.get(i) + "! " + ads.get(0) + "\n");
      } else if (sentimentVals.get(i) == 0) {
        recommendations.add("Hi, " + userNames.get(i) + "! " + ads.get(1) + "\n");
      } else {
        recommendations.add("Hi, " + userNames.get(i) + "! " + ads.get(2) + "\n");
      }
    }
    
      return recommendations;
    }
  
  // Prints the given recommendations
  public static void printRecommendations() {
    ArrayList<String> recommendations = DataAnalyzer.giveRecommendations();
    for (String recommendation : recommendations) {
      System.out.println(recommendation);
    }
  }
}