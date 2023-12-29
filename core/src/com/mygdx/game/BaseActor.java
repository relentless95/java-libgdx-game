package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import sun.jvm.hotspot.HelloWorld;

import java.util.ArrayList;


//import org.reflections.Reflections;

public class BaseActor extends Group {
    private Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean animationPaused;

    // adding velocity
    private Vector2 velocityVec;

    // adding acceleration
    private Vector2 accelerationVec;
    private float acceleration;

    private float maxSpeed;

    //adding deceleration

    private float deceleration;

    // adding the polygon
    private Polygon boundaryPolygon;

    // addoing world boundaries
    private static Rectangle worldBounds;


    public BaseActor(float x, float y, Stage s) {
        // call constructor form Actor class
        super();
        // perform additional initialization tasks
        setPosition(x, y);

        // initialize animation data;
        s.addActor(this);
        animation = null;
        elapsedTime = 0;
        animationPaused = false;

        // adding velocity
        velocityVec = new Vector2(0, 0);

        //adding acceleration
        accelerationVec = new Vector2(0, 0);
        acceleration = 0;

        //adding deceleration
        maxSpeed = 1000;
        deceleration = 0;


    }

    public void setAnimation(Animation<TextureRegion> anim) {
        animation = anim;
        TextureRegion tr = animation.getKeyFrame(0); // get the current frame of the animation based on the elapsed time("stattime") returns a texture region
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();
        setSize(w, h);
        setOrigin(w / 2, h / 2);

        // using a polygon as the shape object
        if (boundaryPolygon == null) {
            setBoundaryRectangle();
        }
    }

    public void setAnimationPaused(boolean pause) {
        animationPaused = pause;
    }

    public void act(float dt) {
        super.act(dt);
        if (!animationPaused) {
            elapsedTime += dt;
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        // apply color tint effect;
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if (animation != null && isVisible()) {
            batch.draw(animation.getKeyFrame(elapsedTime),
                    getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                    getHeight(), getScaleX(), getScaleY(), getRotation());
        }
        super.draw(batch, parentAlpha);

    }

    public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop) {
        int fileCount = fileNames.length;
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        for (int n = 0; n < fileCount; n++) {
            String fileName = fileNames[n];
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }

        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
        if (loop) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        } else {
            anim.setPlayMode(PlayMode.NORMAL);
        }

        if (animation == null) {
            setAnimation(anim);
        }
        return anim;
    }

    // animation using a spreadSheet

    public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols, float frameDuration, boolean loop) {
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);


        Array<TextureRegion> textureArray = new Array<TextureRegion>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++)
                textureArray.add(temp[r][c]);
        }

        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
        if (loop) {
            animation.setPlayMode(PlayMode.LOOP);
        } else {
            anim.setPlayMode(PlayMode.NORMAL);
        }

        if (animation == null) {
            setAnimation(anim);
        }
        return anim;
    }


    // for still images
    public Animation<TextureRegion> loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;

        return loadAnimationFromFiles(fileNames, 1, true);
    }

    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(elapsedTime);
    }

    // adding speed
    public void setSpeed(float speed) {
        // if length is zero, then assme motion angle is zero degrees
        if (velocityVec.len() == 0) {
            velocityVec.set(speed, 0);

        } else {
            velocityVec.setLength(speed);
        }
    }

    public float getSpeed() {
        return velocityVec.len();
    }

    // .velocityVector.setAngle() is deprecated
    public void setMotionAngle(float angel) {
        velocityVec.setAngleDeg(angel);
    }

    // .velocityVector.angle() is deprecated
    // to make the object face the angle of the movement
    public float getMotionAngle() {
        return velocityVec.angleDeg();
    }

    public boolean isMoving() {
        return (getSpeed() > 0);
    }

    public void setAcceleration(float acc) {
        acceleration = acc;
    }

    // addint the new vector to the acceleration vector and specifying the angle
    public void accelerateAtAngle(float angle) {
        accelerationVec.add(new Vector2(acceleration, 0).setAngleDeg(angle));
    }

    public void accelerateForward() {
        accelerateAtAngle(getRotation());
    }

    public void setMaxSpeed(float ms) {
        maxSpeed = ms;
    }

    public void setDeceleration(float dec) {
        deceleration = dec;
    }

    // handling movement
    public void applyPhysics(float dt) {
        // apply acceleration
        velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);
        float speed = getSpeed();

        // decrease speed (decelerate) when not accelerating
        if (accelerationVec.len() == 0) {
            speed -= deceleration * dt;
        }

        // keep speed within set bounds
        speed = MathUtils.clamp(speed, 0, maxSpeed);

        //update velocity
        setSpeed(speed);

        // apply velocity
        moveBy(velocityVec.x * dt, velocityVec.y * dt);

        // reset acceleration
        accelerationVec.set(0, 0);
    }


    // adding collision
    // adding a method to create a rectangle polygon

    public void setBoundaryRectangle() {
        float w = getWidth();
        float h = getHeight();
        float[] vertices = {0, 0, w, 0, w, h, 0, h};
        boundaryPolygon = new Polygon(vertices);

        //
    }

    // adding collision
    // this method requires the with and height of the object to be set correctly do not call this method before setting the width and height
    public void setBoundaryPolygon(int numSides) {
        float w = getWidth();
        float h = getHeight();

        float[] vertices = new float[2 * numSides];
        for (int i = 0; i < numSides; i++) {
            float angle = i * 6.28f / numSides;
            //x-coordinate
            vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2;
            //y-coordinate
            vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2;
        }

        boundaryPolygon = new Polygon(vertices);
    }

    // getBoundaryPolygun returns the collision polygon for this Actor, adjusting it according to the Actor object's current parameters

    public Polygon getBoundaryPolygon() {
        boundaryPolygon.setPosition(getX(), getY());
        boundaryPolygon.setOrigin(getOriginX(), getOriginY());
        boundaryPolygon.setRotation(getRotation());
        boundaryPolygon.setScale(getScaleX(), getScaleY());
        return boundaryPolygon;
    }

    /**
     * Unlike the Rectangle class, which has its own overlaps method, the Polygon class does not. Fortunately,
     * another utility class provided by LibGDX, called Intersector, does have such a method
     **/
    public boolean overlaps(BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();

        /** this method contains a preliminary check to see if the rectangles surrounding the polygons intersect (which is a
         much simpler calculation). If these much larger rectangles do not intersect, then it would be impossible for
         the polygons to intersect, and so the method can immediately return false. Otherwise, there is a possibility
         that the polygons could intersect, in which case the method returns the result of this more precise test,
         which is performed by the static method overlapConvexPolygons from the Intersector class.
         **/

        /** initial test to improve performance **/
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {
            return false;
        }

        return Intersector.overlapConvexPolygons(poly1, poly2);
    }

    /**
     * Recall that the Actor method setPosition actually sets the bottom-left corner of an Actor object to
     * a given location. In order to center an Actor at a given location, shift it from this location
     * by half its width along the x direction and half its width along the y direction.
     **/
    public void centerAtPosition(float x, float y) {
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }

    public void centerAtActor(BaseActor other) {
        centerAtPosition(other.getX() + other.getWidth() / 2, other.getY() + other.getHeight() / 2);
    }

    // to change teh transparency of the actor object usin gthe alpha value of the color associated with it.
    public void setOpacity(float opacity) {
        this.getColor().a = opacity;
    }

    /**
     * the idea is to calculate the direction corresponding to the minimal distance the character needs
     * to be moved so that there is no overlap, and move accordingly.
     * In the Intersector class, there is a variation of the overlapConvexPolygons
     * method that takes three parameters. The first two parameters are the polygons being checked for overlap.
     * The third parameter is a MinimumTranslationVector object, which is used to store the minimum distance
     * and direction required to reposition the polygons as a float named depth and a Vector2 named normal. If
     * there is an overlap between the polygons, then the Actor calling the method will be moved according to the
     * minimum translation vector, and there will no longer be an overlap between the two Actor objects
     **/

    public Vector2 preventOverlap(BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();

        // inital test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {
            return null;
        }

        Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);

        if (!polygonOverlap) {
            return null;
        }

        this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
        return mtv.normal;
    }


    /**
     * The general idea is to write a static4 method that
     * takes a Stage and a class name as parameters, extracts the list of actors from the stage, and creates a new
     * list containing those actors that are instances of the class with the given name (or extensions of the class).
     **/
    public static ArrayList<BaseActor> getList(Stage stage, String className) {
        ArrayList<BaseActor> list = new ArrayList<BaseActor>();
        Class theClass = null;

        try {
            System.out.println("the className" + Class.forName(className));

            theClass = Class.forName(className); // returns the class object associated with the class

        } catch (Exception e) {
//        } catch (ClassNotFoundException e) {

//            error.printStackTrace();
            e.printStackTrace();
            System.out.println("THE ERROR: " + e);

            System.out.println("ERROR OCCURED!!!!!!!!!!!!!!!!!!!!");


//            return list;
        }

        for (Actor a : stage.getActors()) {
            if (theClass.isInstance(a)) {
                System.out.println("Instance of the actor: " + theClass.isInstance(a));
                list.add((BaseActor) a);
            }
        }
        return list;
    }


//    public static ArrayList<BaseActor> getList(Stage stage, String className){
//        ArrayList<BaseActor> list = new ArrayList<>();
//        Reflections reflections = new Reflections("com")
//    }

    /**
     * to know how many instances of a particular type of object
     **/
    public static int count(Stage stage, String className) {
        return getList(stage, className).size();
    }

    // adding world bounderies
    public static void setWorldBounds(float width, float height) {
        worldBounds = new Rectangle(0, 0, width, height);
    }

    public static void setWorldBounds(BaseActor ba) {
        setWorldBounds(ba.getWidth(), ba.getHeight());
    }

    public void boundToWorld() {
        //check left edge
        if (getX() < 0) {
            setX(0);
        }
        //check right edge
        if (getX() + getWidth() > worldBounds.width) {
            setX(worldBounds.width - getWidth());
        }
        // check bottom edge
        if (getY() < 0) {
            setY(0);
        }
        // check top edge
        if (getY() + getHeight() > worldBounds.height) {
            setY(worldBounds.height - getHeight());
        }
    }

    public void alignCamera() {
        Camera cam = this.getStage().getCamera();
        Viewport v = this.getStage().getViewport();


        // center camera on actor
        cam.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);

        //bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x, cam.viewportWidth / 2, worldBounds.width - cam.viewportWidth / 2);
        cam.position.y = MathUtils.clamp(cam.position.y, cam.viewportHeight / 2, worldBounds.height - cam.viewportHeight / 2);
        cam.update();
    }

    /**
     * If this object moves completely past the world bounds,
     * adjust its position to the opposite side of the world.
     */
    public void wrapAroundWorld() {
        if (getX() + getWidth() < 0) {
            setX(worldBounds.width);
        }

        if (getX() > worldBounds.width) {
            setX(-getWidth());
        }

        if (getY() + getHeight() < 0) {
            setY(worldBounds.height);
        }

        if (getY() > worldBounds.height) {
            setY(-getHeight());
        }
    }

    // to check if the character is near another
    public boolean isWithinDistance(float distance, BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        float scaleX = (this.getWidth() + 2 * distance) / this.getWidth();
        float scaleY = (this.getHeight() + 2 * distance) / this.getHeight();
        poly1.setScale(scaleX, scaleY);

        Polygon poly2 = other.getBoundaryPolygon();
        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {
            return false;
        }

        return Intersector.overlapConvexPolygons(poly1, poly2);

    }


}
