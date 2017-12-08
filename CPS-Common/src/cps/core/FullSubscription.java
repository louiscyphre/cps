package cps.core;

import java.time.LocalDateTime;

public class FullSubscription extends SubscriptionRequest {
	private static final long serialVersionUID = 1L;

	public FullSubscription(int id, int customerID, int carID, LocalDateTime startDate) {
		super(id, customerID, carID, startDate);
	}
}
