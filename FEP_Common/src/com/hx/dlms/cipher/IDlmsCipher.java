/**
 * DLMS cipher interface.
 * Local soft-encryption is implemented.
 * Encryption machine should be implemented as required.
 */
package com.hx.dlms.cipher;

import java.io.IOException;

import com.hx.dlms.aa.DlmsContext;

/**
 * @author: Adam Bao, hbao2k@gmail.com
 *
 */
public interface IDlmsCipher {
	
	byte[] createGcmNewKey(DlmsContext context, byte[] plain, byte[] initVector)throws IOException;

	byte[] auth(DlmsContext context, byte[] plain, byte[] initVector) throws IOException;

	byte[] encrypt(DlmsContext context, byte[] plain, byte[] initVector ) throws IOException;
	
	byte[] decrypt(DlmsContext context, byte[] ciphered, byte[] initVector ) throws IOException;
}
