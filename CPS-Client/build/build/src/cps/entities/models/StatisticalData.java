package cps.entities.models;

import java.io.Serializable;
import java.util.Arrays;

public class StatisticalData implements Serializable {
  private static final long serialVersionUID = 1L;

  private double   mean         = 0;
  private double   median       = 0;
  private double   total        = 0;
  private double[] distribution = null;
  private double[] values       = null;
  private String   name         = null;

  public double getMean() {
    return mean;
  }

  public void setMean(double mean) {
    this.mean = mean;
  }

  public double getMedian() {
    return median;
  }

  public void setMedian(double median) {
    this.median = median;
  }

  public StatisticalData(double[] values) {
    this.total = sumOf(values);
    this.mean = this.total / values.length;
    this.median = medianOf(values);
    this.values = values;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  static double sumOf(double[] array) {
    double sum = 0;

    for (double elem : array) {
      sum += elem;
    }

    return sum;
  }

  static double medianOf(double[] array) {
    double[] copy = array.clone();
    Arrays.sort(copy);

    if (copy.length % 2 == 1) {
      // if odd number of elements -> return the middle
      return copy[copy.length / 2];
    } else {
      // if even number of elements -> return the average of the two in the middle
      int m = copy.length / 2;
      return (copy[m - 1] + copy[m]) / 2.0;
    }
  }

  public double[] getDistribution() {
    return distribution;
  }

  public void setDistribution(double[] distribution) {
    this.distribution = distribution;
  }

  public double[] getValues() {
    return values;
  }

  public void setValues(double[] values) {
    this.values = values;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
