package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

/**
 * Created by Andrey on 29.08.2016.
 */
public class Player extends Entity {
    enum State {
        MOVE,
        STAY,
        DAMAGED,
        SHOOT,
        EXTINGUISH,
        THROW_GRENADE,
    }

    public static float BONUS_DURATION = 10.f;


    public final Vector2 startPos = new Vector2(300, 300);
    public final int defaultMoveSpeed = 10;
    public final int BONUS_MOVE_SPEED = 4;
    public final float MAX_HEALTH = 100;
    public static final float MEDICINE_HEALTH = 30;

    Texture texture;
    int health;
    Direction lastDirection;
    State state;
    PlayerAnimation animation;

    float itemCooldown;
    float actionTimeRemaining;
    float bonusTimeRemaining = 0;
    float stateTime = 0;
    Vector2 extinguisherStartPos;
    Rectangle extinguisherRect;

    public Player(Assets assets) {
        state = State.MOVE;
        direction = Direction.NONE;
        lastDirection = Direction.DOWN;

        health = 100;
        rectangle = new Rectangle(startPos.x, Game.MAP_SIZE.y - startPos.y, 19, 36);
        texture = assets.manager.get(assets.heroTextureName);
        animation = new PlayerAnimation(texture);
        moveSpeed = defaultMoveSpeed;
        extinguisherRect = new Rectangle();
    }


    public void render(SpriteBatch batch) {
        batch.draw(animation.getCurrentFrame(state, lastDirection, stateTime), rectangle.x, rectangle.y);
        renderExtinguisher(batch);
    }

    public void updateState() {

        switch (state) {
            case DAMAGED:
                if (animation.damagedAnimation.isAnimationFinished(stateTime)) {
                    System.out.println("DAMAGE_ANIMATION_FINISHED!");
                    state = State.MOVE;
                }
                break;
            case MOVE:
                if (direction == Direction.NONE) {
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
        if(bonusTimeRemaining > 0) {
            bonusTimeRemaining -= Gdx.graphics.getDeltaTime();
        }
        else moveSpeed = defaultMoveSpeed;

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
        }
        if(state == State.EXTINGUISH){
            extinguisherRect = getExtinguisherRectangle(solidObjects);
        }

        stateTime += Gdx.graphics.getDeltaTime();
    }

    public void takeDamage(float damage, Assets assets){
        health -= damage;
        state = State.DAMAGED;
        stateTime = 0;
        assets.takeDamageSound.play();
    }

    public void useMedicine(){
        health += MEDICINE_HEALTH;
        if(health > 100){
            health = 100;
        }
    }

    public void activateSpeedBonus(){
        moveSpeed += BONUS_MOVE_SPEED;
        bonusTimeRemaining = BONUS_DURATION;
    }

    void setExtinguisherPosition(){
        Vector2 frameSize = new Vector2(animation.fireExtinguisherAnimation.getKeyFrame(0).getRegionWidth(), animation.fireExtinguisherAnimation.getKeyFrame(0).getRegionHeight());
        switch (lastDirection) {
            case UP:
                extinguisherStartPos = new Vector2(rectangle.x + rectangle.width / 2 + frameSize.x / 2, rectangle.y + rectangle.height);
                break;
            case DOWN:case NONE:
                extinguisherStartPos = new Vector2(rectangle.x + rectangle.width / 2 + frameSize.x / 2, rectangle.y - frameSize.y);
                break;
            case LEFT:
            case UPLEFT:
            case DOWNLEFT:
                extinguisherStartPos =  new Vector2(rectangle.x - frameSize.x, rectangle.y + rectangle.height / 2 - frameSize.y / 2);
                break;
            case RIGHT:
            case UPRIGHT:
            case DOWNRIGHT:
                extinguisherStartPos =  new Vector2(rectangle.x + rectangle.width, rectangle.y + rectangle.height / 2 - frameSize.y / 2);
                break;
        }
    }

    Rectangle getExtinguisherRectangle(MapObjects solid) {
        Vector2 frameSize = new Vector2(animation.fireExtinguisherAnimation.getKeyFrame(0).getRegionWidth(), animation.fireExtinguisherAnimation.getKeyFrame(0).getRegionHeight());
        Vector2 deltaDistance = Entity.calculateDeltaDistance(lastDirection, animation.EXTINGUISHER_SPEED);
        Rectangle resultRect = new Rectangle(extinguisherStartPos.x + deltaDistance.x * animation.extinguishTime,
                                             extinguisherStartPos.y + deltaDistance.y * animation.extinguishTime,
                                             frameSize.x,
                                             frameSize.y);

        if(Entity.IsCollisionWithMap(resultRect, solid)){
            animation.extinguishTime = 0;
            resultRect.setPosition(extinguisherStartPos);
        }
        return resultRect;
    }

    private void renderExtinguisher(SpriteBatch batch){
        if(state == State.EXTINGUISH) {
            batch.draw(animation.updateExtinguisherAnimation(), extinguisherRect.getX(), extinguisherRect.getY());
        }
        else animation.extinguishTime = 0;
    }
}