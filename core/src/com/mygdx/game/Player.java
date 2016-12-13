package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

/**
 * Created by Andrey on 29.08.2016.
 */
public class Player extends Entity {
    enum State {
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

    Texture texture;
    int health;

    Direction lastDirection;
    State state;

    float itemCooldown;
    float ammo;

    float actionTimeRemaining;

    float stateTime = 0;

    public Player(Assets assets) {
        state = State.MOVE;
        direction = Direction.NONE;
        lastDirection = Direction.DOWN;

        health = 100;
        rectangle = new Rectangle(startPos.x, Game.MAP_SIZE.y - startPos.y, 19, 36);
        texture = assets.manager.get(assets.heroTextureName);
        animation = new PlayerAnimation(texture);
        //sprite = new Sprite(texture);
        //sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2, Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
        moveSpeed = defaultMoveSpeed;
    }

    public void render(SpriteBatch batch) {
        //batch.draw(texture,actorX,actorY);
        //sprite.draw(batch);
        batch.draw(animation.getCurrentFrame(state, lastDirection, stateTime), rectangle.x, rectangle.y);
        if(state == State.EXTINGUISH){
            renderExtinguisher(batch);
        }
    }
    //sprite.draw(batch);
    private void renderExtinguisher(SpriteBatch batch){
        Rectangle rect = animation.getExtinguisherRectangle(lastDirection, rectangle);
        batch.draw(animation.getExtinguisherAnimation(stateTime), rect.getX(), rect.getY());
        //System.out.println("RenderingExt: " + rect.toString() + " " + rectangle.toString());
    }

    public void updatePosition(TouchPad touchPad) {
        //sprite.setPosition(sprite.getX() + touchPad.getDeltaDistance().x * moveSpeed, sprite.getY() + touchPad.getDeltaDistance().y * moveSpeed);
        rectangle.setPosition(rectangle.x + touchPad.getDeltaDistance().x * moveSpeed, rectangle.y + touchPad.getDeltaDistance().y * moveSpeed);
        //moveRectangle(touchPad.getDeltaDistance().x * moveSpeed,touchPad.getDeltaDistance().x * moveSpeed);
    }

    public void updateState() {
        /*


        if(state == State.THROW_GRENADE){
            if(animation.throwGrenadeUpAnimation.isAnimationFinished(animation.stateTime)){
                state = State.STAY;
            }
            return;
        }
        else if (state == State.SHOOT || state == State.EXTINGUISH){
            float angle = v.angle();
            int sidePart = (int) ((angle + 45) / 90);
            //direction = Direction.intToDirection(sidePart);
            //lastDirection = direction;
            return;
        }

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
        int sidePart = (int) ((angle + 45) / 90);
        direction = Direction.intToDirection(sidePart);

        if(state == Player.State.MOVE){
            lastDirection = direction;
        }
        */

        switch (state) {
            case DAMAGED:
                if (animation.damagedAnimation.isAnimationFinished(stateTime)) {
                    System.out.println("DAMAGE_ANIMATION_FINISHED!");
                    state = State.MOVE;
                }
                break;
            case MOVE:
                if (direction == Direction.NONE) {
                    //direction = Direction.NONE;
                    state = Player.State.STAY;
                }
                break;
            case STAY:
                if (direction != Direction.NONE) {
                    state = State.MOVE;
                }
                break;
            case SHOOT:
                if(actionTimeRemaining < 0){
                    state = State.STAY;
                }
                break;
            case EXTINGUISH:
                //if(//extinguishAnimation.hasFinished?){
                    //state = State.STAY;
                //}
                //if firebutton.ispressed() == false state = STAY;
                if(actionTimeRemaining < 0){
                    state = State.STAY;
                }
                break;
            case THROW_GRENADE:
                if(actionTimeRemaining < 0){
                    state = State.STAY;
                }
                break;
        }
    }

    public void updateDirection(Touchpad touchpad){
        Vector2 v = new Vector2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
        //System.out.println("KNOB: " + String.valueOf(v.x) + " " + String.valueOf(v.y));

        if(Math.abs(v.x) < 0.2 && Math.abs(v.y) < 0.2) {
            direction = Direction.NONE;
        }else{
            float angle = v.angle();
            int sidePart = (int) ((angle + 45) / 90);
            direction = Direction.intToDirection(sidePart);
        }

        if(direction != Direction.NONE){
            lastDirection = direction;
        }
    }

    public void update(TouchPad touchPad, MapObjects solidObjects){
        System.out.println("PlayerState: " + state.toString());
        //System.out.println("LastDir: " + lastDirection.toString());
        //updatePosition(touchPad);
        if(itemCooldown > 0){
            itemCooldown -= Gdx.graphics.getDeltaTime();
        }
        if(actionTimeRemaining > 0){
            actionTimeRemaining -= Gdx.graphics.getDeltaTime();
        }

        updateDirection(touchPad.getTouchpad());
        updateState();

        if(state == State.MOVE){
            updatePositionByCountingCollision(solidObjects);
            //updatePosition(touchPad);
        }
        //System.out.println(lastDirection.toString());
        //System.out.println(state.toString());

        stateTime += Gdx.graphics.getDeltaTime();
    }

    public Direction getLastDirection(){
        return lastDirection;
    }


    public void takeDamage(float damage){
        //health -= damage;
        state = State.DAMAGED;
        stateTime = 0;
    }
}
