package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	private DepartmentDao depDAO = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		return depDAO.findAll();
	}
	
	public void saveOrUpdate(Department d) {
		if(d.getId() == null) {
			depDAO.insert(d);
		}
		else {
			depDAO.update(d);
		}
	}
	
	public void Remove(Department d) {
		depDAO.deleteById(d.getId());
	}
}
