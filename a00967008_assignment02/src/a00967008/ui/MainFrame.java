/**
 * Project: a00967008_assignment02
 * File: MainFrame.java
 * Date: May 6, 2018
 * Time: 6:21:28 PM
 */
package a00967008.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import a00967008.book.Book;
import a00967008.book.util.CompareByAuthors;
import a00967008.customer.Customer;
import a00967008.customer.util.CompareByJoinedDate;
import a00967008.database.BookDao;
import a00967008.database.CustomerDao;
import a00967008.database.PurchaseDao;
import a00967008.purchase.Purchase;
import a00967008.purchase.util.CompareByLastName;
import a00967008.purchase.util.CompareByTitle;
import a00967008.ui.dialog.BookListDialog;
import a00967008.ui.dialog.CustomerListDialog;
import a00967008.ui.dialog.PurchaseListDialog;
import net.miginfocom.swing.MigLayout;

/**
 * @author Alan, A00967008
 *
 */
public class MainFrame extends JFrame {
	private boolean sortByAuthor;
	private boolean sortBookDescending;
	private boolean sortByJoinDate;
	private boolean sortByLastName;
	private boolean sortByTitle;
	private boolean sortPurchaseDescending;
	private String[] printedCustomers;
	private String[] printedBooks;
	private String[] printedPurchases;
	private LinkedList<Customer> customerList;
	private ArrayList<Book> bookList;
	private ArrayList<Purchase> purchaseList;
	private ArrayList<String> priceList;
	private String title;
	private String name;
	private String input;
	private boolean filtered;

	private final int ZERO = 0;
	private final double DOUBLE_ZERO = 0.0;

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainFrame(BookDao bookDao, CustomerDao customerDao, PurchaseDao purchaseDao) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Assignment 2");
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('f');
		menuBar.add(mnFile);

		JMenuItem mntmDrop = new JMenuItem("Drop");
		mntmDrop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int message = JOptionPane.showConfirmDialog(MainFrame.this, "Would you like to delete all input data?", "Drop Tables",
						JOptionPane.YES_NO_OPTION);
				if (message == JOptionPane.YES_OPTION) {
					try {
						customerDao.drop();
						bookDao.drop();
						purchaseDao.drop();
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
					}
					JOptionPane.showMessageDialog(MainFrame.this, "Tables are dropped");
				} else {
					dispose();
				}
			}
		});
		mntmDrop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));
		mnFile.add(mntmDrop);

		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_MASK));
		mnFile.add(mntmQuit);

		JMenu mnBooks = new JMenu("Books");
		mnBooks.setMnemonic('b');
		menuBar.add(mnBooks);

		JMenuItem mntmCount = new JMenuItem("Count");
		mntmCount.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mntmCount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int count = bookDao.countAllBooks();
					JOptionPane.showMessageDialog(MainFrame.this, "There are " + count + " books.", "Book Count", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnBooks.add(mntmCount);

		JCheckBoxMenuItem chckbxmntmByAuthor = new JCheckBoxMenuItem("By Author");
		chckbxmntmByAuthor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				sortByAuthor = chckbxmntmByAuthor.isSelected();
			}
		});
		mnBooks.add(chckbxmntmByAuthor);

		JCheckBoxMenuItem chckbxmntmDescending = new JCheckBoxMenuItem("Descending");
		chckbxmntmDescending.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				sortBookDescending = chckbxmntmDescending.isSelected();
			}
		});
		mnBooks.add(chckbxmntmDescending);

		JMenuItem mntmList = new JMenuItem("List");
		mntmList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sortByAuthor == true) {
					try {
						List<Long> bookId = bookDao.getBookIds();
						bookList = new ArrayList<>();

						for (Long id : bookId) {
							bookList.add(bookDao.getBook(id));
						}

						if (sortBookDescending == true) {
							Collections.sort(bookList, Collections.reverseOrder(new CompareByAuthors()));
						} else {
							Collections.sort(bookList, new CompareByAuthors());
						}

						ArrayList<String> titleList = new ArrayList<>();

						for (Book book : bookList) {
							titleList.add(book.getOriginalTitle() + " by " + book.getAuthors());
						}
						printedBooks = titleList.toArray(new String[titleList.size()]);
						BookListDialog bookListDialog = new BookListDialog(printedBooks);
						bookListDialog.setVisible(true);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					try {
						List<Long> bookId = bookDao.getBookIds();
						bookList = new ArrayList<>();

						for (Long id : bookId) {
							bookList.add(bookDao.getBook(id));
						}

						ArrayList<String> titleList = new ArrayList<>();

						for (Book book : bookList) {
							titleList.add(book.getOriginalTitle() + " by " + book.getAuthors());
						}
						printedBooks = titleList.toArray(new String[titleList.size()]);
						BookListDialog bookListDialog = new BookListDialog(printedBooks);
						bookListDialog.setVisible(true);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		mntmList.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		mnBooks.add(mntmList);

		JMenu mnCustomers = new JMenu("Customers");
		mnCustomers.setMnemonic('c');
		menuBar.add(mnCustomers);

		JMenuItem mntmCount_1 = new JMenuItem("Count");
		mntmCount_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
		mntmCount_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int count = customerDao.countAllCustomers();
					JOptionPane.showMessageDialog(MainFrame.this, "There are " + count + " customers.", "Customer Count",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnCustomers.add(mntmCount_1);

		JCheckBoxMenuItem chckbxmntmByJoinDate = new JCheckBoxMenuItem("By Join Date");
		chckbxmntmByJoinDate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				sortByJoinDate = chckbxmntmByJoinDate.isSelected();
			}
		});
		mnCustomers.add(chckbxmntmByJoinDate);

		JMenuItem mntmList_1 = new JMenuItem("List");
		mntmList_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK));
		mntmList_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sortByJoinDate == true) {
					try {
						List<Long> customerId = customerDao.getCustomerIds();
						customerList = new LinkedList<>();

						for (Long cusId : customerId) {
							customerList.add(customerDao.getCustomer(cusId));
						}

						Collections.sort(customerList, new CompareByJoinedDate());

						ArrayList<String> nameList = new ArrayList<>();

						for (Customer c : customerList) {
							nameList.add(c.getFirstName() + " " + c.getLastName());
						}

						printedCustomers = nameList.toArray(new String[nameList.size()]);
						CustomerListDialog customerListDialog = new CustomerListDialog(customerList, printedCustomers, customerDao);
						customerListDialog.setVisible(true);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					try {
						List<Long> customerId = customerDao.getCustomerIds();

						customerList = new LinkedList<>();

						for (Long cusId : customerId) {
							customerList.add(customerDao.getCustomer(cusId));
						}

						ArrayList<String> nameList = new ArrayList<>();

						for (Customer c : customerList) {
							nameList.add(c.getFirstName() + " " + c.getLastName());
						}

						printedCustomers = nameList.toArray(new String[nameList.size()]);
						CustomerListDialog customerListDialog = new CustomerListDialog(customerList, printedCustomers, customerDao);
						customerListDialog.setVisible(true);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		mnCustomers.add(mntmList_1);

		JMenu mnPurchases = new JMenu("Purchases");
		mnPurchases.setMnemonic('p');
		menuBar.add(mnPurchases);

		JMenuItem mntmTotal = new JMenuItem("Total");
		mntmTotal.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_MASK));
		mntmTotal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (filtered == false) {
						String purchaseCount = String.format("%.2f", purchaseDao.countAllPurchases());
						JOptionPane.showMessageDialog(MainFrame.this, "The total purchases cost $" + purchaseCount + ".", "Purchase Count",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						double count = DOUBLE_ZERO;
						for (String price : priceList) {
							count += Double.parseDouble(price);
						}
						String purchaseCount = String.format("%.2f", count);
						JOptionPane.showMessageDialog(MainFrame.this, "The total purchases cost $" + purchaseCount + ".", "Purchase Count",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnPurchases.add(mntmTotal);

		JCheckBoxMenuItem chckbxmntmByLastName = new JCheckBoxMenuItem("By Last Name");
		chckbxmntmByLastName.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				sortByLastName = chckbxmntmByLastName.isSelected();
			}
		});
		mnPurchases.add(chckbxmntmByLastName);

		JCheckBoxMenuItem chckbxmntmByTitle = new JCheckBoxMenuItem("By Title");
		chckbxmntmByTitle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				sortByTitle = chckbxmntmByTitle.isSelected();
			}
		});
		mnPurchases.add(chckbxmntmByTitle);

		JCheckBoxMenuItem chckbxmntmDescending_1 = new JCheckBoxMenuItem("Descending");
		chckbxmntmDescending_1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				sortPurchaseDescending = chckbxmntmDescending_1.isSelected();
			}
		});
		mnPurchases.add(chckbxmntmDescending_1);

		JMenuItem mntmFilterByCustomer = new JMenuItem("Filter by Customer ID");
		mntmFilterByCustomer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				input = JOptionPane.showInputDialog(MainFrame.this, "Enter customer ID:");
				try {
					if (!input.isEmpty()) {
						List<Long> customerId = customerDao.getCustomerIds();

						customerList = new LinkedList<>();

						for (Long cusId : customerId) {
							customerList.add(customerDao.getCustomer(cusId));
						}

						List<Long> bookId = bookDao.getBookIds();
						bookList = new ArrayList<>();

						for (Long id : bookId) {
							bookList.add(bookDao.getBook(id));
						}

						if (sortByLastName == true) {
							int count = ZERO;
							List<Long> purchaseId = purchaseDao.getPurchaseIds();
							purchaseList = new ArrayList<>();
							priceList = new ArrayList<>();

							for (Long purId : purchaseId) {
								purchaseList.add(purchaseDao.getPurchase(purId));
							}

							if (sortPurchaseDescending == true) {
								Collections.sort(purchaseList, Collections.reverseOrder(new CompareByLastName(customerList)));
							} else {
								Collections.sort(purchaseList, new CompareByLastName(customerList));
							}

							ArrayList<String> buyList = new ArrayList<>();

							for (int i = ZERO; i < purchaseList.size(); i++) {
								if (purchaseList.get(i).getCustomerID().equals(input)) {
									for (int j = ZERO; j < customerList.size(); j++) {

										if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
											name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
										}
									}

									for (int j = ZERO; j < bookList.size(); j++) {
										if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
											title = bookList.get(j).getOriginalTitle();
										}
									}
									priceList.add(purchaseList.get(i).getPrice());
									buyList.add(title + " purchased by " + name);
									count++;
								}
							}

							if (count != ZERO) {
								printedPurchases = buyList.toArray(new String[buyList.size()]);
								PurchaseListDialog purchaseListDialog = new PurchaseListDialog(printedPurchases);
								purchaseListDialog.setVisible(true);
								filtered = true;
							} else {
								JOptionPane.showMessageDialog(MainFrame.this, "Customer ID is not valid.", "Warning", JOptionPane.WARNING_MESSAGE);
							}
						} else if (sortByTitle == true) {
							int count = ZERO;
							List<Long> purchaseId = purchaseDao.getPurchaseIds();
							purchaseList = new ArrayList<>();
							priceList = new ArrayList<>();

							for (Long purId : purchaseId) {
								purchaseList.add(purchaseDao.getPurchase(purId));
							}

							if (sortPurchaseDescending == true) {
								Collections.sort(purchaseList, Collections.reverseOrder(new CompareByTitle(bookList)));
							} else {
								Collections.sort(purchaseList, new CompareByTitle(bookList));
							}

							ArrayList<String> buyList = new ArrayList<>();

							for (int i = ZERO; i < purchaseList.size(); i++) {
								if (purchaseList.get(i).getCustomerID().equals(input)) {
									for (int j = ZERO; j < customerList.size(); j++) {

										if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
											name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
										}
									}

									for (int j = ZERO; j < bookList.size(); j++) {
										if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
											title = bookList.get(j).getOriginalTitle();
										}
									}
									priceList.add(purchaseList.get(i).getPrice());
									buyList.add(title + " purchased by " + name);
									count++;
								}
							}
							if (count != ZERO) {
								printedPurchases = buyList.toArray(new String[buyList.size()]);
								PurchaseListDialog purchaseListDialog = new PurchaseListDialog(printedPurchases);
								purchaseListDialog.setVisible(true);
								filtered = true;
							} else {
								JOptionPane.showMessageDialog(MainFrame.this, "Customer ID is not valid.", "Warning", JOptionPane.WARNING_MESSAGE);
							}
						} else {
							int count = ZERO;
							List<Long> purchaseId = purchaseDao.getPurchaseIds();
							purchaseList = new ArrayList<>();
							priceList = new ArrayList<>();

							for (Long purId : purchaseId) {
								purchaseList.add(purchaseDao.getPurchase(purId));
							}

							ArrayList<String> buyList = new ArrayList<>();

							for (int i = ZERO; i < purchaseList.size(); i++) {
								if (purchaseList.get(i).getCustomerID().equals(input)) {
									for (int j = ZERO; j < customerList.size(); j++) {

										if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
											name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
										}
									}

									for (int j = ZERO; j < bookList.size(); j++) {
										if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
											title = bookList.get(j).getOriginalTitle();
										}
									}
									priceList.add(purchaseList.get(i).getPrice());
									buyList.add(title + " purchased by " + name);
									count++;
								}
							}

							if (count != ZERO) {
								printedPurchases = buyList.toArray(new String[buyList.size()]);
								PurchaseListDialog purchaseListDialog = new PurchaseListDialog(printedPurchases);
								purchaseListDialog.setVisible(true);
								filtered = true;
							} else {
								JOptionPane.showMessageDialog(MainFrame.this, "Customer ID is not valid.", "Warning", JOptionPane.WARNING_MESSAGE);
							}
						}
					} else {
						List<Long> customerId = customerDao.getCustomerIds();

						customerList = new LinkedList<>();

						for (Long cusId : customerId) {
							customerList.add(customerDao.getCustomer(cusId));
						}

						List<Long> bookId = bookDao.getBookIds();
						bookList = new ArrayList<>();

						for (Long id : bookId) {
							bookList.add(bookDao.getBook(id));
						}

						if (sortByLastName == true) {
							List<Long> purchaseId = purchaseDao.getPurchaseIds();
							purchaseList = new ArrayList<>();
							priceList = new ArrayList<>();

							for (Long purId : purchaseId) {
								purchaseList.add(purchaseDao.getPurchase(purId));
							}

							if (sortPurchaseDescending == true) {
								Collections.sort(purchaseList, Collections.reverseOrder(new CompareByLastName(customerList)));
							} else {
								Collections.sort(purchaseList, new CompareByLastName(customerList));
							}

							ArrayList<String> buyList = new ArrayList<>();

							for (int i = ZERO; i < purchaseList.size(); i++) {
								for (int j = ZERO; j < customerList.size(); j++) {
									if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
										name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
									}
								}

								for (int j = ZERO; j < bookList.size(); j++) {
									if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
										title = bookList.get(j).getOriginalTitle();
									}
								}
								priceList.add(purchaseList.get(i).getPrice());
								buyList.add(title + " purchased by " + name);
							}
							printedPurchases = buyList.toArray(new String[buyList.size()]);
							PurchaseListDialog purchaseListDialog = new PurchaseListDialog(printedPurchases);
							purchaseListDialog.setVisible(true);
						} else if (sortByTitle == true) {
							List<Long> purchaseId = purchaseDao.getPurchaseIds();
							purchaseList = new ArrayList<>();
							priceList = new ArrayList<>();

							for (Long id : purchaseId) {
								purchaseList.add(purchaseDao.getPurchase(id));
							}

							if (sortPurchaseDescending == true) {
								Collections.sort(purchaseList, Collections.reverseOrder(new CompareByTitle(bookList)));
							} else {
								Collections.sort(purchaseList, new CompareByTitle(bookList));
							}

							ArrayList<String> buyList = new ArrayList<>();

							for (int i = ZERO; i < purchaseList.size(); i++) {
								for (int j = ZERO; j < customerList.size(); j++) {
									if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
										name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
									}
								}

								for (int j = ZERO; j < bookList.size(); j++) {
									if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
										title = bookList.get(j).getOriginalTitle();
									}
								}
								priceList.add(purchaseList.get(i).getPrice());
								buyList.add(title + " purchased by " + name);
							}
							printedPurchases = buyList.toArray(new String[buyList.size()]);
							PurchaseListDialog purchaseListDialog = new PurchaseListDialog(printedPurchases);
							purchaseListDialog.setVisible(true);
						} else {
							List<Long> purchaseId = purchaseDao.getPurchaseIds();
							purchaseList = new ArrayList<>();
							priceList = new ArrayList<>();

							for (Long purId : purchaseId) {
								purchaseList.add(purchaseDao.getPurchase(purId));
							}

							ArrayList<String> buyList = new ArrayList<>();

							for (int i = ZERO; i < purchaseList.size(); i++) {
								for (int j = ZERO; j < bookList.size(); j++) {
									if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
										title = bookList.get(j).getOriginalTitle();
									}
								}

								for (int j = ZERO; j < customerList.size(); j++) {
									if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
										name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
									}
								}
								priceList.add(purchaseList.get(i).getPrice());
								buyList.add(title + " purchased by " + name);
							}
							printedPurchases = buyList.toArray(new String[buyList.size()]);
							PurchaseListDialog purchaseListDialog = new PurchaseListDialog(printedPurchases);
							purchaseListDialog.setVisible(true);
						}
						JOptionPane.showMessageDialog(MainFrame.this, "Filter is removed.");
						filtered = false;
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnPurchases.add(mntmFilterByCustomer);

		JMenuItem mntmList_2 = new JMenuItem("List");
		mntmList_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (filtered == false) {
						List<Long> customerId = customerDao.getCustomerIds();

						customerList = new LinkedList<>();

						for (Long cusId : customerId) {
							customerList.add(customerDao.getCustomer(cusId));
						}

						List<Long> bookId = bookDao.getBookIds();
						bookList = new ArrayList<>();

						for (Long id : bookId) {
							bookList.add(bookDao.getBook(id));
						}

						if (sortByLastName == true) {
							List<Long> purchaseId = purchaseDao.getPurchaseIds();
							purchaseList = new ArrayList<>();
							priceList = new ArrayList<>();

							for (Long purId : purchaseId) {
								purchaseList.add(purchaseDao.getPurchase(purId));
							}

							if (sortPurchaseDescending == true) {
								Collections.sort(purchaseList, Collections.reverseOrder(new CompareByLastName(customerList)));
							} else {
								Collections.sort(purchaseList, new CompareByLastName(customerList));
							}

							ArrayList<String> buyList = new ArrayList<>();

							for (int i = ZERO; i < purchaseList.size(); i++) {
								for (int j = ZERO; j < customerList.size(); j++) {
									if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
										name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
									}
								}

								for (int j = ZERO; j < bookList.size(); j++) {
									if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
										title = bookList.get(j).getOriginalTitle();
									}
								}
								priceList.add(purchaseList.get(i).getPrice());
								buyList.add(title + " purchased by " + name);
							}
							printedPurchases = buyList.toArray(new String[buyList.size()]);
							PurchaseListDialog purchaseListDialog = new PurchaseListDialog(printedPurchases);
							purchaseListDialog.setVisible(true);
						} else if (sortByTitle == true) {
							List<Long> purchaseId = purchaseDao.getPurchaseIds();
							purchaseList = new ArrayList<>();
							priceList = new ArrayList<>();

							for (Long id : purchaseId) {
								purchaseList.add(purchaseDao.getPurchase(id));
							}

							if (sortPurchaseDescending == true) {
								Collections.sort(purchaseList, Collections.reverseOrder(new CompareByTitle(bookList)));
							} else {
								Collections.sort(purchaseList, new CompareByTitle(bookList));
							}

							ArrayList<String> buyList = new ArrayList<>();

							for (int i = ZERO; i < purchaseList.size(); i++) {
								for (int j = ZERO; j < customerList.size(); j++) {
									if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
										name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
									}
								}

								for (int j = ZERO; j < bookList.size(); j++) {
									if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
										title = bookList.get(j).getOriginalTitle();
									}
								}
								priceList.add(purchaseList.get(i).getPrice());
								buyList.add(title + " purchased by " + name);
							}
							printedPurchases = buyList.toArray(new String[buyList.size()]);
							PurchaseListDialog purchaseListDialog = new PurchaseListDialog(printedPurchases);
							purchaseListDialog.setVisible(true);
						} else {
							List<Long> purchaseId = purchaseDao.getPurchaseIds();
							purchaseList = new ArrayList<>();
							priceList = new ArrayList<>();

							for (Long purId : purchaseId) {
								purchaseList.add(purchaseDao.getPurchase(purId));
							}

							ArrayList<String> buyList = new ArrayList<>();

							for (int i = ZERO; i < purchaseList.size(); i++) {
								for (int j = ZERO; j < bookList.size(); j++) {
									if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
										title = bookList.get(j).getOriginalTitle();
									}
								}

								for (int j = ZERO; j < customerList.size(); j++) {
									if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
										name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
									}
								}
								priceList.add(purchaseList.get(i).getPrice());
								buyList.add(title + " purchased by " + name);
							}
							printedPurchases = buyList.toArray(new String[buyList.size()]);
							PurchaseListDialog purchaseListDialog = new PurchaseListDialog(printedPurchases);
							purchaseListDialog.setVisible(true);
						}
					} else {
						if (sortByTitle == true) {
							List<Long> purchaseId = purchaseDao.getPurchaseIds();
							purchaseList = new ArrayList<>();
							priceList = new ArrayList<>();

							for (Long purId : purchaseId) {
								purchaseList.add(purchaseDao.getPurchase(purId));
							}

							if (sortPurchaseDescending == true) {
								Collections.sort(purchaseList, Collections.reverseOrder(new CompareByTitle(bookList)));
							} else {
								Collections.sort(purchaseList, new CompareByTitle(bookList));
							}

							ArrayList<String> buyList = new ArrayList<>();

							for (int i = ZERO; i < purchaseList.size(); i++) {
								if (purchaseList.get(i).getCustomerID().equals(input)) {
									for (int j = ZERO; j < customerList.size(); j++) {

										if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
											name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
										}
									}

									for (int j = ZERO; j < bookList.size(); j++) {
										if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
											title = bookList.get(j).getOriginalTitle();
										}
									}
									priceList.add(purchaseList.get(i).getPrice());
									buyList.add(title + " purchased by " + name);
								}
							}
							printedPurchases = buyList.toArray(new String[buyList.size()]);
							PurchaseListDialog purchaseListDialog = new PurchaseListDialog(printedPurchases);
							purchaseListDialog.setVisible(true);
						} else {
							PurchaseListDialog purchaseListDialog = new PurchaseListDialog(printedPurchases);
							purchaseListDialog.setVisible(true);
						}
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mntmList_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.SHIFT_MASK));
		mnPurchases.add(mntmList_2);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('h');
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(MainFrame.this, "Assignment 2\nBy Alan A00967008", "About Assignment 2",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[]", "[]"));
	}
}
