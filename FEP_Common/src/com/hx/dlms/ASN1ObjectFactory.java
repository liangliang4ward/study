/**
 * Used by ASN1SequenceOf decoding to create specified ASN object.
 */
package com.hx.dlms;

/**
 * @author hbao
 *
 */
public interface ASN1ObjectFactory {
	ASN1Type create();
}
