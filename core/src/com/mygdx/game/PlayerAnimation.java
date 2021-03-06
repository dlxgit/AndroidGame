package com.mygdx.game;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.Animation;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.graphics.g2d.TextureRegion;
        import com.badlogic.gdx.math.Rectangle;
        import com.badlogic.gdx.math.Vector2;

/**
 * Created by Andrey on 11.09.2016.
 */
public class PlayerAnimation {
    public static final float EXTINGUISHER_SPEED = 300.f;
    public static final float MAX_EXTINGUISH_TIME = 0.2f;
    Animation moveUpAnimation;
    Animation moveRightAnimation;
    Animation moveDownAnimation;
    Animation moveLeftAnimation;
    Animation damagedAnimation;

    TextureRegion[] stayWithGunRegion;
    TextureRegion[] stayWithExtinguisherRegion;
    Animation throwGrenadeUpAnimation;
    Animation throwGrenadeRightAnimation;
    Animation throwGrenadeDownAnimation;
    Animation throwGrenadeLeftAnimation;
    Animation fireExtinguisherAnimation;
    Animation grenadeExplosionAnimation;

    TextureRegion grenade;

    float extinguishTime = 0;


    public PlayerAnimation(Texture playerSheet) {
        TextureRegion[] reg = new TextureRegion[2];
        for(int i = 0; i < 2; i++){
            reg[i] = new TextureRegion(playerSheet, (10 + 32 * i), 179, 32, 45);
        }

        TextureRegion moveUpRegion = new TextureRegion(playerSheet, 105, 84, 88, 37);
        TextureRegion moveRightRegion = new TextureRegion(playerSheet, 105, 44, 88, 37);
        TextureRegion moveDownRegion = new TextureRegion(playerSheet, 105, 4, 88, 37);
        TextureRegion moveLeftRegion = new TextureRegion(playerSheet, 105, 124, 88, 37);
        TextureRegion stayWithExtinguisherRegion = new TextureRegion(playerSheet, 237, 3, 117, 87);
        TextureRegion throwGrenadeAnimationRegion = new TextureRegion(playerSheet, 374, 203, 58, 184);
        TextureRegion grenadeExplosionRegion = new TextureRegion(playerSheet,498, 76, 85, 398);
        TextureRegion fireExtinguisherRegion = new TextureRegion(playerSheet,257, 134, 60, 16);

        TextureRegion[][] fireExtinguisherSplitted = fireExtinguisherRegion.split(15, 16);
        TextureRegion[][] throwGrenadeSplitted = throwGrenadeAnimationRegion.split(29, 46);
        TextureRegion[][] moveUpSplitted = moveUpRegion.split(22, 37);
        TextureRegion[][] moveRightSplitted = moveRightRegion.split(22, 37);
        TextureRegion[][] moveDownSplitted = moveDownRegion.split(22, 37);
        TextureRegion[][] moveLeftSplitted = moveLeftRegion.split(22, 37);
        TextureRegion[][] stayWithWeaponRegionSplitted = stayWithExtinguisherRegion.split(29, 41);
        TextureRegion[][] grenadeExplosionSplitted = grenadeExplosionRegion.split(85, 67);
        TextureRegion[] grenadeExplosionFrames = new TextureRegion[grenadeExplosionSplitted.length];

        moveUpAnimation = new Animation(0.02f, moveUpSplitted[0]);
        moveRightAnimation = new Animation(0.02f, moveRightSplitted[0]);
        moveDownAnimation = new Animation(0.02f, moveDownSplitted[0]);
        moveLeftAnimation = new Animation(0.02f, moveLeftSplitted[0]);

        stayWithGunRegion = stayWithWeaponRegionSplitted[0];
        this.stayWithExtinguisherRegion = stayWithWeaponRegionSplitted[1];
        grenade = new TextureRegion(playerSheet, 462, 259, 14, 13);
        damagedAnimation = new Animation(0.3f, reg);

        throwGrenadeDownAnimation = new Animation(0.3f, throwGrenadeSplitted[0]);
        throwGrenadeLeftAnimation = new Animation(0.3f, throwGrenadeSplitted[1]);
        throwGrenadeUpAnimation = new Animation(0.3f, throwGrenadeSplitted[2]);
        throwGrenadeRightAnimation = new Animation(0.3f, throwGrenadeSplitted[3]);
        grenadeExplosionAnimation = new Animation(0.1f, grenadeExplosionFrames);
        fireExtinguisherAnimation = new Animation(0.1f, fireExtinguisherSplitted[0]);
    }


    TextureRegion getCurrentFrame(Player.State playerState, Direction playerLastDirection, float stateTime) {
        switch (playerState) {
            case DAMAGED:
                return damagedAnimation.getKeyFrame(stateTime, false);
            case MOVE:
                return getMoveFrame(playerLastDirection, stateTime);
            case STAY:
                return getMoveFrame(playerLastDirection, 0);
            case SHOOT:
                return stayWithGunRegion[Direction.getSide(playerLastDirection)];
            case EXTINGUISH:
                return stayWithExtinguisherRegion[Direction.getSide(playerLastDirection)];
            case THROW_GRENADE:
                return getThrowFrame(playerLastDirection, stateTime);
        }
        return new TextureRegion();
    }

    TextureRegion updateExtinguisherAnimation(){
        if(extinguishTime > MAX_EXTINGUISH_TIME){
            extinguishTime = 0;
        }
        extinguishTime += Gdx.graphics.getDeltaTime();
        return fireExtinguisherAnimation.getKeyFrame(extinguishTime, true);
    }

    private TextureRegion getMoveFrame(Direction direction, float time){
        switch(direction) {
            case UP:
                return moveUpAnimation.getKeyFrame(time, true);
            case RIGHT:case UPRIGHT:case DOWNRIGHT:
                return moveRightAnimation.getKeyFrame(time, true);
            case DOWN:
                return moveDownAnimation.getKeyFrame(time, true);
            case LEFT:case UPLEFT:case DOWNLEFT:
                return moveLeftAnimation.getKeyFrame(time, true);
        }

        return moveDownAnimation.getKeyFrame(0, true);
    }

    private TextureRegion getThrowFrame(Direction direction, float time){
        switch(direction) {
            case UP:
                return throwGrenadeUpAnimation.getKeyFrame(time, true);
            case RIGHT:case UPRIGHT:case DOWNRIGHT:
                return throwGrenadeRightAnimation.getKeyFrame(time, true);
            case DOWN:
                return throwGrenadeDownAnimation.getKeyFrame(time, true);
            case LEFT:case UPLEFT:case DOWNLEFT:
                return throwGrenadeLeftAnimation.getKeyFrame(time, true);
        }
        return new TextureRegion();
    }
}