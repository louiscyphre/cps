package cps.client.controller;

import java.util.Collection;

import cps.entities.models.OnetimeService;

public interface OnetimeEntriesController extends ViewController {
  public void setOnetimeEntries(Collection<OnetimeService> collection);

  public void removeEntry(int onetimeServiceID);
}
