package org.example;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void createAccount(String username, String password, AsyncCallback<Boolean> callback);
	void login(String username, String password, AsyncCallback<Boolean> callback);
}
