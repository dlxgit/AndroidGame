package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Andrey on 31.08.2016.
 */

public class PressButton {
    enum Type{
        FIRE,
        CHANGE_SLOT,
        START,
        QUIT
    }

    public static final Vector2 FIRE_BUTTON_POS = new Vector2(1600, 200);
    public static final Vector2 CHANGE_SLOT_BUTTON_POS = new Vector2(1400, 200);

    public static final Vector2 START_BUTTON_POS = new Vector2(1920 / 2 - 640 / 2, 1080 / 2 + 30);
    public static final Vector2 QUIT_BUTTON_POS = new Vector2(1920 / 2 - 640 / 2, 1080 / 2 - 250);


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
            case START:
                pos = START_BUTTON_POS;
                Texture startButtonTexture = assets.manager.get(assets.startButtonName);
                sprite = new Sprite(startButtonTexture);
                break;
            case QUIT:
                pos = QUIT_BUTTON_POS;
                Texture quitButtonTexture = assets.manager.get(assets.quitButtonName);
                sprite = new Sprite(quitButtonTexture);
                break;
        }

        sprite.setPosition(pos.x, pos.y);
        isPressed = false;
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }

    private void updatePressState(){
        Rectangle rect = sprite.getBoundingRectangle();
//        isPressed = rect.contains(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - Gdx.input.getX() + camPos.x + rect.getWidth(),
//                camPos.y + (Gdx.graphics.getHeight() / 2 - Gdx.input.getY()) + rect.getHeight() / 3);


        Vector2 pos = new Vector2(Gdx.input.getX(),
                                Gdx.graphics.getHeight()- Gdx.input.getY());

//        Vector2 pos = new Vector2(Gdx.graphics.getWidth() / 2 - Gdx.input.getX() + camPos.x + rect.width,
//                camPos.y + (Gdx.graphics.getHeight() / 2 - Gdx.input.getY()) + rect.getHeight() / 3);

        isPressed = rect.contains(pos);

        //System.out.println("ButtonPos= " + rect.toString());
        //System.out.println("PressPos= " + pos.toString());
//        I/System.out: ButtonPos= [800.0,2620.0,48.0,48.0]
//        I/System.out: PressPos= (805.0,3118.0)
    }

    public boolean isPressed(){
        return isPressed;
    }

    public void update(InputController controller){
        if(controller.isTouched){
            updatePressState();
        }
        else isPressed = false;
        //sprite.setPosition(camPos.x + pos.x - Gdx.graphics.getWidth() / 2 , camPos.y - Gdx.graphics.getHeight() +  pos.y);
    }
}