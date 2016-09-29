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
public class FireButton {
    public static final Vector2 pos = new Vector2(1000, 50);
    Texture texture;
    Sprite sprite;
    boolean isPressed;

    public  FireButton(Assets assets){
        //texture = new Texture(Gdx.files.internal("images/shootButton.png"));
        texture = assets.manager.get(assets.fireButtonTextureName);
        sprite = new Sprite(texture);
        sprite.setPosition(pos.x, pos.y);
        isPressed = false;
    }

    public void render(Batch batch){
        //batch.draw(texture,actorX,actorY);
        sprite.draw(batch);
    }

    private void updatePressState(){
        /*
        System.out.print(sprite.getBoundingRectangle().getX());
        System.out.print(" ");
        System.out.print(sprite.getBoundingRectangle().getY());
        System.out.print(" ");
        System.out.print(sprite.getBoundingRectangle().getWidth());
        System.out.print(" ");
        System.out.print(sprite.getBoundingRectangle().getHeight());
        System.out.println(" Button");
*/
/*
        System.out.print(Gdx.input.getX());
        System.out.print(" ");
        System.out.print(Gdx.input.getY());
        System.out.println(" Press");
        */
        if(sprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY())){
            //System.out.println("CONTAINS!!!!!!!!!!!!!!!!!");

            isPressed = true;
        }
        else isPressed = false;
    }

    public boolean isPressed(){
        return isPressed;
    }

    public void update(){
        //System.out.println("CONTAINS!!!!!!!!!!!!!!!!!");
        updatePressState();
    }
}
