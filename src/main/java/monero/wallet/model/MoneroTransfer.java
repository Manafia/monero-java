package monero.wallet.model;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import monero.utils.MoneroUtils;

/**
 * Models a directional transfer of funds from or to a wallet.
 */
public class MoneroTransfer {

  private MoneroTxWallet tx;
  private String address;
  private Integer accountIndex;
  private Integer subaddressIndex;
  private BigInteger amount;
  private List<MoneroDestination> destinations;
  
  public MoneroTransfer() {
    // nothing to initialize
  }
  
  public MoneroTransfer(MoneroTransfer transfer) {
    this.address = transfer.address;
    this.accountIndex = transfer.accountIndex;
    this.subaddressIndex = transfer.subaddressIndex;
    this.amount = transfer.amount;
    if (transfer.destinations != null) {
      this.destinations = new ArrayList<MoneroDestination>();
      for (MoneroDestination destination : transfer.getDestinations()) {
        this.destinations.add(destination.copy()); 
      }
    }
  }
  
  public MoneroTxWallet getTx() {
    return tx;
  }
  
  public MoneroTransfer setTx(MoneroTxWallet tx) {
    this.tx = tx;
    return this;
  }
  
  public Boolean getIsOutgoing() {
    return this == tx.getOutgoingTransfer();
  }
  
  public Boolean getIsIncoming() {
    if (tx.getIncomingTransfers() == null) return false;
    return tx.getIncomingTransfers().contains(this);
  }
  
  public String getAddress() {
    return address;
  }
  
  public MoneroTransfer setAddress(String address) {
    this.address = address;
    return this;
  }
  
  public Integer getAccountIndex() {
    return accountIndex;
  }
  
  public MoneroTransfer setAccountIndex(Integer accountIndex) {
    this.accountIndex = accountIndex;
    return this;
  }
  
  public Integer getSubaddressIndex() {
    return subaddressIndex;
  }
  
  public MoneroTransfer setSubaddressIndex(Integer subaddressIndex) {
    this.subaddressIndex = subaddressIndex;
    return this;
  }
  
  public BigInteger getAmount() {
    return amount;
  }
  
  public MoneroTransfer setAmount(BigInteger amount) {
    this.amount = amount;
    return this;
  }
  
  public List<MoneroDestination> getDestinations() {
    return destinations;
  }
  
  public MoneroTransfer setDestinations(List<MoneroDestination> destinations) {
    this.destinations = destinations;
    return this;
  }
  
  public MoneroTransfer copy() {
    return new MoneroTransfer(this);
  }
  
  /**
   * Updates this transaction by merging the latest information from the given
   * transaction.
   * 
   * Merging can modify or build references to the transfer given so it
   * should not be re-used or it should be copied before calling this method.
   * 
   * @param transfer is the transfer to merge into this one
   */
  public MoneroTransfer merge(MoneroTransfer transfer) {
    assert(transfer instanceof MoneroTransfer);
    if (this == transfer) return this;
    
    // merge transactions if they're different which comes back to merging transfers
    if (this.getTx() != transfer.getTx()) this.getTx().merge(transfer.getTx());
    
    // otherwise merge transfer fields
    else {
      this.setAddress(MoneroUtils.reconcile(this.getAddress(), transfer.getAddress()));
      this.setAccountIndex(MoneroUtils.reconcile(this.getAccountIndex(), transfer.getAccountIndex()));
      this.setSubaddressIndex(MoneroUtils.reconcile(this.getSubaddressIndex(), transfer.getSubaddressIndex()));
      this.setAmount(MoneroUtils.reconcile(this.getAmount(), transfer.getAmount()));
      
      // merge destinations
      if (this.getDestinations() == null) this.setDestinations(transfer.getDestinations());
      else if (transfer.getDestinations() != null) {
        assertEquals("Cannot merge transfer because destinations are different", this.getDestinations(), transfer.getDestinations());
      }
    }
    
    return this;
  }
  
  public String toString() {
    return toString(0);
  }
  
  public String toString(int indent) {
    StringBuilder sb = new StringBuilder();
    sb.append(MoneroUtils.kvLine("Is outgoing", this.getIsOutgoing(), indent));
    sb.append(MoneroUtils.kvLine("Address", this.getAddress(), indent));
    sb.append(MoneroUtils.kvLine("Account index", this.getAccountIndex(), indent));
    sb.append(MoneroUtils.kvLine("Subaddress index", this.getSubaddressIndex(), indent));
    sb.append(MoneroUtils.kvLine("Amount", this.getAmount() != null ? this.getAmount().toString() : null, indent));
    if (this.getDestinations() != null) {
      sb.append(MoneroUtils.kvLine("Destinations", "", indent));
      for (int i = 0; i < this.getDestinations().size(); i++) {
        sb.append(MoneroUtils.kvLine(i + 1, "", indent + 1));
        sb.append(getDestinations().get(i).toString(indent + 2) + "\n");
      }
    }
    String str = sb.toString();
    return str.substring(0, str.length() - 1);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accountIndex == null) ? 0 : accountIndex.hashCode());
    result = prime * result + ((address == null) ? 0 : address.hashCode());
    result = prime * result + ((amount == null) ? 0 : amount.hashCode());
    result = prime * result + ((destinations == null) ? 0 : destinations.hashCode());
    result = prime * result + ((subaddressIndex == null) ? 0 : subaddressIndex.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    MoneroTransfer other = (MoneroTransfer) obj;
    if (accountIndex == null) {
      if (other.accountIndex != null) return false;
    } else if (!accountIndex.equals(other.accountIndex)) return false;
    if (address == null) {
      if (other.address != null) return false;
    } else if (!address.equals(other.address)) return false;
    if (amount == null) {
      if (other.amount != null) return false;
    } else if (!amount.equals(other.amount)) return false;
    if (destinations == null) {
      if (other.destinations != null) return false;
    } else if (!destinations.equals(other.destinations)) return false;
    if (subaddressIndex == null) {
      if (other.subaddressIndex != null) return false;
    } else if (!subaddressIndex.equals(other.subaddressIndex)) return false;
    return true;
  }
}
