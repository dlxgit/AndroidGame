package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Andrey on 06.09.2016.
 */
public class Enemy extends Entity {
    enum State{
        SPAWN,
        MOVE,
        STAY,
        ATTACK,
        //DAMAGED,
        CAST,
        DEAD,
        EXPLODED
    }

    public final float VISION_DISTANCE = 300.f;
    public final float ATTACK_COOLDOWN = 2.f;
    public final float ATTACK_DAMAGE = 30;
    public final int ACTION_COOLDOWN = 5;

    State state;
    //public static final float MAX_LIVING_TIME = 5.f;
    //Texture texture;
    //Sprite sprite;
    float livingTime;
    public float actionCooldown = 1;

    float attackCooldown;

    EnemyAnimation animation;

    Enemy(){

    };

    public Enemy(Vector2 position, Assets assets){
        moveSpeed = 3.f;
        Texture texture = assets.manager.get(assets.enemyTextureName);
        //sprite = new Sprite(texture);
        rectangle = new Rectangle(position.x, position.y, 27, 49);
        animation = new EnemyAnimation(texture);
        state = State.SPAWN;
        livingTime = 0;
        //pos = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //sprite.setPosition(position.x, position.y);
        health = 100;
        attackCooldown = 0;
        direction = Direction.NONE;
    }

    protected void updatePosition(){
        //sprite.setPosition((float) (sprite.getX() + (moveSpeed * Math.cos((rotationAngle) * Math.PI / 180))),
        //        (float) (sprite.getY() + (moveSpeed * Math.sin((rotationAngle) * Math.PI / 180))));
        //rectangle.setPosition(rectangle.x + touchPad.getDeltaDistance().x * moveSpeed, rectangle.y + touchPad.getDeltaDistance().y * moveSpeed);
        moveRectangle();
    }

    public void update(Player player, MapObjects solidObjects) {

        this.livingTime += Gdx.graphics.getDeltaTime();

        State lastState = state;

        switch(state){
            case SPAWN:
                if(health <= 0) {
                    state = State.DEAD;
                }
                if(animation.isSpawnAnimationFinished()) {
                    direction = Direction.DOWN;
                    state = State.MOVE;
                }
                break;
            case MOVE:
                if(health <= 0) {
                    state = State.DEAD;
                }
                updateEnemyDirection(player.rectangle);
                updatePositionByCountingCollision(solidObjects);
                //updatePosition();
                break;
            case ATTACK:
                if(health <= 0) {
                    state = State.DEAD;
                }
                updateEnemyDirection(player.rectangle);
                if(attackCooldown > 0){
                    attackCooldown -= Gdx.graphics.getDeltaTime();
                }
                else{
                    //PROCESS DAMAGE
                    attackCooldown = ATTACK_COOLDOWN;
                    //System.out.println("Enemy attack success");
                    player.takeDamage(ATTACK_DAMAGE);
                }
                break;
            case DEAD:
                if(isDeathAnimationFinished()){
                    state = State.EXPLODED;
                }
                break;

            default:
                break;
        }

        if(lastState != state){
            animation.stateTime = 0;
        }
        animation.update(state, direction);
    }

    public void render(SpriteBatch batch){
        batch.draw(animation.getCurrentFrame(), rectangle.x, rectangle.y);
    }


    private boolean isNearPlayer(Rectangle playerRect){
        Vector2 distance = calculateDistance(playerRect);
        if(distance.x < VISION_DISTANCE && distance.y < VISION_DISTANCE) {
            return true;
        }
        return false;
    }

    private void follow(Rectangle playerRect){

    }

    Vector2 calculateDistance(Rectangle playerRect){
        Vector2 distance = new Vector2( Math.abs(playerRect.x - rectangle.x),
                Math.abs(playerRect.y - rectangle.y));
        return distance;
    }

    void updateEnemyDirection(Rectangle playerRect) {
        //calculate distance and direction
        Vector2 distance = calculateDistance(playerRect);
        if(overlaps(playerRect)){
            if(state == state.ATTACK){
                if(getStateTime() < 0.5) {
                    state = State.ATTACK;
                }
                else state = State.MOVE;
            }
            if(state == state.MOVE){
                state = State.ATTACK;
            }
        }
        else{
            if(state == State.ATTACK){
                state = State.MOVE;
            }
        }

        if (distance.x < 5 && distance.y < 5)
        {
            direction = Direction.NONE;
        }
        else
        {
            //TODO: check left-right direction zombie sprite bug (almost)
            if ((distance.x > 3 && distance.y > 3) && (distance.x / distance.y > 0.9) && (distance.y / distance.x < 1.1))
            {
                if (playerRect.x >= rectangle.x && playerRect.y <= rectangle.y)
                    direction = Direction.DOWNRIGHT;
                else if (playerRect.x >= rectangle.x && playerRect.y > rectangle.y)
                    direction = Direction.UPRIGHT;
                else if (playerRect.x < rectangle.x && playerRect.y <= rectangle.y)
                    direction = Direction.DOWNLEFT;
                else if (playerRect.x < rectangle.x && playerRect.y > rectangle.y)
                    direction = Direction.UPLEFT;
            }
            else if (distance.x >= distance.y)
            {
                if (playerRect.x > rectangle.x)
                    direction = Direction.RIGHT;
                else
                    direction = Direction.LEFT;
            }
            else if (distance.x < distance.y)
            {
                if (playerRect.y < rectangle.y)
                    direction = Direction.DOWN;
                else
                    direction = Direction.UP;
            }
        }
    }

    public boolean overlaps(Rectangle playerRect){
        if(rectangle.overlaps(playerRect)){
            return true;
        }
        return false;
    }

    public boolean isAction()
    {
        if(state != State.DEAD && actionCooldown <= 0)
        {
            actionCooldown = ACTION_COOLDOWN;
            return true;
        }
        return false;
    }
    public void setActionCooldown(){
        actionCooldown = ACTION_COOLDOWN;
    }

    public boolean isDeathAnimationFinished(){
        return animation.stateTime > animation.FRAME_DURATION * animation.DEATH_FRAMES;
    }

    public float getStateTime(){
        return animation.stateTime;
    }

    public void setStateTime(float time){
        animation.stateTime = time;
    }

    static Rectangle calculateSpawnPosition(Rectangle playerRect, MapObjects solidObjects, Random rand) {
        return Entity.calculateObjectSpawnPosition(new Vector2(30,50),
                solidObjects,
                new Rectangle(playerRect.x - Game.ENEMY_SPAWN_RADIUS.x / 2,
                            playerRect.y - Game.ENEMY_SPAWN_RADIUS.y / 2,
                            Game.ENEMY_SPAWN_RADIUS.x,
                            Game.ENEMY_SPAWN_RADIUS.y),
                rand);
    }

    static Enemy createRandomEnemyNearPlayer(Assets assets, Rectangle playerRect, MapObjects solidObjects, Random rand){
        Vector2 pos = calculateSpawnPosition(playerRect, solidObjects, rand).getPosition(new Vector2());
        if(rand.nextBoolean()){
            return new Enemy(pos, assets);
        }
        else return new AxeEnemy(pos, assets);
    }
}