package com.unicyb.energytaxi.services.documents;

import com.unicyb.energytaxi.database.dao.documents.PricePerKilometersByTariffDAOImpl;
import com.unicyb.energytaxi.entities.documents.PricePerKilometersByTariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PricePerKilometersByTariffService {

    private final PricePerKilometersByTariffDAOImpl pricePerKilometersByTariffDAO;
    @Autowired
    public PricePerKilometersByTariffService(PricePerKilometersByTariffDAOImpl pricePerKilometersByTariffDAO) {
        this.pricePerKilometersByTariffDAO = pricePerKilometersByTariffDAO;
    }

    public boolean add(PricePerKilometersByTariff pricePerKilometersByTariff) {
        return pricePerKilometersByTariffDAO.add(pricePerKilometersByTariff);
    }

    public List<PricePerKilometersByTariff> getAll() {
        return pricePerKilometersByTariffDAO.getAll();
    }

    public boolean update(PricePerKilometersByTariff pricePerKilometersByTariff) {
        return pricePerKilometersByTariffDAO.update(pricePerKilometersByTariff);
    }

    public boolean delete(int ID) {
       return pricePerKilometersByTariffDAO.delete(ID);
    }
}
