package com.main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Rectangle;

public class Bullet {
    int x, y, w, h;
    int speed, dt, md; // dt = dist travelled  md = max dist
    float angle;
    String type;
    boolean active = true;

    Bullet(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        w = Tables.bullet_resources.get(type) == null ? Resources.bullet.getWidth() : Tables.bullet_resources.get(type).getWidth();
        h = Tables.bullet_resources.get(type) == null ? Resources.bullet.getHeight() : Tables.bullet_resources.get(type).getHeight();
        angle = 0f;
        speed = 15;
        dt = 0;
        md = 300;
        calcAngle();
    }

    void draw(SpriteBatch batch) {

        batch.draw(Tables.bullet_resources.get(type) == null ? Resources.bullet : Tables.bullet_resources.get(type), x, y);
    }

    void update(){
        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;
        dt += Math.cos(angle) * speed + Math.sin(angle) * speed;
        active = dt < md;
        hitzombie();
    }

    Rectangle gethitbox() {
        return new Rectangle(x, y, w, h);
    }

    void calcAngle() {
        if(Main.zombies.isEmpty()) return;
        float zx = Main.zombies.get(0).x + Main.zombies.get(0).w, zy = Main.zombies.get(0).y + Main.zombies.get(0).h / 2;
        angle = (float) Math.atan((y - zy)/(x - zx));
        if(x >= zx) angle += Math.PI;
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
