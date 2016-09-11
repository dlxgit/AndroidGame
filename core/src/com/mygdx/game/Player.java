package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

/**
 * Created by Andrey on 29.08.2016.
 */
public class Player extends Actor {
    enum State{
        INIT,
        MOVE,
        MOVE_FAST,
        STAY,
        SHOOT,
        DAMAGED,
        PICKUP,
        DEAD
    }

    public static final Vector2 startPos = new Vector2(300, 300);
    public static final int defaultMoveSpeed = 5;

    Vector2 pos;
    Sprite sprite;
    Texture texture;
    int health;
    int moveSpeed;



    public void create(){
        health = 100;
        pos = new Vector2(startPos);
        texture = new Texture(Gdx.files.internal("images/ship.png"));
        sprite = new Sprite(texture);
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2, Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
        moveSpeed = defaultMoveSpeed;
    }

    public void render(Batch batch){
        //batch.draw(texture,actorX,actorY);
        sprite.draw(batch);
    }
        //sprite.draw(batch);


    public void updatePosition(TouchPad touchPad){
        sprite.setPosition(sprite.getX() + touchPad.getDeltaDistance().x * moveSpeed, sprite.getY() + touchPad.getDeltaDistance().y * moveSpeed);
    }

    public void rotate(Touchpad touchpad){
        Vector2 v = new Vector2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
        float angle = v.angle();
        if(v.x != 0 && v.y != 0) {
            sprite.setRotation(angle);
        }
    }

    public void update(TouchPad touchPad){
        updatePosition(touchPad);
        rotate(touchPad.getTouchpad());
    }
}
