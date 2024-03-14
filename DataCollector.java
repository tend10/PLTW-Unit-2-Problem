// import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class DataCollector {
  public static void main(String[] args) throws IOException {
    FileWriter writer = new FileWriter("reviews.csv");
    writer.append("username~title~review~rating~date\n");
    

    //Pages only have 25 reviews each
    String[] urls = {"https://www.imdb.com/title/tt15239678/reviews?sort=submissionDate&dir=desc&ratingFilter=0", "https://www.imdb.com/title/tt15239678/reviews?sort=submissionDate&dir=asc&ratingFilter=0"};
    ArrayList<String> names = new ArrayList<String>();

    
    for (String url : urls) {
    
      Document document = Jsoup.connect(url).get();
      Elements dataNames = document.select("span.display-name-link");
      Elements dataTitles = document.select("a.title");
      Elements dataReviews = document.select("div.text");
      Elements dataDates = document.select("span.review-date");
      Elements dataRatings = document.select("div.ipl-ratings-bar");

      //Checks for duplicate usernames and separates fields using "~" character
      for (int i = 0; i < dataNames.size()-1; i++) {
        if (!names.contains(dataNames.get(i).text())) {
          writer.append(dataNames.get(i).text() + "~" + dataTitles.get(i).text() + "~" + dataReviews.get(i).text() + "~" + dataRatings.get(i).text() + "~" + dataDates.get(i).text() + "\n");
          names.add(dataNames.get(i).text());
        }
        
      }
    
    }
    writer.flush();
    writer.close();

    DataAnalyzer.targetWordsFormatter("words.txt");
    DataAnalyzer.printRecommendations();
    
  }
}