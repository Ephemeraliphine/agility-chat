package org.ephemerality;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Random;
import java.util.Set;




@Slf4j
@PluginDescriptor(
		name = "Agility Chat"
)
public class AgilityChatPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private AgilityChatConfig config;

	private int lastXp = -1;

	private int ticksRemaining = 0;

	@Override
	protected void startUp()
	{
		log.info("Agility Chat Plugin started!");
		System.out.println("plugin started");
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		if (event.getSkill() != Skill.AGILITY)
			return;

		System.out.println("Agility XP gained: "+event.getXp());
		log.info("Agility XP gained: {}", event.getXp());


		int currentXp = event.getXp();
		if (lastXp == -1)
		{
			lastXp = currentXp;
			return;
		}

		int delta = currentXp - lastXp;
		lastXp = currentXp;

		log.info("Agility XP gained: {}", delta);

		if (delta <= 0 || delta > 1000)
			return;


		int chance = config.chance();
		if (chance <= 0 || new Random().nextInt(chance) != 0)
			return;

		String playerName = client.getLocalPlayer().getName();
		String message = config.message();
		String fullMessage = playerName + ": <col=0000ff>" + message + "</col>";

		client.addChatMessage(ChatMessageType.SPAM, "", fullMessage, null);
		client.getLocalPlayer().setOverheadText(message);
		ticksRemaining = 5;
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		if (ticksRemaining > 0)
		{
			ticksRemaining--;
			if (ticksRemaining == 0)
			{
				client.getLocalPlayer().setOverheadText(null); // Clear the bubble
			}
		}
	}

	@Provides
	AgilityChatConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AgilityChatConfig.class);
	}
}
