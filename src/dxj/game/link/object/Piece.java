package dxj.game.link.object;

import android.graphics.Bitmap;

public class Piece {
	private Bitmap bitmap;
	private Integer id;
	public Bitmap getBitmap()
	{
		return bitmap;
	}
	public Integer getId()
	{
		return id;
	}
	public void setBitmap(Bitmap bitmap)
	{
		this.bitmap=bitmap;
	}
	public void setId(Integer id)
	{
		this.id=id;
	}
}
