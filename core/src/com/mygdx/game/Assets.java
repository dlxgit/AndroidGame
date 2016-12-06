package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
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
    public static final String heroTextureName = "images/hero.png";
    public static final String enemyTextureName = "images/zombie.png";
    public static final String axeEnemyTextureName = "images/axe_enemy.png";

    public static final String npcTextureName = "images/npc.png";
    public static final String fireButtonTextureName = "images/fireButton.png";
    public static final String switchButtonTextureName = "images/switchButton.png";
    public static final String bulletTextureName = "images/bullet.png";

    public static final String itemsTextureName = "images/items.png";

    //public static final String level0FileName = "levels/testLevel.tmx";
    public static final String level0FileName = "levels/level0.tmx";
    public static final String level1FileName = "level1.tmx";
    public static TiledMap lvl0;

    public static final String healthBarBorderFileName = "images/healthBarBorder.png";
    public static final String healthBarLineFileName = "images/healthBarLine.png";

    public static final String fontFileName = "font/arialbd.ttf";

    private BitmapFont font;

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

        manager.load("images/background.jpg", Texture.class);
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
        manager.load(level0FileName, TiledMap.class);
        while(!manager.update())
        {
            System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
        }



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
        manager.load(fireButtonTextureName, Texture.class);
        manager.load(switchButtonTextureName, Texture.class);

        manager.load(healthBarBorderFileName, Texture.class);
        manager.load(healthBarLineFileName, Texture.class);
        manager.load(npcTextureName, Texture.class);
        manager.load(itemsTextureName, Texture.class);
    }

    public void loadFont(){
        //manager.setLoader(BitmapFont.class, new BitmapFontLoader(new InternalFileHandleResolver()));
        manager.load(fontFileName, BitmapFont.class);
    }

    public void loadLevel(int level){
        //manager.setLoader(Texture.class, new (new InternalFileHandleResolver()));
        //manager.load()
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        if(level == 0){
            //manager.load(level0FileName, TiledMap.class);
            lvl0 = new TmxMapLoader().load(level0FileName);
        }
        else if(level == 1) {
            manager.load(level1FileName, TiledMap.class);
        }
    }

    public TiledMap getLevel(int level){
        String mapStrFile = "levels/level" + new Integer(level).toString() + ".tmx";
        return lvl0;//manager.get(mapStrFile);//new TiledMap();
    }

    public void dispose(){
        manager.dispose();
    }
}
