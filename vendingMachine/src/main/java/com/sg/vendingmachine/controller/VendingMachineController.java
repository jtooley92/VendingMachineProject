/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.controller;

import com.sg.vendingmachine.dao.InsufficientFundsException;
import com.sg.vendingmachine.dao.NoItemInventoryException;
import com.sg.vendingmachine.dao.VendingMachineDAO;
import com.sg.vendingmachine.dao.VendingMachineDAOException;
import com.sg.vendingmachine.dao.VendingMachineDAOFileImpl;
import com.sg.vendingmachine.dto.Snack;
import com.sg.vendingmachine.service.VendingMachineService;
import com.sg.vendingmachine.ui.UserIO;
import com.sg.vendingmachine.ui.UserIOConsoleImpl;
import com.sg.vendingmachine.ui.VendingMachineView;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jtooleyful
 */
@Component
public class VendingMachineController {

    @Autowired
    public VendingMachineController(VendingMachineView view, VendingMachineService service) {
        this.service = service;
        this.view = view;
    }

    private VendingMachineService service;
    private VendingMachineView view;

    public void run() throws VendingMachineDAOException {
        boolean keepGoing = true;
        int menuSelection = 0;
        try{
        listSnacks();
        getCalculations();
        exitMessage();
    } catch (NoItemInventoryException | InsufficientFundsException e){
            System.out.println(e.getMessage());
    }
    }

    private void listSnacks() throws VendingMachineDAOException {
        view.displayAllSnacksBanner();
        List<Snack> snackList = service.getAllSnacks();
        view.displaySnackList(snackList);
    }

    private String userSnack() throws VendingMachineDAOException {

        return view.getUserSnack();
    }

    private String userCash() throws VendingMachineDAOException {

        return view.getUserCash();
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }

    private void getCalculations() throws VendingMachineDAOException, NoItemInventoryException, InsufficientFundsException {
        String snackName = userSnack();
        String cash = userCash();
       Snack snack = service.getSnack(snackName);
       String price = snack.getPrice().toString();
       BigDecimal changeCalculation = service.changeCalculion(cash, price);
        System.out.println(changeCalculation);
       List coinCalculation = service.coinCalculation(changeCalculation);
       view.coinChange(coinCalculation);
       service.removeSnack(snack);
    }
    
}
