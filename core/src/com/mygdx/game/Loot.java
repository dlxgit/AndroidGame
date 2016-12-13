package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Andrey on 07.10.2016.
 */

public class Loot {
    public enum Type{
        SMG(0),
        GRENADE(1),
        FIRE_EXTINGUISHER(2), //not added yet
        MEDICINE(3),
        SPEED_BONUS(4);

        private int val;

        private Type(int val) {

            this.val = val;
        }

        public static Type getType(int val){
            switch(val){
                case 0:
                    return SMG;
                case 1:
                    return GRENADE;
                case 2:
                    return FIRE_EXTINGUISHER;
                case 3:
                    return MEDICINE;
                case 4:
                    return SPEED_BONUS;
            }
            return SPEED_BONUS;
        }
    }

    public static final Vector2 IMAGE_SIZE = new Vector2(32, 32);
    public static final int MAX_QUANTITY = 8;

    Type type;
    TextureRegion region;
    Rectangle rectangle;
    //each item should have final ammo (declared in game or ...)

    int quantity; //?? ЗАЧЕМ

    public Loot(Assets assets, Type type, Vector2 position){
        this.type = type;
        this.rectangle = new Rectangle(position.x, position.y , IMAGE_SIZE.x, IMAGE_SIZE.y);
        Texture texture = assets.manager.get(assets.itemsTextureName);
        this.region = new TextureRegion(texture, (int)(type.val * IMAGE_SIZE.x), 0, (int)IMAGE_SIZE.x, (int)IMAGE_SIZE.y);
    }

    void update(){

    }

    boolean activate(){
        //?
        return true;
    }

    void render(SpriteBatch batch){
        batch.draw(region, rectangle.getX(), rectangle.getY());
        //batch.draw(texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    static Rectangle calculateSpawnPosition(MapObjects solidObjects, Random rand) {
        return Entity.calculateObjectSpawnPosition(IMAGE_SIZE, solidObjects, new Rectangle(0, 0, Game.MAP_SIZE.x, Game.MAP_SIZE.y), rand);
    }

    static Loot createLoot(Assets assets, MapObjects solidObjects, Random rand){
        Vector2 pos = calculateSpawnPosition(solidObjects, rand).getPosition(new Vector2());
        return (new Loot(assets, Loot.Type.values()[rand.nextInt(Loot.Type.values().length)],
                pos));
    }
}
