package com.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Cannon {
    Sprite sprite;
    int x, y, w, h, hp, mhp;
    int counter = 0, delay;
    String type;
    boolean active = true;

    //Animation Variables
    int rows, cols;
    Animation anim;
    TextureRegion[] frames;
    TextureRegion frame;
    TextureRegion last_frame;
    float frame_time = 0.2f;

    Cannon(String type, int x, int y){
        this.type = type;
        sprite = new Sprite(Tables.cannon_resources.get(type) == null ? Resources.cannon : Tables.cannon_resources.get(type));
        rows = 1;
        cols = Tables.balance.get("cols_"+type) == null ? 1 : Tables.balance.get("cols_"+type);
        w = (Tables.cannon_resources.get(type) == null ? Resources.cannon : Tables.cannon_resources.get(type)).getWidth() / cols;
        h = (Tables.cannon_resources.get(type) == null ? Resources.cannon : Tables.cannon_resources.get(type)).getHeight() / rows;
        hp = Tables.balance.get("hp_"+type) == null ? 100 : Tables.balance.get("hp_"+type);
        mhp = hp;
        delay = Tables.balance.get("delay_"+type) == null ? 30 : Tables.balance.get("delay_"+type);
        this.x = gridlock(x - w / 2);
        this.y = gridlock(y - h / 2);
        init_animations();
        frame = (TextureRegion)anim.getKeyFrame(frame_time, true);
        sprite = new Sprite(frame);
        sprite.setPosition(this.x, this.y);
    }

    void draw(SpriteBatch batch){
        sprite.draw(batch);
        batch.draw(Resources.red_bar, x, y - 5, w, 5);
        batch.draw(Resources.green_bar, x, y - 5, hp * ((float)w / (float)mhp), 5);
    }

    void update(){
        if(!type.equals("laser") && counter++ > delay) { if(!Main.zombies.isEmpty()) fire(); counter = 0; }
        if(type.equals("laser") && check_frame()) if(!Main.zombies.isEmpty()) fire();
        frame_time += Gdx.graphics.getDeltaTime();
        frame = (TextureRegion)anim.getKeyFrame(frame_time, true);
        sprite = new Sprite(frame);
        sprite.setPosition(this.x, this.y);
        sprite.setRotation(calc_angle());
        active = hp-- > 0;
    }

    boolean check_frame(){
        return (last_frame == (TextureRegion)anim.getKeyFrame(frame_time, true));
    }

    float calc_angle(){
        Zombie closest = null;
        for(Zombie z : Main.zombies){
            if(closest == null) { closest = z; continue; }
            float hyp_closest = (float)Math.sqrt(((x - closest.x) * (x - closest.x)) + ((y - closest.y) * (y - closest.y)));
            float hyp_z = (float)Math.sqrt(((x - z.x) * (x - z.x)) + ((y - z.y) * (y - z.y)));
            if(hyp_z < hyp_closest) closest = z;
        }
        float zx = closest.x + (float)closest.w / 2, zy = closest.y + (float)closest.h / 2;
        return (float)Math.toDegrees(Math.atan((y - zy)/(x - zx)) + (x >= zx ? Math.PI : 0));
    }

    void fire(){
        if(type.equals("double")){
            Resources.sfx_bullet.play(0.2f);
            Main.bullets.add(new Bullet(type, x + w / 2, y + h / 4));
            Resources.sfx_bullet.play(0.2f);
            Main.bullets.add(new Bullet(type, x + w / 2, y + (h / 4) * 3));
            return;
        }
        Resources.sfx_bullet.play(0.2f);
        Main.bullets.add(new Bullet(type, x + w / 2, y + h / 2));
    }

    int gridlock(int n){
        return ((int)((n + 25) / 50) * 50);
    }

    void init_animations(){
        // split texture into individual cells
        TextureRegion[][] sheet =
                TextureRegion.split((Tables.cannon_resources.get(type) == null ? Resources.cannon : Tables.cannon_resources.get(type)), w, h);
        // init numbers of frames to maximum number possible (all rows * all cols)
        frames = new TextureRegion[rows * cols];
        //loop through the texture sheet and fill frames array with cells (in order)
        int index = 0;
        for(int r = 0; r < rows; r++)
            for(int c = 0; c < cols; c++)
                frames[index++] = sheet[r][c];
        //initialize the animation object
        anim = new Animation(frame_time, frames);
        if(type.equals("laser")) last_frame = (TextureRegion)anim.getKeyFrames()[anim.getKeyFrames().length - 6];
    }

    Rectangle gethitbox(){ return new Rectangle(x, y, w, h); }
}