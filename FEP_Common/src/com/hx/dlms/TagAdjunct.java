package com.hx.dlms;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * Any ASN.1 type can be TAGed such as [APPLICATION 1] IMPLICIT or [APPPLICATION 1] EXPLICIT.
 * See ASN.1 document for detail. 
 * The default TAG class is CLASS_UNIVERSAL for DLMS.
 * @author HBAO
 *
 */
public class TagAdjunct implements ASN1Constants,Serializable{
	private static final long serialVersionUID = -8350387811160869562L;

	private static final Logger log = Logger.getLogger(TagAdjunct.class);
	
	protected boolean axdrCodec = false;
	protected boolean implicit = true;
	protected int tagClass = CLASS_UNIVERSAL;
	protected int tagValue = -1;
	protected int adjunctLength = -1;

	public static final TagAdjunct applicationImplicit(int num){
		return new TagAdjunct(true,CLASS_APPLICATION ,num);
	}

	public static final TagAdjunct applicationExplicit(int num){
		return new TagAdjunct(false,CLASS_APPLICATION | PC_CONSTRUCTED ,num);
	}

	public static final TagAdjunct contextSpecificImplicit(int num){
		return new TagAdjunct(true,ASN1Constants.CLASS_CONTEXTSPECIFIC ,num);
	}

	public static final TagAdjunct contextSpecificExplicit(int num){
		return new TagAdjunct(false,ASN1Constants.CLASS_CONTEXTSPECIFIC | PC_CONSTRUCTED ,num);
	}

	public static final TagAdjunct primitiveImplicit(int num){
		return new TagAdjunct(true,ASN1Constants.CLASS_UNIVERSAL,num);
	}
	
	public static final TagAdjunct primitiveExplicit(int num){
		return new TagAdjunct(false,ASN1Constants.CLASS_UNIVERSAL | PC_CONSTRUCTED,num);
	}
	
	public TagAdjunct(boolean isImplicit, int adjunctClass,int adjunctValue){
		implicit = isImplicit;
		tagClass = adjunctClass;
		tagValue = adjunctValue;
	}
	
	public void assign(TagAdjunct adjunct){
		//axdrCodec = adjunct.axdrCodec;
		implicit = adjunct.implicit;
		tagClass = adjunct.tagClass;
		tagValue = adjunct.tagValue;
		adjunctLength = adjunct.adjunctLength;
	}
	
	public void assign(ASN1Type srcType){
		//axdrCodec = adjunct.axdrCodec;
		if( null != srcType.adjunct )
			assign(srcType.adjunct);
		else{
			tagClass = srcType.tagClass;
			tagValue = srcType.tagValue;
			adjunctLength = srcType.length;
		}
	}
	
	public boolean axdrCodec(){
		return axdrCodec;
	}
	
	public TagAdjunct axdrCodec(boolean axdr){
		axdrCodec = axdr;
		return this;
	}

	protected final void encodeTag(EncodeStream output)throws IOException{
		if( tagValue< 0 )
			throw new RuntimeException("tagValue<0");
		
		if( axdrCodec ){  // DLMS special handling
			output.write(tagValue);
			return;
		}
		
		if( tagValue < 31) // Tag short form
			output.write(tagValue+tagClass);
		else{
			//当Tag大于30时，则Tag在多个八位组中编码。在多个八位组中编码时，第一个八位组后五位全部为1，
			//其余的八位组最高位为1表示后续还有，为0表示Tag结束。
			output.write( 0x1F + tagClass);
			byte[] tv = null;
			if( tagValue>16383 ) //if tagValue> 2097151, occupy 4 bytes.
				tv = new byte[3];
			else if( tagValue>127 )
				tv = new byte[2];
			else
				tv = new byte[1];
			int xValue = tagValue;
			for(int i=tv.length-1;i>=0; i--){
				if( i== tv.length-1 )
					tv[i] = (byte)(xValue & 0x7F);
				else
					tv[i] = (byte)(( xValue & 0x7F) | 0x80);
				xValue = xValue >> 7;
			}
			output.write(tv);
		}
	}

	protected boolean decodeTag(DecodeStream input) throws IOException{
		if( input.available()==0 )
			return false;
		if( axdrCodec ){   // special treat. JUST for DLMS
			tagValue = input.read() & 0xFF;
			return true;
		}
		input.mark();
		int aByte = input.read();
		int tc = aByte & 0xC0;
		if( tagValue >0 && tc != (this.tagClass & 0xC0) ){
			String errMsg = "decodeTag error: tagClass="+tagClass+",input="+tc;
			throw new IOException(errMsg);
		}
		
		int tv = aByte & 0x1F;
		if( tv == 0x1f ){
			int peek = input.peek();
			//Special case used in DLMS application 31 tag is '5F', not '5F 1F'
			if( peek>0 && peek< 0x1F ){
				if( tagValue>0 && tv != tagValue ){
					String errMsg = "decodeTag error: tagValue="+tagValue+",input="+tv;
					throw new IOException(errMsg);
				}
				return true;
			}
			tv = 0;
			int hasMore = 0x80;
			while( hasMore !=0 ){
				if( input.available()==0 ){
					input.reset();
					return false;
				}
				int vByte = input.read();
				hasMore = vByte & 0x80;
				tv = (tv << 7 ) | (vByte & 0x7F) ;
			}
		}
		if( tagValue>0 && tv != tagValue ){
			String errMsg = "decodeTag error: tagValue="+tagValue+",input="+tv;
			throw new IOException(errMsg);
		}
		tagValue = tv;
		return true;
	}
	
	protected boolean decodeLength(DecodeStream input) throws IOException{
		if( isImplicit() )
			return true;
		//It has EXPLICIT tag.
		if( input.available() == 0 )
			return false;
		int posSaved = input.position();
		int aByte = input.read();
		
		if( aByte>=0 && aByte <= 127 ){
			adjunctLength = aByte;
			return true;
		}
		aByte = aByte & 0x7F;
		if( aByte> input.available() ){
			input.position(posSaved);
			if( log.isDebugEnabled() ){
				String msg = "decodeAdjunctLength need="+aByte+",buffer remaining="+input.available();
				log.debug(msg);
			}
			return false;
		}
		adjunctLength = 0;
		for(int i=0; i<aByte; i++){
			adjunctLength = adjunctLength<<8 | (input.read()&0xFF);
		}
		return true;
	}
	
	public int identifier(){
		return tagValue;
	}
	
	public boolean isImplicit(){
		return implicit;
	}
	
	public boolean isExplicit(){
		return ! implicit;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if( tagClass == ASN1Constants.CLASS_APPLICATION){
			sb.append("[APPLICATION ").append(tagValue).append("] ");
		}
		else if( tagClass == ASN1Constants.CLASS_CONTEXTSPECIFIC){
			sb.append("[").append(tagValue).append("] ");
		}
		else if( tagClass == ASN1Constants.CLASS_PRIVATE ){
			sb.append("[PRIVATE ").append(tagValue).append("] ");
		}
		else if( tagValue>=0 )
			sb.append("[").append(tagValue).append("] ");
		else
			sb.append("error, class=").append(tagClass).append(",tagValue=").append(tagValue).append(". ");
		if( implicit )
			sb.append("IMPLICIT ");
		else
			sb.append("EXPLICIT ");
		return sb.toString();
	}

	public final int getTagClass() {
		return tagClass;
	}

	public final void setTagClass(int tagClass) {
		this.tagClass = tagClass;
	}
}
