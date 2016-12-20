package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Andrey on 31.08.2016.
 */

public class Bullet extends Entity {
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

    Bullet(){

    };

    Bullet(Assets assets, Vector2 playerCenter, Direction dir){
        Texture playerSheet = assets.manager.get(assets.bulletTextureName);
        TextureRegion bulletAnimationRegion = new TextureRegion(playerSheet, 0, 0, 96, 21);
        TextureRegion[][] bulletAnimationSplitted = bulletAnimationRegion.split(16, 21);
        deathAnimation = new Animation(0.1f, new TextureRegion[]{bulletAnimationSplitted[0][4], bulletAnimationSplitted[0][5]});
        sprite = new Sprite(new TextureRegion(bulletAnimationRegion, 16 * Direction.getSide(dir), 0, 16, 21));

        target = Target.ENEMY;
        attackDamage = 100;
        moveSpeed = 15.f;
        //this.rotationAngle = rotationAngle;
        direction = dir;

        isCollidable = true;

        rectangle = new Rectangle(playerCenter.x - 16 / 2, playerCenter.y - 21 / 2, 16, 21);

        livingTime = 0;

        isCollision = false;
    }

    protected void updatePosition(){
        moveRectangle();
    }

    public void update(MapObjects solidObjects){
        if(!isDead) {
            updatePositionByCountingCollision(solidObjects);
            //updatePosition();
        }
        this.livingTime =  livingTime + Gdx.graphics.getDeltaTime();
        //System.out.println("Bullet " + rectangle.x + " " + rectangle.y + " " + direction);
    }

    public TextureRegion getFrame() {
        return deathAnimation.getKeyFrame(livingTime, false);
    }

    void render(SpriteBatch batch){
        if(isDead){
            batch.draw(getFrame(), rectangle.getX(), rectangle.getY());
        }
        else{
            sprite.setPosition(rectangle.getX(), rectangle.getY());
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
//        if(isDead){
//            //System.out.println("BulletDeadTime " + livingTime + " " + DEATH_TIME);
//        }
        return (!isDead && livingTime > MAX_LIVING_TIME) ||
                (isDead && livingTime > DEATH_TIME);
    }

    boolean isCollisionWithTarget(Rectangle targetRect){
        return rectangle.overlaps(targetRect);
    }
}