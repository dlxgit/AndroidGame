package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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

    private static final int IMAGE_SIZE = 32;


    Type type;
    TextureRegion region;
    Rectangle rectangle;
    //each item should have final ammo (declared in game or ...)

    int quantity; //?? ЗАЧЕМ

    public Loot(Assets assets, Type type, Vector2 position){
        this.type = type;
        this.rectangle = new Rectangle(position.x, position.y , 16, 16);
        Texture texture = assets.manager.get(assets.itemsTextureName);
        this.region = new TextureRegion();
        this.region = new TextureRegion(texture, (type.val * IMAGE_SIZE), 0, IMAGE_SIZE, IMAGE_SIZE);
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
}
