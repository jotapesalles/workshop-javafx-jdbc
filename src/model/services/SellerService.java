package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	private SellerDao depDAO = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
		return depDAO.findAll();
	}
	
	public void saveOrUpdate(Seller d) {
		if(d.getId() == null) {
			depDAO.insert(d);
		}
		else {
			depDAO.update(d);
		}
	}
	
	public void Remove(Seller d) {
		depDAO.deleteById(d.getId());
	}
}
