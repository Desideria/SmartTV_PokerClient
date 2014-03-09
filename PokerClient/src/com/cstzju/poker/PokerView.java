package com.cstzju.poker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class PokerView extends SurfaceView implements SurfaceHolder.Callback,
OnTouchListener {
	
	Context context;
	Player player;

	Rect src = new Rect(0,0,200,280);
	Rect dst = new Rect();
	Bitmap bitmap; 
	
	boolean threadFlag=true;
	SurfaceHolder holder;
	Canvas canvas;
	private Button buttonChupai;
	
	public PokerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public PokerView(Context context, AttributeSet attrs) {
		super(context);
		this.context = context;
		this.player = Player.getInstance(context);
		this.getHolder().addCallback(this);
		this.setOnTouchListener(this);
		setZOrderOnTop(true);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
	}
	
	Thread gameThread = new Thread() {
		@Override
		public void run() {
			holder=getHolder();
			while(threadFlag)
			{
			try {
				canvas = holder.lockCanvas();
				draw(canvas);
			} finally {
				holder.unlockCanvasAndPost(canvas);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
		}
	};
	
	// 绘制玩家手中的牌
	@Override
	public void draw(Canvas canvas) {
//		super.draw(canvas);
		canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
		player.draw(canvas);
		if(player.chupai()) {
			Message message = new Message();
			message.what = 0;
			((MainActivity)context).handler.sendMessage(message);
		}else {
			Message message = new Message();
			message.what = 1;
			((MainActivity)context).handler.sendMessage(message);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()!=MotionEvent.ACTION_UP)
		{
			return true;
		}
		System.out.println(event.getX() + "  " + event.getY()+"-"+(event.getAction()==MotionEvent.ACTION_UP));
		player.onTouch(v, event);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		threadFlag=true;
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		threadFlag=false;
		boolean retry = true;
		while (retry) {// 循环
			try {
				gameThread.join();// 等待线程结束
				retry = false;// 停止循环
			} catch (InterruptedException e) {
			}// 不断地循环，直到刷帧线程结束
		}
	}
	
	

}
