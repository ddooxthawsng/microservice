package thangdm12.account_service.service;

import java.util.List;

import thangdm12.account_service.model.AccountDTO;


public interface AccountService {
    void add(AccountDTO accountDTO);

    void update(AccountDTO accountDTO);

    void updatePassword(AccountDTO accountDTO);

    void delete(Long id);

    List<AccountDTO> getAll();

    AccountDTO getOne(Long id);
}

