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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
	enum State {
		MENU,
		START,
		PLAY,
		PAUSE,
		END
	}

	static final Vector2 MAP_SIZE = new Vector2(3200, 3200);
	final Vector2 VIEWPORT_SIZE = new Vector2(1280, 1024);
	float aspectRatio;
	Vector2 GAMEWORLD_SIZE;

	int survivedNpcs;


	//time need to pass after using smg - to run after using it
	static final float SMG_USING_TIME = 0.5f;
	static final float GRENADE_THROW_ACTION_TIME = 0.5f;

	Assets assets;
	State state;
	private OrthographicCamera camera;
	private Stage stage;
	SpriteBatch batch;
	Texture img;
	TouchPad touchPad;

	InputController inputController;
	PressButton fireButton;
	PressButton changeSlotButton;
	PressButton escapeButton;

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

	ShapeRenderer shapeRenderer;

	Viewport viewport;

	@Override
	public void create() {
		aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
		GAMEWORLD_SIZE = new Vector2(aspectRatio * 3200, 3200);

		camera = new OrthographicCamera(GAMEWORLD_SIZE.x, GAMEWORLD_SIZE.y);
		camera.position.set(GAMEWORLD_SIZE.x / 2 - 1700, GAMEWORLD_SIZE.y / 2 + 1080, 0);
		//viewport = new FitViewport(1920, 1080, camera);
		//viewport = new FitViewport(1280, 1024, camera);
		viewport = new FitViewport(1920, 1080, camera);
		viewport.apply();
		//viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//camera = new OrthographicCamera();

		//camera.setToOrtho(false, 10f * aspectRatio, 10f);
		//camera.setToOrtho(false, 3200 * aspectRatio, 3200);
		//camera.setToOrtho(true, Gdx.graphics.getWidth(),0);
		//camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		//stage = new Stage(new StretchViewport(3200, 3200));
		stage = new Stage(new StretchViewport(1920, 1024));
		touchPad = new TouchPad();
		touchPad.create();
		stage.addActor(touchPad.getTouchpad());

		inputController = new InputController();
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(inputController);
		//Gdx.input.setInputProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);


		survivedNpcs = 0;
		shapeRenderer = new ShapeRenderer();

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


		batch = new SpriteBatch();
		map = new TileMap(assets, camera, 0);

		player = new Player(assets);

		fireButton = new PressButton(assets, PressButton.Type.FIRE);
		changeSlotButton = new PressButton(assets, PressButton.Type.CHANGE_SLOT);

		//stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		//Gdx.input.setInputProcessor();
		rnd = new Random();

		healthBar = new HealthBar(assets, new Vector2(30, 400));

		//lootList.add(new Loot(assets, Loot.Type.SMG, new Rectangle(200,200, 16,16)));
		//lootList.add(new Loot(assets, Loot.Type.FIRE_EXTINGUISHER, new Rectangle(200,200, 16,16)));
		lootList.add(new Loot(assets, Loot.Type.GRENADE, new Rectangle(200, 200, 16, 16)));
		state = State.PLAY;

		font = assets.manager.get("font/testFont.fnt", BitmapFont.class);
	}

	@Override
	public void render() {
		//Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		switch (state) {
			case PLAY:
				camera.update();
				viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				//camera.zoom += 0.01;
				batch.setProjectionMatrix(camera.combined);
				this.update();
				map.update(camera);
				//System.out.println("State: " + player.state.toString() + " dir: " + player.direction.toString() + " lastDir: " + player.lastDirection.toString());
				System.out.println("PlayerState: " + player.state.toString());
				System.out.println("Survived Npcs: " + String.valueOf(survivedNpcs));
				System.out.println("Inventory index: " + String.valueOf(inventory.getCurrentSlot()));
				System.out.println("AMMO: " + String.valueOf(inventory.ammo[inventory.getCurrentSlot()]));
				stage.act(Gdx.graphics.getDeltaTime());
				map.render(batch);

				batch.begin();
				//map.render(batch);
				//batch.draw(img, 0, 0);

				touchPad.render();
				player.render(batch);
				renderEnemies();
				renderBullets();
				renderNpcs();
				renderLoot();

				fireButton.render(batch);
				changeSlotButton.render(batch);

				healthBar.render(batch);
				renderUI(batch);


				if (player.state == Player.State.EXTINGUISH) {
					TextureRegion region = player.animation.getExtinguisherAnimation(player.stateTime);
					Vector2 pos = getItemPosition(player.direction, player.rectangle, region);
					batch.draw(region, pos.x, pos.y);
				}

				renderShape(player.rectangle);
				batch.end();
				stage.draw();

				System.out.println("____________");
				break;

			case END:

				break;

		}
	}

	@Override
	public void resize(int width, int height) {
		//viewport.update(width, height);
		//camera.position.set(GAMEWORLD_SIZE.x / 2, GAMEWORLD_SIZE.y / 2, 0);
	}

	Vector2 calculateGrenadePosition(float time, Rectangle grenadeRect, Direction direction) {
		Vector2 pos = new Vector2();

		return pos;
	}

	Vector2 getItemPosition(Direction dir, Rectangle playerRect, TextureRegion itemRegion) {
		switch (dir) {
			case UP:
				return new Vector2(playerRect.x + playerRect.width / 2, playerRect.y + itemRegion.getRegionHeight());
			case DOWN:
				return new Vector2(playerRect.x + playerRect.width / 2, playerRect.y + playerRect.height);
			case LEFT:
			case UPLEFT:
			case DOWNLEFT:
				return new Vector2(playerRect.x - itemRegion.getRegionWidth(), playerRect.y + playerRect.height / 2 - itemRegion.getRegionHeight() / 2);
			case RIGHT:
			case UPRIGHT:
			case DOWNRIGHT:
				return new Vector2(playerRect.x + playerRect.width, playerRect.y - playerRect.height / 2 + itemRegion.getRegionHeight() / 2);

		}
		return new Vector2(playerRect.x + playerRect.width / 2, playerRect.y + playerRect.height);
	}


	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}

	public void update() {
		gameTime += Gdx.graphics.getDeltaTime();
		//System.out.println("gameTime " + String.valueOf(gameTime));

		player.update(touchPad);
		//System.out.println(player.lastDirection.toString());
		fireButton.update();
		updateButtonState(fireButton);
		changeSlotButton.update();
		updateButtonState(changeSlotButton);

		//if((gameTime / 6) > enemyCount ){
		if (enemyCount < 1) {
			//TODO: spawn axe enemies only near player (and simple zombies too?)
			enemyList.add(new AxeEnemy(new Vector2(rnd.nextInt(Gdx.graphics.getWidth() + 1), rnd.nextInt(Gdx.graphics.getHeight() + 1)), assets));
			System.out.println("Enemy spawning");
			enemyCount++;
		}

		//TODO: make button on screen to control inventory
		if (changeSlotButton.isPressed()) {
			inventory.changeItem();
		} else {
			inventory.isChangeAllowed = true;
		}

		if (fireButton.isPressed() && (player.state == Player.State.MOVE || player.state == Player.State.STAY)) {
			System.out.println("Trying use item_ ");
			if (inventory.useItem()) {
				switch (inventory.getCurrentSlot()) {
					case 0://smg shot
						System.out.println("SMG SHOT");
						bulletList.add(new Bullet(assets, player.rectangle, player.lastDirection));
						player.state = Player.State.SHOOT;
						player.actionTimeRemaining = SMG_USING_TIME;
						break;
					case 1://grenade throw
						System.out.println("GRENADE");
						player.state = Player.State.THROW_GRENADE;
						bulletList.add(new Grenade(assets, player.rectangle, player.lastDirection));
						player.actionTimeRemaining = GRENADE_THROW_ACTION_TIME;
						break;
					case 2://fire-extinguisher use
						System.out.println("FIRE_EXT");
						player.state = Player.State.EXTINGUISH;
						player.actionTimeRemaining = SMG_USING_TIME;
						break;
					case 3://medicine use
						System.out.println("MEDICINE");
						//TODO: use medicine
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

		if (player.health <= 0) {
			state = State.END;
		}

		healthBar.update((int) (player.health / player.MAX_HEALTH * 100), new Vector2(30, 400));
	}

	public void updateButtonState(PressButton button) {

		if (inputController.isTouched && button.sprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
			button.isPressed = true;
		} else button.isPressed = false;
	}

	public void updateBullets() {
		for (Iterator<Bullet> it = bulletList.iterator(); it.hasNext(); ) {
			Bullet bullet = it.next();
			bullet.update();
			//System.out.println("Time: " + String.valueOf(bullet.livingTime) + " " + String.valueOf(bullet.DEATH_TIME));
			if (bullet.isDead()) {
				bullet.livingTime += 0.0001;
			}


			if (bullet.getTarget() == Bullet.Target.PLAYER && bullet.isCollisionWithTarget(player.rectangle)) {
				player.getDamage(bullet.attackDamage);
				it.remove();
				continue;
			}

			//checking collision with map

			if (bullet.isCollidable) {
				if (isCollisionWithMap(bullet.rectangle)) {
					bullet.die();
				}
				continue;
			} else if (bullet.isExploded()) {//if bullet finished its deathanimation(bullet explosion/grenade explosion)
				it.remove();
				continue;
			}

			//TODO:if(bullet.needToCalculateIntersectWithEnemies()) {

			//checking collision with enemies
			for (Iterator<Enemy> itEnemy = enemyList.iterator(); itEnemy.hasNext(); ) {
				Enemy enemy = itEnemy.next();
				//TODO:if(bullet.intersects(enemy.rectangle))

				if (bullet.isCollisionWithTarget(enemy.rectangle)) {
					System.out.println("bullet intersects with enemy");
					enemy.health -= bullet.attackDamage;
					if (bullet.isCollidable) {
						it.remove();
						break;
					}
				}
			}
			//}
		}
	}

	public void updateEnemies() {
		for (Iterator<Enemy> it = enemyList.iterator(); it.hasNext(); ) {
			Enemy enemy = it.next();
			enemy.update(player);

			if (enemy.isAction()) {
				enemy.setActionCooldown();
				bulletList.add(new Axe(assets, player.rectangle, enemy.rectangle, enemy.direction));
			}

			if (enemy.state == Enemy.State.DEAD && enemy.animation.isEnemyDeathAnimationFinished()) {
				it.remove();
			}
		}
	}

	public void updateNpcs() {
		for (Iterator<Npc> it = npcList.iterator(); it.hasNext(); ) {
			Npc npc = it.next();
			npc.update();

			if (npc.isLiving() == false) {
				System.out.println("Npc Death stateTime: " + String.valueOf(npc.animation.stateTime));
				if (npc.animation.isDeathAnimationFinished()) {
					it.remove();
				}
				continue;
			} else if (npc.rectangle.overlaps(player.rectangle)) {//NPC survive
				survivedNpcs++;
				it.remove();
				continue;
			}


			for (Iterator<Enemy> itEnemy = enemyList.iterator(); itEnemy.hasNext(); ) {
				Enemy enemy = itEnemy.next();
				if (npc.rectangle.overlaps(enemy.rectangle)) {
					System.out.println("NPC_TOUCH_ENEMY!!");
					npc.die();
				}
			}
		}
	}

	/*
	public void updateAxes() {
		for (Iterator<Axe> it = axeList.iterator(); it.hasNext(); ) {
			Axe axe = it.next();
			axe.update();
			if (axe.rectangle.overlaps(player.rectangle)) {
				player.getDamage(axe.HERO_DAMAGE);
				it.remove();
			}
			/*else if (axe.rectangle.overlaps(mapObject)){
				it.remove;
			}
		}
	}*/

	public void updateLoot() {
		for (Iterator<Loot> it = lootList.iterator(); it.hasNext(); ) {
			Loot loot = it.next();
			if (loot.rectangle.overlaps(player.rectangle)) {
				//itemPickUp
				if (loot.type == Loot.Type.SPEED_BONUS) {
					System.out.println("Taking speed_bonus");
				} else {
					inventory.takeItem(loot.type.ordinal());
				}
				it.remove();
			}

		}
	}

	public void renderShape(Rectangle rect) {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		if (rect.overlaps(new Rectangle(Gdx.input.getX(), Gdx.input.getY(), 1, 1))) {
			System.out.println("RECTANGLE_INTERSECT");
		}
		shapeRenderer.end();
	}

	public void renderBullets() {
		for (Bullet bullet : bulletList) {
			bullet.render(batch);
			//renderShape(bullet.rectangle);
		}
	}

	public void renderEnemies() {
		for (Enemy enemy : enemyList) {
			//enemy.animation.update(enemy.state, enemy.direction);
			enemy.render(batch);
			//renderShape(enemy.rectangle);
		}
	}

	public void renderNpcs() {
		for (Npc npc : npcList) {
			//npc.animation.getCurrentFrame(npc.state);
			npc.render(batch);
			//renderShape(npc.rectangle);
		}
	}

	public void renderLoot() {
		for (Loot loot : lootList) {
			loot.render(batch);
		}
	}


	public Vector<Npc> initializeNpc(Texture texture) {
		Vector<Npc> npcList = new Vector<Npc>();
		npcList.add(new Npc(texture, Npc.Type.PHOTOGRAPHS, 5 * 48, 8 * 48));
		npcList.add(new Npc(texture, Npc.Type.BABY, 50 * 48, 10 * 48));

		npcList.add(new Npc(texture, Npc.Type.TEACHER, 9 * 48, 15 * 48));
		npcList.add(new Npc(texture, Npc.Type.DOG, 53 * 48, 15 * 48));
		npcList.add(new Npc(texture, Npc.Type.SOLDIER, 28 * 48, 28 * 48));
		npcList.add(new Npc(texture, Npc.Type.SEARCHER, 55 * 48, 4 * 48));
		npcList.add(new Npc(texture, Npc.Type.COOK, 20 * 48, 14 * 48));
		npcList.add(new Npc(texture, Npc.Type.GIRL, 15 * 48, 6 * 48));
		return npcList;
	}

	//creates random loot_item at random position on ammo
	public Loot createLootItem() {
		Random rand = new Random();

		final float STEP_TILE = 64.f;
		final Vector2 mapSize = new Vector2(640, 480);
		final Vector2 LOOT_IMAGE_SIZE = new Vector2(16, 16);


		while (true) {
			Rectangle lootRect = new Rectangle((rand.nextInt((int) mapSize.x)) * STEP_TILE,
					(rand.nextInt((int) mapSize.y)) * STEP_TILE,
					LOOT_IMAGE_SIZE.x,
					LOOT_IMAGE_SIZE.y);

			boolean isPositionFree = true;
			for (int i = 0; i < map.solidObjects.size() && isPositionFree == false; ++i) {
				/*TODO: найти способ взять ректангл у тайла if (lootRect.overlaps(ammo.solidObjects[i])) {
					isPositionFree = false;
				}
				*/
			}
			if (isPositionFree) {
				return (new Loot(assets, Loot.Type.values()[rand.nextInt(Npc.Type.values().length)], lootRect));
			}
		}
	}

	private boolean isCollisionWithMap(Rectangle entityRect) {
		return false;
	}

	public void renderUI(SpriteBatch batch) {
		font.setColor(255f, 255f, 255f, 0);
		font.draw(batch, "Ammo: " + String.valueOf(player.ammo), 400, 500);
	}

}