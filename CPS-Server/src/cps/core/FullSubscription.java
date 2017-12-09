package cps.core;

import java.time.LocalDateTime;

public class FullSubscription extends SubscriptionRequest {
	private static final long serialVersionUID = 1L;

	public FullSubscription(int customerID, int carID, LocalDateTime startDate) {
		super(customerID, carID, startDate);
	}
}
