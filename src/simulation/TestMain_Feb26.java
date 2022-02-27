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

public class TestMain_Feb26 {
	static CPopulation pop;
	static final int POPSIZE = 20;
	static final int GEN = 500; // ���㐔
	static final int EXP = 100; // ��������������
	static String dateName;// �t�@�C���̐擪�ɕt���������
	static String timeStamp; // �����L�^�ɂ�������b�B
	static File aveFile, memFile, statFile;
	static PrintWriter pwAve, pwGType, pwStat;
	// �e�W�c�̐��F�̃v�[��
	static List<String> parentsChrom;
	// ���ϒl���L�^����2�����z��
	static double[][] aveTable;

	public static void main(String[] args) {
		// ��������̂���10�񕪋L�^���Ƃ�
		final int checkTerm = 10;
		char[] Q = new char[checkTerm];
		int checkCount = 6; // 10��̓�6�񋦗͂������ƒ�`
		final double coopValue = 2.76;
		final double defectValue = 1.54;
		// �W�c�����͂�B���������ǂ����󋵂������t���O�F�����Ȃ� N ,���؂� D, ���� C
		char stateFlag = 'N';
		// �W�c�̏�ԋL�^�B���͏�ԁA���؂��ԁA�ǂ���ł��Ȃ�
		char[] popState = new char[GEN];
		// �W�c�̏�ԋL�^�� EXP���ۑ�����e�[�u��
		char[][] popStateTable = new char[GEN][EXP];
		//
		// �L���p�^�[������݂�TFT �̐��̋L�^
		int[] memBasedTFT = new int[GEN];
		// �L���p�^�[������݂�TFT�̐��� EXP���ۑ�����e�[�u��
		int[][] memBasedTFTTable = new int[GEN][EXP];
		// ���F�̃p�^�[�����猩��TFT�̐��̋L�^
		int[] gtypeBasedTFT = new int[GEN];
		// ���F�̃p�^�[�����猩��TFT�̐���EXP���ۑ�����e�[�u��
		int[][] gtypeBasedTFTTable = new int[GEN][EXP];
		// ������
		for (int i = 0; i < GEN; i++) {
			memBasedTFT[i] = 0;
			gtypeBasedTFT[i] = 0;
		}
		for (int i = 0; i < GEN; i++) {
			for (int j = 0; j < EXP; j++) {
				memBasedTFTTable[i][j] = 0;
				gtypeBasedTFTTable[i][j] = 0;
			}
		}
		// �W�c�����͂֎����������ǂ����𔻒肷��t���O�B���̃t���O�������Ă��������
		// ���������Ɣ��肵�Ă��܂��܂ȏ󋵂��L�^����B
		boolean convergeFlag = false;
		// ���ׂĂ̎����Ɋւ��镽�ϒl���ڂ��L�^����z��̏�����
		aveTable = new double[GEN][EXP];
		for (int i = 0; i < GEN; i++) {
			for (int j = 0; j < EXP; j++)
				aveTable[i][j] = 0.0;
		}

		// �L�^�t�@�C���̏���
		makeDate();
		makeFiles();
		// �ꎞ�I�ȕ��ϒl�̋L�^
		double[] tmpAve = new double[GEN];
		// �����񐔂̃C���f�b�N�X
		int exp = 0;
		while (exp < EXP) { // ���͂ւ̎����������������̂݋L�^���Ƃ�B
			// �W�c�̐���
			pop = new CPopulation(POPSIZE);
			// �ꎞ�I���ϒl �������Ƃɏ���������
			for (int i = 0; i < tmpAve.length; i++) {
				tmpAve[i] = 0.0;
			}
			// Q[] ��������
			for (int i = 0; i < Q.length; i++) {
				Q[i] = 'N';
			}
			// �󋵃t���O��������
			stateFlag = 'N';
			// �����t���O��������
			convergeFlag = false;
			// �W�c��Ԃ͂��ׂĂ̐���� 'N'
			for (int i = 0; i < GEN; i++) {
				popState[i] = 'N';
			}
			// System.out.println("exp=" + exp);
			// ���㐔�̃C���f�b�N�X
			int gen = 0;
			while (gen < GEN) {// ���ニ�[�v�̎n�܂�
				// ���������
				int p1 = 0;
				while (p1 < POPSIZE - 1) {
					for (int m = (p1 + 1); m < POPSIZE; m++) {
						int p2 = m;
						game(p1, p2);
					}
					p1++;
				}
				// �W�c�̕��ϗ��������v�l���v�Z����B
				pop.calcStat();
				// ��������̂��߂Ɉꎞ�I�ȕ��ϒl�̕ۑ�
				tmpAve[gen] = pop.mAve;
				// System.out.println(pop.mAve);
				// ��������
				if (tmpAve[gen] >= coopValue) { // ���͂̒l��B������
					stateFlag = '0'; // �t���O�̃Z�b�g
				} else {
					if (tmpAve[gen] <= defectValue) {
						stateFlag = '1';
					} else {
						stateFlag = 'N';
					}
				}
				// Q �̋l�ߑւ�
				for (int i = 1; i < checkTerm; i++) {
					Q[i - 1] = Q[i];
				}
				Q[checkTerm - 1] = stateFlag;
				// Q�̃`�F�b�N���������𖞂����Ă��邩�ǂ������`�F�b�N����B
				int cntCoop = 0;
				int cntDefect = 0;
				int cntNone = 0;
				for (int i = 0; i < Q.length; i++) {
					if (Q[i] == '0') {
						cntCoop++;
					}
					if (Q[i] == '1') {
						cntDefect++;
					}
					if (Q[i] == 'N') {
						cntNone++;
					}
				} // C ,D,N�̐��𐔂���
					// ���̉񐔂�checkCount �𒴂��Ă���Ȃ�
				if (cntCoop >= checkCount) {
					// �����t���O���Z�b�g����B
					convergeFlag = true;
					// System.out.println("�����Fgen=" + gen + "\t�����񐔁Fexp=" + exp);
					// �W�c�̏�Ԃ�
					popState[gen] = '0';
				}
				// Defect ��checkCount �𒴂��Ă���
				if (cntDefect >= checkCount) {
					// �󋵂͗��؂�
					popState[gen] = '1';
				}
				if (cntNone >= checkCount) {
					popState[gen] = 'N';
				}
				// ���̃^�C�~���O�Ő��F�̂𒲍����ATFT�̂��J�E���g����K�v������
				memBasedTFT[gen] = countMemBasedTFT();
				gtypeBasedTFT[gen] = countGtypeBasedTFT();
				// ��������̏I���
				// �e���X�g�B
				List<Integer> parentsList = new ArrayList<Integer>();
				// �e����郁�\�b�h�B�e�̐��������ɂȂ�悤�� List ���쐬����
				makeParents(parentsList);
				// �e�̃��X�g���ł����̂Ńy�A�����O���s��
				// ��������e�ԍ��������_���ɓ���ւ���B
				Collections.shuffle(parentsList);
				// �W�c����chrom���㏑������Ȃ��悤�ɐe�W�c�̐��F�̃v�[��������Ă����B
				parentsChrom = new ArrayList<String>();
				for (int m : parentsList) {
					String tmp = new String(pop.member[m].chrom);
					parentsChrom.add(tmp);
				}
				// �ˑR�ψفB�����̑O�ɐe�W�c�S�̂ɓˑR�ψُ������s���Ă����B
				mutation();
				// �N���X�I�[�o�[
				crossover();
				// �u��������ꂽ���F�̂ł��炽�Ȍ̂����.
				for (int m = 0; m < POPSIZE; m++) {
					char[] tmp = parentsChrom.get(m).toCharArray();
					pop.member[m].replace(tmp);
				}
				gen++;
			} // ���ニ�[�v�̏I���B
				// ���̎��������������A���͂ւ̎����������������̂݋L�^���Ƃ�B
			if (convergeFlag) {
				// ���ϒl�𕽋ϒl�e�[�u���ɕۑ�,�W�c�̏�ԋL�^���e�[�u���ɕۑ�
				for (int i = 0; i < aveTable.length; i++) {
					aveTable[i][exp] = tmpAve[i];
					popStateTable[i][exp] = popState[i];
				}
				// �J�E���g��i�߂�
				exp++;
			}

		} // �������[�v�̏I���
		closeFiles();

	}

	// ��`�q�^����TFT�̂������ăJ�E���g����B
	private static int countGtypeBasedTFT() {
		// �Ƃ����������ׂĂ̌̂�chrom���`�F�b�N
		int[] coopAdr = { 0, 6, 24, 30, 32, 38, 56, 62 }; // �����̃r�b�g�����ׂ�0
		int[] defectAdr = { 1, 7, 25, 31, 33, 39, 57, 63 };// �����̃r�b�g�����ׂ�1
		boolean coopFlag = true;
		boolean defectFlag = true;
		int count = 0;
		// �Ƃ����������ׂĂ̌̂�chrom���`�F�b�N
		for (int m = 0; m < POPSIZE; m++) {
			char[] tmpChrom = pop.member[m].chrom;
			for (int point : coopAdr) {
				if (tmpChrom[point] == '1')
					coopFlag = false;
			}
			for (int point : defectAdr) {
				if (tmpChrom[point] == '0')
					defectFlag = false;
			}
			if (coopFlag && defectFlag) {
				count++;
				System.out.println("Find TFT in gtype");
			}
		}
		return count;
	}

	// �L���p�^�[������ TFT�̂������ăJ�E���g����
	private static int countMemBasedTFT() {
		int count = 0;
		// TFT�L���p�^�[���̐��K�\��
		String pattern1 = new String("[01]0000[01]");
		String pattern2 = new String("[01]0011[01]");
		String pattern3 = new String("[01]1100[01]");
		String pattern4 = new String("[01]1111[01]");
		// �`�F�b�N�p�t���O
		boolean p1, p2, p3, p4;
		p1 = p2 = p3 = p4 = false;
		boolean totalFlag = false;
		// �Ƃ�������1�̂��L���z����`�F�b�N�B
		for (int m = 0; m < POPSIZE; m++) {
			char[] memory = pop.member[m].memRec;
			String strMemory = new String(memory);
			// 4�̃p�^�[�����`�F�b�N����B
			if (strMemory.matches(pattern1))
				p1 = true;
			if (strMemory.matches(pattern2))
				p2 = true;
			if (strMemory.matches(pattern3))
				p3 = true;
			if (strMemory.matches(pattern4))
				p4 = true;
			// p1-p4 �̂ǂꂩ�� true �Ȃ� totalFlag��true;
			// ������2�����g���Ȃ��̂ŁA
			boolean p12 = p1 || p2;
			boolean p34 = p3 || p4;
			if (p12 || p34)
				totalFlag = true;
			// ���̋L���z�񂪂����ꂩ�̃p�^�[���Ƀ}�b�`���Ă���Ȃ���F�̂��`�F�b�N����B
			if (totalFlag) {
				int point = Integer.parseInt(strMemory, 2);
				char lastChar = memory[memory.length - 1];
				char genChar = pop.member[m].chrom[point];
				if (lastChar == genChar) {
					count++;
					// System.out.println("match:"+strMemory+" : "+genChar);
				}
			}
		} // �L���z��`�F�b�N�I���
		return count;
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
	// �e�W�c�̐��F�̃v�[���ɑ΂��čs���̂ŁAparentsList ������Ȃ��B
	// ���\�b�h�̃R�[�h�������� Feb26
	private static void crossover() {
		// �e���X�g�͋����Ȃ̂ŁA�O�������y�A�����O
		for (int m = 0; m < parentsChrom.size() - 1; m += 2) {
			char[] parent1 = parentsChrom.get(m).toCharArray();
			char[] parent2 = parentsChrom.get(m + 1).toCharArray();
			Random randSeed = new Random();
			//
			if (bingo(CHeader.crossProb)) {
				int point = randSeed.nextInt(CHeader.LENGTH);
				// �܂���������ւ��Ȃ��E�S������ւ�邪�N����Ƃ���Ȃ̂�
				while (point == 0 || point == CHeader.LENGTH - 1) {
					point = randSeed.nextInt(CHeader.LENGTH);
				}
				for (int index = 0; index < point; index++) {
					char tmp = parent1[index];
					parent1[index] = parent2[index];
					parent2[index] = tmp;
				}
			} // end of if(�N���X�I�[�o�[���r���S
				// �r���S���悤�����܂��� parent1��m�Aparent2�� m+1 �̏ꏊ�֏����߂��B
			parentsChrom.set(m, new String(parent1));
			parentsChrom.set(m + 1, new String(parent2));
		} // �N���X�I�[�o�[�I���
	}

	// �ˑR�ψك��\�b�h
	private static void mutation() {
		// parentsChrom �ɑ΂��ď���������B
		for (String s : parentsChrom) {
			char[] tmp = s.toCharArray();
			for (int i = 0; i < tmp.length; i++) {
				if (bingo(CHeader.mutProb)) {
					if (tmp[i] == '1') {
						tmp[i] = '0';
					} else {
						tmp[i] = '1';
					}
				}
			} // end of if(�ˑR�ψق��r���S
		} // list �ɂ��邷�ׂĂ̐��F�̂ɂ��ēˑR�ψق��I���B
	} // end of mutation()

	// �e����郁�\�b�h
	private static void makeParents(List<Integer> parentsList) {
		// ���[���b�g�����Bpop �̃����o���ׂĂ� scaled payoff �����Z�B
		double sum = 0.0;
		for (int i = 0; i < POPSIZE; i++) {
			sum += pop.member[i].getAvePayoff();
		}
		// ���[���b�g�̕�
		// �ώZ�̑ΏۂɂȂ� payoff ��average payoff �ɕύX Feb 26
		double[] roulet = new double[POPSIZE];
		roulet[0] = pop.member[0].getAvePayoff() / sum;
		for (int m = 1; m < POPSIZE; m++) {
			roulet[m] = roulet[m - 1] + (pop.member[m].getAvePayoff() / sum);
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
