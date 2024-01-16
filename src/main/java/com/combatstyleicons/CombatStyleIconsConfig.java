package com.combatstyleicons;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("csi")
public interface CombatStyleIconsConfig extends Config
{


	@ConfigItem(
		keyName = "meleeSelections",
		name = "Melee Selections",
		description = "Selections to show Melee Icons"
	)
	default String meleeSelections()
	{
		return "";
	}
	@ConfigItem(
			keyName = "rangeSelections",
			name = "Range Selections",
			description = "Selections to show Range Icons"
	)
	default String rangeSelections()
	{
		return "";
	}

	@ConfigItem(
			keyName = "magicSelections",
			name = "Magic Selections",
			description = "Selections to show Magic Icons"
	)
	default String magicSelections()
	{
		return "";
	}


}
