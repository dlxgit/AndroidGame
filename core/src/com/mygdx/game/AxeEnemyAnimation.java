package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Andrey on 27.10.2016.
 */

public class AxeEnemyAnimation {
    public static final float FRAME_DURATION = 0.3f;
    Animation moveUpAnimation;
    Animation moveRightAnimation;
    Animation moveDownAnimation;
    Animation moveLeftAnimation;
    Animation deathAnimation;

    TextureRegion[] attackTextureRegion;
    float stateTime;


    public AxeEnemyAnimation(Texture enemySheet) {
        TextureRegion[][] enemyMoveRegion = new TextureRegion(enemySheet, 0, 0, 84, 124).split(21, 31);
        moveUpAnimation = new Animation(FRAME_DURATION, enemyMoveRegion[0]);
        moveDownAnimation = new Animation(FRAME_DURATION, enemyMoveRegion[1]);
        moveLeftAnimation = new Animation(FRAME_DURATION, enemyMoveRegion[2]);
        moveRightAnimation = new Animation(FRAME_DURATION, enemyMoveRegion[3]);

        TextureRegion[][] enemyAttackRegion = new TextureRegion(enemySheet, 0, 174, 112, 27).split(28, 27);
        attackTextureRegion = enemyAttackRegion[0];
        deathAnimation = new Animation(FRAME_DURATION, new TextureRegion(enemySheet, 5, 232, 261, 38).split(29, 38)[0]);
        stateTime = 0;
    }

    TextureRegion getMoveAnimationFrame(Direction direction){
        switch(direction){
            case UP:
                return moveUpAnimation.getKeyFrame(stateTime, true);
            case RIGHT:case UPRIGHT:case DOWNRIGHT:
                return moveRightAnimation.getKeyFrame(stateTime, true);
            case DOWN:
                return moveDownAnimation.getKeyFrame(stateTime, true);
            case LEFT:case UPLEFT:case DOWNLEFT:
                return moveLeftAnimation.getKeyFrame(stateTime, true);
            default:
                break;
        }
        return moveDownAnimation.getKeyFrame(stateTime, true);
    }

    TextureRegion getAttackFrame(Direction direction){
        switch(direction) {
            case UP:
                return attackTextureRegion[1];
            case RIGHT:case UPRIGHT:case DOWNRIGHT:
                return attackTextureRegion[2];
            case DOWN:
                return attackTextureRegion[3];
            case LEFT:case UPLEFT:case DOWNLEFT:
                return attackTextureRegion[0];
            default:
                break;
        }
        return attackTextureRegion[3];
    }

    public void update() {
        stateTime += Gdx.graphics.getDeltaTime();
    }

    public TextureRegion getCurrentFrame(Enemy.State state, Direction direction) {
        switch (state) {
            case MOVE:
                return getMoveAnimationFrame(direction);
            case DEAD:
                return deathAnimation.getKeyFrame(stateTime, false);
            case ATTACK:
                return getAttackFrame(direction);
        }
        return getMoveAnimationFrame(Direction.DOWN);
    }
}