package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

// adding sound
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;


public class LevelScreen extends BaseScreen {
    private Turtle turtle;
    private boolean win;

    // to add labels
    private Label starfishLabel;
    //adding signs
    private DialogBox dialogBox;

    // adding music
    private float audioVolume;
    private Sound waterDrop;
    private Music instrumental;
    private Music oceanSurf;

    public void initialize() {
//        not use any more in chap 10
/**        ocean = new BaseActor(0, 0, mainStage);
 ocean.loadTexture("water.jpg");
 ocean.setSize(800, 600);

 starfish = new Starfish(380, 380, mainStage);

 // not used anymore in chapter 10
 //using the list instead
 BaseActor ocean = new BaseActor(0, 0, mainStage);

 // not used anymore in chapter 10
 ocean.loadTexture("water-border.jpg");
 ocean.setSize(1200, 900);
 BaseActor.setWorldBounds(ocean);

 //        turtle = new Turtle(20, 20, mainStage);
 //        rock = new Rock(200, 200, mainStage);

 // not used anymore in chapter 10
 // the base actor class adds them to the stage
 new Starfish(400, 400, mainStage);
 new Starfish(500, 100, mainStage);
 new Starfish(100, 450, mainStage);
 new Starfish(200, 250, mainStage);

 new Rock(200, 150, mainStage);
 new Rock(100, 300, mainStage);
 new Rock(300, 350, mainStage);
 new Rock(450, 200, mainStage);

 // not used anymore in chapter 10
 turtle = new Turtle(20, 20, mainStage);

 // not used anymore in chapter 10
 // adding the sign
 Sign sign1 = new Sign(20, 400, mainStage);
 sign1.setText("West Starfish Bay");

 Sign sign2 = new Sign(600, 300, mainStage);
 sign2.setText("East Starfish Bay");
 **/

        // we now use this in chapter 10;
        // ---------
        TiledmapActor tma = new TiledmapActor("map.tmx", mainStage);

        for (MapObject obj : tma.getTileList("Starfish")) {
            MapProperties props = obj.getProperties();
            new Starfish((float) props.get("x"), (float) props.get("y"), mainStage);
        }

        for (MapObject obj : tma.getTileList("Rock")) {
            MapProperties props = obj.getProperties();
            new Rock((float) props.get("x"), (float) props.get("y"), mainStage);
        }

        for (MapObject obj : tma.getTileList("Sign")) {
            MapProperties props = obj.getProperties();
            Sign s = new Sign((float) props.get("x"), (float) props.get("y"), mainStage);
            s.setText((String) props.get("message"));
        }

        MapObject startPoint = tma.getRectangleList("Start").get(0);
        MapProperties props = startPoint.getProperties();
        turtle = new Turtle((float) props.get("x"), (float) props.get("y"), mainStage);

        win = false;

        // user interface code
        starfishLabel = new Label("Starfish Left:", BaseGame.labelStyle);
        starfishLabel.setColor(Color.CYAN);
//        starfishLabel.setPosition(20, 520);
//        uiStage.addActor(starfishLabel);

        ButtonStyle buttonStyle = new ButtonStyle();
        Texture buttonTex = new Texture(Gdx.files.internal("undo.png"));
        TextureRegion buttonRegion = new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable(buttonRegion);

        Button restartButton = new Button(buttonStyle);
        restartButton.setColor(Color.CYAN);
//        restartButton.setPosition(729, 520);
//        uiStage.addActor(restartButton);


        restartButton.addListener(
                (Event e) -> {
                    // instead of the commented code, to free up memory we need to dispose of the sound being played each time we finish with a music object
//                    if (!(e instanceof InputEvent) ||
//                            !((InputEvent) e).getType().equals(Type.touchDown)) {
//                        return false;
//                    }
                    if (!isTouchDownEvent(e)) {
                        return false;
                    }

                    instrumental.dispose();
                    oceanSurf.dispose();

                    StarfishGame.setActiveScreen(new LevelScreen());
                    return false;
                }
        );

        // for the mute and unmute audio button
        ButtonStyle buttonStyle2 = new ButtonStyle();

        Texture buttonTex2 = new Texture(Gdx.files.internal("audio.png"));
        TextureRegion buttonRegion2 = new TextureRegion(buttonTex2);
        buttonStyle2.up = new TextureRegionDrawable(buttonRegion2);

        Button muteButton = new Button(buttonStyle2);
        muteButton.setColor(Color.CYAN);

        muteButton.addListener(
                (Event e) -> {
                    if (!isTouchDownEvent(e)) {
                        return false;
                    }

                    audioVolume = 1 - audioVolume;
                    instrumental.setVolume(audioVolume);
                    oceanSurf.setVolume(audioVolume);

                    return true;

                }
        );

        uiTable.pad(10);
        uiTable.add(starfishLabel).top();
        uiTable.add().expandX().expandY();
        uiTable.add(muteButton).top();
        uiTable.add(restartButton).top();

        dialogBox = new DialogBox(0, 0, uiStage);
        dialogBox.setBackgroundColor(Color.TAN);
        dialogBox.setFontColor(Color.BROWN);
        dialogBox.setDialogSize(600, 100);
        dialogBox.setFontScale(0.80f);
        dialogBox.alignCenter();
        dialogBox.setVisible(false);

        uiTable.row();
        uiTable.add(dialogBox).colspan(3);
//        uiTable.add(dialogBox).colspan(4);


        // adding music
        waterDrop = Gdx.audio.newSound(Gdx.files.internal("Water_Drop.ogg"));
        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Master_of_the_Feast.ogg"));
        oceanSurf = Gdx.audio.newMusic(Gdx.files.internal("Ocean_Waves.ogg"));

        audioVolume = 1.00f;
        instrumental.setLooping(true);
        instrumental.setVolume(audioVolume);
        instrumental.play();
        oceanSurf.setLooping(true);
        oceanSurf.setVolume(audioVolume);
        oceanSurf.play();
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
                waterDrop.play(audioVolume);
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

        // to display the number of starfish left
        starfishLabel.setText("Starfish Left: " + BaseActor.count(mainStage, "com.mygdx.game.Starfish"));


        // to open the dialogbox when the character is near it
        for (BaseActor signActor : BaseActor.getList(mainStage, "com.mygdx.game.Sign")) {
            Sign sign = (Sign) signActor;
            turtle.preventOverlap(sign);
            boolean nearby = turtle.isWithinDistance(4, sign);

            if (nearby && !sign.isViewing()) {
                dialogBox.setText(sign.getText());
                dialogBox.setVisible(true);
                sign.setViewing(true);
            }

            if (sign.isViewing() && !nearby) {
                dialogBox.setText(" ");
                dialogBox.setVisible(false);
                sign.setViewing(false);
            }

        }
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }


}
