///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package fcardiodemo;
//import Net.PC15.Command.CommandDetail;
//import Net.PC15.Command.CommandParameter;
//import Net.PC15.Command.INCommand;
//import Net.PC15.Command.INCommandResult;
//import Net.PC15.Connector.ConnectorAllocator;
//import Net.PC15.Connector.ConnectorDetail;
//import Net.PC15.Connector.E_ControllerType;
//import Net.PC15.Connector.INConnectorEvent;
//import Net.PC15.Connector.TCPClient.TCPClientDetail;
//import Net.PC15.Connector.TCPServer.TCPServerClientDetail;
//import Net.PC15.Connector.UDP.UDPDetail;
//import Net.PC15.Data.AbstractTransaction;
//import Net.PC15.Data.INData;
//import Net.PC15.FC8800.Command.Card.*;
//import Net.PC15.FC8800.Command.Card.Parameter.*;
//import Net.PC15.FC8800.Command.Card.Result.*;
//import Net.PC15.FC8800.Command.Data.*;
//import Net.PC15.FC8800.Command.DateTime.ReadTime;
//import Net.PC15.FC8800.Command.DateTime.Result.ReadTime_Result;
//import Net.PC15.FC8800.Command.DateTime.WriteTime;
//import Net.PC15.FC8800.Command.DateTime.WriteTimeBroadcast;
//import Net.PC15.FC8800.Command.DateTime.WriteTimeDefine;
//import Net.PC15.FC8800.Command.Door.*;
//import Net.PC15.FC8800.Command.Door.Parameter.OpenDoor_Parameter;
//import Net.PC15.FC8800.Command.Door.Parameter.RemoteDoor_Parameter;
//import Net.PC15.FC8800.Command.FC8800Command;
//import Net.PC15.FC8800.Command.System.*;
//import Net.PC15.FC8800.Command.System.Parameter.*;
//import Net.PC15.FC8800.Command.System.Result.*;
//import Net.PC15.FC8800.Command.Transaction.*;
//import Net.PC15.FC8800.Command.Transaction.Parameter.*;
//import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseByIndex_Result;
//import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseDetail_Result;
//import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabase_Result;
//import Net.PC15.FC8800.FC8800Identity;
//import Net.PC15.Util.ByteUtil;
//import Net.PC15.Util.StringUtil;
//import Net.PC15.Util.TimeUtil;
//import Net.PC15.Util.UInt32Util;
//import io.netty.buffer.ByteBuf;
//import org.apache.log4j.Logger;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableColumn;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Timer;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
///**
// * @author 赖金杰
// */
//public class frmMain_back extends JFrame implements INConnectorEvent {
//	private final static Logger myLog = Logger.getLogger(frmMain_back.class);
//	private ConnectorAllocator _Allocator;
//	private ConcurrentHashMap<String, String> CommandName;
//	private ConcurrentHashMap<String, CommandResultCallback> CommandResult;
//	private Timer timer = new Timer();
//	private boolean mIsClose;
//	@Override
//	public void ClientOnline(TCPServerClientDetail client) {
//		// To change body of generated methods, choose Tools | Templates.
//		throw new UnsupportedOperationException("Not supported yet.");
//	}
//	@Override
//	public void ClientOffline(TCPServerClientDetail client) {
//		// To change body of generated methods, choose Tools | Templates.
//		throw new UnsupportedOperationException("Not supported yet.");
//	}
//	private interface CommandResultCallback {
//		void ResultToLog(StringBuilder strBuf, INCommandResult result);
//	}
//	/**
//	 * Creates new form FormMain
//	 */
//	public frmMain_back() {
//		initComponents();
//		setTitle("FC8800 控制器调试器 V1.0");
//		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//		mIsClose = false;
//		// JFrame.setDefaultLookAndFeelDecorated(true);
//		// 移动位置
//		setSize(1040, 800);
//		Dimension ScreenSize = getToolkit().getScreenSize();
//		Rectangle oldRectangle = getBounds();
//		setBounds((ScreenSize.width - oldRectangle.width) / 2, (ScreenSize.height - oldRectangle.height) / 2, oldRectangle.width, oldRectangle.height);
//		// 加入按钮组
//		bgConnectType.add(RadUDP);
//		bgConnectType.add(RadTCPClient);
//		bgConnectType.add(RadTCPServer);
//		RadTCPClient.setSelected(true);
//		// 初始化timer
//		timer = new Timer();
//		timer.schedule(new TimeTask(), 200, 1000);
//		ShowConnectPanel();
//		_Allocator = ConnectorAllocator.GetAllocator();
//		_Allocator.AddListener(this);
//		strLog = new StringBuilder(50000);
//		frmMain_back frm = this;
//		/**
//		 * 一定要监听退出事件
//		 */
//		addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent e) {
//				frm.setVisible(false);
//				if (_Allocator != null) {
//					_Allocator.DeleteListener(frm);
//					/**
//					 * 这里一定要释放，否则无法退出程序
//					 */
//					_Allocator.Release();
//					_Allocator = null;
//				}
//				mIsClose = true;
//				timer.cancel();
//				timer = null;
//			}
//		});
//		CommandResult = new ConcurrentHashMap<>();
//		CommandName = new ConcurrentHashMap<>();
//		IniCommandName();
//		IniCardDataBase();
//		IniWatchEvent();
//	}
//	private class TimeTask extends TimerTask {
//		@Override
//		public void run() {
//			if (mIsClose) {
//				return;
//			}
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar dt = Calendar.getInstance();
//			LblDate.setText(format.format(dt.getTime()));
//			format.applyLocalizedPattern("HH:mm:ss");
//			lblTime.setText(format.format(dt.getTime()));
//		}
//	}
//	private void ShowConnectPanel() {
//		pnlTCPServer.setVisible(false);
//		PnlUDP.setVisible(false);
//		pnlTCPClient.setVisible(false);
//		if (RadTCPClient.isSelected()) {
//			pnlTCPClient.setVisible(true);
//		}
//		if (RadUDP.isSelected()) {
//			PnlUDP.setVisible(true);
//		}
//		if (RadTCPServer.isSelected()) {
//			pnlTCPServer.setVisible(true);
//		}
//	}
//	/**
//	 * This method is called from within the constructor to initialize the form.
//	 * WARNING: Do NOT modify this code. The content of this method is always
//	 * regenerated by the Form Editor.
//	 */
//	private void initComponents() {
//		bgConnectType = new ButtonGroup();
//		jpConnectSetting = new JPanel();
//		jLabel1 = new JLabel();
//		RadTCPClient = new JRadioButton();
//		RadUDP = new JRadioButton();
//		RadTCPServer = new JRadioButton();
//		jPanel9 = new JPanel();
//		pnlTCPClient = new JPanel();
//		jLabel24 = new JLabel();
//		txtTCPServerIP = new JTextField();
//		jLabel25 = new JLabel();
//		txtTCPServerPort = new JTextField();
//		PnlUDP = new JPanel();
//		jLabel4 = new JLabel();
//		txtUDPRemoteIP = new JTextField();
//		jLabel5 = new JLabel();
//		txtUDPRemotePort = new JTextField();
//		pnlTCPServer = new JPanel();
//		jLabel2 = new JLabel();
//		jLabel3 = new JLabel();
//		txtLocalPort = new JTextField();
//		butBeginServer = new JButton();
//		jComboBox1 = new JComboBox<>();
//		jpSNDetail = new JPanel();
//		jLabel6 = new JLabel();
//		txtSN = new JTextField();
//		txtPassword = new JTextField();
//		jLabel7 = new JLabel();
//		jPanel3 = new JPanel();
//		LblDate = new JLabel();
//		lblTime = new JLabel();
//		prCommand = new JProgressBar();
//		lblCommandName = new JLabel();
//		jpLog = new JScrollPane();
//		txtLog = new JTextArea();
//		jTabSetting = new JTabbedPane();
//		jPanel6 = new JPanel();
//		butReadConnectPassword = new JButton();
//		butWriteConnectPassword = new JButton();
//		butResetConnectPassword = new JButton();
//		butReadTCPSetting = new JButton();
//		butWriteTCPSetting = new JButton();
//		ButReadDeadline = new JButton();
//		ButWriteDeadline = new JButton();
//		butReadVersion = new JButton();
//		jPanel1 = new JPanel();
//		jLabel8 = new JLabel();
//		txtWriteSN = new JTextField();
//		butWriteSN = new JButton();
//		butReadSN = new JButton();
//		butBeginWatch = new JButton();
//		butCloseWatch = new JButton();
//		butCloseAlarm = new JButton();
//		butOpenDoor = new JButton();
//		butCloseDoor = new JButton();
//		butLockDoor = new JButton();
//		butUnlockDoor = new JButton();
//		butHoldDoor = new JButton();
//		butAutoSearchDoor = new JButton();
//		jScrollPane2 = new JScrollPane();
//		jTextArea1 = new JTextArea();
//		jPanel8 = new JPanel();
//		jButton1 = new JButton();
//		jButton2 = new JButton();
//		jPanel2 = new JPanel();
//		butReadCardDataBase = new JButton();
//		butReadCardDatabaseDetail = new JButton();
//		cmbCardDataBaseType = new JComboBox<>();
//		butClearCardDataBase = new JButton();
//		jLabel9 = new JLabel();
//		jScrollPane1 = new JScrollPane();
//		tblCard = new JTable();
//		butReadCardDetail = new JButton();
//		jLabel10 = new JLabel();
//		txtCardData = new JTextField();
//		jLabel11 = new JLabel();
//		txtCardPassword = new JTextField();
//		jLabel12 = new JLabel();
//		TxtCardExpiry = new JTextField();
//		jLabel13 = new JLabel();
//		txtOpenTimes = new JTextField();
//		jLabel14 = new JLabel();
//		cmbCardStatus = new JComboBox<>();
//		chkCardDoor4 = new JCheckBox();
//		chkCardDoor1 = new JCheckBox();
//		chkCardDoor2 = new JCheckBox();
//		chkCardDoor3 = new JCheckBox();
//		jLabel15 = new JLabel();
//		butAddCardToList = new JButton();
//		butCardListClear = new JButton();
//		butUploadCard = new JButton();
//		butDeleteCard = new JButton();
//		butCardListAutoCreate = new JButton();
//		butWriteCardListBySequence = new JButton();
//		butWriteCardListBySort = new JButton();
//		butDeleteCardByList = new JButton();
//		jLabel16 = new JLabel();
//		txtCardAutoCreateSzie = new JTextField();
//		jPanel4 = new JPanel();
//		butReadTransactionDatabaseDetail = new JButton();
//		butTransactionDatabaseEmpty = new JButton();
//		jPanel5 = new JPanel();
//		butClearTransactionDatabase = new JButton();
//		jLabel17 = new JLabel();
//		cmbTransactionType = new JComboBox<>();
//		butReadTransactionDatabaseByIndex = new JButton();
//		butWriteTransactionDatabaseReadIndex = new JButton();
//		butWriteTransactionDatabaseWriteIndex = new JButton();
//		jLabel18 = new JLabel();
//		txtTransactionDatabaseReadIndex = new JTextField();
//		jLabel19 = new JLabel();
//		txtTransactionDatabaseWriteIndex = new JTextField();
//		jLabel20 = new JLabel();
//		txtReadTransactionDatabaseByIndex = new JTextField();
//		txtReadTransactionDatabaseByQuantity = new JTextField();
//		jLabel21 = new JLabel();
//		chkTransactionIsCircle = new JCheckBox();
//		butReadTransactionDatabase = new JButton();
//		jLabel22 = new JLabel();
//		txtReadTransactionDatabasePacketSize = new JTextField();
//		jLabel23 = new JLabel();
//		txtReadTransactionDatabaseQuantity = new JTextField();
//		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		setMinimumSize(new Dimension(1020, 850));
//		setSize(new Dimension(1024, 768));
//		getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		jpConnectSetting.setBorder(BorderFactory.createTitledBorder(null, "通讯参数", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
//		jpConnectSetting.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		jLabel1.setText("通讯方式：");
//		jpConnectSetting.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 21, -1, -1));
//		RadTCPClient.setText("TCP客户端");
//		RadTCPClient.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				RadTCPClientActionPerformed(evt);
//			}
//		});
//		jpConnectSetting.add(RadTCPClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 17, -1, -1));
//		RadUDP.setText("UDP");
//		RadUDP.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				RadUDPActionPerformed(evt);
//			}
//		});
//		jpConnectSetting.add(RadUDP, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 17, -1, -1));
//		RadTCPServer.setText("TCP服务器");
//		RadTCPServer.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				RadTCPServerActionPerformed(evt);
//			}
//		});
//		jpConnectSetting.add(RadTCPServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(198, 17, -1, -1));
//		pnlTCPClient.setBorder(BorderFactory.createTitledBorder("TCP 客户端"));
//		pnlTCPClient.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		jLabel24.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel24.setText("IP地址：");
//		pnlTCPClient.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 20, 79, -1));
//		txtTCPServerIP.setText("192.168.1.150");
//		pnlTCPClient.add(txtTCPServerIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 17, 223, -1));
//		jLabel25.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel25.setText("端口号：");
//		pnlTCPClient.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 47, 79, -1));
//		txtTCPServerPort.setText("8000");
//		pnlTCPClient.add(txtTCPServerPort, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 44, 72, -1));
//		PnlUDP.setBorder(BorderFactory.createTitledBorder("UDP"));
//		PnlUDP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel4.setText("IP地址：");
//		PnlUDP.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 20, 79, -1));
//		txtUDPRemoteIP.setText("192.168.1.169");
//		PnlUDP.add(txtUDPRemoteIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 17, 223, 20));
//		jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel5.setText("端口号：");
//		PnlUDP.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 47, 79, -1));
//		txtUDPRemotePort.setText("8101");
//		PnlUDP.add(txtUDPRemotePort, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 44, 72, -1));
//		pnlTCPServer.setBorder(BorderFactory.createTitledBorder("TCP 客户端"));
//		pnlTCPServer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel2.setText("客户端列表：");
//		pnlTCPServer.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 48, 79, -1));
//		jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel3.setText("监听端口：");
//		pnlTCPServer.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 79, -1));
//		txtLocalPort.setText("8000");
//		pnlTCPServer.add(txtLocalPort, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 72, -1));
//		butBeginServer.setText("开始监听");
//		pnlTCPServer.add(butBeginServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, -1, -1));
//		jComboBox1.setModel(new DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
//		pnlTCPServer.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 45, 240, -1));
//		GroupLayout jPanel9Layout = new GroupLayout(jPanel9);
//		jPanel9.setLayout(jPanel9Layout);
//		jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pnlTCPClient, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE).addComponent(PnlUDP, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE).addComponent(pnlTCPServer, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)).addGap(0, 0, Short.MAX_VALUE)));
//		jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addComponent(pnlTCPClient, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE).addGap(2, 2, 2).addComponent(PnlUDP, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(pnlTCPServer, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
//		pnlTCPServer.getAccessibleContext().setAccessibleName("TCP 服务器");
//		jpConnectSetting.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 35, 340, 75));
//		getContentPane().add(jpConnectSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 350, 115));
//		jpSNDetail.setBorder(BorderFactory.createTitledBorder(""));
//		jpSNDetail.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		jLabel6.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel6.setText("SN：");
//		jpSNDetail.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 3, 36, -1));
//		txtSN.setText("MC-5912T19060002");
//		jpSNDetail.add(txtSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 0, 131, -1));
//		txtPassword.setText("FFFFFFFF");
//		jpSNDetail.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 0, 88, -1));
//		jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel7.setText("通讯密码：");
//		jpSNDetail.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 3, 69, -1));
//		getContentPane().add(jpSNDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 130, 349, 23));
//		jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		LblDate.setFont(new Font("宋体", 0, 14)); // NOI18N
//		LblDate.setForeground(new Color(0, 0, 255));
//		LblDate.setHorizontalAlignment(SwingConstants.CENTER);
//		LblDate.setText("2017年10月23日");
//		jPanel3.add(LblDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 121, -1));
//		lblTime.setFont(new Font("宋体", 0, 14)); // NOI18N
//		lblTime.setForeground(new Color(0, 0, 255));
//		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
//		lblTime.setText("18:09:07");
//		jPanel3.add(lblTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 121, 20));
//		jPanel3.add(prCommand, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, -1, -1));
//		lblCommandName.setHorizontalAlignment(SwingConstants.LEFT);
//		lblCommandName.setText("当前命令：");
//		lblCommandName.setVerticalAlignment(SwingConstants.TOP);
//		jPanel3.add(lblCommandName, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 146, 80));
//		getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 14, -1, 140));
//		txtLog.setColumns(20);
//		txtLog.setRows(5);
//		txtLog.setLineWrap(true);
//		txtLog.setWrapStyleWord(true);
//		txtLog.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent evt) {
//				txtLogMouseClicked(evt);
//			}
//		});
//		jpLog.setViewportView(txtLog);
//		getContentPane().add(jpLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 163, 501, 590));
//		// NOI18N
//		jTabSetting.setName("");
//		jPanel6.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
//		jPanel6.setLayout(null);
//		butReadConnectPassword.setText("读通讯密码");
//		butReadConnectPassword.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butReadConnectPasswordActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butReadConnectPassword);
//		butReadConnectPassword.setBounds(20, 170, 93, 23);
//		butWriteConnectPassword.setText("写通讯密码");
//		butWriteConnectPassword.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butWriteConnectPasswordActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butWriteConnectPassword);
//		butWriteConnectPassword.setBounds(250, 170, 93, 23);
//		butResetConnectPassword.setText("复位通讯密码");
//		butResetConnectPassword.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butResetConnectPasswordActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butResetConnectPassword);
//		butResetConnectPassword.setBounds(130, 170, 105, 23);
//		butReadTCPSetting.setText("读TCP参数");
//		butReadTCPSetting.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butReadTCPSettingActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butReadTCPSetting);
//		butReadTCPSetting.setBounds(20, 130, 109, 23);
//		butWriteTCPSetting.setText("写TCP参数");
//		butWriteTCPSetting.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butWriteTCPSettingActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butWriteTCPSetting);
//		butWriteTCPSetting.setBounds(140, 130, 116, 23);
//		ButReadDeadline.setText("读取控制器有效天数");
//		ButReadDeadline.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				ButReadDeadlineActionPerformed(evt);
//			}
//		});
//		jPanel6.add(ButReadDeadline);
//		ButReadDeadline.setBounds(310, 90, 190, 23);
//		ButWriteDeadline.setText("修改控制器有效天数");
//		ButWriteDeadline.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				ButWriteDeadlineActionPerformed(evt);
//			}
//		});
//		jPanel6.add(ButWriteDeadline);
//		ButWriteDeadline.setBounds(350, 120, 151, 23);
//		butReadVersion.setText("读取版本号");
//		butReadVersion.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butReadVersionActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butReadVersion);
//		butReadVersion.setBounds(10, 80, 138, 23);
//		jPanel1.setBorder(BorderFactory.createTitledBorder("SN"));
//		jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		jLabel8.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel8.setText("SN：");
//		jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 36, -1));
//		txtWriteSN.setText("FC-8940A46060007");
//		jPanel1.add(txtWriteSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 131, -1));
//		butWriteSN.setText("写");
//		butWriteSN.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butWriteSNActionPerformed(evt);
//			}
//		});
//		jPanel1.add(butWriteSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));
//		butReadSN.setText("读");
//		butReadSN.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butReadSNActionPerformed(evt);
//			}
//		});
//		jPanel1.add(butReadSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, -1, -1));
//		jPanel6.add(jPanel1);
//		jPanel1.setBounds(10, 10, 310, 50);
//		butBeginWatch.setText("打开数据监控");
//		butBeginWatch.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butBeginWatchActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butBeginWatch);
//		butBeginWatch.setBounds(30, 220, 140, 23);
//		butCloseWatch.setText("关闭数据监控");
//		butCloseWatch.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butCloseWatchActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butCloseWatch);
//		butCloseWatch.setBounds(180, 220, 130, 23);
//		butCloseAlarm.setText("解除所有报警");
//		butCloseAlarm.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butCloseAlarmActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butCloseAlarm);
//		butCloseAlarm.setBounds(30, 270, 160, 23);
//		butOpenDoor.setText("远程开门");
//		butOpenDoor.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butOpenDoorActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butOpenDoor);
//		butOpenDoor.setBounds(30, 330, 81, 23);
//		butCloseDoor.setText("远程关门");
//		butCloseDoor.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butCloseDoorActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butCloseDoor);
//		butCloseDoor.setBounds(120, 330, 81, 23);
//		butLockDoor.setText("远程锁定");
//		butLockDoor.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butLockDoorActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butLockDoor);
//		butLockDoor.setBounds(300, 330, 81, 23);
//		butUnlockDoor.setText("远程解锁");
//		butUnlockDoor.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butUnlockDoorActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butUnlockDoor);
//		butUnlockDoor.setBounds(390, 330, 81, 23);
//		butHoldDoor.setText("远程常开");
//		butHoldDoor.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butHoldDoorActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butHoldDoor);
//		butHoldDoor.setBounds(210, 330, 81, 23);
//		butAutoSearchDoor.setText("自动搜索控制器");
//		butAutoSearchDoor.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butAutoSearchDoorActionPerformed(evt);
//			}
//		});
//		jPanel6.add(butAutoSearchDoor);
//		butAutoSearchDoor.setBounds(20, 390, 130, 23);
//		jTextArea1.setEditable(false);
//		jTextArea1.setColumns(20);
//		jTextArea1.setRows(5);
//		// jTextArea1.setText("UDP通讯只支持如下指令\n【搜索不同网络标识设备】、【设置设备网络标识】\n【读TCP参数】、【写TCP参数】");
//		jTextArea1.setText("UDP通讯只支持如下指令【搜索不同网络标识设备】、【设置设备网络标识】【读TCP参数】、【写TCP参数】");
//		jScrollPane2.setViewportView(jTextArea1);
//		jPanel6.add(jScrollPane2);
//		jScrollPane2.setBounds(20, 500, 390, 110);
//		jTabSetting.addTab("系统设置", jPanel6);
//		jPanel8.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
//		jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		jButton1.setText("读时间");
//		jButton1.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				jButton1ActionPerformed(evt);
//			}
//		});
//		jPanel8.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, -1, -1));
//		jButton2.setText("写时间");
//		jButton2.setActionCommand("");
//		jButton2.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				jButton2ActionPerformed(evt);
//			}
//		});
//		jPanel8.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, -1));
//		jTabSetting.addTab("时间参数", jPanel8);
//		jPanel2.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
//		jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		butReadCardDataBase.setText("从控制器中读取卡片数据");
//		butReadCardDataBase.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butReadCardDataBaseActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butReadCardDataBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, -1, -1));
//		butReadCardDatabaseDetail.setText("读卡数据库信息");
//		butReadCardDatabaseDetail.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butReadCardDatabaseDetailActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butReadCardDatabaseDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, -1));
//		cmbCardDataBaseType.setModel(new DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
//		jPanel2.add(cmbCardDataBaseType, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 130, -1));
//		butClearCardDataBase.setBackground(new Color(255, 51, 51));
//		butClearCardDataBase.setForeground(new Color(255, 0, 51));
//		butClearCardDataBase.setText("清空卡片数据库");
//		butClearCardDataBase.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butClearCardDataBaseActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butClearCardDataBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));
//		jLabel9.setText("卡区域：");
//		jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 52, -1, -1));
//		tblCard.setModel(new DefaultTableModel(new Object[][]{}, new String[]{}));
//		jScrollPane1.setViewportView(tblCard);
//		jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 480, 400));
//		butReadCardDetail.setText("读取单个卡片在控制器中的信息");
//		butReadCardDetail.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butReadCardDetailActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butReadCardDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 230, -1));
//		jLabel10.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel10.setText("卡号：");
//		jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 543, 36, -1));
//		txtCardData.setText("123456");
//		jPanel2.add(txtCardData, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 540, 70, -1));
//		jLabel11.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel11.setText("密码：");
//		jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 540, 36, -1));
//		txtCardPassword.setText("8888");
//		jPanel2.add(txtCardPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 540, 70, -1));
//		jLabel12.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel12.setText("截止日期：");
//		jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 543, 70, -1));
//		jLabel12.getAccessibleContext().setAccessibleName("有效期：");
//		TxtCardExpiry.setText("2030-12-30 12:30");
//		jPanel2.add(TxtCardExpiry, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 540, 110, -1));
//		jLabel13.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel13.setText("开门次数：");
//		jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 70, -1));
//		txtOpenTimes.setText("65535");
//		jPanel2.add(txtOpenTimes, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 570, 70, -1));
//		jLabel14.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel14.setText("状态：");
//		jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 570, 50, -1));
//		cmbCardStatus.setModel(new DefaultComboBoxModel<>(new String[]{"正常", "挂失卡", "黑名单"}));
//		jPanel2.add(cmbCardStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 570, 70, -1));
//		chkCardDoor4.setSelected(true);
//		chkCardDoor4.setText("门4");
//		jPanel2.add(chkCardDoor4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 600, -1, -1));
//		chkCardDoor1.setSelected(true);
//		chkCardDoor1.setText("门1");
//		jPanel2.add(chkCardDoor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 600, -1, -1));
//		chkCardDoor2.setSelected(true);
//		chkCardDoor2.setText("门2");
//		jPanel2.add(chkCardDoor2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 600, -1, -1));
//		chkCardDoor3.setSelected(true);
//		chkCardDoor3.setText("门3");
//		jPanel2.add(chkCardDoor3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 600, -1, -1));
//		jLabel15.setText("门权限：");
//		jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, -1, -1));
//		butAddCardToList.setText("添加到列表");
//		butAddCardToList.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butAddCardToListActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butAddCardToList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, 160, -1));
//		butCardListClear.setText("清空列表");
//		butCardListClear.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butCardListClearActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butCardListClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(381, 490, 110, -1));
//		butUploadCard.setText("将卡号上传至非排序区");
//		butUploadCard.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butUploadCardActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butUploadCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 630, -1, -1));
//		butDeleteCard.setText("将卡号从控制器删除");
//		butDeleteCard.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butDeleteCardActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butDeleteCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 630, -1, -1));
//		butCardListAutoCreate.setText("自动生成卡号");
//		butCardListAutoCreate.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butCardListAutoCreateActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butCardListAutoCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 160, -1));
//		butWriteCardListBySequence.setText("将列表卡号上传至非排序区");
//		butWriteCardListBySequence.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butWriteCardListBySequenceActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butWriteCardListBySequence, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 690, -1, -1));
//		butWriteCardListBySort.setText("将列表卡号上传至排序区");
//		butWriteCardListBySort.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butWriteCardListBySortActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butWriteCardListBySort, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 690, -1, -1));
//		butDeleteCardByList.setText("删除列表中的卡片");
//		butDeleteCardByList.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butDeleteCardByListActionPerformed(evt);
//			}
//		});
//		jPanel2.add(butDeleteCardByList, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 690, -1, -1));
//		jLabel16.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel16.setText("自动生成数量：");
//		jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 663, 90, -1));
//		txtCardAutoCreateSzie.setText("2000");
//		jPanel2.add(txtCardAutoCreateSzie, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 660, 70, -1));
//		jTabSetting.addTab("卡片管理", jPanel2);
//		jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		butReadTransactionDatabaseDetail.setText("读取记录数据库详情");
//		butReadTransactionDatabaseDetail.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butReadTransactionDatabaseDetailActionPerformed(evt);
//			}
//		});
//		jPanel4.add(butReadTransactionDatabaseDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));
//		butTransactionDatabaseEmpty.setBackground(new Color(255, 0, 0));
//		butTransactionDatabaseEmpty.setForeground(new Color(255, 0, 0));
//		butTransactionDatabaseEmpty.setText("清空所有记录");
//		butTransactionDatabaseEmpty.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butTransactionDatabaseEmptyActionPerformed(evt);
//			}
//		});
//		jPanel4.add(butTransactionDatabaseEmpty, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, -1));
//		jPanel5.setBorder(BorderFactory.createTitledBorder("指定记录类型的操作"));
//		jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//		butClearTransactionDatabase.setBackground(new Color(255, 0, 0));
//		butClearTransactionDatabase.setForeground(new Color(255, 0, 0));
//		butClearTransactionDatabase.setText("清空记录");
//		butClearTransactionDatabase.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butClearTransactionDatabaseActionPerformed(evt);
//			}
//		});
//		jPanel5.add(butClearTransactionDatabase, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, -1, -1));
//		jLabel17.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel17.setText("记录类型：");
//		jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, -1));
//		cmbTransactionType.setModel(new DefaultComboBoxModel<>(new String[]{"读卡记录", "出门开关记录", "门磁记录", "软件操作记录", "报警记录", "系统记录"}));
//		jPanel5.add(cmbTransactionType, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 110, -1));
//		butReadTransactionDatabaseByIndex.setText("按序号读记录");
//		butReadTransactionDatabaseByIndex.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butReadTransactionDatabaseByIndexActionPerformed(evt);
//			}
//		});
//		jPanel5.add(butReadTransactionDatabaseByIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 120, -1));
//		butWriteTransactionDatabaseReadIndex.setText("修改记录读索引");
//		butWriteTransactionDatabaseReadIndex.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butWriteTransactionDatabaseReadIndexActionPerformed(evt);
//			}
//		});
//		jPanel5.add(butWriteTransactionDatabaseReadIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));
//		butWriteTransactionDatabaseWriteIndex.setText("修改记录写索引");
//		butWriteTransactionDatabaseWriteIndex.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butWriteTransactionDatabaseWriteIndexActionPerformed(evt);
//			}
//		});
//		jPanel5.add(butWriteTransactionDatabaseWriteIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));
//		jLabel18.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel18.setText("读索引号：");
//		jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 73, 79, -1));
//		txtTransactionDatabaseReadIndex.setText("0");
//		jPanel5.add(txtTransactionDatabaseReadIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, 60, -1));
//		jLabel19.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel19.setText("写索引号：");
//		jPanel5.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 103, 79, -1));
//		txtTransactionDatabaseWriteIndex.setText("0");
//		jPanel5.add(txtTransactionDatabaseWriteIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 100, 60, -1));
//		jLabel20.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel20.setText("开始索引号：");
//		jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 133, 79, -1));
//		txtReadTransactionDatabaseByIndex.setText("1");
//		jPanel5.add(txtReadTransactionDatabaseByIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, 60, -1));
//		txtReadTransactionDatabaseByQuantity.setText("10");
//		jPanel5.add(txtReadTransactionDatabaseByQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, 60, -1));
//		jLabel21.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel21.setText("读数量：");
//		jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 133, 60, -1));
//		chkTransactionIsCircle.setText("循环");
//		jPanel5.add(chkTransactionIsCircle, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, -1, -1));
//		butReadTransactionDatabase.setText("读新记录");
//		butReadTransactionDatabase.addActionListener(new java.awt.event.ActionListener() {
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				butReadTransactionDatabaseActionPerformed(evt);
//			}
//		});
//		jPanel5.add(butReadTransactionDatabase, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 120, -1));
//		jLabel22.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel22.setText("单次读取数量：");
//		jPanel5.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 90, -1));
//		txtReadTransactionDatabasePacketSize.setText("200");
//		jPanel5.add(txtReadTransactionDatabasePacketSize, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 60, -1));
//		jLabel23.setHorizontalAlignment(SwingConstants.RIGHT);
//		jLabel23.setText("读数量：");
//		jPanel5.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 60, -1));
//		txtReadTransactionDatabaseQuantity.setText("0");
//		jPanel5.add(txtReadTransactionDatabaseQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 170, 60, -1));
//		jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 470, 290));
//		jTabSetting.addTab("记录管理", jPanel4);
//		getContentPane().add(jTabSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 510, 750));
//		pack();
//	}
//	// </editor-fold>//GEN-END:initComponents
//	// GEN-FIRST:event_RadTCPClientActionPerformed
//	private void RadTCPClientActionPerformed(java.awt.event.ActionEvent evt) {
//		// TODO add your handling code here:
//		ShowConnectPanel();
//	}
//	// GEN-LAST:event_RadTCPClientActionPerformed
//	// GEN-FIRST:event_RadUDPActionPerformed
//	private void RadUDPActionPerformed(java.awt.event.ActionEvent evt) {
//		// TODO add your handling code here:
//		ShowConnectPanel();
//	}
//	// GEN-LAST:event_RadUDPActionPerformed
//	// GEN-FIRST:event_RadTCPServerActionPerformed
//	private void RadTCPServerActionPerformed(java.awt.event.ActionEvent evt) {
//		// TODO add your handling code here:
//		ShowConnectPanel();
//	}
//	// GEN-LAST:event_RadTCPServerActionPerformed
//	// GEN-FIRST:event_txtLogMouseClicked
//	private void txtLogMouseClicked(MouseEvent evt) {
//		// TODO add your handling code here:
//		if (evt.getClickCount() == 2) {
//			strLog.delete(0, strLog.length());
//			txtLog.setText("");
//		}
//	}
//	// GEN-LAST:event_txtLogMouseClicked
//	// GEN-FIRST:event_butReadTransactionDatabaseActionPerformed
//	/**
//	 * 读取新记录
//	 * @param evt
//	 */
//	private void butReadTransactionDatabaseActionPerformed(java.awt.event.ActionEvent evt) {
//		// 读取新记录
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		int type = cmbTransactionType.getSelectedIndex() + 1;
//		String strPacketSize = txtReadTransactionDatabasePacketSize.getText();
//		if (!StringUtil.IsNum(strPacketSize) || strPacketSize.length() > 3) {
//			JOptionPane.showMessageDialog(null, "单次读取数量必须为数字，取值范围1-300！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int PacketSize = Integer.valueOf(strPacketSize);
//		if (PacketSize > 300 || PacketSize < 0) {
//			JOptionPane.showMessageDialog(null, "单次读取数量必须为数字，取值范围1-300！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		String strQuantity = txtReadTransactionDatabaseQuantity.getText();
//		if (!StringUtil.IsNum(strQuantity) || strQuantity.length() > 6) {
//			JOptionPane.showMessageDialog(null, "读新记录数量必须是数字，取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int Quantity = Integer.valueOf(strQuantity);
//		if (Quantity < 0) {
//			JOptionPane.showMessageDialog(null, "读新记录数量必须大于0！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		ReadTransactionDatabase_Parameter par = new ReadTransactionDatabase_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
//		par.PacketSize = PacketSize;
//		par.Quantity = Quantity;
//		// 徐铭康修改
//		// ReadTransactionDatabase cmd = new ReadTransactionDatabase(par);
//		ReadTransactionDatabase cmd = new ReadTransactionDatabase(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) y;
//			// x.append("\n记录类型：");
//			x.append("记录类型：");
//			x.append(mWatchTypeNameList[result.DatabaseType.getValue()]);
//			x.append("，本次读取数量：");
//			x.append(result.Quantity);
//			x.append("，未读取新记录数量：");
//			x.append(result.readable);
//			if (result.Quantity > 0) {
//				// x.append("\n");
//				// 开始写出记录日志
//				StringBuilder log = new StringBuilder(result.Quantity * 100);
//				String[] sTransactionList = null;
//				switch (result.DatabaseType) {
//					case OnCardTransaction:
//						sTransactionList = mCardTransactionList;
//						break;
//					case OnButtonTransaction:
//						sTransactionList = mButtonTransactionList;
//						break;
//					case OnDoorSensorTransaction:
//						sTransactionList = mDoorSensorTransactionList;
//						break;
//					case OnSoftwareTransaction:
//						sTransactionList = mSoftwareTransactionList;
//						break;
//					case OnAlarmTransaction:
//						sTransactionList = mAlarmTransactionList;
//						break;
//					case OnSystemTransaction:
//						sTransactionList = mSystemTransactionList;
//						break;
//					default:
//						Logger.getRootLogger().info("");
//				}
//				PrintTransactionDatabase(result.TransactionList, log, sTransactionList);
//				String path = WriteFile("门禁调试器记录数据库", log, false);
//				if (path == null) {
//					// x.append("\n写日志失败！");
//					x.append("写日志失败！");
//				}
//				else {
//					x.append("\n以保存到日志文件，");
//					x.append(path);
//				}
//				x.append("\n");
//				if (result.Quantity < 1000) {
//					x.append(log);
//				}
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butReadTransactionDatabaseActionPerformed
//	// GEN-FIRST:event_butWriteTransactionDatabaseWriteIndexActionPerformed
//	private void butWriteTransactionDatabaseWriteIndexActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		int type = cmbTransactionType.getSelectedIndex() + 1;
//		String strWriteIndex = txtTransactionDatabaseWriteIndex.getText();
//		if (!StringUtil.IsNum(strWriteIndex) || strWriteIndex.length() > 6) {
//			JOptionPane.showMessageDialog(null, "写索引号必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int index = Integer.valueOf(strWriteIndex);
//		if (index > 160000 || index < 0) {
//			JOptionPane.showMessageDialog(null, "写索引号取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		WriteTransactionDatabaseWriteIndex_Parameter par = new WriteTransactionDatabaseWriteIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
//		par.WriteIndex = index;
//		WriteTransactionDatabaseWriteIndex cmd = new WriteTransactionDatabaseWriteIndex(par);
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butWriteTransactionDatabaseWriteIndexActionPerformed
//	// GEN-FIRST:event_butWriteTransactionDatabaseReadIndexActionPerformed
//	private void butWriteTransactionDatabaseReadIndexActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		int type = cmbTransactionType.getSelectedIndex() + 1;
//		String strReadIndex = txtTransactionDatabaseReadIndex.getText();
//		if (!StringUtil.IsNum(strReadIndex) || strReadIndex.length() > 6) {
//			JOptionPane.showMessageDialog(null, "读索引号必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int index = Integer.valueOf(strReadIndex);
//		if (index > 160000 || index < 0) {
//			JOptionPane.showMessageDialog(null, "读索引号取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		WriteTransactionDatabaseReadIndex_Parameter par = new WriteTransactionDatabaseReadIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
//		par.ReadIndex = index;
//		par.IsCircle = chkTransactionIsCircle.isSelected();
//		WriteTransactionDatabaseReadIndex cmd = new WriteTransactionDatabaseReadIndex(par);
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butWriteTransactionDatabaseReadIndexActionPerformed
//	// GEN-FIRST:event_butReadTransactionDatabaseByIndexActionPerformed
//	private void butReadTransactionDatabaseByIndexActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		int type = cmbTransactionType.getSelectedIndex() + 1;
//		String strReadIndex = txtReadTransactionDatabaseByIndex.getText();
//		if (!StringUtil.IsNum(strReadIndex) || strReadIndex.length() > 6) {
//			JOptionPane.showMessageDialog(null, "读记录起始索引号必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int index = Integer.valueOf(strReadIndex);
//		if (index > 160000 || index < 0) {
//			JOptionPane.showMessageDialog(null, "读记录起始索引号取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		String strQuantity = txtReadTransactionDatabaseByQuantity.getText();
//		if (!StringUtil.IsNum(strQuantity) || strQuantity.length() > 3) {
//			JOptionPane.showMessageDialog(null, "读记录数量必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int Quantity = Integer.valueOf(strQuantity);
//		if (Quantity > 400 || Quantity <= 0) {
//			JOptionPane.showMessageDialog(null, "读记录数量取值范围1-400！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		ReadTransactionDatabaseByIndex_Parameter par = new ReadTransactionDatabaseByIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
//		par.ReadIndex = index;
//		par.Quantity = Quantity;
//		// ReadTransactionDatabaseByIndex cmd = new ReadTransactionDatabaseByIndex(par);
//		ReadTransactionDatabaseByIndex cmd = new ReadTransactionDatabaseByIndex(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadTransactionDatabaseByIndex_Result result = (ReadTransactionDatabaseByIndex_Result) y;
//			x.append("\n记录类型：");
//			x.append(mWatchTypeNameList[result.DatabaseType.getValue()]);
//			x.append("，起始号：");
//			x.append(result.ReadIndex);
//			x.append("，读取数量：");
//			x.append(result.Quantity);
//			if (result.Quantity > 0) {
//				x.append("\n");
//				// 开始写出记录日志
//				StringBuilder log = new StringBuilder(result.Quantity * 100);
//				String[] sTransactionList = null;
//				switch (result.DatabaseType) {
//					case OnCardTransaction:
//						sTransactionList = mCardTransactionList;
//						break;
//					case OnButtonTransaction:
//						sTransactionList = mButtonTransactionList;
//						break;
//					case OnDoorSensorTransaction:
//						sTransactionList = mDoorSensorTransactionList;
//						break;
//					case OnSoftwareTransaction:
//						sTransactionList = mSoftwareTransactionList;
//						break;
//					case OnAlarmTransaction:
//						sTransactionList = mAlarmTransactionList;
//						break;
//					case OnSystemTransaction:
//						sTransactionList = mSystemTransactionList;
//						break;
//				}
//				PrintTransactionDatabase(result.TransactionList, log, sTransactionList);
//				x.append(log);
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butReadTransactionDatabaseByIndexActionPerformed
//	// GEN-FIRST:event_butClearTransactionDatabaseActionPerformed
//	private void butClearTransactionDatabaseActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		int type = cmbTransactionType.getSelectedIndex() + 1;
//		ClearTransactionDatabase_Parameter par = new ClearTransactionDatabase_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
//		ClearTransactionDatabase cmd = new ClearTransactionDatabase(par);
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butClearTransactionDatabaseActionPerformed.
//	// GEN-FIRST:event_butTransactionDatabaseEmptyActionPerformed
//	private void butTransactionDatabaseEmptyActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		TransactionDatabaseEmpty cmd = new TransactionDatabaseEmpty(par);
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butTransactionDatabaseEmptyActionPerformed
//	// GEN-FIRST:event_butReadTransactionDatabaseDetailActionPerformed
//	/**
//	 * 读取记录数据库详情
//	 * @param evt
//	 */
//	private void butReadTransactionDatabaseDetailActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		ReadTransactionDatabaseDetail cmd = new ReadTransactionDatabaseDetail(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadTransactionDatabaseDetail_Result result = (ReadTransactionDatabaseDetail_Result) y;
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[1], result.DatabaseDetail.CardTransactionDetail, x);
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[2], result.DatabaseDetail.ButtonTransactionDetail, x);
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[3], result.DatabaseDetail.DoorSensorTransactionDetail, x);
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[4], result.DatabaseDetail.SoftwareTransactionDetail, x);
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[5], result.DatabaseDetail.AlarmTransactionDetail, x);
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[6], result.DatabaseDetail.SystemTransactionDetail, x);
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butReadTransactionDatabaseDetailActionPerformed
//	// GEN-FIRST:event_butDeleteCardByListActionPerformed
//	private void butDeleteCardByListActionPerformed(java.awt.event.ActionEvent evt) {
//		// 删除列表中的卡片
//		if (mCardList == null) {
//			JOptionPane.showMessageDialog(null, "卡片列表为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 5000;
//		// 此函数超时时间设定长一些
//		int ilstLen = mCardList.size();
//		// 徐铭康修改
//		// DeleteCard cmd = new DeleteCard(par);
//		FC8800Command cmd = null;
//		String[] lst = new String[ilstLen];
//		for (int i = 0; i < ilstLen; i++) {
//			lst[i] = mCardList.get(i).GetCardData();
//		}
//		DeleteCard_Parameter par = new DeleteCard_Parameter(dt, lst);
//		cmd = new DeleteCard(par);
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butDeleteCardByListActionPerformed
//	// GEN-FIRST:event_butWriteCardListBySortActionPerformed
//	private void butWriteCardListBySortActionPerformed(java.awt.event.ActionEvent evt) {
//		// 将列表中的卡片上传至排序区
//		if (mCardList == null) {
//			JOptionPane.showMessageDialog(null, "卡片列表为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 5000;// 此函数超时时间设定长一些
//		WriteCardListBySort_Parameter par = new WriteCardListBySort_Parameter(dt, mCardList);
//		// 徐铭康修改
//		// WriteCardListBySort cmd = new WriteCardListBySort(par);
//		WriteCardListBySort cmd = new WriteCardListBySort(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			WriteCardListBySort_Result result = (WriteCardListBySort_Result) y;
//			ArrayList<? extends CardDetail> _list = result.CardList;
//			x.append("上传完毕");
//			if (result.FailTotal > 0) {
//				x.append("失败数量：");
//				x.append(result.FailTotal);
//				x.append("，卡号列表：\n");
//				for (CardDetail c : _list) {
//					x.append(c.GetCardData());
//					x.append("\n");
//				}
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butWriteCardListBySortActionPerformed
//	// GEN-FIRST:event_butWriteCardListBySequenceActionPerformed
//	private void butWriteCardListBySequenceActionPerformed(java.awt.event.ActionEvent evt) {
//		// 将列表中的卡片上传至非排序区
//		if (mCardList == null) {
//			JOptionPane.showMessageDialog(null, "卡片列表为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 8000;// 此函数超时时间设定长一些
//		// 徐铭康 修改
//		WriteCardListBySequence_Parameter par = new WriteCardListBySequence_Parameter(dt, mCardList);
//		// WriteCardListBySequence cmd = new WriteCardListBySequence(par);
//		FC8800Command cmd = new WriteCardListBySequence(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			WriteCardListBySequence_Result result = (WriteCardListBySequence_Result) y;
//			ArrayList<? extends CardDetail> _list = result.CardList;
//			x.append("上传完毕");
//			if (result.FailTotal > 0) {
//				x.append("失败数量：");
//				x.append(result.FailTotal);
//				x.append("，卡号列表：\n");
//				for (CardDetail c : _list) {
//					x.append(c.GetCardData());
//					x.append("\n");
//				}
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butWriteCardListBySequenceActionPerformed
//	// GEN-FIRST:event_butCardListAutoCreateActionPerformed
//	private void butCardListAutoCreateActionPerformed(java.awt.event.ActionEvent evt) {
//		// 自动创建测试卡列表
//		int maxSize = 1000;
//		String strSize = txtCardAutoCreateSzie.getText();
//		if (!StringUtil.IsNum(strSize) || strSize.length() > 6) {
//			JOptionPane.showMessageDialog(null, "待生成的数量为数字，取值范围1-120000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		maxSize = Integer.valueOf(strSize);
//		if (mCardList == null) {
//			mCardList = new ArrayList<>(10000);
//		}
//		if ((maxSize + mCardList.size()) > 120000) {
//			JOptionPane.showMessageDialog(null, "待生成的数量和列表中的卡数相加超过12万！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		Random rnd = new Random();
//		int max = 90000000;
//		int min = 10000000;
//		int iSearch = 0;
//		CardDetail surCard = GetCardDetail();
//		if (surCard == null) {
//			return;
//		}
//		Collections.sort(mCardList);
//		// 徐铭康修改
//		ArrayList<CardDetail> tmplst = new ArrayList<>(1500);
//		// Calendar time=Calendar.getInstance();
//		while (maxSize > 0) {
//			long card = rnd.nextInt(max) % (max - min + 1) + min;
//			// 徐铭康修改
//			// CardDetail cd = new CardDetail(card);
//			CardDetail cd = new CardDetail();
//			try {
//				cd.SetCardData(String.valueOf(card));
//			}
//			catch (Exception e) {
//				JOptionPane.showMessageDialog(null, "生成随机卡号异常", "卡片管理", JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//			iSearch = CardDetail.SearchCardDetail(mCardList, cd);
//			if (iSearch == -1) {
//				if (tmplst.indexOf(cd) == -1) {
//					cd.Password = surCard.Password;// 设定密码
//					cd.Expiry = surCard.Expiry;// 设定有效期
//					cd.OpenTimes = surCard.OpenTimes;// 开门次数
//					cd.CardStatus = surCard.CardStatus;
//					// 设定4个门的开门时段
//					for (int i = 1; i <= 4; i++) {
//						cd.SetTimeGroup(i, 1);// 每个门都设定为1门
//						cd.SetDoor(i, surCard.GetDoor(i));// 设定每个门权限
//					}
//					cd.SetNormal();// 设定卡片没有特权
//					cd.HolidayUse = true;// 设定受节假日限制。
//					tmplst.add(cd);
//					if (tmplst.size() >= 1000) {
//						mCardList.addAll(tmplst);
//						Collections.sort(mCardList);
//						tmplst.clear();
//					}
//					maxSize--;
//				}
//			}
//		}
//		if (tmplst.size() > 0) {
//			mCardList.addAll(tmplst);
//			Collections.sort(mCardList);
//			tmplst.clear();
//		}
//		// Calendar endtime=Calendar.getInstance();
//		// int hs=(int)(endtime.getTimeInMillis()- time.getTimeInMillis());
//		// System.out.println("耗时：" + hs);
//		FillCardToList();
//	}
//	// GEN-LAST:event_butCardListAutoCreateActionPerformed
//	// GEN-FIRST:event_butDeleteCardActionPerformed
//	private void butDeleteCardActionPerformed(java.awt.event.ActionEvent evt) {
//		// 删除卡片
//		CardDetail cd = GetCardDetail();
//		if (cd == null) {
//			return;
//		}
//		// 读取控制器中的卡片数据库信息
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		// 此函数超时时间设定长一些
//		dt.Timeout = 5000;
//		// 徐铭康修改
//		// DeleteCard cmd = new DeleteCard(par);
//		String[] lst = new String[1];
//		lst[0] = cd.GetCardData();
//		DeleteCard_Parameter par = new DeleteCard_Parameter(dt, lst);
//		DeleteCard cmd = new DeleteCard(par);
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butDeleteCardActionPerformed
//	// GEN-FIRST:event_butUploadCardActionPerformed
//	private void butUploadCardActionPerformed(java.awt.event.ActionEvent evt) {
//		// 上传卡片至非排序区
//		CardDetail cd = GetCardDetail();
//		if (cd == null) {
//			return;
//		}
//		ArrayList<CardDetail> lst = new ArrayList<>(1);
//		lst.add(cd);
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 5000;// 此函数超时时间设定长一些
//		// 徐铭康修改
//		WriteCardListBySequence_Parameter par = new WriteCardListBySequence_Parameter(dt, lst);
//		// WriteCardListBySequence cmd = new WriteCardListBySequence(par);
//		FC8800Command cmd = new WriteCardListBySequence(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			WriteCardListBySequence_Result result = (WriteCardListBySequence_Result) y;
//			x.append("上传完毕");
//			ArrayList<? extends CardDetail> _list = result.CardList;
//			if (result.FailTotal > 0) {
//				x.append("失败数量：");
//				x.append(result.FailTotal);
//				x.append("，卡号列表：\n");
//				for (CardDetail c : _list) {
//					x.append(c.GetCardData());
//					x.append("\n");
//				}
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butUploadCardActionPerformed
//	// GEN-FIRST:event_butCardListClearActionPerformed
//	private void butCardListClearActionPerformed(java.awt.event.ActionEvent evt) {
//		ClearCardList();
//		mCardList.clear();
//		mCardList = null;
//	}
//	// GEN-LAST:event_butCardListClearActionPerformed
//	// GEN-FIRST:event_butAddCardToListActionPerformed
//	private void butAddCardToListActionPerformed(java.awt.event.ActionEvent evt) {
//		CardDetail cd = GetCardDetail();
//		if (cd == null) {
//			return;
//		}
//		if (mCardList == null) {
//			mCardList = new ArrayList<>(1000);
//		}
//		// 检查重复
//		int iIndex = mCardList.indexOf(cd);
//		if (iIndex > -1) {
//			mCardList.remove(iIndex);
//		}
//		mCardList.add(cd);
//		if (iIndex > -1) {
//			FillCardToList();// 刷新列表
//		}
//		else {
//			Object[] row = CardDetailToRow(cd, mCardList.size());
//			DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
//			tableModel.addRow(row);
//		}
//	}
//	// GEN-LAST:event_butAddCardToListActionPerformed
//	// GEN-FIRST:event_butReadCardDetailActionPerformed
//	private void butReadCardDetailActionPerformed(java.awt.event.ActionEvent evt) {
//		CardDetail cd = GetCardDetail();
//		if (cd == null) {
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 5000;// 此函数超时时间设定长一些
//		ReadCardDetail_Parameter par = new ReadCardDetail_Parameter(dt, cd.GetCardData());
//		// ReadCardDetail cmd = new ReadCardDetail(par);
//		FC8800Command cmd = new ReadCardDetail(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadCardDetail_Result result = (ReadCardDetail_Result) y;
//			if (result.IsReady) {
//				CardDetail card = (CardDetail) result.Card;
//				x.append("卡片在数据库中存储，卡片信息：\n");
//				Object[] arr = CardDetailToRow(card, 0);
//				StringBuilder builder = new StringBuilder(200);
//				builder.append("卡号：");
//				builder.append(arr[1]);// cd.CardData
//				builder.append("，密码：");
//				builder.append(arr[2]);// cd.Password
//				builder.append("，有效期：");
//				builder.append(arr[3]);// cd.Expiry
//				builder.append("\n有效次数：");
//				builder.append(arr[5]);// OpenTimes
//				builder.append("，特权：");
//				builder.append(arr[6]);// Privilege
//				builder.append("，卡状态：");
//				builder.append(arr[4]);// cd.CardStatus
//				builder.append("权限和时段：\n");
//				int arrIndex = 7;
//				for (int i = 1; i <= 4; i++) {
//					builder.append("门");
//					builder.append(i);
//					builder.append("：");
//					builder.append(arr[arrIndex]);
//					arrIndex++;
//					builder.append("权限");
//					builder.append(",开门时段:");
//					builder.append(arr[arrIndex]);
//					arrIndex++;
//					builder.append("；");
//				}
//				builder.append("\n节假日限制:");
//				builder.append(arr[arrIndex]);
//				x.append(builder);
//				builder = null;
//			}
//			else {
//				x.append("卡片未在数据库中存储！");
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butReadCardDetailActionPerformed
//	// GEN-FIRST:event_butClearCardDataBaseActionPerformed
//	private void butClearCardDataBaseActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 5000;
//		int iType = cmbCardDataBaseType.getSelectedIndex() + 1;
//		ClearCardDataBase_Parameter par = new ClearCardDataBase_Parameter(dt, iType);
//		ClearCardDataBase cmd = new ClearCardDataBase(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadCardDatabaseDetail_Result result = (ReadCardDatabaseDetail_Result) y;
//			x.append("清空区域：");
//			x.append(cmbCardDataBaseType.getSelectedItem());
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butClearCardDataBaseActionPerformed
//	// GEN-FIRST:event_butReadCardDatabaseDetailActionPerformed
//	private void butReadCardDatabaseDetailActionPerformed(java.awt.event.ActionEvent evt) {
//		// 读取控制器中的卡片数据库信息
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		ReadCardDatabaseDetail cmd = new ReadCardDatabaseDetail(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadCardDatabaseDetail_Result result = (ReadCardDatabaseDetail_Result) y;
//			x.append("\n排序区卡容量：");
//			x.append(result.SortDataBaseSize);
//			x.append("；排序区卡数量：");
//			x.append(result.SortCardSize);
//			x.append("\n非排序区卡容量：");
//			x.append(result.SequenceDataBaseSize);
//			x.append("；非排序区卡数量：");
//			x.append(result.SequenceCardSize);
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butReadCardDatabaseDetailActionPerformed
//	// GEN-FIRST:event_butReadCardDataBaseActionPerformed
//	private void butReadCardDataBaseActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 2000;
//		int iType = cmbCardDataBaseType.getSelectedIndex() + 1;
//		ReadCardDataBase_Parameter par = new ReadCardDataBase_Parameter(dt, iType);
//		// 徐铭康修改
//		// ReadCardDataBase cmd = new ReadCardDataBase(par);
//		ReadCardDataBase cmd = new ReadCardDataBase(par);
//		String[] CardTypeList = new String[]{"", "排序区", "非排序区", "所有区域"};
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadCardDataBase_Result result = (ReadCardDataBase_Result) y;
//			x.append("读取卡片数量：");
//			x.append(result.DataBaseSize);
//			x.append("；读取卡存储区域：");
//			x.append(CardTypeList[result.CardType]);
//			if (result.DataBaseSize > 0) {
//				mCardList = result.CardList;
//				String CardStatusList[] = new String[]{"正常", "挂失卡", "黑名单"};
//				DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
//				tableModel.setRowCount(0);// 清除原有行
//				int Index = 1;
//				StringBuilder builder = new StringBuilder(result.DataBaseSize * 140);
//				for (CardDetail cd : mCardList) {
//					Object[] arr = CardDetailToRow(cd, Index);
//					builder.append("卡号：");
//					builder.append(arr[1]);// cd.CardData
//					builder.append("，密码：");
//					builder.append(arr[2]);// cd.Password
//					builder.append("，有效期：");
//					builder.append(arr[3]);// cd.Expiry
//					builder.append("，有效次数：");
//					builder.append(arr[5]);// OpenTimes
//					builder.append("，特权：");
//					builder.append(arr[6]);// Privilege
//					builder.append("，卡状态：");
//					builder.append(arr[4]);// cd.CardStatus
//					builder.append("权限和时段：");
//					int arrIndex = 7;
//					for (int i = 1; i <= 4; i++) {
//						builder.append("门");
//						builder.append(i);
//						builder.append("：");
//						builder.append(arr[arrIndex]);
//						arrIndex++;
//						builder.append("权限");
//						builder.append(",开门时段:");
//						builder.append(arr[arrIndex]);
//						arrIndex++;
//						builder.append("；");
//					}
//					builder.append("节假日限制:");
//					builder.append(arr[arrIndex]);
//					builder.append("\n");
//					// 添加数据到表格
//					tableModel.addRow(arr);
//					Index += 1;
//				}
//				// 更新表格
//				tblCard.invalidate();
//				try {
//					String file = WriteFile("CardDatabase", builder, false);
//					if (file == null) {
//						x.append("\n卡数据导出失败！");
//					}
//					else {
//						x.append("\n卡数据导出成功，地址：");
//						x.append(file);
//					}
//				}
//				catch (Exception e) {
//				}
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butReadCardDataBaseActionPerformed
//	// GEN-FIRST:event_jButton2ActionPerformed
//	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
//		// TODO add your handling code here:
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		CommandParameter par = new CommandParameter(dt);
//		WriteTime cmd = new WriteTime(par);
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_jButton2ActionPerformed
//	// GEN-FIRST:event_jButton1ActionPerformed
//	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
//		// TODO add your handling code here:
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		CommandParameter par = new CommandParameter(dt);
//		ReadTime cmd = new ReadTime(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			x.append("控制器时间：");
//			ReadTime_Result result = (ReadTime_Result) y;
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			x.append(sdf.format(result.ControllerDate.getTime()));
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_jButton1ActionPerformed
//	/**
//	 * 自动搜索控制器
//	 * @param evt
//	 */
//	private void butAutoSearchDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butAutoSearchDoorActionPerformed
//		// 自动搜索门禁控制器
//		CommandDetail dt = new CommandDetail();
//		UDPDetail udp = new UDPDetail("255.255.255.255", 8101);
//		udp.LocalIP = "192.168.1.138";
//		udp.LocalPort = 8886;
//		udp.Broadcast = true;
//		dt.Connector = udp;
//		dt.Identity = new FC8800Identity("0000000000000000", FC8800Command.NULLPassword, E_ControllerType.FC8900);
//		Random rnd = new Random();
//		int max = 65535;
//		int min = 10000;
//		// 搜索时，不需要设定重发
//		dt.RestartCount = 0;
//		dt.Timeout = 5000;// 每隔5秒发送一次，所以这里设定5秒超时
//		// 网络标记就是一个随机数
//		SearchNetFlag = rnd.nextInt(max) % (max - min + 1) + min;// 网络标记
//		SearchTimes = 1;// 搜索次数;
//		SearchEquptOnNetNum_Parameter par = new SearchEquptOnNetNum_Parameter(dt, SearchNetFlag);
//		SearchEquptOnNetNum cmd = new SearchEquptOnNetNum(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			SearchEquptOnNetNum_Result result = (SearchEquptOnNetNum_Result) y;
//			x.append("\n第");
//			x.append(SearchTimes);
//			x.append("次搜索，本次搜索到控制器：");
//			x.append(result.SearchTotal);
//			x.append("个。");
//			if (result.SearchTotal > 0) {
//				x.append("\n");
//				PrintSearchDoor(result.ResultList, x);
//				// 将已搜索到的控制器设定网络标志，防止下次再搜索到
//				SetDoorNetFlag(result.ResultList);
//			}
//			if (SearchTimes < 4) {
//				SearchEquptOnNetNum_Parameter par1 = new SearchEquptOnNetNum_Parameter(dt, SearchNetFlag);
//				SearchEquptOnNetNum cmd1 = new SearchEquptOnNetNum(par1);
//				_Allocator.AddCommand(cmd1);
//			}
//			else {
//				x.append("\n*************搜索结束！*******************");
//				return;
//			}
//			SearchTimes++;
//		});
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butAutoSearchDoorActionPerformed
//	/**
//	 * 远程常开
//	 * @param evt
//	 */
//	private void butHoldDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butHoldDoorActionPerformed
//		// 远程常开
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
//		par.Door.SetDoor(1, 1);// 设定1号门执行操作
//		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
//		HoldDoor cmd = new HoldDoor(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butHoldDoorActionPerformed
//	private void butUnlockDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butUnlockDoorActionPerformed
//		// 远程解除锁定
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
//		par.Door.SetDoor(1, 1);// 设定1号门执行操作
//		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
//		UnlockDoor cmd = new UnlockDoor(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butUnlockDoorActionPerformed
//	private void butLockDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butLockDoorActionPerformed
//		// 远程锁定
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
//		par.Door.SetDoor(1, 1);// 设定1号门执行操作
//		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
//		LockDoor cmd = new LockDoor(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butLockDoorActionPerformed
//	/**
//	 * 远程关门
//	 * @param evt
//	 */
//	private void butCloseDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butCloseDoorActionPerformed
//		// 远程关门
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
//		par.Door.SetDoor(1, 1);// 设定1号门执行操作
//		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
//		CloseDoor cmd = new CloseDoor(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butCloseDoorActionPerformed
//	/**
//	 * 远程开门
//	 * @param evt
//	 */
//	private void butOpenDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butOpenDoorActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		OpenDoor_Parameter par = new OpenDoor_Parameter(dt);
//		par.Door.SetDoor(1, 1);// 设定1号门执行操作
//		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
//		OpenDoor cmd = new OpenDoor(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butOpenDoorActionPerformed
//	/**
//	 * 解除报警，可解除所有报警
//	 * @param evt
//	 */
//	private void butCloseAlarmActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butCloseAlarmActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CloseAlarm_Parameter par = new CloseAlarm_Parameter(dt);
//		par.Alarm.Alarm = 65535;// 解除所有报警
//		CloseAlarm cmd = new CloseAlarm(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butCloseAlarmActionPerformed
//	private void butCloseWatchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butCloseWatchActionPerformed
//		// 关闭数据监控
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		CloseWatch cmd = new CloseWatch(par);
//		_Allocator.CloseForciblyConnect(dt.Connector);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butCloseWatchActionPerformed
//	private void butBeginWatchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butBeginWatchActionPerformed
//		// 打开数据监控
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		BeginWatch cmd = new BeginWatch(par);
//		_Allocator.OpenForciblyConnect(dt.Connector);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butBeginWatchActionPerformed
//	private void butReadSNActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadSNActionPerformed
//		// TODO add your handling code here:
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ReadSN(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			x.append("控制器SN：");
//			ReadSN_Result result = (ReadSN_Result) y;
//			x.append(result.SN);
//			txtWriteSN.setText(result.SN);
//			txtSN.setText(result.SN);
//		});
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butReadSNActionPerformed
//	private void butWriteSNActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butWriteSNActionPerformed
//		String sSN = txtWriteSN.getText();
//		if (sSN.length() != 16) {
//			JOptionPane.showMessageDialog(null, "SN必须是16位！", "错误", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		WriteSN_Parameter par = new WriteSN_Parameter(dt, sSN);
//		INCommand cmd = new WriteSN(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			x.append("控制器SN：");
//			String sSN1 = txtWriteSN.getText();
//			x.append(sSN1);
//			txtSN.setText(sSN1);
//		});
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butWriteSNActionPerformed
//	private void butReadVersionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadVersionActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ReadVersion(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadVersion_Result result = (ReadVersion_Result) y;
//			x.append("控制板固件版本号：");
//			x.append(result.Version);
//		});
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butReadVersionActionPerformed
//	private void ButWriteDeadlineActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButWriteDeadlineActionPerformed
//		CommandDetail dt = getCommandDetail();
//		WriteDeadline_Parameter par = new WriteDeadline_Parameter(dt, 1000);
//		INCommand cmd = new WriteDeadline(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_ButWriteDeadlineActionPerformed
//	private void ButReadDeadlineActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButReadDeadlineActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ReadDeadline(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadDeadline_Result result = (ReadDeadline_Result) y;
//			if (result.Deadline == 0) {
//				x.append("控制板已失效！");
//			}
//			else if (result.Deadline == 65535) {
//				x.append("控制板永久有效！");
//			}
//			else {
//				x.append("控制板剩余有效天数：");
//				x.append(result.Deadline);
//				x.append("天.");
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_ButReadDeadlineActionPerformed
//	private void butWriteTCPSettingActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butWriteTCPSettingActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		if (RadUDP.isSelected()) {
//			dt.Timeout = 15000;// UDP延迟增加
//		}
//		if (mReadTCPDetail == null) {
//			JOptionPane.showMessageDialog(null, "请先读取TCP参数！", "错误", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		mReadTCPDetail.SetServerIP("0.0.0.0");
//		mReadTCPDetail.SetServerAddr("www.123.cn");
//		WriteTCPSetting_Parameter par = new WriteTCPSetting_Parameter(dt, mReadTCPDetail);
//		INCommand cmd = new WriteTCPSetting(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butWriteTCPSettingActionPerformed
//	private void butReadTCPSettingActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadTCPSettingActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		if (RadUDP.isSelected()) {
//			// UDP延迟增加
//			dt.Timeout = 15000;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ReadTCPSetting(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadTCPSetting_Result result = (ReadTCPSetting_Result) y;
//			TCPDetail tcp = result.TCP;
//			PirntTCPDetail(tcp, x);
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butReadTCPSettingActionPerformed
//	private void butResetConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butResetConnectPasswordActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ResetConnectPassword(par);
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butResetConnectPasswordActionPerformed
//	private void butWriteConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butWriteConnectPasswordActionPerformed
//		// String pwd = txtPassword.getText();
//		String pwd = "12345678";
//		if (pwd.length() != 8) {
//			JOptionPane.showMessageDialog(null, "通讯密码必须是8位！", "错误", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		WriteConnectPassword_Parameter par = new WriteConnectPassword_Parameter(dt, pwd);
//		INCommand cmd = new WriteConnectPassword(par);
//		_Allocator.AddCommand(cmd);
//	}
//	// GEN-LAST:event_butWriteConnectPasswordActionPerformed
//	private void butReadConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadConnectPasswordActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ReadConnectPassword(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			x.append("通讯密码：");
//			ReadConnectPassword_Result result = (ReadConnectPassword_Result) y;
//			x.append(result.Password);
//			txtPassword.setText(result.Password);
//		});
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butReadConnectPasswordActionPerformed
//	private TCPDetail mReadTCPDetail;
//	private void PirntTCPDetail(TCPDetail tcp, StringBuilder x) {
//		x.append("\nMAC：");
//		x.append(tcp.GetMAC());
//		x.append("\n");
//		x.append("IP：");
//		x.append(tcp.GetIP());
//		x.append(",子网掩码：");
//		x.append(tcp.GetIPMask());
//		x.append(",网关：");
//		x.append(tcp.GetIPGateway());
//		x.append("\n");
//		x.append("DNS1：");
//		x.append(tcp.GetDNS());
//		x.append(",DNS2：");
//		x.append(tcp.GetDNSBackup());
//		x.append("\n");
//		x.append("TCP端口：");
//		x.append(tcp.GetTCPPort());
//		x.append(",UDP端口：");
//		x.append(tcp.GetUDPPort());
//		x.append("\n");
//		x.append("服务器IP：");
//		x.append(tcp.GetServerIP());
//		x.append(",端口：");
//		x.append(tcp.GetServerPort());
//		x.append("\n");
//		x.append("服务器域名：");
//		x.append(tcp.GetServerAddr());
//	}
//	private void IniCardDataBase() {
//		cmbCardDataBaseType.removeAllItems();
//		cmbCardDataBaseType.addItem("排序卡区域");
//		cmbCardDataBaseType.addItem("非排序卡区域");
//		cmbCardDataBaseType.addItem("所有区域");
//		DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
//		String Cols[] = "序号，卡号，密码，有效期，卡片状态，有效次数，特权，门1权限，门1时段，门2权限，门2时段，门3权限，门3时段，门4权限，门4时段，节假日限制".split("，");
//		// tblCard.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		tblCard.setColumnSelectionAllowed(true);
//		int ColWidth[] = new int[]{40, 70, 60, 110, 60, 60, 70, 55, 55, 55, 55, 55, 55, 55, 55, 70};
//		for (String Col : Cols) {
//			tableModel.addColumn(Col);
//		}
//		for (int i = 0; i < ColWidth.length; i++) {
//			TableColumn Column = tblCard.getColumnModel().getColumn(i);
//			Column.setMinWidth(ColWidth[i]);
//			Column.setMaxWidth(150);
//			Column.setPreferredWidth(ColWidth[i]);
//		}
//		// 设置table内容居中
//		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
//		tcr.setHorizontalAlignment(SwingConstants.CENTER);
//		tblCard.setDefaultRenderer(Object.class, tcr);
//		// 设置table表头居中
//		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tblCard.getTableHeader().getDefaultRenderer();
//		hr.setHorizontalAlignment(SwingConstants.CENTER);
//		//
//		tblCard.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		tblCard.getTableHeader().setReorderingAllowed(false); // 不可整列移动
//		// tblCard.getTableHeader().setResizingAllowed(false); //不可拉动表格
//		// tableModel.addRow(new Object[]{1, "0000000000", "00000000", "2017-11-14
//		// 12:30", "黑名单", "无限制", "防盗设置卡", "有", "64", "有", "64", "有", "64", "有", "64",
//		// "无"});
//		tblCard.invalidate();
//		tblCard.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				tblCard_mouseClicked();
//			}
//		});
//	}
//	private void tblCard_mouseClicked() {
//		int numRow = tblCard.getSelectedRow();
//		javax.swing.table.TableModel model = tblCard.getModel();
//		long Card = Long.parseLong(model.getValueAt(numRow, 1).toString());
//		CardDetail cd = new CardDetail();
//		try {
//			cd.SetCardData(String.valueOf(Card));
//		}
//		catch (Exception e) {
//		}
//		int Index = mCardList.indexOf(cd);
//		if (Index == -1) {
//			return;
//		}
//		cd = mCardList.get(Index);
//		// 序号，卡号，密码，有效期，卡片状态，有效次数，特权，门1权限，门1时段，门2权限，门2时段，门3权限，门3时段，门4权限，门4时段，节假日限制
//		txtCardData.setText(model.getValueAt(numRow, 1).toString());
//		txtCardPassword.setText(model.getValueAt(numRow, 2).toString());
//		TxtCardExpiry.setText(model.getValueAt(numRow, 3).toString());
//		txtOpenTimes.setText(model.getValueAt(numRow, 5).toString());
//		cmbCardStatus.setSelectedIndex(cd.CardStatus);
//		JCheckBox[] doors = new JCheckBox[]{chkCardDoor1, chkCardDoor2, chkCardDoor3, chkCardDoor4};
//		for (int i = 1; i <= 4; i++) {
//			doors[i - 1].setSelected(cd.GetDoor(i));
//		}
//	}
//	private String WriteFile(String sFileName, StringBuilder strBuf, boolean append) {
//		File[] roots = File.listRoots();
//		String Path = "d:\\";
//		for (int i = 1; i < roots.length; i++) {
//			if (roots[i].getFreeSpace() > 0) {
//				Path = roots[i].getPath();
//				break;
//			}
//		}
//		Path += sFileName + ".txt";
//		try {
//			File file = new File(Path);
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//			else {
//				if (!append) {
//					file.delete();
//					file.createNewFile();
//				}
//			}
//			FileOutputStream out = new FileOutputStream(file, true);
//			out.write(strBuf.toString().getBytes("utf-8"));
//			out.close();
//		}
//		catch (Exception e) {
//			return null;
//		}
//		return Path;
//	}
//	private String GetBooleanStr(boolean v) {
//		if (v) {
//			return "有";
//		}
//		else {
//			return "无";
//		}
//	}
//	private ArrayList<CardDetail> mCardList;
//	private void ClearCardList() {
//		DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
//		tableModel.setRowCount(0);// 清除原有行
//	}
//	private void FillCardToList() {
//		if (mCardList == null) {
//			return;
//		}
//		if (mCardList.size() == 0) {
//			return;
//		}
//		DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
//		ClearCardList();
//		int Index = 1;
//		for (CardDetail cd : mCardList) {
//			Object[] row = CardDetailToRow(cd, Index);
//			// 添加数据到表格
//			tableModel.addRow(row);
//			Index += 1;
//		}
//		// 更新表格
//		tblCard.invalidate();
//	}
//	String CardStatusList[] = new String[]{"正常", "挂失卡", "黑名单"};
//	String PrivilegeList[] = new String[]{"无", "首卡特权卡", "常开特权卡", "巡更卡", "防盗设置卡"};
//	String OpenTimesUnlimited = "无限制";
//	private Object[] CardDetailToRow(CardDetail cd, int Index) {
//		String OpenTimes;// 有效期
//		String Privilege = PrivilegeList[0];// "无";//特权
//		if (cd.OpenTimes == 65535) {
//			OpenTimes = OpenTimesUnlimited;// "无限制";
//		}
//		else {
//			OpenTimes = String.valueOf(cd.OpenTimes);
//		}
//		if (cd.IsPrivilege()) {
//			Privilege = PrivilegeList[1];// "首卡特权卡";
//		}
//		else if (cd.IsTiming()) {
//			Privilege = PrivilegeList[2];// "常开特权卡";
//		}
//		else if (cd.IsGuardTour()) {
//			Privilege = PrivilegeList[3];// "巡更卡";
//		}
//		else if (cd.IsAlarmSetting()) {
//			Privilege = PrivilegeList[4];// "防盗设置卡";
//		}
//		Object[] row = new Object[]{Index, cd.GetCardData(), cd.Password.replaceAll("F", ""), TimeUtil.FormatTimeHHmm(cd.Expiry), CardStatusList[cd.CardStatus], OpenTimes, Privilege, GetBooleanStr(cd.GetDoor(1)), cd.GetTimeGroup(1), GetBooleanStr(cd.GetDoor(2)), cd.GetTimeGroup(2), GetBooleanStr(cd.GetDoor(3)), cd.GetTimeGroup(3), GetBooleanStr(cd.GetDoor(4)), cd.GetTimeGroup(4), GetBooleanStr(cd.HolidayUse)};
//		return row;
//	}
//	private CardDetail GetCardDetail() {
//		String Card = txtCardData.getText();
//		String Password = txtCardPassword.getText();
//		String Expiry = TxtCardExpiry.getText();
//		String OpenTimes = txtOpenTimes.getText();
//		int CardStatus = cmbCardStatus.getSelectedIndex();
//		int iOpenTimes = 0;
//		Calendar CardExpiry = Calendar.getInstance();
//		long CardData = 0;
//		if (StringUtil.IsNullOrEmpty(Card)) {
//			JOptionPane.showMessageDialog(null, "卡号不能为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (!StringUtil.IsNum(Card) || Card.length() > 10) {
//			JOptionPane.showMessageDialog(null, "卡号为1-10位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		CardData = Long.valueOf(Card);
//		if (CardData > UInt32Util.UINT32_MAX || CardData == 0) {
//			JOptionPane.showMessageDialog(null, "卡号不能大于4294967295！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (!StringUtil.IsNullOrEmpty(Password)) {
//			if (Password.length() > 8 || Password.length() < 4 || !StringUtil.IsNum(Password)) {
//				JOptionPane.showMessageDialog(null, "密码为4-8位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//				return null;
//			}
//		}
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//		try {
//			Date dt1 = df.parse(Expiry);
//			CardExpiry.setTime(dt1);
//			dt1 = CardExpiry.getTime();
//		}
//		catch (Exception exception) {
//			JOptionPane.showMessageDialog(null, "截止日期格式不正确（yyyy-MM-dd hh:mm）！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (OpenTimes.equals(OpenTimesUnlimited)) {
//			OpenTimes = "65535";
//		}
//		if (!StringUtil.IsNum(OpenTimes) || OpenTimes.length() > 5) {
//			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		iOpenTimes = Integer.valueOf(OpenTimes);
//		if (iOpenTimes > 65535) {
//			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		JCheckBox[] doors = new JCheckBox[]{chkCardDoor1, chkCardDoor2, chkCardDoor3, chkCardDoor4};
//		CardDetail cd = new CardDetail();// 设定卡号
//		try {
//			cd.SetCardData(Card);
//		}
//		catch (Exception e) {
//		}
//		cd.Password = Password;// 设定密码
//		cd.Expiry = CardExpiry;// 设定有效期
//		cd.OpenTimes = iOpenTimes;// 开门次数
//		cd.CardStatus = (byte) CardStatus;
//		// 设定4个门的开门时段
//		for (int i = 1; i <= 4; i++) {
//			cd.SetTimeGroup(i, 1);// 每个门都设定为1门
//			cd.SetDoor(i, doors[i - 1].isSelected());// 设定每个门权限
//		}
//		cd.SetNormal();// 设定卡片没有特权
//		cd.HolidayUse = true;// 设定受节假日限制。
//		return cd;
//	}
//	private Net.PC15.FC89H.Command.Data.CardDetail GetFC89HCardDetail() {
//		String Card = txtCardData.getText();
//		String Password = txtCardPassword.getText();
//		String Expiry = TxtCardExpiry.getText();
//		String OpenTimes = txtOpenTimes.getText();
//		int CardStatus = cmbCardStatus.getSelectedIndex();
//		int iOpenTimes = 0;
//		Calendar CardExpiry = Calendar.getInstance();
//		long CardData = 0;
//		if (StringUtil.IsNullOrEmpty(Card)) {
//			JOptionPane.showMessageDialog(null, "卡号不能为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (!StringUtil.IsNum(Card) || Card.length() > 10) {
//			JOptionPane.showMessageDialog(null, "卡号为1-10位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		CardData = Long.valueOf(Card);
//		if (CardData > UInt32Util.UINT32_MAX || CardData == 0) {
//			JOptionPane.showMessageDialog(null, "卡号不能大于4294967295！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (!StringUtil.IsNullOrEmpty(Password)) {
//			if (Password.length() > 8 || Password.length() < 4 || !StringUtil.IsNum(Password)) {
//				JOptionPane.showMessageDialog(null, "密码为4-8位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//				return null;
//			}
//		}
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//		try {
//			Date dt1 = df.parse(Expiry);
//			CardExpiry.setTime(dt1);
//			dt1 = CardExpiry.getTime();
//		}
//		catch (Exception exception) {
//			JOptionPane.showMessageDialog(null, "截止日期格式不正确（yyyy-MM-dd hh:mm）！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (OpenTimes.equals(OpenTimesUnlimited)) {
//			OpenTimes = "65535";
//		}
//		if (!StringUtil.IsNum(OpenTimes) || OpenTimes.length() > 5) {
//			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		iOpenTimes = Integer.valueOf(OpenTimes);
//		if (iOpenTimes > 65535) {
//			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		JCheckBox[] doors = new JCheckBox[]{chkCardDoor1, chkCardDoor2, chkCardDoor3, chkCardDoor4};
//		Net.PC15.FC89H.Command.Data.CardDetail cd = new Net.PC15.FC89H.Command.Data.CardDetail();// 设定卡号
//		cd.Password = Password;// 设定密码
//		cd.Expiry = CardExpiry;// 设定有效期
//		cd.OpenTimes = iOpenTimes;// 开门次数
//		cd.CardStatus = (byte) CardStatus;
//		// 设定4个门的开门时段
//		for (int i = 1; i <= 4; i++) {
//			cd.SetTimeGroup(i, 1);// 每个门都设定为1门
//			cd.SetDoor(i, doors[i - 1].isSelected());// 设定每个门权限
//		}
//		cd.SetNormal();// 设定卡片没有特权
//		cd.HolidayUse = true;// 设定受节假日限制。
//		return cd;
//	}
//	/**
//	 * 打印记录详情
//	 * @param dt
//	 * @param buf
//	 */
//	private void PrintTransactionDatabaseDetail(String TransactionName, TransactionDetail dt, StringBuilder buf) {
//		buf.append("\n");
//		buf.append(TransactionName);
//		buf.append("：容量：");
//		buf.append(dt.DataBaseMaxSize);
//		buf.append("：可读数量：");
//		buf.append(dt.readable());
//		buf.append("：写索引：");
//		buf.append(dt.WriteIndex);
//		buf.append("：读索引：");
//		buf.append(dt.ReadIndex);
//		buf.append("：循环存储：");
//		buf.append(dt.IsCircle);
//	}
//	/**
//	 * 打印记录日志
//	 * @param TransactionList
//	 * @param log
//	 */
//	private void PrintTransactionDatabase(ArrayList<AbstractTransaction> TransactionList, StringBuilder log, String[] sTransactionList) {
//		int Quantity = TransactionList.size();
//		int code = 0;
//		String sCode = null;
//		for (int i = 0; i < Quantity; i++) {
//			AbstractTransaction Transaction = TransactionList.get(i);
//			log.append("序号：");
//			log.append(Transaction.SerialNumber);
//			if (!Transaction.IsNull()) {
//				code = Transaction.TransactionCode();
//				sCode = null;
//				log.append("，时间：");
//				log.append(TimeUtil.FormatTime(Transaction.TransactionDate()));
//				log.append("，事件：");// 类型
//				log.append(code);
//				if (code < 255) {
//					sCode = sTransactionList[code];
//					if (StringUtil.IsNullOrEmpty(sCode)) {
//						sCode = "未知类型";
//					}
//				}
//				else {
//					sCode = "未知类型";
//				}
//				log.append("--");
//				log.append(sCode);
//				if (Transaction instanceof AbstractDoorTransaction) {
//					AbstractDoorTransaction dt = (AbstractDoorTransaction) Transaction;
//					if (dt.Door != 255) {
//						log.append("，门号：");
//						log.append(dt.Door);
//					}
//				}
//				if (Transaction instanceof CardTransaction) {
//					CardTransaction cardTrn = (CardTransaction) Transaction;
//					log.append("，卡号：");
//					log.append(cardTrn.CardData);
//					log.append("，门号：");
//					log.append(cardTrn.DoorNum());
//					if (cardTrn.IsEnter()) {
//						log.append("，进门读卡");
//					}
//					else {
//						log.append("，出门读卡");
//					}
//				}
//			}
//			log.append("\n");
//		}
//	}
//	private int SearchTimes = 0;
//	private int SearchNetFlag;
//	private void PrintSearchDoor(ArrayList<SearchEquptOnNetNum_Result.SearchResult> lst, StringBuilder log) {
//		ByteBuf tmpBuf = ByteUtil.ALLOCATOR.buffer(256, 300);
//		TCPDetail td = new TCPDetail();
//		for (SearchEquptOnNetNum_Result.SearchResult r : lst) {
//			log.append("SN：");
//			log.append(r.SN);
//			if (r.ResultData != null) {
//				log.append("，附带数据：");
//				log.append(ByteUtil.ByteToHex(r.ResultData));
//				if (r.ResultData.length == 0x89) {
//					tmpBuf.readerIndex(0);
//					tmpBuf.writerIndex(0);
//					tmpBuf.writeBytes(r.ResultData);
//					td.SetBytes(tmpBuf);
//					;
//					PirntTCPDetail(td, log);
//				}
//			}
//			log.append("\n");
//		}
//	}
//	/**
//	 * 设定控制器的网络标志
//	 * @param lst
//	 */
//	private void SetDoorNetFlag(ArrayList<SearchEquptOnNetNum_Result.SearchResult> lst) {
//		// ByteBuf tmpBuf = ByteUtil.ALLOCATOR.buffer(256, 300);
//		// TCPDetail td = new TCPDetail();
//		for (SearchEquptOnNetNum_Result.SearchResult r : lst) {
//			/*
//			 * if (r.ResultData != null) {
//			 *
//			 * if (r.ResultData.length == 0x89) { tmpBuf.readerIndex(0);
//			 * tmpBuf.writerIndex(0); tmpBuf.writeBytes(r.ResultData); td.SetBytes(tmpBuf);
//			 *
//			 *
//			 *
//			 *
//			 * } }
//			 */
//			CommandDetail detail = new CommandDetail();
//			UDPDetail udp = new UDPDetail("255.255.255.255", 8101);
//			udp.Broadcast = true;
//			detail.Connector = udp;
//			detail.Identity = new FC8800Identity(r.SN, FC8800Command.NULLPassword, E_ControllerType.FC8800);
//			SearchEquptOnNetNum_Parameter par = new SearchEquptOnNetNum_Parameter(detail, SearchNetFlag);
//			WriteEquptNetNum cmd = new WriteEquptNetNum(par);
//			_Allocator.AddCommand(cmd);
//		}
//	}
//	// Variables declaration - do not modify//GEN-BEGIN:variables
//	private JButton ButReadDeadline;
//	private JButton ButWriteDeadline;
//	private JLabel LblDate;
//	private JPanel PnlUDP;
//	private JRadioButton RadTCPClient;
//	private JRadioButton RadTCPServer;
//	private JRadioButton RadUDP;
//	private JTextField TxtCardExpiry;
//	private ButtonGroup bgConnectType;
//	private JButton butAddCardToList;
//	private JButton butAutoSearchDoor;
//	private JButton butBeginServer;
//	private JButton butBeginWatch;
//	private JButton butCardListAutoCreate;
//	private JButton butCardListClear;
//	private JButton butClearCardDataBase;
//	private JButton butClearTransactionDatabase;
//	private JButton butCloseAlarm;
//	private JButton butCloseDoor;
//	private JButton butCloseWatch;
//	private JButton butDeleteCard;
//	private JButton butDeleteCardByList;
//	private JButton butHoldDoor;
//	private JButton butLockDoor;
//	private JButton butOpenDoor;
//	private JButton butReadCardDataBase;
//	private JButton butReadCardDatabaseDetail;
//	private JButton butReadCardDetail;
//	private JButton butReadConnectPassword;
//	private JButton butReadSN;
//	private JButton butReadTCPSetting;
//	private JButton butReadTransactionDatabase;
//	private JButton butReadTransactionDatabaseByIndex;
//	private JButton butReadTransactionDatabaseDetail;
//	private JButton butReadVersion;
//	private JButton butResetConnectPassword;
//	private JButton butTransactionDatabaseEmpty;
//	private JButton butUnlockDoor;
//	private JButton butUploadCard;
//	private JButton butWriteCardListBySequence;
//	private JButton butWriteCardListBySort;
//	private JButton butWriteConnectPassword;
//	private JButton butWriteSN;
//	private JButton butWriteTCPSetting;
//	private JButton butWriteTransactionDatabaseReadIndex;
//	private JButton butWriteTransactionDatabaseWriteIndex;
//	private JCheckBox chkCardDoor1;
//	private JCheckBox chkCardDoor2;
//	private JCheckBox chkCardDoor3;
//	private JCheckBox chkCardDoor4;
//	private JCheckBox chkTransactionIsCircle;
//	private JComboBox<String> cmbCardDataBaseType;
//	private JComboBox<String> cmbCardStatus;
//	private JComboBox<String> cmbTransactionType;
//	private JButton jButton1;
//	private JButton jButton2;
//	private JComboBox<String> jComboBox1;
//	private JLabel jLabel1;
//	private JLabel jLabel10;
//	private JLabel jLabel11;
//	private JLabel jLabel12;
//	private JLabel jLabel13;
//	private JLabel jLabel14;
//	private JLabel jLabel15;
//	private JLabel jLabel16;
//	private JLabel jLabel17;
//	private JLabel jLabel18;
//	private JLabel jLabel19;
//	private JLabel jLabel2;
//	private JLabel jLabel20;
//	private JLabel jLabel21;
//	private JLabel jLabel22;
//	private JLabel jLabel23;
//	private JLabel jLabel24;
//	private JLabel jLabel25;
//	private JLabel jLabel3;
//	private JLabel jLabel4;
//	private JLabel jLabel5;
//	private JLabel jLabel6;
//	private JLabel jLabel7;
//	private JLabel jLabel8;
//	private JLabel jLabel9;
//	private JPanel jPanel1;
//	private JPanel jPanel2;
//	private JPanel jPanel3;
//	private JPanel jPanel4;
//	private JPanel jPanel5;
//	private JPanel jPanel6;
//	private JPanel jPanel8;
//	private JPanel jPanel9;
//	private JScrollPane jScrollPane1;
//	private JScrollPane jScrollPane2;
//	private JTabbedPane jTabSetting;
//	private JTextArea jTextArea1;
//	private JPanel jpConnectSetting;
//	private JScrollPane jpLog;
//	private JPanel jpSNDetail;
//	private JLabel lblCommandName;
//	private JLabel lblTime;
//	private JPanel pnlTCPClient;
//	private JPanel pnlTCPServer;
//	private JProgressBar prCommand;
//	private JTable tblCard;
//	private JTextField txtCardAutoCreateSzie;
//	private JTextField txtCardData;
//	private JTextField txtCardPassword;
//	private JTextField txtLocalPort;
//	private JTextArea txtLog;
//	private JTextField txtOpenTimes;
//	private JTextField txtPassword;
//	private JTextField txtReadTransactionDatabaseByIndex;
//	private JTextField txtReadTransactionDatabaseByQuantity;
//	private JTextField txtReadTransactionDatabasePacketSize;
//	private JTextField txtReadTransactionDatabaseQuantity;
//	private JTextField txtSN;
//	private JTextField txtTCPServerIP;
//	private JTextField txtTCPServerPort;
//	private JTextField txtTransactionDatabaseReadIndex;
//	private JTextField txtTransactionDatabaseWriteIndex;
//	private JTextField txtUDPRemoteIP;
//	private JTextField txtUDPRemotePort;
//	private JTextField txtWriteSN;
//	// End of variables declaration//GEN-END:variables
//	private CommandDetail getCommandDetail() {
//		CommandDetail detail = new CommandDetail();
//		String ip = "0", strPort = "0";
//		if (RadTCPClient.isSelected()) {
//			ip = txtTCPServerIP.getText();
//			strPort = txtLocalPort.getText();
//		}
//		if (RadUDP.isSelected()) {
//			ip = txtUDPRemoteIP.getText();
//			strPort = txtUDPRemotePort.getText();
//		}
//		int iPort = Integer.valueOf(strPort);
//		if (ip.length() == 0) {
//			JOptionPane.showMessageDialog(null, "必须输入IP地址！", "错误", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (iPort <= 0 || iPort > 65535) {
//			JOptionPane.showMessageDialog(null, "TCP端口取值范围：1-65535！", "错误", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		detail.Connector = new TCPClientDetail(ip, iPort);
//		if (RadUDP.isSelected()) {
//			UDPDetail udpd = new UDPDetail(ip, iPort);
//			udpd.Broadcast = true;
//			// udpd.LocalPort = 10088;//设定本地绑定端口号
//			detail.Connector = udpd;
//		}
//		String sn, pwd;
//		sn = txtSN.getText();
//		if (sn.length() != 16) {
//			JOptionPane.showMessageDialog(null, "SN必须是16位！", "错误", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		pwd = txtPassword.getText();
//		if (pwd.length() != 8) {
//			JOptionPane.showMessageDialog(null, "通讯密码必须是8位！", "错误", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		detail.Identity = new FC8800Identity(sn, pwd, E_ControllerType.FC8800);
//		return detail;
//	}
//	private void AddCommandResultCallback(String sCommand, CommandResultCallback callback) {
//		if (!CommandResult.containsKey(sCommand)) {
//			CommandResult.put(sCommand, callback);
//		}
//		/*
//		 * else { CommandResult.remove(sCommand); CommandResult.put(sCommand, callback);
//		 * }
//		 */
//	}
//	private void IniCommandName() {
//		if (CommandName.size() > 0) {
//			return;
//		}
//		CommandName.put(WriteSN.class.getName(), "写控制器SN");
//		CommandName.put(WriteSN_Broadcast.class.getName(), "通过广播写SN");
//		CommandName.put(ReadSN.class.getName(), "从控制器中读取SN");
//		CommandName.put(WriteConnectPassword.class.getName(), "修改控制器通讯密码");
//		CommandName.put(ReadConnectPassword.class.getName(), "从控制器中获取通讯密码");
//		CommandName.put(ResetConnectPassword.class.getName(), "重置控制器通讯密码");
//		CommandName.put(ReadTCPSetting.class.getName(), "读取控制器TCP网络参数");
//		CommandName.put(WriteTCPSetting.class.getName(), "修改控制器TCP网络参数");
//		CommandName.put(ReadDeadline.class.getName(), "读取控制器有效期剩余天数");
//		CommandName.put(WriteDeadline.class.getName(), "写入控制器有效期");
//		CommandName.put(ReadVersion.class.getName(), "获取控制器版本号");
//		CommandName.put(ReadSystemStatus.class.getName(), "读取设备运行信息");
//		CommandName.put(ReadAllSystemSetting.class.getName(), "获取所有系统参数");
//		CommandName.put(ReadRecordMode.class.getName(), "读取记录存储方式");
//		CommandName.put(WriteRecordMode.class.getName(), "写入记录存储方式");
//		CommandName.put(ReadKeyboard.class.getName(), "读取读卡器密码键盘启用功能开关");
//		CommandName.put(WriteKeyboard.class.getName(), "写入读卡器密码键盘启用功能开关");
//		CommandName.put(ReadLockInteraction.class.getName(), "读取互锁功能开关");
//		CommandName.put(WriteLockInteraction.class.getName(), "写入互锁功能开关");
//		CommandName.put(ReadFireAlarmOption.class.getName(), "读取消防报警功能参数");
//		CommandName.put(WriteFireAlarmOption.class.getName(), "写入消防报警功能参数");
//		CommandName.put(ReadOpenAlarmOption.class.getName(), "读取匪警报警功能参数");
//		CommandName.put(WriteOpenAlarmOption.class.getName(), "写入匪警报警功能参数");
//		CommandName.put(ReadReaderIntervalTime.class.getName(), "读取读卡间隔时间参数");
//		CommandName.put(WriteReaderIntervalTime.class.getName(), "写入读卡间隔时间参数");
//		CommandName.put(ReadBroadcast.class.getName(), "读取语音段开关参数");
//		CommandName.put(WriteBroadcast.class.getName(), "写入语音段开关参数");
//		CommandName.put(ReadReaderCheckMode.class.getName(), "读取读卡器校验参数");
//		CommandName.put(WriteReaderCheckMode.class.getName(), "写入读卡器校验参数");
//		CommandName.put(ReadBuzzer.class.getName(), "读取主板蜂鸣器参数");
//		CommandName.put(WriteBuzzer.class.getName(), "写入主板蜂鸣器参数");
//		CommandName.put(ReadSmogAlarmOption.class.getName(), "读取烟雾报警功能参数");
//		CommandName.put(WriteSmogAlarmOption.class.getName(), "写入烟雾报警功能参数");
//		CommandName.put(ReadEnterDoorLimit.class.getName(), "读取门内人数上限参数");
//		CommandName.put(WriteEnterDoorLimit.class.getName(), "写入门内人数上限参数");
//		CommandName.put(ReadTheftAlarmSetting.class.getName(), "读取智能防盗主机参数");
//		CommandName.put(WriteTheftAlarmSetting.class.getName(), "写入智能防盗主机参数");
//		CommandName.put(ReadCheckInOut.class.getName(), "读取防潜回参数");
//		CommandName.put(WriteCheckInOut.class.getName(), "写入防潜回参数");
//		CommandName.put(ReadCardPeriodSpeak.class.getName(), "读取卡片到期提示参数");
//		CommandName.put(WriteCardPeriodSpeak.class.getName(), "写入卡片到期提示参数");
//		CommandName.put(ReadReadCardSpeak.class.getName(), "读取定时读卡播报语音参数");
//		CommandName.put(WriteReadCardSpeak.class.getName(), "写入定时读卡播报语音参数");
//		CommandName.put(BeginWatch.class.getName(), "开始数据监控");
//		CommandName.put(BeginWatch_Broadcast.class.getName(), "广播开启监控");
//		CommandName.put(CloseWatch.class.getName(), "关闭数据监控");
//		CommandName.put(CloseWatch_Broadcast.class.getName(), "广播关闭监控");
//		CommandName.put(SendFireAlarm.class.getName(), "通知设备触发消防报警");
//		CommandName.put(CloseFireAlarm.class.getName(), "解除消防报警");
//		CommandName.put(ReadFireAlarmState.class.getName(), "读取消防报警状态");
//		CommandName.put(SendSmogAlarm.class.getName(), "通知设备触发烟雾报警");
//		CommandName.put(CloseSmogAlarm.class.getName(), "解除烟雾报警");
//		CommandName.put(ReadSmogAlarmState.class.getName(), "获取烟雾报警状态");
//		CommandName.put(CloseAlarm.class.getName(), "解除报警");
//		CommandName.put(ReadWorkStatus.class.getName(), "获取控制器各端口工作状态信息");
//		CommandName.put(ReadTheftAlarmState.class.getName(), "获取防盗主机布防状态");
//		CommandName.put(FormatController.class.getName(), "初始化控制器");
//		CommandName.put(SearchEquptOnNetNum.class.getName(), "搜索控制器");
//		CommandName.put(WriteEquptNetNum.class.getName(), "根据SN设置网络标记");
//		CommandName.put(WriteKeepAliveInterval.class.getName(), "写入保活间隔时间");
//		CommandName.put(ReadKeepAliveInterval.class.getName(), "读取保活间隔时间");
//		CommandName.put(SetTheftDisarming.class.getName(), "防盗报警撤防");
//		CommandName.put(SetTheftFortify.class.getName(), "防盗报警布防");
//		CommandName.put(WriteBalcklistAlarmOption.class.getName(), "写入黑名单报警功能开关");
//		CommandName.put(ReadBalcklistAlarmOption.class.getName(), "读取黑名单报警功能开关");
//		CommandName.put(ReadExploreLockMode.class.getName(), "读取防探测功能开关");
//		CommandName.put(WriteExploreLockMode.class.getName(), "写入防探测功能开关");
//		CommandName.put(ReadCheck485Line.class.getName(), "读取485线路反接检测开关");
//		CommandName.put(WriteCheck485Line.class.getName(), "写入485线路反接检测开关");
//		CommandName.put(ReadCardDeadlineTipDay.class.getName(), "读取有效期即将过期提醒时间");
//		CommandName.put(WriteCardDeadlineTipDay.class.getName(), "写入有效期即将过期提醒时间");
//		CommandName.put(ReadTime.class.getName(), "从控制器中读取控制器时间");
//		CommandName.put(WriteTime.class.getName(), "将电脑的最新时间写入到控制器中");
//		CommandName.put(WriteTimeBroadcast.class.getName(), "校时广播");
//		CommandName.put(WriteTimeDefine.class.getName(), "写入自定义时间到控制器");
//		CommandName.put(WriteReaderOption.class.getName(), "写入控制器4个门的读卡器字节数");
//		CommandName.put(ReadReaderOption.class.getName(), "读取控制器4个门的读卡器字节数");
//		CommandName.put(ReadRelayOption.class.getName(), "读取控制器继电器参数");
//		CommandName.put(WriteRelayOption.class.getName(), "写入控制器继电器参数");
//		CommandName.put(OpenDoor.class.getName(), "远程开门指令");
//		CommandName.put(CloseDoor.class.getName(), "远程关门指令");
//		CommandName.put(HoldDoor.class.getName(), "远程保持门常开");
//		CommandName.put(LockDoor.class.getName(), "远程锁定门");
//		CommandName.put(UnlockDoor.class.getName(), "远程解除门锁定状态");
//		CommandName.put(ReadReaderWorkSetting.class.getName(), "读取门的读卡器验证方式参数");
//		CommandName.put(WriteReaderWorkSetting.class.getName(), "设置门的读卡器验证方式参数");
//		CommandName.put(ReadDoorWorkSetting.class.getName(), "读取门的工作模式");
//		CommandName.put(WriteDoorWorkSetting.class.getName(), "写入门的工作模式");
//		CommandName.put(ReadAutoLockedSetting.class.getName(), "读取定时锁定门");
//		CommandName.put(WriteAutoLockedSetting.class.getName(), "写入定时锁定门");
//		CommandName.put(ReadRelayReleaseTime.class.getName(), "读取开门保持时间");
//		CommandName.put(WriteRelayReleaseTime.class.getName(), "写入开门保持时间");
//		CommandName.put(ReadReaderInterval.class.getName(), "读取重复读卡间隔");
//		CommandName.put(WriteReaderInterval.class.getName(), "写入重复读卡间隔");
//		CommandName.put(ReadInvalidCardAlarmOption.class.getName(), "读取未注册卡读卡时报警功能");
//		CommandName.put(WriteInvalidCardAlarmOption.class.getName(), "写入未注册卡读卡时报警功能");
//		CommandName.put(ReadAlarmPassword.class.getName(), "读取胁迫报警功能");
//		CommandName.put(WriteAlarmPassword.class.getName(), "写入胁迫报警功能");
//		CommandName.put(ReadAntiPassback.class.getName(), "读取防潜返");
//		CommandName.put(WriteAntiPassback.class.getName(), "写入防潜返");
//		CommandName.put(WriteOvertimeAlarmSetting.class.getName(), "写入开门超时报警功能");
//		CommandName.put(ReadOvertimeAlarmSetting.class.getName(), "读取开门超时报警功能");
//		CommandName.put(WritePushButtonSetting.class.getName(), "写入出门按钮功能");
//		CommandName.put(ReadPushButtonSetting.class.getName(), "读取出门按钮功能");
//		CommandName.put(ReadSensorAlarmSetting.class.getName(), "读取门磁报警功能");
//		CommandName.put(WriteSensorAlarmSetting.class.getName(), "写入门磁报警功能");
//		CommandName.put(ReadAnyCardSetting.class.getName(), "读取全卡开门功能");
//		CommandName.put(WriteAnyCardSetting.class.getName(), "写入全卡开门功能");
//		CommandName.put(ReadReadingBroadcast.class.getName(), "");
//		CommandName.put(WriteReadingBroadcast.class.getName(), "");
//		CommandName.put(ReadCardDatabaseDetail.class.getName(), "读取控制器中的卡片数据库信息");
//		CommandName.put(ClearCardDataBase.class.getName(), "清空卡片数据库");
//		CommandName.put(ReadCardDataBase.class.getName(), "从控制器中读取卡片数据");
//		CommandName.put(ReadCardDetail.class.getName(), "读取单个卡片在控制器中的信息");
//		CommandName.put(WriteCardListBySequence.class.getName(), "将卡片列表写入到控制器非排序区");
//		CommandName.put(Net.PC15.FC89H.Command.Card.WriteCardListBySequence.class.getName(), "将卡片列表写入到控制器非排序区");
//		CommandName.put(WriteCardListBySort.class.getName(), "将卡片列表写入到控制器排序区");
//		CommandName.put(Net.PC15.FC89H.Command.Card.WriteCardListBySort.class.getName(), "将卡片列表写入到控制器排序区");
//		CommandName.put(DeleteCard.class.getName(), "删除卡片");
//		CommandName.put(Net.PC15.FC89H.Command.Card.DeleteCard.class.getName(), "删除卡片");
//		CommandName.put(ReadTransactionDatabaseDetail.class.getName(), "读取控制器中的卡片数据库信息");
//		CommandName.put(TransactionDatabaseEmpty.class.getName(), "清空所有类型的记录数据库");
//		CommandName.put(ClearTransactionDatabase.class.getName(), "清空指定类型的记录数据库");
//		CommandName.put(WriteTransactionDatabaseReadIndex.class.getName(), "修改指定记录数据库的读索引");
//		CommandName.put(WriteTransactionDatabaseWriteIndex.class.getName(), "修改指定记录数据库的写索引");
//		CommandName.put(ReadTransactionDatabaseByIndex.class.getName(), "读取记录");
//		CommandName.put(ReadTransactionDatabase.class.getName(), "读取新记录");
//		CommandName.put(Net.PC15.FC89H.Command.Transaction.ReadTransactionDatabaseByIndex.class.getName(), "读取记录");
//		CommandName.put(Net.PC15.FC89H.Command.Transaction.ReadTransactionDatabase.class.getName(), "读取新记录");
//	}
//	@Override
//	public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
//		try {
//			StringBuilder strBuf = new StringBuilder(100);
//			GetCommandDetail(strBuf, cmd);
//			String sKey = cmd.getClass().getName();
//			if (CommandResult.containsKey(sKey)) {
//				strBuf.append("命令处理完毕，返回结果数据：");
//				CommandResult.get(sKey).ResultToLog(strBuf, result);
//			}
//			else {
//				strBuf.append("命令处理完毕!");
//			}
//			AddLog(strBuf.toString());
//			strBuf = null;
//		}
//		catch (Exception e) {
//			// System.out.println("fcardiodemo.FormMain.CommandCompleteEvent() -- 发生错误：" + e.toString());
//			Logger.getRootLogger().error("fcardiodemo.FormMain.CommandCompleteEvent() -- 发生错误：" + e);
//		}
//	}
//	@Override
//	public void CommandProcessEvent(INCommand cmd) {
//		try {
//			// lblCommandName.setText("当前命令：" + GetCommandName(cmd) + "\n正在处理" );
//			StringBuilder strBuf = new StringBuilder(100);
//			strBuf.append("<html>");
//			strBuf.append("当前命令：");
//			strBuf.append(GetCommandName(cmd));
//			strBuf.append("<br/>正在处理： ");
//			strBuf.append(cmd.getProcessStep());
//			strBuf.append(" / ");
//			strBuf.append(cmd.getProcessMax());
//			strBuf.append("</html>");
//			lblCommandName.setText(strBuf.toString());
//			if (prCommand.getMaximum() != cmd.getProcessMax()) {
//				// prCommand.setValue(0);
//				prCommand.setMaximum(cmd.getProcessMax());
//			}
//			prCommand.setValue(cmd.getProcessStep());
//			// AddLog(strBuf.toString());
//			strBuf = null;
//		}
//		catch (Exception e) {
//			System.out.println("fcardiodemo.FormMain.CommandProcessEvent() -- 发生错误：" + e.toString());
//		}
//	}
//	@Override
//	public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
//		try {
//			StringBuilder strBuf = new StringBuilder(100);
//			GetCommandDetail(strBuf, cmd);
//			if (isStop) {
//				strBuf.append("命令已手动停止!");
//			}
//			else {
//				strBuf.append("网络连接失败!");
//			}
//			AddLog(strBuf.toString());
//			strBuf = null;
//		}
//		catch (Exception e) {
//			System.out.println("fcardiodemo.FormMain.ConnectorErrorEvent() --- " + e.toString());
//		}
//	}
//	@Override
//	public void ConnectorErrorEvent(ConnectorDetail detail) {
//		try {
//			StringBuilder strBuf = new StringBuilder(100);
//			strBuf.append("网络通道故障，IP信息：");
//			GetConnectorDetail(strBuf, detail);
//			AddLog(strBuf.toString());
//			strBuf = null;
//		}
//		catch (Exception e) {
//			System.out.println("fcardiodemo.FormMain.ConnectorErrorEvent() -- " + e.toString());
//		}
//	}
//	@Override
//	public void CommandTimeout(INCommand cmd) {
//		try {
//			if (cmd instanceof SearchEquptOnNetNum) {
//				CommandCompleteEvent(cmd, cmd.getCommandResult());
//				return;
//			}
//			StringBuilder strBuf = new StringBuilder(100);
//			GetCommandDetail(strBuf, cmd);
//			strBuf.append("命令超时，已失败！");
//			AddLog(strBuf.toString());
//			strBuf = null;
//		}
//		catch (Exception e) {
//			System.out.println("fcardiodemo.FormMain.CommandTimeout() -- " + e.toString());
//		}
//	}
//	@Override
//	public void PasswordErrorEvent(INCommand cmd) {
//		try {
//			StringBuilder strBuf = new StringBuilder(100);
//			GetCommandDetail(strBuf, cmd);
//			strBuf.append("通讯密码错误，已失败！");
//			AddLog(strBuf.toString());
//			strBuf = null;
//		}
//		catch (Exception e) {
//			System.out.println("fcardiodemo.FormMain.PasswordErrorEvent() -- " + e.toString());
//		}
//	}
//	@Override
//	public void ChecksumErrorEvent(INCommand cmd) {
//		try {
//			StringBuilder strBuf = new StringBuilder(100);
//			GetCommandDetail(strBuf, cmd);
//			strBuf.append("命令返回的校验和错误，已失败！");
//			AddLog(strBuf.toString());
//			strBuf = null;
//		}
//		catch (Exception e) {
//			System.out.println("fcardiodemo.FormMain.ChecksumErrorEvent() -- " + e.toString());
//		}
//	}
//	@Override
//	public void WatchEvent(ConnectorDetail detail, INData event) {
//		try {
//			StringBuilder strBuf = new StringBuilder(100);
//			strBuf.append("数据监控:");
//			GetConnectorDetail(strBuf, detail);
//			if (event instanceof FC8800WatchTransaction) {
//				FC8800WatchTransaction WatchTransaction = (FC8800WatchTransaction) event;
//				strBuf.append("，SN：");
//				strBuf.append(WatchTransaction.SN);
//				strBuf.append("\n");
//				PrintWatchEvent(WatchTransaction, strBuf);
//			}
//			else {
//				strBuf.append("，未知事件：");
//				strBuf.append(event.getClass().getName());
//			}
//			AddLog(strBuf.toString());
//			strBuf = null;
//		}
//		catch (Exception e) {
//			System.out.println("fcardiodemo.FormMain.WatchEvent() -- " + e.toString());
//		}
//	}
//	private String[] mWatchTypeNameList;
//	private String[] mCardTransactionList, mButtonTransactionList, mDoorSensorTransactionList, mSoftwareTransactionList, mAlarmTransactionList, mSystemTransactionList;
//	/**
//	 * 初始化监控消息处理的相关操作
//	 */
//	private void IniWatchEvent() {
//		mWatchTypeNameList = new String[]{"", "读卡信息", "出门开关信息", "门磁信息", "远程开门信息", "报警信息", "系统信息", "连接保活消息", "连接确认信息"};
//		mCardTransactionList = new String[256];
//		mButtonTransactionList = new String[256];
//		mDoorSensorTransactionList = new String[256];
//		mSoftwareTransactionList = new String[256];
//		mAlarmTransactionList = new String[256];
//		mSystemTransactionList = new String[256];
//		mCardTransactionList[1] = "合法开门";//
//		mCardTransactionList[2] = "密码开门";// ------------卡号为密码
//		mCardTransactionList[3] = "卡加密码";//
//		mCardTransactionList[4] = "手动输入卡加密码";//
//		mCardTransactionList[5] = "首卡开门";//
//		mCardTransactionList[6] = "门常开";// --- 常开工作方式中，刷卡进入常开状态
//		mCardTransactionList[7] = "多卡开门";// -- 多卡验证组合完毕后触发
//		mCardTransactionList[8] = "重复读卡";//
//		mCardTransactionList[9] = "有效期过期";//
//		mCardTransactionList[10] = "开门时段过期";//
//		mCardTransactionList[11] = "节假日无效";//
//		mCardTransactionList[12] = "未注册卡";//
//		mCardTransactionList[13] = "巡更卡";// -- 不开门
//		mCardTransactionList[14] = "探测锁定";//
//		mCardTransactionList[15] = "无有效次数";//
//		mCardTransactionList[16] = "防潜回";//
//		mCardTransactionList[17] = "密码错误";// ------------卡号为错误密码
//		mCardTransactionList[18] = "密码加卡模式密码错误";// ----卡号为卡号。
//		mCardTransactionList[19] = "锁定时(读卡)或(读卡加密码)开门";//
//		mCardTransactionList[20] = "锁定时(密码开门)";//
//		mCardTransactionList[21] = "首卡未开门";//
//		mCardTransactionList[22] = "挂失卡";//
//		mCardTransactionList[23] = "黑名单卡";//
//		mCardTransactionList[24] = "门内上限已满，禁止入门。";//
//		mCardTransactionList[25] = "开启防盗布防状态(设置卡)";//
//		mCardTransactionList[26] = "撤销防盗布防状态(设置卡)";//
//		mCardTransactionList[27] = "开启防盗布防状态(密码)";//
//		mCardTransactionList[28] = "撤销防盗布防状态(密码)";//
//		mCardTransactionList[29] = "互锁时(读卡)或(读卡加密码)开门";//
//		mCardTransactionList[30] = "互锁时(密码开门)";//
//		mCardTransactionList[31] = "全卡开门";//
//		mCardTransactionList[32] = "多卡开门--等待下张卡";//
//		mCardTransactionList[33] = "多卡开门--组合错误";//
//		mCardTransactionList[34] = "非首卡时段刷卡无效";//
//		mCardTransactionList[35] = "非首卡时段密码无效";//
//		mCardTransactionList[36] = "禁止刷卡开门";// -- 【开门认证方式】验证模式中禁用了刷卡开门时
//		mCardTransactionList[37] = "禁止密码开门";// -- 【开门认证方式】验证模式中禁用了密码开门时
//		mCardTransactionList[38] = "门内已刷卡，等待门外刷卡。";// （门内外刷卡验证）
//		mCardTransactionList[39] = "门外已刷卡，等待门内刷卡。";// （门内外刷卡验证）
//		mCardTransactionList[40] = "请刷管理卡";// (在开启管理卡功能后提示)(电梯板)
//		mCardTransactionList[41] = "请刷普通卡";// (在开启管理卡功能后提示)(电梯板)
//		mCardTransactionList[42] = "首卡未读卡时禁止密码开门。";//
//		mCardTransactionList[43] = "控制器已过期_刷卡";//
//		mCardTransactionList[44] = "控制器已过期_密码";//
//		mCardTransactionList[45] = "合法卡开门—有效期即将过期";//
//		mCardTransactionList[46] = "拒绝开门--区域反潜回失去主机连接。";//
//		mCardTransactionList[47] = "拒绝开门--区域互锁，失去主机连接";//
//		mCardTransactionList[48] = "区域防潜回--拒绝开门";//
//		mCardTransactionList[49] = "区域互锁--有门未关好，拒绝开门";//
//		mButtonTransactionList[1] = "按钮开门";//
//		mButtonTransactionList[2] = "开门时段过期";//
//		mButtonTransactionList[3] = "锁定时按钮";//
//		mButtonTransactionList[4] = "控制器已过期";//
//		mButtonTransactionList[5] = "互锁时按钮(不开门)";//
//		mDoorSensorTransactionList[1] = "开门";//
//		mDoorSensorTransactionList[2] = "关门";//
//		mDoorSensorTransactionList[3] = "进入门磁报警状态";//
//		mDoorSensorTransactionList[4] = "退出门磁报警状态";//
//		mDoorSensorTransactionList[5] = "门未关好";//
//		mSoftwareTransactionList[1] = "软件开门";//
//		mSoftwareTransactionList[2] = "软件关门";//
//		mSoftwareTransactionList[3] = "软件常开";//
//		mSoftwareTransactionList[4] = "控制器自动进入常开";//
//		mSoftwareTransactionList[5] = "控制器自动关闭门";//
//		mSoftwareTransactionList[6] = "长按出门按钮常开";//
//		mSoftwareTransactionList[7] = "长按出门按钮常闭";//
//		mSoftwareTransactionList[8] = "软件锁定";//
//		mSoftwareTransactionList[9] = "软件解除锁定";//
//		mSoftwareTransactionList[10] = "控制器定时锁定";// --到时间自动锁定
//		mSoftwareTransactionList[11] = "控制器定时解除锁定";// --到时间自动解除锁定
//		mSoftwareTransactionList[12] = "报警--锁定";//
//		mSoftwareTransactionList[13] = "报警--解除锁定";//
//		mSoftwareTransactionList[14] = "互锁时远程开门";//
//		mAlarmTransactionList[1] = "门磁报警";//
//		mAlarmTransactionList[2] = "匪警报警";//
//		mAlarmTransactionList[3] = "消防报警";//
//		mAlarmTransactionList[4] = "非法卡刷报警";//
//		mAlarmTransactionList[5] = "胁迫报警";//
//		mAlarmTransactionList[6] = "消防报警(命令通知)";//
//		mAlarmTransactionList[7] = "烟雾报警";//
//		mAlarmTransactionList[8] = "防盗报警";//
//		mAlarmTransactionList[9] = "黑名单报警";//
//		mAlarmTransactionList[10] = "开门超时报警";//
//		mAlarmTransactionList[0x11] = "门磁报警撤销";//
//		mAlarmTransactionList[0x12] = "匪警报警撤销";//
//		mAlarmTransactionList[0x13] = "消防报警撤销";//
//		mAlarmTransactionList[0x14] = "非法卡刷报警撤销";//
//		mAlarmTransactionList[0x15] = "胁迫报警撤销";//
//		mAlarmTransactionList[0x17] = "撤销烟雾报警";//
//		mAlarmTransactionList[0x18] = "关闭防盗报警";//
//		mAlarmTransactionList[0x19] = "关闭黑名单报警";//
//		mAlarmTransactionList[0x1A] = "关闭开门超时报警";//
//		mAlarmTransactionList[0x21] = "门磁报警撤销(命令通知)";//
//		mAlarmTransactionList[0x22] = "匪警报警撤销(命令通知)";//
//		mAlarmTransactionList[0x23] = "消防报警撤销(命令通知)";//
//		mAlarmTransactionList[0x24] = "非法卡刷报警撤销(命令通知)";//
//		mAlarmTransactionList[0x25] = "胁迫报警撤销(命令通知)";//
//		mAlarmTransactionList[0x27] = "撤销烟雾报警(命令通知)";//
//		mAlarmTransactionList[0x28] = "关闭防盗报警(软件关闭)";//
//		mAlarmTransactionList[0x29] = "关闭黑名单报警(软件关闭)";//
//		mAlarmTransactionList[0x2A] = "关闭开门超时报警";//
//		mSystemTransactionList[1] = "系统加电";//
//		mSystemTransactionList[2] = "系统错误复位（看门狗）";//
//		mSystemTransactionList[3] = "设备格式化记录";//
//		mSystemTransactionList[4] = "系统高温记录，温度大于>75";//
//		mSystemTransactionList[5] = "系统UPS供电记录";//
//		mSystemTransactionList[6] = "温度传感器损坏，温度大于>100";//
//		mSystemTransactionList[7] = "电压过低，小于<09V";//
//		mSystemTransactionList[8] = "电压过高，大于>14V";//
//		mSystemTransactionList[9] = "读卡器接反。";//
//		mSystemTransactionList[10] = "读卡器线路未接好。";//
//		mSystemTransactionList[11] = "无法识别的读卡器";//
//		mSystemTransactionList[12] = "电压恢复正常，小于14V，大于9V";//
//		mSystemTransactionList[13] = "网线已断开";//
//		mSystemTransactionList[14] = "网线已插入";//
//	}
//	/**
//	 * 记录事件的描述到StringBuilder中
//	 */
//	private void logTransaction(FC8800WatchTransaction WatchTransaction, StringBuilder strBuf, String[] sTransactionList) {
//		AbstractTransaction Transaction = WatchTransaction.EventData;
//		// 打印消息类型
//		if (WatchTransaction.CmdIndex >= 1 && WatchTransaction.CmdIndex <= 6) {
//			strBuf.append(mWatchTypeNameList[WatchTransaction.CmdIndex]);// 类型
//		}
//		else if (WatchTransaction.CmdIndex == 0x23 && WatchTransaction.CmdIndex == 0x22) {
//			int tmpType = WatchTransaction.CmdIndex - 27;
//			strBuf.append(mWatchTypeNameList[tmpType]);// 类型
//		}
//		else {
//			strBuf.append("未知消息类型：");// 类型
//			strBuf.append(WatchTransaction.CmdIndex);
//		}
//		strBuf.append("，消息时间：");
//		strBuf.append(TimeUtil.FormatTime(Transaction.TransactionDate()));
//		if (sTransactionList == null) {
//			return;
//		}
//		int code = Transaction.TransactionCode();
//		String sCode = null;
//		strBuf.append("\n消息代码：");// 类型
//		strBuf.append(code);
//		if (code < 255) {
//			sCode = sTransactionList[code];
//			if (StringUtil.IsNullOrEmpty(sCode)) {
//				sCode = "未知类型";
//			}
//		}
//		else {
//			sCode = "未知类型";
//		}
//		strBuf.append("--");
//		strBuf.append(sCode);
//		if (Transaction instanceof AbstractDoorTransaction) {
//			AbstractDoorTransaction dt = (AbstractDoorTransaction) Transaction;
//			if (dt.Door != 255) {
//				strBuf.append("，门号：");
//				strBuf.append(dt.Door);
//			}
//		}
//		if (Transaction instanceof CardTransaction) {
//			CardTransaction cardTrn = (CardTransaction) WatchTransaction.EventData;
//			strBuf.append("\n卡号：");
//			strBuf.append(cardTrn.CardData);
//			strBuf.append("，门号：");
//			strBuf.append(cardTrn.DoorNum());
//			if (cardTrn.IsEnter()) {
//				strBuf.append("，进门读卡");
//			}
//			else {
//				strBuf.append("，出门读卡");
//			}
//		}
//	}
//	/**
//	 * 打印监控消息
//	 * @param WatchTransaction 监控消息
//	 * @param strBuf 日志缓冲区
//	 */
//	private void PrintWatchEvent(FC8800WatchTransaction WatchTransaction, StringBuilder strBuf) {
//		switch (WatchTransaction.CmdIndex) {
//			case 1:// 读卡信息
//				logTransaction(WatchTransaction, strBuf, mCardTransactionList);
//				break;
//			case 2:// 出门开关信息
//				logTransaction(WatchTransaction, strBuf, mButtonTransactionList);
//				break;
//			case 3:// 门磁信息
//				logTransaction(WatchTransaction, strBuf, mDoorSensorTransactionList);
//				break;
//			case 4:// 远程开门信息
//				logTransaction(WatchTransaction, strBuf, mSoftwareTransactionList);
//				break;
//			case 5:// 报警信息
//				logTransaction(WatchTransaction, strBuf, mAlarmTransactionList);
//				break;
//			case 6:// 系统信息
//				logTransaction(WatchTransaction, strBuf, mSystemTransactionList);
//				break;
//			default:
//				logTransaction(WatchTransaction, strBuf, null);
//		}
//	}
//	private final StringBuilder strLog;
//	private void GetCommandDetail(StringBuilder strBuf, INCommand cmd) {
//		if (cmd == null) {
//			strBuf.append("命令类型：null");
//			return;
//		}
//		strBuf.append("命令类型：");
//		strBuf.append(GetCommandName(cmd));
//		if (cmd.getCommandParameter() != null) {
//			strBuf.append("，SN:");
//			CommandDetail det = cmd.getCommandParameter().getCommandDetail();
//			strBuf.append(det.Identity.GetIdentity());
//			strBuf.append(",");
//			GetConnectorDetail(strBuf, det.Connector);
//		}
//		strBuf.append("\n");
//	}
//	private String GetCommandName(INCommand cmd) {
//		if (cmd == null) {
//			return "null";
//		}
//		String sKey = cmd.getClass().getName();
//		if (CommandName.containsKey(sKey)) {
//			return CommandName.get(sKey);
//		}
//		else {
//			return sKey;
//		}
//	}
//	private void GetConnectorDetail(StringBuilder strBuf, ConnectorDetail conn) {
//		if (conn == null) {
//			strBuf.append("ConnectorDetail：null");
//			return;
//		}
//		if (conn instanceof TCPClientDetail) {
//			TCPClientDetail tcp = (TCPClientDetail) conn;
//			strBuf.append("TCP远程设备:");
//			strBuf.append(tcp.IP);
//			strBuf.append(":");
//			strBuf.append(tcp.Port);
//		}
//		else if (conn instanceof UDPDetail) {
//			UDPDetail udp = (UDPDetail) conn;
//			strBuf.append("UDP远程设备:");
//			strBuf.append(udp.IP);
//			strBuf.append(":");
//			strBuf.append(udp.Port);
//		}
//		// strBuf.append(",");
//	}
//	private void AddLog(String log) {
//		synchronized (strLog) {
//			// 设置日期格式
//			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//			strLog.append(df.format(new Date()));
//			strLog.append(":");
//			strLog.append(log);
//			// strLog.append("\n");
//			String txt = strLog.toString();
//			Logger.getRootLogger().info("fcardiodemo.FormMain.AddLog() + " + txt);
//			txtLog.setText(txt);
//			if (strLog.length() > 50000) {
//				strLog.delete(0, 30000);
//			}
//		}
//	}
//}
