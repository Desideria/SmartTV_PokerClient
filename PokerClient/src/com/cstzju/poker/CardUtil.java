package com.cstzju.poker;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import com.cstzju.poker.Card.Flower;
import com.cstzju.poker.Card.Number;


//关于扑克牌的帮助类

public final class CardUtil {
	
	private CardUtil() {} //不能进行实例化
	
	static public int getPicID(int id){
		int picID = 0;
		switch(id){
		case 1:
			picID = R.drawable.spa_a;
			break;
		case 2:
			picID = R.drawable.spa_2;
			break;
		case 3:
			picID = R.drawable.spa_3;
			break;
		case 4:
			picID = R.drawable.spa_4;
			break;
		case 5:
			picID = R.drawable.spa_5;
			break;
		case 6:
			picID = R.drawable.spa_6;
			break;
		case 7:
			picID = R.drawable.spa_7;
			break;
		case 8:
			picID = R.drawable.spa_8;
			break;
		case 9:
			picID = R.drawable.spa_9;
			break;
		case 10:
			picID = R.drawable.spa_10;
			break;
		case 11:
			picID = R.drawable.spa_j;
			break;
		case 12:
			picID = R.drawable.spa_q;
			break;
		case 13:
			picID = R.drawable.spa_k;
			break;
		case 14:
			picID = R.drawable.hea_a;
			break;
		case 15:
			picID = R.drawable.hea_2;
			break;
		case 16:
			picID = R.drawable.hea_3;
			break;
		case 17:
			picID = R.drawable.hea_4;
			break;
		case 18:
			picID = R.drawable.hea_5;
			break;
		case 19:
			picID = R.drawable.hea_6;
			break;
		case 20:
			picID = R.drawable.hea_7;
			break;
		case 21:
			picID = R.drawable.hea_8;
			break;
		case 22:
			picID = R.drawable.hea_9;
			break;
		case 23:
			picID = R.drawable.hea_10;
			break;
		case 24:
			picID = R.drawable.hea_j;
			break;
		case 25:
			picID = R.drawable.hea_q;
			break;
		case 26:
			picID = R.drawable.hea_k;
			break;
		case 27:
			picID = R.drawable.clu_a;
			break;
		case 28:
			picID = R.drawable.clu_2;
			break;
		case 29:
			picID = R.drawable.clu_3;
			break;
		case 30:
			picID = R.drawable.clu_4;
			break;
		case 31:
			picID = R.drawable.clu_5;
			break;
		case 32:
			picID = R.drawable.clu_6;
			break;
		case 33:
			picID = R.drawable.clu_7;
			break;
		case 34:
			picID = R.drawable.clu_8;
			break;
		case 35:
			picID = R.drawable.clu_9;
			break;
		case 36:
			picID = R.drawable.clu_10;
			break;
		case 37:
			picID = R.drawable.clu_j;
			break;
		case 38:
			picID = R.drawable.clu_q;
			break;
		case 39:
			picID = R.drawable.clu_k;
			break;
		case 40:
			picID = R.drawable.dio_a;
			break;
		case 41:
			picID = R.drawable.dio_2;
			break;
		case 42:
			picID = R.drawable.dio_3;
			break;
		case 43:
			picID = R.drawable.dio_4;
			break;
		case 44:
			picID = R.drawable.dio_5;
			break;
		case 45:
			picID = R.drawable.dio_6;
			break;
		case 46:
			picID = R.drawable.dio_7;
			break;
		case 47:
			picID = R.drawable.dio_8;
			break;
		case 48:
			picID = R.drawable.dio_9;
			break;
		case 49:
			picID = R.drawable.dio_10;
			break;
		case 50:
			picID = R.drawable.dio_j;
			break;
		case 51:
			picID = R.drawable.dio_q;
			break;
		case 52:
			picID = R.drawable.dio_k;
			break;
		case 53:
			picID = R.drawable.joker_small;
			break;
		case 54:
			picID = R.drawable.joker;
		default:
			break;
		}
		return picID;
	}

	// 根据牌的id获得一张牌的花色：方块，梅花,红桃,黑桃,小王,大王
	static public Flower getFlowerType(int id) {
		Flower flowerType = null;
		if (id >= 1 && id <= 13) {
			flowerType = Flower.HEI_TAO;
		} else if (id >= 14 && id <= 26) {
			flowerType = Flower.HONG_TAO;
		} else if (id >= 27 && id <= 39) {
			flowerType = Flower.MEI_HUA;
		} else if (id >= 40 && id <= 52) {
			flowerType = Flower.FANG_KUAI;
		} else if (id == 53) {
			flowerType = Flower.XIAO_WANG;
		} else if (id == 54) {
			flowerType = Flower.DA_WANG;
		}
		return flowerType;
	}

	// 根据牌的id获得牌面数字：2-10，A,Q,J,K,XIAOWANG,DAWANG
	static public Number getNumberType(int id) {
		if (id < 1 || id > 54) {
			throw new RuntimeException("牌的数字不合法");
		}
		Number numberType = null;
		if (id >= 1 && id <= 52) {
			numberType = numToType(id % 13);
		} else if (id == 53) {
			numberType = Number.XIAO_WANG;
		} else if (id == 54) {
			numberType = Number.DA_WANG;
		} else {
			numberType = null;
		}
		return numberType;
	}

	//将阿拉伯数字0到12转换成对应的小牌型,被上面的getNumberType方法调用
	private static Number numToType(int num) {
		Number type = null;
		switch (num) {
		case 0:
			type = Number.K;
			break;
		case 1:
			type = Number.JIAN;
			break;
		case 2:
			type = Number.ER;
			break;
		case 3:
			type = Number.SAN;
			break;
		case 4:
			type = Number.SI;
			break;
		case 5:
			type = Number.WU;
			break;
		case 6:
			type = Number.LIU;
			break;
		case 7:
			type = Number.QI;
			break;
		case 8:
			type = Number.BA;
			break;
		case 9:
			type = Number.JIU;
			break;
		case 10:
			type = Number.SHI;
			break;
		case 11:
			type = Number.J;
			break;
		case 12:
			type = Number.Q;
			break;

		}
		return type;
	}

	//根据牌的id获得牌的等级，用于比较
	 public static int getGrade(int id) {
		 if (id < 1 || id > 54) {
			 throw new RuntimeException("牌的数字不合法");
			 }
	  int grade = 0;
	  
	  // 首先判断大小王
	  if (id == 53) {
		  grade = 16;   //这张牌为小王
	  } else if (id == 54) {
		  grade = 17;   //这张牌为大王
	  } else {
	   //以下为扑克牌是数字的情况
	   int result = id % 13;
	   if (result == 1) {   //牌面为A
		   grade = 14;
	   } else if (result == 2) {   //牌面为2
		   grade = 15;
	   } else if (result == 3) {   //牌面为3
		   grade = 3;
	   } else if (result == 4) {
		   grade = 4;
	   } else if (result == 5) {
		   grade = 5;
	   } else if (result == 6) {
		   grade = 6;
	   } else if (result == 7) {
		   grade = 7;
	   } else if (result == 8) {
		   grade = 8;
	   } else if (result == 9) {
		   grade = 9;
	   } else if (result == 10) {
		   grade = 10;
	   } else if (result == 11) {
		   grade = 11;
	   } else if (result == 12) {
		   grade = 12;
	   } else if (result == 0) {
		   grade = 13;
	   }
	  }
	  return grade;
	 }

	 //一组扑克牌根据规则进行排序
	 public static List<Card> sortCards(List<Card> cards){
		 Collections.sort(cards, new CardComparator()); 
		 return cards;
	 }
	 
	 static public int[] Objects2Id(List<Card> cards){
			int size = cards.size();
			int []ids = new int[size];
			for(int i=0;i<cards.size();i++){
				Card card = cards.get(i);
				ids[i] = card.id;
			}
			return ids;
		}
		
		static public List<Card> Id2Object(int ids[]){
			List<Card> cards = new ArrayList<Card>();
			for(int i=0;i<ids.length;i++){
				int id = ids[i];
				Card card = new Card(id);
				cards.add(card);
			}
			return cards;
		}
}
