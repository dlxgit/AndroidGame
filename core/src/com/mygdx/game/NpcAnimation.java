package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Andrey on 26.10.2016.
 */

public class NpcAnimation {

    Animation photographsAnimation;
    Animation babyAnimation;
    Animation teacherAnimation;
    Animation girlAnimation;
    Animation dogAnimation;
    Animation soldierAnimation;
    Animation searcherAnimation;
    Animation cookAnimation;

    Animation npcDeathAnimation;

    float stateTime;


    public static Rectangle getNpcImageRectangle(Npc.Type type, int frameIndex){
        switch(type){
            case PHOTOGRAPHS:
                return new Rectangle(41 * frameIndex, 350, 41, 50);
            case BABY:
                return new Rectangle(28 * frameIndex, 213, 23, 32);
            case TEACHER:
                return new Rectangle(37 * frameIndex, 2, 37, 41);
            case GIRL:
                return new Rectangle(50 * frameIndex, 92, 50, 67);
            case DOG:
                return new Rectangle(34 * frameIndex, 49, 31, 34);
            case SOLDIER:
                return new Rectangle(36 * frameIndex, 252, 35, 46);
            case SEARCHER:
                return new Rectangle(31 * frameIndex, 306, 29, 39);
            case COOK:
                return new Rectangle(54 * frameIndex, 454, 52, 66);
            case DEAD_NPC:
                return new Rectangle(45 * frameIndex, 593, 53, 45);
        }
        return new Rectangle();
    }


    public NpcAnimation(Texture npcSheet){
        stateTime = 0;
        Rectangle imageRectangle = new Rectangle();


        TextureRegion[] photographsAnimationSplitted = new TextureRegion[2];
        //imageRectangle = new
        for(int i = 0; i < 2; i++){

            photographsAnimationSplitted[i] = new TextureRegion(npcSheet, 41 * i, 350, 41, 50);
        }
        photographsAnimation = new Animation(0.5f, photographsAnimationSplitted);


        TextureRegion[] babyAnimationSplitted = new TextureRegion[7];
        for(int i = 0; i < 7; i++){
            babyAnimationSplitted[i] = new TextureRegion(npcSheet, 28 * i, 213, 23, 32);
        }
        babyAnimation = new Animation(0.5f, babyAnimationSplitted);


        TextureRegion[] teacherAnimationSplitted = new TextureRegion[2];
        for(int i = 0; i < 2; i++) {
            teacherAnimationSplitted[i] = new TextureRegion(npcSheet, 37 *i, 2, 37, 41);
        }
        teacherAnimation = new Animation(0.5f, teacherAnimationSplitted);


        TextureRegion[] girlAnimationSplitted = new TextureRegion[7];
        for(int i = 0; i < 7; i++){
            girlAnimationSplitted[i] = new TextureRegion(npcSheet, 50 * i, 92, 50, 67);
        }
        girlAnimation = new Animation(0.5f, girlAnimationSplitted);



        TextureRegion[] dogAnimationSplitted = new TextureRegion[4];
        for(int i = 0; i < 4; i++){
            dogAnimationSplitted[i] = new TextureRegion(npcSheet, 34 * i, 49, 31, 34);
        }
        dogAnimation = new Animation(0.5f, dogAnimationSplitted);



        TextureRegion[] soldierAnimationSplitted = new TextureRegion[3];
        for(int i = 0; i < 3; i++){
            soldierAnimationSplitted[i] = new TextureRegion(npcSheet, 36 * i, 252, 35, 46);
        }
        soldierAnimation = new Animation(0.5f, soldierAnimationSplitted);


        TextureRegion[] searcherAnimationSplitted = new TextureRegion[3];
        for(int i = 0; i < 3; i++){
            searcherAnimationSplitted[i] = new TextureRegion(npcSheet, 31 * i, 306, 29, 39);
        }
        searcherAnimation = new Animation(0.5f, searcherAnimationSplitted);


        TextureRegion[] cookAnimationSplitted = new TextureRegion[9];
        for(int i = 0; i < 9; i++) {
            cookAnimationSplitted[i] = new TextureRegion(npcSheet, 54 * i, 454, 52, 66);
        }
        cookAnimation = new Animation(0.5f, cookAnimationSplitted);


        TextureRegion[] npcDeathAnimationSplitted = new TextureRegion[8];
            for(int i = 0; i < 8; i++) {
                npcDeathAnimationSplitted[i] = new TextureRegion(npcSheet, 45 * i, 593, 53, 45);
            }
        npcDeathAnimation = new Animation(0.5f, npcDeathAnimationSplitted);
    }

    public void update(){
        stateTime += Gdx.graphics.getDeltaTime();
    }

    public TextureRegion getCurrentFrame(Npc.Type type, boolean isLiving){
        //System.out.println("GETTING FRAMEEE");
        if(isLiving == false){
            return npcDeathAnimation.getKeyFrame(stateTime, false);
        }

        switch(type){
            case PHOTOGRAPHS:
                return photographsAnimation.getKeyFrame(stateTime, true);
            case BABY:
                return babyAnimation.getKeyFrame(stateTime, true);
            case TEACHER:
                return teacherAnimation.getKeyFrame(stateTime, true);
            case DOG:
                return dogAnimation.getKeyFrame(stateTime, true);
            case SOLDIER:
                return soldierAnimation.getKeyFrame(stateTime, true);
            case SEARCHER:
                return searcherAnimation.getKeyFrame(stateTime, true);
            case COOK:
                return cookAnimation.getKeyFrame(stateTime, true);
            case GIRL:
                return girlAnimation.getKeyFrame(stateTime, true);
            default:
                break;
        }
        return new TextureRegion();
    }

    public boolean isDeathAnimationFinished(){
        if(npcDeathAnimation.isAnimationFinished(stateTime)){
            return true;
        }
        return false;
    }
}
