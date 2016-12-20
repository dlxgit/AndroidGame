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
        ATTACK,
        DEAD,
        EXPLODED
    }

    public final float ATTACK_COOLDOWN = 2.f;
    public final float ATTACK_DAMAGE = 30;
    public final int ACTION_COOLDOWN = 5;

    State state;
    float livingTime;
    float actionCooldown = 1;
    float attackCooldown;
    EnemyAnimation animation;

    Enemy(){};

    public Enemy(Vector2 position, Assets assets){
        moveSpeed = 3.f;
        Texture texture = assets.manager.get(Assets.enemyTextureName);
        rectangle = new Rectangle(position.x, position.y, 27, 49);
        animation = new EnemyAnimation(texture);
        state = State.SPAWN;
        livingTime = 0;
        health = 100;
        attackCooldown = 0;
        direction = Direction.NONE;
    }

    public void update(Player player, MapObjects solidObjects, Assets assets) {

        this.livingTime += Gdx.graphics.getDeltaTime();

        State lastState = state;

        switch(state){
            case SPAWN:
                if(health <= 0) {
                    state = State.DEAD;
                    assets.enemyExplosionSound.play();
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
                break;
            case ATTACK:
                if(health <= 0) {
                    state = State.DEAD;
                }
                updateEnemyDirection(player.rectangle);
                if(attackCooldown > 0){
                    attackCooldown -= Gdx.graphics.getDeltaTime();
                }
                else{  //deal damage

                    attackCooldown = ATTACK_COOLDOWN;
                    player.takeDamage(ATTACK_DAMAGE, assets);
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

        if (player.state == Player.State.EXTINGUISH && player.getExtinguisherRectangle(solidObjects).overlaps(rectangle)) {
            health -= Game.FIRE_EXTINGUISHER_DAMAGE;
        }
        if(lastState != state){
            animation.stateTime = 0;
        }
        animation.update(state, direction);
    }

    public void render(SpriteBatch batch){
        batch.draw(animation.getCurrentFrame(), rectangle.x, rectangle.y);
    }


    private Vector2 calculateDistance(Rectangle playerRect){
        return new Vector2( Math.abs(playerRect.x - rectangle.x),
                            Math.abs(playerRect.y - rectangle.y));
    }

    void updateEnemyDirection(Rectangle playerRect) {
        Vector2 distance = calculateDistance(playerRect);
        if(overlaps(playerRect)){
            if(state == State.ATTACK){
                if(getStateTime() < 0.5) {
                    state = State.ATTACK;
                }
                else state = State.MOVE;
            }
            if(state == State.MOVE){
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

    private boolean overlaps(Rectangle playerRect){
        return rectangle.overlaps(playerRect);
    }

    boolean isAction()
    {
        if(state != State.DEAD && state != State.EXPLODED && actionCooldown <= 0)
        {
            actionCooldown = ACTION_COOLDOWN;
            return true;
        }
        return false;
    }

    void setActionCooldown(){
        actionCooldown = ACTION_COOLDOWN;
    }

    public boolean isDeathAnimationFinished(){
        return animation.stateTime > EnemyAnimation.FRAME_DURATION * EnemyAnimation.DEATH_FRAMES;
    }

    public float getStateTime(){
        return animation.stateTime;
    }

    public static Rectangle calculateSpawnPosition(Rectangle playerRect, MapObjects solidObjects, Random rand) {
        return Entity.calculateObjectSpawnPosition(new Vector2(30,50),
                solidObjects,
                new Rectangle(playerRect.x - Game.ENEMY_SPAWN_RADIUS.x / 2,
                            playerRect.y - Game.ENEMY_SPAWN_RADIUS.y / 2,
                            Game.ENEMY_SPAWN_RADIUS.x,
                            Game.ENEMY_SPAWN_RADIUS.y),
                rand);
    }

    public static Enemy createRandomEnemyNearPlayer(Assets assets, Rectangle playerRect, MapObjects solidObjects, Random rand){
        Vector2 pos = calculateSpawnPosition(playerRect, solidObjects, rand).getPosition(new Vector2());
        if(rand.nextBoolean()){
            return new Enemy(pos, assets);
        }
        else return new AxeEnemy(pos, assets);
    }
}