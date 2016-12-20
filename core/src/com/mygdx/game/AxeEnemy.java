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
        health = 100;
        attackCooldown = 0;
        Texture texture = assets.manager.get(assets.axeEnemyTextureName);
        animation = new AxeEnemyAnimation(texture);
        actionCooldown = 2;
    }

    @Override
    public void update(Player player, MapObjects solidObjects, Assets assets) {
        this.livingTime += Gdx.graphics.getDeltaTime();
        State lastState = state;

        switch(state){
            case MOVE:
                if(health <= 0) {
                    state = State.DEAD;
                }
                updateEnemyDirection(player.rectangle);
                updatePositionByCountingCollision(solidObjects);

                break;
            case ATTACK:
                if(health <= 0) {
                    state = State.DEAD;
                }
                updateEnemyDirection(player.rectangle);
                if(attackCooldown > 0){
                    attackCooldown -= Gdx.graphics.getDeltaTime();
                }
                else{   //PROCESS DAMAGE
                    attackCooldown = ATTACK_COOLDOWN;
                    player.takeDamage(ATTACK_DAMAGE, assets);
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

        if (player.state == Player.State.EXTINGUISH && player.getExtinguisherRectangle(solidObjects).overlaps(rectangle)) {
            health -= Game.FIRE_EXTINGUISHER_DAMAGE;
        }
        if(lastState != state){

            animation.stateTime = 0;
        }
        animation.update();
        actionCooldown -= Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch batch){
        if(state == State.ATTACK && attackCooldown > 1.5 || state != State.ATTACK){
                batch.draw(animation.getCurrentFrame(state, direction), rectangle.getX(), rectangle.getY());
        }
        else batch.draw(animation.getCurrentFrame(state.MOVE, direction), rectangle.getX(), rectangle.getY());
    }

    @Override
    public boolean isDeathAnimationFinished(){
        return animation.deathAnimation.getKeyFrameIndex(animation.stateTime) == animation.deathAnimation.getKeyFrames().length - 1;
    }

    @Override
    public float getStateTime(){
        return animation.stateTime;
    }
}