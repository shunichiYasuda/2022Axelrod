package simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestMain_Feb24 {
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
		// �e�̃��X�g���ł����̂Ńy�A�����O���s��
		// ��������e�ԍ��������_���ɓ���ւ���B
		Collections.shuffle(parentsList);
		// check
		// �e�̈ꗗid�E ���F�́E�X�P�[�����O�㗘��
		for (int m : parentsList) {
			System.out.print(m + "\t:");
			printRec(pop.member[m].chrom);
			System.out.print("\t:" + pop.member[m].getScaledPayoff());
			System.out.println();
		}
		// �ˑR�ψفB�����̑O�ɐe�W�c�S�̂ɓˑR�ψُ������s���Ă����B
		mutation(parentsList);
		// �N���X�I�[�o�[
		crossover(parentsList);
		// check
		System.out.println("---------------------------------------------------------------");
		// �ˑR�ψقƃN���X�I�[�o�[��B�e�̈ꗗid�E ���F�́E�X�P�[�����O�㗘��
		for (int m : parentsList) {
			System.out.print(m + "\t:");
			printRec(pop.member[m].chrom);
			System.out.print("\t:" + pop.member[m].getScaledPayoff());
			System.out.println();
		}
		// �u��������ꂽ���F�̂ł��炽�Ȍ̂����.
		//pop.member �̐��F�̂��㏑������Ƃ��ɁA�e�̌̔ԍ��𗘗p����̂ŁA�����Ȃ��
		//�㏑����NG.�Ȃ̂ŁApop.member ���� parentsList �̒����̐��� chrom�z�������Ă����āA
		//������ɐe�ԍ������� chrom���R�s�[���Ă����A�ꊇ���Ēu��������B
		List<String> tmpParentsChrom = new ArrayList<String>();
		for( int m:parentsList) {
			tmpParentsChrom.add(new String(pop.member[m].chrom));
		}
		//check
		for(String str:tmpParentsChrom) {
			System.out.println(str);
		}
		//�u������
		for(int i=0;i<POPSIZE;i++) {
			char[] tmpChrom = new char[CHeader.LENGTH];
			tmpChrom = tmpParentsChrom.get(i).toCharArray();
			pop.member[i].replace(tmpChrom);
		}
		//check
		System.out.println("----------------new pop member ----------------------");
		for(int i=0;i<POPSIZE;i++) {
			printRec(pop.member[i].chrom);
			System.out.println();
		}
		

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
