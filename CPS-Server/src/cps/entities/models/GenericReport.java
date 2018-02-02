package cps.entities.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Serves as a basis for generating custom reports.
 * Encapsulates a HashMap for storage.
 * @param <T> the generic type */
public class GenericReport<T> implements Serializable {
  private static final long serialVersionUID = 1L;

  /** Data entries. */
  private Map<String, T> entries = new HashMap<>();
  
  /** Number of data rows. */
  int numRows;

  /** Instantiates a new generic report.
   * @param numRows the number of rows
   * @param keys the keys */
  public GenericReport(int numRows, String... keys) {
    for (String key : keys) {
      entries.put(key, null);
    }

    this.numRows = numRows;
  }

  /** Get the data item associated with the key.
   * @param key the key
   * @return the data */
  public T getData(String key) {
    return entries.get(key);
  }

  /** Set the data item associated with the key.
   * @param key the key
   * @param data the data */
  public void setData(String key, T data) {
    if (entries.containsKey(key)) {
      // Only allow inserting predefined keys
      entries.put(key, data);
    }
  }

  /** Return the key set of the HashMap.
   * @return the key set */
  public Set<String> keySet() {
    return entries.keySet();
  }

  /** Check if there is a data item associated with the key.
   * @param key the key
   * @return true if key exists */
  public boolean containsKey(String key) {
    return entries.containsKey(key);
  }

  public int getNumRows() {
    return numRows;
  }

  public void setNumRows(int numRows) {
    this.numRows = numRows;
  }

}
