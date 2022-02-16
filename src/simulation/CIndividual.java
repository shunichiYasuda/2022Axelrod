package simulation;

public class CIndividual {
	// ���F�̂̒����Ȃǂ̊e��萔�� CHeader.java �Œ�`�B
	char[] chrom; // ���F�́B�����͑ΐ헚���̒����ɉ����Č��܂�
	char[] memRec; // �ΐ헚���B������CConst �Ō��߂�ꂽ�񐔂�2
	int adr; // �������������F�̏�̈ʒu
	// �����֌W
	double payoff, scaledPayoff, cumPayoff, avePayoff;
	// �Q�[���J�E���g�B�̂ɂ���ăQ�[���񐔂��قȂ�̂�
	int gameCount = 0;

	// constructor
	public CIndividual() {
		chrom = new char[CHeader.LENGTH]; // ���F�́B�L�����܂�
		memRec = new char[2 * CHeader.PRE]; // �L���̒����u�����̎�E����̎�v���L�����Ă���Q�[����
		initBinary(chrom);
		// ���F�̂̍ŏ��� 2*PRE����ΐ헚���Ƃ��� memRec�ɃR�s�[
		for (int i = 0; i < memRec.length; i++) {
			this.memRec[i] = this.chrom[i];
		}
		// �L�����w���������F�̏�̃A�h���X
		String tmp = new String(this.memRec);
		this.adr = Integer.parseInt(tmp, 2);
		// �����֌W������
		payoff = scaledPayoff = cumPayoff = avePayoff = 0.0;
	}// end of constructor
		// �e�탁�\�b�h

	// setter
	public void setPayoff(double p) {
		this.payoff = p;
		this.cumPayoff += p;
	}

	// getter
	public int getAdr() {
		return this.adr;
	}

	public char[] getChrom() {
		return this.chrom;
	}

	public char[] getMemory() {
		return this.memRec;
	}

	// �����񏉊���
	public void initBinary(char[] in) {
		double d;
		for (int i = 0; i < in.length; i++) {
			d = Math.random();
			if (d > 0.5) {
				in[i] = '1';
			} else {
				in[i] = '0';
			}
		} // end of for
	} // end of void initBinary()
		//
}
