package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
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
    public static final Vector2 FIRE_BUTTON_POS = new Vector2(1000, 2400);
    public static final Vector2 CHANGE_SLOT_BUTTON_POS = new Vector2(800, 2400);
    public static final Vector2 ESCAPE_BUTTON_POS = new Vector2(1500, 2400);

    Texture texture;
    Sprite sprite;
    boolean isPressed;

    public PressButton(Assets assets, Type type){
        Vector2 pos = new Vector2();
        switch(type){
            case FIRE:
                Texture fireTexture = assets.manager.get(assets.fireButtonTextureName);
                sprite = new Sprite(fireTexture);
                pos = FIRE_BUTTON_POS;
                break;
            case CHANGE_SLOT:
                Texture changeSlotTexture = assets.manager.get(assets.switchButtonTextureName);
                sprite = new Sprite(changeSlotTexture);
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
        Rectangle rect = sprite.getBoundingRectangle();
        isPressed = rect.contains(Gdx.input.getX() + rect.getWidth(),  Game.MAP_SIZE.y - Gdx.input.getY() + rect.getHeight() / 3);
//        //        isPressed = sprite.getBoundingRectangle().overlaps(new Rectangle(Gdx.input.getX(),  Game.MAP_SIZE.y - Gdx.input.getY() - sprite.getHeight(), 1, 1));
//
//        //isPressed = sprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY());
        //System.out.println("ButtonSTate: " + sprite.getBoundingRectangle().toString() + " " + (new Rectangle(Gdx.input.getX(), Game.MAP_SIZE.y - Gdx.input.getY(), 1,1)).toString());
        //System.out.println(isPressed);
    }

    public boolean isPressed(){
        return isPressed;
    }

    public void update(InputController controller){
        //System.out.println("CONTAINS!!!!!!!!!!!!!!!!!");
        if(!controller.isTouched){
            isPressed = false;
            return;
        }
        updatePressState();
    }
}
