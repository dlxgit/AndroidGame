package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Andrey on 07.10.2016.
 */

public class Item {
    enum Type{
        PISTOL,
        RIFLE,
        FIRE_EXTINGUISHER,
        SODA_GRENADE,

        AMMO_BONUS,
        HEALTH_BONUS,

    }

    Texture texture;
    Sprite sprite;
    Rectangle rectangle;
    //each item should have final ammo (declared in game or ...)

    int quantity;

    void update(){

    }

    boolean activate(){
        //?
        return true;
    }

    void render(SpriteBatch batch){
        sprite.draw(batch);
    }
}
