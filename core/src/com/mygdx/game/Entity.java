package com.mygdx.game;

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

    public Rectangle moveRectangle()
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
}
