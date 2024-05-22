package nl.inholland.BankAPI.Model;

import jakarta.persistence.Entity;
import lombok.Data;


public enum TransactionType {
    TRANSFER,
    DEPOSIT,
    WITHDRAWAL
}
