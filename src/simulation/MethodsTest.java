package simulation;

public class MethodsTest {

	public static void main(String[] args) {
		//pattern ���o���\�b�h
		//test �̂��߂�6�P�^�o�C�i���z��𐶐��B
		int L =6;
		char[] mem = new char[L];
		
		//
		String pattern1 = new String("[01]0000[01]");
		String pattern2 = new String("[01]0011[01]");
		String pattern3 = new String("[01]1100[01]");
		String pattern4 = new String("[01]1111[01]");
		//4�̃p�^�[���̂ǂ�ł�������Ώo�͂��邱�Ƃɂ���
		String str = new String("111001");
		for(int i=0;i<20;i++) {
			//initBinary(mem);
			// str = mem.toString();
			//
			if(str.matches(pattern1)) {
				System.out.println(str);
			}
			if(str.matches(pattern2)) {
				System.out.println(str);
			}
			if(str.matches(pattern3)) {
				System.out.println(str);
			}
			if(str.matches(pattern4)) {
				System.out.println(str);
			}
		}

	}
	// �����񏉊���
		public static void initBinary(char[] in) {
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
}
