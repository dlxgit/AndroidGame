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
 * Created by Andrey on 07.12.2016.
 */


public class Smoke extends Bullet {
    public static final float DAMAGE = 35;
    public static final Vector2 EXPLOSION_RADIUS = new Vector2(100, 100);

    static Animation animation;
    float stateTime;
    Sprite sprite;

    public Smoke(Assets assets, Rectangle playerRect, Direction playerDirection){
        isCollidable = false;
        attackDamage = 10;
        target = Target.ENEMY;
        livingTime = 0;
        DEATH_TIME = 3;
        Texture playerSheet = assets.manager.get(assets.heroTextureName);
        sprite = new Sprite(playerSheet, 462, 260, 14, 14); //grenade
        //sprite.setOrigin(sprite.getOriginX() + sprite.getWidth() / 2, sprite.getOriginY() + sprite.getHeight() / 2);

        //angle = new Vector2(playerRect.getX() - enemyRect.getX(), playerRect.getY() - enemyRect.getY()).angle();
        moveSpeed = 5.f;

        isDead = true;


    }

    @Override
    public void update(MapObjects solidObjects) {
        livingTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(animation.getKeyFrame(stateTime, true), rectangle.x, rectangle.y);
    }
}

