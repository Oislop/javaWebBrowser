package webBrowser;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class WebBrowser {
	private Display frame;
	private Shell shell_dis;

	private Button buttonAddPage; // 增加页面按钮
	private Button buttonGoto; // 转到按钮
	private Button buttonSearch; // 搜索按钮
	private Button buttonForward; // 前进按钮
	private Button buttonBack; // 后退按钮
	private Button buttonRefresh; // 刷新按钮
	private Button buttonHomepage; // 主页按钮
	private Button buttonFav; // 收藏夹按钮
	private Button buttondownload; // 下载
	private Button buttonhistory; // 历史记录
	private Button buttonemail; // email
	private Combo combo_address;// 地址栏
	private Text Text_search;// 搜索栏
	private Label labelstatus; // 状态栏

	private Label labelFavoriteName; // 收藏书签名
	private Label labelFavoriteUrl; // 收藏书签地址
	private Text textFavoriteName; // 收藏书签名
	private Text textFavoriteUrl; // 收藏书签地址

	private Composite compositeFavorite;
	private List listbookmark; // 收藏夹列表
	private Composite compositeHistory;
	private List listhistory;// 历史纪录

	CTabItem tabItemPage;

	int i = 0;

	private FormData fDataL;

	boolean boolTClose = false; // 页面标签是否有关闭按钮
	boolean boolConn = false; // 测试地址是否可用辅助变量
	boolean boolAddTextGFouce = false;
	boolean boolhistory = false;

	int connTestCount = 0; // 连接测试次数
	CTabFolder tabFolderPages; // 多标签窗口面板

	BOOK_MARK book_Mark = new BOOK_MARK(); // 收藏夹文件存取操作类
	history history_list = new history();// 历史纪录文件存储类

	GridData dataB = new GridData();
	GridData dataC = new GridData();
	GridLayout gridLayout;
	Composite compositePages;
	Composite compositeGuideNAddress;

	// 搜索引擎枚举
	public enum SearchEngine {
		NULL, GOOGLE, SOUGOU, BAIDU, BING
	}

	String stringpage = "about:blank";
	// String stringHomepage = "about:blank"; // 主页地址
	String stringHomepage = "https://www.baidu.com/";
	SearchEngine serachEngine = SearchEngine.SOUGOU; // 搜索引擎

	public static void main(String[] args) {
		File file = new File("D:\\Test\\history");
		File file1 = new File("D:\\Test\\bookmark");
		if(!file.exists()) {
			file.mkdirs();
		}

		if(!file1.exists()) {
			file1.mkdirs();
		}

		WebBrowser browser = new WebBrowser();
		browser.createFrame();
		browser.createTool();
		browser.creatButton();
		browser.createBrowser();
		browser.shell_dis.open();
		while (!browser.shell_dis.isDisposed()) {
			if (!browser.frame.readAndDispatch())
				browser.frame.sleep();
		}
		browser.frame.dispose();

	}

	private void createFrame() {
		frame = new Display(); // 屏幕
		shell_dis = new Shell(frame); // 窗口
		shell_dis.setText("Browser"); // 设置浏览器标题
		shell_dis.setSize(1400, 1100); // 设置浏览器窗口大小
		gridLayout = new GridLayout(); // 布局方式
		gridLayout.numColumns = 1; // 设置shellAll每行只摆放一个控件
		shell_dis.setLayout(gridLayout); // 设置布局
	}

	private void creatButton() {

		back();
		pre();
		fre();
		goHome();
	}

	public void createBrowser() {
		setPage();
		bookMark();
		historyFrame();
		addnewPage();
		bottomStu();
		setTextAddress();
		goTo();
		setTextSearch();
		find();
		download();
		bookMarkButton();
		historyButton();
		book_Mark.saveUrl(stringpage, stringpage, false);
		listbookmark.add(stringpage);
		eamil();
		tabFolder();
		addPage(stringHomepage, null);

	}

	/**
	 * TODO 历史记录按钮实现
	 */
	private void historyButton() {

		buttonhistory = new Button(compositeGuideNAddress, SWT.NONE);
		buttonhistory.setLayoutData(new GridData(100, SWT.DEFAULT));
		buttonhistory.setText("历史纪录");
		buttonhistory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				compositeHistory.setVisible(true);
				listhistory.setFocus();
			}
		});
	}

	/**
	 * TODO 历史纪录实现
	 */
	private void historyFrame() {
		compositeHistory = new Composite(compositePages, SWT.BORDER);
		compositeHistory.setVisible(false); // 默认不显示
		FormData fDataC = new FormData(); // 外布局
		fDataC.top = new FormAttachment(2, 1000, 0);
		fDataC.right = new FormAttachment(999, 1000, 0);
		fDataC.height = 400;
		fDataC.width = 240;
		compositeHistory.setLayoutData(fDataC); // 设置外布局
		gridLayout = new GridLayout(); // 内布局
		gridLayout.numColumns = 1; // 每行放一个控件
		compositeHistory.setLayout(gridLayout);

		Menu menuFav = new Menu(compositeHistory); // 右键菜单
		MenuItem del = new MenuItem(menuFav, SWT.NONE);
		del.setText("删除");
		del.addSelectionListener(new SelectionAdapter() { // 删除
			public void widgetSelected(SelectionEvent e) {
				int i = JOptionPane.showConfirmDialog(null, "是否删除", "提示:", JOptionPane.YES_NO_OPTION);
				if (i == JOptionPane.OK_OPTION) {
					history_list.delUrl(listhistory.getItem(listhistory.getFocusIndex()));
					listhistory.remove(listhistory.getSelectionIndex());
				}
				return;
			}

		});

		final String[] s = history_list.getFavorite(); // 获取所有记录
		listhistory = new List(compositeHistory, SWT.V_SCROLL); // 列表控件
		GridData rdl = new GridData(); // 外布局
		rdl.heightHint = 350;
		rdl.widthHint = 210;
		listhistory.setLayoutData(rdl);
		listhistory.setMenu(menuFav); // 设置右键菜单

		for (int i = 0; i < s.length; i++) {// 把所有添加进列表
			listhistory.add(s[i]);
		}

		listhistory.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {

				compositeHistory.setVisible(false);
			}

		});
		listhistory.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.button == 1) {// 单击鼠标左键
					GC gc = new GC(listhistory);

					int count = (e.y / gc.textExtent("Aj").y) + (listhistory.getVerticalBar().getSelection()); // 获取鼠标单击位置
					gc.dispose();
					if (count < listhistory.getItemCount()) {// 如果鼠标点中列表中的项目，在新窗口打开网页
						addPage(history_list.getUrl(listhistory.getItem(listhistory.getFocusIndex())), null);
						compositeHistory.setVisible(false); // 隐藏历史记录
					}
				} else {// 单击鼠标右键
					listhistory.setFocus();
					GC gc = new GC(listhistory);
					int count = (e.y / gc.textExtent("jj").y) + (listhistory.getVerticalBar().getSelection()); // 获取氥单击位置
					gc.dispose();
					if (count < listhistory.getItemCount())
						listhistory.select(count); // 如果鼠标单击列表中的项目，则选中项目。（默认单击右键不会选中）
				}
			}
		});

	}

	private void tabFolder() {
		tabFolderPages = new CTabFolder(compositePages, SWT.NONE);
		tabFolderPages.setSimple(false); // 样式
		FormData dataTF = new FormData();
		dataTF.top = new FormAttachment(0); // 四边贴在上层控件
		dataTF.left = new FormAttachment(0);
		dataTF.bottom = new FormAttachment(100);
		dataTF.right = new FormAttachment(100);
		tabFolderPages.setLayoutData(dataTF);
		tabFolderPages.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {// 双击关闭窗口
				// if (tabFolderPages.getItemCount() > 1) // 如果标签大于１个则关闭被选中的标签，只有一个时不关闭
				tabFolderPages.getItem(tabFolderPages.getSelectionIndex()).dispose();
				combo_address.setText(getSelectedBrowser().getUrl()); // 原地址页面已关闭，把地址栏设置为当前的页面地址
			}
		});
		tabFolderPages.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {// 单击某标签后把按钮的状态和地址栏设置成当前页面对应的按钮状态和地址
				combo_address.setText(getSelectedBrowser().getUrl());
			}

			public void mouseUp(MouseEvent e) {// 让页面获取焦点（此处解决切换标签后不用手动单击页面获取焦点即可用鼠标滚轮滚动页面）
				getSelectedBrowser().forceFocus();
			}
		});

		tabFolderPages.addMouseListener(new MouseAdapter() {

			// Browser browser = new Browser(tabFolderPages, SWT.MULTI);
			// CTabItem tabItemPage = null;
			// CTabItem tabItemPage = new CTabItem(tabFolderPages, SWT.BORDER);
			// tabItemPage.setShowClose(true);

			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 3) {// 右键
					Menu menu = new Menu(shell_dis, SWT.POP_UP);
					tabFolderPages.setMenu(menu);
					MenuItem itemClose = new MenuItem(menu, SWT.NONE);
					itemClose.setText("关闭当前标签");
					itemClose.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							if (tabFolderPages.getItemCount() != 1) {// 不是只存在一个标签的情况下
								tabFolderPages.getItem(tabFolderPages.getSelectionIndex()).dispose();
								combo_address.setText(getSelectedBrowser().getUrl());
							} else {// 只有一个标签
								shell_dis.dispose();
							}
						}
					});
					MenuItem menuItem_itemCloseAll = new MenuItem(menu, SWT.NONE);
					menuItem_itemCloseAll.setText("关闭所有标签");
					menuItem_itemCloseAll.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							shell_dis.dispose();
						}
					});
				}
			}
		});

	}

	private void createTool() {
		compositeGuideNAddress = new Composite(shell_dis, SWT.NONE); // 地址和工具面板
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_composite.heightHint = 40;// 高度和宽度
		gd_composite.widthHint = 549;
		compositeGuideNAddress.setLayoutData(gd_composite);
		GridLayout fl_composite = new GridLayout();
		fl_composite.numColumns = 13;
		compositeGuideNAddress.setLayout(fl_composite);
	}

	/**
	 * TODO 下载按钮实现
	 */
	private void download() {
		buttondownload = new Button(compositeGuideNAddress, SWT.NONE);// 设置布局
		buttondownload.setLayoutData(new GridData(50, SWT.DEFAULT));
		buttondownload.setText("下载");// 设置形状
		buttondownload.addSelectionListener(new SelectionAdapter() {// 设置监听
			public void widgetSelected(SelectionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						new downloadButton();// 调用下载函数
					}
				}).start();
			}
		});
	}

	private void eamil() {
		buttonemail = new Button(compositeGuideNAddress, SWT.NONE);
		buttonemail.setLayoutData(new GridData(70, SWT.DEFAULT));
		buttonemail.setText("email");
		buttonemail.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						new Email();
					}
				}).start();
			}
		});
	}

	private void bookMarkButton() {

		buttonFav = new Button(compositeGuideNAddress, SWT.NONE);
		buttonFav.setLayoutData(new GridData(70, SWT.DEFAULT));
		buttonFav.setText("收藏夹");
		buttonFav.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				compositeFavorite.setVisible(true);
				listbookmark.setFocus();
			}
		});
	}

	/**
	 * TODO 地址栏实现
	 */
	private void setTextAddress() {
		combo_address = new Combo(compositeGuideNAddress, SWT.BORDER);
		GridData gd_combo = new GridData(GridData.FILL_HORIZONTAL);// 在窗口变化时，自动扩展水平方向的大小
		combo_address.setLayoutData(gd_combo);
		combo_address.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {// 添加监听
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {// 添加键盘监听
				if (e.character == SWT.CR) {
					e.doit = false;
					gotoUrl(combo_address.getText());// 调用url跳转函数
				}
			}
		});
	}

	/**
	 * TODO 搜索栏实现
	 */
	private void setTextSearch() {
		Text_search = new Text(compositeGuideNAddress, SWT.BORDER);
		GridData gd_combo = new GridData(-1, GridData.FILL_HORIZONTAL);// 在窗口变化时，自动扩展水平方向的大小
		gd_combo.widthHint = 150;// 起始宽度
		gd_combo.heightHint = 26;
		gd_combo.minimumWidth = 100;// 设置最小宽度
		Text_search.setLayoutData(gd_combo);
	}

	/**
	 * TODO 搜索按钮实现
	 */
	private void find() {
		buttonSearch = new Button(compositeGuideNAddress, SWT.NONE);// 设置布局
		buttonSearch.setText("Find");// 设置形状
		buttonSearch.setToolTipText("右键更改搜索引擎");// 设置鼠标提示
		buttonSearch.setLayoutData(new GridData(70, SWT.DEFAULT));
		buttonSearch.addSelectionListener(new SelectionAdapter() {// 设置监听
			public void widgetSelected(SelectionEvent e) {
				gotoSearch(Text_search.getText());
			}

		});

		buttonSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 3) {// 右键
					Menu menu_itemRightMouse = new Menu(shell_dis, SWT.POP_UP);
					buttonSearch.setMenu(menu_itemRightMouse);
					MenuItem menuItem_item1 = new MenuItem(menu_itemRightMouse, SWT.NONE);

					menuItem_item1.setText("百度");
					menuItem_item1.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							serachEngine = SearchEngine.BAIDU; // 更改搜索引擎
						}
					});

					MenuItem menuItem_item2 = new MenuItem(menu_itemRightMouse, SWT.NONE);
					menuItem_item2.setText("谷歌");
					menuItem_item2.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							serachEngine = SearchEngine.GOOGLE;
						}
					});
					MenuItem menuItem_item3 = new MenuItem(menu_itemRightMouse, SWT.NONE);
					menuItem_item3.setText("必应");
					menuItem_item3.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							serachEngine = SearchEngine.BING;
						}
					});
					MenuItem menuItem_item4 = new MenuItem(menu_itemRightMouse, SWT.NONE);
					menuItem_item4.setText("搜狗");
					menuItem_item4.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							serachEngine = SearchEngine.SOUGOU;
						}
					});

				}
			}
		});

	}

	/**
	 * TODO 跳转按钮实现
	 */
	private void goTo() {
		buttonGoto = new Button(compositeGuideNAddress, SWT.NONE);// 设置布局
		buttonGoto.setText("GO");// 设置形状
		buttonGoto.setLayoutData(new GridData(70, SWT.DEFAULT));
		buttonGoto.addSelectionListener(new SelectionAdapter() {// 添加监听
			public void widgetSelected(SelectionEvent e) {
				addHistory();// 添加历史记录
				gotoUrl(combo_address.getText());// 跳转到输入的url
			}

		});
	}

	/**
	 * TODO 主页按钮实现
	 */
	private void goHome() {
		buttonHomepage = new Button(compositeGuideNAddress, SWT.NONE);// 设置布局
		buttonHomepage.setText("HOME");// 设置形状
		buttonHomepage.setLayoutData(new GridData(70, SWT.DEFAULT));
		buttonHomepage.addSelectionListener(new SelectionAdapter() {// 添加监听
			public void widgetSelected(SelectionEvent e) {
				getSelectedBrowser().setUrl(stringHomepage);// 跳转到主页
				addHistory();// 添加历史记录
			}
		});
	}

	/**
	 * TODO 状态栏实现
	 */
	private void bottomStu() {
		labelstatus = new Label(compositePages, SWT.FILL);
		labelstatus.setText("Browser");
		fDataL = new FormData(); // 外布局
		fDataL.bottom = new FormAttachment(100, 7); // 控件与底部的边对齐于上层面板的5/100的位置
		fDataL.height = 33; // 控件高
		labelstatus.setLayoutData(fDataL);
	}

	/**
	 * TODO 刷新按钮实现
	 */
	private void fre() {
		buttonRefresh = new Button(compositeGuideNAddress, SWT.NONE);// 设置布局
		buttonRefresh.setLayoutData(new GridData(70, SWT.DEFAULT));
		buttonRefresh.setText("F5刷新");// 设置形状
		buttonRefresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {// 添加监听
				// 获取当前浏览器事件
				Browser b = (Browser) tabFolderPages.getItem(tabFolderPages.getSelectionIndex()).getControl();
				b.refresh();// 刷新事件
				addHistory();// 添加历史记录
			}
		});
	}

	/**
	 * TODO 前进按钮实现
	 */
	private void pre() {
		buttonForward = new Button(compositeGuideNAddress, SWT.NONE);// 设置布局
		buttonForward.setLayoutData(new GridData(70, SWT.DEFAULT));
		buttonForward.setText("->");// 设置形状
		buttonForward.addSelectionListener(new SelectionAdapter() {// 添加监听
			public void widgetSelected(SelectionEvent e) {
				addHistory();// 添加历史记录
				getSelectedBrowser().forward();// 前进事件
			}
		});
	}

	/**
	 * TODO 后退按钮实现
	 */
	private void back() {
		buttonBack = new Button(compositeGuideNAddress, SWT.None);// 设置布局
		buttonBack.setLayoutData(new GridData(70, SWT.DEFAULT));
		buttonBack.setText("<-");// 设置形状
		buttonBack.addSelectionListener(new SelectionAdapter() {// 添加监听
			public void widgetSelected(SelectionEvent e) {
				addHistory();// 添加历史记录
				getSelectedBrowser().back();// 后退事件
			}
		});

	}

	private void addnewPage() {
		buttonAddPage = new Button(compositePages, SWT.NONE);
		FormData fDataB = new FormData(); // 按钮位置（外布局）
		fDataB.right = new FormAttachment(999, 1000, 0);
		fDataB.height = 23;
		fDataB.width = 24;
		buttonAddPage.setText("+");
		buttonAddPage.setLayoutData(fDataB);
		buttonAddPage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addPage(stringHomepage, null);
			}
		});

	}

	private void setPage() {
		compositePages = new Composite(shell_dis, SWT.BORDER); // 放置页面的面板
		FormLayout formLayout = new FormLayout(); // 面板内布局
		compositePages.setLayout(formLayout);
		dataC = new GridData(GridData.FILL_BOTH); // 面板外布局
		compositePages.setLayoutData(dataC);
	}

	/**
	 * TODO 书签实现
	 */
	private void bookMark() {
		compositeFavorite = new Composite(compositePages, SWT.BORDER);
		compositeFavorite.setVisible(false); // 默认不显示
		FormData fDataC = new FormData(); // 外布局
		fDataC.top = new FormAttachment(2, 1000, 0);
		fDataC.right = new FormAttachment(999, 1000, 0);
		fDataC.height = 400;
		fDataC.width = 240;
		compositeFavorite.setLayoutData(fDataC); // 设置外布局
		gridLayout = new GridLayout(); // 内布局
		gridLayout.numColumns = 1; // 每行放一个控件
		compositeFavorite.setLayout(gridLayout);

		Menu menuFav = new Menu(compositeFavorite); // 收藏夹右键菜单
		MenuItem add = new MenuItem(menuFav, SWT.NONE); // 右键菜单项目
		MenuItem del = new MenuItem(menuFav, SWT.NONE);
		add.setText("添加");
		del.setText("删除");
		add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				saveBookmark(tabFolderPages.getItem(tabFolderPages.getSelectionIndex()).getToolTipText(),
						combo_address.getText(), true);
			}

		});
		del.addSelectionListener(new SelectionAdapter() { // 删除书签

			public void widgetSelected(SelectionEvent e) {
				int i = JOptionPane.showConfirmDialog(null, "是否删除", "提示:", JOptionPane.YES_NO_OPTION);
				if (i == JOptionPane.OK_OPTION) {
					book_Mark.delUrl(listbookmark.getItem(listbookmark.getFocusIndex()));
					listbookmark.remove(listbookmark.getSelectionIndex());
				}
				return;
			}

		});

		final String[] s = book_Mark.getFavorite(); // 获取所有书签
		listbookmark = new List(compositeFavorite, SWT.V_SCROLL); // 书签列表控件
		GridData rdl = new GridData(); // 外布局
		rdl.heightHint = 350;
		rdl.widthHint = 210;
		listbookmark.setLayoutData(rdl);
		listbookmark.setMenu(menuFav); // 设置右键菜单

		for (int i = 0; i < s.length; i++) {// 把所有书签添加进列表
			listbookmark.add(s[i]);
		}

		listbookmark.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {

				compositeFavorite.setVisible(false);
			}

		});
		listbookmark.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.button == 1) {// 单击鼠标左键
					GC gc = new GC(listbookmark);

					int count = (e.y / gc.textExtent("Aj").y) + (listbookmark.getVerticalBar().getSelection()); // 获取鼠标单击位置
					gc.dispose();
					if (count < listbookmark.getItemCount()) {// 如果鼠标点中列表中的项目，在新窗口打开网页
						addPage(book_Mark.getUrl(listbookmark.getItem(listbookmark.getFocusIndex())), null);

						compositeFavorite.setVisible(false); // 隐藏收藏夹
					}
				} else {// 单击鼠标右键
					listbookmark.setFocus();
					GC gc = new GC(listbookmark);
					int count = (e.y / gc.textExtent("jj").y) + (listbookmark.getVerticalBar().getSelection()); // 获取氥单击位置
					gc.dispose();
					if (count < listbookmark.getItemCount())
						listbookmark.select(count); // 如果鼠标单击列表中的项目，则选中项目。（默认单击右键不会选中）
				}
			}
		});
	}

	private void addPage(String address, Browser browser) {
		if (tabFolderPages.getItemCount() == 1) {
			tabFolderPages.getItem(0).setShowClose(true);
			boolTClose = true;
		}

		tabItemPage = new CTabItem(tabFolderPages, SWT.BORDER); // 网页标签窗口
		tabItemPage.setShowClose(boolTClose);
		if (browser == null)
			browser = new Browser(tabFolderPages, SWT.MULTI);
		tabItemPage.setControl(browser);
		tabFolderPages.setSelection(tabItemPage);

		tabItemPage.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (tabFolderPages.getItemCount() == 1)
					tabFolderPages.getItem(0).setShowClose(false); // 如果标签只剩一个，则隐藏标签关闭按钮
				Object o = e.getSource();
				((CTabItem) o).getControl().dispose(); // 释放CTabItem内的浏览器控件

				CTabItem tT = tabFolderPages.getSelection();
				if (!tT.getControl().isDisposed()) {
					Browser tempB = (Browser) tT.getControl(); // 获取即将显示的浏览器控件
					combo_address.setText(tempB.getUrl()); // 设置地址栏的文本为即将显示的网页地址
				}
			}
		});

		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent e) {
				if (e.title.length() > 12)
					tabItemPage.setText(e.title.substring(0, 12) + "...");
				else
					tabItemPage.setText(e.title);

				tabItemPage.setToolTipText(e.title);
				combo_address.setText(getSelectedBrowser().getUrl());
				addHistory();
			}
		});

		browser.addLocationListener(new LocationAdapter() {
			public void changing(org.eclipse.swt.browser.LocationEvent e) {
				if (e.location.indexOf("javascript:") == 0 || e.location.indexOf("res:") == 0)
					return;
				combo_address.setText(e.location);
			}
		});

		browser.addStatusTextListener(new org.eclipse.swt.browser.StatusTextListener() {
			public void changed(org.eclipse.swt.browser.StatusTextEvent e) {
				if (e.text.toString().equals("")) {
					labelstatus.setVisible(false);

				} else {
					labelstatus.setText(toDoubleAnd(e.text));
					GC gc = new GC(labelstatus);
					labelstatus.setSize(gc.textExtent(e.text));
					labelstatus.setVisible(true);
				}
			}
		});

		browser.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent e) {
				if (tabFolderPages.getItemCount() == 1) {
					tabFolderPages.getItem(0).setShowClose(true);
					boolTClose = true;
				}
				final Browser browserNew = new Browser(tabFolderPages, SWT.NONE);
				e.browser = browserNew;
				addPage(null, browserNew);
			}
		});
		if (address != null) {
			tabItemPage.setText("正在打开......");
			browser.setUrl(address);
		}
		browser.forceFocus();

	}

	private void addHistory() {
		saveHistory(tabFolderPages.getItem(tabFolderPages.getSelectionIndex()).getToolTipText(),
				combo_address.getText());
	}

	private String toDoubleAnd(String s) {
		StringBuilder a = new StringBuilder();
		a.insert(0, s);

		int count = 0;
		while (count >= 0) {// 此函数可以简化
			count = a.indexOf("&", count);
			if (count < 0)
				break;
			if (count != a.indexOf("&&")) {
				a.insert(count, '&');
				count += 2;
			} else {
				count += 2;
			}
		}
		return a.toString();
	}

	private String getSearchAddress(String word, SearchEngine se) {
		if (se == SearchEngine.GOOGLE)
			return "http://search.myrevery.com/search?prmdo=1&sout=1&tbo=1&q=" + word + "&btnG=%E6%90%9C%E7%B4%A2";
		if (se == SearchEngine.SOUGOU)
			return "http://www.sogou.com/sogou?query=" + word + "&pid=sogou-brse-3c72a43f255c4f04";
		if (se == SearchEngine.BAIDU)
			return "http://www.baidu.com/baidu?word=" + word + "&tn=myie2dg&ch=15";
		if (se == SearchEngine.BING)
			return "http://cn.bing.com/search?q=" + word + "&form=MXBTDF&pc=MXBR";
		return word;
	}

	private Browser getSelectedBrowser() {
		return (Browser) tabFolderPages.getItem(tabFolderPages.getSelectionIndex()).getControl();
	}

	private void gotoUrl(final String address) {

		getSelectedBrowser().setUrl(address);

		File f = new File(address);
		if (f.exists() || (address.indexOf("file://") == 0 && new File(address.substring(7)).exists())
				|| address.indexOf("about:") == 0) {
			if (address.equals("about:blank")) {

			}
			getSelectedBrowser().setUrl(address);
			return;
		}
		if (address.indexOf("javascript:") == 0) {
			getSelectedBrowser().execute(address);
			return;
		}

		if ((address.length() > 2 && address.substring(0, 2).equals("s "))) {
			getSelectedBrowser().setUrl(getSearchAddress(address.substring(2), serachEngine));
			return;
		}

		if (address.indexOf(".") < 0) {
			getSelectedBrowser().setUrl(getSearchAddress(address, serachEngine));
			return;
		}

		getSelectedBrowser().forceFocus();
	}

	private void gotoSearch(final String address) {

		if ((address.length() > 2 && address.substring(0, 2).equals("s "))) {
			getSelectedBrowser().setUrl(getSearchAddress(address.substring(2), serachEngine));
			return;
		}

		if (address.indexOf(".") < 0) {
			getSelectedBrowser().setUrl(getSearchAddress(address, serachEngine));
			return;
		}
		getSelectedBrowser().forceFocus();
	}

	/**
	 * TODO 书签保存实现
	 */
	private void saveBookmark(final String fName, String fUrl, final boolean isNewFav) {
		Shell shell = new Shell(shell_dis);

		shell.setText("新建网页收藏");
		shell.setSize(480, 200);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		shell.setLayout(gridLayout);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData dataFill = new GridData(GridData.FILL_HORIZONTAL);

		Composite compositeTitleAUrl = new Composite(shell, SWT.NONE);
		compositeTitleAUrl.setLayout(gridLayout);
		compositeTitleAUrl.setLayoutData(dataFill);

		Composite compositeDialogButton = new Composite(shell, SWT.NONE);
		compositeDialogButton.setLayout(gridLayout);
		compositeDialogButton.setLayoutData(dataFill);

		labelFavoriteName = new Label(compositeTitleAUrl, SWT.PUSH);
		labelFavoriteName.setText("名称:");

		textFavoriteName = new Text(compositeTitleAUrl, SWT.BORDER);
		textFavoriteName.setLayoutData(dataFill);
		textFavoriteName.setText(fName);

		labelFavoriteUrl = new Label(compositeTitleAUrl, SWT.NONE);
		labelFavoriteUrl.setText("网址:");

		textFavoriteUrl = new Text(compositeTitleAUrl, SWT.BORDER);
		textFavoriteUrl.setLayoutData(dataFill);
		textFavoriteUrl.setText(fUrl);

		Button buttonClose = new Button(compositeDialogButton, SWT.PUSH);
		buttonClose.setLayoutData(dataFill);
		buttonClose.setText("取消");

		Button buttonOK = new Button(compositeDialogButton, SWT.PUSH);
		buttonOK.setLayoutData(dataFill);
		buttonOK.setText("确定");
		buttonOK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				if (book_Mark.saveUrl(textFavoriteName.getText(), textFavoriteUrl.getText(), false)) {
					listbookmark.add(textFavoriteName.getText());
					shell.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "书签已存在！", "提示", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		buttonClose.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		shell.open();
	}

	private void saveHistory(final String fName, String fUrl) {
		history_list.saveUrl(fName, fUrl, false);
		listhistory.add(fName);
	}

}

/**
 * TODO 下载函数实现
 */
class downloadButton extends Frame {
	/**
	 *
	 */
	private static final long serialVersionUID = 5004912846339637700L;
	private static int threadnum;
	private static int thread;
	private static int len = 0;

	// 以下为线程状态
	private static final String DOWNLOAD_INIT = "1";
	private static final String DOWNLOAD_ING = "2";
	private static final String DOWNLOAD_PAUSE = "3";
	// 当前线程状态
	private static String stateDownload = DOWNLOAD_INIT;
	// 线程数组
	private Thread[] mThreads;

	// 框架
	JFrame mainframe;
	JPanel panel;
	// 创建输入输出文件文本域
	JTextField urltextfield = new JTextField(20);
	static JTextField outfilepath_textfield = new JTextField(20);

	// 创建按钮
	JButton action = new JButton("开始下载");
	JButton stop = new JButton("暂停下载");
	JButton constart = new JButton("继续下载");
	JButton cancelbutton = new JButton("取消下载");
	JButton outfilepath_button = new JButton("...");

	// 下拉框
	JComboBox<String> jcb;

	// 创建label
	private JLabel urLabel = new JLabel("URL");
	private JLabel saveLabel = new JLabel("另存为");
	private JLabel threadLabel = new JLabel("最大下载线程数量");
	JScrollPane jscrollPane;

	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	private static void trustAllHosts() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// Log.i(TAG, "checkClientTrusted");
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// Log.i(TAG, "checkServerTrusted");
			}
		} };
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public downloadButton() {
		this.setUndecorated(true);
		mainframe = new JFrame("下载");
		mainframe.setSize(575, 170);
		mainframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainframe.setResizable(false);

		outfilepath_textfield.setText("");
		urltextfield.setText("");
		// 定义工具包
		Toolkit kit = Toolkit.getDefaultToolkit();
		// 获取屏幕的尺寸
		Dimension screenSize = kit.getScreenSize();
		// 获取屏幕的宽
		int screenWidth = screenSize.width / 2;
		// 获取屏幕的高
		int screenHeight = screenSize.height / 2;
		// 获取窗口高度
		int height = mainframe.getHeight();
		// 获取窗口宽度
		int width = mainframe.getWidth();
		// 将窗口设置到屏幕的中部
		mainframe.setLocation(screenWidth - width / 2, screenHeight - height / 2);
		// 初始化面板
		initPanel();
		mainframe.add(panel);
		mainframe.setVisible(true);

	}

	public void initPanel() {
		this.panel = new JPanel();
		panel.setLayout(null);
		// String info1 =
		// "https://dldir1.qq.com/qqfile/qq/PCQQ9.1.1/24953/QQ9.1.1.24953.exe";
		// String info2 = "D:\\a.exe";

		urLabel.setBounds(10, 20, 120, 25);
		urltextfield.setBounds(120, 20, 400, 25);
		this.panel.add(urLabel);
		this.panel.add(urltextfield);

		saveLabel.setBounds(10, 50, 120, 25);
		outfilepath_textfield.setBounds(120, 50, 400, 25);
		outfilepath_button.setBounds(520, 50, 30, 25);
		this.panel.add(saveLabel);
		this.panel.add(outfilepath_textfield);
		this.panel.add(outfilepath_button);

		threadLabel.setBounds(10, 90, 80, 25);
		this.panel.add(threadLabel);

		action.setBounds(150, 90, 85, 25);
		this.panel.add(action);

		stop.setBounds(250, 90, 85, 25);
		this.panel.add(stop);

		constart.setBounds(360, 90, 85, 25);
		this.panel.add(constart);

		cancelbutton.setBounds(470, 90, 85, 25);
		this.panel.add(cancelbutton);

		String str[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		jcb = new JComboBox<String>(str);
		jcb.setBounds(90, 90, 40, 25);
		this.panel.add(jcb);

		urltextfield.setOpaque(false);
		outfilepath_textfield.setOpaque(false);
		// 增加动作监听
		event();
	}

	public void send() {
		// YES_NO_OPTION
		// 是：0，否：1，取消：2
		int result = JOptionPane.showConfirmDialog(null, "是否开始下载?", "确认", 0);
		if (result == 1) {
			return;
		}
		if (urltextfield.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "URL不能为空", "提示", 2);
			return;
		}
		if (outfilepath_textfield.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "保存路径不能为空", "提示", 2);
			return;
		}

		download_file();

	}

	public File openChoseWindow(int type) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(type);// 选择的文件类型(文件夹or文件)
		jfc.showDialog(new JLabel(), "选择");
		File file = jfc.getSelectedFile();
		return file;
	}

	protected void cancel() {
		if (mThreads != null) {
			if (stateDownload.equals(DOWNLOAD_PAUSE))
				onStart();
			for (Thread dt : mThreads) {
				((download) dt).cancel();
			}
		}
		JOptionPane.showMessageDialog(null, "已取消下载", "下载取消!", JOptionPane.ERROR_MESSAGE);

	}

	protected void onPause() {
		if (mThreads != null)
			stateDownload = DOWNLOAD_PAUSE;
	}

	protected void onStart() {
		if (mThreads != null)
			synchronized (DOWNLOAD_PAUSE) {
				stateDownload = DOWNLOAD_ING;
				DOWNLOAD_PAUSE.notifyAll();
			}
	}

	public void open() {
		File file = openChoseWindow(JFileChooser.DIRECTORIES_ONLY);
		if (file == null)
			return;
		outfilepath_textfield.setText(file.getAbsolutePath());
	}

	public void event() {
		outfilepath_button.addActionListener(e -> open());
		action.addActionListener(e -> send());
		stop.addActionListener(e -> onPause());
		constart.addActionListener(e -> onStart());
		cancelbutton.addActionListener(e -> cancel());
	}

	public void download_file() {
		new Thread(new Runnable() {
			public void run() {
				try {
					threadnum = jcb.getSelectedIndex() + 1;
					if (threadnum == 0) {
						JOptionPane.showMessageDialog(null, "下载线程要大于0", "下载错误!", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (mThreads == null) {
						mThreads = new Thread[threadnum];
					}
					String urlpath = urltextfield.getText();
					// String path = outfilepath_textfield.getText();

					URL url = new URL(urlpath);
					String path1 = outfilepath_textfield.getText() + "\\"
							+ urlpath.substring(urlpath.lastIndexOf("/") + 1);
					String path = path1;
					outfilepath_textfield.setText(path);
					System.out.println(path);
					HttpURLConnection connection = null;
					if (url.getProtocol().toLowerCase().equals("https")) {
						trustAllHosts();
						HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
						https.setHostnameVerifier(DO_NOT_VERIFY);
						connection = https;
					} else {
						connection = (HttpURLConnection) url.openConnection();
					}
					connection.setConnectTimeout(5000);
					connection.setRequestMethod("GET");
					connection.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
					int code = connection.getResponseCode();

					if (code == 200) {
						int length = connection.getContentLength();
						RandomAccessFile raf = new RandomAccessFile(path, "rwd");
						raf.setLength(length);
						raf.close();
						// 分块下载
						int blockSize = length / threadnum;
						for (int i = 0; i < threadnum; i++) {
							// 开始位置，获取上次取消下载的进度，默认返回i*blockLength,即第i个线程开始下载的位置
							int startindex = blockSize * (i);
							// 结束位置，-1是为了防止上一个线程和下一个线程重复下载衔接处数据
							int endindex = blockSize * (i + 1) - 1;
							// 将最后一个线程结束位置扩大，防止文件下载不完全，大了不影响，小了文件失效
							if ((i + 1) == threadnum) {
								endindex = length;
							}
							mThreads[i] = new download(urlpath, i, startindex, endindex);
							mThreads[i].start();
							thread++;
						}
					} else {
						JOptionPane.showMessageDialog(null, "请求服务器失败，请重试", "下载错误!", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {

				}
			}
		}).start();
	}

	private static class download extends Thread {
		private boolean isGoOn = true;// 是否继续下载
		private String urlpath;
		private int i;
		private int startindex;
		private int endindex;

		public download(String urlpath, int i, int startindex, int endindex) {
			this.urlpath = urlpath;
			this.i = i;
			this.startindex = startindex;
			this.endindex = endindex;
		}

		public void cancel() {
			isGoOn = false;
		}

		public void run() {
			try {
				File tempFlie = new File(i + ".txt");
				if (tempFlie.exists()) {
					JOptionPane.showMessageDialog(null, "继续下载", "提示!", JOptionPane.INFORMATION_MESSAGE);
					FileInputStream fis = new FileInputStream(tempFlie);
					byte[] b = new byte[1024];
					int length = fis.read(b);
					int downindex = Integer.parseInt(new String(b, 0, length));
					startindex = downindex;
					fis.close();
				}

				URL url = new URL(urlpath);
				HttpURLConnection connection = null;
				if (url.getProtocol().toLowerCase().equals("https")) {
					trustAllHosts();
					HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
					https.setHostnameVerifier(DO_NOT_VERIFY);
					connection = https;
				} else {
					connection = (HttpURLConnection) url.openConnection();
				}

				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				connection.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
				connection.setRequestProperty("Range", "bytes=" + startindex + "-" + endindex);

				int code = connection.getResponseCode();
				// len = 0;
				if (code == 206) {
					InputStream is = connection.getInputStream();
					String path = outfilepath_textfield.getText();
					RandomAccessFile raf = new RandomAccessFile(path, "rwd");
					raf.seek(startindex);
					int b = 0;
					int downlen = 0;
					byte[] buffer = new byte[1024];

					while ((b = is.read(buffer)) != -1) {
						// 断点断续文件读取
						RandomAccessFile file = new RandomAccessFile(i + ".txt", "rwd");
						if (!isGoOn)// 判断是否继续下载
							break;
						raf.write(buffer, 0, b);
						downlen += b;
						synchronized (DOWNLOAD_PAUSE) {// 设置暂停下载
							if (stateDownload.equals(DOWNLOAD_PAUSE)) {
								DOWNLOAD_PAUSE.wait();
							}
						}
						len = ((downlen + startindex) / 1024);
						// 将已下载数据的位置记录写入到文件
						file.write((startindex + downlen + "").getBytes());
						file.close();
					}

					is.close();
					raf.close();
					if (!isGoOn)
						return;

				}

			} catch (Exception e) {

			} finally {
				thread--;
				if (thread == 0) {
					for (int i = 0; i < threadnum; i++) {
						File tempFlie = new File(i + ".txt");
						tempFlie.delete();
					}
					JOptionPane.showMessageDialog(null, "下载成功", "成功!", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}

	}
}
