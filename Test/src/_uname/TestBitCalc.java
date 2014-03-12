package _uname;

public class TestBitCalc {

	
	
	public static void main(String[] args) {
		System.out.println(Integer.MAX_VALUE);
		System.out.println(getMaxInt());
		System.out.println(getMaxInt1());
		System.out.println(getMaxInt2());
		System.out.println(getMinInt());
		System.out.println(getMinInt1());
		System.out.println(getMaxLong());
		System.out.println(mulTwo(9));
		System.out.println(divTwo(9));
		System.out.println(isOddMun(3));
		swap(2, 3);
		System.out.println(abs(12312));
		System.out.println(max(6, 9));
		System.out.println(getBit(5, 11));
		System.out.println(setBitToOne(5,2));
		System.out.println(setBitToZero(7,2));
	}
	
	public static int getMaxInt(){
		return (1<<31)-1;
	}
	
	public static int getMaxInt1(){
		return ~(1<<31);
	}
	
	public static int getMaxInt2(){
		return (1<<-1)-1;
	}
	
	public static int getMinInt(){
		return 1<<31;
	}
	
	public static int getMinInt1(){
		return (1<<-1);
	}
	
	public static long getMaxLong(){
		return ((long)1<<127)-1;
	}
	
	public static int mulTwo(int n){
		return n<<1;
	}
	
	public static int divTwo(int n){
		return n>>1;
	}
	
	public static boolean isOddMun(int n){
		return (n&1)==1;
	}
	
	public static void swap(int a,int b){
		System.out.println("before swap a="+a+",b="+b);
		a^=b;
		b^=a;
		a^=b;
		System.out.println("after  swap a="+a+",b="+b);
	}
	
	public static int abs(int n){
		return (n^(n>>31))-(n>>31);
	}
	
	public static int max(int a,int b){
		return  b & ((a-b) >> 31) | a & (~(a-b) >> 31);
	}
	public static int getBit(int n, int m){  
	    return (n >> (m-1)) & 1;  
	}  
	
	public static int setBitToOne(int n,int m){
		return n | (1 << (m-1));  
	}
	
	public static int setBitToZero(int n, int m){  
	    return n & ~(1 << (m-1));  
	    /* 将1左移m-1位找到第m位，取反后变成111...0...1111 
	       n再和这个数做与运算*/  
	}  
}
