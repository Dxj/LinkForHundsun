package dxj.game.link.object;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dxj.game.link.R;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class Board {
	private Piece piece[][];
	private int selectX,selectY;
	public Board(Config config)
	{
		piece=new Piece[config.getXSize()][config.getYSize()];
		selectX=-1;
	}
	public Piece[][] getBoard()
	{
		return piece;
	}
	public static int getPiecePairsNum()
	{
		try
		{
			int pairsNum=0;
			// 得到R.drawable所有的属性, 即获取drawable目录下的所有图片
			Field[] drawableFields = R.drawable.class.getFields();

			for (Field field : drawableFields)
			{
				// 如果该Field的名称以p_开头
				if (field.getName().indexOf("p_") != -1)
				{
					pairsNum++;
				}
			}
			return pairsNum;
		}
		catch (Exception e)
		{
			return -1;
		}
	}
	public static List<Integer> getRandomPairs(int pairsNum,int size)
	{
		// 创建一个随机数生成器
		Random random = new Random();
		// 创建结果集合
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < size; i++)
		{
			try
			{
				// 随机获取一个数字
				int index = random.nextInt(pairsNum);
				// 添加到结果集中
				result.add(index);
			}
			catch (IndexOutOfBoundsException e)
			{
				return result;
			}
		}
		return result;
	}
	public void createBoard(Config config)
	{
		int i=0;
		List<Integer> randomPairs=getRandomPairs(getPiecePairsNum(),config.getXSize()*config.getYSize()/2);
		List<Integer> randomPictures=new ArrayList<Integer>();
		for (Integer Pair : randomPairs)
		{
			randomPictures.add(Pair*10);
			randomPictures.add(Pair*10+1);
		}
		Collections.shuffle(randomPictures);
		for(int x=0;x<config.getXSize();x++)
		{
			for(int y=0;y<config.getYSize();y++)
			{
				piece[x][y]=new Piece();
				piece[x][y].setId(randomPictures.get(i));
				if(randomPictures.get(i)%10==0)
				{
					try {
						piece[x][y].setBitmap(BitmapFactory.decodeResource(
								config.getContext().getResources(), R.drawable.class.getField("p_"+((Integer)(randomPictures.get(i)/10)).toString()).getInt(R.drawable.class)));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(randomPictures.get(i)%10==1)
				{
					try {
						piece[x][y].setBitmap(BitmapFactory.decodeResource(
								config.getContext().getResources(), R.drawable.class.getField("t_"+((Integer)(randomPictures.get(i)/10)).toString()).getInt(R.drawable.class)));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
				i++;
			}
		}
	}
	public Point getSelect()
	{
		if(selectX==-1)return null;
		return new Point(selectX,selectY);
	}
	public void setSelect(int selectX,int selectY)
	{
		this.selectX=selectX;
		this.selectY=selectY;
	}

}
