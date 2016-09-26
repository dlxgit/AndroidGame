package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.Vector;

/**
 * Created by Andrey on 13.09.2016.
 */
public class TileMap {
    public static final float SCALE = 10.f;
    OrthogonalTiledMapRenderer renderer;
    TiledMap lvl;
    Vector<MapObject> allObjects;
    Vector<MapObject> solidObjects;

    public TileMap(Assets assets, OrthographicCamera camera, int level){
        assets.loadLevel(level);
        lvl = assets.getLevel(level);
        renderer = new OrthogonalTiledMapRenderer(lvl, SCALE);
        //renderer.setView(camera);
    }

    public void render(SpriteBatch batch){
        int[] backgroundLayers = { 0, 1 }; // don't allocate every frame!
        int[] foregroundLayers = { 2 };
        TiledMapTileLayer layer = (TiledMapTileLayer)lvl.getLayers().get(0);
        //renderer.render();
    }
}
