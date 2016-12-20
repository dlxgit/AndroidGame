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


public class Game extends ApplicationAdapter {
    private enum State {
        MENU,
        PLAY,
        END,
        LEVEL_COMPLETE
    }

    public static final int ENEMY_MAX_QUANTITY = 7;
    public static final int NPC_MAX_QUANTITY = 8;
    public static final Vector2 ENEMY_SPAWN_RADIUS = new Vector2(400, 400);
    public static final Vector2 MAP_SIZE = new Vector2(3200, 3200);
    public static final float SMG_USING_TIME = 0.5f;    //delay to allow to move after using item
    public static final float GRENADE_THROW_ACTION_TIME = 0.5f;
    public static final float FIRE_EXTINGUISHER_DAMAGE = 50f;


    private Assets assets;

    private Stage stage;

    private OrthographicCamera camera;
    private Viewport viewport;
    float lastFrameDuration;
    SpriteBatch batch;
    private SpriteBatch hudBatch;
    private TouchPad touchPad;
    private InputController inputController;

    private State state;

    private Player player;
    private Inventory inventory;

    private Vector<Enemy> enemyList;
    private Vector<Bullet> bulletList;
    private Vector<Npc> npcList;
    private Vector<Loot> lootList;

    private Random rnd;
    private int survivedNpcs;

    private TileMap map;
    private MapObjects solidObjects;

    private Hud hud;
    private HealthBar healthBar;
    private PressButton fireButton;
    private PressButton changeSlotButton;
    private PressButton startButton;
    private PressButton quitButton;


    @Override
    public void create() {
        float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        Vector2 GAMEWORLD_SIZE = new Vector2(aspectRatio * 3200, 3200);
        camera = new OrthographicCamera(GAMEWORLD_SIZE.x, GAMEWORLD_SIZE.y);
        camera.position.set(GAMEWORLD_SIZE.x / 2 - 1700, GAMEWORLD_SIZE.y / 2 + 1080, 0);
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();

        stage = new Stage(new StretchViewport(1920, 1024));
        touchPad = new TouchPad();
        touchPad.create();
        stage.addActor(touchPad.getTouchpad());
        inputController = new InputController();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputController);
        Gdx.input.setInputProcessor(multiplexer);

        assets = new Assets();

        bulletList = new Vector<Bullet>();
        enemyList = new Vector<Enemy>();
        lootList = new Vector<Loot>();
        inventory = new Inventory();
        npcList = new Vector<Npc>();

        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        map = new TileMap(camera, 0);


        healthBar = new HealthBar(assets, new Vector2(30, 400));

        state = State.MENU;

        fireButton = new PressButton(assets, PressButton.Type.FIRE);
        changeSlotButton = new PressButton(assets, PressButton.Type.CHANGE_SLOT);
        startButton = new PressButton(assets, PressButton.Type.START);
        quitButton = new PressButton(assets, PressButton.Type.QUIT);

        hud = new Hud(assets);
        rnd = new Random();
        lastFrameDuration = 0;
        assets.menuMusic.play();
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
        hudBatch.dispose();
        assets.dispose();
        map.dispose();
    }

    void initializeWorld(int level) {
        survivedNpcs = 0;

        bulletList = new Vector<Bullet>();
        enemyList = new Vector<Enemy>();
        lootList = new Vector<Loot>();
        inventory = new Inventory();
        npcList = new Vector<Npc>();
        npcList = Npc.initializeNpc(assets);

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
        hudBatch.draw(assets.levelFinishTexture,
                Gdx.graphics.getWidth() / 2 - assets.levelFinishTexture.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - assets.levelFinishTexture.getHeight() / 2);
        hudBatch.end();
    }

    private void onGameOver(){
        if(inputController.isTouched){
            state = State.MENU;
        }
        hudBatch.begin();
        hudBatch.draw(assets.gameOverTexture,
                Gdx.graphics.getWidth() / 2 - assets.gameOverTexture.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - assets.gameOverTexture.getHeight() / 2);
        hudBatch.end();
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

    private void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if(lastFrameDuration < 0.035){
            lastFrameDuration += deltaTime;
            return;
        }
        lastFrameDuration = deltaTime;

        updateCamera();
        checkEnemySpawn();
        checkLootSpawn();
        player.update(touchPad, solidObjects);
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

        if(survivedNpcs > NPC_MAX_QUANTITY - 2){
            assets.level1Music.stop();
            assets.level0Music.stop();
            assets.levelFinishSound.play();
            state = State.LEVEL_COMPLETE;
        }
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

    private void updateBullets() {
        for (Iterator<Bullet> it = bulletList.iterator(); it.hasNext(); ) {
            Bullet bullet = it.next();
            bullet.update(solidObjects);

            if (bullet.isExploded()) {//if bullet finished its deathanimation(bullet explosion/grenade explosion)
                it.remove();
                continue;
            }
            if (bullet.getTarget() == Bullet.Target.PLAYER) { //axe
                if (bullet.isCollisionWithTarget(player.rectangle)) {
                    player.takeDamage(10, assets);
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

    private void updateEnemies() {
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

    private void updateNpcs() {
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

    private void updateLoot() {
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

    private void renderBullets() {
        for (Bullet bullet : bulletList) {
            bullet.render(batch);
        }
    }

    private void renderEnemies() {
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



    public void handleInventory() {
        changeSlotButton.update(inputController);
        fireButton.update(inputController);
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

    private void checkEnemySpawn() {
        if (enemyList.size() < ENEMY_MAX_QUANTITY && (enemyList.isEmpty() || enemyList.lastElement().livingTime > 2)) {
            enemyList.add(Enemy.createRandomEnemyNearPlayer(assets, new Rectangle(camera.position.x, camera.position.y, 30, 30), solidObjects, rnd));
        }
    }

    private void checkLootSpawn() {
        if (lootList.size() < Loot.MAX_QUANTITY) {
            lootList.add(Loot.createLoot(assets, solidObjects, rnd));
        }
    }
}