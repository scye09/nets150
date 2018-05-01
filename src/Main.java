import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
  
  private static String summaryFile = "plot_summaries1.txt";
  private static String metadataFile = "movie.metadata.tsv";
  
  /**
   * Read the file that contains movie summaries to extract the id and summary
   * @return Map of <ID, Summary> entries
   */
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
      scanner.close();
    } catch (FileNotFoundException e) {

      e.printStackTrace();
    }
    return movieSummaries;
  }
  
  /**
   * Read the file that contains movie titles and their associated id's.
   * @return Hashmap of <ID, title> pairs
   */
  public static HashMap<Integer, String> readMetadataFile() {
    File file = new File(metadataFile);
    HashMap<Integer, String> movieTitles = new HashMap<>();
    try {
      Scanner scanner = new Scanner(file);
  
      while (scanner.hasNextLine()) {
        // assumes there is a year; data is not well-formatted in file
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
      scanner.close();
    } catch (FileNotFoundException e) {
      // do nothing
    }
    return movieTitles;
  }
  
  /**
   * Match movies and their summaries together.
   * @return hashmap of <movie, summary> pairs
   */
  public static HashMap<String, String> getMovies() {
    HashMap<Integer, String> movieSummaries = readSummaryFile();
    HashMap<Integer, String> movieTitles = readMetadataFile();
    
    HashMap<String, String> movies = new HashMap<>();
    
    for (Map.Entry<Integer, String> entry : movieSummaries.entrySet()) {
      if (movieTitles.get(entry.getKey()) != null) {
        movies.put(movieTitles.get(entry.getKey()).toLowerCase(), entry.getValue());
      }
    }
    return movies;
  }
  
  public static void main(String[] args) {

    System.out.println("Please enter a movie title or description");
    
    Scanner scanner = new Scanner(System.in);
    if (scanner.hasNextLine()) {
      String input = scanner.nextLine();
//      getMoviesAndSummaries(input);
//      System.out.println(getMovieGivenSummary(input));
      System.out.println(getMovieGivenTitle(input));
      
    }
    scanner.close();
  }
  
  /**
   * Given a movie title (assumed to be in the database), return a different movie whose summary
   * most closely matches the given movie's summary using the vector space model
   * @param movieTitle is movie to match
   * @return movie title with highest cosine similarity
   */
  public static String getMovieGivenTitle(String movieTitle) {
    movieTitle = movieTitle.toLowerCase();
    String match = "No matches";
    
    HashMap<String, String> getMovies = getMovies();
    ArrayList<Movie> movies = new ArrayList<>(getMovies.size() + 1);
    
    // ensure that movie is in the database
    if (getMovies.containsKey(movieTitle)) {
      movies.add(new Movie(movieTitle, getMovies.get(movieTitle)));
      
      for (Map.Entry<String, String> entry : getMovies.entrySet()) {
        // add the movie to the corpus if the movie is not the movie given as input
        if (entry.getKey() != null && !entry.getKey().equalsIgnoreCase(movieTitle)) {
        movies.add(new Movie(entry.getKey(), entry.getValue()));
        }
      }
      
      Corpus corpus = new Corpus(movies);
      VectorSpaceModel model = new VectorSpaceModel(corpus);
      
      // calculate cosine similarity between input movie and all other movies
      HashMap<String, Double> sorted = new HashMap<>();
      for (int i = 1; i < movies.size(); i++) {
        double similarity = model.cosineSimilarity(movies.get(0), movies.get(i));
        sorted.put(movies.get(i).getTitle(), similarity);
    
      }
      // sort movies by cosine similarity
      TreeMap<String, Double> sortedMovies = new TreeMap<>(new ValueComparator(sorted));
      sortedMovies.putAll(sorted);
      
      Iterator<Map.Entry<String, Double>> iterator = sortedMovies.entrySet().iterator();
  
      if (iterator.hasNext()) {
        match = iterator.next().getKey();
      }
    }
    
    return match;
  }
  
  /**
   * Given a string of words / a movie summary, find a movie that most closely matches this string
   * using the vector space model.
   * @param summary is query text to match 
   * @return movie with highest cosine similarity 
   */
  public static String getMovieGivenSummary(String summary) {
    String bestMatch = "";
    
    // get movies in <title, summary> form
    HashMap<String, String> getMovies = getMovies();
    ArrayList<Movie> movies = new ArrayList<>(getMovies.size() + 1);
    movies.add(new Movie("query", summary));
    
    // add movies to arraylist to create corpus
    for (Map.Entry<String, String> entry : getMovies.entrySet()) {
      if (entry.getKey() != null) {
        movies.add(new Movie(entry.getKey(), entry.getValue()));
      }
    }
    
    Corpus corpus = new Corpus(movies);
    VectorSpaceModel model = new VectorSpaceModel(corpus);
    
    // compare movie summaries to the input text
    HashMap<String, Double> sorted = new HashMap<>();
    for (int i = 1; i < movies.size(); i++) {
      double similarity = model.cosineSimilarity(movies.get(0), movies.get(i));
      sorted.put(movies.get(i).getTitle(), similarity);
  
    }
    // sort movies by cosine similarity
    TreeMap<String, Double> sortedMovies = new TreeMap<>(new ValueComparator(sorted));
    sortedMovies.putAll(sorted);
    
    Iterator<Map.Entry<String, Double>> iterator = sortedMovies.entrySet().iterator();
    if (iterator.hasNext()) {
      bestMatch = iterator.next().getKey();
    }
    
    return bestMatch;
  }
  
  /**
   * Class to sort a HashMap by value
   */
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
