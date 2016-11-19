package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    public final float DEATH_TIME = 1.f;

    Texture texture;
    float livingTime;
    float rotationAngle;

    boolean isDead = false;

    Animation deathAnimation;
    Sprite sprite;

    Bullet(){};

    Bullet(Assets assets, Rectangle playerRect, Direction dir){
        Texture bulletTexture = assets.manager.get(assets.bulletTextureName);
        TextureRegion bulletAnimationRegion = new TextureRegion(bulletTexture, 322, 510, 32, 21);
        TextureRegion[][] bulletAnimationSplitted = bulletAnimationRegion.split(16, 21);
        deathAnimation = new Animation(0.1f, bulletAnimationSplitted[0]);

        moveSpeed = 5.f;
        //this.rotationAngle = rotationAngle;
        direction = dir;
        Vector2 playerCenter = new Vector2();
        playerRect.getCenter(playerCenter);

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
        rectangle = new Rectangle(playerCenter.x - 16 / 2, playerCenter.y / 2 - 21 / 2, 16, 21);

        livingTime = 0;
        moveSpeed = 8.f;
    }

    protected void updatePosition(){
        //sprite.setPosition((float)(sprite.getX() + (moveSpeed * Math.cos((rotationAngle) * Math.PI / 180))),
        //                   (float)(sprite.getY() + (moveSpeed * Math.sin((rotationAngle)* Math.PI / 180))));
        moveRectangle();
        //sprite.setPosition(rectangle.getX(), rectangle.getY());
    }

    public void update(){
        if(!isDead) {
            updatePosition();
        }
        this.livingTime += Gdx.graphics.getDeltaTime();
        System.out.println("Bullet " + rectangle.x + " " + rectangle.y + " " + direction);
    }
/*
public TextureRegion getFrame(){
    if(isDead){
        return deathAnimation.getKeyFrame(deathTime, false);
    }

}*/

    void render(SpriteBatch batch){
        //batch.draw(texture,actorX,actorY);
        //sprite.setPosition(rectangle.getX(), rectangle.getY());
        //animation.(batch);
        //System.out.println(String.valueOf(rectangle.x) + " , " + String.valueOf(rectangle.y));
        if(isDead){

        }
    }

    public boolean isDead(){
        return isDead;
    }

    void die(){
        isDead = true;
        livingTime = 0;
    }

}