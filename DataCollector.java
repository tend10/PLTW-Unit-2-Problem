import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class DataCollector {
  public static void main(String[] args) throws IOException {
    // initializes reviews.csv, a file where the movie reviews are stored
    FileWriter writer = new FileWriter("reviews.csv");

    // writes a header for the reviews.csv file
    writer.append("username~title~review~rating~date\n");

    // URL from which IMDB reviews are taken - Pages only have 25 reviews each
    String[] urls = {"https://www.imdb.com/title/tt15239678/reviews?sort=submissionDate&dir=desc&ratingFilter=0", "https://www.imdb.com/title/tt15239678/reviews?sort=submissionDate&dir=asc&ratingFilter=0", "https://www.imdb.com/title/tt15239678/reviews?sort=reviewVolume&dir=desc&ratingFilter=0"};

    // ArrayList to store usernames
    ArrayList<String> names = new ArrayList<String>();

    // goes through the IMDB page and selects the relevant elements to include in the reviews.csv file
    for (String url : urls) {
      Document document = Jsoup.connect(url).get();
      Elements dataNames = document.select("span.display-name-link");
      Elements dataTitles = document.select("a.title");
      Elements dataReviews = document.select("div.text");
      Elements dataDates = document.select("span.review-date");
      Elements dataRatings = document.select("div.ipl-ratings-bar");

      //Checks for duplicate usernames and separates fields using "~" character
      for (int i = 0; i < dataNames.size()-2; i++) {
        if (!names.contains(dataNames.get(i).text())) {
          writer.append(dataNames.get(i).text() + "~" + dataTitles.get(i).text() + "~" + dataReviews.get(i).text() + "~" + dataRatings.get(i).text() + "~" + dataDates.get(i).text() + "\n");
          names.add(dataNames.get(i).text());
        }
      }
    }

    // closes out the reviews.csv file
    writer.flush();
    writer.close();

    // method calls - formats targetedwords.txt file and prints out movie recommendations
    DataAnalyzer.targetWordsFormatter("targetedwords.txt");
    DataAnalyzer.printRecommendations();
  }
}
