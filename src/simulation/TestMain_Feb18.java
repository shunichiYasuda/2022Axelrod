package simulation;

public class TestMain_Feb18 {
	static CPopulation pop;
	static final int POPSIZE = 20;

	public static void main(String[] args) {
		// 集団の生成
		pop = new CPopulation(POPSIZE);
		//総当たり戦
		int p1 = 0;
		while(p1<POPSIZE-1) {
			for(int m=(p1+1);m<POPSIZE;m++) {
				int p2 = m;
				//対戦するのは p1とp2である。
				//ゲームを複数回行う。
				//情報を確認
				//System.out.println("個体番号："+p1+","+p2);
				game(p1,p2);
			}
			p1++;
		}
		//総当たり戦が終わったので全体の累積利得を確認する
		
	}

	//
	static void game(int p1, int p2) { // 個体番号 p1,p2 でゲームを行う。
		// それぞれのプレイヤーの「手」
		// 生成の時に memory ができて、そのときに adr も choice も決まっている。
		// ゲームで記憶が更新されるたびに adr も choice も更新されている。
		char select_p1 = pop.member[p1].getChoice();
		char select_p2 = pop.member[p2].getChoice();
		//情報の確認
		/*
		 * System.out.println("("+ p1 +","+p2+")="+"("+select_p1+", "+select_p2+")");
		 * System.out.println("memory before game:"); System.out.print(p1+":");
		 * printRec(pop.member[p1].getMemory()); System.out.print("\t"+p2+":");
		 * printRec(pop.member[p2].getMemory());
		 */
		
		// C は 0, Dは 1 いずれchar である。
		if (select_p1 == '0' && select_p2 == '0') {
			pop.member[p1].setPayoff(3.0);
			pop.member[p2].setPayoff(3.0);
		}
		if (select_p1 == '0' && select_p2 == '1') {
			pop.member[p1].setPayoff(0.0);
			pop.member[p2].setPayoff(5.0);
		}
		if (select_p1 == '1' && select_p2 == '0') {
			pop.member[p1].setPayoff(5.0);
			pop.member[p2].setPayoff(0.0);
		}
		if (select_p1 == '1' && select_p2 == '1') {
			pop.member[p1].setPayoff(1.0);
			pop.member[p2].setPayoff(1.0);
		}
		//チェック
		//double payoff_p1 = pop.member[p1].getPayoff();
		//double payoff_p2 = pop.member[p2].getPayoff();
		//System.out.println("player"+p1+"="+payoff_p1+"\tplayer"+p2+"="+payoff_p2);
		pop.member[p1].reMem(select_p2);
		pop.member[p2].reMem(select_p1);
		//
		/*
		 * System.out.println("\nmemory after game:"); System.out.print(p1+":");
		 * printRec(pop.member[p1].getMemory()); System.out.print("\t"+p2+":");
		 * printRec(pop.member[p2].getMemory()); System.out.println("");
		 */
		// ゲームカウントを増やす
		pop.member[p1].gameCount++;
		pop.member[p2].gameCount++;
	}// end of game()
		// 支援メソッド

	private static void printRec(char[] in) {
		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i]);
		}
	}

	private static void printRec(double[] in) {
		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i] + "\t");
		}
	}

	private static void printRec(int[] in) {
		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i] + "\t");
		}
	}
}
