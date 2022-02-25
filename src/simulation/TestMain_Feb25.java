package simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class TestMain_Feb25 {
	static CPopulation pop;
	static final int POPSIZE = 20;
	static String dateName;// �t�@�C���̐擪�ɕt���������
	static String timeStamp; // �����L�^�ɂ�������b�B
	static File aveFile, memFile, statFile;
	static PrintWriter pwAve, pwGType, pwStat;

	public static void main(String[] args) {
		// �L�^�t�@�C���̏���
		makeDate();
		makeFiles();
		// �W�c�̐���
		pop = new CPopulation(POPSIZE);
	}

	// ���t����t�@�C����������̂ŁB
	static void makeDate() {
		// �L�^�p�t�@�C���̂��߂̓��t�擾
		Calendar cal1 = Calendar.getInstance();
		int year = cal1.get(Calendar.YEAR); // ���݂̔N���擾
		int month = cal1.get(Calendar.MONTH); // ���݂̌���-1���擾
		int day = cal1.get(Calendar.DATE);
		int hour = cal1.get(Calendar.HOUR_OF_DAY); // ���݂̎����擾
		int minute = cal1.get(Calendar.MINUTE); // ���݂̕����擾
		int second = cal1.get(Calendar.SECOND); // ���݂̕b���擾
		String[] monthArray = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jly", "Aug", "Sep", "Oct", "Nov", "Dec" }; // ���\�������₷�����邽��
		dateName = new String(monthArray[month] + day + "_" + year);
		timeStamp = new String(dateName + ":" + hour + ":" + minute + ":" + second);
	}

	// �t�@�C���쐬���\�b�h
	private static void makeFiles() {
		// �L�^�t�@�C���̏���
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

	// �t�@�C���N���[�Y
	static void closeFiles() {
		pwGType.close();
		pwStat.close();
		pwAve.close();
	}

	// ��_�������\�b�h
	private static void crossover(List<Integer> parentsList) {
		// �e���X�g�͋����Ȃ̂ŁA�O�������y�A�����O
		for (int m = 0; m < parentsList.size() - 1; m += 2) {
			int parent1, parent2;
			parent1 = parentsList.get(m);
			parent2 = parentsList.get(m + 1);
			Random randSeed = new Random();
			if (bingo(CHeader.crossProb)) {
				int point = randSeed.nextInt(CHeader.LENGTH);
				// �܂���������ւ��Ȃ��E�S������ւ�邪�N����Ƃ���Ȃ̂�
				while (point == 0 || point == CHeader.LENGTH - 1) {
					point = randSeed.nextInt(CHeader.LENGTH);
				}
				for (int index = 0; index < point; index++) {
					char tmp = pop.member[parent1].chrom[index];
					pop.member[parent1].chrom[index] = pop.member[parent2].chrom[index];
					pop.member[parent2].chrom[index] = tmp;
				}
			} // end of if(�N���X�I�[�o�[���r���S
		} // �N���X�I�[�o�[�I���
	}

	// �ˑR�ψك��\�b�h
	private static void mutation(List<Integer> parentsList) {
		for (int m = 0; m < parentsList.size(); m++) {
			int id = parentsList.get(m);
			for (int index = 0; index < CHeader.LENGTH; index++) {
				if (bingo(CHeader.mutProb)) {
					if (pop.member[id].chrom[index] == '1') {
						pop.member[id].chrom[index] = '0';
					} else {
						pop.member[id].chrom[index] = '1';
					}
				} // end of if(�ˑR�ψق��r���S
			}
		} // end of for(m=0 ...���̗̈�̂��ׂĂ̌̂ɂ��ēˑR�ψُI���
	}

	// �e����郁�\�b�h
	private static void makeParents(List<Integer> parentsList) {
		// ���[���b�g�����Bpop �̃����o���ׂĂ� scaled payoff �����Z�B
		double sum = 0.0;
		for (int i = 0; i < POPSIZE; i++) {
			sum += pop.member[i].getScaledPayoff();
		}
		// ���[���b�g�̕�
		double[] roulet = new double[POPSIZE];
		roulet[0] = pop.member[0].getScaledPayoff() / sum;
		for (int m = 1; m < POPSIZE; m++) {
			roulet[m] = roulet[m - 1] + (pop.member[m].getScaledPayoff() / sum);
		}
		/*
		 * for(int m=0;m<roulet.length;m++){ System.out.println("\t"+roulet[m]); }
		 */
		// ���[���b�g���񂵂ďW�c�Ɠ��� �������e��I��
		double border;
		int p_index;
		for (int i = 0; i < POPSIZE; i++) {
			p_index = 0; // �������̈ʒu�ɒ���
			border = Math.random();
			while (roulet[p_index] < border)
				p_index++;
			parentsList.add(p_index);
		}
		// System.out.println("");
		// �e�̐�����ł���Ό�z�ł��Ȃ��̂łЂƂI�ђ���
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
		// �������o���āA�m���ȉ��Ȃ�r���S
		if (Math.random() < prob)
			r = true;
		return r;
	}

	//
	static void game(int p1, int p2) { // �̔ԍ� p1,p2 �ŃQ�[�����s���B
		// ���ꂼ��̃v���C���[�́u��v
		// �����̎��� memory ���ł��āA���̂Ƃ��� adr �� choice �����܂��Ă���B
		// �Q�[���ŋL�����X�V����邽�т� adr �� choice ���X�V����Ă���B
		char select_p1 = pop.member[p1].getChoice();
		char select_p2 = pop.member[p2].getChoice();

		// C �� 0, D�� 1 ������char �ł���B
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
		// �Q�[���J�E���g�𑝂₷�i�C���FFeb19 2022 �j
		// ���Q�[���J�E���g��setPayoff ���Ă΂ꂽ�ۂɂ��̒��ŃJ�E���g��������
		// pop.member[p1].gameCount++;
		// pop.member[p2].gameCount++;
	}// end of game()
		// �x�����\�b�h

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
