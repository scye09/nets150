import java.util.HashMap;
import java.util.Set;

public class Movie implements Comparable<Movie> {
  
  private String title;
  private int id;
  private HashMap<String, Integer> tf;
  private String summary;
  
  public Movie(String title, String summary) {
    this.title = title;
    this.summary = summary.replaceAll("[^A-Za-z0-9 ]", "").toLowerCase();
    processTF();
  }
  
  private void processTF() {
    tf = new HashMap<>();
    for (String term : summary.split(" ")) {
      if (!term.equals("")) {
        if (tf.containsKey(term)) {
          tf.put(term, tf.get(term) + 1);
        } else {
          tf.put(term, 1);
        }
      }
    }
  }
  
  public String getTitle() {
    return this.title;
  }
  
  public int getId() {
    return this.id;
  }
  
  public String getSummary() {
    return this.summary;
  }
  
  public Set<String> getTermList() {
    return tf.keySet();
  }
  
  public double getTermFrequency(String word) {
    if (tf.containsKey(word)) {
      return tf.get(word);
    } else {
      return 0;
    }
  }

  @Override
  public int compareTo(Movie o) {
    return title.compareToIgnoreCase(o.title);
  }

}
