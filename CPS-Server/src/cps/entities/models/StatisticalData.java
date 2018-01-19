package cps.entities.models;

import java.io.Serializable;
import java.util.Collection;

public class StatisticalData implements Serializable {
  private static final long serialVersionUID = 1L;

  private double             mean;
  private double             median;
  private Collection<Double> distribution;

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

  public Collection<Double> getDistribution() {
    return distribution;
  }

  public void setDistribution(Collection<Double> distribution) {
    this.distribution = distribution;
  }

  public StatisticalData(double mean, double median, Collection<Double> distribution) {
    this.mean = mean;
    this.median = median;
    this.distribution = distribution;
  }
}
