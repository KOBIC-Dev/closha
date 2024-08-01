package org.kobic.bioexpress.rcp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.editors.text.TextEditor;
import org.kobic.bioexpress.gbox.local.FileService;
import org.kobic.bioexpress.gbox.local.FileServiceImpl;
import org.kobic.bioexpress.gbox.rapidant.RapidantService;
import org.kobic.bioexpress.gbox.rapidant.RapidantServiceImpl;
import org.kobic.bioexpress.model.common.KeywordModel;
import org.kobic.bioexpress.model.sso.Member;
import org.kobic.bioexpress.rcp.constant.Constants;
import org.kobic.bioexpress.rcp.utils.Utils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import raonwiz.k.livepass.client.KLivepassClient;

public class Activator implements BundleActivator {

	final static Logger logger = Logger.getLogger(Activator.class);

	private static BundleContext context;

	private static Activator plugin;

	private static Properties prop;

	private static Member member;

	private static boolean isAdmin;

	private static String service = Constants.CLOSHA;

	private static FileService fileService;
	private static RapidantService rapidantService;
	private static KLivepassClient klivepassClient;

	private static List<KeywordModel> keyword;
	private static List<Member> members;

	private static String pipelineTemplate;

	private static long sessionTime;

	private static MessageConsole messageConsole;

	private static boolean isEnableLimit = true;

	private static int UP_MAX_VALUE = Integer.parseInt(Constants.GBOX_MIN_SPEED_VALUE);
	private static int DOWN_MAX_VALUE = Integer.parseInt(Constants.GBOX_MIN_SPEED_VALUE);

	@SuppressWarnings("unused")
	private Map<String, TextEditor> editorMap;

	public static BundleContext getContext() {
		return context;
	}

	public static Properties getProperties(String properties) {
		try {
			prop.load(Activator.class.getResourceAsStream(properties));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop;
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static FileService getFileService() {
		return fileService;
	}

	public static RapidantService getRapidantService() {
		return rapidantService;
	}

	public static KLivepassClient getKlivepassClient() {
		return klivepassClient;
	}

	public static void setKlivepassClient(KLivepassClient klivepassClient) {
		Activator.klivepassClient = klivepassClient;
	}

	public static Member getMember() {
		return member;
	}

	public static void setMember(Member member) {
		Activator.member = member;
	}

	public static boolean isAdmin() {
		return isAdmin;
	}

	public static void setAdmin(boolean isAdmin) {
		Activator.isAdmin = isAdmin;
	}

	public static List<KeywordModel> getKeyword() {
		return keyword;
	}

	public static String getPipelineTemplate() {
		return pipelineTemplate;
	}

	public static List<Member> getMembers() {
		return members;
	}

	public static void setMembers(List<Member> members) {
		Activator.members = members;
	}

	public static String getService() {
		return service;
	}

	public static void setService(String service) {
		Activator.service = service;
	}

	public static long getSessionTime() {
		return sessionTime;
	}

	public static void setSessionTime(long sessionTime) {
		Activator.sessionTime = sessionTime;
	}

	public static MessageConsole getMessageConsole() {
		return messageConsole;
	}

	public static boolean isEnableLimit() {
		return isEnableLimit;
	}

	public static void setEnableLimit(boolean isEnableLimit) {
		Activator.isEnableLimit = isEnableLimit;
	}

	public static int getUP_MAX_VALUE() {
		return UP_MAX_VALUE;
	}

	public static void setUP_MAX_VALUE(int uP_MAX_VALUE) {
		UP_MAX_VALUE = uP_MAX_VALUE;
	}

	public static int getDOWN_MAX_VALUE() {
		return DOWN_MAX_VALUE;
	}

	public static void setDOWN_MAX_VALUE(int dOWN_MAX_VALUE) {
		DOWN_MAX_VALUE = dOWN_MAX_VALUE;
	}

	public void start(BundleContext bundleContext) throws Exception {

		Activator.context = bundleContext;

		plugin = this;

		prop = new Properties();

		fileService = new FileServiceImpl();

		logger.info("Attempting to connect to the RAPIDANT server.");

//		Display.getDefault().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//
//				rapidantService = RapidantServiceImpl.getInstance();
//
//				boolean res = rapidantService.connect();
//
//				logger.info("Result of connecting to RAPIDANT server: [" + res + "]");
//
//				if (res) {
//					klivepassClient = rapidantService.getRapidantClient();
//				}
//			}
//		});

		rapidantService = RapidantServiceImpl.getInstance();

		boolean res = rapidantService.connect();

		logger.info("Result of connecting to RAPIDANT server: [" + res + "]");

		if (res) {
			klivepassClient = rapidantService.getRapidantClient();
			sessionTime = System.currentTimeMillis();
		}

		keyword = new ArrayList<KeywordModel>();

		String temp[] = null;
		List<String> lines = null;

		Utils utils = new Utils();

		lines = utils.readLines("/org/kobic/bioexpress/rcp/data/keywords-all.tab");

		for (String line : lines) {

			temp = line.split("\t");

			if (temp.length == 4) {
				KeywordModel keywordModel = new KeywordModel();
				keywordModel.setId(temp[0]);
				keywordModel.setName(temp[1]);
				keywordModel.setDescription(temp[2]);
				keywordModel.setCategory(temp[3]);

				keyword.add(keywordModel);
			}
		}

		pipelineTemplate = utils.readFileToString("/org/kobic/bioexpress/rcp/data/pipeline_template.xml");

		if (messageConsole == null) {
			messageConsole = new MessageConsole("Console", null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { messageConsole });
		}

		logger.info("bioexpress.workbench.start");
	}

	public void stop(BundleContext bundleContext) throws Exception {

		Activator.context = null;
		plugin = null;
		rapidantService.disconnect();

		logger.info("bioexpress.workbench.stop");
	}

}