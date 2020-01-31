package de.mikaaust.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Eine Game App mit Elon Musk
 *
 *
 * @author Mika Aust
 * @version 1.0.0
 *
 *
 */
public class MyGame extends ApplicationAdapter {
	/**
	 * @param args Definition alle Variablen
	 */
	SpriteBatch batch;
	Texture background;
	Texture [] man;
	int manY, manX;
	int manState = 0;
	int pause = 0;
	int gamestate = 0;
	BitmapFont font;
	int score =0;
	Texture dizzy;
	float gravity = 0.5f;
	float velocity = 0;
	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();
	Texture bomb;
	Texture coin;
	Random random;
	int coinCount;
	int bombCount;
	Rectangle manRectangle;
	@Override
	/**
	 * Definition der Bilder etc.
	 */
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[7];
		man[0] = new Texture("muskmoving1.png");
		man[1] = new Texture("muskmoving2.png");
		man[2] = new Texture("muskmoving3.png");
		man[3] = new Texture("muskmoving4.png");
		man[4] = new Texture("muskmoving5.png");
		man[5] = new Texture("muskmoving6.png");
		man[6] = new Texture("muskmoving7.png");
		dizzy = new Texture("dizzy1.png");
		random = new Random();
		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		manY = Gdx.graphics.getHeight()/2;
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
	}
	public void createCoin(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int) height);
		coinXs.add(Gdx.graphics.getWidth());
	}
	public void createBomb(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int) height);
		bombXs.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {
		batch.begin();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (gamestate == 0){
			if ( Gdx.input.justTouched()){
				gamestate = 1;
			}
		}
		else if(gamestate == 1) {

			if (coinCount < 50) {
				coinCount++;
			} else {
				coinCount = 0;
				createCoin();
			}
			coinRectangles.clear();
			for (int i = 0; i < coinXs.size(); i++) {
				batch.draw(coin, coinXs.get(i), coinYs.get(i));
				coinXs.set(i, coinXs.get(i) - 4);
				coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
			}
			if (bombCount < 250) {
				bombCount++;
			} else {
				bombCount = 0;
				createBomb();
			}
			bombRectangles.clear();
			for (int i = 0; i < bombXs.size(); i++) {
				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i) - 4);
				bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}
			if (Gdx.input.justTouched()) {
				velocity -= 10;
			}
			if (pause < 7) {
				pause++;
			} else {
				pause = 0;
				if (manState < man.length - 1) {
					manState++;
				} else {
					manState = 0;
				}
			}
			velocity += gravity;
			manY -= velocity;
			if (manY <= 0) {
				manY = 0;
			}
			if (manY >= Gdx.graphics.getHeight()- man[manState].getHeight()){
				manY =Gdx.graphics.getHeight() - man[manState].getHeight();
				velocity = 5.0f;
			}
			manRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - man[0].getWidth()/2, manY, man[manState].getWidth(), man[manState].getHeight());
			for (int i = 0; i<coinRectangles.size(); i++){
				if (Intersector.overlaps(manRectangle, coinRectangles.get(i))){
					score++;
					coinRectangles.remove(i);
					coinXs.remove(i);
					coinYs.remove(i);
					break;
				}
			}
			for (int i = 0; i<bombRectangles.size(); i++){
				if (Intersector.overlaps(manRectangle, bombRectangles.get(i))){
					gamestate=2;
				}
			}
		}else if (gamestate == 2){
			//Game over
			if (Gdx.input.justTouched()){
				score = 0;
				gamestate = 1;
				velocity = 0;
				coinXs.clear();
				coinYs.clear();
				coinRectangles.clear();
				coinCount = 0;
				bombXs.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombCount = 0;
				manY = Gdx.graphics.getHeight()/2;
			}
		}
		if (gamestate ==2){
			batch.draw(dizzy, Gdx.graphics.getWidth()/2-dizzy.getWidth()/2, manY);
		}else {
			batch.draw(man[manState], Gdx.graphics.getWidth()/2-man[0].getWidth()/2, manY);
		}
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();
	}
	public void dispose(){
		batch.dispose();
		background.dispose();

	

}}
