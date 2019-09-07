package Ian.demo.AxonSimple.Controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Ian.demo.AxonSimple.domain.command.AccountCreateCommand;
import Ian.demo.AxonSimple.domain.command.AccountDepositCommand;
import Ian.demo.AxonSimple.domain.command.AccountWithdrawCommand;
import Ian.demo.AxonSimple.domain.entity.Account;

@RestController
@RequestMapping("/")
public class DemoService {
	@Autowired
	private CommandGateway commandGateway;
	
	@Autowired
    private QueryGateway queryGateway;

	@PostMapping("")
	public CompletableFuture<Object> createBankAccount(@RequestParam String name) {
		UUID accountId = UUID.randomUUID();
		AccountCreateCommand createAccountCommand = new AccountCreateCommand(accountId.toString(), name);
		return commandGateway.send(createAccountCommand);
	}

	@PutMapping("/{accountId}/deposit/{amount}")
	public CompletableFuture<Object> depositMoney(@PathVariable String accountId, @PathVariable Double amount) {
		return commandGateway.send(new AccountDepositCommand(accountId, amount));
	}
	
	@PutMapping("/{accountId}/withdraw/{amount}")
	public CompletableFuture<Object> WithdrawMoney(@PathVariable String accountId, @PathVariable Double amount) {
		return commandGateway.send(new AccountWithdrawCommand(accountId, amount));
	}
	
	
	@GetMapping("/q/{accountId}")
	public Object getall(@PathVariable String accountId) throws InterruptedException, ExecutionException {
		return queryGateway.query(accountId, Account.class).get();
	}
}
