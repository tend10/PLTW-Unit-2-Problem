import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.ArrayList;

public class DataAnalyzer {
  // initialize Hashmap to store words and their corresponding sentiment values
  private static HashMap<String, Double> targetWords = new HashMap<String, Double>();

  // stores a list of sentiment values for each word in a review
  private static ArrayList<Integer> sentimentValues = new ArrayList<Integer>();

  // stores all the usernames that correspond to a review
  private static ArrayList<String> usernames = new ArrayList<String>();

  
  // Parses the reviews.csv file and stores the target words in a hashmap, then uses the reviews to map them to specific sentiment values using words from targetedwords.txt
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

  
  // Parses reviews.csv to get the usernames that correspond to reviews, then stores the usernames in an ArrayList
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

  
  // Parses targetedwords.txt and stores the target words in a hashmap
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

  
  // Removes all punctuation from a file and returns only alphabetic characters - used for sentiment value analysis
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

    // method calls to get usernames and sentiment values of reviews
    ArrayList<Integer> sentimentVals = DataAnalyzer.getSentimentValues("reviews.csv");
    ArrayList<String> userNames = DataAnalyzer.getUsernames("reviews.csv");

    // Goes through each username and sentiment value, and adds the corresponding advertisement to the recommendations list.
    ArrayList<String> recommendations = new ArrayList<String>();
    for (int i = 0; i < sentimentVals.size(); i++) {
      if (sentimentVals.get(i) > 2) {
        recommendations.add("Hi, " + userNames.get(i) + "! " + ads.get(0) + "\n");
      } else if (sentimentVals.get(i) < 0) {
        recommendations.add("Hi, " + userNames.get(i) + "! " + ads.get(1) + "\n");
      } else {
        recommendations.add("Hi, " + userNames.get(i) + "! " + ads.get(2) + "\n");
      }
    }
      return recommendations;
    }

  
  // Goes through an ArrayList of recommendations and prints each one to the console
  public static void printRecommendations() {
    ArrayList<String> recommendations = DataAnalyzer.giveRecommendations();
    for (String recommendation : recommendations) {
      System.out.println(recommendation);
    }
  }
}

