package dxj.game.link.object;

import android.content.Context;

public class Config {
	public static final int IMAGE_WIDTH = 40;
	public static final int IMAGE_HEIGHT = 40;
	private int xSize;
	private int ySize;
	private int beginX;
	private int beginY;
	private int time;
	private Context context;
	public Config(int xSize,int ySize,int beginX,int beginY,int time,Context context)
	{
		this.xSize=xSize;
		this.ySize=ySize;
		this.beginX=beginX;
		this.beginY=beginY;
		this.time=time;
		this.context=context;
	}
	public int getXSize()
	{
		return xSize;
	}
	public int getYSize()
	{
		return ySize;
	}
	public int getBeginX()
	{
		return beginX;
	}
	public int getBeginY()
	{
		return beginY;
	}
	public int getTime()
	{
		return time;
	}
	public Context getContext()
	{
		return context;
	}
}
