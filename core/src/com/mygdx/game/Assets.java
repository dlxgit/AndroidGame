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
    public static final String healthBarBorderName = "images/healthBarBorder.png";
    public static final String healthBarLineName = "images/healthBarLine.png";
    public static final String startButtonName = "images/startGameButton.png";
    public static final String quitButtonName = "images/quitGameButton.png";
    public static final String gameOverScreenName = "images/gameOverScreen.png";
    public static final String levelFinishName = "images/levelComplete.png";
    public static final String backGroundTextureName = "images/background.jpg";

    Texture gameOverTexture;
    Texture levelFinishTexture;
    Texture backGroundTexture;
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


    public Assets(){
        manager = new AssetManager();
        load();
    }

    public void load(){
        loadTextures();
        loadAudio();
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
        backGroundTexture = manager.get(backGroundTextureName);
        gameOverTexture = manager.get(gameOverScreenName);
        levelFinishTexture = manager.get(levelFinishName);
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

        while(!manager.update())
        {
            System.out.println("Loading textures: " + (int)(manager.getProgress() * 100) + "%");
        }
        System.out.println("Textures are loaded.");
    }

    public void loadAudio(){
        manager.setLoader(Music.class, new MusicLoader(new InternalFileHandleResolver()));;
        manager.load("sounds/level0.ogg", Music.class);
        manager.load("sounds/level1.ogg", Music.class);
        manager.load("sounds/menu.ogg", Music.class);

        while(!manager.update())
        {
            System.out.println("Loading music: " + (int)(manager.getProgress() * 100) + "%");
        }
        System.out.println("Music resources are loaded.");


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
            System.out.println("Loading sounds: " + (int)(manager.getProgress() * 100) + "%");
        }
        System.out.println("Sound resources are loaded.");
    }

    public void dispose(){
        manager.dispose();
    }
}
