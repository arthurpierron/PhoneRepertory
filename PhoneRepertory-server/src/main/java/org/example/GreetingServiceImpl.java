package org.example;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	public String getFilePath() {
		return getServletContext().getRealPath("/WEB-INF/data/users.txt");
	}

	@Override
	public boolean createAccount(String username, String password) {
		System.out.println(">> Tentative de création de compte : " + username);

		Map<String, String> users = readUsers();

		if (users.containsKey(username)) {
			System.out.println(">> Échec : utilisateur déjà existant.");
			return false;
		}
		users.put(username, password);
		saveUsers(users);

		Map<String, String> newUsers = readUsers();
		if (newUsers.containsKey(username)) {
			System.out.println(">> Compte créé avec succès !");
			return true;
		} else {
			System.out.println(">> ERREUR : compte non écrit dans le fichier !");
			return false;
		}
	}


	@Override
	public boolean login(String username, String password) {
		Map<String, String> users = readUsers();
		return password.equals(users.get(username));
	}

	private void saveUsers(Map<String, String> users) {
		File file = new File(getFilePath());

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (Map.Entry<String, String> entry : users.entrySet()) {
				writer.write(entry.getKey() + ":" + entry.getValue());
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> readUsers() {
		Map<String, String> users = new HashMap<>();
		File file = new File(getFilePath());

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(":");
				if (parts.length == 2) {
					users.put(parts[0], parts[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return users;
	}
}
