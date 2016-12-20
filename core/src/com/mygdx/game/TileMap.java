package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

/**
 * Created by Andrey on 13.09.2016.
 */
public class TileMap {
    public static final float STEP_TILE = 64.f;
    public static final float SCALE = 10.f;
    OrthogonalTiledMapRenderer renderer;
    TiledMap lvl;
    int nLevel;

    public TileMap(OrthographicCamera camera, int level){
        lvl = new TmxMapLoader().load("levels/level" + String.valueOf(level) + ".tmx");
        renderer = new OrthogonalTiledMapRenderer(lvl);
        renderer.setView(camera);
        renderer.setMap(lvl);
        nLevel = level;
    }

    public void update(OrthographicCamera camera){
        renderer.setView(camera);
    }

    public void render(SpriteBatch batch){
        renderer.render();
    }

    public void dispose(){
        lvl.dispose();
    }
}
