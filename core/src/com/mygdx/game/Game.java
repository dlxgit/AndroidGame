package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Game extends ApplicationAdapter {
	private OrthographicCamera camera;
	private Stage stage;
	SpriteBatch batch;
	Texture img;
	TouchPad touchPad;

	InputController inputController;

	FireButton fireButton;
	Player player;

	Vector<Shot> shotList;

	InputMultiplexer multiplexer;


	@Override
	public void create () {
		shotList = new Vector<Shot>();

		camera = new OrthographicCamera();
		float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
		//camera.setToOrtho(false, 10f * aspectRatio, 10f);
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		touchPad = new TouchPad();
		touchPad.create();
		player = new Player();
		player.create();

		fireButton = new FireButton();

		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		stage.addActor(touchPad.getTouchpad());
		stage.addActor(player);
		stage.addActor(fireButton);

		inputController = new InputController();
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(inputController);
		//Gdx.input.setInputProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render () {
		//Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		this.update();
		//batch.begin();
		//batch.draw(img, 0, 0);
		//touchPad.render();

		stage.act(Gdx.graphics.getDeltaTime());

		//player.render(batch);
		//renderBullets();
		//fireButton.render(batch);
		//batch.end();
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

		player.update(touchPad);
		fireButton.update();
		updateFireButtonState();

		if(fireButton.isPressed()){
			System.out.println("NEW BULLET!@@@@@@@@@@@@@@@@@@@@@@@");
			shotList.add(new Shot(new Vector2(player.sprite.getX(), player.sprite.getY()), player.sprite.getRotation()));
		}
		updateBullets();
	}

	public void updateFireButtonState(){

		if(inputController.isTouched && fireButton.sprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY())){
			fireButton.isPressed = true;
		}
		else fireButton.isPressed = false;
	}


	public void updateBullets(){
		for (Iterator<Shot> it = shotList.iterator(); it.hasNext();){
			Shot shot = it.next();
			shot.update();
			if(shot.livingTime > shot.maxLivingTime){
				it.remove();
			}
		}
	}

	public void renderBullets(){
		for(Shot shot : shotList){
			shot.sprite.draw(batch);
		}
	}
}
