package com.cstzju.poker;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Player {
	
	Context context;
	public ArrayList<Card> cards = new ArrayList<Card>(20);
	public ArrayList<Card> myCards = new ArrayList<Card>(7);
	private ArrayList<Card> preCards = new ArrayList<Card>(7);
	private Card card1 = new Card(1);
	
	Rect src = new Rect(0,0,200,280);
	Rect dst = new Rect();
	
	//单例模式
	private static Player player = null;
	
	private Player(Context context) {
		this.context = context;
		for(int i=1;i<=20;++i) {
			 card1 = new Card(i);
			 this.cards.add(card1);
		 }
		 for(int i=3;i<8;++i) {
			 card1 = new Card(i);
			 this.preCards.add(card1);
		 }
	}
	
	public static Player getInstance(Context context) {
		if(player==null) {
			player = new Player(context);
		}
		return player;
	}
	
	
	
	public void draw(Canvas canvas) {
		for(int i=0;i<cards.size();++i) {
			Bitmap cardBitmap = BitmapFactory.decodeResource(context.getResources(), cards.get(i).picID);
			int distance = 0;
			if(cards.get(i).selected) {
				distance = 30;
			}
			dst.set(i*33, 90-distance, i*33+120, 260-distance);
			canvas.drawBitmap(cardBitmap, src, dst, null);
		}
	}
	
	public void onTouch(View v, MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		for (int i=0;i<cards.size();++i) {
			// 判断是那张牌被选中，设置标志, 并添加到出牌列队
			if(x>i*33 && x<(i+1)*33 && y>90 && y<260) {
				Card card = cards.get(i);
				if(!card.selected) {
					card.selected = true;
					myCards.add(card);
				}else{
					card.selected = false;
					myCards.remove(card);
				}
				break;
			}
		}
	}
	
	public boolean chupai() {
		CardType mCardType = GameRule.getCardType(myCards);
		CardType preCardType = GameRule.getCardType(preCards);
		return GameRule.isOvercomePrev(myCards, mCardType, preCards, preCardType);
	}
	
}
