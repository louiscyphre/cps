package cps.client.controller;

import java.util.Collection;

import cps.entities.models.OnetimeService;

public interface OnetimeEntriesController extends ViewController {
  /**
   * @param collection
   */
  public void setOnetimeEntries(Collection<OnetimeService> collection);

  /**
   * @param onetimeServiceID
   */
  public void removeEntry(int onetimeServiceID);
}
