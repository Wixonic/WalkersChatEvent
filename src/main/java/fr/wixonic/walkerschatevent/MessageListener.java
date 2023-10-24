package fr.wixonic.walkerschatevent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class MessageListener implements Listener {
	private final Map<String, Consumer<Player>> answers = new HashMap<>();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Bukkit.getLogger().warning(event.getMessage().toLowerCase().replaceAll(" ", ""));

		Consumer<Player> answer = this.answers.get(event.getMessage().toLowerCase().replaceAll(" ", ""));
		if (answer != null) answer.accept(event.getPlayer());
	}

	public void listenTo(String answer, Consumer<Player> listener) {
		this.answers.put(answer, listener);
	}

	public void listenTo(List<String> answers, Consumer<Player> listener) {
		for (String answer : answers) {
			this.answers.put(answer, listener);
		}
	}

	public void stopListeningTo(String answer) {
		this.answers.remove(answer);
	}

	public void stopListeningTo(List<String> answers) {
		for (String answer : answers) {
			this.answers.remove(answer);
		}
	}
}