package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import gui.listeners.DataChangeListener;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Seller entity;
	
	private SellerService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtID;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private Label lblErrorName;
	
	@FXML
	private Label lblErrorEmail;
	
	@FXML
	private Label lblErrorBirthDate;
	
	@FXML
	private Label lblErrorBaseSalary;
	
	@FXML
	private Button btnSave;
	
	@FXML
	private Button btnCancel;
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null!");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null!");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}
	private void notifyDataChangeListeners() {
		for(DataChangeListener l : dataChangeListeners) {
			l.onDataChanged();
		}
	}
	private Seller getFormData() {
		ValidationException exception = new ValidationException("Validation error.");
		Seller dep = new Seller();
		dep.setId(gui.util.Utils.tryParseToInt(txtID.getText()));
		if(txtName.getText() == null || txtName.getText().trim() == null) {
			exception.addError("Name", "Field can't be empty!");
		}
		dep.setName(txtName.getText());
		if(exception.getErrors().size() > 0) throw exception;
		
		return dep;
	}
	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtID);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}
	
	public void setSeller(Seller d) {
		entity = d;	
	}
	
	public void setSellerService(SellerService s) {
		service = s;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null.");
		}
		txtID.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f",entity.getBaseSalary()));
		if(entity.getBirthDate()!= null)
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		if(errors.containsKey("Name"))
			lblErrorName.setText(errors.get("Name"));
	}
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	
}
