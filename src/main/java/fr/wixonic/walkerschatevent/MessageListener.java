package fr.wixonic.walkerschatevent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public final class MessageListener implements Listener {
	private final Map<String, BiConsumer<Player, String>> answers = new HashMap<>();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String key = Question.format(event.getMessage());

		BiConsumer<Player, String> answer = this.answers.get(key);
		if (answer != null)
			answer.accept(event.getPlayer(), key);
	}

	public void listenTo(List<String> answers, BiConsumer<Player, String> listener) {
		for (String answer : answers) {
			this.answers.put(Question.format(answer), listener);
		}
	}

	public void stopListeningTo(String answer) {
		this.answers.remove(Question.format(answer));
	}

	public void stopListeningTo(List<String> answers) {
		for (String answer : answers) {
			this.answers.remove(Question.format(answer));
		}
	}
}