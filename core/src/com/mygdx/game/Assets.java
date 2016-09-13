package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Andrey on 11.09.2016.
 */
public class Assets {
    public static AssetManager manager;
    public static final String heroTextureName = "images/hero.png";
    public static final String enemyTextureName = "images/zloyvrag.png";
    public static final String fireButtonTextureName = "images/shootButton.png";

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
        System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
    }

    public void loadTextures() {
        manager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        manager.load(heroTextureName, Texture.class);
        manager.load(enemyTextureName, Texture.class);
        manager.load(fireButtonTextureName, Texture.class);
    }

    public static void dispose(){
        manager.dispose();
    }
}
