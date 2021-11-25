package com.main;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;

import java.awt.Rectangle;
import java.security.cert.CertPathValidatorException;

public class Tooltip {
    int x, y, w, h;
    String type;
    boolean hidden = true;
    Button close;
    BitmapFont font = new BitmapFont();
    GlyphLayout layout = new GlyphLayout();

    Tooltip(String type, Button parent){
        this.type = type;
        w = 200;
        h = 100;
        x = (parent.x + parent.w / 2) - w / 2;
        y = parent.y - h - 10;
        close = new Button("close", x + w - Resources.button_close.getWidth() - 1, y + h - Resources.button_close.getHeight() - 1);
        close.locked = false;
    }

    void draw(SpriteBatch batch){
        if(hidden) return;
        batch.draw(Resources.tooltip_bg, x, y, w, h);
        close.draw(batch);

        String[] words = (Tables.tooltips.get(type) == null ? "no info available" : Tables.tooltips.get(type)).split(" ");
        int rx = 5, ry = 5; //relative pos of the text to the position of the tooltip
        for(String s : words){
            if (rx + layout.width >= w - 35){
                rx = 5;
                ry += layout.height + 5;
            }
            font.setColor(Color.FIREBRICK);
            font.draw(batch, s, x + rx, y + h - ry);
            layout.setText(font, " " + s);
            rx += layout.width;
        }

        font.getData().setScale(1.5f);
        font.setColor(Color.GOLD);
        font.draw(batch, "Unlock: " + (Tables.balance.get("unlock_"+type) == null ? 0 : Tables.balance.get("unlock_"+type)), x + 35, y + 30);
        font.getData().setScale(1.0f);
    }

    Rectangle gethitbox() { return new Rectangle(x, y, w, h); }
}
