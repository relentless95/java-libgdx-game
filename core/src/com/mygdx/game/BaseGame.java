package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

// for the fonts

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

//import com.badlogic.gdx.graphics.Texture.TextureFilter;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


// to create the button
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.Texture;


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

    // to create the button
    public static TextButtonStyle textButtonStyle;

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
        /**to use the stand alone extension heiro for fonts
         labelStyle = new LabelStyle();
         labelStyle.font = new BitmapFont();

         //        labelStyle.font = new BitmapFont(Gdx.files.internal("myCustomFont.fnt"));
         **/

//        to use  libgdx extension freetypefont
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));

        FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();
        fontParameters.size = 48;
        fontParameters.color = Color.WHITE;
        fontParameters.borderWidth = 2;
        fontParameters.borderColor = Color.BLACK;
        fontParameters.borderStraight = true;
        fontParameters.minFilter = TextureFilter.Linear;
        fontParameters.magFilter = TextureFilter.Linear;

        BitmapFont customFont = fontGenerator.generateFont(fontParameters);
        labelStyle = new LabelStyle();

        labelStyle.font = customFont;

        // to create the button
        textButtonStyle = new TextButtonStyle();
        Texture buttonTex = new Texture( Gdx.files.internal("button.png") );
        NinePatch buttonPatch = new NinePatch(buttonTex, 24,24,24,24);
        textButtonStyle.up = new NinePatchDrawable( buttonPatch );
        textButtonStyle.font = customFont;
        textButtonStyle.fontColor = Color.GRAY;


    }


    /**
     * Used to switch screens while game is running.
     * Method is static to simplify usage.
     */
    public static void setActiveScreen(BaseScreen s) {
        game.setScreen(s);
    }
}
