package dxj.game.link.view;

import java.util.List;

import dxj.game.link.R;
import dxj.game.link.object.Config;
import dxj.game.link.object.Piece;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class BoardView extends View{

	private Paint paint;
	private Piece piece[][];
	private List<Point> points;
	private Bitmap selectImage;
	private int selectX,selectY;
	private Config config;
	public BoardView(Context context, AttributeSet attrs)
	{
		super(context,attrs);
		paint=new Paint();
		paint.setColor(Color.RED);
		paint.setStrokeWidth(3);
		selectImage=BitmapFactory.decodeResource(context.getResources(),
				R.drawable.selected);
		selectX=-1;
		selectY=-1;
		points=null;
	}
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		if(config==null)return;
		if(piece!=null)
		{
			for(int x=0;x<config.getXSize();x++)
			{
				for(int y=0;y<config.getYSize();y++)
				{
					if(piece[x][y]!=null)
					{
						canvas.drawBitmap(piece[x][y].getBitmap()
								,config.getBeginX()+config.IMAGE_WIDTH*x
								,config.getBeginY()+config.IMAGE_HEIGHT*y
								,null);						
					}
				}
			}
		}
		if(selectX!=-1)
		{
			canvas.drawBitmap(selectImage
					,config.getBeginX()+config.IMAGE_WIDTH*selectX
					,config.getBeginY()+config.IMAGE_HEIGHT*selectY
					,null);
		}
		drawLine(canvas);
	}
	public void setBoard(Piece piece[][])
	{
		this.piece=piece;
	}
	public void setPoints(List<Point> points)
	{
		this.points=points;
	}
	public void setSelect(int selectX,int selectY)
	{
		this.selectX=selectX;
		this.selectY=selectY;
	}
	public void setConfig(Config config)
	{
		this.config=config;
	}
	private void drawLine(Canvas canvas)
	{
		Point currentPoint,nextPoint;
		if(points==null)return;
		for(int i=0;i<points.size()-1;i++)
		{
			currentPoint=points.get(i);
			nextPoint=points.get(i+1);
			canvas.drawLine(config.getBeginX()+config.IMAGE_WIDTH*currentPoint.x+config.IMAGE_WIDTH/2
					, config.getBeginY()+config.IMAGE_HEIGHT*currentPoint.y+config.IMAGE_HEIGHT/2
					, config.getBeginX()+config.IMAGE_WIDTH*nextPoint.x+config.IMAGE_WIDTH/2
					, config.getBeginY()+config.IMAGE_HEIGHT*nextPoint.y+config.IMAGE_HEIGHT/2
					, paint);
		}
		points=null;			
	}
}
