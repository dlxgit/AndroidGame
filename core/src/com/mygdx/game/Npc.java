package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Andrey on 26.10.2016.
 */

public class Npc extends  Entity{
    public enum Type {
        PHOTOGRAPHS,
        BABY,
        TEACHER,
        GIRL,
        DOG,
        SOLDIER,
        SEARCHER,
        COOK,
        DEAD_NPC
    }

    Vector2 position;
    NpcAnimation animation;
    boolean isLiving;
    Type type;


    public Npc(Texture texture, Npc.Type type, float posX, float posY){
        this.isLiving = true;
        this.type = type;
        this.position = new Vector2(posX, Game.MAP_SIZE.y - posY);
        this.animation = new NpcAnimation(texture);

        this.rectangle = animation.getNpcImageRectangle(type, 0);
        this.rectangle.setPosition(posX, Game.MAP_SIZE.y - posY);
    }

    public boolean isLiving(){
        return isLiving;
    }

    public void die(){
        isLiving = false;
        animation.stateTime = 0;
    }

    void update(){
        animation.update();
    }

    public void render(SpriteBatch batch){
        batch.draw(animation.getCurrentFrame(type, isLiving), position.x, position.y);
    }
}