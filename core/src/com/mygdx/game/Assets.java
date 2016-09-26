package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Created by Andrey on 11.09.2016.
 */
public class Assets {
    public static AssetManager manager;
    public static final String heroTextureName = "images/hero.png";
    public static final String enemyTextureName = "images/zloyvrag.png";
    public static final String fireButtonTextureName = "images/fireButton.png";
    public static final String level0FileName = "levels/level0.tmx";
    public static final String level1FileName = "level1.tmx";
    public static TiledMap lvl0;

    public Assets(){
        //manager.load(heroTextureName, Texture.class);
        manager = new AssetManager();
        load();
    }

    public void load(){
        loadTextures();
        while(!manager.update())
        {
            System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
        }
        System.out.println("Loaded: " + (int) (manager.getProgress() * 100) + "%");
    }

    public void loadTextures() {
        manager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        manager.load(heroTextureName, Texture.class);
        manager.load(enemyTextureName, Texture.class);
        manager.load(fireButtonTextureName, Texture.class);
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

    public static void dispose(){
        manager.dispose();
    }
}
