

public class SingletonTest {

	public static void main(String[] args) {
		Logger l1=Logger.getInstance();
		System.out.println("Instance 1 hashcode"+l1.hashCode());
		Logger l2=Logger.getInstance();
		System.out.println("Instance 2 hashcode"+l2.hashCode());
		System.out.print(l1==l2);

	}

}
