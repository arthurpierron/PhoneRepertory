package org.example;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	public GreetingResponse greetServer(String username, String password) throws IllegalArgumentException {
		// Validation côté serveur
		if (!FieldVerifier.isValidName(username) || password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("Nom d'utilisateur ou mot de passe invalide.");
		}

		// Si tout va bien, construire une réponse
		GreetingResponse response = new GreetingResponse();
		response.setServerInfo(getServletContext().getServerInfo());
		response.setUserAgent(getThreadLocalRequest().getHeader("User-Agent"));

		return response;
	}
}

