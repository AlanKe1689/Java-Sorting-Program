/**
 * Project: a00967008_assignment02
 * File: CustomerListDialog.java
 * Date: May 6, 2018
 * Time: 6:21:28 PM
 */
package a00967008.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import a00967008.customer.Customer;
import a00967008.database.CustomerDao;
import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author Alan, A00967008
 *
 */
public class CustomerListDialog extends JDialog {
	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 * 
	 * @throws Exception
	 */
	public CustomerListDialog(LinkedList<Customer> customerList, String[] nameList, CustomerDao customerDao) throws Exception {
		setBounds(100, 100, 450, 300);
		setTitle("Customer List");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		{
			JLabel lblSelectACustomer = new JLabel("Select a customer for details.");
			contentPanel.add(lblSelectACustomer, "cell 0 0");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 0 1,grow");
			{
				@SuppressWarnings({ "rawtypes", "unchecked" })
				JList list = new JList(nameList);
				list.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						String selected = list.getSelectedValue().toString();

						for (Customer customer : customerList) {
							if (selected.equalsIgnoreCase(customer.getFirstName() + " " + customer.getLastName())) {
								CustomerDialog customerDialog = new CustomerDialog(customer, customerDao);
								customerDialog.setVisible(true);
							}
						}
					}
				});
				scrollPane.setViewportView(list);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Close");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Close");
				buttonPane.add(cancelButton);
			}
		}
	}
}
