package com.example.adapter;


import com.example.entity.User;
import com.example.entity.Wallet;
import com.example.entity.WalletHistory;
import com.example.ports.spi.WalletPersistencePort;
import com.example.repository.UserRepository;
import com.example.repository.WalletHistoryRepository;
import com.example.repository.WalletRepository;
import com.example.util.RandomStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

import static util.SecurityUtils.getCurrentUserLogin;

@Service
public class WalletJpaAdapter implements WalletPersistencePort {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Método para crear una nueva cartera para el usuario en la sesión actual.
     *
     * @return ResponseEntity indicando al usuario si se ha completado la operación o ha surgido un error.
     */
    @Override
    public ResponseEntity createWallet() {
        String outputMsg = "";
        Wallet newWallet = new Wallet();
        RandomStringUtil randomStringUtil = new RandomStringUtil();

        User currentUser = userRepository.findByLogin(getCurrentUserLogin());

        String address = randomStringUtil.getAlphaNumericString(45);
        newWallet.setAddress(address);
        newWallet.setUser(currentUser);
        List<Wallet> walletList = walletRepository.findAll();

        boolean found = false;

        for (Wallet wallet : walletList) {
            if (newWallet.getAddress().equals(wallet.getAddress()))
                found = true;
        }
        if (found) {
            outputMsg = "No se puede dar de alta la cartera con direccion '" + newWallet.getAddress() + "' porque ya existe";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        } else {
            walletRepository.save(newWallet);
            outputMsg = "Wallet con address " + newWallet.getAddress() + " creada correctamente para el usuario "
                    + getCurrentUserLogin();
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.OK);
        }
    }

    public ResponseEntity getWallets() {
        User currentUser = userRepository.findByLogin(getCurrentUserLogin());
        List<Wallet> walletList = walletRepository.findByUserId(currentUser.getId());
        Map<String, Double> mapAddressBalance = new LinkedHashMap<>();

        for (Wallet wallet :
                walletList) {
            mapAddressBalance.put(wallet.getAddress(), wallet.getBalance());
        }

        return new ResponseEntity(mapAddressBalance, HttpStatus.OK);
    }

    /**
     * Método para añadir fondos a una cartera específica que se obtiene a partir de la lista de carteras del usuario.
     *
     * @param n      - Número de la cartera en la lista de carteras del usuario conectado
     * @param amount - Cantidad a añadir a dicha cartera
     * @return - ResponseEntity indicando al usuario si se ha completado el ingreso o ha surgido un error.
     */
    @Override
    public ResponseEntity addFunds(Integer n, double amount) {
        String outputMsg = "";
        User currentUser = userRepository.findByLogin(getCurrentUserLogin());
        List<Wallet> walletList = walletRepository.findByUserId(currentUser.getId());
        WalletHistory walletHistory = new WalletHistory();

        Wallet selectedWallet = null;

        if (n >= walletList.size() || n < 0) {
            outputMsg = "La cartera seleccionada no existe.";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        } else {
            for (int i = 0; i <= n; i++) {
                selectedWallet = walletList.get(i);
            }
            if (amount <= 0) {
                outputMsg = "La cantidad a añadir debe ser mayor que 0";
                return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
            }
            selectedWallet.setBalance(selectedWallet.getBalance() + amount);
            walletHistory.setWallet(selectedWallet);
            walletHistory.setTransfer_date(new Timestamp(System.currentTimeMillis()));
            walletHistory.setAmount(amount);

            walletHistoryRepository.save(walletHistory);
            walletRepository.save(selectedWallet);
            outputMsg = "Se ha añadido una cantidad de " + amount + " a la cartera con direccion: " + selectedWallet.getAddress();
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.OK);

        }
    }

    /**
     * Método para obtener el balance de una cartera específica que se obtiene a partir de la lista de carteras del usuario.
     *
     * @param n - Número de la cartera en la lista de carteras del usuario conectado
     * @return - ResponseEntity indicando al usuario si se ha completado la operación o ha surgido un error.
     */
    @Override
    public ResponseEntity getBalance(Integer n) {
        String outputMsg = "";

        User currentUser = userRepository.findByLogin(getCurrentUserLogin());
        List<Wallet> walletList = walletRepository.findByUserId(currentUser.getId());

        Wallet selectedWallet = null;

        if (n >= walletList.size() || n < 0) {
            outputMsg = "La cartera seleccionada no existe.";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        } else {
            selectedWallet = walletList.get(n);
            outputMsg = "Saldo de la cartera: " + selectedWallet.getBalance();
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.OK);
        }
    }

    /**
     * Método para obtener los movimientos de una cartera específica que se obtiene a partir de la lista de carteras del usuario.
     *
     * @param n - Número de la cartera en la lista de carteras del usuario conectado
     * @return - ResponseEntity indicando al usuario el balance de movimientos o indicando si ha ocurrido algún error.
     */
    public ResponseEntity getHistory(Integer n) {
        String outputMsg = "";

        User currentUser = userRepository.findByLogin(getCurrentUserLogin());
        List<Wallet> walletList = walletRepository.findByUserId(currentUser.getId());
        Wallet selectedWallet = null;

        if (n >= walletList.size() || n < 0) {
            outputMsg = "La cartera seleccionada no existe.";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        } else {
            selectedWallet = walletList.get(n);

            return new ResponseEntity<>(selectedWallet.getWalletHistoryList(), HttpStatus.OK);
        }
    }

    /**
     * Método para transferir fondos desde una cartera específica,
     * que se obtiene a partir de la lista de carteras del usuario,
     * a otra cartera que se obtiene a través de su dirección.
     *
     * @param n         - Número de la cartera en la lista de carteras del usuario conectado.
     * @param toAddress - Dirección de la cartera a la que se desea realizar la transferencia.
     * @param amount    - Cantidad a transferir.
     * @return ResponseEntity indicando si se ha conseguido completar la operación o indicando si ha ocurrido algún error.
     */
    public ResponseEntity transferToWallet(Integer n, String toAddress, double amount) {
        String outputMsg = "";
        WalletHistory fromWalletHistory = new WalletHistory();
        WalletHistory toWalletHistory = new WalletHistory();

        User currentUser = userRepository.findByLogin(getCurrentUserLogin());
        List<Wallet> walletList = walletRepository.findByUserId(currentUser.getId());
        Wallet selectedWallet = null;
        Wallet toWallet = walletRepository.findByAddress(toAddress);


        if (n >= walletList.size() || n < 0) {
            outputMsg = "La cartera seleccionada no existe.";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        } else {
            selectedWallet = walletList.get(n);

            if (amount <= 0) {
                outputMsg = "La cantidad a transferir debe ser mayor que 0.";
                return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
            }
            if (toWallet == null) {
                outputMsg = "La cartera con direccion " + toAddress + " a la que se quiere realizar la transferencia no existe.";
                return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
            }
            if (selectedWallet.getBalance() - amount < 0) {
                outputMsg = "Error. La cartera no dispone de fondos suficientes.";
                return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
            }
            selectedWallet.setBalance(selectedWallet.getBalance() - amount);
            fromWalletHistory.setWallet(selectedWallet);
            fromWalletHistory.setTransfer_date(new Timestamp(System.currentTimeMillis()));
            fromWalletHistory.setAmount(-amount);

            toWallet.setBalance(toWallet.getBalance() + amount);
            toWalletHistory.setWallet(toWallet);
            toWalletHistory.setTransfer_date(new Timestamp(System.currentTimeMillis()));
            toWalletHistory.setAmount(amount);

            walletRepository.save(selectedWallet);
            walletHistoryRepository.save(fromWalletHistory);

            walletRepository.save(toWallet);
            walletHistoryRepository.save(toWalletHistory);

            outputMsg = "Se ha transferido la cantidad de " + amount + " desde la cartera " +
                    selectedWallet.getAddress() + " a la cartera " + toAddress;
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.OK);

        }
    }
}
