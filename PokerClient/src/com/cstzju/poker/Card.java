package com.cstzju.poker;


public class Card {
	
	//枚举列出牌面所有的数字
	public enum Number {
		JIAN, ER, SAN, SI, WU, LIU, QI, BA, JIU, SHI, J, Q, K, XIAO_WANG, DA_WANG
	}
	
	//枚举列出所有扑克牌可能的花色
	public enum Flower {
		HEI_TAO, HONG_TAO, MEI_HUA, FANG_KUAI, XIAO_WANG, DA_WANG
	}
	 
	 public int id;                       //牌的数字ID用1到54表示
	 public Flower flowerType;            // 牌的花色，方块，梅花,红桃,黑桃,小王,大王
	 public Number numberType;            // 牌面数字，2-10,A,J,Q,K
     public int grade;                    // 牌的等级，用于对牌进行排序
	 public int picID;                    // 每张牌对应的id
	 public boolean selected = false;
	 
	 // 通过牌的id构造这张牌
	 public Card(int id) {
		 this.id = id;
		 picID = CardUtil.getPicID(id);
		 flowerType = CardUtil.getFlowerType(id);
	     numberType = CardUtil.getNumberType(id);
	     grade = CardUtil.getGrade(id);
	 }

	 

}
