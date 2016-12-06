package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
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
    enum Target{
        PLAYER,
        ENEMY
    }

    public final float MAX_LIVING_TIME = 3.f;
    public float DEATH_TIME = 1.f;
    public float attackDamage;

    Texture texture;
    float livingTime;
    float rotationAngle;
    boolean isDead = false;
    boolean isCollidable;
    Animation deathAnimation;
    Sprite sprite;

    Target target;

    Bullet(){};

    Bullet(Assets assets, Rectangle playerRect, Direction dir){
        System.out.println("BulletAdd");
        Texture playerSheet = assets.manager.get(assets.heroTextureName);

        TextureRegion bulletAnimationRegion = new TextureRegion(playerSheet, 322, 510, 32, 21);
        TextureRegion[][] bulletAnimationSplitted = bulletAnimationRegion.split(16, 21);
        deathAnimation = new Animation(0.1f, bulletAnimationSplitted[0]);

        sprite = new Sprite(new TextureRegion(playerSheet, 213, 158, 14, 14));

        target = Target.ENEMY;
        attackDamage = 100;
        moveSpeed = 5.f;
        //this.rotationAngle = rotationAngle;
        direction = dir;
        Vector2 playerCenter = new Vector2();
        playerRect.getCenter(playerCenter);
        isCollidable = true;
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
        sprite.setRotation(angle);
        rectangle = new Rectangle(playerCenter.x - 16 / 2, playerCenter.y / 2 - 21 / 2, 16, 21);

        livingTime = 0;
        moveSpeed = 8.f;
        isCollision = false;
    }

    protected void updatePosition(){
        //moveRectangle();

        sprite.setPosition(rectangle.getX(), rectangle.getY());
    }

    public void update(MapObjects solidObjects){
        if(!isDead) {
            updatePositionByCountingCollision(solidObjects);
            //updatePosition();
        }
        this.livingTime += Gdx.graphics.getDeltaTime();
        //System.out.println("Bullet " + rectangle.x + " " + rectangle.y + " " + direction);
    }

    public TextureRegion getFrame() {
        return deathAnimation.getKeyFrame(livingTime, false);
    }

    void render(SpriteBatch batch){
        //batch.draw(texture,actorX,actorY);
        //animation.(batch);
        //System.out.println(String.valueOf(rectangle.x) + " , " + String.valueOf(rectangle.y));
        if(isDead){
            batch.draw(getFrame(), rectangle.getX(), rectangle.getY());
        }
        else{
            sprite.draw(batch);
        }
    }

    public boolean isDead(){
        return isDead;
    }

    void die(){
        isDead = true;
        livingTime = 0;
    }

    final Target getTarget()  {
        return target;
    }

    boolean isExploded(){
        return (!isDead && livingTime > MAX_LIVING_TIME) ||
                (isDead && livingTime > DEATH_TIME);
    }

    boolean isCollisionWithTarget(Rectangle targetRect){
        if (rectangle.overlaps(targetRect)){
            return true;
        }
        return false;
    }
}