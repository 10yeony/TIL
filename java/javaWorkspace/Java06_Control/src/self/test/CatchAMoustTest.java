package self.test;

import java.util.Scanner;

class CatchAMouse{
	public static String solv(int x, int y, int z) {
		if(Math.abs(x-z) < Math.abs(y-z)) {
			return "CatA catch!";
		}
		else if(Math.abs(x-z) > Math.abs(y-z)) {
			return "CatB catch!";
		}
		else {
			return "Mouse Escapes!";
		}
		/*
		 * ����...
		 * Math.abs()
		 */
	}
}
public class CatchAMoustTest {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("CatA�� ��ǥ�� �Է��ϼ���>>>");
		int cata = sc.nextInt();
		System.out.println("CatB�� ��ǥ�� �Է��ϼ���>>>");
		int catb = sc.nextInt();
		System.out.println("Mouse�� ��ǥ�� �Է��ϼ���>>>");
		int mouse = sc.nextInt();
		// ������� ���� �Է¹ް�
		// �Է¹��� ���� CatchAMouse�� ���� ������ ��. �ٵ� static�̴ϱ� ��ü ���� �ʿ�X.
		String result = CatchAMouse.solv(cata, catb, mouse); 
		// Ŭ������ �޶� CatchAMouse.solv�� ���ְų� CatchAMouse�� �ִ� �޼ҵ带 �����ؼ� ���� Ŭ���� �ȿ� �־��ָ� ��.
		System.out.printf("Result :: %s", result);

	}

}