package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Andrey on 06.09.2016.
 */
public class Enemy {
    enum State{
        INIT,
        MOVE,
        STAY,
        ATTACK,
        DAMAGED,
        CAST,
        DEAD
    }

    Direction direction;
    State state;
    public static final float moveSpeed = 5.f;
    //public static final float MAX_LIVING_TIME = 5.f;
    Texture texture;
    Sprite sprite;
    float livingTime;
    //Vector2 pos;
    float rotationAngle;
    int health;


    public Enemy(Vector2 position, int enemyCount){
        texture = new Texture(Gdx.files.internal("images/zloyvrag.png"));
        sprite = new Sprite(texture);
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
    }

    public void draw(Batch batch){
        //batch.draw(texture,actorX,actorY);
        sprite.draw(batch);
    }
}
