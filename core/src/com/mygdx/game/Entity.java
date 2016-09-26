package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Andrey on 13.09.2016.
 */
public class Entity {
    float health;
    Vector2 position;
    Rectangle rectangle;
    float speed;
    float diagonal_speed;
    Direction dir;

    public Rectangle MoveRectangle()
    {
        Vector2 pos = new Vector2();
        rectangle.getPosition(pos);
        switch (dir)
        {
            case UP:
                pos.y -= speed;
                break;
            case UPRIGHT:
                pos.x += (diagonal_speed * speed);
                pos.y -= (diagonal_speed * speed);
                break;
            case RIGHT:
                pos.x += speed;
                break;
            case DOWNRIGHT:
                pos.x += (diagonal_speed * speed);
                pos.y += (diagonal_speed * speed);
                break;
            case DOWN:
                pos.y += speed;
                break;
            case DOWNLEFT:
                pos.x -= (diagonal_speed * speed);
                pos.y += (diagonal_speed * speed);
                break;
            case LEFT:
                pos.x -= speed;
                break;
            case UPLEFT:
                pos.x -= (diagonal_speed * speed);
                pos.y -= (diagonal_speed * speed);
                break;
            case NONE:
                break;
            default:
                break;
        }
        rectangle.setPosition(pos);
        return rectangle;
    }

}
