package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Andrey on 31.08.2016.
 */
public class Bullet extends Actor {
    public static final float MAX_LIVING_TIME = 3.f;
    public static final float moveSpeed = 5.f;

    Texture texture;
    Sprite sprite;
    float livingTime;
    Vector2 pos;
    float rotationAngle;

    public Bullet(Vector2 pos, float rotationAngle){
        texture = new Texture(Gdx.files.internal("images/shot.png"));
        this.pos = pos;
        this.rotationAngle = rotationAngle;
        sprite = new Sprite(texture);
        sprite.setPosition(pos.x, pos.y);
        sprite.setRotation(rotationAngle);
        livingTime = 0;
    }

    private void updatePosition(){
        sprite.setPosition((float)(sprite.getX() + (moveSpeed * Math.cos((rotationAngle) * Math.PI / 180))),
                           (float)(sprite.getY() + (moveSpeed * Math.sin((rotationAngle)* Math.PI / 180))));
    }

    @Override
    public void act(float delta){
        this.livingTime += Gdx.graphics.getDeltaTime();

        if(livingTime > MAX_LIVING_TIME){
            System.out.println("destroy bullet");
            this.remove();
        }
        updatePosition();
    }

    @Override
    public void draw(Batch batch, float alpha){
        //batch.draw(texture,actorX,actorY);
        sprite.draw(batch);
    }
}
