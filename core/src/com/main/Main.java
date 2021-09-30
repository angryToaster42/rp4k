package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Random;

public class Main extends ApplicationAdapter {
	// GAME VARIABLES
	SpriteBatch batch;
	Random r;

	// CONTROL VARIABLES

	// GAME LISTS
	ArrayList<Zombie> zombies = new ArrayList<Zombie>();

	@Override
	public void create () {
		batch = new SpriteBatch();
		r = new Random();

		for(int i = 0; i < 1000; i++) {
			zombies.add(new Zombie("zzz", i * 20 + 526, r.nextInt(450), 1));
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		//update();
		batch.begin();
		batch.draw(Resources.bg, 0, 0);
		for(Zombie z : zombies) z.draw(batch);
		for(Zombie z : zombies) z.update();
		batch.end();
	}

	//void update() {
		//zombie.update();
	//}

	// dont write anything bellow
	@Override
	public void dispose () {
		batch.dispose();
	}
}
