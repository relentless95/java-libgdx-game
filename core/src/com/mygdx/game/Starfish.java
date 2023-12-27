package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Starfish extends BaseActor {


    // to ensure the starfish is collected once and avoid being repeatedly added unnecessarily
    public boolean collected;

    public Starfish(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("starfish.png");

        Action spin = Actions.rotateBy(30, 1);
        this.addAction(Actions.forever(spin));

        //  so that each object uses an eight-sided collision polygon for improved accuracy:
        setBoundaryPolygon(8);


        // to ensure the starfish is cllected once and avoid being repeatedly added unnecessarily
        collected = false;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
        clearActions();
        addAction(Actions.fadeOut(1));
        addAction(Actions.after(Actions.removeActor()));
    }


}
