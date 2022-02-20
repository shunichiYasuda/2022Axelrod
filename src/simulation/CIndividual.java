package simulation;

public class CIndividual {
	// ���F�̂̒����Ȃǂ̊e��萔�� CHeader.java �Œ�`�B
	char[] chrom; // ���F�́B�����͑ΐ헚���̒����ɉ����Č��܂�
	char[] memRec; // �ΐ헚���B������CConst �Ō��߂�ꂽ�񐔂�2
	int adr; // �������������F�̏�̈ʒu
	char myChoice; //�������������F�̏�̈ʒu�ɂ���s����I������B
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
		// �L�����w���������F�̏�̃A�h���X 0���琔����̂ŁA�z��̃A�h���X�ɂȂ�i�r�b�g�𐔂���Ƃ��ɒ��Ӂj
		String tmp = new String(this.memRec);
		this.adr = Integer.parseInt(tmp, 2);
		//���̃A�h���X�ɂ���s���i0:����'C', 1�F���؂�'D'�j
		this.myChoice = this.chrom[this.adr];
		// �����֌W������
		payoff = scaledPayoff = cumPayoff = avePayoff = 0.0;
	}// end of constructor
		

	// setter
	public void setPayoff(double p) {
		//�Q�[���̗������͂����Ă���A�Ƃ������Ƃ̓Q�[����1��I������Ƃ������ƂȂ̂�
		//���̃��\�b�h�̒���gameCount �������߁A���ϗ������v�Z���Ă���
		this.gameCount++;
		this.payoff = p;
		this.cumPayoff += p;
		this.avePayoff = cumPayoff / gameCount;
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
	public double getPayoff() {
		return this.payoff;
	}
	public double getCumPayoff() {
		return this.cumPayoff;
	}
	public double getAvePayoff() {
		return this.avePayoff;
	}
	public double getScaledPayoff() {
		return this.scaledPayoff;
	}
	public char getChoice() {
		return this.myChoice;
	}
	//
	// �ΐ헚���̍X�V�B���̑ΐ킲�ƂɍX�V�����B�󂯓����̂͑ΐ푊���choice
	public void reMem(char in) {
		// �L���̈�̃r�b�g��
		int L = this.memRec.length;
		char[] tmp = new char[L];
		// �͂��߂ɍŏ���2�r�b�g���̂ĂāA�ȍ~���l�߂�
		for (int i = 0; i < tmp.length - 2; i++) {
			tmp[i] = this.memRec[2 + i];
		}
		// �I��肩��2�r�b�g�� [L-2] �͎������o���� choice �ł���B
		tmp[L - 2] = this.getChoice();
		// �Ō�̃r�b�g�� in �ł���B
		tmp[L - 1] = in;
		// tmp �� memRec ���X�V����B
		for (int i = 0; i < L; i++) {
			this.memRec[i] = tmp[i];
		}
		// �L�����X�V���ꂽ����F�̎��̂��X�V�����B
		// ���F�̂̐擪 L �r�b�g���ΐ헚���ɒu�������B
		for (int i = 0; i < L; i++) {
			this.chrom[i] = this.memRec[i];
		}
		//�L�����X�V���ꂽ��Aadr ���ς��A myChoice ���ς��B
		//�L���̍X�V�i�Q�[���̃v���C�j���g���K�[�ɂȂ�Ƃ������Ƃ��B
		String str= new String(this.memRec);
		this.adr = Integer.parseInt(str, 2);
		this.myChoice = this.chrom[this.adr];
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
