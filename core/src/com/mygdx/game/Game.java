package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Iterator;
import java.util.Vector;
import java.util.Random;

public class Game extends ApplicationAdapter {
	enum State{
		MENU,
		START,
		PLAY,
		PAUSE,
		END
	}

	Assets assets;
	State state;
	private OrthographicCamera camera;
	private Stage stage;
	SpriteBatch batch;
	Texture img;
	TouchPad touchPad;

	InputController inputController;
	FireButton fireButton;
	Player player;
	InputMultiplexer multiplexer;
	float gameTime;
	int enemyCount;
	Random rnd;
	Vector<Enemy> enemyList;
	Vector<Bullet> bulletList;
	TileMap map;

	HealthBar healthBar;


	BitmapFont font;

	@Override
	public void create () {
		assets = new Assets();

		gameTime = 0;
		enemyCount = 0;

		bulletList = new Vector<Bullet>();
		enemyList = new Vector<Enemy>();

		camera = new OrthographicCamera();
		float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
		//camera.setToOrtho(false, 10f * aspectRatio, 10f);
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		map = new TileMap(assets, camera, 0);
		touchPad = new TouchPad();
		touchPad.create();
		player = new Player(assets);

		fireButton = new FireButton(assets);

		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		stage.addActor(touchPad.getTouchpad());

		inputController = new InputController();
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(inputController);
		//Gdx.input.setInputProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);

		rnd = new Random();

		healthBar = new HealthBar(assets, new Vector2(30, 400));

		//font = assets.manager.get(assets.fontFileName);
		state = State.PLAY;

	}

	@Override
	public void render () {
		//Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		switch (state) {
			case PLAY:
				camera.update();
				this.update();
				//System.out.println("State: " + player.state.toString() + " dir: " + player.direction.toString() + " lastDir: " + player.lastDirection.toString());
				stage.act(Gdx.graphics.getDeltaTime());

				batch.begin();
				//batch.draw(img, 0, 0);
				map.render(batch);
				touchPad.render();
				player.render(batch);
				renderEnemies();
				renderBullets();
				fireButton.render(batch);

				healthBar.render(batch);


				//font.draw(batch, "Ammo: " + String.valueOf(player.ammo), 100, 100);

				batch.end();
				stage.draw();
				break;

			case END:

				break;


		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public void update(){
		//System.out.println(touchPad.getDeltaDistance().x);
		//System.out.print(touchPad.getDeltaDistance().y);
		gameTime += Gdx.graphics.getDeltaTime();
		//System.out.println("gameTime ");
		//System.out.print(gameTime);
		//System.out.println(player.direction.toString());
		player.update(touchPad);
		//System.out.println(player.lastDirection.toString());
		fireButton.update();
		updateFireButtonState();

		//if((gameTime / 6) > enemyCount ){
		if(enemyCount < 1){
			enemyList.add(new Enemy(new Vector2(rnd.nextInt(Gdx.graphics.getWidth() + 1),rnd.nextInt(Gdx.graphics.getHeight() + 1))));
			System.out.println("SPAWN!NEW");
			enemyCount++;
		}

		if(fireButton.isPressed()) {
			System.out.println("NEW BULLET!@@@@@@@@@@@@@@@@@@@@@@@");
			if (player.isItemUsingAllowed() && player.ammo > 0) {
				player.setItemCooldown(player.ITEM_COOLDOWN);
				bulletList.add(new Bullet(player.rectangle, player.lastDirection, player.sprite.getRotation()));
			}
		}
		updateEnemies();

		if(player.health <= 0){
			state = State.END;
		}
		updateBullets();

		healthBar.update((int)(player.health / player.MAX_HEALTH * 100), new Vector2(30, 400));
	}

	public void updateFireButtonState(){

		if(inputController.isTouched && fireButton.sprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY())){
			fireButton.isPressed = true;
		}
		else fireButton.isPressed = false;
	}

	public void updateBullets(){
		for (Iterator<Bullet> it = bulletList.iterator(); it.hasNext();){
			Bullet bullet = it.next();
			bullet.update();
			if(bullet.livingTime > bullet.MAX_LIVING_TIME){
				it.remove();
			}

			Enemy intersected = bullet.intersects(enemyList);
			if (intersected != null){
				System.out.println("LOLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
				intersected.state = Enemy.State.DEAD;
				it.remove();
			}
		}
	}

	public void updateEnemies(){
		for (Iterator<Enemy> it = enemyList.iterator(); it.hasNext();){
			Enemy enemy = it.next();
			enemy.update(player);
			if(enemy.state == Enemy.State.DEAD && enemy.animation.isEnemyDeathAnimationFinished()){
				it.remove();
			}
		}
	}

	public void renderBullets(){
		for(Bullet bullet : bulletList){
			bullet.render(batch);
		}
	}

	public void renderEnemies(){
		for(Enemy enemy : enemyList){
			enemy.animation.play(enemy.state, enemy.direction);
			enemy.render(batch);
		}
	}
}