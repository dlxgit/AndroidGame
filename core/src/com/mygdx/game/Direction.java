package com.mygdx.game;

/**
 * Created by Andrey on 11.09.2016.
 */
public enum Direction {
    RIGHT,
    UPRIGHT,
    UP,
    UPLEFT,
    LEFT,
    DOWNLEFT,
    DOWN,
    DOWNRIGHT,
    NONE;

    static Direction intToDirection(int side) {
        switch (side) {
            case 0:
                return Direction.RIGHT;
            case 1:
                return Direction.UP;
            case 2:
                return Direction.LEFT;
            case 3:
                return Direction.DOWN;
        }
        return NONE;
    }

    static int getSide(Direction direction){
        switch (direction) {
            case UP:
                return 1;
            case RIGHT: case UPRIGHT: case DOWNRIGHT:
                return 0;
            case DOWN:
                return 3;
            case LEFT: case UPLEFT: case DOWNLEFT:
                return 2;
        }
        return 0;
    }
}
