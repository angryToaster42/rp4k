package com.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.Rectangle;

public class Cannon {
    Sprite sprite;
    int x, y, w, h;
    int counter = 0, delay;
    float angle;
    String type;

    //Animation Variables
    int rows, cols;
    Animation anim;
    TextureRegion[] frames;
    TextureRegion frame;
    TextureRegion last_frame;
    float frame_time = 0.2f;

    Cannon(String type, int x, int y) {
        sprite = new Sprite(Tables.cannon_resources.get(type) == null ? Resources.cannon : Tables.cannon_resources.get(type));
        this.type = type;
        rows = 1;
        cols = Tables.balance.get("cols_"+type) == null ? 1 : Tables.balance.get("cols_"+type);
        w = (Tables.cannon_resources.get(type) == null ? Resources.cannon : Tables.cannon_resources.get(type)).getWidth() / cols;
        h = (Tables.cannon_resources.get(type) == null ? Resources.cannon : Tables.cannon_resources.get(type)).getHeight() / rows;
        delay = Tables.balance.get("delay_"+type) == null ? 30 : Tables.balance.get("delay_"+type);
        this.x = gridlock(x - w / 2);
        this.y = gridlock(y - h / 2);
        angle = 0f;
        sprite.setPosition(this.x, this.y);
        init_animations();
    }

    void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    void update(){
        if(!type.equals("laser") && counter++ > delay) { if(!Main.zombies.isEmpty()) fire(); counter = 0; }
        if(type.equals("laser") && check_frame()) { if(!Main.zombies.isEmpty()) fire(); }
        frame_time += Gdx.graphics.getDeltaTime();
        frame = (TextureRegion) anim.getKeyFrame(frame_time, true);
        sprite = new Sprite(frame);
        sprite.setPosition(this.x, this.y);
        sprite.setRotation(calcAngle());
    }

    boolean check_frame(){
        return (last_frame == (TextureRegion)anim.getKeyFrame(frame_time, true));
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
        return (float)Math.toDegrees(Math.atan((y-zy)/(x-zx)) + (x > zx ? Math.PI : 0));
    }

    void fire() {
        Resources.sfx_bullet.play(0.2f);
        Main.bullets.add(new Bullet("missile", x + w / 2, y + h / 2));
    }

    int gridlock(int n){
        return ((int)((n + 25) / 50) * 50);
    }

    void init_animations(){
        // split texture into individual cells
        TextureRegion[][] sheet =
                TextureRegion.split((Tables.cannon_resources.get(type) == null ? Resources.cannon : Tables.cannon_resources.get(type)), w, h);
        // init numbers of frames of maximum number possible (all rows * all cols)
        frames = new TextureRegion[rows * cols];
        // loop through the texture sheet and fill frames array with cells (in order)
        int index = 0;
        for(int c = 0; c < cols; c++)
            frames[index++] = sheet[0][c];

        //init the animation object
        anim = new Animation(frame_time, frames);
        if(type.equals("laser")) last_frame = (TextureRegion)anim.getKeyFrames()[anim.getKeyFrames().length - 6];
    }


    Rectangle gethitbox() {
        return new Rectangle(x, y, w, h);
    }
}
