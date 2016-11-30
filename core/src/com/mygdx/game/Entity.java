package com.mygdx.game;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Andrey on 13.09.2016.
 */
public class Entity {
    float health;
    Rectangle rectangle;
    float moveSpeed;
    //float 0.66 * moveSpeed;
    Direction direction;

    public void moveRectangle()
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
        //return rectangle;
    }

    public Rectangle moveRectangle(Rectangle rectangle)
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

    /*
    public Rectangle moveRectangle(Direction lastDirection)
    {
        switch (lastDirection)
        {
            case UP:
                rectangle.y += moveSpeed;
                break;
            case UPRIGHT:
                rectangle.x += (0.66 * moveSpeed * moveSpeed);
                rectangle.y += (0.66 * moveSpeed);
                break;
            case RIGHT:
                rectangle.x += moveSpeed;
                break;
            case DOWNRIGHT:
                rectangle.x += (0.66 * moveSpeed * moveSpeed);
                rectangle.y -= (0.66 * moveSpeed);
                break;
            case DOWN:
                rectangle.y -= moveSpeed;
                break;
            case DOWNLEFT:
                rectangle.x -= (0.66 * moveSpeed * moveSpeed);
                rectangle.y -= (0.66 * moveSpeed);
                break;
            case LEFT:
                rectangle.x -= moveSpeed;
                break;
            case UPLEFT:
                rectangle.x -= (0.66 * moveSpeed * moveSpeed);
                rectangle.y += (0.66 * moveSpeed);
                break;
            case NONE:
                break;
            default:
                break;
        }
        return rectangle;
    }
   */

    static boolean IsCollisionWithMap(Rectangle rectangle, MapObjects solid) {
        for (RectangleMapObject rectangleObject : solid.getByType(RectangleMapObject.class)){
            if (Intersector.overlaps(rectangle, rectangleObject.getRectangle())) {
                return true;
            }
        }
        return false;
    }

    void UpdatePositionByCollision(MapObjects solidObjects) {
        /*
        Rectangle oldRect = rectangle;
        Rectangle newRect = moveRectangle(oldRect);

        if (IsCollisionWithMap(newRect, solidObjects)) {
            if (newRect != oldRect){
                rectangle.setPosition(oldRect.x, newRect.y);
                if (IsCollisionWithMap(rectangle, solidObjects)) {
                    rectangle.setPosition(newRect.x, oldRect.y);
                    if (IsCollisionWithMap(rectangle, solidObjects)) {
                        rectangle = oldRect;
                    }
                }
            }
        }
        */

        Rectangle newRect = moveRectangle(rectangle);
        Vector2 oldPosition = new Vector2(rectangle.getX(), rectangle.getY());
        if (IsCollisionWithMap(newRect, solidObjects)) {
            if (newRect != rectangle){
                rectangle.setY(newRect.y);
                if (IsCollisionWithMap(rectangle, solidObjects)) {
                    rectangle.setX(newRect.x);
                    if (IsCollisionWithMap(rectangle, solidObjects)) {
                        rectangle.setPosition(oldPosition);
                    }
                }
            }
        }
    }

}
