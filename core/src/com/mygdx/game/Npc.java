package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

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

    private Vector2 position;
    private boolean isLiving;
    private Type type;

    NpcAnimation animation;

    public Npc(Texture texture, Npc.Type type, float posX, float posY){
        this.isLiving = true;
        this.type = type;
        this.position = new Vector2(posX, Game.MAP_SIZE.y - posY);
        this.animation = new NpcAnimation(texture);

        this.rectangle = NpcAnimation.getNpcImageRectangle(type, 0);
        this.rectangle.setPosition(posX, Game.MAP_SIZE.y - posY);
    }

    boolean isLiving(){
        return isLiving;
    }

    void die(){
        isLiving = false;
        animation.stateTime = 0;
    }

    void update(){
        animation.update();
    }

    public void render(SpriteBatch batch){
        batch.draw(animation.getCurrentFrame(type, isLiving), position.x, position.y);
    }

    public static Vector<Npc> initializeNpc(Assets assets) {
        Texture texture = assets.manager.get(Assets.npcTextureName);
        Vector<Npc> npcList = new Vector<Npc>();
        npcList.add(new Npc(texture, Npc.Type.PHOTOGRAPHS, 5 * 48, 8 * 48));
        npcList.add(new Npc(texture, Npc.Type.BABY, 48 * 48, 10 * 48));
        npcList.add(new Npc(texture, Npc.Type.TEACHER, 9 * 48, 15 * 48));
        npcList.add(new Npc(texture, Npc.Type.DOG, 53 * 48, 15 * 48));
        npcList.add(new Npc(texture, Npc.Type.SOLDIER, 28 * 48, 28 * 48));
        npcList.add(new Npc(texture, Npc.Type.SEARCHER, 55 * 48, 4 * 48));
        npcList.add(new Npc(texture, Npc.Type.COOK, 22 * 48, 14 * 48));
        npcList.add(new Npc(texture, Npc.Type.GIRL, 15 * 48, 6 * 48));

        return npcList;
    }
}