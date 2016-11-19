package com.mygdx.game;


import com.badlogic.gdx.Gdx;

import java.util.Dictionary;
import java.util.Enumeration;

/**
 * Created by Andrey on 05.11.2016.
 */

public class Inventory {
    Integer ammo[]; //pairs of each item-types and its quantity(ammo - for weapons)

    private int nItem;
    private float cooldown;

    final int ITEM_AMMO[] = new int[]{12, 6, 50, 1};


    public Inventory(){
        //nItem = 0;
        nItem = 2;

        ammo = new Integer[4];
        initializeInventory();
        cooldown = 0;
    }

    public void takeItem(int itemIndex){
        ammo[itemIndex] += ITEM_AMMO[itemIndex];
    }

    public void changeItem(){
        while(ammo[nItem] == 0){
            if(nItem == ammo.length){
                nItem = 0;
            }
            else ++nItem;
        }
    }

    public boolean useItem() {
        //TODO:: bug with endless bullets after taking loot smg         (или нету)
        if (ammo[nItem] > 0 && cooldown == 0) { //if it has ammo and no cooldown
            System.out.println("Item_use");
            //TODO:COOLDOWN FROM CONST (just 1 value or for each item?)
            if(nItem != 2){ //set cooldown if its not fire-extinguisher(no cooldown)
                cooldown = 1;
            }

            ammo[nItem]--;
            return true;
        }
        return false;
    }

    public void update(){
        if(cooldown > 0) {
            cooldown -= Gdx.graphics.getDeltaTime();
        }
        else{
            cooldown = 0;
        }
    }

    private void initializeInventory(){
        ammo[0] = 3;
        ammo[1] = 3;
        ammo[2] = 0;
        ammo[3] = 0;
    }

    public int getCurrentSlot(){
        return nItem;
    }
}
