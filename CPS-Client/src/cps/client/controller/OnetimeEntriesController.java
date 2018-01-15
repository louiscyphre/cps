package cps.client.controller;

import java.util.List;

import cps.entities.models.OnetimeService;

public interface OnetimeEntriesController extends ViewController {
  public void setOnetimeEntries(List<OnetimeService> list);
}
