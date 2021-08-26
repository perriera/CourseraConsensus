
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

    // static public void assertion(Set<UTXO> claimedUTXO, InputInterface input)
    //         throws CoinConsumedMultipleTimesException {
    //     UTXO utxo = new UTXO(input);
    //     if (!claimedUTXO.add(utxo))
    //         throw new CoinConsumedMultipleTimesException();
    // }

}

