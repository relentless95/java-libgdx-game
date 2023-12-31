package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

// to add buttons to the menu screen;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;

public class MenuScreen extends BaseScreen {

    public void initialize() {
        BaseActor ocean = new BaseActor(0, 0, mainStage);
        ocean.loadTexture("water.jpg");
        ocean.setSize(800, 600);

        BaseActor title = new BaseActor(0, 0, mainStage);
        title.loadTexture("starfish-collector.png");
//        title.centerAtPosition(400, 300);
//        title.moveBy(0, 100);


//        BaseActor start = new BaseActor(0, 0, mainStage);
//        start.loadTexture("message-start.png");
//        start.centerAtPosition(400, 300);
//        start.moveBy(0, -100);

        TextButton startButton = new TextButton("Start", BaseGame.textButtonStyle);
//        startButton.setPosition(150, 150);
//        uiStage.addActor(startButton);

        startButton.addListener(
                (Event e) ->
                {
                    if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(Type.touchDown)) {
                        return false;
                    }
//                    StarfishGame.setActiveScreen(new LevelScreen());
                    // we use this instead to display cut scene
                    StarfishGame.setActiveScreen(new StoryScreen());
                    return false;
                }
        );

        TextButton quitButton = new TextButton("Quit", BaseGame.textButtonStyle);
//        quitButton.setPosition(500, 150);
//        uiStage.addActor(quitButton);

        quitButton.addListener(
                (Event e) -> {
                    if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(Type.touchDown)) {
                        return false;
                    }


                    Gdx.app.exit();
                    return false;
                });


            // to define/align the positions of the buttons and title
            System.out.println("THE UITABLE IN MENUSCREEN: " + uiTable);
            uiTable.add(title).colspan(2);
            uiTable.row();
            uiTable.add(startButton);
            uiTable.add(quitButton);
        }

        public void update(float dt) {
            if (Gdx.input.isKeyPressed(Keys.S)) {
                // we use this instead to display the cutscene
//                StarfishGame.setActiveScreen(new LevelScreen());
                StarfishGame.setActiveScreen(new StoryScreen());


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

    public boolean keyDown(int keyCode) {
        if (Gdx.input.isKeyPressed(Keys.ENTER)) {
            StarfishGame.setActiveScreen(new LevelScreen());
        }
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        return false;
    }
}
