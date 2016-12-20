package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Andrey on 11.09.2016.
 */
public class Assets {
    AssetManager manager;

    public static final String playerImageTextureName = "images/hero.png";
    public static final String heroTextureName = "images/hero.png";
    public static final String enemyTextureName = "images/zombie.png";
    public static final String axeEnemyTextureName = "images/axe_enemy.png";

    public static final String npcTextureName = "images/npc.png";

    public static final String fireButtonTextureName = "images/fireButton.png";
    public static final String switchButtonTextureName = "images/switchButton.png";

    public static final String bulletTextureName = "images/bullet.png";

    public static final String itemsTextureName = "images/items.png";

    //public static final String level0FileName = "levels/testLevel.tmx";
    public static final String level0Name = "levels/level0.tmx";
    public static final String level1Name = "level1.tmx";
    public static TiledMap lvl0;

    public static final String healthBarBorderName = "images/healthBarBorder.png";
    public static final String healthBarLineName = "images/healthBarLine.png";

    public static final String startButtonName = "images/startGameButton.png";
    public static final String quitButtonName = "images/quitGameButton.png";

    public static final String fontName = "font/arialbd.ttf";
    public static final String gameOverScreenName = "images/gameOverScreen.png";
    public static final String levelFinishName = "images/levelComplete.png";
    public static final String backGroundTextureName = "images/background.jpg";
    private BitmapFont font;

    Music menuMusic;
    Music level0Music;
    Music level1Music;

    Sound levelFinishSound;

    Sound bulletSound;
    Sound takeItemSound;
    Sound npcDeathSound;

    Sound takeDamageSound;
    Sound npcSurviveSound;
    Sound enemyExplosionSound;

    Texture backGroundTexture;
    public Assets(){
        //manager.load(heroTextureName, Texture.class);
        manager = new AssetManager();
        load();
    }

    public void load(){
        /*
        loadFont();
        while(!manager.update())
        {
            System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
        }
        System.out.println("Loaded: " + (int) (manager.getProgress() * 100) + "%");
*/
        loadTextures();

        while(!manager.update())
        {
            System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
        }
        System.out.println("Loaded: " + (int) (manager.getProgress() * 100) + "%");
        backGroundTexture = manager.get(backGroundTextureName);

        //manager.load(fontFileName, BitmapFont.class);

        //Music mus = manager.get("sounds/beast_attack.mp3");

        manager.setLoader(Sound.class, new SoundLoader(new InternalFileHandleResolver()));
        manager.load("sounds/beast_attack.mp3", Sound.class);


        while(!manager.update())
        {
            System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
        }
        System.out.println("Loaded: " + (int) (manager.getProgress() * 100) + "%");
        Sound sound = manager.get("sounds/beast_attack.mp3", Sound.class);
        manager.setLoader(BitmapFont.class, new BitmapFontLoader(new InternalFileHandleResolver()));
        manager.load("font/testFont.fnt", BitmapFont.class);

        manager.setLoader(TiledMap.class, new TmxMapLoader());
        //manager.load(level0Name, TiledMap.class);
        while(!manager.update())
        {
            System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
        }


        loadAudio();

        /*
        loadFont();
        while(!manager.update())
        {
            System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
        }
        System.out.println("Loaded: " + (int) (manager.getProgress() * 100) + "%");
        */
    }

    public void loadTextures() {
        manager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        manager.load(heroTextureName, Texture.class);
        manager.load(enemyTextureName, Texture.class);
        manager.load(axeEnemyTextureName, Texture.class);
        manager.load(bulletTextureName, Texture.class);
        manager.load(fireButtonTextureName, Texture.class);
        manager.load(switchButtonTextureName, Texture.class);

        manager.load(startButtonName, Texture.class);
        manager.load(quitButtonName, Texture.class);

        manager.load(healthBarBorderName, Texture.class);
        manager.load(healthBarLineName, Texture.class);
        manager.load(npcTextureName, Texture.class);
        manager.load(itemsTextureName, Texture.class);

        manager.load(gameOverScreenName, Texture.class);
        manager.load(levelFinishName, Texture.class);
        manager.load(backGroundTextureName, Texture.class);
    }

    public void loadFont(){
        //manager.setLoader(BitmapFont.class, new BitmapFontLoader(new InternalFileHandleResolver()));
        manager.load(fontName, BitmapFont.class);
    }

    public void loadLevel(int level){
        //manager.setLoader(Texture.class, new (new InternalFileHandleResolver()));
        //manager.load()
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        if(level == 0){
            //manager.load(level0FileName, TiledMap.class);
            lvl0 = new TmxMapLoader().load(level0Name);
        }
        else if(level == 1) {
            manager.load(level1Name, TiledMap.class);
        }
    }

    public TiledMap getLevel(int level){
        String mapStrFile = "levels/level" + new Integer(level).toString() + ".tmx";
        return lvl0;//manager.get(mapStrFile);//new TiledMap();
    }

    public void loadAudio(){
        manager.setLoader(Music.class, new MusicLoader(new InternalFileHandleResolver()));;


        manager.load("sounds/level0.ogg", Music.class);
        manager.load("sounds/level1.ogg", Music.class);
        manager.load("sounds/level2.ogg", Music.class);
        //audio.finish.o
        manager.load("sounds/menu.ogg", Music.class);

        while(!manager.update())
        {
            System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
        }

        manager.setLoader(Sound.class, new SoundLoader(new InternalFileHandleResolver()));


        manager.load("sounds/loot.wav", Sound.class);
        manager.load("sounds/npc_death.wav", Sound.class);
        manager.load("sounds/npc_survive.wav", Sound.class);
        manager.load("sounds/shot.wav", Sound.class);
        manager.load("sounds/finish.ogg", Sound.class);
        manager.load("sounds/taken_damage.ogg", Sound.class);
        manager.load("sounds/enemy_explosion.ogg", Sound.class);
        while(!manager.update())
        {
            System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
        }

        menuMusic = manager.get("sounds/menu.ogg");
        level0Music = manager.get("sounds/level0.ogg");
        level1Music = manager.get("sounds/level1.ogg");
        levelFinishSound = manager.get("sounds/finish.ogg");

        bulletSound = manager.get("sounds/shot.wav");
        takeItemSound = manager.get("sounds/loot.wav");
        npcDeathSound = manager.get("sounds/npc_death.wav");
        takeDamageSound = manager.get("sounds/taken_damage.ogg");
        npcSurviveSound = manager.get("sounds/npc_survive.wav");
        enemyExplosionSound = manager.get("sounds/enemy_explosion.ogg");
    }

    public void dispose(){
        manager.dispose();
    }
}
