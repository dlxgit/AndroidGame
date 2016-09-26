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
public class Player{
    enum State{
        INIT,
        MOVE,
        MOVE_FAST,
        STAY,
        SHOOT,
        DAMAGED,
        PICKUP,
        RELOAD,
        DEAD
    }

    PlayerAnimation animation;
    public static final Vector2 startPos = new Vector2(300, 300);
    public static final int defaultMoveSpeed = 5;

    Vector2 pos;
    Sprite sprite;
    Texture texture;
    int health;
    int slotNo;
    int nSlots;
    int moveSpeed;
    float currentFrame;
    Direction direction;
    Direction lastDirection;
    State state;

    public Player(Assets assets){
        state = State.MOVE;
        direction = Direction.DOWN;
        lastDirection = Direction.DOWN;

        health = 100;
        pos = new Vector2(startPos);
        texture = assets.manager.get(assets.heroTextureName);
        animation = new PlayerAnimation(texture);
        sprite = new Sprite(texture);
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2, Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
        moveSpeed = defaultMoveSpeed;
    }

    public void render(Batch batch){
        //batch.draw(texture,actorX,actorY);
        //sprite.draw(batch);
        animation.play(state, direction);
        batch.draw(animation.getCurrentFrame(), sprite.getX(), sprite.getY());
    }
        //sprite.draw(batch);


    public void updatePosition(TouchPad touchPad){
        sprite.setPosition(sprite.getX() + touchPad.getDeltaDistance().x * moveSpeed, sprite.getY() + touchPad.getDeltaDistance().y * moveSpeed);
    }

    public void updateDirection(Touchpad touchpad) {
        Vector2 v = new Vector2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
        float angle = v.angle();
        int nAngle = (int) ((angle + 45) / 90);
        //System.out.println(nAngle);
        switch(nAngle){
            case 0:
                direction = Direction.RIGHT;
                break;
            case 1:
                direction = Direction.UP;
                break;
            case 2:
                direction = Direction.LEFT;
                break;
            case 3:
                direction = Direction.DOWN;
                break;
        }

        if (v.x != 0 && v.y != 0) {
            sprite.setRotation(angle);
        }
    }

    public void update(TouchPad touchPad){
        updatePosition(touchPad);
        updateDirection(touchPad.getTouchpad());
        animation.update(state, direction);

    }
}
