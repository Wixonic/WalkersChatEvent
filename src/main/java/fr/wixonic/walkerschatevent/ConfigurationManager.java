package fr.wixonic.walkerschatevent;

import org.bukkit.configuration.file.FileConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public final class ConfigurationManager {
	private final FileConfiguration config;

	public ConfigurationManager(FileConfiguration pluginConfig) {
		this.config = pluginConfig;
	}

	public void fillDefault() {
		if (this.config.getKeys(false).isEmpty()) {
			try {
				this.config.loadFromString(new String(Objects.requireNonNull(Main.getInstance().getResource("config.yml")).readAllBytes(), StandardCharsets.UTF_8));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public boolean getBoolean(String key) {
		return this.config.getBoolean(key);
	}

	public int getInt(String key) {
		return this.config.getInt(key);
	}

	public List<String> getKeys(String key) {
		return Objects.requireNonNull(this.config.getConfigurationSection(key)).getKeys(false).stream().toList();
	}

	public List<String> getList(String key) {
		return this.config.getStringList(key);
	}

	public String getString(String key) {
		return this.config.getString(key);
	}
}