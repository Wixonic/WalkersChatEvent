package fr.wixonic.walkerschatevent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Question {
	public final static MessageListener listener = new MessageListener();

	public List<String> answers;

	public int expires;
	public int reward;
	public String text;
	public int winners;

	public static String format(String string) {
		return string.toLowerCase().replaceAll(" ", "").replaceAll("’", "'");
	}

	public void start() {
		Bukkit.broadcastMessage("§3" + this.text + "\n§r§7Answer this question to win $" + this.reward);
		this.answers.replaceAll(Question::format);

		Question.listener.listenTo(answers, (Player player, String answer) -> {
			Question.listener.stopListeningTo(answer);
			this.answers.remove(answer);
			this.rewards(player);

			if (this.answers.isEmpty())
				Bukkit.broadcastMessage("§6The chat event is over!");
			else
				Bukkit.broadcastMessage(
						"§6" + this.answers.size() + " answer" + (this.answers.size() > 1 ? "s" : "") + " remaining!");
		});

		Executors.newSingleThreadScheduledExecutor().schedule(this::stop, this.expires, TimeUnit.SECONDS);
	}

	private void rewards(Player player) {
		player.sendMessage("§2This is a right answer! You won $" + this.reward);
		Main.economy.depositPlayer(player, this.reward);
	}

	public void stop() {
		if (!this.answers.isEmpty()) {
			Question.listener.stopListeningTo(answers);
			Bukkit.broadcastMessage("§3The chat event is over!");
		}
	}
}