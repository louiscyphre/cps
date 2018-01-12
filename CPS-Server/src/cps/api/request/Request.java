package cps.api.request;

import java.io.Serializable;
import cps.api.response.ServerResponse;

public abstract class Request implements Serializable {
	private static final long serialVersionUID = 1L;

	public abstract <T> ServerResponse handle(RequestHandler<T> handler, T session);
}
