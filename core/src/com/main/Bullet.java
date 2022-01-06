package com.main;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Rectangle;

public class Bullet {
    int x, y, w, h;
    int speed, dt, md; // dt = dist travelled  md = max dist
    float angle;
    String type;
    boolean active = true;
    Sprite sprite;

    Bullet(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        sprite = new Sprite(Tables.resources.get("bullet_" + type) == null ? Resources.bullet : Tables.resources.get("bullet_"+type));
        w = Tables.bullet_resources.get(type) == null ? Resources.bullet.getWidth() : Tables.bullet_resources.get(type).getWidth();
        h = Tables.bullet_resources.get(type) == null ? Resources.bullet.getHeight() : Tables.bullet_resources.get(type).getHeight();
        angle = calcAngle();
        speed = 15;
        dt = 0;
        md = 300;
        sprite.setPosition(x, y);
        sprite.setRotation((float)Math.toDegrees(calcAngle()) - 90f);
    }

    void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    void update(){
        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;
        sprite.setPosition(x, y);
        dt += Math.cos(angle) * speed + Math.sin(angle) * speed;
        active = dt < md;
        hitzombie();
    }

    Rectangle gethitbox() {
        return new Rectangle(x, y, w, h);
    }

    float calcAngle() {
        Zombie closest = null;
        for(Zombie z : Main.zombies){
            if(closest == null) { closest = z; continue; }
            if(Math.sqrt((x - z.x) * (x - z.x) + (y - z.y) - (y - z.y)) < Math.sqrt((x - closest.x) * (x - closest.x) + (y - closest.y) * (y - closest.y))) {
                closest = z;
            }
        }
        float zx = Main.zombies.get(0).x + (float)closest.w / 2, zy = closest.y + (float)closest.h / 2;
        return (float)(Math.atan((y-zy)/(x-zx)) + (x > zx ? Math.PI : 0));
    }

    void hitzombie() {
        if (Main.zombies.isEmpty()) return;
        for(Zombie z : Main.zombies) {
            if(z.gethitbox().contains(gethitbox())) {
                z.hp--;
                this.active = false;
            }
        }
    }
}
