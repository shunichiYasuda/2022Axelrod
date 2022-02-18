package simulation;

public class TestMain_Feb18 {
	static CPopulation pop;
	static final int POPSIZE = 20;

	public static void main(String[] args) {
		// �W�c�̐���
		pop = new CPopulation(POPSIZE);
		//���������
		int p1 = 0;
		while(p1<POPSIZE-1) {
			for(int m=p1;m<POPSIZE;m++) {
				int p2 = m;
				//�ΐ킷��̂� p1��p2�ł���B
				
			}
			p1++;
		}
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
		//
		// �Q�[���J�E���g�𑝂₷
		pop.member[p1].gameCount++;
		pop.member[p2].gameCount++;
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