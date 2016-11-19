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


    final int DEATH_FRAMES = 5;
    final float FRAME_DURATION = 0.5f;

    public EnemyAnimation(Texture enemySheet){

        //50 * int(enemy.currentFrame), 12, 33, 51

        TextureRegion[] moveUpAnimationSplitted = new TextureRegion[4];
        for(int i = 0; i < 4; i++){
            moveUpAnimationSplitted[i] = new TextureRegion(enemySheet, 15 + 36 * i, 84 + 59 + 59, 27, 49);
        }
        moveUpAnimation = new Animation(0.5f, moveUpAnimationSplitted);

        TextureRegion[] moveRightAnimationSplitted = new TextureRegion[3];
        for(int i = 0; i < 3; i++){
            moveRightAnimationSplitted[i] = new TextureRegion(enemySheet, 15 + 36 * i, 84 + 59 * 3, 27, 49);
        }
        moveRightAnimation = new Animation(0.5f, moveRightAnimationSplitted);

        TextureRegion[] moveLeftAnimationSplitted = new TextureRegion[3];
        for(int i = 0; i < 3; i++){
            moveLeftAnimationSplitted[i] = new TextureRegion(enemySheet, 15 + 36 * i, 84 + 59, 27, 49);
        }
        moveLeftAnimation = new Animation(0.5f, moveLeftAnimationSplitted);

        TextureRegion[] moveDownAnimationSplitted =  new TextureRegion[6];
        for(int i = 0; i < 6; i++){
            moveDownAnimationSplitted[i] = new TextureRegion(enemySheet, 15 + 36 * i, 84, 27, 49);
        }
        moveDownAnimation = new Animation(0.5f, moveDownAnimationSplitted[0]);

        //TextureRegion deathAnimationRegion = new TextureRegion(enemySheet, 5, 344, 400, 48);
        TextureRegion[] deathAnimationSplitted = new TextureRegion[10];
        for(int i = 0; i < 10; i++){
            deathAnimationSplitted[i] = new TextureRegion(enemySheet, 5 + 40 * i, 344, 34, 48);
        }
        deathAnimation = new Animation(0.5f, deathAnimationSplitted);

        TextureRegion[] spawnAnimationSplitted = new TextureRegion[5];
        for(int i = 0; i < 5; i++){
            spawnAnimationSplitted[i] = new TextureRegion(enemySheet, 15 + 50 * i, 12, 33, 51);
        }

        spawnAnimation = new Animation(1, spawnAnimationSplitted);

        //TextureRegion[][] deathAnimationSplitted = deathAnimationRegion.split(40, 37);
        //System.out.print(moveUpSplitted.length());

        stateTime = 0f;
        currentFrame = new TextureRegion();
    }

    protected void update(Enemy.State state, Direction direction) {
        //System.out.println("EnemyState: " + state.toString());

        switch (state) {
            case SPAWN:
                currentFrame = spawnAnimation.getKeyFrame(stateTime, false);
                break;
            case MOVE:
                currentFrame = getMoveAnimationFrame(direction);
                break;
            case DEAD:
                currentFrame = deathAnimation.getKeyFrame(stateTime, false);
                break;
            default:
                break;
        }
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public boolean isSpawnAnimationFinished(){
        return spawnAnimation.isAnimationFinished(stateTime);
    }

    public boolean isEnemyDeathAnimationFinished(){
        //return deathAnimation.isAnimationFinished(stateTime);
        //System.out.println("Enemy StateTime: " + String.valueOf(stateTime));
        return stateTime > FRAME_DURATION * DEATH_FRAMES;
    }

    private TextureRegion getMoveAnimationFrame(Direction direction){
        TextureRegion frame = new TextureRegion();
        switch(direction) {
            case UP:
                frame = moveUpAnimation.getKeyFrame(stateTime, true);
                break;
            case RIGHT:case UPRIGHT:case DOWNRIGHT:
                frame = moveRightAnimation.getKeyFrame(stateTime, true);
                break;
            case DOWN:
                frame = moveDownAnimation.getKeyFrame(stateTime, true);
                break;
            case LEFT:case UPLEFT:case DOWNLEFT:
                frame = moveLeftAnimation.getKeyFrame(stateTime, true);
                break;
            default:
                break;
        }
        return frame;
    }
}
