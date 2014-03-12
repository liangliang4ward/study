package test_.serial;

public class FunCollection {
	public void R_W_judge(byte[] messageString){
		new WriteBean(messageString);
		new judgeR_W(messageString);
	}

}
