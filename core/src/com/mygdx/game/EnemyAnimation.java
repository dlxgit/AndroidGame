package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Andrey on 13.09.2016.
 */
public class EnemyAnimation {
    Animation spawnAnimation;
    Animation deathAnimation;

    Animation moveUpAnimation;
    Animation moveRightAnimation;
    Animation moveDownAnimation;
    Animation moveLeftAnimation;

    float stateTime;
    TextureRegion currentFrame;

    public EnemyAnimation(Texture enemySheet){

        //50 * int(enemy.currentFrame), 12, 33, 51

        TextureRegion spawnAnimationRegion = new TextureRegion(enemySheet, 0, 84, 250, 37);
        TextureRegion[][] spawnAnimationSplitted = spawnAnimationRegion.split(50, 37);
        //System.out.print(moveUpSplitted.length());
        spawnAnimation = new Animation(0.0005f, spawnAnimationSplitted[0]);
        stateTime = 0f;
        currentFrame = new TextureRegion();
    }

    public void update(Enemy.State state, Direction direction) {
        stateTime += Gdx.graphics.getDeltaTime();
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }


    public void play(Enemy.State state, Direction direction) {
        if (state == Enemy.State.SPAWN) {
            currentFrame = spawnAnimation.getKeyFrame(stateTime, true);
            return;
        }

        if (state == Enemy.State.MOVE) {
            System.out.println("MOVEEEEEE");
            switch(direction) {
                case UP:
                    currentFrame = moveUpAnimation.getKeyFrame(stateTime, true);
                    break;
                case RIGHT:case UPRIGHT:case DOWNRIGHT:
                    currentFrame = moveRightAnimation.getKeyFrame(stateTime, true);
                    break;
                case DOWN:
                    currentFrame = moveDownAnimation.getKeyFrame(stateTime, true);
                    break;
                case LEFT:case UPLEFT:case DOWNLEFT:
                    currentFrame = moveLeftAnimation.getKeyFrame(stateTime, true);
                    break;
            }
            return;
        }
    }
}
