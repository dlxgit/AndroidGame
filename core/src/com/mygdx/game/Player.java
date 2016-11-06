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
        //TODO: MOVE_FAST,  (убрать из состояний, сделать лишь таймер и увеличение мса x2)
        STAY,

        DAMAGED,
        PICKUP,//
        RELOAD,//not here
        DEAD,

        SHOOT,
        EXTINGUISH,
        THROW_GRENADE,
    }


    PlayerAnimation animation;
    public final Vector2 startPos = new Vector2(300, 300);
    public final int defaultMoveSpeed = 10;
    public final float ITEM_COOLDOWN = 0.5f;
    public final float MAX_HEALTH = 100;

    public final float BULLET_DAMAGE = 100;

    Texture texture;
    int health;

    Direction lastDirection;
    State state;

    boolean isBeastForm; //TODO: убрать поле и в месте проверки героя на состояние зверя проверять float beastTimerRemaining > 0;


    float itemCooldown;
    float ammo;

    public Player(Assets assets){
        state = State.MOVE;
        direction = Direction.DOWN;
        lastDirection = Direction.DOWN;

        health = 100;
        rectangle = new Rectangle(startPos.x, startPos.y, 37, 88);
        texture = assets.manager.get(assets.heroTextureName);
        animation = new PlayerAnimation(texture);
        //sprite = new Sprite(texture);
        //sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2, Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
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

    public void updateState(Touchpad touchpad) {
        Vector2 v = new Vector2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
        //System.out.println("KNOB: " + String.valueOf(v.x) + " " + String.valueOf(v.y));


        if(state == State.DAMAGED){
            if(animation.damagedAnimation.isAnimationFinished(animation.stateTime)){
                System.out.println("DAMAGE_ANIMATION_FINISHED!");
                state = State.MOVE;
            }
        }
        if(Math.abs(v.x) < 0.2 && Math.abs(v.y) < 0.2){
            //direction = Direction.NONE;
            if(state != State.DAMAGED) {
                state = Player.State.STAY;
            }
            return;
        }
        else if (state != State.DAMAGED){
            state = Player.State.MOVE;
        }

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
    }

    public void update(TouchPad touchPad){
        //updatePosition(touchPad);
        if(itemCooldown > 0){
            itemCooldown -= Gdx.graphics.getDeltaTime();
        }

        updateState(touchPad.getTouchpad());
        if(state == State.MOVE){
            updatePosition(touchPad);
        }
        animation.update(state, lastDirection);
    }

    public Direction getLastDirection(){
        return lastDirection;
    }


    public void getDamage(float damage){
        health -= damage;
        state = State.DAMAGED;
        animation.stateTime = 0;
    }
}
