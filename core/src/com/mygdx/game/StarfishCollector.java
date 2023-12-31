package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.compression.lzma.Base;

public class StarfishCollector extends GameBeta {

//    private Turtle turtle;
//    private Starfish starfish;
//    private BaseActor ocean;

//    private Rock rock;

    // using the list instead we onlhy need turtle and win
    private Turtle turtle;

    private boolean win;

    public void initialize() {
//        ocean = new BaseActor(0, 0, mainStage);
//        ocean.loadTexture("water.jpg");
//        ocean.setSize(800, 600);

//        starfish = new Starfish(380, 380, mainStage);

        //using the list instead
        BaseActor ocean = new BaseActor(0, 0, mainStage);
        ocean.loadTexture("water-border.jpg");
        ocean.setSize(1200, 900);

        BaseActor.setWorldBounds(ocean);
//        turtle = new Turtle(20, 20, mainStage);
//        rock = new Rock(200, 200, mainStage);


        // the base actor class adds them to the stage
        new Starfish(400, 400, mainStage);
        new Starfish(500, 100, mainStage);
        new Starfish(100, 450, mainStage);
        new Starfish(200, 250, mainStage);

        new Rock(200, 150, mainStage);
        new Rock(100, 300, mainStage);
        new Rock(300, 350, mainStage);
        new Rock(450, 200, mainStage);

        turtle = new Turtle(20, 20, mainStage);

        win = false;

    }

    public void update(float dt) {

//        //to prevent overlap with the rock. to see how this method is implemented check BaseActor
//        turtle.preventOverlap(rock);
////        rock.preventOverlap(turtle); // switching like this could be used to move the rock arround
//
//        if (turtle.overlaps(starfish) && !starfish.isCollected()) {
//            starfish.collect();
//            Whirlpool whirl = new Whirlpool(0, 0, mainStage);
//            whirl.centerAtActor(starfish);
//            whirl.setOpacity(0.25f);
//
//            BaseActor youWinMessage = new BaseActor(0, 0, mainStage);
//            youWinMessage.loadTexture("assets/you-win.png");
//            youWinMessage.centerAtPosition(400, 300);
//            youWinMessage.setOpacity(0);
//            youWinMessage.addAction(Actions.delay(1));
//            youWinMessage.addAction(Actions.after(Actions.fadeIn(1)));
//        }

        // now using a list instead
        for (BaseActor rockActor : BaseActor.getList(mainStage, "com.mygdx.game.Rock")) {
            turtle.preventOverlap(rockActor);
        }

        for (BaseActor starfishActor : BaseActor.getList(mainStage, "com.mygdx.game.Starfish")) {
            Starfish starfish = (Starfish) starfishActor;
            if (turtle.overlaps(starfish) && !starfish.collected) {
                starfish.collected = true;
                starfish.clearActions();
                starfish.addAction(Actions.fadeOut(1));
                starfish.addAction(Actions.after(Actions.removeActor()));


                Whirlpool whirl = new Whirlpool(0, 0, mainStage);
                whirl.centerAtActor(starfish);
                whirl.setOpacity(0.25f);
            }
        }
        if (BaseActor.count(mainStage, "com.mygdx.game.Starfish") == 0 && !win) {
            win = true;
            BaseActor youWinMessage = new BaseActor(0, 0, uiStage);
            youWinMessage.loadTexture("you-win.png");
            youWinMessage.centerAtPosition(400, 300);
            youWinMessage.setOpacity(0);
            youWinMessage.addAction(Actions.delay(1));
            youWinMessage.addAction(Actions.after(Actions.fadeIn(1)));

        }
    }


}
