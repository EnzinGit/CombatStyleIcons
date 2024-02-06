package com.combatstyleicons;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NpcOverlay extends Overlay {

    @Inject
    private Client client;

    @Inject
    private CombatStyleIconsConfig config;

    private BufferedImage meleeIcon, rangeIcon, magicIcon;
    @Inject
    private NpcOverlay()
    {
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGH);

            this.meleeIcon = ImageUtil.loadImageResource(this.getClass(), "/Attack_icon.png");
            this.rangeIcon = ImageUtil.loadImageResource(this.getClass(), "/Ranged_icon.png");
            this.magicIcon = ImageUtil.loadImageResource(this.getClass(), "/Magic_icon.png");

    }

    @Override
    public Dimension render(Graphics2D graphics) {

        client.getNpcs().forEach(npc -> {
            if(CombatStyleIconsPlugin.instance.checkContains(npc.getId(), config.magicSelections()))
            {
                net.runelite.api.Point base = npc.getCanvasImageLocation(this.magicIcon, (npc.getModelHeight() / 2));
                net.runelite.api.Point png = new net.runelite.api.Point(base.getX(), base.getY());
                OverlayUtil.renderImageLocation(graphics, png, this.magicIcon);

            }else if(CombatStyleIconsPlugin.instance.checkContains(npc.getId(), config.meleeSelections())) {
                net.runelite.api.Point base = npc.getCanvasImageLocation(this.meleeIcon, (npc.getModelHeight() / 2));
                net.runelite.api.Point png = new net.runelite.api.Point(base.getX(), base.getY());
                OverlayUtil.renderImageLocation(graphics, png, this.meleeIcon);
            }else if(CombatStyleIconsPlugin.instance.checkContains(npc.getId(), config.rangeSelections()))
            {
                net.runelite.api.Point base = npc.getCanvasImageLocation(this.rangeIcon, (npc.getModelHeight() / 2));
                net.runelite.api.Point png = new net.runelite.api.Point(base.getX(), base.getY());
                OverlayUtil.renderImageLocation(graphics, png, this.rangeIcon);
            }
        });



        return null;
    }
}
