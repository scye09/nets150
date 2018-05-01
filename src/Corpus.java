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
  
  /**
   * An arraylist of all documents in the corpus.
   */
  private ArrayList<Movie> movies;
  
  /**
   * The inverted index. 
   * It will map a term to a set of documents that contain that term.
   */
  private HashMap<String, Set<Movie>> invertedIndex;
  
  /**
   * The constructor - it takes in an arraylist of movies.
   * It will generate the inverted index based on the movies.
   * @param movies the list of movies
   */
  public Corpus(ArrayList<Movie> movies) {
    this.movies = movies;
    invertedIndex = new HashMap<String, Set<Movie>>();
    
    createInvertedIndex();
  }
  
  /**
   * This method will create an inverted index.
   */
  private void createInvertedIndex() {
//    System.out.println("Creating the inverted index");
    
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
  
  /**
   * This method returns the idf for a given term.
   * @param term a term in a movie summary
   * @return the idf for the term
   */
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

  /**
   * Get the movies
   * @return movies
   */
  public ArrayList<Movie> getMovies() {
    return movies;
  }

  /**
   * Get the inverted index
   * @return inveted index
   */
  public HashMap<String, Set<Movie>> getInvertedIndex() {
    return invertedIndex;
  }
}
