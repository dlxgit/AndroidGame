package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Andrey on 06.09.2016.
 */
public class Enemy extends Actor {
    public static final float moveSpeed = 5.f;

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
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        health = 100;
    }

    private void updatePosition(){
        sprite.setPosition((float) (sprite.getX() + (moveSpeed * Math.cos((rotationAngle) * Math.PI / 180))),
                (float) (sprite.getY() + (moveSpeed * Math.sin((rotationAngle) * Math.PI / 180))));
    }

    @Override
    public void act(float delta) {
        this.livingTime += Gdx.graphics.getDeltaTime();

        if (health <= 0) {
            System.out.println("destroy bullet");
            this.remove();
        }
        //updatePosition();
    }

    @Override
    public void draw(Batch batch, float alpha){
        //batch.draw(texture,actorX,actorY);
        sprite.draw(batch);
    }
}
