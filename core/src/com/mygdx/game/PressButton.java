package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Andrey on 31.08.2016.
 */

public class PressButton {
    enum Type{
        FIRE,
        CHANGE_SLOT,
        ESCAPE
    }
    public static final Vector2 FIRE_BUTTON_POS = new Vector2(1000, 50);
    public static final Vector2 CHANGE_SLOT_BUTTON_POS = new Vector2(800, 50);
    public static final Vector2 ESCAPE_BUTTON_POS = new Vector2(1500, 900);

    Texture texture;
    Sprite sprite;
    boolean isPressed;

    public PressButton(Assets assets, Type type){
        texture = assets.manager.get(assets.fireButtonTextureName);
        sprite = new Sprite(texture);

        Vector2 pos = new Vector2();
        switch(type){
            case FIRE:
                pos = FIRE_BUTTON_POS;
                break;
            case CHANGE_SLOT:
                pos = CHANGE_SLOT_BUTTON_POS;
                break;
            case ESCAPE:
                pos = ESCAPE_BUTTON_POS;
                break;
        }
        sprite.setPosition(pos.x, pos.y);
        isPressed = false;
    }

    public void render(Batch batch){
        sprite.draw(batch);
    }

    private void updatePressState(){
        isPressed = sprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY());

    }

    public boolean isPressed(){
        return isPressed;
    }

    public void update(){
        //System.out.println("CONTAINS!!!!!!!!!!!!!!!!!");
        updatePressState();
    }
}
