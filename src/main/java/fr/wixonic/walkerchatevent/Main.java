package fr.wixonic.walkerchatevent;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
	public static ConfigurationManager configManager;
	public static InventoryListener inventoryListener;
	public static PlayerJoinListener playerJoinListener;
	public static Market market;

	@Override
	public final void onEnable() {
		configManager = new ConfigurationManager(getConfig());
		configManager.fillDefault();
		saveConfig();
	}
}