package towerDefense;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class World extends BasicGameState {
	public static int ID = 2;
	static ArrayList<Enemy> enemies;
	private Level l;

	public int getID(){
		return ID;
	}
	
	public static void reset(){
		
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		l = new Level();
		//System.out.println("Level created");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		/*
		l.render();
		for(Enemy e : enemies){
			e.render();
		}
		*/
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		/*
		l.update()
		for(Enemy e : enemies){
			e.update();
		}
		*/
	}
	
	public void keyReleased(int key, char c){
	}
	
	public void keyPressed(int key, char c){

	}
}
