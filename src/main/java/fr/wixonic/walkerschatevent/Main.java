package fr.wixonic.walkerschatevent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Main extends JavaPlugin {
	private static final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
	public static ConfigurationManager configManager;
	private static Main instance;

	public static void execute() {
		List<String> keys = Main.configManager.getKeys("questions");
		String key = keys.get(new Random().nextInt(0, keys.size()));

		Bukkit.broadcastMessage("§6" + Main.configManager.getString("questions." + key + ".text") + "\n§7Answer this question to win $" + Main.configManager.getInt("questions." + key + ".reward"));
	}

	public static void loop() {
		int delay;
		String string = Main.configManager.getString("delay");

		if (string.contains("-")) {
			try {
				delay = new Random().nextInt(Integer.parseInt(string.split("-")[0]), Integer.parseInt(string.split("-")[1]));
			} catch (Exception ignored) {
				Bukkit.getLogger().warning("Invalid field in config.yml: delay");
				delay = 1000;
			}
		} else delay = Integer.parseInt(string);

		Main.scheduledExecutor.schedule(Main::loop, delay, TimeUnit.SECONDS);
		Main.execute();
	}

	public static Main getInstance() {
		return Main.instance;
	}

	@Override
	public final void onEnable() {

		Main.instance = this;

		Main.configManager = new ConfigurationManager(this.getConfig());
		Main.configManager.fillDefault();
		this.saveConfig();

		if (Main.configManager.getBoolean("enabled")) Main.loop();
		this.getCommand("chatevent").setExecutor(new ChatEvent());
	}
}