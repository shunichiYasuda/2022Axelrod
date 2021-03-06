package simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestMain_Feb25 {
	static CPopulation pop;
	static final int POPSIZE = 20;
	static final int GEN = 50; // 世代数
	static final int LOOP = 1; // 実験回数
	static String dateName;// ファイルの先頭に付加する日時
	static String timeStamp; // 実験記録につける日時秒。
	static File aveFile, memFile, statFile;
	static PrintWriter pwAve, pwGType, pwStat;
	// 親集団の染色体プール
	static List<String> parentsChrom;

	public static void main(String[] args) {
		// 記録ファイルの準備
		makeDate();
		makeFiles();
		// 実験回数のインデックス
		int exp = 0;
		while (exp < LOOP) {
			// 集団の生成
			pop = new CPopulation(POPSIZE);
//			//check
//			System.out.println("----------------before game ----------------------");
//			for(int i=0;i<POPSIZE;i++) {
//				printRec(pop.member[i].chrom);
//				System.out.println();
//			}
			// 世代数のインデックス
			int gen = 0;
			while (gen < GEN) {
				// 総当たり戦
				int p1 = 0;
				while (p1 < POPSIZE - 1) {
					for (int m = (p1 + 1); m < POPSIZE; m++) {
						int p2 = m;
						game(p1, p2);
					}
					p1++;
				}
				// 集団の平均利得等統計値を計算する。
				pop.calcStat();
				// 総当たり戦ゲームが終わったので
				// また、スケーリングを行う。スケーリングは行わない。Feb26
				// pop.scaling();
				// scaling payoff に基づいて parents リストを作る。
				// 親リスト。
				List<Integer> parentsList = new ArrayList<Integer>();
				// 親を作るメソッド。親の数が偶数になるように List を作成する
				makeParents(parentsList);
				// 親のリストができたのでペアリングを行う
				// いったん親番号をランダムに入れ替える。
				Collections.shuffle(parentsList);
				// 集団内でchromが上書きされないように親集団の染色体プールを作っておく。
				// めんどくさいのでListとして作成
				parentsChrom = new ArrayList<String>();
				for (int m : parentsList) {
					String tmp = new String(pop.member[m].chrom);
					parentsChrom.add(tmp);
				}

				// check 突然変異とクロスオーバー前の染色体
				System.out.println("-------before crossover-------");
				for (String s : parentsChrom) {
					System.out.println(s);
				}
				// 突然変異。交叉の前に親集団全体に突然変異処理を行っておく。
				// ここでGA過程の対象を親集団の染色体プールに限定しておく Feb26
				// したがってmutation,crossover のコードを前面書き換え
				mutation();
				// クロスオーバー
				crossover();
				// 突然変異とクロスオーバー後。
				System.out.println("------after crossover-------");
				for (String s : parentsChrom) {
					System.out.println(s);
				}
				// 置き換えられた染色体であらたな個体を作る.
				// pop.member の染色体を上書きする。親集団の染色体プールを作ってあるので、単純に
				//頭から menber.chrom を置き換える。
				for(int m=0;m<POPSIZE;m++) {
					char[] tmp = parentsChrom.get(m).toCharArray();
					pop.member[m].replace(tmp);
				}
				gen++;
			} // 世代ループの終わり。
			exp++;
		} // 実験ループの終わり

	}

	// 日付からファイル名をつくるので。
	static void makeDate() {
		// 記録用ファイルのための日付取得
		Calendar cal1 = Calendar.getInstance();
		int year = cal1.get(Calendar.YEAR); // 現在の年を取得
		int month = cal1.get(Calendar.MONTH); // 現在の月数-1を取得
		int day = cal1.get(Calendar.DATE);
		int hour = cal1.get(Calendar.HOUR_OF_DAY); // 現在の時を取得
		int minute = cal1.get(Calendar.MINUTE); // 現在の分を取得
		int second = cal1.get(Calendar.SECOND); // 現在の秒を取得
		String[] monthArray = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jly", "Aug", "Sep", "Oct", "Nov", "Dec" }; // 月表示を見やすくするため
		dateName = new String(monthArray[month] + day + "_" + year);
		timeStamp = new String(dateName + ":" + hour + ":" + minute + ":" + second);
	}

	// ファイル作成メソッド
	private static void makeFiles() {
		// 記録ファイルの準備
		memFile = new File(dateName + "_GType.txt");
		statFile = new File(dateName + "_stat.txt");
		aveFile = new File(dateName + "_ave.txt");
		try {
			FileWriter fw = new FileWriter(memFile);
			FileWriter fw2 = new FileWriter(statFile);
			FileWriter fw3 = new FileWriter(aveFile);
			BufferedWriter bw = new BufferedWriter(fw);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			BufferedWriter bw3 = new BufferedWriter(fw3);
			pwGType = new PrintWriter(bw);
			pwStat = new PrintWriter(bw2);
			pwAve = new PrintWriter(bw3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ファイルクローズ
	static void closeFiles() {
		pwGType.close();
		pwStat.close();
		pwAve.close();
	}

	// 一点交叉メソッド
	// 親集団の染色体プールに対して行うので、parentsList がいらない。
	//メソッドのコード書き換え Feb26
	private static void crossover() {
		// 親リストは偶数なので、前から二つずつペアリング
		for (int m = 0; m < parentsChrom.size() - 1; m += 2) {
			char[]  parent1 = parentsChrom.get(m).toCharArray();
			char[] parent2 = parentsChrom.get(m+1).toCharArray();
			Random randSeed = new Random();
			//
			if (bingo(CHeader.crossProb)) {
				int point = randSeed.nextInt(CHeader.LENGTH);
				// まったく入れ替わらない・全部入れ替わるが起きるといやなので
				while (point == 0 || point == CHeader.LENGTH - 1) {
					point = randSeed.nextInt(CHeader.LENGTH);
				}
				for (int index = 0; index < point; index++) {
					char tmp = parent1[index];
					parent1[index] = parent2[index];
					parent2[index] = tmp;
				}
			} // end of if(クロスオーバーがビンゴ
			//ビンゴしようがしまいが parent1はm、parent2は m+1 の場所へ書き戻す。
			parentsChrom.set(m, new String(parent1));
			parentsChrom.set(m+1, new String(parent2));
		} // クロスオーバー終わり
	}

	// 突然変異メソッド
	private static void mutation() {
		// parentsChrom に対して処理をする。
		for (String s : parentsChrom) {
			char[] tmp = s.toCharArray();
			for (int i = 0; i < tmp.length; i++) {
				if (bingo(CHeader.mutProb)) {
					if(tmp[i]=='1') {
						tmp[i] = '0';
					}else {
						tmp[i] = '1';
					}
				}
			} //end of if(突然変異がビンゴ
		}//list にあるすべての染色体について突然変異が終了。
	} //end of mutation()

	// 親を作るメソッド
	private static void makeParents(List<Integer> parentsList) {
		// ルーレットを作る。pop のメンバすべての scaled payoff を合算。
		double sum = 0.0;
		for (int i = 0; i < POPSIZE; i++) {
			sum += pop.member[i].getAvePayoff();
		}
		// ルーレットの幅
		// 積算の対象になる payoff をaverage payoff に変更 Feb 26
		double[] roulet = new double[POPSIZE];
		roulet[0] = pop.member[0].getAvePayoff() / sum;
		for (int m = 1; m < POPSIZE; m++) {
			roulet[m] = roulet[m - 1] + (pop.member[m].getAvePayoff() / sum);
		}
		/*
		 * for(int m=0;m<roulet.length;m++){ System.out.println("\t"+roulet[m]); }
		 */
		// ルーレットを回して集団と同じ 数だけ親を選択
		double border;
		int p_index;
		for (int i = 0; i < POPSIZE; i++) {
			p_index = 0; // 初期化の位置に注意
			border = Math.random();
			while (roulet[p_index] < border)
				p_index++;
			parentsList.add(p_index);
		}
		// System.out.println("");
		// 親の数が奇数であれば交配できないのでひとつ選び直す
		if (parentsList.size() % 2 == 1) {
			p_index = 0;
			border = Math.random();
			while (roulet[p_index] < border)
				p_index++;
			parentsList.add(p_index);
		}
	}

	//
	private static boolean bingo(double prob) {
		boolean r = false;
		// 乱数を出して、確率以下ならビンゴ
		if (Math.random() < prob)
			r = true;
		return r;
	}

	//
	static void game(int p1, int p2) { // 個体番号 p1,p2 でゲームを行う。
		// それぞれのプレイヤーの「手」
		// 生成の時に memory ができて、そのときに adr も choice も決まっている。
		// ゲームで記憶が更新されるたびに adr も choice も更新されている。
		char select_p1 = pop.member[p1].getChoice();
		char select_p2 = pop.member[p2].getChoice();

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
		pop.member[p1].reMem(select_p2);
		pop.member[p2].reMem(select_p1);
		// ゲームカウントを増やす（修正：Feb19 2022 ）
		// 下ゲームカウントはsetPayoff が呼ばれた際にその中でカウントが増える
		// pop.member[p1].gameCount++;
		// pop.member[p2].gameCount++;
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
