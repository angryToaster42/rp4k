package com.main;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UI {
    static int money = 1;
    static int wave = 0;
    static BitmapFont font = new BitmapFont();

    static void draw(SpriteBatch batch){
        font.setColor(Color.PINK);
        font.draw(batch, "money: " + money, 15, 585);
        font.setColor(Color.FIREBRICK);
        font.draw(batch, "wave: " + wave, 15, 565);
    }
}