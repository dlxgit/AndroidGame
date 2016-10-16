package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Andrey on 05.10.2016.
 */

public class HealthBar {
    public final Vector2 healthLineMaxSize = new Vector2(150, 27);
    Texture barTexture;
    Sprite bar;
    Texture healthLineTexture;
    Sprite healthLine;
    int healthPercent;

    public HealthBar(Assets assets, Vector2 viewPortCenter){
        healthPercent = 100;
        barTexture = assets.manager.get(assets.healthBarBorderFileName);
        bar = new Sprite(barTexture);
        healthLineTexture = assets.manager.get(assets.healthBarLineFileName);
        healthLine = new Sprite(healthLineTexture);
        setPosition(viewPortCenter);
    }

    public void render(SpriteBatch batch){
        bar.draw(batch);
        healthLine.draw(batch);
    }

    public void update(int healthPercent, Vector2 viewPortCenter){
        setHealthPercent(healthPercent);
        setPosition(viewPortCenter);
    }

    public void setPosition(Vector2 viewPortCenter){
        bar.setPosition(viewPortCenter.x - 350, viewPortCenter.y + 265);
        healthLine.setPosition(bar.getX() + 5, bar.getY() - 40);
    }

    public void setHealthPercent(int healthPercent){
        this.healthPercent = healthPercent;
        this.healthLine.setRegion(0,0, healthLineMaxSize.x * healthPercent, healthLineMaxSize.y);
    }
}