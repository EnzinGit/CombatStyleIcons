package com.combatstyleicons;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;

import java.awt.*;
import java.util.function.Consumer;

import static net.runelite.api.MenuAction.MENU_ACTION_DEPRIORITIZE_OFFSET;

@Slf4j
@PluginDescriptor(
	name = "Combat Style Icons", description = "Used for marking combat styles", tags = {"icon", "style", "tag", "tags", "type", "iconify"}
)
public class CombatStyleIconsPlugin extends Plugin
{

	public static CombatStyleIconsPlugin instance;

	@Inject
	private Client client;
	@Inject
	private ConfigManager configManager;
	@Inject
	private CombatStyleIconsConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject NpcOverlay npcOverlay;

	@Override
	protected void startUp() throws Exception
	{
		//log.info("Example started!");
		CombatStyleIconsPlugin.instance = this;
		overlayManager.add(npcOverlay);
	}

	public void forEachNpc(final Consumer<NPC> consumer)
	{
		for(NPC npc : client.getNpcs())
		{
			consumer.accept(npc);
		}
	}





	@Override
	protected void shutDown() throws Exception
	{
		//log.info("Example stopped!");
		overlayManager.remove(npcOverlay);
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		int type = event.getType();

		if (type >= MENU_ACTION_DEPRIORITIZE_OFFSET)
		{
			type -= MENU_ACTION_DEPRIORITIZE_OFFSET;
		}

		final MenuAction menuAction = MenuAction.of(type);


		if (menuAction == MenuAction.EXAMINE_NPC && client.isKeyPressed(KeyCode.KC_SHIFT))
		{


			// Only show draw options to npcs not affected by a wildcard entry, as wildcards will not be removed by menu options

			MenuEntry test = client.createMenuEntry(-1)
					.setOption(ColorUtil.prependColorTag("Combat Icon", Color.cyan))

					.setTarget(event.getTarget())
					.setIdentifier(event.getIdentifier())
					.setType(MenuAction.RUNELITE_SUBMENU);
			MenuEntry test4 = client.createMenuEntry(3)

					.setOption("Clear")
					.setParent(test)
					.setIdentifier(event.getIdentifier())
					.setType(MenuAction.RUNELITE)
					.onClick(e -> this.clearIcon(event.getMenuEntry()));
			MenuEntry test1 = client.createMenuEntry(0)
					.setOption(ColorUtil.prependColorTag("Melee", Color.red))
					.setParent(test)
					.setIdentifier(event.getIdentifier())
					.setType(MenuAction.RUNELITE)
					.onClick(e -> this.toggleDraw(event.getMenuEntry(), IconType.Melee));
			MenuEntry test2 = client.createMenuEntry(1)
					.setOption(ColorUtil.prependColorTag("Range", Color.green))

					.setParent(test)
					.setIdentifier(event.getIdentifier())
					.setType(MenuAction.RUNELITE)
					.onClick(e -> this.toggleDraw(event.getMenuEntry(), IconType.Range));
			MenuEntry test3 = client.createMenuEntry(2)
					.setOption(ColorUtil.prependColorTag("Magic", Color.MAGENTA))
					.setParent(test)
					.setIdentifier(event.getIdentifier())
					.setType(MenuAction.RUNELITE)
					.onClick(e -> this.toggleDraw(event.getMenuEntry(), IconType.Magic));


		}
	}

	public void clearIcon(MenuEntry click)
	{
		if(this.checkContains(click.getNpc().getId(), config.meleeSelections()))
		{
			CombatStyleIconsPlugin.instance.client.addChatMessage(ChatMessageType.BROADCAST, "Combat-Style-Icons", "Removed " + click.getNpc().getName() + " Marker", "HERE 2");

			configManager.setConfiguration("csi", "meleeSelections", config.meleeSelections().replaceAll(click.getNpc().getId() + " ", ""));
		}else if(this.checkContains(click.getNpc().getId(), config.magicSelections()))
		{
			CombatStyleIconsPlugin.instance.client.addChatMessage(ChatMessageType.BROADCAST, "Combat-Style-Icons", "Removed " + click.getNpc().getName() + " Marker", "HERE 2");

			configManager.setConfiguration("csi", "magicSelections", config.magicSelections().replaceAll(click.getNpc().getId() + " ", ""));
		}else if(this.checkContains(click.getNpc().getId(), config.rangeSelections()))
		{
			CombatStyleIconsPlugin.instance.client.addChatMessage(ChatMessageType.BROADCAST, "Combat-Style-Icons", "Removed " + click.getNpc().getName() + " Marker", "HERE 2");

			configManager.setConfiguration("csi", "rangeSelections", config.rangeSelections().replaceAll(click.getNpc().getId() + " ", ""));
		}
	}

	public void toggleDraw(MenuEntry click, IconType type)
	{
		this.clearIcon(click);

		switch(type)
		{
			case Magic:
				CombatStyleIconsPlugin.instance.client.addChatMessage(ChatMessageType.BROADCAST, "Combat-Style-Icons", "Added " + click.getNpc().getName() + " Marker", "HERE 2");

				configManager.setConfiguration("csi", "magicSelections", config.magicSelections() + click.getNpc().getId() + " ");
				break;
			case Melee:
				CombatStyleIconsPlugin.instance.client.addChatMessage(ChatMessageType.BROADCAST, "Combat-Style-Icons", "Added " + click.getNpc().getName() + " Marker", "HERE 2");

				configManager.setConfiguration("csi", "meleeSelections", config.meleeSelections() + click.getNpc().getId() + " ");
				break;
			case Range:
				CombatStyleIconsPlugin.instance.client.addChatMessage(ChatMessageType.BROADCAST, "Combat-Style-Icons", "Added " + click.getNpc().getName() + " Marker", "HERE 2");

				configManager.setConfiguration("csi", "rangeSelections", config.rangeSelections() + click.getNpc().getId() + " ");
				break;
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}



	@Provides
	CombatStyleIconsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CombatStyleIconsConfig.class);
	}

	public boolean checkContains(int id, String list)
	{
		String[] parts = list.split(" ");

		for(String part : parts)
		{
			try{
				if(Integer.parseInt(part) == id)
				{
					return true;
				}
			}catch(Exception e)
			{

			}

		}
		return false;
	}
}
