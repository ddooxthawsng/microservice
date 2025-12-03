package thangdm12.account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thangdm12.account_service.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
