import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * Code is adapted from Swap's Corpus class
 * This class represents a corpus of documents.
 * It will create an inverted index for these documents.
 *
 */
public class Corpus {
  
  private ArrayList<Movie> movies;
  
  private HashMap<String, Set<Movie>> invertedIndex;
  
  public Corpus(ArrayList<Movie> movies) {
    this.movies = movies;
    invertedIndex = new HashMap<String, Set<Movie>>();
    
    createInvertedIndex();
  }
  
  private void createInvertedIndex() {
    System.out.println("Creating the inverted index");
    
    for (Movie movie : movies) {
      Set<String> terms = movie.getTermList();
      
      for (String term : terms) {
        if (invertedIndex.containsKey(term)) {
          Set<Movie> list = invertedIndex.get(term);
          list.add(movie);
        } else {
          Set<Movie> list = new TreeSet<Movie>();
          list.add(movie);
          invertedIndex.put(term, list);
        }
      }
    }
  }
  
  public double getInverseDocumentFrequency(String term) {
    if (invertedIndex.containsKey(term)) {
      double size = movies.size();
      Set<Movie> list = invertedIndex.get(term);
      double documentFrequency = list.size();
      
      return Math.log10(size / documentFrequency);
    } else {
      return 0;
    }
  }

  public ArrayList<Movie> getMovies() {
    return movies;
  }

  public HashMap<String, Set<Movie>> getInvertedIndex() {
    return invertedIndex;
  }
}
