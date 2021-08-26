
import java.security.PublicKey;
import java.util.Set;

abstract public class Exceptions extends Exception {

    public Exceptions(String name) {
        super(name);
    }

    static public void diagnostics(Exceptions ex) {
        System.err.println("\n\n");
        System.err.println("\t" + ex.getClass().getSimpleName());
        System.err.println("\t" + ex.getMessage());
        System.err.println("\n");
    }

}

/**
 * @brief CoinConsumedMultipleTimesException
 * 
 * @implNote (3) no UTXO is claimed multiple times by {@code tx},
 * 
 */
class CoinConsumedMultipleTimesException extends Exceptions {

    public CoinConsumedMultipleTimesException() {
        super("CoinConsumedMultipleTimesException");
    }

    static public void assertion(Set<UTXO> claimedUTXO, InputInterface input)
            throws CoinConsumedMultipleTimesException {
        UTXO utxo = new UTXO(input);
        if (!claimedUTXO.add(utxo))
            throw new CoinConsumedMultipleTimesException();
    }

}

/**
 * @brief VerifySignatureOfConsumeCoinException
 * 
 * @implNote (2) the signatures on each input of {@code tx} are valid,
 * 
 */

class VerifySignatureOfConsumeCoinException extends Exceptions {

    public VerifySignatureOfConsumeCoinException() {
        super("VerifySignatureOfConsumeCoinException");
    }

    static public void assertion(UTXOPool utxoPool, TransactionInterface tx, int index, InputInterface input)
            throws VerifySignatureOfConsumeCoinException {
        UTXO utxo = new UTXO(input);
        OutputInterface correspondingOutput = utxoPool.getTxOutput(utxo);
        PublicKey pk = correspondingOutput.getAddress();
        if (!new Crypto().verifySignature(pk, tx.getRawDataToSign(index), input.getSignature()))
            throw new VerifySignatureOfConsumeCoinException();
    }

}

/**
 * @brief
 * 
 * @implNote (1) all outputs claimed by {@code tx} are in the current UTXO pool,
 * 
 */
class ConsumedCoinAvailableException extends Exceptions {

    public ConsumedCoinAvailableException() {
        super("ConsumedCoinAvailableException");
    }

    static public void assertion(UTXOPool utxoPool, InputInterface input) throws ConsumedCoinAvailableException {
        UTXO utxo = new UTXO(input);
        if (!utxoPool.contains(utxo))
            throw new ConsumedCoinAvailableException();
    }

}

/**
 * @brief TransactionOutputLessThanZeroException
 * 
 * @implNote (4) all of {@code tx}s output values are non-negative, and
 * 
 */
class TransactionOutputLessThanZeroException extends Exceptions {

    public TransactionOutputLessThanZeroException() {
        super("TransactionOutputLessThanZeroException");
    }

    static public void assertion(OutputInterface output) throws TransactionOutputLessThanZeroException {
        if (output.getValue() < 0)
            throw new TransactionOutputLessThanZeroException();
    }

}

/**
 * @brief TransactionInputSumLessThanOutputSumException
 * 
 * @implNote (5) the sum of {@code tx}s input values is greater than or equal to
 *           the sum of its output values; and false otherwise.
 */

class TransactionInputSumLessThanOutputSumException extends Exceptions {

    public TransactionInputSumLessThanOutputSumException() {
        super("TransactionOutputSumLessThanInputSumException");
    }

    static public void assertion(double outputSum, double inputSum)
            throws TransactionInputSumLessThanOutputSumException {
        if (outputSum > inputSum)
            throw new TransactionInputSumLessThanOutputSumException();
    }

}
