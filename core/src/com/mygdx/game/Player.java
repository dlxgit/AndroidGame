package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

/**
 * Created by Andrey on 29.08.2016.
 */
public class Player extends Entity{
    enum State{
        INIT,
        MOVE,
        MOVE_FAST,
        STAY,
        SHOOT,
        DAMAGED,
        PICKUP,//
        RELOAD,//not here
        DEAD
    }


    PlayerAnimation animation;
    public final Vector2 startPos = new Vector2(300, 300);
    public final int defaultMoveSpeed = 10;
    public final float ITEM_COOLDOWN = 0.5f;
    public final float MAX_HEALTH = 100;

    Vector2 pos;
    Sprite sprite;
    Texture texture;
    int health;
    int slotNo;
    int nSlots;
    Direction lastDirection;
    State state;



    float itemCooldown;
    float ammo;

    public Player(Assets assets){
        ammo = 100;
        itemCooldown = 0;
        state = State.MOVE;
        direction = Direction.DOWN;
        lastDirection = Direction.DOWN;

        health = 100;
        pos = new Vector2(startPos);
        rectangle = new Rectangle(startPos.x, startPos.y, 88, 37);
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
        batch.draw(animation.getCurrentFrame(), rectangle.x, rectangle.y);
    }
        //sprite.draw(batch);

    public void updatePosition(TouchPad touchPad){
        //sprite.setPosition(sprite.getX() + touchPad.getDeltaDistance().x * moveSpeed, sprite.getY() + touchPad.getDeltaDistance().y * moveSpeed);
        rectangle.setPosition(rectangle.x + touchPad.getDeltaDistance().x * moveSpeed, rectangle.y + touchPad.getDeltaDistance().y * moveSpeed);
        //moveRectangle(touchPad.getDeltaDistance().x * moveSpeed,touchPad.getDeltaDistance().x * moveSpeed);
    }

    public void updateDirection(Touchpad touchpad) {
        Vector2 v = new Vector2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
        System.out.println("KNOB: " + String.valueOf(v.x) + " " + String.valueOf(v.y));
        if(Math.abs(v.x) < 0.2 && Math.abs(v.y) < 0.2){
            //direction = Direction.NONE;
            state = Player.State.STAY;
            return;
        }
        else state = Player.State.MOVE;

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

        if(state == Player.State.MOVE){
            lastDirection = direction;
        }

        if (v.x != 0 && v.y != 0) {
            sprite.setRotation(angle);
        }
    }

    public void update(TouchPad touchPad){
        //updatePosition(touchPad);
        if(itemCooldown > 0){
            itemCooldown -= Gdx.graphics.getDeltaTime();
        }

        updateDirection(touchPad.getTouchpad());
        if(state == State.MOVE){
            updatePosition(touchPad);
        }
        animation.update(state, lastDirection);
    }

    public Direction getLastDirection(){
        return lastDirection;
    }

    public float getItemCooldown(){
        return itemCooldown;
    }

    public void setItemCooldown(float cooldown){
        itemCooldown = cooldown;
    }

    public boolean isItemUsingAllowed(){
        if(state != State.MOVE && state != State.STAY){
            return false;
        }
        if (itemCooldown > 0) {
            return false;
        }

        return true;
    }

    public void getDamage(float damage){
        health -= damage;
        state = State.DAMAGED;
    }
}
