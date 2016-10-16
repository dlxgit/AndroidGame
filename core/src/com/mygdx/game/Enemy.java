package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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
    public final float ATTACK_COOLDOWN = 3.f;
    public final float ATTACK_DAMAGE = 30;

    State state;
    //public static final float MAX_LIVING_TIME = 5.f;
    Texture texture;
    Sprite sprite;
    float livingTime;
    float rotationAngle;

    float attackCooldown;

    EnemyAnimation animation;


    public Enemy(Vector2 position){
        moveSpeed = 3.f;
        texture = new Texture(Gdx.files.internal("images/zombie.png"));
        sprite = new Sprite(texture);
        rectangle = new Rectangle(position.x, position.y, 250,37);
        animation = new EnemyAnimation(texture);
        state = State.SPAWN;
        livingTime = 0;
        //pos = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sprite.setPosition(position.x, position.y);
        health = 100;
        attackCooldown = 0;
    }

    private void updatePosition(){
        //sprite.setPosition((float) (sprite.getX() + (moveSpeed * Math.cos((rotationAngle) * Math.PI / 180))),
        //        (float) (sprite.getY() + (moveSpeed * Math.sin((rotationAngle) * Math.PI / 180))));
        //rectangle.setPosition(rectangle.x + touchPad.getDeltaDistance().x * moveSpeed, rectangle.y + touchPad.getDeltaDistance().y * moveSpeed);
        moveRectangle();
    }

    public void update(Player player) {
        this.livingTime += Gdx.graphics.getDeltaTime();


        updateEnemyDirection(player.rectangle);

        switch(state){
            case SPAWN:
                if(animation.isSpawnAnimationFinished())
                    state = State.MOVE;
                break;
            case MOVE:
                updatePosition();
                break;
            case ATTACK:
                if(attackCooldown > 0){
                    attackCooldown -= Gdx.graphics.getDeltaTime();
                }
                else{
                    //PROCESS DAMAGE
                    player.getDamage(ATTACK_DAMAGE);
                }
                break;
        }

        if (health <= 0) {
            System.out.println("destroy bullet");
        }
        animation.update(state, direction);
    }

    public void render(Batch batch){
        //batch.draw(texture,actorX,actorY);
        //sprite.draw(batch);
        animation.play(state, direction);
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
        if (distance.x < 5 && distance.y < 5)
        {
            direction = Direction.NONE;
        }
        else
        {
            //TODO: check left-right direction zombie sprite bug (almost)
            if ((distance.x > 3 && distance.y > 3) && (distance.x / distance.y > 0.9) && (distance.y / distance.x < 1.1))
            {
                System.out.println("111111111111");
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
                System.out.println("2222222222");
                if (playerRect.x > rectangle.x)
                    direction = Direction.RIGHT;
                else
                    direction = Direction.LEFT;
            }
            else if (distance.x < distance.y)
            {
                System.out.println("33333333");
                if (playerRect.y < rectangle.y)
                    direction = Direction.DOWN;
                else
                    direction = Direction.UP;
            }
        }
        //System.out.println(direction.toString());
    }
}