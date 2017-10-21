package snake;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Bonus {
	
	public Point pt;
	
	public static enum bonusType {bGrandis,bRetrecis,bRapide,bLent,bMort,bInverseBonus,bInverseMalus};
	
	public bonusType type;
	public Image imageBonus;
	public int rayon;
	
	
	
	public Bonus(Point pt,int numBonus){
		this(pt,bonusType.values()[numBonus]);
	}
	
	public Bonus(Point pt,bonusType bonus){
		this.pt=pt;
		this.type = bonus;
		
		try{
			this.imageBonus = new Image(imagePath());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		this.rayon = 8;
	}
	
	public void applyBonus(Snake s){
		switch(this.type){
		case bGrandis:
			s.grandir();
		break;
		case bRetrecis:
			s.retrecir();
		break;
		case bRapide:
			s.plusRapide();
		break;
		case bLent:
			s.plusLent();
		break;
		case bMort:
			
		break;
		case bInverseBonus:
			
		break;
		}
	}
	
	private String imagePath(){
		String path = "images/snake/";
		switch(type){
		case bGrandis:
			path+="Grand";
		break;
		case bRetrecis:
			path+="Petit";
		break;
		case bRapide:
			path+="Lapin";
		break;
		case bLent:
			path+="Tortue";
		break;
		case bMort:
			path+="Remi";
		break;
		case bInverseBonus:
			path+="InverseBonus";
		break;
		case bInverseMalus:
			path+="InverseMalus";
		break;
		}
		
		return path+".png";
	}
	
	public Boolean isInBonus(Point p){
		return(pt.x-p.x <= rayon &&  pt.y - p.y <= rayon);
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		imageBonus.draw(pt.x-10*rayon,pt.y-10*rayon,10+20*rayon,10+20*rayon);
	}
	
}
