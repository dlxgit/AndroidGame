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

    TextureRegion[] bulletRegion;
    Animation bulletDestroyAnimation;


    public PlayerAnimation(Texture playerSheet) {
        //(105 + 22 * int(hero.currentFrame), 84, 21, 37));
        TextureRegion moveUpRegion = new TextureRegion(playerSheet, 105, 84, 88, 37);
        TextureRegion[][] moveUpSplitted = moveUpRegion.split(22, 37);
        //System.out.print(moveUpSplitted.length());
        moveUpAnimation = new Animation(0.02f, moveUpSplitted[0]);

        //105 + 22 * int(hero.currentFrame), 44, 21, 37));
        TextureRegion moveRightRegion = new TextureRegion(playerSheet, 105, 44, 88, 37);
        TextureRegion[][] moveRightSplitted = moveRightRegion.split(22, 37);
        moveRightAnimation = new Animation(0.02f, moveRightSplitted[0]);

        //105 + 22 * int(hero.currentFrame), 4, 21, 37));
        TextureRegion moveDownRegion = new TextureRegion(playerSheet, 105, 4, 88, 37);
        TextureRegion[][] moveDownSplitted = moveDownRegion.split(22, 37);
        moveDownAnimation = new Animation(0.02f, moveDownSplitted[0]);

        //105 + 22 * int(hero.currentFrame), 124, 21, 37));
        TextureRegion moveLeftRegion = new TextureRegion(playerSheet, 105, 124, 88, 37);
        TextureRegion[][] moveLeftSplitted = moveLeftRegion.split(22, 37);
        moveLeftAnimation = new Animation(0.02f, moveLeftSplitted[0]);

        //10 + 32 * int(hero.currentFrame), 179, 32, 45
        TextureRegion damagedRegion = new TextureRegion(playerSheet, 105, 179, 64, 37);
        TextureRegion[][] damagedSplitted = moveUpRegion.split(22, 37);

        TextureRegion[] reg = new TextureRegion[2];
        for(int i = 0; i < 2; i++){
            reg[i] = new TextureRegion(playerSheet, (10 + 32 * i), 179, 32, 45);
        }

        damagedAnimation = new Animation(0.3f, reg);
        //damagedAnimation = new Animation(0.02f, damagedSplitted[0]);

        TextureRegion stayWithExtinguisherRegion = new TextureRegion(playerSheet, 237, 3, 117, 87);
        TextureRegion[][] stayWithWeaponRegionSplitted = stayWithExtinguisherRegion.split(29, 41);
        this.stayWithGunRegion = stayWithWeaponRegionSplitted[0];
        this.stayWithExtinguisherRegion = stayWithWeaponRegionSplitted[1];

        TextureRegion throwGrenadeAnimationRegion = new TextureRegion(playerSheet, 374, 203, 58, 184);
        TextureRegion[][] throwGrenadeSplitted = throwGrenadeAnimationRegion.split(29, 46);
        throwGrenadeDownAnimation = new Animation(0.3f, throwGrenadeSplitted[0]);
        throwGrenadeLeftAnimation = new Animation(0.3f, throwGrenadeSplitted[1]);
        throwGrenadeUpAnimation = new Animation(0.3f, throwGrenadeSplitted[2]);
        throwGrenadeRightAnimation = new Animation(0.3f, throwGrenadeSplitted[3]);


        grenade = new TextureRegion(playerSheet, 462, 259, 14, 13);

        TextureRegion grenadeExplosionRegion = new TextureRegion(playerSheet,498, 76, 85, 398);
        TextureRegion[][] grenadeExplosionSplitted = grenadeExplosionRegion.split(85, 67);
        TextureRegion[] grenadeExplosionFrames = new TextureRegion[grenadeExplosionSplitted.length];
        for(int i = 0; i < grenadeExplosionSplitted.length; ++i) {
            grenadeExplosionFrames[i] = grenadeExplosionSplitted[i][0];
        }
        grenadeExplosionAnimation = new Animation(0.1f, grenadeExplosionFrames);


        TextureRegion fireExtinguisherRegion = new TextureRegion(playerSheet,257, 134, 60, 16);
        TextureRegion[][] fireExtinguisherSplitted = fireExtinguisherRegion.split(15, 16);
        fireExtinguisherAnimation = new Animation(0.1f, fireExtinguisherSplitted[0]);



/*
        bulletRegion = new TextureRegion(playerSheet, 258, 510, 48, 21).split(16,21)[0];


        TextureRegion bulletAnimationRegion = new TextureRegion(playerSheet, 322, 510, 32, 21);
        TextureRegion[][] bulletAnimationSplitted = bulletAnimationRegion.split(16, 21);
        bulletDestroyAnimation = new Animation(0.1f, bulletAnimationSplitted[0]);
        */
    }

    Rectangle getExtinguisherRectangle(Direction dir, Rectangle playerRect) {
        Vector2 frameSize = new Vector2(fireExtinguisherAnimation.getKeyFrame(0).getRegionWidth(),fireExtinguisherAnimation.getKeyFrame(0).getRegionHeight());
        //Vector2 framePosition = Bullet.getExtinguisherRectangle(direction, playerRect, frameSize);
        Vector2 playerCenter = new Vector2(playerRect.getCenter(new Vector2()));

        System.out.println("DIRRRRR: " + dir.toString());
        Vector2 pos = new Vector2();
        switch (dir) {
            case UP:
                pos = new Vector2(playerRect.x + playerRect.width / 2 + frameSize.x / 2, playerRect.y + playerRect.height);
                break;
            case DOWN:case NONE:
                pos = new Vector2(playerRect.x + playerRect.width / 2 + frameSize.x / 2, playerRect.y - frameSize.y);
                break;
            case LEFT:
            case UPLEFT:
            case DOWNLEFT:
                pos =  new Vector2(playerRect.x - frameSize.x, playerRect.y + playerRect.height / 2 - frameSize.y / 2);
                break;
            case RIGHT:
            case UPRIGHT:
            case DOWNRIGHT:
                pos =  new Vector2(playerRect.x + playerRect.width, playerRect.y + playerRect.height / 2 - frameSize.y / 2);
                break;
        }
        return new Rectangle(pos.x, pos.y, frameSize.x, frameSize.y);
    }

    public TextureRegion getCurrentFrame(Player.State playerState, Direction playerLastDirection, float stateTime) {
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

    public TextureRegion getExtinguisherAnimation(float stateTime){
        return fireExtinguisherAnimation.getKeyFrame(stateTime, true);
    }

    /*
    private TextureRegion getStayFrame(Direction playerLastDirection){
        switch(playerLastDirection){
            case UP:
                return moveUpAnimation.getKeyFrame(0, true);
            case RIGHT:case UPRIGHT:case DOWNRIGHT:
                return moveRightAnimation.getKeyFrame(0, true);
            case DOWN:
                return moveDownAnimation.getKeyFrame(0, true);
            case LEFT:case UPLEFT:case DOWNLEFT:
                return moveLeftAnimation.getKeyFrame(0, true);
        }
        return new TextureRegion();
    }*/

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