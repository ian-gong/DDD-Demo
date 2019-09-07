package Ian.demo.AxonSimple.domain.query;

import org.springframework.data.jpa.repository.JpaRepository;

import Ian.demo.AxonSimple.domain.entity.AccountEntity;

public interface AccountEntityRepository extends JpaRepository<AccountEntity, String> {
}
