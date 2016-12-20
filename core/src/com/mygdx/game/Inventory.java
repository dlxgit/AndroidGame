package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.Dictionary;
import java.util.Enumeration;

/**
 * Created by Andrey on 05.11.2016.
 */

public class Inventory {
    Integer ammo[]; //quantity of each item in inventory

    private int nItem;
    private float cooldown;

    final int ITEM_AMMO[] = new int[]{ //ammo that can be given from picking-up loot.
            12, //pistol
            6,  //grenade,
            50, //fire-ex
            1   //medicine
    };

    boolean isChangeAllowed = true;

    public Inventory(){
        nItem = 0;

        initializeInventory();
        cooldown = 0;
    }

    public void takeItem(int itemIndex){

        ammo[itemIndex] += ITEM_AMMO[itemIndex];
    }

    public void changeItem() {
        if (isChangeAllowed) {
            nItem = getNextItemIndex();
            isChangeAllowed = false;
        }
    }

    public boolean useItem() {
        if (ammo[nItem] > 0 && cooldown == 0) { //if it has ammo and no cooldown
            System.out.println("Item_use");
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
        System.out.println(Loot.Type.getType(getCurrentSlot()).toString());
    }

    private void initializeInventory(){
        ammo = new Integer[]{
                12,
                5,
                5,
                1
        };
    }

    public int getCurrentSlot(){
        return nItem;
    }

    private int getNextItemIndex(){
        for(int i = nItem + 1; i < ammo.length; ++i){
            if(ammo[i] > 0){
                return i;
            }
        }
        for(int i = 0; i < nItem; ++i){
            if(ammo[i] > 0){
                return i;
            }
        }
        return nItem;
    }

    public int getCurrentAmmo(){
        return ammo[nItem];
    }
}
