package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.particles.values.RectangleSpawnShapeValue;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import org.w3c.dom.css.Rect;

import java.util.Random;

/**
 * Created by Andrey on 13.09.2016.
 */
public class Entity {
    float health;
    Rectangle rectangle;
    float moveSpeed;
    Direction direction;
    boolean isCollision = true;


    protected void updatePositionByCountingCollision(MapObjects solidObjects) {
        Vector2 oldPosition = new Vector2(rectangle.getX(), rectangle.getY());
        Rectangle newRect = moveRectangle(new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height));

        if (!Entity.IsCollisionWithMap(newRect, solidObjects)) {//try move by x and y
            rectangle = newRect;
        }
        else{//if collision
            isCollision = true;
            if (Math.abs(newRect.x - rectangle.x) > 2 || Math.abs(newRect.y - rectangle.y) > 2){//if is moving
                rectangle.setY(newRect.y);//try move by y
                if (Entity.IsCollisionWithMap(rectangle, solidObjects)) {
                    rectangle.setPosition(oldPosition.x, newRect.y);//try move by x
                    if (Entity.IsCollisionWithMap(rectangle, solidObjects)) {
                        rectangle.setPosition(oldPosition);
                    }
                }
            }
        }
    }

    protected void moveRectangle()
    {
        switch (direction)
        {
            case UP:
                rectangle.y += moveSpeed;
                break;
            case UPRIGHT:
                rectangle.x += (0.66 * moveSpeed);
                rectangle.y += (0.66 * moveSpeed);
                break;
            case RIGHT:
                rectangle.x += moveSpeed;
                break;
            case DOWNRIGHT:
                rectangle.x += (0.66 * moveSpeed);
                rectangle.y -= (0.66 * moveSpeed);
                break;
            case DOWN:
                rectangle.y -= moveSpeed;
                break;
            case DOWNLEFT:
                rectangle.x -= (0.66 * moveSpeed);
                rectangle.y -= (0.66 * moveSpeed);
                break;
            case LEFT:
                rectangle.x -= moveSpeed;
                break;
            case UPLEFT:
                rectangle.x -= (0.66 * moveSpeed);
                rectangle.y += (0.66 * moveSpeed);
                break;
            case NONE:
                break;
            default:
                break;
        }
    }

    protected Rectangle moveRectangle(Rectangle rectangle)
    {
        switch (direction)
        {
            case UP:
                rectangle.y += moveSpeed;
                break;
            case UPRIGHT:
                rectangle.x += (0.66 * moveSpeed);
                rectangle.y += (0.66 * moveSpeed);
                break;
            case RIGHT:
                rectangle.x += moveSpeed;
                break;
            case DOWNRIGHT:
                rectangle.x += (0.66 * moveSpeed);
                rectangle.y -= (0.66 * moveSpeed);
                break;
            case DOWN:
                rectangle.y -= moveSpeed;
                break;
            case DOWNLEFT:
                rectangle.x -= (0.66 * moveSpeed);
                rectangle.y -= (0.66 * moveSpeed);
                break;
            case LEFT:
                rectangle.x -= moveSpeed;
                break;
            case UPLEFT:
                rectangle.x -= (0.66 * moveSpeed);
                rectangle.y += (0.66 * moveSpeed);
                break;
            case NONE:
                break;
            default:
                break;
        }
        return rectangle;
    }

    public static Vector2 calculateDeltaDistance(Direction dir, float moveSpeed){
        float dx = 0;
        float dy = 0;
        switch (dir){
            case UP:
                dy = moveSpeed;
                break;
            case RIGHT:
                dx = moveSpeed;
                break;
            case DOWN:
                dy = -moveSpeed;
                break;
            case LEFT:
                dx = -moveSpeed;
                break;
            default:
                break;
        }
        return new Vector2(dx, dy);
    }

    public static boolean IsCollisionWithMap(Rectangle rectangle, MapObjects solid) {
        for (RectangleMapObject rectangleObject : solid.getByType(RectangleMapObject.class)){
            if (Intersector.overlaps(rectangle, rectangleObject.getRectangle())) {
                return true;
            }
        }
        return false;
    }



    static Rectangle calculateObjectSpawnPosition(Vector2 objectSize, MapObjects solidObjects, Rectangle mapSector, Random rand){
        while (true) {
            Rectangle resultRect = new Rectangle((rand.nextInt((int) (mapSector.width / TileMap.STEP_TILE))) * TileMap.STEP_TILE + mapSector.x,
                                                (rand.nextInt((int) (mapSector.height / TileMap.STEP_TILE))) * TileMap.STEP_TILE + mapSector.y,
                                                objectSize.x,
                                                objectSize.y);
            boolean isPositionFree = true;
            for (RectangleMapObject rectangleObject : solidObjects.getByType(RectangleMapObject.class)){
                if (Intersector.overlaps(resultRect, rectangleObject.getRectangle())) {
                    isPositionFree = false;
                    break;
                }
            }
            if (isPositionFree) {
                return resultRect;
            }
        }
    }
}
