package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Andrey on 27.10.2016.
 */

public class AxeEnemyAnimation {
    Animation moveUpAnimation;
    Animation moveRightAnimation;
    Animation moveDownAnimation;
    Animation moveLeftAnimation;

    Animation deathAnimation;

    /*
    Animation attackUpAnimation;
    Animation attackRightAnimation;
    Animation attackDownAnimation;
    Animation attackLeftAnimation;
    */
    TextureRegion[] attackTextureRegion;

    TextureRegion currentFrame;

    float stateTime;

    public AxeEnemyAnimation(Texture enemySheet) {

        TextureRegion[][] enemyMoveRegion = new TextureRegion(enemySheet, 0, 0, 84, 124).split(21, 31);
        moveUpAnimation = new Animation(0.5f, enemyMoveRegion[0]);
        moveDownAnimation = new Animation(0.5f, enemyMoveRegion[1]);
        moveLeftAnimation = new Animation(0.5f, enemyMoveRegion[2]);
        moveRightAnimation = new Animation(0.5f, enemyMoveRegion[3]);

        TextureRegion[][] enemyAttackRegion = new TextureRegion(enemySheet, 0, 174, 112, 27).split(28, 27);
        attackTextureRegion = enemyAttackRegion[0];

        //174, 28, 27
        stateTime = 0;

        deathAnimation = new Animation(0.5f, new TextureRegion(enemySheet, 5, 232, 261, 38).split(29, 38)[0]);

        /*
        TextureRegion[][] attackFrames = new TextureRegion(enemySheet, 84, 124).split(enemySheet.getWidth() / 4, enemySheet.getHeight());
        attackUpAnimation = new Animation(0.5f, attackFrames[0][0]);
        attackRightAnimation = new Animation(0.5f, attackFrames[0][1]);
        attackDownAnimation = new Animation(0.5f, attackFrames[0][2]);
        attackLeftAnimation = new Animation(0.5f, attackFrames[0][3]);

        //0 174 112 28

        TextureRegion[][] tmp = new TextureRegion(enemySheet, 0, 174, 112, 28).split(enemySheet.getWidth() / 4, enemySheet.getHeight() / 4);
        TextureRegion[] enemyMoveFrames = new TextureRegion[4 * 4];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                enemyMoveFrames[index++] = tmp[i][j];
            }
        }

        moveUpAnimation = new Animation(0.5f, enemyMoveFrames[0]);
        moveRightAnimation = new Animation(0.5f, enemyMoveFrames[1]);
        moveDownAnimation = new Animation(0.5f, enemyMoveFrames[2]);
        moveLeftAnimation = new Animation(0.5f, enemyMoveFrames[3]);
        */
    }

    void updateFrame(Enemy.State state, Direction direction)
    {
        switch (state) {
            case MOVE:
                currentFrame = getMoveAnimationFrame(direction);
                break;
            case DEAD:
                currentFrame = deathAnimation.getKeyFrame(stateTime, false);
                break;
            case ATTACK:
                currentFrame =getAttackFrame(direction);
                break;
            default:
                break;
        }
    }

    TextureRegion getMoveAnimationFrame(Direction direction){
        switch(direction) {
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

    protected void update(Enemy.State state, Direction direction)
    {
        stateTime += Gdx.graphics.getDeltaTime();
        updateFrame(state, direction);
    }

    public TextureRegion getCurrentFrame()
    {
        return currentFrame;
    }
}