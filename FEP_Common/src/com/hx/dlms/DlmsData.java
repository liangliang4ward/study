package com.hx.dlms;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;

import cn.hexing.fk.utils.HexDump;


public class DlmsData extends ASN1Type implements DlmsDataType,Serializable {
	private static final long serialVersionUID = 7736617568376540292L;

	private static final ASN1ObjectFactory dataFactory = new ASN1ObjectFactory(){
		public ASN1Type create() {
			return new DlmsData();
		}
	};
//	private static final Logger log = Logger.getLogger(DlmsData.class);
	
	//refData is applicable for COMPACT-ARRAY,Bit-string,array,structure,CompactArray.typeDesc
	protected ASN1Type refData = null;
	//private DlmsTypeDesc compactType = null;
	//compactArray decode object is saved by using defaultValue; 
	
	public DlmsData(){
		super(TAG_ANY);
		codec = AXDR_CODEC;
		forceEncodeTag = true;
	}
	
	@Override
	public void assignValue(ASN1Type srcObj ){
		super.assignValue(srcObj);
		if( srcObj instanceof DlmsData ){
			refData = ((DlmsData)srcObj).refData;
		}
	}
	
	public void setBool( boolean data){
		tagValue = BOOL;
		value = new byte[]{ data ? (byte)0x01 : 0 };
	}
	
	public boolean getBool(){
		if( tagValue != BOOL )
			throw new RuntimeException("getBool type error: tag="+tagValue);
		if( null == value || value.length==0 )
			throw new RuntimeException("ASN1Boolean is null, should decode first");
		if( value[0] == 0 )
			return false;
		else
			return true;
	}
	
	public void setBcd(int bcdVal){
		if( bcdVal<0 )
			throw new RuntimeException("Illegal bcd , negative value.");
		byte[] v = new byte[12];
		int offset = 0;
		do{
			v[offset++] = (byte)(bcdVal % 100);
			bcdVal = bcdVal / 100;
		}while(bcdVal>0);
		byte[] result = new byte[offset];
		for(int i=0; i<result.length; i++){
			result[i] = v[--offset];
		}
		setBcd(result);
	}
	
	public void setBcd(byte[] bcd){
		tagValue = BCD;
		if( null == bcd || bcd.length == 0)
			throw new RuntimeException("set BCD, value is null");
		value = new byte[bcd.length+1];
		value[0] = (byte)bcd.length;
		for(int i=0; i<bcd.length; i++){
			if( bcd[i]<0 || bcd[i]> 99 )
				throw new RuntimeException("Not BCD, value="+bcd[i]);
			int c = ((bcd[i] / 10)<<4) | (bcd[i] % 10) ;
			value[i+1] = (byte)c ;
		}
	}
	

	public void setBcdStringAsOctets( String bcdStr ){
		byte[] octs = new byte[ bcdStr.length()/2 ];
		for( int i=0; i<octs.length; i++ ){
			int c1 = bcdStr.charAt( i<<1 ) - '0';
			int c2 = bcdStr.charAt( (i<<1) + 1) - '0';
			octs[i] = (byte)( ( c1 << 4 ) | c2 );
		}
		this.setOctetString(octs);
	}
	
	public int getBcdAsInt(){
		if( tagValue != BCD || null==value || value.length<2 )
			throw new RuntimeException("getBcd type error: tag="+tagValue);
		int bcd = 0;
		for(int i=1; i<value.length; i++ )
			bcd = (bcd * 100) + ( (value[i]>>4) & 0x0F)*10 + (value[i]&0x0F);
		return bcd;
	}
	
	public byte[] getBcd(){
		if( tagValue != BCD || null==value || value.length<2 )
			throw new RuntimeException("getBcd type error: tag="+tagValue);
		byte[] bcd = new byte[value.length];
		for(int i=0; i<bcd.length; i++ )
			bcd[i] = value[i];
		return bcd;
	}
	
	public void setDlmsInteger(byte dlmsInteger){
		tagValue = INTEGER;
		value = new byte[] { dlmsInteger };
	}
	
	public byte getDlmsInteger(){
		if( tagValue != INTEGER )
			throw new RuntimeException("getDlmsInteger type error: tag="+tagValue);
		return value[0];
	}
	
	public void setUnsigned( int unsigned ){
		tagValue = UNSIGNED;
		value = new byte[] { (byte)( unsigned & 0xFF ) };
	}
	public void setUnsigned(byte[] unsigned){
		tagValue = UNSIGNED;
		value = unsigned;
	}
	
	
	public int getUnsigned(){
		if( tagValue != UNSIGNED && tagValue != ENUM )
			throw new RuntimeException("getUnsigned type error: tag="+tagValue);
		return value[0] & 0xFF;
	}
	
	public void setDlmsLong( int dlmsLong ){
		tagValue = LONG;
		byte byte1 = (byte)((dlmsLong >> 8) & 0xFF);
		byte byte2 = (byte)( dlmsLong & 0xFF );
		value = new byte[]{ byte1,byte2 };
	}
	
	public void setDlmsLong(byte[] dlmsLong){
		tagValue = LONG;
		value = dlmsLong;
	}
	
	
	public int getDlmsLong(){
		if( tagValue != LONG )
			throw new RuntimeException("getDlmsLong type error, tag="+tagValue);
		ByteBuffer buf = ByteBuffer.wrap(value);
		return buf.getShort();
	}
	
	public void setUnsignedLong( int ulong ){
		tagValue = UNSIGNED_LONG;
		byte byte1 = (byte)((ulong >> 8) & 0xFF);
		byte byte2 = (byte)( ulong & 0xFF );
		value = new byte[]{ byte1,byte2 };
	}
	public void setUnsignedLong(byte[] ulong){
		tagValue = UNSIGNED_LONG;
		value = ulong;
	}
	
	public int getUnsignedLong(){
		if( tagValue != UNSIGNED_LONG )
			throw new RuntimeException("getUnsignedLong type error, tag="+tagValue);
		ByteBuffer buf = ByteBuffer.wrap(value);
		return (0xFFFF & buf.getShort());
	}
	
	public void setDoubleLong(int dblong ){
		tagValue = DOUBLE_LONG;
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putInt(dblong);
		value = buf.array();
	}
	
	public void setDoubleLong(byte[] dblong){
		tagValue = DOUBLE_LONG;
		value = dblong;
	}
	
	public int getDoubleLong(){
		if( tagValue != DOUBLE_LONG && tagValue != DOUBLE_LONG_UNSIGNED)
			throw new RuntimeException("getDoubleLong type error, tag="+tagValue);
		ByteBuffer buf = ByteBuffer.wrap(value);
		return buf.getInt();
	}
	
	public void setDoubleLongUnsigned(long dblong ){
		tagValue = DOUBLE_LONG_UNSIGNED;
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putInt( (int)(dblong & 0xFFFFFFFF));
		value = buf.array();
	}

	public void setDoubleLongUnsigned(byte[] dblong ){
		tagValue = DOUBLE_LONG_UNSIGNED;
		value = dblong;
	}
	
	
	public long getDoubleLongUnsigned(){
		if( tagValue != DOUBLE_LONG && tagValue != DOUBLE_LONG_UNSIGNED )
			throw new RuntimeException("getDoubleLongUnsigned type error, tag="+tagValue);
		ByteBuffer buf = ByteBuffer.wrap(value);
		return (long)( Long.parseLong(Integer.toHexString( buf.getInt() ),16));
	}

	public void setLong64(long long64 ){
		tagValue = LONG64;
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putLong( long64 );
		value = buf.array();
	}
	public void setLong64(byte[] long64 ){
		tagValue = LONG64;
		value =long64;
	}
	
	public long getLong64(){
		if( tagValue != LONG64 && tagValue != UNSIGNED64 )
			throw new RuntimeException("getLong64 type error, tag="+tagValue);
		ByteBuffer buf = ByteBuffer.wrap(value);
		return buf.getLong();
	}

	public void setUnsigned64(long long64 ){
		tagValue = UNSIGNED64;
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putLong( long64 );
		value = buf.array();
	}
	public void setUnsigned64(byte[] long64){
		tagValue = UNSIGNED64;
		value = long64;
	}
	
	public long getUnsigned64(){
		if( tagValue != LONG64 && tagValue != UNSIGNED64 )
			throw new RuntimeException("getUnsigned64 type error, tag="+tagValue);
		ByteBuffer buf = ByteBuffer.wrap(value);
		return buf.getLong();
	}
	
	public void setEnum(int enumValue ){
		setUnsigned(enumValue);
		tagValue = ENUM;
	}
	
	public int getEnum(){
		if( tagValue != ENUM && tagValue != UNSIGNED )
			throw new RuntimeException("getEnum type error, tag="+tagValue);
		return 0xFF & value[0];
	}
	
	public void setFloatingPoint( float fval ){
		tagValue = FLOATING_POINT;
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putFloat(fval);
		value = buf.array();
	}
	
	public float getFloatingPoint(){
		if( tagValue != FLOATING_POINT )
			throw new RuntimeException("getFloatingPoint type error, tag="+tagValue);
		ByteBuffer buf = ByteBuffer.wrap(value);
		return buf.getFloat();
	}
	
	public void setFloat32( float fval ){
		tagValue = FLOAT32;
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putFloat(fval);
		value = buf.array();
	}
	
	public float getFloat32(){
		if( tagValue != FLOAT32 )
			throw new RuntimeException("getFloat32 type error, tag="+tagValue);
		ByteBuffer buf = ByteBuffer.wrap(value);
		return buf.getFloat();
	}
	
	public void setFloat64( double dblval ){
		tagValue = FLOAT64;
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putDouble(dblval);
		value = buf.array();
	}
	
	public double getFloat64(){
		if( tagValue != FLOAT64 )
			throw new RuntimeException("getFloat64 type error, tag="+tagValue);
		ByteBuffer buf = ByteBuffer.wrap(value);
		return buf.getDouble();
	}
	
	public void setOctetString( byte[] octetString ){
		tagValue = OCTET_STRING;
		ASN1OctetString data = new ASN1OctetString(octetString);
		data.setAxdrCodec();
		try {
			value = data.encode();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte[] getOctetString(){
		if( tagValue != OCTET_STRING )
			throw new RuntimeException("getOctetString type error, tag="+tagValue);
		if( null != refData && (refData instanceof ASN1OctetString) )
			return ((ASN1OctetString)refData).getValue();
		if( null != value && value.length>0 ){
			ASN1OctetString data = new ASN1OctetString();
			data.setAxdrCodec();
			try {
				data.decode(new DecodeStream(value));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return data.getValue();
		}
		return null;
	}
	
	public void setVisiableString(String vstring){
		ASN1String str = ASN1String.VisibleString();
		str.setAxdrCodec();
		str.setString(vstring);
		try {
			value = str.encode();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		tagValue = VISIABLE_STRING;
	}
	
	
	public String getVisiableString(){
		if( tagValue != VISIABLE_STRING && tagValue != OCTET_STRING )
			throw new RuntimeException("getVisiableString type error, tag="+tagValue);
		if( null != refData && (refData instanceof ASN1String) )
			return ((ASN1String)refData).getString();
		if( null != value && value.length>0 ){
			ASN1String str = ASN1String.VisibleString();
			str.setAxdrCodec();
			try {
				str.decode(new DecodeStream(value));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return str.getString();
		}
		return null;
	}
	
	public void setBitString(ASN1BitString bitString){
		bitString.setAxdrCodec();
		try {
			value = bitString.encode();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		tagValue = BIT_STRING;
	}
	
	public ASN1BitString getBitString(){
		if( tagValue != BIT_STRING )
			throw new RuntimeException("getBitString type error, tag="+tagValue);
		if( null != refData && (refData instanceof ASN1BitString) )
			return (ASN1BitString)refData;
		if( null != value && value.length>0 ){
			ASN1BitString bitString = new ASN1BitString();
			bitString.setAxdrCodec();
			try {
				bitString.decode(new DecodeStream(value));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return bitString;
		}
		return null;
	}
	
	public void setArray( DlmsData[] array) throws IOException{
		tagValue = ARRAY;
		if( null == array ){
			value = new byte[] { (byte)0 };
			return;
		}
		ASN1SequenceOf sequence = new ASN1SequenceOf(array);
		sequence.setAxdrCodec();
		value = sequence.encode();
	}
	
	public final DlmsData[] getArray(){
		if( tagValue != ARRAY )
			throw new RuntimeException("getArray type error, tag="+tagValue);
		if( null != refData && (refData instanceof ASN1SequenceOf) ){
			ASN1SequenceOf myArray = (ASN1SequenceOf)refData;
			DlmsData[] reData = new DlmsData[myArray.members.length];
			for(int i =0;i < reData.length;i++){
				reData[i] = (DlmsData) myArray.members[i];
			}
			return reData;
		}
		if( null != value && value.length>0 ){
			ASN1SequenceOf myArray = new ASN1SequenceOf(dataFactory);
			myArray.setAxdrCodec();
			try {
				myArray.decode(new DecodeStream(value));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return (DlmsData[])myArray.members;
		}
		return null;
	}
	
	public void setArray(ASN1SequenceOf array){
		if( null == array ){
			tagValue = ARRAY;
			value = new byte[]{ (byte)0 };	//Array SIZE=0;
			return;
		}
		if( null != array.members && array.members.length>0 ){
			//Validate the elements of array. Make sure the element type must be DlmsData.
			for(int i=0; i<array.members.length; i++ ){
				if( ! ( array.members[0] instanceof DlmsData) )
					throw new IllegalArgumentException("Element of array type is not DlmsData");
			}
		}
		try {
			array.setAxdrCodec();
			value = array.encode();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		tagValue = ARRAY;
	}
	
	public int getArraySize(){
		if( tagValue != ARRAY || !isDecodeDone() )
			return -1;
		ASN1SequenceOf data = (ASN1SequenceOf)refData;
		return null == data.members ? 0 :data.members.length;
	}
	
	public DlmsData getArrayAt(int index){
		if(index<0 || index> getArraySize() )
			return null;
		ASN1SequenceOf data = (ASN1SequenceOf)refData;
		return (DlmsData)data.members[index];
	}

	public void setCompactArray(DlmsTypeDesc type,Object array){
		if( null == type || null == array || !( array instanceof Object[] ) )
			throw new IllegalArgumentException("setCompactArray,IllegalArgument, parameters is null");
		if( type.tagValue != ARRAY ){
			DlmsTypeDesc t = new DlmsTypeDesc();
			t.setArrayType( ((Object[])array).length, type);
			type = t;
		}
		type.arraySize = ((Object[])array).length;
		EncodeStream output = new EncodeStream();
		try {
			type.encode(output);
			switch( type.arrayType.tagValue ){
			case BOOL:
				if( array instanceof boolean[]){
					boolean[] data = (boolean[])array;
					for(int i=0; i<data.length; i++)
						output.write(data[i] ? 0xFF : 0);
				}
				break;
			case BCD:
				if( array instanceof byte[] ){
					byte[] data = (byte[])array;
					for(int i=0; i<data.length; i++){
						int b = (data[i]/10)<<4 | (data[i] % 10);
						output.write(b);
					}
				}
				else if( array instanceof int[]){
					int[] data = (int[])array;
					for(int i=0; i<data.length; i++){
						int b = data[i] & 0xFF;
						output.write((b/10)<<4 | (b % 10));
					}
				}
				else{
					throw new RuntimeException("bcd compact-array, type="+array.getClass());
				}
				break;
			case INTEGER:
			case UNSIGNED:
			case ENUM:
				if( array instanceof int[]){
					int[] data = (int[])array;
					for(int i=0; i<data.length; i++){
						output.write(data[i] & 0xFF);
					}
				}
				else{
					throw new RuntimeException("int8 compact-array, type="+array.getClass());
				}
				break;
			case LONG:
			case UNSIGNED_LONG:
				if( array instanceof int[]){
					int[] data = (int[])array;
					for(int i=0; i<data.length; i++){
						output.write( (data[i]>>8) & 0xFF );
						output.write( data[i] & 0xFF );
					}
				}
				else{
					throw new RuntimeException("int16 compact-array, type="+array.getClass());
				}
				break;
			case DOUBLE_LONG:
			case DOUBLE_LONG_UNSIGNED:
				if( array instanceof int[]){
					int[] data = (int[])array;
					for(int i=0; i<data.length; i++){
						output.write( (data[i]>>24) & 0xFF);
						output.write( (data[i]>>16) & 0xFF);
						output.write( (data[i]>>8) & 0xFF);
						output.write( data[i] & 0xFF );
					}
				}
				else{
					throw new RuntimeException("int32 compact-array, type="+array.getClass());
				}
				break;
			case FLOATING_POINT:
			case FLOAT32:
				if( array instanceof float[]){
					float[] data = (float[])array;
					ByteBuffer buf = ByteBuffer.allocate(4);
					for(int i=0; i<data.length; i++){
						buf.putFloat(data[i]);
						output.write(buf.array());
						buf.rewind();
					}
				}
				else{
					throw new RuntimeException("float32 compact-array, type="+array.getClass());
				}
				break;
			case LONG64:
			case UNSIGNED64:
				if( array instanceof long[]){
					long[] data = (long[])array;
					ByteBuffer buf = ByteBuffer.allocate(8);
					for(int i=0; i<data.length; i++){
						buf.putLong(data[i]);
						output.write(buf.array());
						buf.rewind();
					}
				}
				else{
					throw new RuntimeException("long64 compact-array, type="+array.getClass());
				}
				break;
			case FLOAT64:
				if( array instanceof double[]){
					double[] data = (double[])array;
					ByteBuffer buf = ByteBuffer.allocate(8);
					for(int i=0; i<data.length; i++){
						buf.putDouble(data[i]);
						output.write(buf.array());
						buf.rewind();
					}
				}
				else{
					throw new RuntimeException("float64 compact-array, type="+array.getClass());
				}
				break;
			case DATE_TIME: 
			case DATE: 
			case TIME: 
			{
				if( array instanceof DlmsDateTime ){
					DlmsDateTime[] datetimes = (DlmsDateTime[])array;
					byte[] arData = null;
					for(int i=0; i< datetimes.length; i++ ){
						if( type.arrayType.tagValue == DATE_TIME )
							arData = datetimes[i].getDateTimeValue();
						else if( type.arrayType.tagValue == DATE )
							arData = datetimes[i].getDateValue();
						else
							arData = datetimes[i].getTimeValue();
						output.write( arData );
					}
				}
				else{
					throw new RuntimeException("DATE_TIME compact-array, type="+array.getClass());
				}
				break;
			}
			case NULL:
			case DONT_CARE:
			default:
				throw new RuntimeException("compactArray not supported type:"+type.tagValue);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		refData = type;
		value = output.dump();
		tagValue = COMPACT_ARRAY;
	}

	public DlmsTypeDesc getCompactArrayType(){
		if( tagValue != COMPACT_ARRAY )
			return null;
		return (DlmsTypeDesc)refData;
	}

	public Object getCompactArray(){
		if( tagValue != COMPACT_ARRAY )
			return null;
		return defaultValue;
	}

	public void setStructure(ASN1SequenceOf struct){
		if( null == struct ){
			tagValue = STRUCTURE;
			value = new byte[]{ (byte)0 };	//structure is empty
			return;
		}
		if( null != struct.members && struct.members.length>0 ){
			//Validate the elements of structure. Make sure the element type must be DlmsData.
			for(int i=0; i<struct.members.length; i++ ){
				if( ! ( struct.members[0] instanceof DlmsData) )
					throw new IllegalArgumentException("Element of array type is not DlmsData");
			}
		}
		try {
			struct.setAxdrCodec();
			value = struct.encode();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		refData = struct;
		tagValue = STRUCTURE;
	}
	
	public final ASN1SequenceOf getStructure(){
		if( tagValue != STRUCTURE || !(refData instanceof ASN1SequenceOf) )
			return null;
		return (ASN1SequenceOf)refData;
	}

	public int getStructureSize(){
		if( tagValue != STRUCTURE || !(refData instanceof ASN1SequenceOf) )
			return -1;
		ASN1SequenceOf data = (ASN1SequenceOf)refData;
		return null == data.members ? 0 :data.members.length;
	}
	
	public DlmsData getStructureItem(int index){
		if(index<0 || index> getStructureSize() )
			return null;
		ASN1SequenceOf data = (ASN1SequenceOf)refData;
		return (DlmsData)data.members[index];
	}

	protected DlmsTypeDesc extractType(){
		DlmsTypeDesc type = new DlmsTypeDesc();
		type.tagValue = tagValue;
		if( null != refData ){
			if( tagValue == COMPACT_ARRAY ){
				ASN1SequenceOf array = (ASN1SequenceOf)refData;
				DlmsData el = (DlmsData)(array.members[0]);
				type.arraySize = array.members.length;
				type.arrayType = el.extractType();
			}
		}
		return type;
	}

	public void setNull(){
		tagValue = NULL;
		value = new byte[0];
	}
	
	public boolean isNull(){
		return tagValue == NULL || tagValue == DONT_CARE; 
	}
	
	public void setDontcare(){
		tagValue = DONT_CARE;
		value = new byte[0];
	}
	
	public boolean isDontcare(){
		return tagValue == DONT_CARE;
	}
	
	public int getDataType(){
		return tagValue;
	}
	
	@Override
	public void encode(EncodeStream output) throws IOException{
		if( (tagValue == NULL || tagValue == DONT_CARE) && null == value )
			return;
		if( isOptional() ){
			//A-XDR OPTIONAL attribute using one byte flag
			if( null != value )
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
/*		//Only encode if it is variable length
		if( fixedLength<0 )
			encodeLength(output);
		encodeContent(output);
*/
		encodeTag(output);
		if( null != value ){
			output.write(value);
			return;
		}
	}
	
	@Override
	public boolean decode(DecodeStream input) throws IOException{
		boolean result = true;
		if( decodeState == DecodeState.DECODE_ADJUNCT_TAG ){
			//First step: if optional, check to see if present or not.
			if( isOptional() ){
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
			decodeState = DecodeState.DECODE_TAG;
		}
		boolean fixedType = tagValue>0 && fixedLength>0 ;
		if( decodeState == DecodeState.DECODE_TAG ){
			if( ! fixedType )
				result = decodeTag(input);
			refData = null;
			value = null;
		}
		if( ! result )
			return false;
		//decode contents now.
		int len = -1;
		switch( tagValue ){
		case NULL:
		case DONT_CARE:
			len = 0;
			break;
		case BCD:
			len = input.read();
			break;
		case BOOL:
		case INTEGER:
		case UNSIGNED:
		case ENUM:
			len = 1;
			break;
		case LONG:
		case UNSIGNED_LONG:
			len = 2;
			break;
		case DOUBLE_LONG:
		case DOUBLE_LONG_UNSIGNED:
		case FLOATING_POINT:
		case FLOAT32:
			len = 4;
			break;
		case LONG64:
		case UNSIGNED64:
		case FLOAT64:
			len = 8;
			break;
		case OCTET_STRING:
		case VISIABLE_STRING:
			if( null == refData ){
				refData = new ASN1OctetString();
				refData.setAxdrCodec();
			}
			ASN1OctetString octetString = (ASN1OctetString)refData;
			result = octetString.decode(input);
			break;
		case BIT_STRING:
			if( null == refData ){
				refData = new ASN1BitString();
				refData.setAxdrCodec();
			}
			ASN1BitString bitstr = (ASN1BitString)refData;
			result = bitstr.decode(input);
			break;
		case ARRAY:
		case STRUCTURE:
			if( null == refData ){
				refData = new ASN1SequenceOf(dataFactory);
				refData.setAxdrCodec();
			}
			ASN1SequenceOf array = (ASN1SequenceOf)refData;
			result = array.decode(input);
			break;
		case COMPACT_ARRAY:
			result = decodeCompactArray(input);
			break;
		case DATE_TIME:
			len = 12;
			break;
		case DATE:
			len = 5;
			break;
		case TIME:
			len = 4;
			break;
		default:
			break;
//			throw new RuntimeException("Not support yet.");
		}
		
		if( len<0 && fixedLength>0 )
			len = fixedLength;
		
		if( len>=0 ){
			if( input.available()>= len ){
				value = new byte[len];
				input.read(value);
				result = true;
			}
			else
				result = false;
		}
		if( result ){
			if( null == value && null != refData )
				value = refData.encode();
			this.decodeState = DecodeState.DECODE_DONE;
		}
		return result;
	}
	
	private boolean decodeCompactArray(DecodeStream input) throws IOException{
		if( input.available()<1 )
			return false;
		if( null == refData ){
			refData = new DlmsTypeDesc();
			refData.decode(input);
		}
		DlmsTypeDesc compactType = (DlmsTypeDesc)refData;
		switch(compactType.tagValue){
		case BOOL:{
			if( input.available()< compactType.arraySize )
				return false;
			boolean[] data = new boolean[compactType.arraySize];
			defaultValue = data;
			for(int i=0; i<data.length; i++){
				data[i] = input.read() != 0 ;
			}
			return true;
		}
		case BCD:{
			if( input.available()< compactType.arraySize )
				return false;
			int[] data = new int[compactType.arraySize];
			defaultValue = data;
			for(int i=0; i<data.length; i++){
				int b = input.read() & 0xFF ;
				data[i] = (b>>4) * 10 + (b & 0x0F);
			}
			return true;
		}
		case INTEGER:
		case UNSIGNED:
		case ENUM:{
			if( input.available()< compactType.arraySize )
				return false;
			int[] data = new int[compactType.arraySize];
			defaultValue = data;
			for(int i=0; i<data.length; i++){
				if( tagValue == INTEGER )
					data[i] = input.read() ;
				else
					data[i] = input.read() & 0xFF ;
			}
			return true;
		}
		case LONG:
		case UNSIGNED_LONG:{
			if( input.available()< compactType.arraySize*2 )
				return false;
			int[] data = new int[compactType.arraySize];
			defaultValue = data;
			byte[] buf = new byte[2];
			for(int i=0; i<data.length; i++){
				input.read(buf);
				ByteBuffer bf = ByteBuffer.wrap(buf);
				if( tagValue == LONG )
					data[i] = bf.getShort();
				else
					data[i] = bf.getShort() & 0xFFFF;
			}
			return true;
		}
		case DOUBLE_LONG:
		case DOUBLE_LONG_UNSIGNED:
		{
			if( input.available()< compactType.arraySize*4 )
				return false;
			int[] data = new int[compactType.arraySize];
			defaultValue = data;
			byte[] buf = new byte[4];
			for(int i=0; i<data.length; i++){
				input.read(buf);
				ByteBuffer bf = ByteBuffer.wrap(buf);
				data[i] = bf.getInt();
			}
			return true;
		}
		case FLOATING_POINT:
		case FLOAT32:
		{
			if( input.available()< compactType.arraySize*4 )
				return false;
			float[] data = new float[compactType.arraySize];
			defaultValue = data;
			byte[] buf = new byte[4];
			for(int i=0; i<data.length; i++){
				input.read(buf);
				ByteBuffer bf = ByteBuffer.wrap(buf);
				data[i] = bf.getFloat();
			}
			return true;
		}
		case LONG64:
		case UNSIGNED64:
		{
			if( input.available()< compactType.arraySize*8 )
				return false;
			long[] data = new long[compactType.arraySize];
			defaultValue = data;
			byte[] buf = new byte[8];
			for(int i=0; i<data.length; i++){
				input.read(buf);
				ByteBuffer bf = ByteBuffer.wrap(buf);
				data[i] = bf.getLong();
			}
			return true;
		}
		case FLOAT64:
		{
			if( input.available()< compactType.arraySize*8 )
				return false;
			double[] data = new double[compactType.arraySize];
			defaultValue = data;
			byte[] buf = new byte[8];
			for(int i=0; i<data.length; i++){
				input.read(buf);
				ByteBuffer bf = ByteBuffer.wrap(buf);
				data[i] = bf.getDouble();
			}
			return true;
		}
		case DATE_TIME:
		case DATE:
		case TIME:
		{
			int itemLen = 12;
			if( compactType.tagValue == DATE )
				itemLen = 5;
			else if( compactType.tagValue == TIME )
				itemLen = 4;
			if( input.available() < compactType.arraySize * itemLen )
				return false;
			DlmsDateTime[] datetimes = new DlmsDateTime[compactType.arraySize];
			defaultValue = datetimes;
			byte[] buf = new byte[itemLen];
			for(int i=0; i < datetimes.length; i++){
				input.read(buf);
				datetimes[i] = new DlmsDateTime();
				datetimes[i].setDlmsDataValue(buf,0);
			}
			return true;
		}
		default:
			throw new RuntimeException("compactArray not supported type:"+compactType.tagValue);
		}
	}
	
	@Override
	public String toString(){
		if( ! this.isDecodeDone() )
			return "<DlmsData value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(256);
		sb.append("<DLMSData type=\"");
		sb.append(TYPE_NAME[tagValue]);
		sb.append("\" value=\"");
		sb.append(HexDump.toHex(value));
		sb.append("\"/>");
		return sb.toString();
	}
	
	public String getStringValue(){
		switch( tagValue ){
		case NULL:
		case DONT_CARE:
			return "";
		case BOOL:	return Boolean.toString(getBool());
		case BCD:	return Integer.toString(getBcdAsInt());
		case INTEGER:	return Integer.toString(this.getDlmsInteger());
		case UNSIGNED: return Integer.toString(this.getUnsigned());
		case ENUM: return Integer.toString(this.getEnum());
		case LONG: return Integer.toString(this.getDlmsLong());
		case UNSIGNED_LONG: return Integer.toString(this.getUnsignedLong());
		case DOUBLE_LONG: return Integer.toString(this.getDoubleLong());
		case DOUBLE_LONG_UNSIGNED: return Long.toString(this.getDoubleLongUnsigned());
		case FLOATING_POINT:
		case FLOAT32:
			return Float.toString(this.getFloat32());
		case LONG64:
			return Long.toString(this.getLong64());
		case UNSIGNED64:
			return Long.toString(this.getLong64());
		case FLOAT64:
			return Double.toString(this.getFloat64());
		case OCTET_STRING:
			return HexDump.toHex(getOctetString());
		case VISIABLE_STRING:
			return this.getVisiableString();
		case BIT_STRING:
		case ARRAY:
		case STRUCTURE:
		case COMPACT_ARRAY:
			return HexDump.toHex(this.getValue());
		case DATE_TIME:
		case DATE :
		case TIME : {
			DlmsDateTime dlmsDate = new DlmsDateTime(this);
			return dlmsDate.toString();
		}
		default:
			throw new RuntimeException("Not support yet.");
		}
	}
	
	public boolean isEmpty(){
		switch( tagValue ){
		case NULL:
		case DONT_CARE:
			return true;
		case OCTET_STRING:
		case VISIABLE_STRING:
			return null != value && value.length==1 ;
		}
		return false;
	}
	
	public String getTypeName(){
		return TYPE_NAME[tagValue];
	}
	
	public void setTypeofDateTime(){
		tagValue = DATE_TIME;
		fixedLength = 12;
	}
	
	public void setTypeofData(){
		tagValue = DATE;
		fixedLength = 5;
	}
	
	public void setTypeofTime(){
		tagValue = TIME;
		fixedLength = 4;
	}

	
	/**在设置DlmsDateTime的时候，统一不使用Date类型进行传递,替代setDlmsDateTime(Date datetime,boolean isDayLightSavingActive)*/
	public void setDlmsDateTime(String dateTime,boolean isDayLightSavingActive){
		if(dateTime==null || dateTime.length()!=19)
			throw new RuntimeException("uncorrect dateTime value,format is yyyy-MM-dd HH:mm:ss");
		DlmsDateTime dlmsDatetime = new DlmsDateTime(dateTime,isDayLightSavingActive);
		ByteBuffer buf = ByteBuffer.allocate(13);
		buf.put((byte)12);
		buf.put( dlmsDatetime.getDateTimeValue() );
		value = buf.array();
		tagValue = OCTET_STRING;
	}
	/**在设置DlmsDateTime的时候，统一不使用Date类型进行传递,替代setDlmsDateTime(Date datetime)*/
	public void setDlmsDateTime(String dateTime){
		if(dateTime==null || dateTime.length()!=19)
			throw new RuntimeException("uncorrect dateTime value,format is yyyy-MM-dd HH:mm:ss");
		DlmsDateTime dlmsDatetime = new DlmsDateTime(dateTime);
		ByteBuffer buf = ByteBuffer.allocate(13);
		buf.put((byte)12);
		buf.put( dlmsDatetime.getDateTimeValue() );
		value = buf.array();
		
		tagValue = OCTET_STRING;
	}
	/**
	 * 旧的时间格式,用于读通道数据。
	 */
	public void setOldDlmsDateTime(String dateTime){
		if(dateTime==null || dateTime.length()!=19)
			throw new RuntimeException("uncorrect dateTime value,format is yyyy-MM-dd HH:mm:ss");
		DlmsDateTime dlmsDatetime = new DlmsDateTime(dateTime);
		ByteBuffer buf = ByteBuffer.allocate(12);
		buf.put( dlmsDatetime.getDateTimeValue() );
		value = buf.array();
		tagValue = DATE_TIME;
	}
	
	public void changeToOldTime(){
		if(tagValue!=OCTET_STRING){
			throw new RuntimeException("can't change to old time.this tag!=OCTET,tag="+tagValue);
		}
		ByteBuffer buf = ByteBuffer.allocate(12);
		buf.put(this.getDateTime().getDateTimeValue());
		value = buf.array();
		tagValue = DATE_TIME;
	}
	/**设置DlmsDate 格式2012-08-09*/
	public void setDlmsDate( String date){
//		tagValue = DATE;
		if(date!=null && date.length()==10)
			date+=" 00:00:00";
		if(date.length()!=19){
			throw new RuntimeException("uncorrect dateTime value,format is yyyy-MM-dd HH:mm:ss or yyyy-MM-dd");
		}
		DlmsDateTime dlmsDatetime = new DlmsDateTime(date);
		ByteBuffer buf = ByteBuffer.allocate(6);
		buf.put((byte)5);
		buf.put( dlmsDatetime.getDateValue() );
		value = buf.array();
		tagValue = OCTET_STRING;
	}
	/**设置DlmsTime 格式 08:00:00*/
	public void setDlmsTime(String time){
		if(time!=null && time.length()==8)
			time="0000-00-00 "+time;
		if(time.length()!=19){
			throw new RuntimeException("uncorrect dateTime value,format is yyyy-MM-dd HH:mm:ss or HH:mm:ss");
		}
		DlmsDateTime dlmsDatetime = new DlmsDateTime( time );
		ByteBuffer buf = ByteBuffer.allocate(5);
		buf.put((byte)4);
		buf.put( dlmsDatetime.getTimeValue() );
		value = buf.array();
		tagValue = OCTET_STRING;
	}
	/**
	 * 时间为伊朗历，不建议
	 * @param datetime
	 */
	public void setDlmsDateTime(Date datetime){
//		tagValue = DATE_TIME;
		DlmsDateTime dlmsDatetime = new DlmsDateTime(datetime);
		ByteBuffer buf = ByteBuffer.allocate(13);
		buf.put((byte)12);
		buf.put( dlmsDatetime.getDateTimeValue() );
		value = buf.array();
		
		tagValue = OCTET_STRING;
	}
	/**include daylight saving active*/
	public void setDlmsDateTime(Date datetime,boolean isDayLightSavingActive){
		DlmsDateTime dlmsDatetime = new DlmsDateTime(datetime,isDayLightSavingActive);
		ByteBuffer buf = ByteBuffer.allocate(13);
		buf.put((byte)12);
		buf.put( dlmsDatetime.getDateTimeValue() );
		value = buf.array();
		tagValue = OCTET_STRING;
	}
	public void setDlmsDate( Date date){
//		tagValue = DATE;
		DlmsDateTime dlmsDatetime = new DlmsDateTime(date);
		ByteBuffer buf = ByteBuffer.allocate(6);
		buf.put((byte)5);
		buf.put( dlmsDatetime.getDateValue() );
		value = buf.array();
		tagValue = OCTET_STRING;
	}
	
	public void setDlmsTime( Date time ){
//		tagValue = TIME;
		DlmsDateTime dlmsDatetime = new DlmsDateTime( time );
		ByteBuffer buf = ByteBuffer.allocate(5);
		buf.put((byte)4);
		buf.put( dlmsDatetime.getTimeValue() );
		value = buf.array();
		tagValue = OCTET_STRING;
	}
	
	public DlmsDateTime getDateTime(){
		if( tagValue == OCTET_STRING ){
			int len = value[0];
			if( len == 12 || len == 5 || len == 4 ){
				DlmsDateTime result = new DlmsDateTime();
				result.setDlmsDataValue(value, 1);
				return result;
			}
		}
		else if( tagValue == DATE_TIME || tagValue == DATE || tagValue == TIME ){
			return new DlmsDateTime(this);
		}
		throw new RuntimeException("Invalid date-time type:"+ getTypeName() );
	}
	
	public int type(){
		return tagValue;
	}
}
