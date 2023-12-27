package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;



/**
 * Created when program is launched;
 * manages the screens that appear during the game.
 */
public abstract class BaseGame extends Game {

    /**
     * Stores reference to game; used when calling setActiveScreen method.
     */

    private static BaseGame game;
    public static LabelStyle labelStyle;

    /**
     * Called when game is initialized; stores global reference to game object.
     */
    public BaseGame() {
        game = this;
    }


    public void create() {
        // prepare for multiple classes/stages/actors to receive discrete input
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);
        labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
    }

    /**
     * Used to switch screens while game is running.
     * Method is static to simplify usage.
     */
    public static void setActiveScreen(BaseScreen s) {
        game.setScreen(s);
    }
}
