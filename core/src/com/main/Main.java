package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Random;

public class Main extends ApplicationAdapter {
	// GAME VARIABLES
	SpriteBatch batch;
	Random r;
	String cannontype;

	// CONTROL VARIABLES

	// GAME LISTS
	static ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	static ArrayList<Cannon> cannon = new ArrayList<Cannon>();
	static ArrayList<Button> button = new ArrayList<Button>();
	static ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	@Override
	public void create () {
		batch = new SpriteBatch();
		r = new Random();
		setup();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		update();
		batch.begin();
		batch.draw(Resources.bg, 0, 0);

		// RENDER SPRITES
		for(Cannon z : cannon) z.draw(batch);
		for(Button z : button) z.draw(batch);
		for(Zombie z : zombies) z.draw(batch);
		for(Bullet b : bullets) b.draw(batch);

		UI.draw(batch);

		batch.end();
	}

	void update() {
		tap();
		spawn_zombies();

		// UPDATE SPRITES
		for(Zombie z : zombies) z.update();
		for(Cannon z : cannon) z.update();
		for(Button z : button) z.update();
		for(Bullet b : bullets) b.update();

		removesprite();
	}

	void tap() {
		if(Gdx.input.justTouched()) {
			int x = Gdx.input.getX(), y = Gdx.graphics.getHeight() - Gdx.input.getY();

			for(Button b : button) if (b.gethitbox().contains(x,y)) {
				if (b.locked) b.locked = false;
				else {
					deselect();
					cannontype = b.type;
					b.selected = true;
				}
				return;
			}

			for(Cannon c : cannon) if(c.gethitbox().contains(x, y)) return;
			if(buildable(x, y)) if(UI.money >= Tables.balance.get("cost_"+cannontype)) {
				UI.money -= Tables.balance.get("cost_"+cannontype);
				cannon.add(new Cannon(cannontype, x, y));
			}
		}
	}

	void deselect(){
		for(Button b : button) b.selected = false;
	}

	boolean buildable(int x, int y){
		return (y < 1000 && ((y < 200 || y > 300) && y < 500));
	}

	void setup() {
		//spawn zombies
		Tables.init();
		spawn_zombies();

		//draw buttons
			button.add(new Button("cannon", 200 + button.size() * 75, 525));
			button.add(new Button("double", 200 + button.size() * 75, 525));
			button.add(new Button("super", 200 + button.size() * 75, 525));
			button.add(new Button("fire", 200 + button.size() * 75, 525));
			button.add(new Button("laser", 200 + button.size() * 75, 525));
	}

	void removesprite() {
		for(Zombie z : zombies) if (!z.active) {zombies.remove(z); break;}
		for(Bullet b : bullets) if (!b.active) {bullets.remove(b); break;}
	}

	void spawn_zombies() {
		if(!zombies.isEmpty()) return;
		UI.wave++;
		//for(int i = 0; i < 5 * UI.wave; i++){ }

		for(int i = 0; i < 1; i++) {
			zombies.add(new Zombie("riot", i * 20 + 1024, r.nextInt(450)));
		}
	}

	// dont write anything bellow
	@Override
	public void dispose () {
		batch.dispose();
	}
}
