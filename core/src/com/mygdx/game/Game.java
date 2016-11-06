package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Iterator;
import java.util.Vector;
import java.util.Random;

//почему нельзя писать assets.manager.get() возвращающий текстуру, прямо в вызове функции?? вместо того чтобы перед этим инициализировать текстуру таким же образом и передавать в функцию готовую текстуру

/*
Bugs:
	exploding enemy can attack;

 */

/*
To do:
	bullet start position depending on direction (correctly);
	try to integrate with tiled;

	fix enemy state-calculating methods
 */

/*
	в анимациях убрать поле currentFrame и просто возвращать TextureRegion из функции прямо в метод отрисовки.
	но может быть проблема с вычислением состояний когда они зависят от isAnimationFinished(); так что можно оставить как есть?
 */

//сделать при спасении NPC принтовать очки на экране за спасение (как дамаг в вов)
//в конце уровня подсчет очков прямо как в оригинале


/*
	сделать static анимации, храня свойства обьектов для визуального отображения внутри других(самих) классов
 */

/*
	сделать класс анимации NPC таким: передавать в него параметр тип NPC и хранить для каждого лишь одну анимацию(а не все сразу) пример: сейчас у каждого NPC есть анимация, в которой все анимации всех NPC.
	либо сделать чисто один статичный класс анимации для каждого из типов сущностей (пуля, игрок, враг(каждый?)).

 */


/*
	fix user interface (game window)
 */

/*
	записать все константы в класс
	адаптировать код связанный с врагами к разным типам врагов
	ввести топоры от китайцев + гранаты как один класс,полоску загрузки ресурсов
 */

/*
	добавить миникарту
 */

public class Game extends ApplicationAdapter {
	enum State{
		MENU,
		START,
		PLAY,
		PAUSE,
		END
	}


	int survivedNpcs;





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
	Vector<Npc> npcList;
	Vector<Loot> lootList;
	Inventory inventory;



	TileMap map;

	HealthBar healthBar;


	BitmapFont font;

	@Override
	public void create () {


		survivedNpcs = 0;



		assets = new Assets();

		gameTime = 0;
		enemyCount = 0;





		bulletList = new Vector<Bullet>();
		enemyList = new Vector<Enemy>();
		lootList = new Vector<Loot>();
		inventory = new Inventory();

		Texture npcTexture = assets.manager.get(assets.npcTextureName);
		npcList = new Vector<Npc>();
		//npcList = initializeNpc(npcTexture);


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
		lootList.add(new Loot(assets, Loot.Type.SMG, new Rectangle(200,200, 16,16)));
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
				System.out.println("PlayerState: " + player.state.toString());
				System.out.println("Survived: " + String.valueOf(survivedNpcs));

				stage.act(Gdx.graphics.getDeltaTime());

				batch.begin();
				//batch.draw(img, 0, 0);
				map.render(batch);
				touchPad.render();
				player.render(batch);
				renderEnemies();
				renderBullets();
				renderNpcs();
				renderLoot();

				fireButton.render(batch);

				healthBar.render(batch);


				//font.draw(batch, "Ammo: " + String.valueOf(player.ammo), 100, 100);

				batch.end();
				stage.draw();

				System.out.println("____________");
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
		gameTime += Gdx.graphics.getDeltaTime();
		//System.out.println("gameTime " + String.valueOf(gameTime));

		player.update(touchPad);
		//System.out.println(player.lastDirection.toString());
		fireButton.update();
		updateFireButtonState();

		//if((gameTime / 6) > enemyCount ){
		if(enemyCount < 1){
			//enemyList.add(new Enemy(new Vector2(rnd.nextInt(Gdx.graphics.getWidth() + 1),rnd.nextInt(Gdx.graphics.getHeight() + 1))));
			System.out.println("Enemy spawning");
			enemyCount++;
		}

		if(fireButton.isPressed() && (player.state == Player.State.MOVE || player.state == Player.State.STAY)) {
			System.out.println("Trying use item_ ");
			if(inventory.useItem()){
					switch (inventory.getCurrentSlot()){
						case 0://smg shot
							System.out.println("SHOT");
							bulletList.add(new Bullet(player.rectangle, player.lastDirection));
							break;
						case 1://grenade throw
							System.out.println("GRENADE");
							break;
						case 2://fire-extinguisher use
							System.out.println("FIRE_EXT");
							break;
						case 3://medicine use
							System.out.println("MEDICINE");
							break;
						default:
							break;
					}
			}
			/*
			if (player.isItemUsingAllowed() && player.ammo > 0) {
				player.setItemCooldown(player.ITEM_COOLDOWN);
				bulletList.add(new Bullet(player.rectangle, player.lastDirection, player.sprite.getRotation()));
			}
			*/
		}

		updateBullets();
		updateEnemies();
		updateNpcs();
		updateLoot();
		inventory.update();

		if(player.health <= 0){
			state = State.END;
		}

		healthBar.update((int)(player.health / player.MAX_HEALTH * 100), new Vector2(30, 400));
	}

	public void updateFireButtonState(){

		if(inputController.isTouched && fireButton.sprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY())){
			fireButton.isPressed = true;
		}
		else fireButton.isPressed = false;
	}

	public void updateBullets() {
		for (Iterator<Bullet> it = bulletList.iterator(); it.hasNext(); ) {
			Bullet bullet = it.next();
			bullet.update();
			if (bullet.livingTime > bullet.MAX_LIVING_TIME) {
				it.remove();
				continue;
			}

			for (Iterator<Enemy> itEnemy = enemyList.iterator(); itEnemy.hasNext(); ) {
				Enemy enemy = itEnemy.next();
				if (bullet.rectangle.overlaps(enemy.rectangle)) {
					System.out.println("bullet intersects with enemy");
					enemy.health -= player.BULLET_DAMAGE;
					it.remove();
					break;
				}
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

	public void updateNpcs() {
		for (Iterator<Npc> it = npcList.iterator(); it.hasNext(); ) {
			Npc npc = it.next();
			npc.update();

			if(npc.isLiving() == false){
				System.out.println("Npc Death stateTime: " + String.valueOf(npc.animation.stateTime));
				if(npc.animation.isDeathAnimationFinished()) {
					it.remove();
				}
				continue;
			}
			else if (npc.rectangle.overlaps(player.rectangle)){//NPC survive
				survivedNpcs++;
				it.remove();
				continue;
			}


			for(Iterator<Enemy> itEnemy = enemyList.iterator(); itEnemy.hasNext(); ) {
				Enemy enemy = itEnemy.next();
				if (npc.rectangle.overlaps(enemy.rectangle)) {
					System.out.println("NPC_TOUCH_ENEMY!!");
					npc.die();
				}
			}
		}
	}

	public void updateLoot(){
		for(Iterator<Loot> it = lootList.iterator(); it.hasNext();){
			Loot loot = it.next();
			if(loot.rectangle.overlaps(player.rectangle)){
				//itemPickUp
				if(loot.type == Loot.Type.SPEED_BONUS) {
					System.out.println("Taking speed_bonus");
				}else{
					inventory.takeItem(loot.type.ordinal());
				}
				it.remove();
			}

		}
	}



	public void renderShape(Rectangle rect){
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		shapeRenderer.end();
	}

	public void renderBullets(){
		for(Bullet bullet : bulletList){
			bullet.render(batch);
			//renderShape(bullet.rectangle);
		}
	}

	public void renderEnemies(){
		for(Enemy enemy : enemyList){
			enemy.animation.play(enemy.state, enemy.direction);
			enemy.render(batch);
			//renderShape(enemy.rectangle);
		}
	}

	public void renderNpcs(){
		for(Npc npc : npcList){
			//npc.animation.update(npc.state);
			npc.render(batch);
			//renderShape(npc.rectangle);
		}
	}

	public void renderLoot(){
		for(Loot loot: lootList){
			loot.render(batch);
		}
	}


	public Vector<Npc> initializeNpc(Texture texture){
		Vector<Npc> npcList = new Vector<Npc>();
		npcList.add(new Npc(texture, Npc.Type.PHOTOGRAPHS, 5 * 48, 8 * 48));
		npcList.add(new Npc(texture, Npc.Type.BABY,  50 * 48,  10 * 48));

		npcList.add(new Npc(texture, Npc.Type.TEACHER, 9 * 48, 15 * 48));
		npcList.add(new Npc(texture, Npc.Type.DOG, 53 * 48, 15 * 48));
		npcList.add(new Npc(texture, Npc.Type.SOLDIER, 28 * 48, 28 * 48));
		npcList.add(new Npc(texture, Npc.Type.SEARCHER, 55 * 48, 4 * 48));
		npcList.add(new Npc(texture, Npc.Type.COOK, 20 * 48, 14 * 48));
		npcList.add(new Npc(texture, Npc.Type.GIRL, 15 * 48, 6 * 48));
		return npcList;
	}

	//creates random loot_item at random position on ammo
	public Loot createLootItem(){
		Random rand = new Random();

		final float STEP_TILE = 64.f;
		final Vector2 mapSize = new Vector2(640,480);
		final Vector2 LOOT_IMAGE_SIZE = new Vector2(16, 16);


		while (true) {
			Rectangle lootRect = new Rectangle((rand.nextInt((int)mapSize.x)) * STEP_TILE,
											   (rand.nextInt((int)mapSize.y)) * STEP_TILE,
												LOOT_IMAGE_SIZE.x,
												LOOT_IMAGE_SIZE.y);

			boolean isPositionFree = true;
			for (int i = 0; i < map.solidObjects.size() && isPositionFree == false; ++i) {
				/*TODO: найти способ взять ректангл у тайла if (lootRect.overlaps(ammo.solidObjects[i])) {
					isPositionFree = false;
				}
				*/
			}
			if(isPositionFree){
				return (new Loot(assets, Loot.Type.values()[rand.nextInt(Npc.Type.values().length)], lootRect));
			}
		}
	}
}