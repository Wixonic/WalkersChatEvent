package fr.wixonic.walkerschatevent;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Main extends JavaPlugin {
	private static final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
	public static ConfigurationManager configManager;

	public static Economy economy;
	private static Main instance;
	private static Question question;

	public static void execute() {
		List<String> keys = Main.configManager.getKeys("questions");
		String key = keys.get(new Random().nextInt(0, keys.size()));

		Main.question = new Question();
		Main.question.answers = Main.configManager.getList("questions." + key + ".answers");
		Main.question.expires = Main.configManager.getInt("questions." + key + ".expires");
		Main.question.reward = Main.configManager.getInt("questions." + key + ".reward");
		Main.question.text = Main.configManager.getString("questions." + key + ".text");
		Main.question.winners = Main.configManager.getInt("questions." + key + ".winners");

		Main.question.start();
	}

	public static void loop() {
		int delay;
		String string = Main.configManager.getString("delay");

		if (string.contains("-")) {
			try {
				delay = new Random().nextInt(Integer.parseInt(string.split("-")[0]), Integer.parseInt(string.split("-")[1]));
			} catch (Exception ignored) {
				Main.getInstance().getLogger().warning("Invalid field in config.yml: delay");
				delay = 1000;
			}
		} else delay = Integer.parseInt(string);

		Main.scheduledExecutor.schedule(() -> {
			Main.execute();
			Main.loop();
		}, delay, TimeUnit.SECONDS);
	}

	public static Main getInstance() {
		return Main.instance;
	}

	@Override
	public void onEnable() {
		Main.instance = this;

		if (!this.setupEconomy()) {
			this.getLogger().severe("Disabled due to no Vault dependency found");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		this.saveDefaultConfig();
		Main.configManager = new ConfigurationManager(this.getConfig());

		if (Main.configManager.getBoolean("enabled")) Main.loop();
		this.getServer().getPluginManager().registerEvents(Question.listener, this);
		Objects.requireNonNull(this.getCommand("chatevent")).setExecutor(new ChatEvent());

		this.getLogger().info(Main.configManager.getKeys("questions").size() + " questions loaded");
	}

	private boolean setupEconomy() {
		if (this.getServer().getPluginManager().getPlugin("Vault") == null) return false;
		RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) return false;
		Main.economy = rsp.getProvider();
		return true;
	}
}