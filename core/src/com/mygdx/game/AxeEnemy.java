package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Andrey on 26.10.2016.
 */

public class AxeEnemy extends Enemy{

    AxeEnemyAnimation animation;



    public AxeEnemy(Vector2 position, Assets assets){
        state = State.MOVE;
        direction = Direction.DOWN;
        moveSpeed = 3.f;
        rectangle = new Rectangle(position.x, position.y, 18, 30);
        livingTime = 0;
        //pos = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //sprite.setPosition(position.x, position.y);
        health = 100;
        attackCooldown = 0;
        Texture texture = assets.manager.get(assets.axeEnemyTextureName);
        animation = new AxeEnemyAnimation(texture);
        actionCooldown = 2;
    }

    @Override
    public void update(Player player, MapObjects solidObjects) {
        this.livingTime += Gdx.graphics.getDeltaTime();
        System.out.println("Health: " + health);
        State lastState = state;



        switch(state){
            case MOVE:
                updateEnemyDirection(player.rectangle);
                updatePositionByCountingCollision(solidObjects);
                //updatePosition();
                break;
            case ATTACK:
                updateEnemyDirection(player.rectangle);
                if(attackCooldown > 0){
                    //System.out.println("Enemy attack on cooldown, HERO HEALTH: " + player.health);
                    attackCooldown -= Gdx.graphics.getDeltaTime();
                }
                else{
                    //PROCESS DAMAGE
                    attackCooldown = ATTACK_COOLDOWN;
                    //System.out.println("Enemy attack success");
                    player.getDamage(ATTACK_DAMAGE);
                }
                break;
            case DEAD:
                if(isDeathAnimationFinished()){
                    state = State.EXPLODED;
                }
                break;
            default:
                break;
        }

        if(isDying()){ //if is now dying
            System.out.println("Enemy DEAD");
            state = State.DEAD;
        }

        //System.out.println("Enemy TIME: " + String.valueOf(animation.stateTime));
        if(lastState != state){
            System.out.println("Reset");
            animation.stateTime = 0;
        }
        animation.update(state, direction);
        actionCooldown -= Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(animation.getCurrentFrame(), rectangle.getX(), rectangle.getY());
    }

    @Override
    public boolean isDeathAnimationFinished(){
        return state == State.DEAD && animation.deathAnimation.getKeyFrameIndex(animation.stateTime) == animation.deathAnimation.getKeyFrames().length - 1;

        //if(state == State.DEAD && animation.stateTime > animation.deathAnimation.getFrameDuration() * animation.deathAnimation.getKeyFrameIndex())
    }
}
