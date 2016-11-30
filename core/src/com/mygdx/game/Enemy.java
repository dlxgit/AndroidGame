package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

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
        DEAD
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

    public Enemy(){

    }

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

    public void update(Player player) {
        this.livingTime += Gdx.graphics.getDeltaTime();

        State lastState = state;

        switch(state){
            case SPAWN:
                if(animation.isSpawnAnimationFinished()) {
                    direction = Direction.DOWN;
                    state = State.MOVE;
                }
                break;
            case MOVE:
                updateEnemyDirection(player.rectangle);
                updatePosition();
                break;
            case ATTACK:
                updateEnemyDirection(player.rectangle);
                if(attackCooldown > 0){
                    //System.out.println("Enemy attack on cooldown, HERO HEALTH: " + player.health);
                    attackCooldown -= Gdx.graphics.getDeltaTime();
                }
                else{
                    //PROCESS DAMAGE
                    attackCooldown = ATTACK_COOLDOWN;
                    //System.out.println("Enemy attack success");
                    player.getDamage(ATTACK_DAMAGE);
                }
                break;
            case DEAD:
                break;
            default:
                break;
        }

        if (health <= 0) {
            System.out.println("Enemy DEAD");
            state = State.DEAD;
        }

        //System.out.println("Enemy TIME: " + String.valueOf(animation.stateTime));
        if(lastState != state){
            //System.out.println("Reset");
            animation.stateTime = 0;
        }
        animation.update(state, direction);
    }

    public void render(SpriteBatch batch){
        //batch.draw(texture,actorX,actorY);
        //sprite.draw(batch);

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
            state = State.ATTACK;
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
                //System.out.println("111111111111");
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
                //System.out.println("2222222222");
                if (playerRect.x > rectangle.x)
                    direction = Direction.RIGHT;
                else
                    direction = Direction.LEFT;
            }
            else if (distance.x < distance.y)
            {
                //System.out.println("33333333");
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
        //System.out.println(String.valueOf(actionCooldown));
        if(actionCooldown <= 0)
        {
            //System.out.println("COOLDOWN ZERO");
            actionCooldown = ACTION_COOLDOWN;
            return true;
        }
        return false;
    }
    public void setActionCooldown(){
        actionCooldown = ACTION_COOLDOWN;
    }
}