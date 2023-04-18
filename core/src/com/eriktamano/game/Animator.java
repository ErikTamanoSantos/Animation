package com.eriktamano.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import org.w3c.dom.Text;


public class Animator implements ApplicationListener {

	// Constant rows and columns of the sprite sheet
	private static final int FRAME_COLS = 4, FRAME_ROWS = 2;

	private int width, height, direction = 1;

	Rectangle left, right;

	// Objects used
	Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
	Texture walkSheet;
	SpriteBatch spriteBatch;

	// A variable for tracking elapsed time for the animation
	float stateTime;

	OrthographicCamera camera;

	Texture background;
	TextureRegion bgRegion;

	int posx, posy;

	@Override
	public void create() {

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		Gdx.graphics.setResizable(false);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		camera.update();

		left = new Rectangle(0, 0, width/3, height);
		right = new Rectangle(width*2/3, 0, width/3, height);

		background = new Texture(Gdx.files.internal("topdown_background.jpg"));
		background.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
		bgRegion = new TextureRegion(background);
		posx = 0;
		posy = 0;

		// Load the sprite sheet as a Texture
		walkSheet = new Texture(Gdx.files.internal("Miner_spritesheet.png"));

		// Use the split utility method to create a 2D array of TextureRegions. This is
		// possible because this sprite sheet contains frames of equal size and they are
		// all aligned.
		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / FRAME_COLS,
				walkSheet.getHeight() / FRAME_ROWS);

		// Place the regions into a 1D array in the correct order, starting from the top
		// left, going across first. The Animation constructor requires a 1D array.
		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}

		// Initialize the Animation with the frame interval and array of frames
		walkAnimation = new Animation<TextureRegion>(0.100f, walkFrames);

		// Instantiate a SpriteBatch for drawing and reset the elapsed animation
		// time to 0
		spriteBatch = new SpriteBatch();
		stateTime = 0f;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {
		int touch = virtual_joystick_control();
		if (touch != 0) {
			direction = touch;
		}
		posx += direction * 5;
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		bgRegion.setRegion(posx,posy, width, height);

		// Get current frame of animation for the current stateTime
		TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		spriteBatch.begin();
		// primer pintem el background
		spriteBatch.draw(bgRegion,0,0);
		int spriteWidth = walkSheet.getWidth() / FRAME_COLS;
		int spriteHeight = walkSheet.getHeight() / FRAME_ROWS;
		int x = (width - spriteWidth) / 2;
		int y = (height - spriteHeight) / 2;
		spriteBatch.draw(currentFrame, x+40, y, spriteWidth/2-40, spriteHeight/2, spriteWidth, spriteHeight, direction, 1, 0); // Draw current frame at (50, 50)
		spriteBatch.end();


	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() { // SpriteBatches and Textures must always be disposed
		spriteBatch.dispose();
		walkSheet.dispose();
	}

	protected int virtual_joystick_control() {
		// iterar per multitouch
		// cada "i" és un possible "touch" d'un dit a la pantalla
		for(int i=0;i<10;i++)
			if (Gdx.input.isTouched(i)) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
				// traducció de coordenades reals (depen del dispositiu) a 800x480
				camera.unproject(touchPos);
				if (left.contains(touchPos.x, touchPos.y)) {
					return -1;
				} else if (right.contains(touchPos.x, touchPos.y)) {
					return 1;
				}
			}
		return 0;
	}
}

