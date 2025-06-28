package org.ephemerality;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("agilitychat")
public interface AgilityChatConfig extends Config
{
	@ConfigItem(
			keyName = "chance",
			name = "Trigger Chance",
			description = "1 in X chance for message to appear",
			position = 0
	)
	default int chance()
	{
		return 10;
	}

	@ConfigItem(
			keyName = "message",
			name = "Message",
			description = "Message to show above your head",
			position = 1
	)
	default String message()
	{
		return "Parkour!";
	}
}