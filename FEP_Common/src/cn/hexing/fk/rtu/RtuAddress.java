package cn.hexing.fk.rtu;


public class RtuAddress {
	private String peerAddr;
	
	public RtuAddress(String peerAddr){
		this.peerAddr = peerAddr;
	}

	public String getIp() {
		int i = peerAddr.indexOf(':');
		if( i>0 )
			return peerAddr.substring(0, i);
		else
			return "";
	}

	public int getPort() {
		int i = peerAddr.indexOf(':');
		if( i>0 )
			return Integer.parseInt(peerAddr.substring(i+1));
		else
			return 0;
	}

	public String toString(){
		return peerAddr;
	}

	public String getPeerAddr() {
		return peerAddr;
	}

	public void setPeerAddr(String peerAddr) {
		this.peerAddr = peerAddr;
	}
}
