package com.cstzju.poker;

import java.util.Comparator;

import com.cstzju.poker.Card.Flower;

//比较牌面排列大小的比较器
public class CardComparator implements Comparator<Card> {
	 public int compare(Card card1, Card card2) {
	        int result = -1; 

	        int grade1 = card1.grade;
	        int grade2 = card2.grade; 

	        if (grade1 > grade2) {
	            result = 1;
	        } else if (grade1 < grade2) {
	            result = -1;
	        } else {
	            // 等级相同的情况，比如都是9,只是花色不同
	            Flower bigType1 = card1.flowerType;
	            Flower bigType2 = card2.flowerType;
	            // 从左到右，方块、梅花、红桃、黑桃
	            if (bigType1.equals(Flower.HEI_TAO)) {
	                result = 1;
	            } else if (bigType1.equals(Flower.HONG_TAO)) {
	                if (bigType2.equals(Flower.MEI_HUA)
	                        || bigType2.equals(Flower.FANG_KUAI)) {
	                    result = 1;
	                }
	            } else if (bigType1.equals(Flower.MEI_HUA)) {
	                if (bigType2.equals(Flower.FANG_KUAI)) {
	                    result = 1;
	                }
	            }
	            // 2张牌的等级不可能完全相同,程序内部采用这种设计
	            else {
	                result = -1;
	            }
	        } 

	        return result;
	    } 
}
