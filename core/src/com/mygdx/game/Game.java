package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
	}

	@Override
	public void render () {
		//Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		this.update();

		batch.begin();
		//batch.draw(img, 0, 0);
		touchPad.render();

		stage.act(Gdx.graphics.getDeltaTime());
		player.render(batch);
		//renderBullets();
		//renderEnemies();
		//fireButton.render(batch);
		batch.end();
		stage.draw();
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
		System.out.println(gameTime);
		System.out.println(player.direction.toString());
		player.update(touchPad);
		fireButton.update();
		updateFireButtonState();


		if((gameTime / 6) > enemyCount ){

			//enemyList.add(new Enemy(new Vector2(rnd.nextInt(Gdx.graphics.getWidth() + 1),rnd.nextInt(Gdx.graphics.getHeight() + 1)), enemyCount));
			//enemyCount++;

		}

		if(fireButton.isPressed()){
			System.out.println("NEW BULLET!@@@@@@@@@@@@@@@@@@@@@@@");
			bulletList.add(new Bullet(new Vector2(player.sprite.getX(), player.sprite.getY()), player.sprite.getRotation()));

		}
		updateBullets();
		updateEnemies();
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
			if(bullet.livingTime > bullet.MAX_LIVING_TIME || bullet.intersects(enemyList)){
				it.remove();
			}
		}
	}

	public void updateEnemies(){
		for (Iterator<Enemy> it = enemyList.iterator(); it.hasNext();){
			Enemy enemy = it.next();
			enemy.update();
			if(enemy.state == Enemy.State.DAMAGED){
				it.remove();
			}
		}
	}


	public void renderBullets(){
		for(Bullet bullet : bulletList){
			bullet.sprite.draw(batch);
		}
	}

	public void renderEnemies(){
		for(Enemy enemy : enemyList){
			enemy.sprite.draw(batch);
		}
	}
}