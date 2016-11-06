package com.mygdx.game;

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

    Animation attackUpAnimation;
    Animation attackRightAnimation;
    Animation attackDownAnimation;
    Animation attackLeftAnimation;

    float stateTime;

    public AxeEnemyAnimation(Texture enemySheet) {
        /*
        TextureRegion[] moveUpAnimationSplitted = new TextureRegion[4];
        for(int i = 0; i < 4; i++){
            moveUpAnimationSplitted[i] = new TextureRegion(enemySheet, );
        }
        moveUpAnimation = new Animation(0.5f, moveUpAnimationSplitted);

        TextureRegion[] moveRightAnimationSplitted = new TextureRegion[4];
        for(int i = 0; i < 4; i++){
            moveRightAnimationSplitted[i] = new TextureRegion(enemySheet, );
        }
        moveRightAnimation = new Animation(0.5f, moveRightAnimationSplitted);

        TextureRegion[] moveDownAnimationSplitted = new TextureRegion[4];
        for(int i = 0; i < 4; i++){
            moveDownAnimationSplitted[i] = new TextureRegion(enemySheet, );
        }
        moveDownAnimation = new Animation(0.5f, moveDownAnimationSplitted);

        TextureRegion[] moveLeftAnimationSplitted = new TextureRegion[4];
        for(int i = 0; i < 4; i++){
            moveLeftAnimationSplitted[i] = new TextureRegion(enemySheet, );
        }
        moveLeftAnimation = new Animation(0.5f, moveLeftAnimationSplitted);
*/

/*
        TextureRegion[] attackUpAnimationSplitted = new TextureRegion[2];
        for(int i = 0; i < 2; i++){
            attackUpAnimationSplitted[i] = new TextureRegion(enemySheet, );
        }
        attackUpAnimation = new Animation(0.5f, attackUpAnimationSplitted);

        TextureRegion[] attackRightAnimationSplitted = new TextureRegion[2];
        for(int i = 0; i < 2; i++){
            attackRightAnimationSplitted[i] = new TextureRegion(enemySheet, );
        }
        attackRightAnimation = new Animation(0.5f, attackRightAnimationSplitted);

        TextureRegion[] attackDownAnimationSplitted = new TextureRegion[2];
        for(int i = 0; i < 2; i++){
            attackDownAnimationSplitted[i] = new TextureRegion(enemySheet, );
        }
        attackDownAnimation = new Animation(0.5f, attackDownAnimationSplitted);

        TextureRegion[] attackLeftAnimationSplitted = new TextureRegion[2];
        for(int i = 0; i < 2; i++){
            attackLeftAnimationSplitted[i] = new TextureRegion(enemySheet, );
        }
        attackLeftAnimation = new Animation(0.5f, attackLeftAnimationSplitted);
*/
        //174, 28, 27
        stateTime = 0;

        deathAnimation = new Animation(0.5f, new TextureRegion(enemySheet, 5, 232, 270, 38).split(enemySheet.getWidth() / 4, enemySheet.getHeight())[0]);

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
    }

    public void play(){
        //if()
    }
}
