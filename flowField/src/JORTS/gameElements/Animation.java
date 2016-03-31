package JORTS.gameElements;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import JORTS.core.GameWindow;
import JORTS.core.Main;

public class Animation implements Observer,Drawable{

	int totalFrames=9;
	int msPerFrame=10;
	
	int currentFrame=0;
	int currentMs=0;
	
	Vector2i frameSize;
	Vector2i frameGrid;
	Texture tex;
	RectangleShape img;
	@Override
	public void update(Observable arg0, Object arg1) {
		currentMs+=Main.deltaT;
		if(currentMs>msPerFrame)
		{
			currentFrame++;
			currentMs=0;
			updateSprite();
		}
	}
	public Animation(String file,Vector2i frameGrid,int msPerFrame,Vector2i frameSize)
	{
		this.frameGrid=frameGrid;
		totalFrames=frameGrid.x*frameGrid.y;
		this.msPerFrame=msPerFrame;
		this.frameSize=frameSize;
		tex=new Texture();
		try {
			tex.loadFromFile(Paths.get("animations//"+file));
		} catch(IOException ex) {
		    ex.printStackTrace();
		}
		img = new RectangleShape();
		img.setTexture(tex);
		img.setTextureRect(new IntRect(0, 0, this.frameSize.x, this.frameSize.y));		
		
		AnimationManager.animations.add(this);
		Main.game.addObserver(this);
		updateSprite();
	}
	public void updateSprite()
	{
		if(currentFrame<totalFrames	)
			img.setTextureRect(new IntRect((currentFrame%frameGrid.y) * frameSize.x, (currentFrame/frameGrid.y) * frameSize.y, frameSize.x, frameSize.y));
		else
		{
			Main.game.deleteObserver(this);
			AnimationManager.animations.remove(this);
		}
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		arg0.draw(img);
	}
	public static void createExplosionAnimation(Vector2f pos,Vector2f size)
	{
		Animation a=new Animation("explosion.png",new Vector2i(4,4),30,new Vector2i(64,64));
		a.img.setOrigin(Vector2f.div(size, 2));
		a.img.setSize(size);
		a.img.setPosition(pos);
	}
	public static void createDeadBody(Vector2f pos,Vector2f size)
	{
		Animation a=new Animation("skull.png",new Vector2i(1,1),2000,new Vector2i(48,48));
		a.img.setOrigin(Vector2f.div(size, 2));
		a.img.setSize(size);
		a.img.setPosition(pos);
	}
}
