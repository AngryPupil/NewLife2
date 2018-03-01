package net.pupil;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class TestJavaFX {
	private static final int WIDTH = 1360;
	private static final int HEIGHT = 725;
	private static final String url = "221.130.6.197:8080/xxtsc/monitor/modules.jsp?projectId=32&projectName=%E6%B2%B3%E5%8C%97%E6%A0%A1%E8%AE%AF%E9%80%9A&enterTime=";
	private static String dateParam = "2016-12-05";
	private static String endDate = "2016-12-06";
	private static String imgStorePath = dateParam;
	private static String folderName = "";
	private static String headPath = "d:\\\\imgs\\";
	private static int mTabIndex = 1;
	private static final String tailUrl = "#tabs1";
	private static final String urlStart = "http://";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final TestCutImg cam = new TestCutImg(imgStorePath, "png");
		
		JFrame frame = new JFrame("JavaFX in Swing");
		final JFXPanel webBrowser = new JFXPanel();
		frame.setLayout(new BorderLayout());
		frame.add(webBrowser, BorderLayout.CENTER);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Group root = new Group();
				Scene scene = new Scene(root, WIDTH, HEIGHT);
				webBrowser.setScene(scene);
				Double widthDouble = new Integer(WIDTH).doubleValue();
				Double heightDouble = new Integer(HEIGHT).doubleValue();

				VBox box = new VBox(10);
				HBox urlBox = new HBox(10);
				final TextField urlTextField = new TextField();
				urlTextField.setText(url+dateParam+tailUrl);
				Button go = new Button("go");
				urlTextField.setPrefWidth(WIDTH - 70);
				urlBox.getChildren().addAll(urlTextField, go);
				urlBox.setVisible(false);

				final WebView view = new WebView();
				
				//设置Cookies
				URI uri = URI.create(urlStart + url + dateParam + tailUrl);
				Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
				headers.put("Set-Cookie", Arrays.asList("JSESSIONID=0ECCA078DD3B9279DF4A7A7BBE13FDDC"));
				try {
					java.net.CookieHandler.getDefault().put(uri, headers);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				view.setMinSize(widthDouble, heightDouble);
				view.setPrefSize(widthDouble, heightDouble);
				final WebEngine eng = view.getEngine();
				eng.load(urlStart + url + dateParam + tailUrl);
				root.getChildren().add(view);
				
				//模拟点击tab的按钮
				final Button btn = new Button("tab");
				btn.setVisible(false);
				final Runnable runnable = new Runnable() {
					boolean flag = false;
					public void run() {
						while (true) {
							
							try {
								Thread.sleep(500);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							
							System.out.println("flag--->"+flag);
							if (flag) {
								String item = "_"+getItemName();
								imgStorePath = dateParam + item;
								createFolder();
								cam.modifyFileStorePath(headPath+folderName+"\\"+imgStorePath);
								cam.snapShot();
								Platform.runLater(new Runnable() {
								    @Override
								    public void run() {
								        btn.fire();
								        flag = false;
								    }
								});
								break;
							}
							
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									try {
										Document doc = eng.getDocument();
										Element ele = doc.getElementById("Main");
										NodeList nl = ele.getChildNodes();
										Node div = nl.item(5);
										System.out.println("1 "+div);
										NodeList nlDiv = div.getChildNodes();
										Node div1 = nlDiv.item(1);
										System.out.println("2 "+div);
										NodeList nlDiv1 = div1.getChildNodes();
										Node ul = nlDiv1.item(0);
										Node li = ul.getChildNodes().item(1);
										System.out.println("li "+li);
										NodeList nl2 = li.getChildNodes();
										Node aNode = nl2.item(0);
										System.out.println(aNode);
										System.out.println("加载完成！");
										flag = true;
									} catch (NullPointerException e) {
										System.out.println("未加载完成");
									}
								}
							});
							
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					private void createFolder() {
						SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
						SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
						try {
							folderName = format.format(format1.parse(dateParam));
							String path = headPath+folderName+"\\";
							File file = new File(path);
							if (!file.exists()) {
								System.out.println("创建文件夹--->"+path);
								file.mkdirs();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					private String getItemName() {
						switch (mTabIndex) {
						case 1:
							return "1_业务";
						case 2:
							return "2_数据库";
						case 3:
							return "3_主机";
						case 4:
							return "4_网络";
						case 5:
							return "5_软件";

						default:
							break;
						}
						return "";
					}
				};
				btn.setOnAction(new EventHandler<ActionEvent>() {
					
					//tab index
					int tabIdx = 1;
					
					@Override
					public void handle(ActionEvent event) {
						if (tabIdx > 4) {
							System.out.println("tab大于4了");
							
							//进入下一个日期
							calDate();
							if (dateParam.equals(endDate)) {
								System.out.println("全部截图已完成！！！！！");
								urlTextField.setText("全部截图已完成！！！！！");
								return;
							}
							eng.load(urlStart + url + dateParam + tailUrl);
							mTabIndex = 1;
							tabIdx = 1;
							
							return;
						}
						System.out.println("tab--->"+tabIdx);
						mTabIndex++;
						//防止文字重叠
						eng.executeScript("$('#modules').find('a')[" + 3 + "].click();");
						
						eng.executeScript("$('#modules').find('a')[" + tabIdx++ + "].click();");
						eng.executeScript("$(window).scrollTop(0);");
						
						Thread thread = new Thread(runnable);
						thread.start();
					}

				});

//				box.getChildren().add(btn);
//				box.getChildren().add(urlBox);
				box.getChildren().add(view);
				root.getChildren().add(box);

				go.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (!urlTextField.getText().startsWith(urlStart)) {
							eng.load(urlStart + urlTextField.getText());
						} else {
							eng.load(urlTextField.getText());
						}
					}
				});
				
				eng.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> arg0, State oldState, State newState) {
						if (newState == State.SUCCEEDED) {
							
							System.out.println("页面执行完毕，开始执行一系列操作！！");
							//文档加载完毕后执行截图操作
							Thread thread = new Thread(runnable);
							thread.start();
							
						}
					}
				});
			}
		});

		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocation(6, 0);
		frame.setVisible(true);
	}
	
	private static void calDate() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(dateParam);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime();
			dateParam = format.format(date);
			System.out.println("下一个日期--->" + dateParam);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}