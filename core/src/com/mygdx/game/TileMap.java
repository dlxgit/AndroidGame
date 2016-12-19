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
    Vector<MapObject> allObjects;
    Vector<MapObject> solidObjects;
    MapObjects objects;


    public TileMap(Assets assets, OrthographicCamera camera, int level){
        lvl = new TmxMapLoader().load("levels/level0.tmx");
        renderer = new OrthogonalTiledMapRenderer(lvl);
        renderer.setView(camera);
        renderer.setMap(lvl);
        allObjects = new Vector<MapObject>();
    }

    public void update(OrthographicCamera camera){
        renderer.setView(camera);
    }

    public void render(SpriteBatch batch){
        //int[] backgroundLayers = { 0, 1 }; // don't allocate every frame!
        //int[] foregroundLayers = { 2 };
        //TiledMapTileLayer layer = (TiledMapTileLayer)lvl.getLayers().get(0);

/*
        for(MapObject obj : lvl.getLayers().get("solid").getObjects()){
            renderer.renderObject(obj);
        }

        lvl.getTileSets();
        */

        /*for(MapObject obj : lvl.getLayers().get(0).getObjects()){
            renderer.renderObject(obj);
        }*/
        //batch.draw(lvl.getLayers().get("solid").getObjects().get(0).);
        renderer.render();
    }
}
