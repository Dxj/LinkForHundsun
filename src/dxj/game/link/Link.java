package dxj.game.link;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dxj.game.link.R;
import dxj.game.link.logic.Logic;
import dxj.game.link.object.Board;
import dxj.game.link.object.Config;
import dxj.game.link.view.BoardView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Description: <br/>
 * site: <a href="http://www.crazyit.org">crazyit.org</a> <br/>
 * Copyright (C), 2001-2012, Yeeku.H.Lee <br/>
 * This program is protected by copyright laws. <br/>
 * Program Name: <br/>
 * Date:
 * @author Yeeku.H.Lee kongyeeku@163.com
 * @version 1.0
 */

public class Link extends Activity
{
	private Config config;
	private Board board;
	private Logic logic;
	// 游戏界面
	private BoardView boardView;
	// 开始按钮
	private Button startButton;
	// 记录剩余时间的TextView
	private TextView timeTextView;
	private Vibrator vibrator;
	private Timer timer;
	private int gameTime;
	private AlertDialog.Builder lostDialog;
	private AlertDialog.Builder successDialog;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}
	// 初始化游戏的方法
	private void init()
	{
		config= new Config(6, 7, 40, 55 , 100, this);
		board= new Board(config);
		logic= new Logic();
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		timer=new Timer();
		gameTime=-1;
		// 得到游戏区域对象
		boardView = (BoardView) findViewById(R.id.boardView);
		// 获取显示剩余时间的文本框
		timeTextView = (TextView) findViewById(R.id.timeText);
		// 获取开始按钮
		startButton = (Button) this.findViewById(R.id.startButton);
		logic.setBoard(board);
		logic.setConfig(config);
		boardView.setConfig(config);
		handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
					case 0x123:
						timeTextView.setText("剩余时间： " + gameTime);
						gameTime--;
						// 时间小于0, 游戏失败
						if (gameTime < 0)
						{
							timer.cancel();
							timer=null;
							lostDialog.show();
						}
						break;
				}
			}
		};
		// 为开始按钮的单击事件绑定事件监听器
		startButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View source)
			{
				gameTime=config.getTime();
				board.createBoard(config);
				boardView.setBoard(board.getBoard());
				boardView.postInvalidate();
				setTimer();
			}
		});
		
		// 为游戏区域的触碰事件绑定监听器
		this.boardView.setOnTouchListener(new View.OnTouchListener()
		{
			public boolean onTouch(View view, MotionEvent e)
			{
				List<Point> points;
				Point point=ConvertToXY(e.getX(),e.getY());
				if (e.getAction() == MotionEvent.ACTION_DOWN)
				{
					if(point.x>=0&&point.x<config.getXSize()&&point.y>=0&&point.y<config.getYSize())
					{
						if(board.getBoard()[point.x][point.y]==null)return true;
						if(board.getSelect()==null)
						{
							board.setSelect(point.x, point.y);
							boardView.setSelect(point.x, point.y);
						}
						else
						{
							points=logic.link(board.getSelect(), point);
							if(points.size()==0)
							{
								board.setSelect(point.x,point.y);
								boardView.setSelect(point.x,point.y);
							}
							else
							{
								boardView.setPoints(points);
								boardView.setSelect(-1,-1);
								boardView.postInvalidate();
								vibrator.vibrate(100);
								board.getBoard()[board.getSelect().x][board.getSelect().y]=null;
								board.getBoard()[point.x][point.y]=null;
								board.setSelect(-1, -1);
								if(!hasImage())
								{
									timer.cancel();
									timer=null;
									gameTime=-1;
									successDialog.show();
								}
							}
							
						}
					}
				}
				if (e.getAction() == MotionEvent.ACTION_UP)
				{
					boardView.postInvalidate();
				}
				return true;
			}
		});

		// 初始化游戏胜利的对话框
		successDialog = new AlertDialog.Builder(this).setTitle("Success")
				.setMessage("游戏胜利！ 重新开始").setIcon(R.drawable.success)
				.setPositiveButton("确定",
			new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					gameTime=config.getTime();
					board.createBoard(config);
					boardView.setBoard(board.getBoard());
					boardView.postInvalidate();
					setTimer();
				}
			});
		lostDialog=new AlertDialog.Builder(this).setTitle("Lost")
				.setMessage("游戏失败！ 重新开始").setIcon(R.drawable.lost)
				.setPositiveButton("确定", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						gameTime=config.getTime();
						board.createBoard(config);
						boardView.setBoard(board.getBoard());
						boardView.postInvalidate();
						setTimer();
					}
				});
	}
	private Point ConvertToXY(float x,float y)
	{
		return new Point((int)(x-config.getBeginX())/config.IMAGE_WIDTH,(int)(y-config.getBeginY())/config.IMAGE_HEIGHT);
	}
	private void setTimer()
	{
		if(timer!=null)
		{
			timer.cancel();
			timer=null;
		}
		timer=new Timer();
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				handler.sendEmptyMessage(0x123);
			}
		}, 0, 1000);		
	}
	private boolean hasImage()
	{
		for(int x=0;x<config.getXSize();x++)
		{
			for(int y=0;y<config.getYSize();y++)
			{
				if(board.getBoard()[x][y]!=null)
				{
					return true;
				}
			}
		}
		return false;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(gameTime>=0)
		{
			setTimer();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		timer.cancel();
		timer=null;
		super.onPause();
	}

}