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
import com.badlogic.gdx.maps.MapObjects;
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
    некорректно рисует огнетушитель
    нет текста на экране
    плохое расположение интерфейса
    нет меню
    нет музыки
    не реализована полоска хп, боеприпасы на экране

 */


/*
    доработать огнетушитель (координаты дыма + отдалять и приближать дым по мере нажатия)
    разобраться почему герой умирает при закомменченой строчке player.health -= damage;

    реализовать функции: определения точки спавна врагов (рядом с игроком) + рандомный тип врага, + спавн лута

 */


public class Game extends ApplicationAdapter {
    enum State {
        MENU,
        START,
        PLAY,
        PAUSE,
        END
    }

    static final int ENEMY_MAX_QUANTITY = 5;
    static final Vector2 ENEMY_SPAWN_RADIUS = new Vector2(700, 700);
    static final Vector2 MAP_SIZE = new Vector2(3200, 3200);
    final Vector2 VIEWPORT_SIZE = new Vector2(1280, 1024);
    float aspectRatio;
    Vector2 GAMEWORLD_SIZE;

    int survivedNpcs;


    //time need to pass after using smg - to run after using it
    static final float SMG_USING_TIME = 0.5f;
    static final float GRENADE_THROW_ACTION_TIME = 0.5f;
    static final float FIRE_EXTINGUISHER_DAMAGE = 10f;
    Assets assets;
    State state;
    private OrthographicCamera camera;
    private Stage stage;
    SpriteBatch batch;
    SpriteBatch hudBatch;
    Texture img;
    TouchPad touchPad;

    InputController inputController;

    PressButton fireButton;
    PressButton changeSlotButton;
    PressButton escapeButton;

    Player player;
    InputMultiplexer multiplexer;
    float gameTime;
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

    MapObjects solidObjects;
    Hud hud;

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

        bulletList = new Vector<Bullet>();
        enemyList = new Vector<Enemy>();

        lootList = new Vector<Loot>();

        inventory = new Inventory();

        Texture npcTexture = assets.manager.get(assets.npcTextureName);
        npcList = new Vector<Npc>();
        //npcList = initializeNpc(npcTexture);


        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        map = new TileMap(assets, camera, 0);

        player = new Player(assets);

        fireButton = new PressButton(assets, PressButton.Type.FIRE);
        changeSlotButton = new PressButton(assets, PressButton.Type.CHANGE_SLOT);
        escapeButton = new PressButton(assets, PressButton.Type.ESCAPE);

        //stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        //Gdx.input.setInputProcessor();
        rnd = new Random();

        healthBar = new HealthBar(assets, new Vector2(30, 400));

        lootList.add(new Loot(assets, Loot.Type.SMG, new Vector2(200, 1500 + 1200)));
        lootList.add(new Loot(assets, Loot.Type.FIRE_EXTINGUISHER, new Vector2(200, 1600 + 1200)));
        lootList.add(new Loot(assets, Loot.Type.GRENADE, new Vector2(200, 1700 + 1200)));
        state = State.PLAY;

        font = assets.manager.get("font/testFont.fnt", BitmapFont.class);

        solidObjects = map.lvl.getLayers().get("solid").getObjects();
        hud = new Hud(assets);
    }

    @Override
    public void render() {
        //Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        switch (state) {
            case PLAY:
                updateCamera();
                this.update();
                map.update(camera);
                //System.out.println("State: " + player.state.toString() + " dir: " + player.direction.toString() + " lastDir: " + player.lastDirection.toString());
//				System.out.println("PlayerState: " + player.state.toString());
//				System.out.println("Survived Npcs: " + String.valueOf(survivedNpcs));
//				System.out.println("Inventory index: " + String.valueOf(inventory.getCurrentSlot()));
//				System.out.println("AMMO: " + String.valueOf(inventory.ammo[inventory.getCurrentSlot()]));
                stage.act(Gdx.graphics.getDeltaTime());
                renderGame();

                System.out.println("____________");
                break;
            case PAUSE:
                renderGame();
                break;
            case END:
//                if(gotoMenuButton.isPressed()){
//                    state = State.MENU;
//                }
                break;
            case MENU:
//                if(startButton.isPressed()){
//                    state = State.PLAY;
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        //viewport.update(width, height);
        //camera.position.set(GAMEWORLD_SIZE.x / 2, GAMEWORLD_SIZE.y / 2, 0);
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    private void updateCamera(){
        camera.update();

        camera.position.set(player.rectangle.x, player.rectangle.y, 0);
        //System.out.println()
        if (camera.position.x > GAMEWORLD_SIZE.x - VIEWPORT_SIZE.x / 2)
            camera.position.x = GAMEWORLD_SIZE.x - VIEWPORT_SIZE.x / 2;
        else if (camera.position.x < Gdx.graphics.getWidth() / 2)
            camera.position.x = Gdx.graphics.getWidth() / 2;
//                if(camera.position.x> GAMEWORLD_SIZE.x - VIEWPORT_SIZE.x / 2) camera.position.x=GAMEWORLD_SIZE.x - VIEWPORT_SIZE.x / 2;
//                else if(camera.position.x<Gdx.graphics.getWidth() / 2)camera.position.x = Gdx.graphics.getWidth() / 2;


        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //camera.zoom += 0.1;
        batch.setProjectionMatrix(camera.combined);
    }


    private void renderGame(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        map.render(batch);

        batch.begin();

        touchPad.render();
        player.render(batch);
        renderEnemies();
        renderNpcs();
        renderLoot();
        renderBullets();


        healthBar.render(batch);
        renderUI(batch);

//                if (player.state == Player.State.EXTINGUISH) {
//                    TextureRegion sprite = player.animation.getExtinguisherAnimation(player.stateTime);
//                    Vector2 pos = getExtinguisherRectangle(player.direction, player.rectangle, sprite);
//                    batch.draw(sprite, pos.x, pos.y);
//                }

        //renderShape(player.rectangle);
        //renderShape(new Rectangle(0,0, 100, 100));
        //renderShape(new Rectangle(320.5f, 190.5f, 383.5f, 47f));

        //renderShape(fireButton.sprite.getBoundingRectangle());

        batch.end();

        hudBatch.begin();
        fireButton.render(hudBatch);
        changeSlotButton.render(hudBatch);
        escapeButton.render(hudBatch);
        hud.render(hudBatch, font);
        hudBatch.end();

        stage.draw();
    }

    public void update() {
        gameTime += Gdx.graphics.getDeltaTime();
        //System.out.println("gameTime " + String.valueOf(gameTime));

        player.update(touchPad, solidObjects);
        //System.out.println(player.lastDirection.toString());
        fireButton.update(inputController);
        changeSlotButton.update(inputController);
        escapeButton.update(inputController);
        //updateButtonState(changeSlotButton);

        checkEnemySpawn();
        checkLootSpawn();

        //TODO: make button on screen to control inventory
        handleInventory();

        updateBullets();
        updateEnemies();
        updateNpcs();
        updateLoot();
        inventory.update();

        if (player.health <= 0) {
            state = State.END;
        }

        healthBar.update((int) (player.health / player.MAX_HEALTH * 100), new Vector2(30, 400));
        hud.update(player.health, inventory);
    }

    public void updateBullets() {
        for (Iterator<Bullet> it = bulletList.iterator(); it.hasNext(); ) {
            Bullet bullet = it.next();
            bullet.update(solidObjects);

            if (bullet.isExploded()) {//if bullet finished its deathanimation(bullet explosion/grenade explosion)
                System.out.println("Bullet explosion(remove)");
                it.remove();
                continue;
            }

            if (bullet.getTarget() == Bullet.Target.PLAYER) { //axe
                if (bullet.isCollisionWithTarget(player.rectangle)) {
                    player.takeDamage(bullet.attackDamage);
                    System.out.println("BulletRemoveAfterHittingPlayer" + bullet.livingTime);
                    it.remove();
                }
//                else if(isCollisionWithMap(bullet.rectangle)){
//                    it.remove();
//                }
                continue;
            }

            if (!bullet.isDead() && bullet.isCollidable) {//bullet
                if (bullet.isCollision) {
                    bullet.die();
                    continue;
                }
            }

            //checking collision with enemies (grenades+bullets)
            for (Enemy enemy : enemyList) {
                //checkExtinguish
                if (player.state == Player.State.EXTINGUISH && player.animation.getExtinguisherRectangle(player.lastDirection, player.rectangle).overlaps(enemy.rectangle)) {
                    enemy.health -= FIRE_EXTINGUISHER_DAMAGE;
                }

                if (bullet.isCollisionWithTarget(enemy.rectangle)) {
                    System.out.println("bullet intersects with enemy");

                    if (bullet.isCollidable && !bullet.isDead()) { //if bullet - remove enemy and stop checking others.
                        System.out.println("BulletRemove_@@@@@@@@@@@@@@@@@@@ " + bullet.livingTime);
                        enemy.health -= bullet.attackDamage;
                        break;
                    } else if (!bullet.isCollidable && bullet.isDead()) {//if grenade
                        enemy.health -= bullet.attackDamage;
                    }
                }
            }
        }
    }

    void checkEnemySpawn() {
        if (enemyList.size() < ENEMY_MAX_QUANTITY && (enemyList.isEmpty() || enemyList.lastElement().livingTime > 2)) {
            enemyList.add(Enemy.createRandomEnemyNearPlayer(assets, new Rectangle(camera.position.x, camera.position.y, 30, 30), solidObjects, rnd));
            //enemyList.add(new AxeEnemy(new Vector2(player.rectangle.x + 200, player.rectangle.y), assets));
            //enemyList.add(new AxeEnemy(new Vector2(rnd.nextInt(Gdx.graphics.getWidth() + 1), rnd.nextInt(Gdx.graphics.getHeight() + 1)), assets));
            System.out.println("Enemy spawn");
        }
    }

    void checkLootSpawn() {
        if (lootList.size() < Loot.MAX_QUANTITY) {
            lootList.add(Loot.createLoot(assets, solidObjects, rnd));
            System.out.println("Loot spawn");
        }
    }


    public void updateEnemies() {
        for (Iterator<Enemy> it = enemyList.iterator(); it.hasNext(); ) {
            Enemy enemy = it.next();
            enemy.update(player, solidObjects);

            if (enemy.isAction()) {
                enemy.setActionCooldown();
                bulletList.add(new Axe(assets, player.rectangle, enemy.rectangle, enemy.direction));
            }

            System.out.println("EnemyState :  " + enemy.state.toString());
            if (enemy.state == Enemy.State.EXPLODED) {
                it.remove();
            }
        }
    }


    public void updateNpcs() {
        for (Iterator<Npc> it = npcList.iterator(); it.hasNext(); ) {
            Npc npc = it.next();
            npc.update();

            if (!npc.isLiving()) {
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

            for (Enemy enemy : enemyList) {
                if (npc.rectangle.overlaps(enemy.rectangle)) {
                    System.out.println("NPC_TOUCH_ENEMY!!");
                    npc.die();
                }
            }
        }
    }

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

		shapeRenderer.end();
//		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.rect(100, 100, 500, 500);
//
//		shapeRenderer.end();
	}

    public void renderBullets() {
        for (Bullet bullet : bulletList) {
            bullet.render(batch);
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

    public void renderUI(SpriteBatch batch) {
        font.setColor(255f, 255f, 255f, 0);
        font.draw(batch, "Ammo: " + String.valueOf(player.ammo), 400, 500);
    }

    public void handleInventory() {
        if (changeSlotButton.isPressed() && inventory.isChangeAllowed) {
            inventory.changeItem();

        } else if (!changeSlotButton.isPressed()) {
            inventory.isChangeAllowed = true;
        }

        if (fireButton.isPressed() && (player.state == Player.State.MOVE || player.state == Player.State.STAY)) {

            if (inventory.useItem()) {
                System.out.println("ITEM_USING_ ");
                switch (inventory.getCurrentSlot()) {
                    case 0://smg shot
                        System.out.println("SMG SHOT");
                        bulletList.add(new Bullet(assets, player.rectangle.getCenter(new Vector2()), player.lastDirection));
                        player.state = Player.State.SHOOT;
                        player.actionTimeRemaining = SMG_USING_TIME;
                        break;
                    case 1://grenade throw
                        System.out.println("GRENADE");
                        player.state = Player.State.THROW_GRENADE;
                        bulletList.add(new Grenade(assets, player.rectangle.getCenter(new Vector2()), player.lastDirection));
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
        }
    }
}