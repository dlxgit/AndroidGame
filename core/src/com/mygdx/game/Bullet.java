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

    public Bullet(Rectangle playerRect, Direction dir){
        moveSpeed = 5.f;
        texture = new Texture(Gdx.files.internal("images/bullet.png"));
        //this.rotationAngle = rotationAngle;
        direction = dir;
        sprite = new Sprite(texture);
        Vector2 playerCenter = new Vector2();
        playerRect.getCenter(playerCenter);
        sprite.setPosition(playerCenter.x - sprite.getWidth() / 2, playerCenter.y - sprite.getHeight() / 2);

        int angle = 0;
        if(dir == Direction.UP){
            angle = 90;
        }
        else if (dir == Direction.LEFT){
            angle = 180;
        }
        else if (dir == Direction.DOWN){
            angle = 270;
        }
        rectangle = sprite.getBoundingRectangle();
        //sprite.setOriginCenter();
        sprite.setRotation(angle);

        //rectangle = new Rectangle(sprite.get)
        //rectangle = new Rectangle(pos.x, pos.y, 7,3);
        //rectangle = sprite.getBoundingRectangle();

        livingTime = 0;
        moveSpeed = 8.f;
    }

    private void updatePosition(){
        //sprite.setPosition((float)(sprite.getX() + (moveSpeed * Math.cos((rotationAngle) * Math.PI / 180))),
        //                   (float)(sprite.getY() + (moveSpeed * Math.sin((rotationAngle)* Math.PI / 180))));
        moveRectangle();
        sprite.setPosition(rectangle.getX(), rectangle.getY());
    }

    public void update(){
        this.livingTime += Gdx.graphics.getDeltaTime();

        updatePosition();
        System.out.println("Bullet " + rectangle.x + " " + rectangle.y + " " + direction);
    }



    public void render(Batch batch){
        //batch.draw(texture,actorX,actorY);
        //sprite.setPosition(rectangle.getX(), rectangle.getY());
        sprite.draw(batch);
        //System.out.println(String.valueOf(rectangle.x) + " , " + String.valueOf(rectangle.y));
    }
}