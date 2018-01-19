package cps.entities.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PeriodicReport implements Serializable {
  private static final long serialVersionUID = 1L;

  private Map<String, StatisticalData> entries = new HashMap<>();

  public PeriodicReport() {
    entries.put("realizedOrders", null);
    entries.put("canceledOrders", null);
    entries.put("disabledParkingHours", null);
  }

  public StatisticalData getData(String key) {
    return entries.get(key);
  }

  public void setData(String key, StatisticalData data) {
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
