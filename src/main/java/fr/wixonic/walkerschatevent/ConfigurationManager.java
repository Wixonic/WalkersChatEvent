package fr.wixonic.walkerschatevent;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public final class ConfigurationManager {
	private final FileConfiguration config;

	public ConfigurationManager(FileConfiguration pluginConfig) {
		this.config = pluginConfig;
	}

	public void fillDefault() {
		if (!this.config.contains("enabled") || !this.config.contains("delay")) {
			try {
				this.config.loadFromString("""
# Configuration file for W47K3R5 Chat Events plugin

# Enables automatic chat events
enabled: true
# Delay in seconds between two chat events, defaults to 0.5-1 days (10-20 minutes)
delay: 600-1200

questions:
  faded-release:
    # Answers in lowercase
    answers:
      - 2015
    # Time to answer the question (in seconds)
    expires: 10
    reward: 1000
    text: |
      When Faded has been released?
      Answer by sending the year in chat.
    # Sets to -1 to allow everyone to win until the timer expires
    winners: 1
  
  the-spectre-release:
    answers:
      - 2017
    expires: 10
    reward: 1500
    text: |
      When The Spectre has been released?
      Answer by sending the year in chat.
    winners: 1""");
			} catch (InvalidConfigurationException e) {
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
		return this.config.getConfigurationSection(key).getKeys(false).stream().toList();
	}

	public List<String> getList(String key) {
		return this.config.getStringList(key);
	}

	public String getString(String key) {
		return this.config.getString(key);
	}


	public void set(String key, Object value) {
		this.config.set(key, value);
	}
}