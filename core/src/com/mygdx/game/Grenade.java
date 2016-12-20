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
 * Created by Andrey on 06.11.2016.
 */

public class Grenade extends Bullet{
    public static final float MOVE_SPEED = 15;
    public static final Vector2 EXPLOSION_RADIUS = new Vector2(100, 100);
    float rotationAngle;
    Vector2 startPos;
    Sprite sprite;

    static Animation deathAnimation;
    float stateTime;

    public Grenade(Assets assets, Vector2 playerCenter, Direction playerDirection){
        isCollidable = false;
        attackDamage = 150;
        target = Target.ENEMY;
        livingTime = 0;
        DEATH_TIME = 3;
        Texture playerSheet = assets.manager.get(assets.heroTextureName);
        sprite = new Sprite(playerSheet, 462, 260, 14, 14); //grenade

        moveSpeed = 5.f;
        rotationAngle = 0;
        direction = playerDirection;

        rectangle = new Rectangle(playerCenter.x +  - 16 / 2, playerCenter.y - 21 / 2, 16, 21);
        startPos = new Vector2(rectangle.x, rectangle.y);
        isDead = false;
        livingTime = 0;
        moveSpeed = 8.f;

        TextureRegion[][] deathAnimationSplitted = new TextureRegion(playerSheet, 500, 76, 89, 396).split(89, 66);
        TextureRegion[] deathAnimationRegions = new TextureRegion[6];
        for(int i = 0; i < 6; ++i){
            deathAnimationRegions[i] = deathAnimationSplitted[i][0];
        }
        deathAnimation = new Animation(0.2f, deathAnimationRegions);
    }

    @Override
    public void updatePosition(){
        switch (direction){
            case UP:
                rectangle.y += MOVE_SPEED / 10;
                break;
            case DOWN:
                rectangle.y -= MOVE_SPEED / 10;
                break;
            case RIGHT:
                rectangle.y = startPos.y + (float) (- 3 * Math.pow((livingTime * 2 - 1.8),2) + 9) * MOVE_SPEED;
                rectangle.x += MOVE_SPEED / 5;
                break;
            case LEFT:
                rectangle.y = startPos.y + (float) (- 3 * Math.pow((livingTime * 2 - 1.8),2) + 9) * MOVE_SPEED;
                rectangle.x -= MOVE_SPEED / 5;
                break;
        }
    }

    @Override
    public void update(MapObjects solidObjects){
        if(!isDead) {
            updatePosition();
            System.out.println(String.valueOf(rectangle.y) + " " + String.valueOf(startPos.y));
            if(livingTime > 1.65){
                die();
                rectangle = new Rectangle(rectangle.getX() - EXPLOSION_RADIUS.x / 2,
                                          rectangle.getY() - EXPLOSION_RADIUS.y / 2,
                                          EXPLOSION_RADIUS.x,
                                          EXPLOSION_RADIUS.y);
            }
            sprite.setPosition(rectangle.getX(), rectangle.getY());
        }
        else{
            stateTime += Gdx.graphics.getDeltaTime();
        }

        livingTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch batch){
        if(!isDead){ //draw grenade
            sprite.draw(batch);
        }
        else { //draw explosion
            batch.draw(deathAnimation.getKeyFrame(stateTime), rectangle.x, rectangle.y);
        }
    }
}
