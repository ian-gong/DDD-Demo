package Ian.demo.AxonSimple.domain.command;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class AccountCreateCommand {
	@TargetAggregateIdentifier
	private String accountId;

	private String name;

	public AccountCreateCommand(String accountId, String name) {
		this.accountId = accountId;
		this.name = name;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
