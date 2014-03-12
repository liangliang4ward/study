/**
 * This abstract class is the super class for all ASN.1 types
 */
package com.hx.dlms;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * @author hbao
 *
 */
public class ASN1Type implements ASN1Constants, Serializable{
	private static final long serialVersionUID = 603548087167882233L;

	private static final Logger log = Logger.getLogger(ASN1Type.class);
	protected int codec = DEFAULT_CODEC;  //Default codec is A-XDR
	protected boolean forceEncodeTag = false;
	public boolean isEncodeLength = true;
	
	public boolean isEncodeTag = true;
	protected int tagValue = 0;
	protected int tagClass = CLASS_UNIVERSAL; //bit8 bit7
	protected int tagConstructed = PC_PRIMITIVE; //bit6
	
	protected int length = -1;
	protected int fixedLength = -1;
	
	protected byte[] value = null;
	
	private boolean optional = false;
	protected Object defaultValue = null;
	
	protected enum DecodeState{ DECODE_ADJUNCT_TAG,DECODE_ADJUNCT_LENGTH,DECODE_TAG,DECODE_LENGTH,DECODE_VALUE,DECODE_DONE };
	protected DecodeState decodeState = DecodeState.DECODE_ADJUNCT_TAG;
	protected int decodeOffset = -1, decodeRemainLength = -1;
	
	protected TagAdjunct adjunct = null;
	/**
	 * @param tagNumber
	 */
	public ASN1Type(int tagNumber){
		this(CLASS_UNIVERSAL,tagNumber,PC_PRIMITIVE,-1,false);
	}

	/**
	 * @param tagNumber
	 * @param fixedLen
	 */
	public ASN1Type(int tagNumber, int fixedLen){
		this(CLASS_UNIVERSAL,tagNumber,PC_PRIMITIVE,fixedLen,false);
	}
	
	/**
	 * @param tagClass
	 * @param tagNumber
	 * @param constructFlag
	 * @param fixedLen
	 * @param isOptional
	 */
	public ASN1Type(int tagClass,int tagNumber, int constructFlag, int fixedLen, boolean isOptional){
		this.tagClass = tagClass;
		tagValue = tagNumber;
		tagConstructed = constructFlag;
		fixedLength = fixedLen;
		optional = isOptional;
	}
	
	public ASN1Type setTagAdjunct(TagAdjunct tagAdjunct){
		adjunct = tagAdjunct;
		adjunct.tagClass = adjunct.tagClass | tagConstructed;
		if( adjunct.isExplicit() )
			this.setBerCodec();
		return this;
	}
	
	public TagAdjunct getTagAdjunct(){
		return adjunct;
	}
	
	public int identifier(){
		if( null != adjunct )
			return adjunct.identifier();
		return tagValue;
	}
	
	public void assignValue(ASN1Type srcObj ){
		tagClass = srcObj.tagClass;
		tagValue = srcObj.tagValue;
		tagConstructed = srcObj.tagConstructed;
		
		this.codec = srcObj.codec;
		fixedLength = srcObj.fixedLength;
		length = srcObj.length;
		value = srcObj.value;
	}
	
	public final void assignTag(ASN1Type srcType){
		if( null != adjunct ){
			adjunct.tagClass = srcType.tagClass | srcType.tagConstructed;
			adjunct.tagValue = srcType.tagValue;
		}
		else{
			tagClass = srcType.tagClass;
			tagValue = srcType.tagValue;
			tagConstructed = srcType.tagConstructed;
		}
		decodeState = srcType.decodeState;
	}
	
	public int getConstructFlag(){
		return tagConstructed;
	}
	
	public ASN1Type forceEncodeTag( boolean force ){
		forceEncodeTag = force;
		return this;
	}
	
	public ASN1Type codec(int myCodec ){
		codec = myCodec;
		return this;
	}
	
	public int codec(){
		return codec;
	}
	
	public final ASN1Type setAxdrCodec(){
		codec = AXDR_CODEC;
		return this;
	}
	
	public final boolean isAxdrCodec(){
		return codec == AXDR_CODEC; 
	}
	
	public final ASN1Type setBerCodec(){
		codec = BER_CODEC;
		return this;
	}
	
	public final boolean isBerCodec(){
		return codec == BER_CODEC;
	}
	
	public final boolean isConstructed(){
		return this.tagConstructed == PC_CONSTRUCTED;
	}
	
	public final ASN1Type setOptional(boolean o){
		optional = o;
		return this;
	}
	
	public final boolean isOptional(){
		return optional;
	}
	
	public ASN1Type setDefaultValue(Object defVal){
		defaultValue = defVal;
		if( null != defVal )
			optional = true;
		return this;
	}
	
	public Object getDefaultValue(){
		return defaultValue;
	}
	
	public void setValue(byte[] v){
		value = v;
	}
	
	public byte[] getValue(){
		return value;
	}
	
	/**
	 * Type is fixed length, for example: Integer8,Integer16,Integer32,Unsigned8,Unsigned16
	 * @param len
	 */
	public ASN1Type fixedLength(int len){
		fixedLength = len;
		return this;
	}
	
	public byte[] encode() throws IOException{
		EncodeStream output = new EncodeStream();
		encode(output);
		return output.dump();
	}
	
	public void encodeAxdr(EncodeStream output) throws IOException{
		if( optional || null != defaultValue ){
			//A-XDR OPTIONAL attribute using one byte flag
			if( null != value && value.length>0 )
				output.write(1);
			else{
				output.write(0);
				return;
			}
		}
		
		if( null != adjunct ){
			if( forceEncodeTag ){
				adjunct.encodeTag(output);
			}
		}
		//Only encode if it is variable length
		if( fixedLength<0  && isEncodeLength)
			encodeLength(output);
		encodeContent(output);
	}
	
	public void onPrepareContent() throws IOException{
		//sub-class override it to populate contents.
	}
	
	public void encode(EncodeStream output) throws IOException{
		onPrepareContent();
		if( isAxdrCodec() ){
			encodeAxdr(output);
			return;
		}
		if( null == value && null != defaultValue )
			return;
		if( optional && null == value )
			return;

		if( null != adjunct ){
			adjunct.encodeTag(output);
//			doEncodeTag(output,adjunct.tagClass,adjunct.tagValue);
			if( adjunct.isExplicit() ){
				EncodeStream exout = new EncodeStream();
				//encodeMyTag(output,tagClass+tagConstructed,tagValue);
				encodeTag(exout);
				encodeLength(exout);
				encodeContent(exout);
				value = exout.dump();
				if( null== value )
					value = new byte[0];
				doEncodeLength(value.length,output);
				doEncodeContent(value,output);
			}
			else{
				encodeLength(output);
				encodeContent(output);
			}
		}
		else{
			encodeTag(output);
			encodeLength(output);
			encodeContent(output);
		}
	}

	protected final void doEncodeTag(EncodeStream output, int tagC, int tagV)throws IOException{
		if( tagV< 0 )
			throw new RuntimeException("tagValue<0");
		if( tagV < 31) // Tag short form
			output.write(tagV+tagC);
		else{
			//当Tag大于30时，则Tag在多个八位组中编码。在多个八位组中编码时，第一个八位组后五位全部为1，
			//其余的八位组最高位为1表示后续还有，为0表示Tag结束。
			output.write( 0x1F + tagC);
			byte[] tv = null;
			if( tagV>16383 ) //if tagValue> 2097151, occupy 4 bytes.
				tv = new byte[3];
			else if( tagV>127 )
				tv = new byte[2];
			else
				tv = new byte[1];
			int xValue = tagV;
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
	
	public void encodeTag(EncodeStream output) throws IOException{
		if(isEncodeTag){
			doEncodeTag(output,tagClass+tagConstructed,tagValue);			
		}
	}
	
	protected boolean decodeTag(DecodeStream input) throws IOException{
		if( null != adjunct && adjunct.isImplicit() ){
			decodeState = DecodeState.DECODE_LENGTH;
			return true;
		}
		if( input.available()==0 )
			return false;
		int posSaved = input.position();
		int aByte = input.read();
		int tc = aByte & 0xC0;
		if( tagValue >0 && tc != this.tagClass ){
			String errMsg = "decodeTag error: tagClass="+tagClass+",input="+tc;
			if( log.isDebugEnabled() )
				log.debug(errMsg);
			throw new IOException(errMsg);
		}
		this.tagConstructed = aByte & 0x20;
		
		int tv = aByte & 0x1F;
		if( tv == 0x1f ){
			tv = 0;
			int hasMore = 0x80;
			while( hasMore !=0 ){
				if( input.available()==0 ){
					input.position(posSaved);
					log.debug("decode tag error. tagValue need more bytes.");
					return false;
				}
				int vByte = input.read();
				hasMore = vByte & 0x80;
				tv = (tv << 7 ) | (vByte & 0x7F) ;
			}
		}
		if( tagValue == ASN1Constants.TAG_ANY )
			tagValue = tv;
		if( tagValue>0 && tv != tagValue ){
			String errMsg = "decodeTag error: tagValue="+tagValue+",input="+tv;
			if( log.isDebugEnabled() )
				log.debug(errMsg);
			throw new IOException(errMsg);
		}
		decodeState = DecodeState.DECODE_LENGTH;
		return true;
	}
	
	protected final void doEncodeLength(int len, EncodeStream output) throws IOException{
		if( len<0 )
			return;
		if( length <= 127 ){
			output.write(len);
			return;
		}

		//length>127,long form
        int eLen = len >> 8;
        byte numOctets = 1;
        for (; eLen > 0; eLen = eLen >> 8) {
            numOctets++;
        }
        byte[] _encoded = new byte[numOctets+1];
        //First byte indicate length attribute has how much bytes,bit8 is '1'
        _encoded[0] = (byte) (numOctets | 0x80);

        eLen = len;
        int index = numOctets ;
        for (int i = 0; i < numOctets; i++ ) {
            _encoded[index - i] = (byte) (eLen & 0xFF );
            eLen = eLen >> 8 ;
        }
        output.write(_encoded);
	}
	
	public void encodeLength(EncodeStream output) throws IOException{
		if( null == value ){
			output.write(0);
			return ;  //ASN1 NULL ?
		}
		//BER codec or A-XDR codec for 'variable length'
		if( fixedLength>=0 && value.length == fixedLength)
			length = fixedLength;
		else
			length = value.length;
		doEncodeLength(length,output);
	}
	
	public boolean decodeLength(DecodeStream input) throws IOException{
		if( input.available() == 0 )
			return false;
		int posSaved = input.position();
		int aByte = input.read();
		if( aByte>=0 && aByte <= 127 ){
			length = aByte;
			decodeState = DecodeState.DECODE_VALUE;
			return true;
		}
		aByte = aByte & 0x7F;
		if( aByte> input.available() ){
			input.position(posSaved);
			if( log.isDebugEnabled() ){
				String msg = "decodeLength need="+aByte+",buffer remaining="+input.available();
				log.debug(msg);
			}
			return false;
		}
		length = 0;
		for(int i=0; i<aByte; i++){
			length = length<<8 | (input.read()&0xFF);
		}
		decodeState = DecodeState.DECODE_VALUE;
		return true;
	}
	
	protected final void doEncodeContent(byte[] content,EncodeStream output) throws IOException{
		output.write(content);
	}
	
	public void encodeContent(EncodeStream output) throws IOException{
		if( tagClass == TAG_NULL )
			return;
		if( null == value || value.length==0 )
			return;
			//throw new IOException("encodeContent exception: null==value || value.length==0");
		if( fixedLength>0 && tagValue != TAG_BITSTRING && tagValue != TAG_SEQUENCEOF ){
			if( fixedLength> value.length ){
				output.write(value);
				byte[] tailPadding = new byte[fixedLength-value.length];
				Arrays.fill(tailPadding, (byte)0);
				output.write(tailPadding);
			}
			else{
				output.write(value, 0, fixedLength);
			}
		}
		else
			output.write(value);
	}
	
	public boolean decodeContent(DecodeStream input) throws IOException{
		if( decodeOffset<0 ){
			int vlen = -1;
			vlen = fixedLength>0 ? fixedLength : (length>0 ? length : 0);
			value = new byte[vlen];
			decodeOffset = 0;
			decodeRemainLength = vlen;
		}
		int bytesRead = input.read(value,decodeOffset,decodeRemainLength);
		if( bytesRead>0 ){
			decodeOffset += bytesRead;
			decodeRemainLength -= bytesRead;
		}
		if( decodeRemainLength == 0 ){
			decodeState = DecodeState.DECODE_DONE;
			if( isConstructed() )
				onDecodeConstructedComplete(input);
			onDecodeContentComplete(input);
		}
		return decodeRemainLength == 0;
	}
	
	protected void onDecodeContentComplete(DecodeStream input) throws IOException{
		
	}
	
	protected void onDecodeConstructedComplete(DecodeStream input) throws IOException{
		//Handle constructed string
/*		if( tagValue == ASN1Constants.TAG_OCTETSTRING ||
				tagValue == ASN1Constants.TAG_BITSTRING ||
				tagValue == ASN1Constants.TAG_UTF8STRING ||
				tagValue == ASN1Constants.TAG_T61STRING ||
				tagValue == ASN1Constants.TAG_IA5STRING ||
				tagValue == ASN1Constants.TAG_VISIBLESTRING ||
				tagValue == ASN1Constants.TAG_GENERALSTRING ||
				tagValue == ASN1Constants.TAG_PRINTABLESTRING ){
			//value contain multiple same kind type object.
			//General speaking, A-XDR codec do not support primitive type constructed.
		}
*/	}
	
	public static int detectTagValue(DecodeStream input) throws IOException{
		if( input.available()==0 )
			return -1;
		int posSaved = input.position();
		int aByte = input.read();
		//int myTagClass = aByte & 0xC0;
		
		int myTagValue = aByte & 0x1F;
		if( myTagValue == 0x1f ){
			int peek = input.peek();
			//Special case used in DLMS application 31 tag is '5F', not '5F 1F'
			if( peek>0 && peek< 0x1F ){
				input.position(posSaved);
				return myTagValue;
			}
			myTagValue = 0;
			int hasMore = 0x80;
			while( hasMore !=0 ){
				if( input.available()==0 ){
					input.position(posSaved);
					return -1;
				}
				aByte = input.read();
				hasMore = aByte & 0x80;
				myTagValue = ( myTagValue << 7 ) | (aByte & 0x7F) ;
			}
		}
		input.position(posSaved);
		return myTagValue;
	}
	
	/**
	 * test to see if this ASN1Type match the tagValue. 
	 * Choice type must override this function.
	 * @return
	 */
	protected boolean isTagMatch(int srcTagValue){
		if( null != adjunct ){
			return adjunct.identifier() == srcTagValue;
		}
		return tagValue == srcTagValue;
	}
	
	protected boolean decodeAxdr(DecodeStream input) throws IOException{
		//Algorithm
		if( decodeState == DecodeState.DECODE_ADJUNCT_TAG ){
			//First step: if optional, check to see if present or not.
			if( optional ){
				if( input.available() == 0 )
					return false;
				int presentFlag = input.read();
				if( 0 == presentFlag ){
					//Optional type not present.
					decodeState = DecodeState.DECODE_DONE;
					return true;
				}
			}
			/**
			 * Step2: try to decode adjunct tag such as [x] IMPLICIT
			 * EXPLICIT construct tag always BER codec.
			 */
			if( null != adjunct && forceEncodeTag ){
				if( ! adjunct.decodeTag(input) )
					return false;
			}
			
			if( forceEncodeTag )
				decodeState = DecodeState.DECODE_TAG;
			else
				decodeState = DecodeState.DECODE_LENGTH;
		}
		/**
		 * step 3: As to A-XDR codec only applicable to implicit construct tag.
		 * never decode adjunct length
		 */

		//Only choice can have tag to decode.
		if( decodeState == DecodeState.DECODE_TAG ){
			decodeTag(input);
		}
		
		/**
		 * step 4: Only decode variable length
		 */
		if( decodeState == DecodeState.DECODE_LENGTH ){
			if( fixedLength>=0 ){ //fixed length
				decodeState = DecodeState.DECODE_VALUE;
			}
			else
				decodeLength(input);
		}
		
		if( decodeState == DecodeState.DECODE_VALUE )
			decodeContent(input);
		return decodeState == DecodeState.DECODE_DONE;
	}
	
	protected boolean decodeBer(DecodeStream input) throws IOException{
		//Algorithm
		if( decodeState == DecodeState.DECODE_ADJUNCT_TAG ){
			/**
			 * First step: if optional, check to see if present or not.
			 */
			if( optional ){
				int nextTagValue = detectTagValue(input);
				if( nextTagValue<0 )
					return false;
				if( !isTagMatch(nextTagValue) ){
					//Optional type not present.
					decodeState = DecodeState.DECODE_DONE;
					return true;
				}
			}
			/**
			 * Step2: try to decode adjunct tag such as [x] IMPLICIT / EXPLICIT
			 */
			if( null == adjunct )
				decodeState = DecodeState.DECODE_TAG;
			else if( adjunct.decodeTag(input) )
				decodeState = DecodeState.DECODE_ADJUNCT_LENGTH;
		}
		if( decodeState == DecodeState.DECODE_ADJUNCT_LENGTH ){
			if( adjunct.decodeLength(input))
				decodeState = DecodeState.DECODE_TAG;
		}

		if( decodeState == DecodeState.DECODE_TAG )
			decodeTag(input);
		if( decodeState == DecodeState.DECODE_LENGTH )
			decodeLength(input);
		if( decodeState == DecodeState.DECODE_VALUE )
			decodeContent(input);
		return decodeState == DecodeState.DECODE_DONE;
	}
	
	public boolean decode(DecodeStream input) throws IOException{
		try {
			if( this.isAxdrCodec() )
				return decodeAxdr(input);
			else
				return decodeBer(input);
		} catch (Error e) {
			throw new RuntimeException("Catch an error ."+e);
		}
	}
	
	public boolean isDecodeDone(){
		return decodeState == DecodeState.DECODE_DONE;
	}
	
	public void reuseDecode(){
		decodeState = DecodeState.DECODE_ADJUNCT_TAG;
		decodeOffset = -1;
	}
}
