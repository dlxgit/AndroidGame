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
 * Created by Andrey on 06.11.2016.
 */

public class Grenade extends Bullet{
    public final float MOVE_SPEED = 10;
    public final float HERO_DAMAGE = 35;
    float angle;
    float rotationAngle;
    Vector2 startPos;

    static Animation deathAnimation;
    float stateTime;

    public Grenade(Assets assets, Rectangle playerRect, Direction playerDirection){
        isCollidable = false;
        attackDamage = 150;
        target = Target.ENEMY;
        livingTime = 0;
        DEATH_TIME = 3;
        Texture playerSheet = assets.manager.get(assets.heroTextureName);
        sprite = new Sprite(playerSheet, 462, 260, 14, 14); //grenade
        //sprite.setOrigin(sprite.getOriginX() + sprite.getWidth() / 2, sprite.getOriginY() + sprite.getHeight() / 2);

        //angle = new Vector2(playerRect.getX() - enemyRect.getX(), playerRect.getY() - enemyRect.getY()).angle();

        moveSpeed = 5.f;
        rotationAngle = 0;
        direction = playerDirection;
        Vector2 playerCenter = new Vector2();
        playerRect.getCenter(playerCenter);

        int angle = 0;
        if(playerDirection == Direction.UP){
            angle = 90;
        }
        else if (playerDirection == Direction.LEFT){
            angle = 180;
        }
        else if (playerDirection == Direction.DOWN){
            angle = 270;
        }

        rectangle = new Rectangle(playerCenter.x - 16 / 2, playerCenter.y - 21 / 2, 16, 21);
        //rectangle = enemyRect;
        startPos = new Vector2(rectangle.x, rectangle.y);

        isDead = false;
        livingTime = 0;
        moveSpeed = 8.f;


        TextureRegion[][] deathAnimationSplitted = new TextureRegion(playerSheet, 500, 76, 89, 396).split(89, 66);
        TextureRegion[] deathAnimationRegions = new TextureRegion[6];
        for(int i = 0; i < 6; ++i){
            deathAnimationRegions[i] = deathAnimationSplitted[i][0];
        }
        deathAnimation = new Animation(0.5f, deathAnimationRegions);
    }

    @Override
    public void updatePosition(){
        switch (direction){
            case UP:
                rectangle.y += MOVE_SPEED / 4;
                break;
            case DOWN:
                rectangle.y -= MOVE_SPEED / 4;
                break;
            case RIGHT:
                rectangle.y = startPos.y + (float) (- 3 * Math.pow((livingTime - 1.8),2) + 9) * MOVE_SPEED;
                rectangle.x += MOVE_SPEED / 5;
                break;
            case LEFT:
                /*
                rectangle.y += (- Math.pow(-livingTime - 3,2) - 9) * MOVE_SPEED;
                rectangle.x -= MOVE_SPEED;
                */
                rectangle.y = startPos.y + (float) (- 3 * Math.pow((livingTime - 1.8),2) + 9) * MOVE_SPEED;
                rectangle.x -= MOVE_SPEED / 5;
                break;
        }
    }

    @Override
    public void update(){
        if(!isDead) {
            updatePosition();
            System.out.println(String.valueOf(rectangle.y) + " " + String.valueOf(startPos.y));
            if(livingTime > 2){
                System.out.println("Grenade death");
                //die();
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
        if(!isDead){
            System.out.println("Throw Grenade");
            sprite.draw(batch);
        }
        else {
            System.out.println("Grenade Explosion");
            batch.draw(deathAnimation.getKeyFrame(stateTime), rectangle.x, rectangle.y);
        }
    }

    static float calculateAngle(Rectangle rect1, Rectangle rect2){
        return new Vector2(rect1.getX() - rect2.getX(), rect1.getY() - rect2.getY()).angle();
    }

    public boolean isKillingEnemy(Rectangle enemyRect){
        if (enemyRect.overlaps(new Rectangle(rectangle.x + rectangle.width / 2 - 100, rectangle.y + rectangle.height / 2 + 60, 200, 120))){
            return true;
        }
        return false;
    }
}
