package com.iconify;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.*;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NpcOverlay extends Overlay {

    @Inject
    private Client client;

    @Inject
    private IconifyConfig config;

    private BufferedImage meleeIcon, rangeIcon, magicIcon;
    @Inject
    private NpcOverlay()
    {
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGH);

        try {
            this.meleeIcon = ImageIO.read(getClass().getResourceAsStream("/Attack_icon.png"));
            this.rangeIcon = ImageIO.read(getClass().getResourceAsStream("/Ranged_icon.png"));
            this.magicIcon = ImageIO.read(getClass().getResourceAsStream("/Magic_icon.png"));

        } catch (IOException e) {
            IconifyPlugin.instance.doLog(e.getMessage());
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        client.getNpcs().forEach(npc -> {
            if(IconifyPlugin.instance.checkContains(npc.getId(), config.magicSelections()))
            {
                net.runelite.api.Point base = npc.getCanvasImageLocation(this.magicIcon, (npc.getModelHeight() / 2));
                net.runelite.api.Point png = new net.runelite.api.Point(base.getX(), base.getY());
                OverlayUtil.renderImageLocation(graphics, png, this.magicIcon);

            }else if(IconifyPlugin.instance.checkContains(npc.getId(), config.meleeSelections())) {
                net.runelite.api.Point base = npc.getCanvasImageLocation(this.meleeIcon, (npc.getModelHeight() / 2));
                net.runelite.api.Point png = new net.runelite.api.Point(base.getX(), base.getY());
                OverlayUtil.renderImageLocation(graphics, png, this.meleeIcon);
            }else if(IconifyPlugin.instance.checkContains(npc.getId(), config.rangeSelections()))
            {
                net.runelite.api.Point base = npc.getCanvasImageLocation(this.rangeIcon, (npc.getModelHeight() / 2));
                net.runelite.api.Point png = new net.runelite.api.Point(base.getX(), base.getY());
                OverlayUtil.renderImageLocation(graphics, png, this.rangeIcon);
            }
        });



        return null;
    }
}
