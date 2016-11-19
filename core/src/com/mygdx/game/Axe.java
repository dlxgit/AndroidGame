package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Andrey on 16.11.2016.
 */

public class Axe extends Bullet {

    public final float MOVE_SPEED = 10;
    public final float HERO_DAMAGE = 35;
    float angle;
    float rotationAngle;
    public Axe(Assets assets, Rectangle playerRect, Rectangle enemyRect, Direction dir){
        Texture bulletTexture = assets.manager.get(assets.axeEnemyTextureName);
        sprite = new Sprite(bulletTexture, 117, 18, 16, 16);
        sprite.setOrigin(sprite.getOriginX() + sprite.getWidth() / 2, sprite.getOriginY() + sprite.getHeight() / 2);

        angle = new Vector2(playerRect.getX() - enemyRect.getX(), playerRect.getY() - enemyRect.getY()).angle();

        moveSpeed = 5.f;
        rotationAngle = 0;
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
        //rectangle = enemyRect;

        livingTime = 0;
        moveSpeed = 8.f;
    }

    @Override
    public void updatePosition(){
        rectangle.x += MOVE_SPEED * Math.cos(angle);
        rectangle.y += MOVE_SPEED * Math.sin(angle);
    }

    @Override
    public void update(){
        if(!isDead) {
            updatePosition();
            sprite.setPosition(rectangle.getX(), rectangle.getY());
            sprite.rotate90(true);
        }
        this.livingTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch batch){
        //sprite.draw(batch);
    }

    static float calculateAngle(Rectangle rect1, Rectangle rect2){
        return new Vector2(rect1.getX() - rect2.getX(), rect1.getY() - rect2.getY()).angle();
    }
}
