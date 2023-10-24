package fr.wixonic.walkerschatevent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public final class Question {
	private final static MessageListener listener = new MessageListener();

	public List<String> answers;
	public int expires;
	public int reward;
	public String text;
	public int winners;

	public void start() {
		Bukkit.broadcastMessage("§6" + this.text + "\n§8Answer this question to win $" + this.reward);

		Question.listener.listenTo(answers, (Player player) -> {
			if (this.winners == 1) {
				this.winners -= 1;
				this.rewards(player);
				Question.listener.stopListeningTo(answers);
			} else if (this.winners == -1) {
				this.rewards(player);
			}
		});

		Executors.newSingleThreadScheduledExecutor().schedule(this::stop, this.expires, TimeUnit.SECONDS);
	}

	private void rewards(Player player) {
		player.sendMessage("This is the right answer! You won $" + this.reward);
		Main.economy.depositPlayer(player, this.reward);
	}

	public void stop() {
		if (this.winners > 0 || this.winners == -1) {
			Question.listener.stopListeningTo(answers);
			Bukkit.broadcastMessage("§6The chat event is over!");
		}
	}
}