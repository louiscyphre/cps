package cps.client.controller;

import java.util.Collection;

import cps.entities.models.OnetimeService;

/**
 * Interface for controllers with OnetimeEntries 
 */
public interface OnetimeEntriesController extends ViewController {
  /**
   * Sets inner OnetimeService collection with accordance to the given collection.
   * @param collection of the onetime services
   * @see OnetimeService
   */
  public void setOnetimeEntries(Collection<OnetimeService> collection);

  /**
   * Removes the given onetime service from inner collection.
   * @param onetimeServiceID
   */
  public void removeEntry(int onetimeServiceID);
}
