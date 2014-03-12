package test_;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import test_.serial.WriteBean;



/*try{    

ir=new InputStreamReader(System.in); //从键盘接收了一个字符串的输入，并创建了一个字符输入流的对象
in=new BufferedReader(ir);
String s=in.readLine();//从输入流in中读入一行，并将读取的值赋值给字符串变量s
System.out.println("Input value is: "+s);
int i = Integer.parseInt(s);//转换成int型
i*=2;
System.out.println("Input value changed after doubled: "+i);
}catch(IOException e)
{System.out.println(e);}
*/

public class NumberInput{
	public static void main(String args[]){
		try{
			loop1:for(int i=0;i<5;i++){
			InputStreamReader ir;
			BufferedReader in;
			//ir=new InputStreamReader(System.in);
			ir=new InputStreamReader(System.in,"8859_1");
			in=new BufferedReader(ir);
			//new WriteBean((byte)ir);
			String s=in.readLine();//从输入流in中读入一行，并将读取的值赋值给字符串变量s
			System.out.println("Input value is: "+s);
			/*int i = Integer.parseInt(s);//转换成int型
			
			i*=2;
			int c=i;
			System.out.println("Input value changed after doubled: "+c);*/
			
			
				if(s.equals("guanji")){
			//
			
			byte[] order=new byte[] { 0x3A, 0x48, 0x7A, 0x5F,(byte) 0xFF, (byte) 0xFF, 0x73, 0x68, 0x75, 0x74, 0x77, 0x03 };
			
			new WriteBean(order);
			System.out.println("/////////////////");
			break;
			}
			else
			{
				System.out.println("对不起，您输入的不是内部可执行命令，请重新输入：");
				continue loop1;
			}
				}
		
		//System.out.println("对不起5次操作无效，請您閱讀操作說明，怎么成繁體了！！！");
			
		}catch(IOException e){System.out.println(e);}
	}
}