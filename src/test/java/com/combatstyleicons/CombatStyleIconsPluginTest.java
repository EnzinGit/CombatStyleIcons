package com.combatstyleicons;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CombatStyleIconsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CombatStyleIconsPlugin.class);
		RuneLite.main(args);
	}
}