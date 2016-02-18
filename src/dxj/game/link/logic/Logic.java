package dxj.game.link.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dxj.game.link.object.Board;
import dxj.game.link.object.Config;
import android.graphics.Point;



public class Logic {
	private Board board;
	private Config config;
	public List<Point> link(Point prePoint,Point nextPoint)
	{
		List<Point> points=new ArrayList<Point>();//关键点
		List<Point> topVerticalChannel,leftHorizontalChannel;
		List<Point> bottomVerticalChannel,rightHorizontalChannel;
		int minRangeX,minRangeY,maxRangeX,maxRangeY;
		int distance=10000;
		Point leftPoint,topPoint,rightPoint,bottomPoint;
		Point Corner,leftTempPoint,topTempPoint,rightTempPoint,bottomTempPoint;
		if(prePoint.equals(nextPoint))return points;
		if(Math.abs(board.getBoard()[prePoint.x][prePoint.y].getId()-board.getBoard()[nextPoint.x][nextPoint.y].getId())!=1)
		{
			return points;
		}
		if(prePoint.x<nextPoint.x)
		{
			leftPoint=prePoint;
			rightPoint=nextPoint;
		}
		else
		{
			leftPoint=nextPoint;
			rightPoint=prePoint;
		}
		if(prePoint.y<nextPoint.y)
		{
			topPoint=prePoint;
			bottomPoint=nextPoint;
		}
		else
		{
			topPoint=nextPoint;
			bottomPoint=prePoint;
		}
		//直线
		if(prePoint.y==nextPoint.y)
		{
			
			if(!isXBlock(leftPoint,rightPoint))
			{
				points.add(prePoint);
				points.add(nextPoint);
				return points;
			}

		}
		if(prePoint.x==nextPoint.x)
		{
			if(!isYBlock(topPoint,bottomPoint))
			{
				points.add(prePoint);
				points.add(nextPoint);
				return points;
			}
		}
		leftHorizontalChannel=getHorizontalChannel(leftPoint);
		topVerticalChannel=getVerticalChannel(topPoint);
		rightHorizontalChannel=getHorizontalChannel(rightPoint);
		bottomVerticalChannel=getVerticalChannel(bottomPoint);
		//2折线
		if(leftHorizontalChannel.get(leftHorizontalChannel.size()-1).x>=rightPoint.x)
		{
			if(topPoint==leftPoint)
			{
				if(bottomVerticalChannel.get(0).y<=leftPoint.y)
				{
					Corner=new Point(rightPoint.x,leftPoint.y);
					points.add(leftPoint);
					points.add(Corner);
					points.add(rightPoint);
					return points;
				}
			}
			else
			{
				if(topVerticalChannel.get(topVerticalChannel.size()-1).y>=leftPoint.y)
				{
					Corner=new Point(rightPoint.x,leftPoint.y);
					points.add(leftPoint);
					points.add(Corner);
					points.add(rightPoint);
					return points;
				}
			}
		}
		if(rightHorizontalChannel.get(0).x<=leftPoint.x)
		{
			if(topPoint==leftPoint)
			{
				if(topVerticalChannel.get(topVerticalChannel.size()-1).y>=rightPoint.y)
				{
					Corner=new Point(leftPoint.x,rightPoint.y);
					points.add(leftPoint);
					points.add(Corner);
					points.add(rightPoint);
					return points;
				}
			}
			else
			{
				if(bottomVerticalChannel.get(0).y<=rightPoint.y)
				{
					Corner=new Point(leftPoint.x,rightPoint.y);
					points.add(leftPoint);
					points.add(Corner);
					points.add(rightPoint);
					return points;
				}
			}
		}
		//3折线（可以解出直线和2折线）
		if(rightHorizontalChannel.get(0).x<leftHorizontalChannel.get(0).x)
		{
			minRangeX=leftHorizontalChannel.get(0).x;
		}
		else
		{
			minRangeX=rightHorizontalChannel.get(0).x;
		}
		if(rightHorizontalChannel.get(rightHorizontalChannel.size()-1).x<leftHorizontalChannel.get(leftHorizontalChannel.size()-1).x)
		{
			maxRangeX=rightHorizontalChannel.get(rightHorizontalChannel.size()-1).x;
		}
		else
		{
			maxRangeX=leftHorizontalChannel.get(leftHorizontalChannel.size()-1).x;
		}
		if(minRangeX<=maxRangeX)
		{

			for(int x=minRangeX;x<=maxRangeX;x++)
			{
				topTempPoint=new Point(x,topPoint.y);
				bottomTempPoint=new Point(x,bottomPoint.y);
				if(!isYBlock(topTempPoint,bottomTempPoint))
				{
					if(Math.abs(x-minRangeX)+Math.abs(x-maxRangeX)<distance)
					{
						distance=Math.abs(x-minRangeX)+Math.abs(x-maxRangeX);
						points.clear();
						points.add(topPoint);
						points.add(topTempPoint);
						points.add(bottomTempPoint);
						points.add(bottomPoint);
						if(distance==Math.abs(rightPoint.x-leftPoint.x))
							return points;
					}
				}
			}
		}
		if(bottomVerticalChannel.get(0).y<topVerticalChannel.get(0).y)
		{
			minRangeY=topVerticalChannel.get(0).y;
		}
		else
		{
			minRangeY=bottomVerticalChannel.get(0).y;
		}
		if(bottomVerticalChannel.get(bottomVerticalChannel.size()-1).y<topVerticalChannel.get(topVerticalChannel.size()-1).y)
		{
			maxRangeY=bottomVerticalChannel.get(bottomVerticalChannel.size()-1).y;
		}
		else
		{
			maxRangeY=topVerticalChannel.get(topVerticalChannel.size()-1).y;
		}
		if(minRangeY<=maxRangeY)
		{
			for(int y=minRangeY;y<=maxRangeY;y++)
			{
				leftTempPoint=new Point(leftPoint.x,y);
				rightTempPoint=new Point(rightPoint.x,y);
				if(!isXBlock(leftTempPoint,rightTempPoint))
				{
					if(Math.abs(y-minRangeY)+Math.abs(y-maxRangeY)<distance)
					{
						distance=Math.abs(y-minRangeY)+Math.abs(y-maxRangeY);
						points.clear();
						points.add(leftPoint);
						points.add(leftTempPoint);
						points.add(rightTempPoint);
						points.add(rightPoint);
						if(distance==Math.abs(topPoint.y-bottomPoint.y))
							return points;
					}
				}
			}
		}
		return points;
	}
	private boolean isXBlock(Point leftPoint, Point rightPoint)
	{
		for (int x = leftPoint.x+1; x < rightPoint.x;x++)
		{
			if (board.getBoard()[x][leftPoint.y]!=null)
			{// 有障碍
				return true;
			}
		}
		return false;
	}
	private boolean isYBlock(Point topPoint,Point bottomPoint)
	{
		for(int y=topPoint.y+1;y<bottomPoint.y;y++)
		{
			if(board.getBoard()[topPoint.x][y]!=null)
			{
				return true;
			}
		}
		return false;
	}
	private List<Point> getVerticalChannel(Point point)
	{
		List<Point> verticalChannel=new ArrayList<Point>();

		int y=point.y-1;
		while(y>-1)
		{
			if(board.getBoard()[point.x][y]!=null)
			{
				break;
			}
			verticalChannel.add(new Point(point.x,y));
			y--;
		}
		Collections.reverse(verticalChannel);
		verticalChannel.add(point);
		y=point.y+1;
		while(y<config.getYSize())
		{
			if(board.getBoard()[point.x][y]!=null)
			{
				break;
			}
			verticalChannel.add(new Point(point.x,y));
			y++;
		}

		return verticalChannel;
	}
	private List<Point> getHorizontalChannel(Point point)
	{
		List<Point> horizontalChannel=new ArrayList<Point>();

		int x=point.x-1;
		while(x>-1)
		{
			if(board.getBoard()[x][point.y]!=null)
			{
				break;
			}
			horizontalChannel.add(new Point(x,point.y));
			x--;
		}
		Collections.reverse(horizontalChannel);
		horizontalChannel.add(point);
		x=point.x+1;
		while(x<config.getXSize())
		{
			if(board.getBoard()[x][point.y]!=null)
			{
				break;
			}
			horizontalChannel.add(new Point(x,point.y));
			x++;
		}

		return horizontalChannel;
	}
	public void setBoard(Board board)
	{
		this.board=board;
	}
	public void setConfig(Config config)
	{
		this.config=config;
	}
}
