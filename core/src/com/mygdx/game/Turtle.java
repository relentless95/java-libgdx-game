package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class Turtle extends BaseActor {
    public Turtle(float x, float y, Stage s) {
        super(x, y, s);
        String[] filenames = {"turtle-1.png", "turtle-2.png", "turtle-3.png",
                "turtle-4.png", "turtle-5.png", "turtle-6.png" };

        loadAnimationFromFiles(filenames, 0.1f, true);

        // adding movement i.e acceleration etc
        setAcceleration(400);
        setMaxSpeed(100);
        setDeceleration(400);

        //  so that each object uses an eight-sided collision polygon for improved accuracy:
        setBoundaryPolygon(8);
    }

    //    the act method, which will check if arrow keys are being pressed and, if so,
    // accelerate the turtle in the corresponding direction.
    public void act(float dt) {
        super.act(dt);
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            accelerateAtAngle(180);
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            accelerateAtAngle(0);
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            accelerateAtAngle(98);
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            accelerateAtAngle(270);
        }

        applyPhysics(dt);

        setAnimationPaused(!isMoving());

        if (getSpeed() > 0) {
            setRotation(getMotionAngle());
        }

        boundToWorld();
        alignCamera();
    }
}
