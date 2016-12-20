package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Andrey on 12.12.2016.
 */

public class Hud {

    public static final Vector2 HEALTH_BAR_BORDER_POSITION = new Vector2(50, 1000);
    public static final Vector2 HEALTH_BAR_POSITION = new Vector2(HEALTH_BAR_BORDER_POSITION.x + 15.f,
                                                                  HEALTH_BAR_BORDER_POSITION.y + 4.f);

    private Texture healthBar;
    private Texture healthBarBorder;

    private int healthBarPercentage;
    private int currentAmmo;
    private Loot.Type currentItem;


    public Hud(Assets assets){
        Texture healthBar = assets.manager.get(Assets.healthBarLineName);
        Texture healthBarBorder = assets.manager.get(Assets.healthBarBorderName);
        this.healthBar = healthBar;
        this.healthBarBorder = healthBarBorder;
        healthBarPercentage = 100;
    }

    public void update(int health, Inventory inventory){
        this.healthBarPercentage = health;
        if(healthBarPercentage < 0){
            healthBarPercentage = 0;
        }
        currentItem = Loot.Type.getType(inventory.getCurrentSlot());
        currentAmmo = inventory.getCurrentAmmo();
    }

    public void render(SpriteBatch batch){
        batch.draw(healthBarBorder, HEALTH_BAR_BORDER_POSITION.x, HEALTH_BAR_BORDER_POSITION.y);
        batch.draw(healthBar, HEALTH_BAR_POSITION.x, HEALTH_BAR_POSITION.y, 0, 0, healthBar.getWidth() * healthBarPercentage / 100, healthBar.getHeight());
    }
}