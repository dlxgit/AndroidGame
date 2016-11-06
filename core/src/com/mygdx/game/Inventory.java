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

    final int ITEM_AMMO[] = new int[]{12, 6, 10, 1};


    public Inventory(){
        nItem = 0;
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
        if (ammo[nItem] > 0 && cooldown == 0) {
            System.out.println("Item_use");
            //TODO:COOLDOWN FROM CONST (just 1 value or for each item?)
            cooldown = 1;
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

        //lootList.add(new Loot())
    }

    public int getCurrentSlot(){
        return nItem;
    }
}
