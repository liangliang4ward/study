package test_;
import java.util.*;


public class ListTest {
	
	public static int curIdx;
	
	public static boolean  hasMoreElements(List lst, int curIdx){
		return curIdx < lst.size();
	}

	public static Object nextElement(List lst, int idx){
		return lst.get(idx);
	}

	public static ArrayList lst;
	public static LinkedList lnkLst;
	public static void main(String[] args) 
	{
//		 int [] c = new int [10];
//		 c[1] = 3;
//		 c[9] = 5;
//		// c --¡· 20£»
//		 int[] q= new int[20];
//		 for(int i=0;i<10;i++){
//			 q[i]=c[i];
//		 }
//		 c=q;
//		 
//		 c[18] = 17;
//		 System.out.print("ddddddddddddd"+c[18]);
		 
		lst = new ArrayList(2);
		lst.add(new Integer(3));
		System.out.println(lst.get(0));
		lst.add(new Integer(5));
		lst.add(new Integer(7));
		lst.add(new Integer(9));
		
		curIdx = 0;
		while(hasMoreElements(lst, curIdx)){
			System.out.println(nextElement(lst, curIdx++));
		}

/*		for(int j=0;j<lst.size();j++)
		{
			System.out.println("List index"+lst.get(j));
		}*/
		
		lnkLst = new LinkedList();
		lnkLst.add(new Integer(2));
		lnkLst.add(new Integer(4));
		lnkLst.add(new Integer(6));
		lnkLst.add(new Integer(8));
		
		
/*		for(int i=0;i<lnkLst.size();i++)
		{
			System.out.println("linkList index "+lnkLst.get(i));   
		}*/
		
		curIdx = 0;
		while(hasMoreElements(lnkLst, curIdx)){
			System.out.println(nextElement(lnkLst, curIdx++));
		}
	}

}
