package cps.server.session;

public class SessionHolder {
	private CustomerSession customerSession;
	private ServiceSession serviceSession;

	public SessionHolder() {
		this.customerSession = null;
		this.serviceSession = null;
	}

	public SessionHolder(CustomerSession customerSession, ServiceSession serviceSession) {
		this.customerSession = customerSession;
		this.serviceSession = serviceSession;
	}

	public SessionHolder(CustomerSession customerSession) {
		this.customerSession = customerSession;
		this.serviceSession = null;
	}

	public SessionHolder(ServiceSession serviceSession) {
		this.customerSession = null;
		this.serviceSession = serviceSession;
	}

	public CustomerSession getCustomerSession() {
		return customerSession;
	}

	public CustomerSession acquireCustomerSession() {
		if (customerSession == null) {
			customerSession = new CustomerSession();
		}
		
		return customerSession;
	}

	public void setCustomerSession(CustomerSession customerSession) {
		this.customerSession = customerSession;
	}

	public ServiceSession getServiceSession() {
		return serviceSession;
	}
	
	public ServiceSession acquireServiceSession() {
		if (serviceSession == null) {
			serviceSession = new ServiceSession();
		}
		
		return serviceSession;
	}

	public void setServiceSession(ServiceSession serviceSession) {
		this.serviceSession = serviceSession;
	}
}
