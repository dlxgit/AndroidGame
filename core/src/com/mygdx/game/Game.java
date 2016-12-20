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

 */


//пуля не удаляется после попадания в киайца

//сделать рекорды + как цель - спасти больше npc, второй параметр: как можно быстрее.
//сделать спавн npc Случайным? (генерация уровня), тем самым будет 1 уровень, и целью будет поставить рекорд, спася больше npc, и за меньшее время.
//сделать счетчик времени на экране

//огнетушитель менять дистанцию + !!не коцает врагов?
//

public class Game extends ApplicationAdapter {
    enum State {
        MENU,
        PLAY,
        END,
        LEVEL_COMPLETE
    }

    static final int ENEMY_MAX_QUANTITY = 5;
    //static final int NPC_MAX_QUANTITY = 9;
    static final int NPC_MAX_QUANTITY = 1;
    static final Vector2 ENEMY_SPAWN_RADIUS = new Vector2(400, 400);
    static final Vector2 MAP_SIZE = new Vector2(3200, 3200);
    float aspectRatio;
    Vector2 GAMEWORLD_SIZE;

    int survivedNpcs;

    static final float SMG_USING_TIME = 0.5f;    //delay to allow to move after using item
    static final float GRENADE_THROW_ACTION_TIME = 0.5f;
    static final float FIRE_EXTINGUISHER_DAMAGE = 50f;
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
    PressButton startButton;
    PressButton quitButton;

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
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();

        stage = new Stage(new StretchViewport(1920, 1024));
        touchPad = new TouchPad();
        touchPad.create();
        stage.addActor(touchPad.getTouchpad());
        inputController = new InputController();
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputController);
        Gdx.input.setInputProcessor(multiplexer);

        shapeRenderer = new ShapeRenderer();
        assets = new Assets();

        bulletList = new Vector<Bullet>();
        enemyList = new Vector<Enemy>();
        lootList = new Vector<Loot>();
        inventory = new Inventory();
        npcList = new Vector<Npc>();

        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        map = new TileMap(camera, 0);

        fireButton = new PressButton(assets, PressButton.Type.FIRE);
        changeSlotButton = new PressButton(assets, PressButton.Type.CHANGE_SLOT);
        startButton = new PressButton(assets, PressButton.Type.START);
        quitButton = new PressButton(assets, PressButton.Type.QUIT);

        rnd = new Random();

        healthBar = new HealthBar(assets, new Vector2(30, 400));

        state = State.MENU;

        hud = new Hud(assets);
        assets.menuMusic.play();
    }

    void initializeWorld(int level) {
        survivedNpcs = 0;
        gameTime = 0;

        bulletList = new Vector<Bullet>();
        enemyList = new Vector<Enemy>();
        lootList = new Vector<Loot>();
        inventory = new Inventory();
        npcList = new Vector<Npc>();
        npcList = initializeNpc();

        map = new TileMap(camera, level);
        player = new Player(assets);
        rnd = new Random();

        solidObjects = map.lvl.getLayers().get("solid").getObjects();
        if(level == 0){
            assets.level0Music.play();
        }
        else{
            assets.level1Music.play();
        }
    }

    @Override
    public void render() {
        switch (state) {
            case PLAY:
                this.update();
                renderGame();
                System.out.println("____________");
                break;
            case LEVEL_COMPLETE:
                onLevelComplete();
                break;
            case END:
                onGameOver();
                break;
            case MENU:
                onMenu();
                break;
            default:
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        batch.dispose();
        assets.dispose();
        map.dispose();
    }

    private void onMenu(){
        startButton.update(inputController);
        quitButton.update(inputController);
        if(startButton.isPressed()){
            assets.menuMusic.stop();
            state = State.PLAY;
            initializeWorld(0);
        }
        else if(quitButton.isPressed()){
            dispose();
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        hudBatch.begin();
        hudBatch.draw(assets.backGroundTexture, Gdx.graphics.getWidth() / 2 - assets.backGroundTexture.getWidth() / 2, Gdx.graphics.getHeight() / 2 - assets.backGroundTexture.getHeight() / 2);
        startButton.render(hudBatch);
        quitButton.render(hudBatch);
        hudBatch.end();
    }

    private void onLevelComplete() {
        if (inputController.isTouched) {
            map.dispose();
            if(map.nLevel == 0){
                state = State.PLAY;
                initializeWorld(1);
                assets.level1Music.play();
            }
            else {
                assets.level0Music.stop();
                assets.level1Music.stop();
                state = State.MENU;
            }
        }
        hudBatch.begin();
        hudBatch.draw(assets.levelFinishTexture, Gdx.graphics.getWidth() / 2 - assets.levelFinishTexture.getWidth() / 2, Gdx.graphics.getHeight() / 2 - assets.levelFinishTexture.getHeight() / 2);
        hudBatch.end();
    }

    private void onGameOver(){
        if(inputController.isTouched){
            state = State.MENU;
        }
        hudBatch.begin();
        hudBatch.draw(assets.gameOverTexture, Gdx.graphics.getWidth() / 2 - assets.gameOverTexture.getWidth() / 2, Gdx.graphics.getHeight() / 2 - assets.gameOverTexture.getHeight() / 2);
        hudBatch.end();
    }

    private void updateCamera(){
        camera.update();
        camera.position.set(player.rectangle.x, player.rectangle.y, 0);
        if (camera.position.x > 2220) {
            camera.position.x = 2220;
        }
        else if (camera.position.x < Gdx.graphics.getWidth() / 2) {
            camera.position.x = Gdx.graphics.getWidth() / 2 + 50;
        }
        if (camera.position.y > 2660) {
            camera.position.y = 2660;
        }
        else if (camera.position.y < 2010) {
            camera.position.y = 2010;
        }
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        batch.end();

        hudBatch.begin();
        fireButton.render(hudBatch);
        changeSlotButton.render(hudBatch);
        hud.render(hudBatch);
        hudBatch.end();

        stage.draw();
    }

    public void update() {
        gameTime += Gdx.graphics.getDeltaTime();

        updateCamera();
        player.update(touchPad, solidObjects);
        fireButton.update(inputController);
        changeSlotButton.update(inputController);
        checkEnemySpawn();
        checkLootSpawn();
        handleInventory();

        updateBullets();
        updateEnemies();
        updateNpcs();
        updateLoot();
        inventory.update();

        if (player.health <= 0 || survivedNpcs + npcList.size() < NPC_MAX_QUANTITY - 1) {
            assets.level1Music.stop();
            assets.level0Music.stop();
            state = State.END;
            return;
        }
        healthBar.update((int)(player.health / player.MAX_HEALTH * 100), new Vector2(30, 400));
        hud.update(player.health, inventory);
        map.update(camera);
        stage.act(Gdx.graphics.getDeltaTime());

        if(survivedNpcs > NPC_MAX_QUANTITY - 2 && map.nLevel == 0){
            assets.level1Music.stop();
            assets.level0Music.stop();
            assets.levelFinishSound.play();
            state = State.LEVEL_COMPLETE;
        }

//        if(survivedNpcs > NPC_MAX_QUANTITY - 2 && npcList.size() == 0 || npcList.size() == 0 && gameTime > 30){
//            state = State.LEVEL_COMPLETE;
//        }
    }

    public void updateBullets() {
        for (Iterator<Bullet> it = bulletList.iterator(); it.hasNext(); ) {
            Bullet bullet = it.next();
            bullet.update(solidObjects);

            if (bullet.isExploded()) {//if bullet finished its deathanimation(bullet explosion/grenade explosion)
                it.remove();
                continue;
            }
            if (bullet.getTarget() == Bullet.Target.PLAYER) { //axe
                if (bullet.isCollisionWithTarget(player.rectangle)) {
                    player.takeDamage(bullet.attackDamage, assets);
                    it.remove();
                }
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
                if (bullet.isCollisionWithTarget(enemy.rectangle)) {
                    if (bullet.isCollidable && !bullet.isDead()) { //if bullet - remove enemy and stop checking others.
                        bullet.isCollision = true;
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
       }
    }

    void checkLootSpawn() {
        if (lootList.size() < Loot.MAX_QUANTITY) {
            lootList.add(Loot.createLoot(assets, solidObjects, rnd));
        }
    }


    public void updateEnemies() {
        for (Iterator<Enemy> it = enemyList.iterator(); it.hasNext(); ) {
            Enemy enemy = it.next();
            enemy.update(player, solidObjects, assets);

            if (enemy.isAction()) {
                enemy.setActionCooldown();
                bulletList.add(new Axe(assets, player.rectangle, enemy.rectangle, enemy.direction));
            }
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
                if (npc.animation.isDeathAnimationFinished()) {
                    it.remove();
                }
                continue;
            } else if (npc.rectangle.overlaps(player.rectangle)) {//NPC survive
                survivedNpcs++;
                assets.npcSurviveSound.play();
                it.remove();
                continue;
            }

            for (Enemy enemy : enemyList) {
                if (npc.rectangle.overlaps(enemy.rectangle)) {
                    npc.die();
                    assets.npcDeathSound.play();
                }
            }
        }
    }

    public void updateLoot() {
        for (Iterator<Loot> it = lootList.iterator(); it.hasNext(); ) {
            Loot loot = it.next();
            if (loot.rectangle.overlaps(player.rectangle)) { //itemPickUp
                assets.takeItemSound.play();
                if (loot.type == Loot.Type.SPEED_BONUS) {
                    player.activateSpeedBonus();
                } else {
                    inventory.takeItem(loot.type.ordinal());
                }
                it.remove();
            }
        }
    }

    public void renderBullets() {
        for (Bullet bullet : bulletList) {
            bullet.render(batch);
        }
    }

    public void renderEnemies() {
        for (Enemy enemy : enemyList) {
            enemy.render(batch);
        }
    }

    public void renderNpcs() {
        for (Npc npc : npcList) {
            npc.render(batch);
        }
    }

    public void renderLoot() {
        for (Loot loot : lootList) {
            loot.render(batch);
        }
    }

    public Vector<Npc> initializeNpc() {
        Texture texture = assets.manager.get(assets.npcTextureName);
        Vector<Npc> npcList = new Vector<Npc>();
        npcList.add(new Npc(texture, Npc.Type.PHOTOGRAPHS, 5 * 48, 8 * 48));
        npcList.add(new Npc(texture, Npc.Type.BABY, 48 * 48, 10 * 48));
        npcList.add(new Npc(texture, Npc.Type.TEACHER, 9 * 48, 15 * 48));
        npcList.add(new Npc(texture, Npc.Type.DOG, 53 * 48, 15 * 48));
        npcList.add(new Npc(texture, Npc.Type.SOLDIER, 28 * 48, 28 * 48));
        npcList.add(new Npc(texture, Npc.Type.SEARCHER, 55 * 48, 4 * 48));
        npcList.add(new Npc(texture, Npc.Type.COOK, 22 * 48, 14 * 48));
        npcList.add(new Npc(texture, Npc.Type.GIRL, 15 * 48, 6 * 48));

        return npcList;
    }

    public void handleInventory() {
        if (changeSlotButton.isPressed() && inventory.isChangeAllowed) {
            inventory.changeItem();

        } else if (!changeSlotButton.isPressed()) {
            inventory.isChangeAllowed = true;
        }

        if (fireButton.isPressed() && (player.state == Player.State.MOVE || player.state == Player.State.STAY)) {

            if (inventory.useItem()) {
                switch (inventory.getCurrentSlot()) {
                    case 0://smg shot
                        bulletList.add(new Bullet(assets, player.rectangle.getCenter(new Vector2()), player.lastDirection));
                        player.state = Player.State.SHOOT;
                        player.actionTimeRemaining = SMG_USING_TIME;
                        assets.bulletSound.play();
                        break;
                    case 1://grenade throw
                        player.state = Player.State.THROW_GRENADE;
                        bulletList.add(new Grenade(assets, player.rectangle.getCenter(new Vector2()), player.lastDirection));
                        player.actionTimeRemaining = GRENADE_THROW_ACTION_TIME;
                        break;
                    case 2://fire-extinguisher use
                        player.state = Player.State.EXTINGUISH;
                        player.actionTimeRemaining = SMG_USING_TIME;
                        player.setExtinguisherPosition();
                        break;
                    case 3://health bonus use
                        if(inventory.getCurrentAmmo() > 0) {
                            player.useMedicine();
                            assets.takeItemSound.play();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}