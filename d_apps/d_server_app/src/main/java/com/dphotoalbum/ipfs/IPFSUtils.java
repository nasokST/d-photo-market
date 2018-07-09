package com.dphotoalbum.ipfs;

import java.math.BigInteger;
import java.util.Arrays;

import com.dphotoalbum.objects.IPFSHashInterface;

import io.ipfs.multihash.Multihash;

public class IPFSUtils {
	
	public static IPFSHashInterface uploadDoc(byte[] docFile) {
		IPFSHashInterface hashInterface = null;
		
		// TODO file uploading
		
		return hashInterface;
	}
	
	public static byte[] downloadFile(IPFSHashInterface hashInterface) {
		byte[] docFile = null;
		
		// TODO file downloading
		
		return docFile;
	}
	
	public static IPFSHashInterface ipfsHashToHashInterface(Multihash multihash) {
		IPFSHashInterface hashInterface = new IPFSHashInterface();

		hashInterface.digest = Arrays.copyOfRange(multihash.toBytes(), 2, multihash.toBytes().length);
		hashInterface.hashFunction = new BigInteger(String.valueOf(multihash.toBytes()[0]));
		hashInterface.size = new BigInteger(String.valueOf(multihash.toBytes()[1]));

		return hashInterface;
	}

	public static Multihash hashInterfaceToIPFSHash(IPFSHashInterface hashInterface) {
		
		byte[] multihashBts = new byte[hashInterface.digest.length + 2];
		
		multihashBts[0] = hashInterface.hashFunction.byteValue();
		multihashBts[1] = hashInterface.size.byteValue();
		System.arraycopy(hashInterface.digest, 0, multihashBts, 2, hashInterface.digest.length);

		Multihash multihash = new Multihash(multihashBts);
		return multihash;
	}	
}
