package simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestMain_Feb23 {
	static CPopulation pop;
	static final int POPSIZE = 20;

	public static void main(String[] args) {
		// �W�c�̐���
		pop = new CPopulation(POPSIZE);
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
		// �܂��A�X�P�[�����O���s��
		pop.scaling();
		// scaling payoff �Ɋ�Â��� parents ���X�g�����B
		// �e���X�g�B
		List<Integer> parentsList = new ArrayList<Integer>();
		// �e����郁�\�b�h�B�e�̐��������ɂȂ�悤�� List ���쐬����
		makeParents(parentsList);
		// check
//		for (int s : parentsList) {
//			System.out.println(s);
//		}
		// �e�̃��X�g���ł����̂Ńy�A�����O���s��
		// ��������e�ԍ��������_���ɓ���ւ���B
		Collections.shuffle(parentsList);
//		System.out.print("�e�ԍ��F");
//		for (int m = 0; m < parentsList.size(); m++) {
//			System.out.print("\t" + parentsList.get(m));
//		}
//		System.out.println("");
		//�ˑR�ψق�

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
