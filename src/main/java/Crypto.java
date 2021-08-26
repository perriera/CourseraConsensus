import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class Crypto implements CryptoInterface {

	public Crypto() {}
	
	/**
	 * @return true is {@code signature} is a valid digital signature of
	 *         {@code message} under the key {@code pubKey}. Internally, this uses
	 *         RSA signature, but the student does not have to deal with any of the
	 *         implementation details of the specific signature algorithm
	 */
	public boolean verifySignature(PublicKey pubKey, byte[] message, byte[] signature) {
		Signature sig = null;
		try {
			sig = Signature.getInstance("SHA1withDSA", "SUN");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sig.initVerify(pubKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		try {
			sig.update(message);
			return sig.verify(signature);
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return false;

	}
}
