import java.util.Comparator;

// Compares two edges
/**
 * filler documentation
 */
public class EdgeComparator implements Comparator<Edge> {

  // Returns -1 if o1 has the smaller weight, returns 1 if o2 has the smaller
  // weight
  /**
   * filler documentation
   */
  public int compare(Edge o1, Edge o2) {
    return Double.compare(o1.getWeight(), o2.getWeight());
  }

}