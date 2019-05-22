package monero.wallet.model;

/**
 * Receives progress notifications as a wallet is synchronized.
 */
public interface MoneroSyncListener {

  /**
   * Invoked with info as sync progress is made.
   * 
   * @param height is the height of the last block processed during a sync session
   * @param numBlocksDone is the number of blocks synced
   * @param numBlocksTotal is the total number of blocks to sync
   * @param percentDone is the sync progress as a percentage
   * @param message is a human-readable description of the current progress
   */
  public void onSyncProgress(Long height, long numBlocksDone, long numBlocksTotal, double percentDone, String message);
}
