import java.util.HashMap;
import java.util.Set;

/**
 * Code adapted from Swap's Vector Space model
 * This class implements the Vector-Space model.
 * It takes a corpus and creates the tf-idf vectors for each document.
 *
 */
public class VectorSpaceModel {
  
  /**
   * The corpus of movies.
   */
  private Corpus corpus;

  private HashMap<Movie, HashMap<String, Double>> tfIdfWeights;
  
  public VectorSpaceModel(Corpus corpus) {
    this.corpus = corpus;
    tfIdfWeights = new HashMap<Movie, HashMap<String, Double>>();
    
    createTfIdfWeights();
  }

  /**
   * This creates the tf-idf vectors.
   */
  private void createTfIdfWeights() {
//    System.out.println("Creating the tf-idf weight vectors");
    Set<String> terms = corpus.getInvertedIndex().keySet();
    
    for (Movie movie : corpus.getMovies()) {
      HashMap<String, Double> weights = new HashMap<String, Double>();
      
      for (String term : terms) {
        double tf = movie.getTermFrequency(term);
        double idf = corpus.getInverseDocumentFrequency(term);
        
        double weight = tf * idf;
        
        weights.put(term, weight);
      }
      tfIdfWeights.put(movie, weights);
    }
  }
  
  /**
   * This method will return the magnitude of a vector.
   * @param document the document whose magnitude is calculated.
   * @return the magnitude
   */
  private double getMagnitude(Movie movie) {
    double magnitude = 0;
    HashMap<String, Double> weights = tfIdfWeights.get(movie);
    
    for (double weight : weights.values()) {
      magnitude += weight * weight;
    }
    
    return Math.sqrt(magnitude);
  }

  /**
   * This will take two documents and return the dot product.
   * @param d1 Document 1
   * @param d2 Document 2
   * @return the dot product of the documents
   */
  private double getDotProduct(Movie d1, Movie d2) {
    double product = 0;
    HashMap<String, Double> weights1 = tfIdfWeights.get(d1);
    HashMap<String, Double> weights2 = tfIdfWeights.get(d2);
    
    for (String term : weights1.keySet()) {
      product += weights1.get(term) * weights2.get(term);
    }
    
    return product;
  }
  
  /**
   * This will return the cosine similarity of two documents.
   * This will range from 0 (not similar) to 1 (very similar).
   * @param d1 Document 1
   * @param d2 Document 2
   * @return the cosine similarity
   */
  public double cosineSimilarity(Movie d1, Movie d2) {
    return getDotProduct(d1, d2) / (getMagnitude(d1) * getMagnitude(d2));
  }
}