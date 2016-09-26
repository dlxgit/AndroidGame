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
public class Enemy {
    enum State{
        SPAWN,
        MOVE,
        STAY,
        ATTACK,
        DAMAGED,
        CAST,
        DEAD
    }

    public final float VISION_DISTANCE = 300.f;

    Direction direction;
    State state;
    public static final float moveSpeed = 5.f;
    //public static final float MAX_LIVING_TIME = 5.f;
    Texture texture;
    Sprite sprite;
    float livingTime;
    //Vector2 pos;
    float rotationAngle;

    EnemyAnimation animation;
    Rectangle rectangle;

    int health;


    public Enemy(Vector2 position, int enemyCount){
        texture = new Texture(Gdx.files.internal("images/zombie.png"));
        sprite = new Sprite(texture);
        animation = new EnemyAnimation(texture);
        state = State.SPAWN;
        livingTime = 0;
        //pos = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sprite.setPosition(position.x, position.y);
        health = 100;
    }

    private void updatePosition(){
        sprite.setPosition((float) (sprite.getX() + (moveSpeed * Math.cos((rotationAngle) * Math.PI / 180))),
                (float) (sprite.getY() + (moveSpeed * Math.sin((rotationAngle) * Math.PI / 180))));
    }

    public void update() {
        this.livingTime += Gdx.graphics.getDeltaTime();

        if (health <= 0) {
            System.out.println("destroy bullet");
        }
        updatePosition();
        animation.update(state, direction);
    }

    public void draw(Batch batch){
        //batch.draw(texture,actorX,actorY);

        //sprite.draw(batch);
        animation.play(state, direction);
        batch.draw(animation.getCurrentFrame(),  sprite.getX(), sprite.getY());
    }

    private boolean isNearPlayer(Rectangle playerRect){
        if(Math.abs(playerRect.x - rectangle.x) < VISION_DISTANCE && Math.abs(playerRect.y - rectangle.y) < VISION_DISTANCE) {
            return true;
        }
        return false;
    }

    private void follow(Rectangle playerRect){

    }

    void ComputeEnemyDirection(Rectangle playerRect)
    {
        //compute distance and dir
        Vector2 distance = new Vector2( Math.abs(playerRect.x - rectangle.x),
                                        Math.abs(playerRect.y - rectangle.y));
        if (distance.x > 5 || distance.y > 5)
        {
            //TODO: check left-right dir zombie sprite bug (almost)
            if ((distance.x > 3 && distance.y > 3) && (distance.x / distance.y > 0.9) && (distance.y / distance.x < 1.1))
            {
                if (playerRect.x >= rectangle.x && playerRect.y >= rectangle.y)
                    direction = Direction.DOWNRIGHT;
                else if (playerRect.x >= rectangle.x && playerRect.y < rectangle.y)
                    direction = Direction.UPRIGHT;
                else if (playerRect.x < rectangle.x && playerRect.y >= rectangle.y)
                    direction = Direction.DOWNLEFT;
                else if (playerRect.x < rectangle.x && playerRect.y < rectangle.y)
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
                if (playerRect.y > rectangle.y)
                    direction = Direction.DOWN;
                else
                    direction = Direction.UP;
            }
        }
    }

    private void move(){
        switch(direction){

        }
    }

}
