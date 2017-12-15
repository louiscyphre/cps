package cps.api.response;

import java.util.Collection;
import cps.entities.models.OnetimeService;

public class ListOnetimeEntriesResponse extends ServerResponse {
	private static final long serialVersionUID = 1L;
	private Collection<OnetimeService> data;

	public ListOnetimeEntriesResponse(String description, Collection<OnetimeService> data) {
		super(STATUS_OK, description);
		this.data = data;
	}

	public Collection<OnetimeService> getData() {
		return data;
	}

	public void setData(Collection<OnetimeService> data) {
		this.data = data;
	}
}
