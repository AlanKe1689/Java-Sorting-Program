/**
 * Project: a00967008_assignment02
 * File: CustomerDialog.java
 * Date: May 6, 2018
 * Time: 6:21:28 PM
 */
package a00967008.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import a00967008.customer.Customer;
import a00967008.database.CustomerDao;
import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author Alan, A00967008
 *
 */
public class CustomerDialog extends JDialog {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;

	/**
	 * Create the dialog.
	 */
	public CustomerDialog(Customer customer, CustomerDao dao) {
		setBounds(100, 100, 500, 400);
		setTitle("Customer");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][]"));
		{
			JLabel lblId = new JLabel("ID");
			contentPanel.add(lblId, "cell 0 0,alignx trailing");
		}
		{
			textField = new JTextField();
			contentPanel.add(textField, "cell 1 0,growx");
			textField.setColumns(10);
		}
		{
			JLabel lblFirstName = new JLabel("First Name");
			contentPanel.add(lblFirstName, "cell 0 1,alignx trailing");
		}
		{
			textField_1 = new JTextField();
			contentPanel.add(textField_1, "cell 1 1,growx");
			textField_1.setColumns(10);
		}
		{
			JLabel lblLastName = new JLabel("Last Name");
			contentPanel.add(lblLastName, "cell 0 2,alignx trailing");
		}
		{
			textField_2 = new JTextField();
			contentPanel.add(textField_2, "cell 1 2,growx");
			textField_2.setColumns(10);
		}
		{
			JLabel lblStreet = new JLabel("Street");
			contentPanel.add(lblStreet, "cell 0 3,alignx trailing");
		}
		{
			textField_3 = new JTextField();
			contentPanel.add(textField_3, "cell 1 3,growx");
			textField_3.setColumns(10);
		}
		{
			JLabel lblCity = new JLabel("City");
			contentPanel.add(lblCity, "cell 0 4,alignx trailing");
		}
		{
			textField_4 = new JTextField();
			contentPanel.add(textField_4, "cell 1 4,growx");
			textField_4.setColumns(10);
		}
		{
			JLabel lblPostalCode = new JLabel("Postal Code");
			contentPanel.add(lblPostalCode, "cell 0 5,alignx trailing");
		}
		{
			textField_5 = new JTextField();
			contentPanel.add(textField_5, "cell 1 5,growx");
			textField_5.setColumns(10);
		}
		{
			JLabel lblPhone = new JLabel("Phone");
			contentPanel.add(lblPhone, "cell 0 6,alignx trailing");
		}
		{
			textField_6 = new JTextField();
			contentPanel.add(textField_6, "cell 1 6,growx");
			textField_6.setColumns(10);
		}
		{
			JLabel lblEmail = new JLabel("Email");
			contentPanel.add(lblEmail, "cell 0 7,alignx trailing");
		}
		{
			textField_7 = new JTextField();
			contentPanel.add(textField_7, "cell 1 7,growx");
			textField_7.setColumns(10);
		}
		{
			JLabel lblJoinedDate = new JLabel("Joined Date");
			contentPanel.add(lblJoinedDate, "cell 0 8,alignx trailing");
		}
		{
			textField_8 = new JTextField();
			contentPanel.add(textField_8, "cell 1 8,growx");
			textField_8.setColumns(10);
		}
		{
			textField.setText(customer.getCustomerID());
			textField.setEditable(false);
			textField.setEnabled(false);

			textField_1.setText(customer.getFirstName());
			textField_2.setText(customer.getLastName());
			textField_3.setText(customer.getStreetName());
			textField_4.setText(customer.getCity());
			textField_5.setText(customer.getPostalCode());
			textField_6.setText(customer.getPhone());
			textField_7.setText(customer.getEmailAddress());

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			textField_8.setText(customer.getJoinDate().format(formatter));
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
							Customer customer = new Customer.Builder(textField.getText(), textField_6.getText()).setFirstName(textField_1.getText())
									.setLastName(textField_2.getText()).setStreetName(textField_3.getText()).setCity(textField_4.getText())
									.setPostalCode(textField_5.getText()).setEmailAddress(textField_7.getText())
									.setJoinDate(LocalDate.parse(textField_8.getText(), formatter)).build();

							dao.update(customer);
						} catch (SQLException e1) {
							System.out.println(e1.getMessage());
						} finally {
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
