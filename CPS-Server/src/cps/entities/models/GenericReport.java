package cps.entities.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GenericReport<T> implements Serializable {
  private static final long serialVersionUID = 1L;

  private Map<String, T> entries = new HashMap<>();

  public GenericReport(String... keys) {
    for (String key : keys) {
      entries.put(key, null);
    }
  }

  public T getData(String key) {
    return entries.get(key);
  }

  public void setData(String key, T data) {
    if (entries.containsKey(key)) {
      // Only allow inserting predefined keys
      entries.put(key, data);
    }
  }

  public Set<String> keySet() {
    return entries.keySet();
  }

  public boolean containsKey(String key) {
    return entries.containsKey(key);
  }

}
