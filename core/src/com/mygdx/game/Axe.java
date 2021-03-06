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
 * Created by Andrey on 16.11.2016.
 */

public class Axe extends Bullet {
    public static final float ROTATE_INTERVAL = 0.2f;
    public static final float MOVE_SPEED = 10;
    private Vector2 step;
    private Sprite sprite;
    private float lastRotateTime;

    public Axe(Assets assets, Rectangle playerRect, Rectangle enemyRect, Direction dir){
        float dx = playerRect.x - enemyRect.x;
        float dy = playerRect.y - enemyRect.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        step = new Vector2((dx/(distance)) * MOVE_SPEED,
                          ((dy/(distance)) * MOVE_SPEED));

        isCollidable = true;
        Texture bulletTexture = assets.manager.get(Assets.axeEnemyTextureName);
        sprite = new Sprite(bulletTexture, 117, 18, 16, 16);
        target = Target.PLAYER;

        attackDamage = 50;
        moveSpeed = 5.f;
        direction = dir;
        rectangle = new Rectangle(enemyRect.x, enemyRect.y, 16, 21);
        livingTime = 0;
        moveSpeed = 8.f;
        isDead = false;
        isCollision = false;
        lastRotateTime = 0;
    }

    @Override
    public void updatePosition(){
        rectangle.x += step.x;
        rectangle.y += step.y;
    }

    @Override
    public void update(MapObjects solidObjects){
        this.livingTime += Gdx.graphics.getDeltaTime();

        if(!isDead) {
            updatePositionByCountingCollision(solidObjects);
            if(isCollision){
                die();
            }
            sprite.setPosition(rectangle.getX(), rectangle.getY());
        }

        sprite.setOrigin(sprite.getX() + sprite.getWidth() / 2,
                         sprite.getY() - sprite.getHeight() / 2);

        if(this.livingTime < lastRotateTime + ROTATE_INTERVAL) {
            sprite.rotate90(false);
            lastRotateTime = livingTime;
        }
    }

    @Override
    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }

    @Override
    public boolean isExploded(){
        return isDead;
    }
}
