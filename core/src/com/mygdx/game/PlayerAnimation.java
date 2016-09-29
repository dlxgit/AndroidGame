package com.mygdx.game;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.Animation;
        import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Andrey on 11.09.2016.
 */
public class PlayerAnimation {
    Animation moveUpAnimation;
    Animation moveRightAnimation;
    Animation moveDownAnimation;
    Animation moveLeftAnimation;

    Animation damagedAnimation;
    float stateTime;
    TextureRegion currentFrame;

    public PlayerAnimation(Texture playerSheet) {
        //(105 + 22 * int(hero.currentFrame), 84, 21, 37));
        TextureRegion moveUpRegion = new TextureRegion(playerSheet, 105, 84, 88, 37);
        TextureRegion[][] moveUpSplitted = moveUpRegion.split(22, 37);
        //System.out.print(moveUpSplitted.length());
        moveUpAnimation = new Animation(0.0005f, moveUpSplitted[0]);
        stateTime = 0f;
        currentFrame = new TextureRegion();

        //105 + 22 * int(hero.currentFrame), 44, 21, 37));
        TextureRegion moveRightRegion = new TextureRegion(playerSheet, 105, 44, 88, 37);
        TextureRegion[][] moveRightSplitted = moveRightRegion.split(22, 37);
        moveRightAnimation = new Animation(0.0005f, moveRightSplitted[0]);
        stateTime = 0f;

        //105 + 22 * int(hero.currentFrame), 4, 21, 37));
        TextureRegion moveDownRegion = new TextureRegion(playerSheet, 105, 4, 88, 37);
        TextureRegion[][] moveDownSplitted = moveDownRegion.split(22, 37);
        moveDownAnimation = new Animation(0.0005f, moveDownSplitted[0]);
        stateTime = 0f;

        //105 + 22 * int(hero.currentFrame), 124, 21, 37));
        TextureRegion moveLeftRegion = new TextureRegion(playerSheet, 105, 124, 88, 37);
        TextureRegion[][] moveLeftSplitted = moveLeftRegion.split(22, 37);
        moveLeftAnimation = new Animation(0.0005f, moveLeftSplitted[0]);
        stateTime = 0f;

        //10 + 32 * int(hero.currentFrame), 179, 32, 45
        TextureRegion damagedRegion = new TextureRegion(playerSheet, 105, 179, 64, 37);
        TextureRegion[][] damagedSplitted = moveUpRegion.split(22, 37);
        //System.out.print(damagedSplitted.length());
        damagedAnimation = new Animation(0.0005f, damagedSplitted[0]);
        stateTime = 0f;
    }

    public void update(Player.State state, Direction direction) {
        stateTime += Gdx.graphics.getDeltaTime();
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public void play(Player.State playerState, Direction playerLastDirection) {
        switch (playerState) {
            case DAMAGED:
                currentFrame = damagedAnimation.getKeyFrame(stateTime, true);
                break;
            case MOVE:
                //System.out.println("MOVEEEEEE");
                setCurrentFrame(playerLastDirection, stateTime);
                break;
            case STAY:
                setCurrentFrame(playerLastDirection, 0);
                break;
        }
    }

    //if player.State == STAY
    /*
    private void setStayFrame(Direction playerLastDirection){
        switch(playerLastDirection){
            case UP:
                currentFrame = moveUpAnimation.getKeyFrame(0, true);
                break;
            case RIGHT:case UPRIGHT:case DOWNRIGHT:
                currentFrame = moveRightAnimation.getKeyFrame(0, true);
                break;
            case DOWN:
                currentFrame = moveDownAnimation.getKeyFrame(0, true);
                break;
            case LEFT:case UPLEFT:case DOWNLEFT:
                currentFrame = moveLeftAnimation.getKeyFrame(0, true);
                break;
        }
    }*/

    private void setCurrentFrame(Direction direction, float time){
        switch(direction) {
            case UP:
                currentFrame = moveUpAnimation.getKeyFrame(time, true);
                break;
            case RIGHT:case UPRIGHT:case DOWNRIGHT:
                currentFrame = moveRightAnimation.getKeyFrame(time, true);
                break;
            case DOWN:
                currentFrame = moveDownAnimation.getKeyFrame(time, true);
                break;
            case LEFT:case UPLEFT:case DOWNLEFT:
                currentFrame = moveLeftAnimation.getKeyFrame(time, true);
                break;
        }
    }
}