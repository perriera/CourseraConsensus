import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class UTXOPool implements UTXOPoolInterface {

    /**
     * The current collection of UTXOs, with each one mapped to its corresponding
     * transaction output
     */
    private HashMap<UTXO, OutputInterface> H;

    /** Creates a new empty UTXOPool */
    public UTXOPool() {
        H = new HashMap<UTXO, OutputInterface>();
    }

    /** Creates a new UTXOPool that is a copy of {@code uPool} */
    public UTXOPool(UTXOPool uPool) {
        H = new HashMap<UTXO, OutputInterface>(uPool.H);
    }

    /**
     * Adds a mapping from UTXO {@code utxo} to transaction output @code{txOut} to
     * the pool
     */
    public void addUTXO(UTXO utxo, OutputInterface txOut) {
        H.put(utxo, txOut);
    }

    /** Removes the UTXO {@code utxo} from the pool */
    public void removeUTXO(UTXO utxo) {
        H.remove(utxo);
    }

    /**
     * @return the transaction output corresponding to UTXO {@code utxo}, or null if
     *         {@code utxo} is not in the pool.
     */
    public OutputInterface getTxOutput(UTXO ut) {
        return H.get(ut);
    }

    /** @return true if UTXO {@code utxo} is in the pool and false otherwise */
    public boolean contains(UTXO utxo) {
        return H.containsKey(utxo);
    }

    /** Returns an {@code ArrayList} of all UTXOs in the pool */
    public ArrayList<UTXO> getAllUTXO() {
        Set<UTXO> setUTXO = H.keySet();
        ArrayList<UTXO> allUTXO = new ArrayList<UTXO>();
        for (UTXO ut : setUTXO) {
            allUTXO.add(ut);
        }
        return allUTXO;
    }
}
