package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by Andrey on 31.08.2016.
 */

public class Bullet extends Entity {
    enum State{
        INIT,
        MOVE,
        EXPLODE
    }

    public final float MAX_LIVING_TIME = 3.f;

    Texture texture;
    Sprite sprite;
    float livingTime;
    float rotationAngle;

    public Bullet(Vector2 pos, Direction dir, float rotationAngle){
        moveSpeed = 5.f;
        texture = new Texture(Gdx.files.internal("images/bullet.png"));
        this.rotationAngle = rotationAngle;
        direction = dir;
        sprite = new Sprite(texture);
        sprite.setPosition(pos.x, pos.y);
        //sprite.setRotation(rotationAngle);
        rectangle = new Rectangle(pos.x, pos.y, 7,3);
        livingTime = 0;
        moveSpeed = 5.f;
    }

    private void updatePosition(){
        //sprite.setPosition((float)(sprite.getX() + (moveSpeed * Math.cos((rotationAngle) * Math.PI / 180))),
        //                   (float)(sprite.getY() + (moveSpeed * Math.sin((rotationAngle)* Math.PI / 180))));
        moveRectangle();
    }

    public void update(){
        this.livingTime += Gdx.graphics.getDeltaTime();

        if(livingTime > MAX_LIVING_TIME){
            System.out.println("destroy bullet");
        }
        updatePosition();
    }

    public boolean intersects(Vector<Enemy> enemyList){
        for (Enemy enemy : enemyList){
            if(sprite.getBoundingRectangle().overlaps(enemy.sprite.getBoundingRectangle())){
                enemy.state = Enemy.State.DAMAGED;
                return true;
            }
        }
        return false;
    }

    public void render(Batch batch){
        //batch.draw(texture,actorX,actorY);
        sprite.setPosition(rectangle.x, rectangle.y);
        sprite.draw(batch);
        System.out.println(String.valueOf(rectangle.x) + " , " + String.valueOf(rectangle.y));
    }
}