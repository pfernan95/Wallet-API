package com.example.adapter;


import com.example.data.WalletDTO;
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
import java.util.List;

@Service
public class WalletJpaAdapter implements WalletPersistencePort {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Método para crear una nueva cartera para un usuario específico que se identifica a través de un id.
     * @param user_id - Id del usuario que desea crear una nueva cartera
     * @return ResponseEntity indicando al usuario si se ha completado la operación o ha surgido un error.
     */
    @Override
    public ResponseEntity createWallet(Integer user_id){
        String outputMsg = "";
        Wallet newWallet = new Wallet();
        RandomStringUtil randomStringUtil = new RandomStringUtil();

        User user = userRepository.findById(user_id).orElse(null);

        if(user != null) {
            String address = randomStringUtil.getAlphaNumericString(45);
            newWallet.setAddress(address);
            newWallet.setUser(user);
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
                        + newWallet.getUser().getId();
                return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}",HttpStatus.ACCEPTED);
            }
        } else {
            outputMsg = "No se puede dar de alta la cartera ya que el usuario con id: " + user_id + " no existe.";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método para añadir fondos a una cartera específica que se obtiene a través de su id
     * @param id - Id de la cartera a la que se desea añadir fondos
     * @param amount - Cantidad a añadir a dicha cartera
     * @return - ResponseEntity indicando al usuario si se ha completado el ingreso o ha surgido un error.
     */
    @Override
    public ResponseEntity addFunds(Integer id, double amount){
        String outputMsg = "";
        Wallet wallet = walletRepository.findById(id).orElse(null);
        WalletHistory walletHistory = new WalletHistory();

        if (amount <= 0 ){
            outputMsg = "La cantidad a añadir debe ser mayor que 0";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        }
        if (wallet != null) {
            wallet.setBalance(wallet.getBalance() + amount);
            walletHistory.setWallet(wallet);
            walletHistory.setTransfer_date(new Timestamp(System.currentTimeMillis()));
            walletHistory.setAmount(amount);

            walletHistoryRepository.save(walletHistory);
            walletRepository.save(wallet);
            outputMsg = "Se ha añadido una cantidad de " + amount + " a la cartera con direccion: " + wallet.getAddress();
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}",HttpStatus.ACCEPTED);

        } else {
            outputMsg = "No se puede añadir saldo a la cartera con direccion " + wallet.getAddress() + " porque no existe.";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método para obtener el balance de una cartera específica que se obtiene a través de su id
     * @param id - Id de la cartera de la que se desea conocer el balance
     * @return - ResponseEntity indicando al usuario si se ha completado la operación o ha surgido un error.
     */
    @Override
    public ResponseEntity getBalance(Integer id){
        String outputMsg = "";

        Wallet wallet = walletRepository.findById(id).orElse(null);

        if (wallet != null) {
            outputMsg = "Saldo de la cartera: " + wallet.getBalance();
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.ACCEPTED);
        } else {
            outputMsg = "No se ha podido encontrar la cartera con id: " + id;
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método para obtener los movimientos de una cartera específica que se obtiene a través de su id
     * @param id - Id de la cartera de la que se desea conocer el historial de movimientos.
     * @return - ResponseEntity indicando al usuario el balance de movimientos o indicando si ha ocurrido algún error.
     */
    public ResponseEntity getHistory(Integer id){
        String outputMsg = "";

        Wallet wallet = walletRepository.findById(id).orElse(null);

        if (wallet != null) {
            List<WalletHistory> walletHistoryList = walletHistoryRepository.findByWalletId(id);
            return new ResponseEntity<List>(walletHistoryList, HttpStatus.OK);
        }
            outputMsg = "No se ha podido recuperar el historial ya que la cartera no existe.";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
    }

    /**
     * Método para transferir fondos desde una cartera específica, que obtenemos
     * a través de su id, a otra cartera que se obtiene a través de su dirección.
     * @param id - Id de la cartera desde la que se desea realizar la transferencia.
     * @param toAddress - Dirección de la cartera a la que se desea realizar la transferencia.
     * @param amount - Cantidad a transferir.
     * @return ResponseEntity indicando si se ha conseguido completar la operación o indicando si ha ocurrido algún error.
     */
    public ResponseEntity transferToWallet(Integer id, String toAddress, double amount){
        String outputMsg = "";
        WalletHistory fromWalletHistory = new WalletHistory();
        WalletHistory toWalletHistory = new WalletHistory();

        if (amount <= 0 ){
            outputMsg = "La cantidad a transferir debe ser mayor que 0";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        }
        Wallet fromWallet = walletRepository.findById(id).orElse(null);
        Wallet toWallet = walletRepository.findByAddress(toAddress);

        if (fromWallet != null && toWallet != null) {

            if(fromWallet.getBalance() - amount < 0 ){
                outputMsg = "Error. La cartera no dispone de fondos suficientes";
                return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
            }
            fromWallet.setBalance(fromWallet.getBalance() - amount);
            fromWalletHistory.setWallet(fromWallet);
            fromWalletHistory.setTransfer_date(new Timestamp(System.currentTimeMillis()));
            fromWalletHistory.setAmount(-amount);

            toWallet.setBalance(toWallet.getBalance() + amount);
            toWalletHistory.setWallet(toWallet);
            toWalletHistory.setTransfer_date(new Timestamp(System.currentTimeMillis()));
            toWalletHistory.setAmount(amount);

            walletRepository.save(fromWallet);
            walletHistoryRepository.save(fromWalletHistory);

            walletRepository.save(toWallet);
            walletHistoryRepository.save(toWalletHistory);

            outputMsg = "Se ha transferido la cantidad de " + amount + " desde la cartera " +
                    fromWallet.getAddress() + " a la cartera " + toAddress;
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}",HttpStatus.ACCEPTED);

        } else if (fromWallet == null){
            outputMsg = "La cartera desde la que se quiere realizar la transferencia no existe.";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        } else if (toWallet == null){
            outputMsg = "La cartera con direccion " + toAddress + " a la que se quiere realizar la transferencia no existe.";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
