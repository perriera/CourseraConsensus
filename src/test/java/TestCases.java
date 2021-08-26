
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

abstract public class TestCases {
	protected ScroogeCoinAuthority authority;
	protected ScroogeCoinPool bitcoins;
	protected TxHandlerInterface txHandler;

	@Before
	public void setUpCoin() throws Exception {
		authority = new ScroogeCoinAuthority();
		bitcoins = new ScroogeCoinPool(authority);
	}

	abstract public void setUpHandler() throws Exception;

	@Test(expected = VerifySignatureOfConsumeCoinException.class)
	public void testValidTxSign() throws Exception {

		/**
		 * In the first transaction, we assume that Scrooge has created 10 coins and
		 * assigned them to himself, we don’t doubt that because the system-Scroogecoin
		 * has a building rule which says that Scrooge has right to create coins.
		 * 
		 * In the second transaction, Scrooge transferred 10 coins to Alice.
		 * 
		 */
		Transaction tx1 = new Transaction();
		tx1.addInput(bitcoins.getGenesiseTx().getHash(), 0);
		tx1.addOutput(10, authority.getAlice().getPublicKey());
		byte[] sig1 = authority.signMessage(authority.getAlice().getPrivateKey(), tx1.getRawDataToSign(0));
		tx1.addSignature(sig1, 0);
		tx1.finalize();
		assertFalse(txHandler.isValidTx(tx1));
	}

	@Test
	public void testValidTxSign2() throws Exception {

		/**
		 * In the second transaction, Scrooge transferred 3.9 coins to Alice and 5.9
		 * coins to Bob. The sum of the two outputs is 0.2 less than the input because
		 * the transaction fee was 0.2 coin.
		 */
		Transaction tx2 = new Transaction();
		tx2.addInput(bitcoins.getGenesiseTx().getHash(), 0);
		tx2.addOutput(10, authority.getAlice().getPublicKey());
		byte[] sig2 = authority.signMessage(authority.getCreator().getPrivateKey(), tx2.getRawDataToSign(0));
		tx2.addSignature(sig2, 0);
		tx2.finalize();
		assertTrue(txHandler.isValidTx(tx2));

		/**
		 * In the third transaction, there were two inputs and one output, Alice and Bob
		 * transferred 9.7 coins to mike, and the transaction fee was 0.1 coin.
		 * 
		 */
		Transaction tx3 = new Transaction();
		tx3.addInput(bitcoins.getGenesiseTx().getHash(), 0);
		tx3.addOutput(4, authority.getAlice().getPublicKey());
		tx3.addOutput(6, authority.getBob().getPublicKey());
		byte[] sig3 = authority.signMessage(authority.getCreator().getPrivateKey(), tx3.getRawDataToSign(0));
		tx3.addSignature(sig3, 0);
		tx3.finalize();
		assertTrue(txHandler.isValidTx(tx3));
	}

	@Test(expected = TransactionInputSumLessThanOutputSumException.class)
	public void testValidTxValue() throws Exception {
		Transaction tx = new Transaction();
		tx.addInput(bitcoins.getGenesiseTx().getHash(), 0);
		tx.addOutput(4, authority.getAlice().getPublicKey());
		tx.addOutput(7, authority.getBob().getPublicKey());
		byte[] sig = authority.signMessage(authority.getCreator().getPrivateKey(), tx.getRawDataToSign(0));
		tx.addSignature(sig, 0);
		tx.finalize();
		txHandler.isValidTx(tx);
	}

	@Test(expected = TransactionOutputLessThanZeroException.class)
	public void testValidTxValue2() throws Exception {
		Transaction tx1 = new Transaction();
		tx1.addInput(bitcoins.getGenesiseTx().getHash(), 0);
		tx1.addOutput(4, authority.getAlice().getPublicKey());
		tx1.addOutput(-7, authority.getBob().getPublicKey());
		byte[] sig1 = authority.signMessage(authority.getCreator().getPrivateKey(), tx1.getRawDataToSign(0));
		tx1.addSignature(sig1, 0);
		tx1.finalize();
		txHandler.isValidTx(tx1);
	}

	@Test
	public void testTransfer() throws Exception {
		/** 
		 * Scrooge transfer 10 coins to Alice
		 */
		TransactionInterface tx1 = new Transaction();
		tx1.addInput(bitcoins.getGenesiseTx().getHash(), 0);
		tx1.addOutput(10, authority.getAlice().getPublicKey());
		byte[] sig1 = authority.signMessage(authority.getCreator().getPrivateKey(), tx1.getRawDataToSign(0));
		tx1.addSignature(sig1, 0);
		tx1.finalize();

		assertTrue(txHandler.isValidTx(tx1));
		TransactionInterface[] acceptedRx = txHandler.handleTxs(new TransactionInterface[] { tx1 });
		assertEquals(acceptedRx.length, 1);

		/** 
		 * Alice transfer 4 to bob, 6 to mike
		 */
		Transaction tx2 = new Transaction();
		tx2.addInput(tx1.getHash(), 0);
		tx2.addOutput(4, authority.getBob().getPublicKey());
		tx2.addOutput(6, authority.getMike().getPublicKey());
		byte[] sig2 = authority.signMessage(authority.getAlice().getPrivateKey(), tx2.getRawDataToSign(0));
		tx2.addSignature(sig2, 0);
		tx2.finalize();

		assertTrue(txHandler.isValidTx(tx2));
		acceptedRx = txHandler.handleTxs(new Transaction[] { tx2 });
		assertEquals(acceptedRx.length, 1);
	}

	@Test
	public void testMultipTxDepenonEachother() throws Exception {
		// Scrooge transfer 10 coins to Alice
		Transaction tx1 = new Transaction();
		tx1.addInput(bitcoins.getGenesiseTx().getHash(), 0);
		tx1.addOutput(10, authority.getAlice().getPublicKey());
		byte[] sig1 = authority.signMessage(authority.getCreator().getPrivateKey(), tx1.getRawDataToSign(0));
		tx1.addSignature(sig1, 0);
		tx1.finalize();

		// Alice transfer 4 to bob, 6 to mike
		Transaction tx2 = new Transaction();
		tx2.addInput(tx1.getHash(), 0);
		tx2.addOutput(4, authority.getBob().getPublicKey());
		tx2.addOutput(6, authority.getMike().getPublicKey());
		byte[] sig2 = authority.signMessage(authority.getAlice().getPrivateKey(), tx2.getRawDataToSign(0));
		tx2.addSignature(sig2, 0);
		tx2.finalize();

		// Bob transfer 4 to mike
		Transaction tx3 = new Transaction();
		tx3.addInput(tx2.getHash(), 0);
		tx3.addOutput(4, authority.getMike().getPublicKey());
		byte[] sig3 = authority.signMessage(authority.getBob().getPrivateKey(), tx3.getRawDataToSign(0));
		tx3.addSignature(sig3, 0);
		tx3.finalize();

		TransactionInterface[] acceptedRx = txHandler.handleTxs(new Transaction[] { tx1, tx2, tx3 });
		assertEquals(acceptedRx.length, 3);
	}

	@Test(expected = ConsumedCoinAvailableException.class)
	public void testDoubleSpending() throws Exception {
		// Scrooge transfer 10 coins to Alice
		Transaction tx1 = new Transaction();
		tx1.addInput(bitcoins.getGenesiseTx().getHash(), 0);
		tx1.addOutput(10, authority.getAlice().getPublicKey());
		byte[] sig1 = authority.signMessage(authority.getCreator().getPrivateKey(), tx1.getRawDataToSign(0));
		tx1.addSignature(sig1, 0);
		tx1.finalize();

		assertTrue(txHandler.isValidTx(tx1));
		TransactionInterface[] acceptedRx = txHandler.handleTxs(new Transaction[] { tx1 });
		assertEquals(acceptedRx.length, 1);

		// Alice transfer 10 coins to bob
		Transaction tx2 = new Transaction();
		tx2.addInput(tx1.getHash(), 0);
		tx2.addOutput(10, authority.getBob().getPublicKey());
		byte[] sig2 = authority.signMessage(authority.getAlice().getPrivateKey(), tx2.getRawDataToSign(0));
		tx2.addSignature(sig2, 0);
		tx2.finalize();
		assertTrue(txHandler.isValidTx(tx2));
		acceptedRx = txHandler.handleTxs(new Transaction[] { tx2 });
		assertEquals(acceptedRx.length, 1);

		// Alice then transfer the same 10 coins to mike
		Transaction tx3 = new Transaction();
		tx3.addInput(tx1.getHash(), 0);
		tx3.addOutput(10, authority.getBob().getPublicKey());
		byte[] sig3 = authority.signMessage(authority.getAlice().getPrivateKey(), tx3.getRawDataToSign(0));
		tx3.addSignature(sig3, 0);
		tx3.finalize();
		txHandler.isValidTx(tx3);
	}

}
