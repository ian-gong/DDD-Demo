package Ian.demo.AxonSimple.domain.query;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import Ian.demo.AxonSimple.domain.entity.Account;
import Ian.demo.AxonSimple.domain.entity.AccountEntity;
import Ian.demo.AxonSimple.domain.event.AccountCreatedEvent;
import Ian.demo.AxonSimple.domain.event.AccountMoneyDepositedEvent;
import Ian.demo.AxonSimple.domain.event.AccountMoneyWithdrawnEvent;

@Component
//@EventHandler 用户物化视图的处理
//@QueryHandler 和 @CommandHandler 是同级的，理论上不应该写到物化视图处理这里
public class AccountProjector {

	@Autowired
	AxonConfiguration axonConfiguration;

	@Autowired 
	private AccountEntityRepository accountEntityRepository;

	//@EventHandler
	public void on(AccountCreatedEvent event) {
		AccountEntity account = new AccountEntity(event.getAccountId(), event.getName());
		accountEntityRepository.save(account);
		System.out.println("---------------this is @EventHandler----------------");
	}

	///@EventHandler 
	public void on(AccountMoneyDepositedEvent event) {
		String accountId = event.getAccountId();
		AccountEntity accountView = accountEntityRepository.getOne(accountId);
		accountView.setDeposit(accountView.getDeposit() + event.getAmount());
		accountEntityRepository.save(accountView);
	}

	//@EventHandler
	public void on(AccountMoneyWithdrawnEvent event) {
		String accountId = event.getAccountId();
		AccountEntity accountView = accountEntityRepository.getOne(accountId);
		accountView.setDeposit(accountView.getDeposit() - event.getAmount());
		accountEntityRepository.save(accountView);
	}

	@QueryHandler
	public Account query(String Accountid) {
		// WARN: 强烈不建议使用这种方式将聚合数据暴露给外界，而应该使用物化视图的方式将保存的视图数据显示出来。
		// 这里这样做，只是用于debug，有时候，可能写的代码有问题，导致聚合数据跟视图数据不一致。
		final Account[] theOrder = new Account[1];
		Repository<Account> orderRepository = axonConfiguration.repository(Account.class);
		orderRepository.load(Accountid).execute(order -> {
			theOrder[0] = order;
		});

		return theOrder[0];
	}
}
