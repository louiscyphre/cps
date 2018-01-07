package cps.api.response;

import java.util.Collection;
import cps.entities.models.OnetimeService;

public class ListOnetimeEntriesResponse extends ServerResponse {
	private static final long serialVersionUID = 1L;
	private Collection<OnetimeService> data;
	private int customerID;

	public ListOnetimeEntriesResponse(Collection<OnetimeService> data, int customerID) {
		super(data != null, "ListOnetimeEntriesRequest");
		this.data = data;
		this.customerID = customerID;
	}

	public Collection<OnetimeService> getData() {
		return data;
	}

	public void setData(Collection<OnetimeService> data) {
		this.data = data;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
}
