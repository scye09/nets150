import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
  
  private static String summaryFile = "plot_summaries1.txt";
  private static String metadataFile = "movie.metadata.tsv";
  
  public static HashMap<Integer, String> readSummaryFile() {
    File file = new File(summaryFile);
    HashMap<Integer, String> movieSummaries = new HashMap<>();
    try {
      Scanner scanner = new Scanner(file);

      while (scanner.hasNextLine()) {
        Pattern pattern = Pattern.compile("(\\d+)(\\s+)(.*)");
        Matcher m = pattern.matcher(scanner.nextLine());
        if (m.find()) {
          int id = Integer.parseInt(m.group(1));
          String description = m.group(3);
          movieSummaries.put(id, description);
        }
        
      }
    } catch (FileNotFoundException e) {

      e.printStackTrace();
    }
    return movieSummaries;
  }
  
  public static HashMap<Integer, String> readMetadataFile() {
    File file = new File(metadataFile);
    HashMap<Integer, String> movieTitles = new HashMap<>();
    try {
      Scanner scanner = new Scanner(file);
  
      while (scanner.hasNextLine()) {
        // assumes there is a year; data is not well-formatted
        Pattern pattern = Pattern.compile("(\\d+)(\t)([^\\s]+)(\t)(.*)(\t*)(\\d\\d\\d\\d)(.*)");
        Matcher m = pattern.matcher(scanner.nextLine());
        if (m.find()) {
          int id = Integer.parseInt(m.group(1));
          String title = m.group(5);
          if (title.contains("\t")) {
            title = title.substring(0, title.indexOf("\t"));
          }
          movieTitles.put(id, title);
   
        }
      }
    } catch (FileNotFoundException e) {
      
    }
    return movieTitles;
  }
  
  public static HashMap<String, String> getMovies(String searchText) {
    HashMap<Integer, String> movieSummaries = readSummaryFile();
    HashMap<Integer, String> movieTitles = readMetadataFile();
    
    HashMap<String, String> movies = new HashMap<>();
    
    for (Map.Entry<Integer, String> entry : movieSummaries.entrySet()) {
      movies.put(movieTitles.get(entry.getKey()), entry.getValue());
    }
    return movies;
  }
  
  public static void getMoviesAndSummaries(String searchText) {
    
    HashMap<String, String> getMovies = getMovies(searchText);
    ArrayList<Movie> movies = new ArrayList<>(getMovies.size() + 1);
    movies.add(new Movie("query", searchText));
    
    
    for (Map.Entry<String, String> entry : getMovies.entrySet()) {
      if (entry.getKey() != null) {
      movies.add(new Movie(entry.getKey(), entry.getValue()));
      }
    }
  
    Corpus corpus = new Corpus(movies);
    VectorSpaceModel model = new VectorSpaceModel(corpus);
    
    
    HashMap<String, Double> sorted = new HashMap<>();
    for (int i = 1; i < movies.size(); i++) {
      double similarity = model.cosineSimilarity(movies.get(0), movies.get(i));
      sorted.put(movies.get(i).getTitle(), similarity);
  
    }
    TreeMap<String, Double> sortedMovies = new TreeMap<>(new ValueComparator(sorted));
    sortedMovies.putAll(sorted);
    
    int i = 0;
    Iterator<Map.Entry<String, Double>> iterator = sortedMovies.entrySet().iterator();
    while (i < 10) {
      i++;
        System.out.println(iterator.next());
    }
  } 
  
  public static void main(String[] args) {
    System.out.println("Please enter the description of a movie you enjoyed");
    
    Scanner scanner = new Scanner(System.in);
    if (scanner.hasNextLine()) {
      String input = scanner.nextLine();
      getMoviesAndSummaries(input);
      
    }
  }
  
  static class ValueComparator implements Comparator<String> {
    HashMap<String, Double> sortedMap = new HashMap<String, Double>();
    
    public ValueComparator(HashMap<String, Double> map) {
      this.sortedMap.putAll(map);
    }

    @Override
    public int compare(String a, String b) {
      if (sortedMap.get(a) >= sortedMap.get(b)) {
        return -1;
      } else {
        return 1;
      }
    }
    
  }

}
