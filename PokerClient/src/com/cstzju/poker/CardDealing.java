package com.cstzju.poker;

import java.util.ArrayList;
import java.util.Collections;

//包换对扑克牌的一系列操作
public class CardDealing{
	
	ArrayList<Card> cardP1 = new ArrayList<Card>(17);
	ArrayList<Card> cardP2 = new ArrayList<Card>(17);
	ArrayList<Card> cardP3 = new ArrayList<Card>(17);
	ArrayList<Card> cardTop3 = new ArrayList<Card>(3);  //最后地主的三张牌
	ArrayList<Card> cardAll = new ArrayList<Card>(54); 
	int boss = 0;
	
	//设计模式：模板方法模式
	public void init(){
		shuffle();
		divide();
	}
	
	//输入牌的数组，对牌进行洗牌
	public void shuffle() {
        for (int i = 0; i < 54; i++) {
            Card card = new Card(i + 1);
            cardAll.add(card);
        }
        //洗牌即交换1000次
        for (int i = 0; i <= 1000; i++) {
            Collections.shuffle(cardAll);// 打乱牌的位置
        }
    }
	
	//对玩家进行发牌
    public void divide() {
    	
    	//可能决定地主的方式：定义拥有黑桃三的为地主
    	
        //1号玩家的牌
        for (int j = 0; j < 17; j++) {
            Card card = cardAll.get(j * 3);
            cardP1.add(card);
            if(card.id==1){  //一号玩家是地主
            	boss = 1;
            }
        }

        // 2号玩家的牌
        for (int j = 0; j < 17; j++) {
            Card card = cardAll.get(j * 3 + 1);
            cardP2.add(card);
            if(card.id==1){  //一号玩家是地主
            	boss = 2;
            }
        }

        // 3号玩家的牌
        for (int j = 0; j < 17; j++) {
            Card card = cardAll.get(j * 3 + 2);
            cardP3.add(card);
            if(card.id==1){  //一号玩家是地主
            	boss = 3;
            }
        }

        for (int i = 51; i < 54; i++) {
            Card card = cardAll.get(i);
            cardTop3.add(card);
        }

        CardUtil.sortCards(cardP1);
        CardUtil.sortCards(cardP2);
        CardUtil.sortCards(cardP3);
    }

	public ArrayList<Card> getCardP1() {
		return cardP1;
	}
    
	public ArrayList<Card> getCardP2() {
		return cardP2;
	}
	
	public ArrayList<Card> getCardP3() {
		return cardP3;
	}
	
	public ArrayList<Card> getCardTop3() {
		return cardTop3;
	}
    
	public ArrayList<Card> getAllCards() {
		return cardAll;
	}

	public int getBoss() {
		return boss;
	}
	
}
