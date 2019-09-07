package Ian.demo.AxonSimple.domain.entity;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.stereotype.Service;

import Ian.demo.AxonSimple.domain.command.*;
import Ian.demo.AxonSimple.domain.event.*;
import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import javax.persistence.Entity;
import javax.persistence.Id;

//@CommandHandler 用于接收聚合命令
//@EventSourcingHandler 用户生成聚合对象(如果物化视图数据丢失，可通过@EventSourcingHandler还原集合对象)
@Aggregate
public class Account {
	
	
	@AggregateIdentifier
	private String accountId;

	private Double deposit;

	public Account() {

		System.out.println("--------------我被实例化了---------------");
	}

	@CommandHandler
	public Account(AccountCreateCommand command) {
		System.out.println("-----------Account AccountCreateCommand begin");
		apply(new AccountCreatedEvent(command.getAccountId(), command.getName()));
	}

	@CommandHandler
	public void handle(AccountDepositCommand command) {
		apply(new AccountMoneyDepositedEvent(command.getAccountId(), command.getAmount()));
	}

	@CommandHandler
	public void handle(AccountWithdrawCommand command) {
		if (deposit >= command.getAmount()) {
			apply(new AccountMoneyWithdrawnEvent(command.getAccountId(), command.getAmount()));
		} else {
			throw new IllegalArgumentException("余额不足");
		}
	}

	@EventSourcingHandler
	public void on(AccountCreatedEvent event) {
		System.out.println("-----------Account AccountCreatedEvent begin");
		this.accountId = event.getAccountId();
		this.deposit = 1d;
	}

	

	@EventSourcingHandler
	public void on(AccountMoneyDepositedEvent event) {
		System.out.println("-----------Account AccountMoneyDepositedEvent begin");
		this.deposit += event.getAmount();
	}

	@EventSourcingHandler
	public void on(AccountMoneyWithdrawnEvent event) {
		 
		System.out.println("-----------Account AccountMoneyWithdrawnEvent begin");
		this.deposit -= event.getAmount();
	}

	public String getAccountId() {
		return accountId;
	}

	public Double getDeposit() {
		return deposit;
	}
}
