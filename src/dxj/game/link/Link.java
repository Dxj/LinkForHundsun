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
	// ��Ϸ����
	private BoardView boardView;
	// ��ʼ��ť
	private Button startButton;
	// ��¼ʣ��ʱ���TextView
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
	// ��ʼ����Ϸ�ķ���
	private void init()
	{
		config= new Config(6, 7, 40, 55 , 100, this);
		board= new Board(config);
		logic= new Logic();
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		timer=new Timer();
		gameTime=-1;
		// �õ���Ϸ�������
		boardView = (BoardView) findViewById(R.id.boardView);
		// ��ȡ��ʾʣ��ʱ����ı���
		timeTextView = (TextView) findViewById(R.id.timeText);
		// ��ȡ��ʼ��ť
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
						timeTextView.setText("ʣ��ʱ�䣺 " + gameTime);
						gameTime--;
						// ʱ��С��0, ��Ϸʧ��
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
		// Ϊ��ʼ��ť�ĵ����¼����¼�������
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
		
		// Ϊ��Ϸ����Ĵ����¼��󶨼�����
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

		// ��ʼ����Ϸʤ���ĶԻ���
		successDialog = new AlertDialog.Builder(this).setTitle("Success")
				.setMessage("��Ϸʤ���� ���¿�ʼ").setIcon(R.drawable.success)
				.setPositiveButton("ȷ��",
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
				.setMessage("��Ϸʧ�ܣ� ���¿�ʼ").setIcon(R.drawable.lost)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
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