package com.cstzju.poker;

import java.util.ArrayList;
import java.util.List;

/*
 定义斗地主游戏规则：一共有10种牌型
 火箭：即双王（大王和小王），最大的牌。
 炸弹：四张点数相同的牌，如7777
 单牌：任意一张单牌
 对牌：任意两张点数相同的牌
 三张：任意三张点数相同的牌，如888
 三带一：点数相同的三张牌+一张单牌或一对牌。如：333+6 或 444+99
 单顺：任意五张或五张以上点数相连的牌，如：45678或78910JQK。不包括2和双王
 双顺：三对或更多的连续对牌，如：334455、7788991010JJ。不包括 2点和双王
 三顺：二个或更多的连续三张牌，如：333444 、555666777888。不包括 2点和双王
 飞机带翅膀：三顺＋同数量的单牌或同数量的对牌。如：444555+79 或333444555+7799JJ
 四带二：四张牌＋两手牌。（注意：四带二不是炸弹）。如：5555＋3＋8 或 4444＋55＋77
 */

public final class GameRule {

	// 牌的类型共有10种：1. 单 2.对子 3.3不带 4.3带1 5.炸弹 6.顺子 7.4带2 8.连对 9.飞机 10.对王

	private GameRule() {
	}

    //检测牌的类型，如果遵守规则，返回牌的类型，否则返回null
    public static CardType getCardType(List<Card> myCards) {
        CardType cardType = null;
        if (myCards != null) {
            if (isDan(myCards)) {
                cardType = CardType.DAN;
            } else if (isDuiWang(myCards)) {
                cardType = CardType.WANG_ZHA;
            } else if (isDuiZi(myCards)) {
                cardType = CardType.DUI_ZI;
            } else if (isZhaDan(myCards)) {
                cardType = CardType.ZHA_DAN;
            } else if (isSanDaiYi(myCards) != -1) {
                cardType = CardType.SAN_DAI_YI;
            } else if (isSanBuDai(myCards)) {
                cardType = CardType.SAN_BU_DAI;
            } else if (isShunZi(myCards)) {
                cardType = CardType.SHUN_ZI;
            } else if (isLianDui(myCards)) {
                cardType = CardType.LIAN_DUI;
            } else if (isSiDaiEr(myCards)) {
                cardType = CardType.SI_DAI_ER;
            } else if (isFeiJi(myCards)) {
                cardType = CardType.FEI_JI;
            }
        }
        return cardType;
    }

    //判断我选择出的牌和上家的牌的大小，决定是否可以出牌
	public static boolean isOvercomePrev(List<Card> myCards,
			CardType myCardType, List<Card> prevCards, CardType prevCardType) {
		
		// 我的牌和上家的牌都不能为null
		if (myCards == null || prevCards == null) {
			return false;
		}
		if (myCardType == null || prevCardType == null) {
			return false;  //上家出的牌不合法，所以不能出
		}

		int prevSize = prevCards.size();   //上一首牌的个数
		int mySize = myCards.size();
		if (prevSize == 0 && mySize != 0) {  //我先出牌，上家没有牌
			return true;
		}

		// 集中判断是否王炸，免得多次判断王炸
		if (prevCardType == CardType.WANG_ZHA) {
			return false;  //上家王炸，肯定不能出。
		} else if (myCardType == CardType.WANG_ZHA) {
			return true;   //我王炸，肯定能出。
		}

		// 集中判断对方不是炸弹，我出炸弹的情况
		if (prevCardType != CardType.ZHA_DAN && myCardType == CardType.ZHA_DAN) {
			return true;
		}

		// 默认情况：上家和自己想出的牌都符合规则
		CardUtil.sortCards(myCards);      // 对牌排序
		CardUtil.sortCards(prevCards);    // 对牌排序

		int myGrade = myCards.get(0).grade;
		int prevGrade = prevCards.get(0).grade;

		// 比较2家的牌，主要有2种情况：
		// 1.我出和上家一种类型的牌，即对子管对子；
		// 2.我出炸弹，此时，和上家的牌的类型可能不同
		// 王炸的情况已经排除

		// 单
		if (prevCardType == CardType.DAN && myCardType == CardType.DAN) {
			return compareGrade(myGrade, prevGrade);   //一张牌可以大过上家的牌
		}
		
		// 对子
		else if (prevCardType == CardType.DUI_ZI
				&& myCardType == CardType.DUI_ZI) {
			return compareGrade(myGrade, prevGrade); // 2张牌可以大过上家的牌

		}
		
		// 3不带
		else if (prevCardType == CardType.SAN_BU_DAI
				&& myCardType == CardType.SAN_BU_DAI) {
			return compareGrade(myGrade, prevGrade); // 3张牌可以大过上家的牌
		}
		
		// 炸弹
		else if (prevCardType == CardType.ZHA_DAN
				&& myCardType == CardType.ZHA_DAN) {
			return compareGrade(myGrade, prevGrade);  // 4张牌可以大过上家的牌

		}
		
		// 3带1
		else if (prevCardType == CardType.SAN_DAI_YI
				&& myCardType == CardType.SAN_DAI_YI) {
			// 3带1只需比较第2张牌的大小
			myGrade = myCards.get(1).grade;
			prevGrade = prevCards.get(1).grade;
			return compareGrade(myGrade, prevGrade);
		}
		
		// 4带2
		else if (prevCardType == CardType.SI_DAI_ER
				&& myCardType == CardType.SI_DAI_ER) {
			// 4带2只需比较第3张牌的大小
			myGrade = myCards.get(2).grade;
			prevGrade = prevCards.get(2).grade;
		}
		
		// 顺子
		else if (prevCardType == CardType.SHUN_ZI
				&& myCardType == CardType.SHUN_ZI) {
			if (mySize != prevSize) {
				return false;
			} else {
				// 顺子只需比较最大的1张牌的大小
				myGrade = myCards.get(mySize - 1).grade;
				prevGrade = prevCards.get(prevSize - 1).grade;
				return compareGrade(myGrade, prevGrade);
			}
		}
		
		// 连对
		else if (prevCardType == CardType.LIAN_DUI
				&& myCardType == CardType.LIAN_DUI) {
			if (mySize != prevSize) {
				return false;
			} else {
				// 顺子只需比较最大的1张牌的大小
				myGrade = myCards.get(mySize - 1).grade;
				prevGrade = prevCards.get(prevSize - 1).grade;
				return compareGrade(myGrade, prevGrade);
			}
		}
		
		// 飞机
		else if (prevCardType == CardType.FEI_JI
				&& myCardType == CardType.FEI_JI) {
			if (mySize != prevSize) {
				return false;
			} else {
				// 顺子只需比较第5张牌的大小(特殊情况333444555666没有考虑，即12张的飞机，可以有2种出法)
				myGrade = myCards.get(4).grade;
				prevGrade = prevCards.get(4).grade;
				return compareGrade(myGrade, prevGrade);
			}
		}

		// 默认不能出牌
		return false;
	}
	
	//比较grade大小	
	private static boolean compareGrade(int grade1, int grade2) {
		return grade1 > grade2;
	}
	
	// 十种判断牌类型的方法

	// 判断当前牌是否为单牌，是返回true，否返回false
	public static boolean isDan(List<Card> myCards) {
		boolean flag = false; // 默认不是单牌
		if (myCards != null && myCards.size() == 1) {
			flag = true;
		}
		return flag;
	}

	// 判断是否为对子，是返回true，否返回false
	public static boolean isDuiZi(List<Card> myCards) {
		boolean flag = false; // 默认不是对子
		if (myCards != null && myCards.size() == 2) {
			int grade1 = myCards.get(0).grade;
			int grade2 = myCards.get(1).grade;
			if (grade1 == grade2) {
				flag = true;
			}
		}
		return flag;
	}

	// 判断是否为3带1，是返回带的牌的位置，否返回-1
	public static int isSanDaiYi(List<Card> myCards) {
		int flag = -1; // 默认不是3带1
		if (myCards != null && myCards.size() == 4) {
			CardUtil.sortCards(myCards); // 对牌进行排序
			int[] grades = new int[4];
			grades[0] = myCards.get(0).grade;
			grades[1] = myCards.get(1).grade;
			grades[2] = myCards.get(2).grade;
			grades[3] = myCards.get(3).grade;
			// 炸弹不为3带1
			if ((grades[1] == grades[0]) && (grades[2] == grades[0])
					&& (grades[3] == grades[0])) {
				return -1;
			}
			// 3带1，被带的牌在牌头
			else if ((grades[1] == grades[0] && grades[2] == grades[0])) {
				return 0;
			}
			// 3带1，被带的牌在牌尾
			else if (grades[1] == grades[3] && grades[2] == grades[3]) {
				return 3;
			}
		}
		return flag;
	}

	// 判断是否为3不带，是返回true，否返回false
	public static boolean isSanBuDai(List<Card> myCards) {

		boolean flag = false; // 默认不是3不带
		if (myCards != null && myCards.size() == 3) {
			int grade0 = myCards.get(0).grade;
			int grade1 = myCards.get(1).grade;
			int grade2 = myCards.get(2).grade;
			if (grade0 == grade1 && grade2 == grade0) {
				flag = true;
			}
		}
		return flag;
	}

	// 判断当前牌是否为顺子，是返回true，否返回false
	public static boolean isShunZi(List<Card> myCards) {

		boolean flag = true; // 默认是顺子
		if (myCards != null) {
			int size = myCards.size();
			// 顺子牌的个数在5到12之间
			if (size < 5 || size > 12) {
				return false;
			}
			CardUtil.sortCards(myCards); // 对牌进行排序
			for (int n = 0; n < size - 1; n++) {
				int prev = myCards.get(n).grade;
				int next = myCards.get(n + 1).grade;
				// 小王、大王、2不能加入顺子
				if (prev == 17 || prev == 16 || prev == 15 || next == 17
						|| next == 16 || next == 15) {
					flag = false;
					break;
				} else {
					if (prev - next != -1) {
						flag = false;
						break;
					}
				}
			}
		}

		return flag;
	}

	// 判断牌是否为炸弹，是返回true，否返回false
	public static boolean isZhaDan(List<Card> myCards) {

		boolean flag = false; // 默认不是炸弹
		if (myCards != null && myCards.size() == 4) {
			int[] grades = new int[4];
			grades[0] = myCards.get(0).grade;
			grades[1] = myCards.get(1).grade;
			grades[2] = myCards.get(2).grade;
			grades[3] = myCards.get(3).grade;
			if ((grades[1] == grades[0]) && (grades[2] == grades[0])
					&& (grades[3] == grades[0])) {
				flag = true;
			}
		}
		return flag;
	}

	//判断牌是否为王炸，是返回true，否返回false
	public static boolean isDuiWang(List<Card> myCards) {
		
		boolean flag = false;  // 默认不是对王

		if (myCards != null && myCards.size() == 2) {

			int gradeOne = myCards.get(0).grade;
			int gradeTwo = myCards.get(1).grade;
			// 只有小王和大王的等级之后才可能是33
			if (gradeOne + gradeTwo == 33) {
				flag = true;
			}
		}
		return flag;
	}
	
	//判断牌是否为连对，是返回true，否返回false
	public static boolean isLianDui(List<Card> myCards) {
		
		boolean flag = true;  // 默认是连对
		if (myCards == null) {
			flag = false;
			return flag;
		}
		
		int size = myCards.size();
		if (size < 6 || size % 2 != 0) {
			flag = false;
		} else {
			// 对牌进行排序
			CardUtil.sortCards(myCards);
			for (int i = 0; i < size; i = i + 2) {
				if (myCards.get(i).grade != myCards.get(i + 1).grade) {
					flag = false;
					break;
				}

				if (i < size - 2) {
					if (myCards.get(i).grade - myCards.get(i + 2).grade != -1) {
						flag = false;
						break;
					}
				}
			}
		}

		return flag;
	}
	
	//判断牌是否为飞机，是返回true，否返回false
	public static boolean isFeiJi(List<Card> myCards) {
		boolean flag = false;
		// 默认不是单
		if (myCards != null) {

			int size = myCards.size();
			if (size >= 6) {
				// 对牌进行排序
				CardUtil.sortCards(myCards);

				if (size % 3 == 0 && size % 4 != 0) {
					flag = isFeiJiBuDai(myCards);
				} else if (size % 3 != 0 && size % 4 == 0) {
					flag = isFeiJiDai(myCards);
				} else if (size == 12) {
					flag = isFeiJiBuDai(myCards) || isFeiJiDai(myCards);
				}
			}
		}
		return flag;
	}

	
	//判断牌是否为飞机不带，是返回true，否返回false
	public static boolean isFeiJiBuDai(List<Card> myCards) {
		if (myCards == null) {
			return false;
		}

		int size = myCards.size();
		int n = size / 3;

		int[] grades = new int[n];

		if (size % 3 != 0) {
			return false;
		} else {
			for (int i = 0; i < n; i++) {
				if (!isSanBuDai(myCards.subList(i * 3, i * 3 + 3))) {
					return false;
				} else {
					// 如果连续的3张牌是一样的，记录其中一张牌的grade
					grades[i] = myCards.get(i * 3).grade;
				}
			}
		}

		for (int i = 0; i < n - 1; i++) {
			if (grades[i] == 15) {// 不允许出现2
				return false;
			}

			if (grades[i + 1] - grades[i] != 1) {
				System.out.println("等级连续,如 333444"
						+ (grades[i + 1] - grades[i]));
				return false;// grade必须连续,如 333444
			}
		}

		return true;
	}
	
	//判断牌是否为飞机带，是返回true，否返回false
	public static boolean isFeiJiDai(List<Card> myCards) {
		int size = myCards.size();
		int n = size / 4;// 此处为“除”，而非取模
		int i = 0;
		for (i = 0; i + 2 < size; i = i + 3) {
			int grade1 = myCards.get(i).grade;
			int grade2 = myCards.get(i + 1).grade;
			int grade3 = myCards.get(i + 2).grade;
			if (grade1 == grade2 && grade3 == grade1) {

				// return isFeiJiBuDai(myCards.subList(i, i + 3 *
				// n));8张牌时，下标越界,subList不能取到最后一个元素
				List<Card> cards = new ArrayList<Card>();
				for (int j = i; j < i + 3 * n; j++) {// 取字串
					cards.add(myCards.get(j));
				}
				return isFeiJiBuDai(cards);
			}

		}

		return false;
	}
	
	//判断牌是否为4带2，是返回true，否返回false
	public static boolean isSiDaiEr(List<Card> myCards) {
		boolean flag = false;
		if (myCards != null && myCards.size() == 6) {

			// 对牌进行排序
			CardUtil.sortCards(myCards);
			for (int i = 0; i < 3; i++) {
				int grade1 = myCards.get(i).grade;
				int grade2 = myCards.get(i + 1).grade;
				int grade3 = myCards.get(i + 2).grade;
				int grade4 = myCards.get(i + 3).grade;

				if (grade2 == grade1 && grade3 == grade1 && grade4 == grade1) {
					flag = true;
				}
			}
		}
		return flag;
	}
	
}
